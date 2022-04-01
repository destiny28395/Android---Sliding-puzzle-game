package com.example.myapplication;



import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Button {
    private int x;
    private int y;
    private int x_end;
    private int y_end;
    private int size;
    private String text;
    private static Paint s_paint = null;
    public Button(int x,int y,int x_end,int y_end,int size,String text)
    {
        this.x = x;
        this.y = y;
        this.x_end = x_end;
        this.y_end = y_end;
        this.size = size;
        this.text = text;
        s_paint = new Paint();

    }
    public void onDraw(Canvas canvas,Paint textColor)
    {
        s_paint.setColor(Color.LTGRAY);
        s_paint.setStyle(Paint.Style.STROKE);
        s_paint.setStrokeWidth(10);
        canvas.drawRect(x,y,x_end,y_end,s_paint);
        s_paint = textColor;
        s_paint.setStyle(Paint.Style.FILL);
        s_paint.setTextSize(100);
        canvas.drawText(text,x+10,y + size/2 + 30,s_paint);
    }
    public int PositionX()
    {
        return x;
    }
    public int PositionY()
    {
        return y;
    }
    public int PositionX_end()
    {
        return x_end;
    }
    public int PositionY_end()
    {
        return y_end;
    }


}



