package com.example.lenovo.battery;

/**
 * Created by lenovo on 2018/2/27.
 */

public class CollectedStation {
    private String name;   //电站名字
    private String address;//电站地址
    private int imageId;   //电站图片

    public CollectedStation(String name,int imageId,String address){
        this.name = name;
        this.imageId = imageId;
        this.address = address;
    }
    public String getName(){
        return name;
    }
    public int getImageId(){
        return imageId;
    }
    public String getAddress(){
        return address;
    }
}
