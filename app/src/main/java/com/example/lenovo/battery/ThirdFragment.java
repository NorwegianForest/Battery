package com.example.lenovo.battery;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;

/**
 * Created by lenovo on 2018/2/9.
 * 第三个分页面为电站地图，功能有待进一步开发
 */

public class ThirdFragment extends Fragment {

    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        //获取地图控件引用
        mMapView = view.findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        // 开启定位图层
//        mBaiduMap.setMyLocationEnabled(true);

//        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;   //默认为 LocationMode.NORMAL 普通态

// 构造定位数据
//        MyLocationData locData = new MyLocationData.Builder()
//                .accuracy(10)
                // 此处设置开发者获取到的方向信息，顺时针0-360
//                .direction(100).latitude(30.5)
//                .longitude(103.5).build();

// 设置定位数据
//        mBaiduMap.setMyLocationData(locData);

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, BitmapDescriptorFactory.fromResource(R.drawable.profile));
//        mBaiduMap.setMyLocationConfiguration(config);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
}
