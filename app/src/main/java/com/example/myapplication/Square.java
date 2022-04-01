package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Square {
    private int x, y;
    private int size;
    private int dx = 1;
    private int dy = 1;
    public int speed = 200;


    private static Paint s_paint = null;
    private static Paint s_painttext = null;

    String myText;

    private int m_index = 0;

    public boolean m_isDead;

    private Rect m_rectSrc;


    public Square(int x, int y, int size, int index, boolean isDead) {
        if (s_paint == null) {
            s_paint = new Paint();
            s_paint.setStyle(Paint.Style.FILL);
            s_paint.setColor(Color.BLUE);
        }
        if (s_painttext == null) {
            s_painttext = new Paint();
            s_painttext.setStyle(Paint.Style.FILL);
            s_painttext.setColor(Color.YELLOW);
            s_painttext.setTextSize(50);
        }
        this.x = x;
        this.y = y;
        this.size = size;
        m_index = index;

        //Log.println(Log.DEBUG, "Random", "new number " + index);
        int l = index % 4 * 270 + 200;
        int t = index / 4 * 270 + 200;

        m_rectSrc = new Rect(l, t, l + size, t + size);
        myText = "" + index;
        m_isDead = isDead;
    }

    public void onDraw(Canvas canvas)
    {
        if(m_isDead == true)
        {
            return;
        }

        canvas.drawRect(x,y,x+size,y+size,s_paint);

        canvas.drawText(myText,x+50,y+50,s_painttext);

    }

    public void onDrawbitmap(Canvas canvas, Bitmap bitmap)
    {
        if(m_isDead == true)
        {
            return;
        }
        canvas.drawBitmap(bitmap,m_rectSrc, new Rect(x,y,x+size,y+size), s_paint);
    }


    public void SetPosition(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public int getValue() { return m_index; }
    public int get_sizeSquare() { return size; }

    public int getX_Square() { return x; }
    public int getY_Square() { return y; }
}

