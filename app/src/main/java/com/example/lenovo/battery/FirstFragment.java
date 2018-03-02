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
 * 附近电站页面
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

        refresh();

        refreshLayout = view.findViewById(R.id.first_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
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

    private void refresh() {
        CountDownLatch l2 = new CountDownLatch(1);
        try {
            user.loadVehicle(l2);
            l2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
