package com.example.lenovo.battery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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

import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.Station;
import com.battery.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/2/9.
 * 主页中的第一个分页面，附近电站页面
 */

public class FirstFragment extends Fragment{

    /*private static final String TAG=FirstFragment.class.getSimpleName();*/
    RecyclerView recyclerView;
    private User user;
    private SwipeRefreshLayout refreshLayout;
    private List<Station> stationList;
    private Handler handler;
    private boolean first;
    private View view;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first,container,false);

        recyclerView = view.findViewById(R.id.baseViewPage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        first = true;
        handler = new Handler();
        // 首先刷新数据
        refresh();

        refreshLayout = view.findViewById(R.id.first_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        // 刷新布局的事件
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        return view;
    }

    /**
     * 根据用户的id，和数据库中的用户对应的参考车辆id，请求附近电站数据列表，并更新UI
     */
    private void refresh() {
        RequestBody body = new FormBody.Builder()
                .add("user_id", Integer.toString(user.getId())).build();
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
                        handler.post(runStation);
                    }
                }.start();

            }
        });
    }

    Runnable runStation = new Runnable() {
        @Override
        public void run() {
            StationAdapterPro adapterPro = new StationAdapterPro(stationList);
            adapterPro.setUserId(user.getId());
            recyclerView.setAdapter(adapterPro);

            if (!first) {
                refreshLayout.setRefreshing(false);
                Snackbar.make(view, "刷新完成", Snackbar.LENGTH_SHORT).show();
            }

            first = false;
        }
    };

    public void setUser(User user) {
        this.user = user;
    }
}
