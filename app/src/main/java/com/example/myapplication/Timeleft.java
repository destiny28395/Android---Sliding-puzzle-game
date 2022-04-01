package com.example.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Timeleft {
    private double total_sec;
    private double sec;
    private double min;
    private int x;
    private int y;
    private boolean isIncrease;

    public Timeleft(boolean state){
        if (state == true){
            this.isIncrease = true;
        }
        else{
            this.isIncrease = false;
        }
    }
    public void update(){
        if (sec < 0.001){
            min -= 1;
            sec = 59.999;
        }
        if (sec > 59.999){
            min += 1;
            sec = 0;
        }
    }
    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(100);

        if (Get_min() < 0){
            canvas.drawText("00:00", x,y,paint);
        }
        else{
            if (Get_min() >= 10 && Get_sec() >= 10){
                canvas.drawText(+(int)(Get_min()) + ":" + (int)(Get_sec()), x,y,paint);
            }
            if (Get_min() < 10 && Get_sec() < 10){
                canvas.drawText("0"+(int)(Get_min()) + ":0" + (int)(Get_sec()), x,y,paint);
            }
            if (Get_min() >= 10 && Get_sec() < 10){
                canvas.drawText((int)(Get_min()) + ":0" + (int)(Get_sec()), x,y,paint);
            }
            if (Get_min() < 10 && Get_sec() >= 10){
                canvas.drawText("0"+(int)(Get_min()) + ":" + (int)(Get_sec()), x,y,paint);
            }
        }
    }
    public void setTime(double minutes, double seconds){
        this.min= minutes;
        this.sec = seconds;
        total_sec = min * 60 + sec;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public void setDecrease(double deltaTime)
    {

        if (isIncrease == false && min >= 0)
        {
//            sec -= deltaTime;
            total_sec -= deltaTime;
        }
    }
    public void setIncrease(double deltaTime)
    {
        if (isIncrease == false){
//            sec += deltaTime;
            total_sec += deltaTime;
        }
    }
    public int Get_sec()
    {
        return ((int)total_sec) % 60;
    }
    public int Get_min()
    {
        return ((int)total_sec) / 60;
    }
    public double GetTotalTimeInSec()
    {
        return total_sec;
    }
}
