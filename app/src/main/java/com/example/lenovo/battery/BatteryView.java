package com.example.lenovo.battery;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2018/2/26.
 */

public class BatteryView extends View {
    private int mPower=100;

    public BatteryView(Context context) {
        super(context);
    }
    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int battery_left = 0;
        int battery_top = 0;
        int battery_width = 75;
        int battery_height = 45;

        int battery_head_width = 9;
        int battery_head_height = 9;

        int battery_inside_margin = 9;
        //画外框
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        Rect rect = new Rect(battery_left, battery_top,battery_left + battery_width, battery_top + battery_height);
        canvas.drawRect(rect, paint);

        float power_percent = mPower / 100.0f;
        Paint paint2 = new Paint(paint);

        //画电量
        if(power_percent != 0) {
            int p_left = battery_left + battery_inside_margin;
            int p_top = battery_top + battery_inside_margin;
            /*int p_right = p_left - battery_inside_margin + (int)((battery_width - battery_inside_margin) * power_percent);*/
            int p_right = p_left + (int)((battery_width - battery_inside_margin) * power_percent);
            int p_bottom = p_top + battery_height - battery_inside_margin * 2;
            Rect rect2 = new Rect(p_left, p_top, p_right , p_bottom);
            if(power_percent < 0.10){
                paint2.setColor(Color.RED);
                paint2.setStyle(Paint.Style.FILL);
                canvas.drawRect(rect2, paint2);
            }
            else if(power_percent > 0.1){
                paint2.setStyle(Paint.Style.FILL);
                canvas.drawRect(rect2, paint2);
            }
        }
        //画电池头
        int h_left = battery_left + battery_width;
        int h_top = battery_top + battery_height / 2 - battery_head_height / 2;
        int h_right = h_left + battery_head_width;
        int h_bottom = h_top + battery_head_height;
        Rect rect3 = new Rect(h_left, h_top, h_right, h_bottom);
        canvas.drawRect(rect3, paint2);
    }
    public void setPower(int power) {
        mPower = power;
        if(mPower < 0) {
            mPower = 0;
        }
        invalidate();
    }

}
