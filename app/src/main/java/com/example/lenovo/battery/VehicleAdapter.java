package com.example.lenovo.battery;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.battery.Vehicle;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by szl on 2018/3/1.
 */

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private Context context;
    private List<Vehicle> vehicleList;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }
}
