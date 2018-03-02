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

import java.net.CacheRequest;
import java.util.List;

/**
 * Created by lenovo on 2018/2/26.
 */

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private List<Car> mCarList;

    public CarAdapter(List<Car> carList) {
        mCarList=carList;
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{
        View carView;
        TextView carName;
        TextView carElec;
        TextView carPriority;
        BatteryView batteryView;

        public ViewHolder (View view){
            super(view);
            carView = view;
            batteryView = (BatteryView) view.findViewById(R.id.battery_view);
            carName = (TextView) view.findViewById(R.id.car_name);
            carElec = (TextView) view.findViewById(R.id.car_elec);
            carPriority = (TextView) view.findViewById(R.id.car_priority);
        }
    }

    public CarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout,parent,false);
        final CarAdapter.ViewHolder holder=new CarAdapter.ViewHolder(view);
        /*holder.carView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Car car = mCarList.get(position);
                Toast.makeText(view.getContext(), "clik", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), PowerActivity.class);
                view.getContext().startActivity(intent);
            }
        });//汽车item的点击事件*/

        holder.carView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final int position = holder.getAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                AlertDialog show = builder.setMessage("确定删除该车？")
                        .setPositiveButton("取消", null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mCarList.remove(position);
                                notifyItemRemoved(position);
                            }

                        })

                        .show();
                return false;
            }
        });//汽车item的长按事件
        return holder;
    }
    public void onBindViewHolder(CarAdapter.ViewHolder holder, int position){
        Car car = mCarList.get(position);
        int d = (int)(car.getElec()*100);
        holder.carElec.setText(String.valueOf(d)+"%");
        holder.carName.setText(car.getName());
        holder.carPriority.setText(car.getPriority());
        holder.batteryView.setPower(d);
    }
    public int getItemCount(){
        return mCarList.size();
    }
}
