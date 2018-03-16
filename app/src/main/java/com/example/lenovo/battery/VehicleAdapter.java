package com.example.lenovo.battery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.battery.Constants;
import com.battery.HttpUtil;
import com.battery.Vehicle;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by szl on 2018/3/1.
 * 用户爱车数据的配适器，参照Android教程中的例程
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private Context context;
    private List<Vehicle> vehicleList;
    private String userId;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView brand;
        TextView number;
        TextView plate;
        TextView battery;
        TextView model;
        TextView battery_number;
        TextView mileage;
        TextView time;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view;
            brand = view.findViewById(R.id.vehicle_item_brand);
            number = view.findViewById(R.id.vehicle_item_number);
            plate = view.findViewById(R.id.vehicle_item_plate);
            battery = view.findViewById(R.id.vehicle_item_battery);
            model = view.findViewById(R.id.vehicle_item_model);
            battery_number = view.findViewById(R.id.vehicle_item_battery_number);
            mileage = view.findViewById(R.id.vehicle_item_mileage);
            time = view.findViewById(R.id.vehicle_item_time);
        }
    }

    public VehicleAdapter(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public VehicleAdapter(List<Vehicle> vehicleList, String userId) {
        this.vehicleList = vehicleList;
        this.userId = userId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Vehicle vehicle = vehicleList.get(position);

        String brandStr = vehicle.getBrand() + " - " + vehicle.getModel();
        holder.brand.setText(brandStr);

        String numberStr = "车辆编号: " + vehicle.getNumber();
        holder.number.setText(numberStr);

        String plateStr = "车牌号码: " + vehicle.getPlate();
        holder.plate.setText(plateStr);

        Double rate = vehicle.getBattery().getResidualCapacity() / vehicle.getBattery().getActualCapacity();
        NumberFormat nFromat = NumberFormat.getPercentInstance();
        String rates = "剩余电量 - " + nFromat.format(rate);
        holder.battery.setText(rates);

        String modelStr = "电池型号: " + vehicle.getBattery().getModel();
        holder.model.setText(modelStr);

        String batteryStr = "电池编号: " + vehicle.getBattery().getNumber();
        holder.battery_number.setText(batteryStr);

        String mileageStr = "剩余里程: " + Double.toString(5.0 * vehicle.getBattery().getResidualCapacity()) + "km";
        holder.mileage.setText(mileageStr);

        String timeStr = "充电需要: " + (int) (4.0 * (vehicle.getBattery().getActualCapacity() - vehicle.getBattery().getResidualCapacity())) + "min";
        holder.time.setText(timeStr);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Vehicle v = vehicleList.get(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("更换参考车辆为 " + v.getPlate() + " ?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeReference(userId, Integer.toString(v.getId()));
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });
    }

    private void changeReference(String userId, String vehicleId) {
        RequestBody body = new FormBody.Builder().add("user_id", userId)
                .add("vehicle_id", vehicleId).build();
        HttpUtil.sendRequest(Constants.CHANGEREFERENCE, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }
}
