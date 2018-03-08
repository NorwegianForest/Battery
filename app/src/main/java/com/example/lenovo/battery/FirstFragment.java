package com.example.lenovo.battery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.battery.User;

import java.util.concurrent.CountDownLatch;

/**
 * Created by lenovo on 2018/2/9.
 * 主页中的第一个分页面，附近电站页面
 */

public class FirstFragment extends Fragment{

    /*private static final String TAG=FirstFragment.class.getSimpleName();*/
    RecyclerView recyclerView;
    private User user;
    private SwipeRefreshLayout refreshLayout;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_first,container,false);

        recyclerView = view.findViewById(R.id.baseViewPage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // 首先刷新数据
        refresh();

        refreshLayout = view.findViewById(R.id.first_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        // 刷新布局的事件
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                refreshLayout.setRefreshing(false);
                Snackbar.make(view, "刷新完成", Snackbar.LENGTH_SHORT)
                        .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });

        return view;
    }

    /**
     * 根据用户的id，和数据库中的用户对应的参考车辆id，请求附近电站数据列表，并更新UI
     */
    private void refresh() {
        // 线程控制，必须等待User对象加载车辆数据完成后，根据车辆的经纬度来获取附近电站数据
        // 这里只等待1条线程结束，故创建CountDownLatch对象参数为1
        CountDownLatch l2 = new CountDownLatch(1);
        try {
            user.loadVehicle(l2);
            l2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 等待获取附近电站数据的线程完成，才能更新UI
        CountDownLatch l3 = new CountDownLatch(1);
        com.battery.Station myStation = new com.battery.Station();
        try {
            myStation.loadAround(user.getId(), user.getVehicleList().get(0).getId(), l3);
            l3.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StationAdapterPro adapterPro = new StationAdapterPro(myStation.getStationList());
        adapterPro.setUserId(user.getId());
        recyclerView.setAdapter(adapterPro);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
