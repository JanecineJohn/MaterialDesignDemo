package com.example.xin.materialdesigndemo;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout search;
    private List<Pictures> picturesList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefresh;//定义SwipeRefreshLayout组件，用于下拉刷新
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initPictures();//初始化图片
        init();//初始化组件
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PicturesAdapter(picturesList));
    }

    private void init(){
        //点击搜索
        search = (LinearLayout) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"假装进入了搜索页面",Toast.LENGTH_SHORT).show();
            }
        });

        //下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this,"假装刷新了一下界面",
                        Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
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
    private void initPictures(){
        Pictures a = new Pictures(R.drawable.a,"图片a");
        picturesList.add(a);
        Pictures b = new Pictures(R.drawable.b,"图片b");
        picturesList.add(b);
        Pictures c = new Pictures(R.drawable.c,"图片c");
        picturesList.add(c);
        Pictures d = new Pictures(R.drawable.d,"图片d");
        picturesList.add(d);
        Pictures e = new Pictures(R.drawable.e,"图片e");
        picturesList.add(e);
        Pictures f = new Pictures(R.drawable.f,"图片f");
        picturesList.add(f);
        Pictures g = new Pictures(R.drawable.g,"图片g");
        picturesList.add(g);
        Pictures h = new Pictures(R.drawable.h,"图片h");
        picturesList.add(h);
        Pictures i = new Pictures(R.drawable.i,"图片i");
        picturesList.add(i);
        Pictures j = new Pictures(R.drawable.j,"图片j");
        picturesList.add(j);
        Pictures k = new Pictures(R.drawable.k,"图片k");
        picturesList.add(k);
        Pictures l = new Pictures(R.drawable.l,"图片l");
        picturesList.add(l);
        Pictures m = new Pictures(R.drawable.m,"图片m");
        picturesList.add(m);
        Pictures n = new Pictures(R.drawable.n,"图片n");
        picturesList.add(n);
    }
}
