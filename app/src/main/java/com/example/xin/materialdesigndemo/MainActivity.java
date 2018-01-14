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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Socket socket;
    DataOutputStream out;
    DataInputStream in;
    Handler handler;
    String type="高清壁纸1920x1080";
    int page=1;/**用于后续下拉加载更多，自增达到加载下一页图片的效果*/
    private List<String> picturesList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;//定义SwipeRefreshLayout组件，用于下拉刷新
    PicturesAdapter adapter = new PicturesAdapter(picturesList);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();//初始化组件
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.i("在handler中picturesList的大小",picturesList.size()+"");
//                StaggeredGridLayoutManager layoutManager =
//                        new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
//                //layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(new PicturesAdapter(picturesList));
                adapter.upDateList(picturesList);
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
        final EditText search = (EditText) findViewById(R.id.search);
        Button searchBt = (Button) findViewById(R.id.searchBt);
        searchBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //只要点击搜索，先把页数清零
                page = 1;
                type = search.getText().toString();
                initPictures(type,page);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                            >= recyclerView.computeVerticalScrollRange()){
                        Toast.makeText(MainActivity.this,"正在加载...",Toast.LENGTH_SHORT).show();
                        initPictures(type,page);
                    }
                }
            }
        });
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
                        type = "高清壁纸";
                        page = 1;
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
                    socket = new Socket("10.242.10.30",10100);
                    //socket = new Socket("192.168.1.102",10100);
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
                    //page++;
                    //Log.i("页数：",page+"");
                    handler.sendMessage(handler.obtainMessage());
                    //adapter.upDateList(picturesList);
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
        page++;
    }
}
