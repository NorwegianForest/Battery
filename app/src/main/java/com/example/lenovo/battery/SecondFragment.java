package com.example.lenovo.battery;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/2/9.
 * 第二分页面，推荐电站
 * 推荐排队时间最短和距离最近的两个电站
 */

public class SecondFragment extends Fragment {

    private static final String TAG = FirstFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private int userId;
    private List<com.battery.Station> stationList;
    private SwipeRefreshLayout refreshLayout;
    private Handler handler;
    private View view;
    private boolean first;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_first,container,false);

        recyclerView = view.findViewById(R.id.baseViewPage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        first = true;
        handler = new Handler();
        loadRecommend();

        refreshLayout = view.findViewById(R.id.first_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        // 刷新后重新请求并加载数据
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecommend();
            }
        });

        return view;
    }

    /**
     * 获取推荐电站数据，并更新UI
     */
    private void loadRecommend() {
        RequestBody body = new FormBody.Builder()
                .add("user_id", Integer.toString(userId)).build();
        HttpUtil.sendRequest(Constants.RECOMMENDADDRESS, body, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                stationList = new Gson().fromJson(responseData,
                        new TypeToken<List<com.battery.Station>>(){}.getType());
                new Thread() {
                    @Override
                    public void run() {
                        handler.post(runRecommend);
                    }
                }.start();
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * UI更新推荐电站数据的线程
     */
    Runnable runRecommend = new Runnable() {
        @Override
        public void run() {
            StationAdapterPro adapter = new StationAdapterPro(stationList);
            adapter.setUserId(userId);
            recyclerView.setAdapter(adapter);

            if (!first) {
                refreshLayout.setRefreshing(false);
                Snackbar.make(view, "刷新完成", Snackbar.LENGTH_SHORT).show();
            }

            first = false;
        }
    };

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
