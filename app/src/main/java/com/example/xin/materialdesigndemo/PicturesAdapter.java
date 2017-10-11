package com.example.xin.materialdesigndemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by xin on 2017/10/11.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder>{
    private Context mContext;
    private List<Pictures> mPicturesList;

    public PicturesAdapter(List<Pictures> picturesList){
        mPicturesList = picturesList;
    }
    @Override
    public PicturesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.pictures_item,
                parent,false);
        final ViewHolder holder = new ViewHolder(view);//将自定义了布局的View作为参数，创建一个ViewHolder
        //为viewHolder里的图片(ImageView)设置点击事件
        holder.pictureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取点击的图片位置
                int position = holder.getAdapterPosition();
                Pictures pictures = mPicturesList.get(position);
                Intent intent = new Intent(mContext,picturesDetail.class);
                intent.putExtra(picturesDetail.PICTURES_TEXT,pictures.getText());
                intent.putExtra(picturesDetail.PICTURES_ID,pictures.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(PicturesAdapter.ViewHolder holder, int position) {
        Pictures pictures = mPicturesList.get(position);
        holder.pictureText.setText(pictures.getText());
        //用Glide在指定组件加载指定ID的图片
        Glide.with(mContext).load(pictures.getImageId()).into(holder.pictureImage);
    }

    @Override
    public int getItemCount() {
        return mPicturesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView pictureImage;
        TextView pictureText;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            pictureImage = itemView.findViewById(R.id.pictures_image);
            pictureText = itemView.findViewById(R.id.pictures_text);
        }
    }
}
