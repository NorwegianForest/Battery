package com.example.lenovo.battery;

/**
 * Created by lenovo on 2018/2/27.
 */

public class Car {
    private String name;     //车的型号
    private String priority; //管理权限
    private double elec;     //电量
    private String brand;
    private String number;
    private String plate;
    private String battery;
    private String model;
    private String battery_number;
    private String mileage;
    private String time;

    public Car(String name,String priority,double elec){
        this.name = name;
        this.priority = priority;
        this.elec = elec;
    }

    public Car(String brand, String number, String plate, String battery, String model, String battery_number, String mileage, String time) {
        this.brand = brand;
        this.number = number;
        this.plate = plate;
        this.battery = battery;
        this.model = model;
        this.battery_number = battery_number;
        this.mileage = mileage;
        this.time = time;
    }

    public String getName(){
        return name;
    }
    public String getPriority(){
        return priority;
    }
    public double getElec(){return elec;}

    public void setName(String name) {
        this.name = name;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setElec(double elec) {
        this.elec = elec;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBattery_number() {
        return battery_number;
    }

    public void setBattery_number(String battery_number) {
        this.battery_number = battery_number;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
