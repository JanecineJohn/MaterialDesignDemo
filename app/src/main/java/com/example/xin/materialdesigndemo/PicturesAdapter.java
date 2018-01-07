package com.example.xin.materialdesigndemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.xin.materialdesigndemo.utils.ScreenUtils;

import java.util.List;

/**
 * Created by xin on 2017/10/11.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.ViewHolder>{
    private Context mContext;
    private List<String> mPicturesList;
    public String imageUrl;

    private int normalType = 0;     // 第一种ViewType，正常的item
    private int footType = 1;       // 第二种ViewType，底部的提示View

    

    public PicturesAdapter(List<String> picturesList){
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
            }
        });
        return holder;
    }

    /**Glide嵌套能使图片不会出现左右移动，可是加载很慢：
     * final ByteArrayOutputStream baos = new ByteArrayOutputStream();
     * bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
     * byte[] bytes = baos.toByteArray();
     * //上面是将bitmap对象转成字节流，以便glide加载
     * //下面按照图片原本的高宽比进行加载
     * float itemWidth = (ScreenUtils.getScreenWidth(holder.itemView.getContext()) - 8*3) / 2;
     *      int showWidth = (int) itemWidth;
     *      float scale = (itemWidth+0f)/width;
     *     int showHeight = (int) (height*scale);
     *     Glide.with(mContext).load(bytes).fitCenter()
     *     .placeholder(R.mipmap.ic_launcher)
     *     .error(R.mipmap.ic_launcher_round)
     *     .override(showWidth,showHeight)
     *     .into(holder.pictureImage);
     * */
    @Override
    public void onBindViewHolder(final PicturesAdapter.ViewHolder holder, int position) {
        imageUrl = mPicturesList.get(position);
        final float itemWidth = (ScreenUtils.getScreenWidth(holder.itemView.getContext()) - 8*3) / 2;
        Glide.with(mContext)
                .load(imageUrl)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();

                        LinearLayout.LayoutParams layoutParams =
                                (LinearLayout.LayoutParams) holder.pictureImage.getLayoutParams();
                        layoutParams.width = (int) itemWidth;
                        float scale = (itemWidth+0f)/width;
                        layoutParams.height = (int) (height*scale);

                        holder.pictureImage.setLayoutParams(layoutParams);
                        holder.pictureImage.setImageBitmap(bitmap);
                    }
                });
        //用Glide将URL对应的网络图片加载到屏幕中，可是出现闪烁、左右偏移、图片越来越小等问题
//        Glide.with(mContext)
//                .load(imageUrl)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
//                .placeholder(R.mipmap.ic_launcher)
//                .error(R.mipmap.ic_launcher_round)
//                .into(holder.pictureImage);

        /**计算item宽度，计算图片等比例缩放后的高度，为imageView设置参数*/
//        LinearLayout.LayoutParams layoutParams =
//                (LinearLayout.LayoutParams) holder.pictureImage.getLayoutParams();
//        float itemWidth = (ScreenUtils.getScreenWidth(holder.itemView.getContext()) - 8*3) / 2;
//        layoutParams.width = (int) itemWidth;
//        float scale = (itemWidth+0f)/book.width;
//        layoutParams.height = (int) (book.height*scale);
//        holder.pictureImage.setLayoutParams(layoutParams);
//        Glide.with(mContext).load(book.image).override(layoutParams.width,layoutParams.height)
//                .into(holder.pictureImage);
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
