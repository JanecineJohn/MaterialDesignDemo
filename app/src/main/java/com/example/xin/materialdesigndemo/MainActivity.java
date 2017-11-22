package com.example.xin.materialdesigndemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText search;
    RecyclerView recyclerView;
    Socket socket;
    DataOutputStream out;
    DataInputStream in;
    Handler handler;
    String type="彭于晏";
    int page=1;/**用于后续下拉加载更多，自增达到加载下一页图片的效果*/
    private List<String> picturesList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;//定义SwipeRefreshLayout组件，用于下拉刷新
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();//初始化组件
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i("在handler中picturesList的大小",picturesList.size()+"");
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new PicturesAdapter(picturesList));
            }
        };


        initPictures(type,page);//初始化图片
        //recyclerView.setAdapter(new PicturesAdapter(picturesList));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init(){
        //点击搜索
        search = (EditText) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //写搜索逻辑
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        StaggeredGridLayoutManager layoutManager =
//                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        initPictures(type,page);//重新初始化图片
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    //初始化图片
    private void initPictures(final String mtype, final int mpage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    picturesList.clear();//先清空集合
                    socket = new Socket("192.168.56.1",10100);
                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());
                    String url;
                    out.writeUTF(mtype);
                    out.writeUTF(mpage+"");
                    int size = Integer.parseInt(in.readUTF());//拿到图片张数
                    for (int i=0;i<size;i++){
                        url = in.readUTF();
                        picturesList.add(url);
                    }
                    //Log.i("pictureList的大小------------------",picturesList.size()+"");
                    page++;
                    //Log.i("页数：",page+"");
                    handler.sendMessage(handler.obtainMessage());
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.print("初始化连接失败，输入输出流异常");
                }finally {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
