package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientThread extends Thread{
    Socket clientSocket;
    ServerThread serverThread;

    DataInputStream inputStream = null;
    DataOutputStream outputStream = null;

    List<String> imgUrl = new ArrayList<>();


    public ClientThread(Socket socket,ServerThread serverThread){
        clientSocket = socket;
        this.serverThread = serverThread;
        //初始化输入输出流
        try {
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("输入输出流初始化异常");
        }
    }

    @Override
    public void run() {
            try {
                String type = inputStream.readUTF();
                int page = Integer.parseInt(inputStream.readUTF().trim());
                String url;
                imgUrl = serverThread.SearchImage(type,page);

                //先把图片数通知客户端
                outputStream.writeUTF(imgUrl.size() + "");

                for (int i=0;i<imgUrl.size();i++){
                    url = imgUrl.get(i);
                    outputStream.writeUTF(url);//写入字符串
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    outputStream.close();
                    inputStream.close();
                    clientSocket.close();
                    //System.out.println("关闭成功");
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
    }
}
