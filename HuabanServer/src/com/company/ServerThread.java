package com.company;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThread extends Thread{
    ServerSocket serverSocket;
    InetAddress myIPaddress = null;
    public static final int PORT = 10100;//指定监听10100端口

    public ServerThread(){
        //构造函数
        try {
            //创建一个服务器Socket，监听10100端
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //获取本地IP地址
        try {
            myIPaddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println("服务器地址：" + myIPaddress.getHostAddress());

    }

    @Override
    public void run() {
        while (true){
            try {
                //用accept方法阻塞式等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println("与" + socket.getInetAddress().getHostAddress() + "建立连接");

                ClientThread clientThread = new ClientThread(socket,this);
                clientThread.start();
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("接受客户端连接失败");
            }
        }
    }

    //服务器提供查询方法
    public List<String> SearchImage(String type,int page){
        List<String> imgUrl = new ArrayList<>();//将图片url放到集合中返回
        String finalURL = "";//将用来暂时存放url链接文字
        Document document;
        try {
            String url ="http://image.baidu.com/search/avatarjson?tn=resultjsonavatarnew&ie=utf-8&word="+type+"&cg=star&pn="+page*30+"&rn=30&itg=0&z=0&fr=&width=&height=&lm=-1&ic=0&s=0&st=-1&gsm="+Integer.toHexString(page*30);
            document = Jsoup.connect(url).data("query","Java")
                    .userAgent("Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)")
                    .timeout(5000).get();
            String xmlSource = document.toString();

            System.out.println(xmlSource);/**打印出请求得到的数据*/

            xmlSource = StringEscapeUtils.unescapeHtml3(xmlSource);
            String reg = "objURL\":\"http://.+?\\.(jpg|jpeg)";
            Pattern pattern = Pattern.compile(reg);
            Matcher m = pattern.matcher(xmlSource);
            while (m.find()){
                finalURL = m.group().substring(9);//图片的url
                imgUrl.add(finalURL);
            }
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("请求百度图片失败");
        }
        return imgUrl;
    }


    //释放资源，关闭连接
    public void finalize(){
        try {
            serverSocket.close();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("关闭服务端失败");
        }
        serverSocket = null;
    }
}
