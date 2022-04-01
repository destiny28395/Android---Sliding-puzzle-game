package com.example.myapplication;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TimeUse {
    private double sec;
    private double min;
    private int x;
    private int y;
    private boolean isIncrease;

    public TimeUse(boolean state){
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
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(100);

        if (min < 0){
            canvas.drawText("00:00", x,y,paint);
        }
        else{
            if (min > 10 && sec > 10){
                canvas.drawText(+(int)(min) + ":" + (int)(sec), x,y,paint);
            }
            if (min < 10 && sec < 10){
                canvas.drawText("0"+(int)(min) + ":0" + (int)(sec), x,y,paint);
            }
            if (min > 10 && sec < 10){
                canvas.drawText((int)(min) + ":0" + (int)(sec), x,y,paint);
            }
            if (min < 10 && sec > 10){
                canvas.drawText("0"+(int)(min) + ":" + (int)(sec), x,y,paint);
            }
        }
    }
    public void setTime(double minutes, double seconds){
        this.min= minutes;
        this.sec = seconds;
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setDecrease(double deltaTime){
//        if (isIncrease){
//            sec += deltaTime;
//        }
        if (isIncrease == false && min >= 0){
            sec += deltaTime;
        }
    }
    public void setIncrease(double deltatime)
    {
        if (isIncrease == false){
            sec -= deltatime;
        }
    }
    public double Get_sec()
    {
        return sec;
    }
    public double Get_min()
    {
        return min;
    }
}
