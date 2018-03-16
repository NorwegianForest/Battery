package com.example.lenovo.battery;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.Station;
import com.battery.Vehicle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapActivity extends AppCompatActivity implements BaiduMap.OnMapClickListener {

    public LocationClient mLocationClient;
    private TextureMapView mMapView;
    private BaiduMap mBaiduMap;
    private List<Marker> markerList;
    private InfoWindow mInfoWindow;

    private boolean isFirstLocate = true;

    private List<Station> stationList;
    private String userId;
    private Vehicle vehicle;
    private Handler handler;

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        // 设置统一的状态栏颜色
        MainActivity.setStatusBarColor(this);

        Toolbar toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("电站地图");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//启用回退功能
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        handler = new Handler();
        userId = getIntent().getStringExtra("id");

        mMapView = findViewById(R.id.mTexturemap);
        mBaiduMap = mMapView.getMap();

        // 设置地图单击事件监听者
        mBaiduMap.setOnMapClickListener(this);

        mBaiduMap.setMyLocationEnabled(true);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void navigateToUser(BDLocation location) {
        if (isFirstLocate) {
            Toast.makeText(this, "nav to " + location.getAddrStr(), Toast.LENGTH_SHORT).show();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);

            // 移动到自己的位置
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            isFirstLocate = false;

            // 标记所有电站点
            loadStations(getIntent().getStringExtra("id"));
        }

        MyLocationData.Builder locationBuilder = new MyLocationData.
                Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    private void requestVehicleLocation() {
        RequestBody body = new FormBody.Builder().add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.REFERENCEADDRESS, body, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (responseData.equals("无结果")) {

                } else {
                    vehicle = new Gson().fromJson(responseData, Vehicle.class);
                    navigateToVehicle();
                }
            }
        });
    }

    private void navigateToVehicle() {
         if (isFirstLocate) {
            LatLng ll = new LatLng(vehicle.getLatitude(), vehicle.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);

            // 移动到自己的位置
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll);
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            isFirstLocate = false;

            // 标记所有电站点
            loadStations(getIntent().getStringExtra("id"));

            setMarkerListener();
        }

        MyLocationData.Builder locationBuilder = new MyLocationData.
                Builder();
        locationBuilder.latitude(vehicle.getLatitude());
        locationBuilder.longitude(vehicle.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    private void setMarkerListener(){
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Button button = new Button(getApplicationContext());
                button.setText("导航");
                for (Marker m : markerList) {
                    if (marker == m) {
                        index = markerList.indexOf(m);
                        button.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent intent = new Intent(MapActivity.this, StationActivity.class);
                                intent.putExtra("id", userId);
                                intent.putExtra("station_id", Integer.toString(stationList.get(index).getId()));
                                startActivity(intent);
                            }
                        });
                        LatLng ll = new LatLng(stationList.get(index).getLatitude(), stationList.get(index).getLongitude());
                        mInfoWindow = new InfoWindow(button, ll, -47);
                        mBaiduMap.showInfoWindow(mInfoWindow);
                    }
                }
                return false;
            }
        });
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
//                navigateToUser(location);
                requestVehicleLocation();
            }
        }
    }

    private void loadStations(String userId) {
        RequestBody body = new FormBody.Builder()
                .add("user_id", userId).build();
        HttpUtil.sendRequest(Constants.STATIONADDRESS, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                stationList = new Gson().fromJson(responseData,
                        new TypeToken<List<Station>>(){}.getType());
                new Thread() {
                    @Override
                    public void run() {
                        handler.post(runPutMarker);
                    }
                }.start();
            }
        });
    }

    Runnable runPutMarker = new Runnable() {
        @Override
        public void run() {
            markerList = new ArrayList<>();
            for (Station station : stationList) {
                // 设置坐标点
                LatLng point = new LatLng(station.getLatitude(), station.getLongitude());
                View view = getLayoutInflater().inflate(R.layout.marker_text, null);
                TextView textView = view.findViewById(R.id.station_name);
                textView.setText(station.getName());
                // 创建OverlayOptions属性
                OverlayOptions option =  new MarkerOptions()
                        .position(point)
                        .icon(BitmapDescriptorFactory.fromView(view));
                markerList.add( (Marker) mBaiduMap.addOverlay(option));
            }
        }
    };
}
