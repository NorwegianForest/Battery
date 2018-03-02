package com.example.lenovo.battery;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.battery.*;
import com.battery.Station;

import java.util.List;

/**
 * Created by szl on 2018/3/1.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context context;
    private List<Record> recordList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name;
        TextView time;
        TextView money;
        TextView oldBattery;
        TextView newBattery;
        TextView date;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view;
            name = view.findViewById(R.id.record_name);
            time = view.findViewById(R.id.record_time);
            money = view.findViewById(R.id.record_money);
            oldBattery = view.findViewById(R.id.record_old);
            newBattery = view.findViewById(R.id.record_new);
            date = view.findViewById(R.id.record_date);
        }
    }

    public RecordAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Record record = recordList.get(position);
        holder.name.setText(record.getStation().getName());
        holder.time.setText("预约时间: " + record.getDate());
        holder.money.setText("花费: " + Double.toString(record.getMoney()));
        holder.oldBattery.setText("旧电池编号: " + record.getOldBattery().getNumber());
        holder.newBattery.setText("新电池编号: " + record.getNewBattery().getNumber());
        holder.date.setText("完成时间: " + record.getDate());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }
}
