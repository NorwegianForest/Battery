package com.example.lenovo.battery;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ThemedSpinnerAdapter;

import com.battery.Constants;
import com.battery.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/2/9.
 */

public class SecondFragment extends Fragment {

    private static final String TAG = FirstFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private int userId;
    private List<com.battery.Station> stationList;
    private SwipeRefreshLayout refreshLayout;
    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_first,container,false);

        recyclerView = view.findViewById(R.id.baseViewPage);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        handler = new Handler();
        loadRecommend();

        refreshLayout = view.findViewById(R.id.first_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecommend();
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

    Runnable runRecommend = new Runnable() {
        @Override
        public void run() {
            StationAdapterPro adapter = new StationAdapterPro(stationList);
            adapter.setUserId(userId);
            recyclerView.setAdapter(adapter);
        }
    };

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
