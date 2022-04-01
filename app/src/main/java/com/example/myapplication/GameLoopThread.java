package com.example.myapplication;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    static final long FPS = 60;
    private GameView view;
    private boolean running = false;
    public float deltatime = 0;
    public GameLoopThread(GameView view)
    {
        this.view = view;
    }
    public void setRunning(boolean run)
    {
        running = run;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long StartTime;
        long SleepTime;
        long currenttime;
        long lasttime = -1;

        while (running) {
            Canvas c = null;
            StartTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {

                    currenttime = System.currentTimeMillis();

                    if (lasttime == -1)
                        lasttime = currenttime;

                    deltatime = (currenttime - lasttime) / 1000.f;
                    lasttime = currenttime;


                    view.Update(c);
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);

                }
            }
//            SleepTime = ticksPS - (System.currentTimeMillis() - StartTime);
//            try{
//                if(SleepTime >0)
//                    Thread.sleep(SleepTime);
//                else
//                    Thread.sleep(10);
//            }catch (Exception e)
//            {
//
//            }
//        }

        }
    }
}
