package com.example.lenovo.battery;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.battery.*;

import java.util.List;

/**
 * Created by szl on 2018/2/28.
 * 电站数据列表配适器，参照Android教程中的例程
 * 命名中的Pro是区别于之前的StationAdapter类，进行改进
 */

public class StationAdapterPro extends RecyclerView.Adapter<StationAdapterPro.ViewHolder> {

    private int userId;
    private List<com.battery.Station> stationList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View stationView;
        ImageView stationImage;
        TextView stationName;
        TextView time;
        TextView distance;

        ViewHolder(View view) {
            super(view);
            stationView = view;
            stationImage = view.findViewById(R.id.station_item_image);
            stationName = view.findViewById(R.id.station_item_name);
            time = view.findViewById(R.id.station_item_time);
            distance = view.findViewById(R.id.station_item_distance);
        }
    }

    public StationAdapterPro(List<com.battery.Station> stationList) {
        this.stationList = stationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        com.battery.Station station = stationList.get(position);
        holder.stationImage.setImageResource(R.drawable.charging);
        holder.stationName.setText(station.getName());
        String timeStr = "排队时长:" + Integer.toString(station.getQueueTime()) + "min";
        holder.time.setText(timeStr);
        String distanceStr = "距离您的位置:" + station.getDistance() + "km";
        holder.distance.setText(distanceStr);

        holder.stationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.battery.Station station = stationList.get(position);
                Intent intent = new Intent(v.getContext(),StationActivity.class);
                intent.putExtra("id", Integer.toString(userId));
                intent.putExtra("station_id", Integer.toString(station.getId()));
                intent.putExtra("name", station.getName());
                intent.putExtra("address", station.getAddress());
                intent.putExtra("queueTime", Integer.toString(station.getQueueTime()));
                intent.putExtra("distance", Double.toString(station.getDistance()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stationList == null ? 0 : stationList.size();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
