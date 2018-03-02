package com.example.lenovo.battery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lenovo on 2018/2/16.
 */

public class CollectedStationAdapter extends RecyclerView.Adapter<CollectedStationAdapter.ViewHolder> {

    private List<CollectedStation> mList;

    public CollectedStationAdapter(List<CollectedStation> collectedStationList) {
        mList = collectedStationList;
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{
        View stationView;
        ImageView stationImage;
        TextView stationName;
        TextView stationAddr;

        public ViewHolder (View view){
            super(view);
            stationView = view;
            stationImage = (ImageView) view.findViewById(R.id.station_image);
            stationName = (TextView) view.findViewById(R.id.station_name);
            stationAddr = (TextView) view.findViewById(R.id.station_address);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collectedstation,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.stationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                CollectedStation station = mList.get(position);
                Intent intent = new Intent(view.getContext(), StationActivity.class);
                view.getContext().startActivity(intent);
            }
        });//收藏电站item的点击事件

        holder.stationView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final int position = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                AlertDialog show = builder.setMessage("确定删除该收藏？")
                        .setPositiveButton("取消", null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(view.getContext(), "预约成功", Toast.LENGTH_SHORT).show();
                            }

                        })

                        .show();
                return false;
            }
        });//收藏电站item的长按事件
        return holder;
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        CollectedStation station = mList.get(position);
        holder.stationImage.setImageResource(station.getImageId());
        holder.stationName.setText(station.getName());
        holder.stationAddr.setText(station.getAddress());
    }
    public int getItemCount(){
        return mList.size();
    }

}
