package com.example.xin.materialdesigndemo;

/**
 * Created by xin on 2017/10/11.
 */

public class Pictures {
    private String text;
    private int imageId;

    public Pictures(int imageId,String text){
        this.imageId = imageId;
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public int getImageId(){
        return imageId;
    }
}
