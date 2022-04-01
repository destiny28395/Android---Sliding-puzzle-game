package com.example.myapplication;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Random;


public class GameView extends SurfaceView {
    private final SurfaceHolder holder;
    private final GameLoopThread gameLoopThread;
    private Canvas myCanvas = null;
    Random random = new Random();
    public int k_iDeadSquare;
    private Button ba = new Button(245,350,845,550, 200, "        3x3");
    private Button bon = new Button(245, 650, 845, 850, 200, "        4x4");
    private Button nam = new Button(245, 950, 845, 1150, 200, "        5x5");
    private Button btn_score =  new Button(245, 1250, 845, 1450, 200, " TIMEBOARD"); // exit stateflashcreen va win lose
    private Button savetimeboard = new Button(245,350,845,550, 200, "  AUTO SAVE");
    private Button btn_exit = new Button(245, 1550, 845, 1750, 200, "       EXIT"); // exit stateflashcreen va win lose
    private Button btn_gomenu = new Button(30, 1500, 700, 1600, 100, " Back to menu"); // back menu trong ingame
    private Button btn_playnew = new Button(30, 1600, 700, 1700, 100, "   NEW GAME"); // newgame trong ingame
    private Button btn_exitin = new Button(30, 1700, 700, 1800, 100, "         EXIT"); // exit ingame
    private Button btn_lose = new Button(245,50,845,250, 200, "   YOU LOSE");
    private Button btn_win = new Button(245,50,845,250, 200, "   YOU WIN");
    private Button btn_playnew1 = new Button(245, 650, 845, 850, 200, " PLAY AGAIN"); //state lose win
    private Button btn_gomenu1 = new Button(245, 950, 845, 1150, 200, "      MENU"); // state lose win
    private Button btn_gomenu2 =  new Button(45, 1450, 1045, 1750, 200, "   BACK TO MENU"); // state lose win
    //    private Button btn_score = new Button(245,050,845,250, 200, "YOUR SCORE: " +score);
    private Bitmap bitmap;
    private boolean m_isUsingBitmap = true;


    private boolean m_isCheckIndex = false;

    enum GameState{
        StateWinGame,
        StateFlashScreen,
        StateModeGame,
        StateInGame,
        StateLose,
        StateScore
    }
    GameState m_GameState;

    ///IO
    Context m_context;

    public GameView(Context context)
    {
        super(context);
        ///IO
        m_context = context;


        gameLoopThread = new GameLoopThread(this);
        m_GameState = GameState.StateFlashScreen;


        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;
                gameLoopThread.setRunning(false);
                while (retry)
                {

                    try
                    {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e)
                    {
                    }
                }
            }
            @Override

            public void surfaceCreated(SurfaceHolder holder)
            {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            @Override

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {

            }

        });
    }
    public ArrayList<Square> ar;
    public Timeleft timeleft = new Timeleft(false);
    public TimeUse timeuse = new TimeUse(true);

    private void init(int n,int time)
    {
        k_iDeadSquare= n*n-1;
        ///IO
        m_sData = "";
        LoadData();
        timeleft.setTime(time,0);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pan);
        ar = new ArrayList<Square>();

        ArrayList<Integer> arr = RandomIndex(n);

        int k = 0;
        for(int i=0;i<n;i++)
        {
            for(int j=0;j<n;j++)
            {
                ar.add(new Square(j * 200 ,i * 200, 190, arr.get(k), false));
                k++;
            }
        }
        ar.get(k_iDeadSquare).m_isDead = true;
    }

    public static ArrayList<Integer> RandomIndex(int n)
    {
        ArrayList<Integer> arrIndex = new ArrayList<>();

        Random random = new Random();

        int index;

        do {
            index = random.nextInt(n*n-1);

            if (arrIndex.indexOf(index) < 0)
            {
                arrIndex.add(index);
            }

        } while (arrIndex.size() < n*n-1);

        arrIndex.add(n*n-1);

        return arrIndex;
    }

    public boolean isCorrectIndex()
    {
        int z =(int) (Math.sqrt(k_iDeadSquare+1));
        for(int k = 0 ; k < k_iDeadSquare; k++)
        {
            int x = ar.get(k).getX_Square();
            int y = ar.get(k).getY_Square();

            int v = ar.get(k).getValue();
            int iv = (v % z) * 200;
            int jv = (v /z) * 200;

            Log.println(Log.DEBUG, "INDEX", "Check " + k + "(" + x + "," + y + ")" +
                    "~v(" + v +  "(" + iv + "," + jv + ")");
            //so 2 gia tri voi nhau
            if (x != iv || y != jv)
                return false;
        }
        return  true;

    }

    public void onDraw(Canvas canvas) {
        if (canvas == null)
            return;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);

        if (m_GameState == GameState.StateFlashScreen) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);
            canvas.drawText("MODE", 370, 200, textPaint);
            textPaint.setColor(Color.YELLOW);
            ba.onDraw(canvas, textPaint);
            textPaint.setColor(Color.CYAN);
            bon.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            nam.onDraw(canvas, textPaint);
            textPaint.setColor(Color.BLUE);
            btn_score.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            btn_exit.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);

        }


        if (m_GameState == GameState.StateInGame) {
            if (myCanvas == null)
                myCanvas = canvas;
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);

//        Log.println(Log.DEBUG, "DRAW", "Draw " + ar.size());
            for (int i = 0; i < ar.size(); i++) {
                if (m_isUsingBitmap == true && bitmap != null) {
                    ar.get(i).onDrawbitmap(canvas, bitmap);
                } else {
                    ar.get(i).onDraw(canvas);
                }
            }

            timeleft.setPosition(canvas.getWidth()/2+250, canvas.getHeight() - 100);
            timeleft.draw(canvas);
            timeuse.setPosition(canvas.getWidth()/2+250, canvas.getHeight() - 200);
            timeuse.draw(canvas);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);
            btn_playnew.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_gomenu.onDraw(canvas, textPaint);
            textPaint.setColor(Color.BLUE);
            btn_exitin.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);

        }
        if (m_GameState == GameState.StateWinGame) {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);

            btn_win.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_playnew1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_gomenu1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.BLUE);
            btn_exit.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            savetimeboard.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            btn_score.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
        }
        if(m_GameState == GameState.StateScore)
            {
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
                Paint textPaint = new Paint();
                textPaint.setColor(Color.WHITE);
                textPaint.setStyle(Paint.Style.FILL);
                textPaint.setTextSize(120);
                LoadData();
                Button btn_score1 =  new Button(45, 350, 1045, 550, 200, "          LAST PLAY: ");
                Button btn_score2 =  new Button(45, 750, 1045, 1050, 200, ""+m_sData);
                btn_score1.onDraw(canvas, textPaint);
                textPaint.setColor(Color.GREEN);
                btn_score2.onDraw(canvas, textPaint);
                textPaint.setColor(Color.GREEN);
                btn_gomenu2.onDraw(canvas, textPaint);
                textPaint.setColor(Color.GREEN);
            }
        if (m_GameState == GameState.StateLose)
        {
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);

            btn_lose.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_playnew1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_gomenu1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.BLUE);
            btn_exit.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            savetimeboard.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            btn_score.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
        }


    }

    public void Update(Canvas canvas)
    {
        Paint paint = new Paint();
        if(m_GameState == GameState.StateFlashScreen)
        {
            return;
        }
        if (m_GameState == GameState.StateInGame)
        {
            timeleft.setDecrease(gameLoopThread.deltatime);
            timeleft.update();
            timeuse.setDecrease(gameLoopThread.deltatime);
            timeuse.update();
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);
//            btn_score.onDraw(canvas, textPaint);
//            textPaint.setColor(Color.RED);

            if(isCorrectIndex())
            {

                m_GameState = GameState.StateWinGame;
                StringBuilder sb = new StringBuilder();
                sb.append(m_sData);
                if ((int) (Math.sqrt(k_iDeadSquare+1)) == 3)
                {
                    sb.append("YOU WIN 3x3 ");
                }
                if ((int) (Math.sqrt(k_iDeadSquare+1)) == 4)
                {
                    sb.append("YOU WIN 4x4 ");
                }
                if ((int) (Math.sqrt(k_iDeadSquare+1)) == 5)
                {
                    sb.append("YOU WIN 5x5");
                }
//                sb.append("" + (int) timeleft.Get_min() + ":" + (int) timeleft.Get_sec());
//                sb.append("" + random.nextInt() + ":" + random.nextInt());
                sb.append('\n');
                m_sData = sb.toString();
                SaveData();

            }

            if(timeleft.GetTotalTimeInSec() < 0)
            {
                m_GameState = GameState.StateLose;
                StringBuilder sb = new StringBuilder();
                sb.append(m_sData);
                sb.append("\n");
                sb.append("YOU LOSE");
//                sb.append("\n");
//                sb.append("" + (int) timeleft.Get_min() + ":" + (int) timeleft.Get_sec());
//                sb.append("" + random.nextInt() + ":" + random.nextInt());
                m_sData = sb.toString();
                SaveData();
            }
        }
        if(m_GameState == GameState.StateWinGame)
        {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);

            btn_win.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_playnew1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_gomenu1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.BLUE);
            btn_exit.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            savetimeboard.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_score.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
        }
        if (m_GameState == GameState.StateLose)
        {
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(120);

            btn_score.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_lose.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_playnew1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_gomenu1.onDraw(canvas, textPaint);
            textPaint.setColor(Color.BLUE);
            btn_exit.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
            savetimeboard.onDraw(canvas, textPaint);
            textPaint.setColor(Color.RED);
            btn_score.onDraw(canvas, textPaint);
            textPaint.setColor(Color.GREEN);
        }
    }
    public void Swap_square(int squareA, int squareB)
    {
        int x = ar.get(squareA).getX_Square(),y = ar.get(squareA).getY_Square();
        ar.get(squareA).SetPosition(ar.get(squareB).getX_Square(),ar.get(squareB).getY_Square());
        ar.get(squareB).SetPosition(x,y);
    }
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                if (m_GameState == GameState.StateFlashScreen)
                {
                    int x;
                    int y;
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();
                    if(x>ba.PositionX() && x<ba.PositionX_end() && y>ba.PositionY() && y<ba.PositionY_end())
                    {
                        init(3,5);
                        RandomIndex(3);
                        m_GameState = GameState.StateInGame;


                    }
                    if(x>bon.PositionX() && x<bon.PositionX_end() && y>bon.PositionY() && y<bon.PositionY_end())
                    {
                        init(4,10);
                        RandomIndex( 4);
                        m_GameState = GameState.StateInGame;

                    }
                    if(x>nam.PositionX() && x<nam.PositionX_end() && y>nam.PositionY() && y<nam.PositionY_end())
                    {
                        init(5,15);
                        RandomIndex(5);
                        m_GameState = GameState.StateInGame;

                    }
//                    if(x>sau.PositionX() && x<sau.PositionX_end() && y>sau.PositionY() && y<sau.PositionY_end())
//                    {
//                        init(6);
//                        Check_index(6);
//                        RandomIndex(6);
//                        m_GameState = GameState.StateInGame;
//                    }
                    if(x>btn_score.PositionX() && x<btn_score.PositionX_end() && y>btn_score.PositionY() && y<btn_score.PositionY_end())
                    {
                       m_GameState= GameState.StateScore; //ben kia m co them 2 dong code cua ong thay vo chua? , them roi
                    }
                    if(x>btn_exit.PositionX() && x<btn_exit.PositionX_end() && y>btn_exit.PositionY() && y<btn_exit.PositionY_end())
                    {
                        System.exit(0);
                    }

                }
                if (m_GameState == GameState.StateInGame) {

                    int x, y;
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();

                    Log.println(Log.DEBUG, "TOUCH", "touch at " + x + " " + y + " " + motionEvent.getX());
                    int iSelectedSquare = 0;
                    if(x>btn_gomenu.PositionX() && x<btn_gomenu.PositionX_end() && y>btn_gomenu.PositionY() && y<btn_gomenu.PositionY_end())
                    {
                        m_GameState = GameState.StateFlashScreen;
                        break;
                    }
                    if(x>btn_playnew.PositionX() && x<btn_playnew.PositionX_end() && y>btn_playnew.PositionY() && y<btn_playnew.PositionY_end())
                    {
                        if(k_iDeadSquare == 8)
                        {
                            init(3,5);
                            RandomIndex(3);
                            m_GameState = GameState.StateInGame;
                        }
                        if(k_iDeadSquare == 15)
                        {
                            init(4,10);
                            RandomIndex(4);
                            m_GameState = GameState.StateInGame;
                        }

                        if(k_iDeadSquare == 24)
                        {
                            init(5,15);
                            RandomIndex(5);
                            m_GameState = GameState.StateInGame;
                        }
//                        if(k_iDeadSquare == 35)
//                        {
//                            init(6);
//                            Check_index(6);
//                            RandomIndex(6);
//                            m_GameState = GameState.StateInGame;
//                        }
                    }
                    if(x>btn_exitin.PositionX() && x<btn_exitin.PositionX_end() && y>btn_exitin.PositionY() && y<btn_exitin.PositionY_end())
                    {
                        System.exit(0);
                    }
                    for (int i = 0; i < ar.size(); i++) {

                        if (x > ar.get(i).getX_Square() && x <= ar.get(i).getX_Square() + ar.get(i).get_sizeSquare()
                                && y > ar.get(i).getY_Square() && y <= ar.get(i).getY_Square() + ar.get(i).get_sizeSquare()) {
                            iSelectedSquare = i;
                            break;
                        }
                    }

                    if ((Math.abs(ar.get(iSelectedSquare).getX_Square() - ar.get(k_iDeadSquare).getX_Square()) <= 251 && ar.get(iSelectedSquare).getY_Square() == ar.get(k_iDeadSquare).getY_Square())
                            || (Math.abs(ar.get(iSelectedSquare).getY_Square() - ar.get(k_iDeadSquare).getY_Square()) <= 251 && ar.get(iSelectedSquare).getX_Square() == ar.get(k_iDeadSquare).getX_Square()))
                        Swap_square(iSelectedSquare, k_iDeadSquare);

                    else {
                        break;
                    }

                }
                if(m_GameState == GameState.StateWinGame){
                    int x, y;
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();
                    if(x>btn_gomenu1.PositionX() && x<btn_gomenu1.PositionX_end() && y>btn_gomenu1.PositionY() && y<btn_gomenu1.PositionY_end())
                    {
                        m_GameState = GameState.StateFlashScreen;
                        break;
                    }
                    if(x>btn_playnew1.PositionX() && x<btn_playnew1.PositionX_end() && y>btn_playnew1.PositionY() && y<btn_playnew1.PositionY_end())
                    {
                        if(k_iDeadSquare == 8)
                        {
                            init(3,5);
                            RandomIndex(3);
                            m_GameState = GameState.StateInGame;
                        }
                        if(k_iDeadSquare == 15)
                        {
                            init(4,10);

                            RandomIndex(4);
                            m_GameState = GameState.StateInGame;
                        }

                        if(k_iDeadSquare == 24)
                        {
                            init(5,15);
                            RandomIndex(5);
                            m_GameState = GameState.StateInGame;
                        }
//                        if(k_iDeadSquare == 35)
//                        {
//                            init(6);
//                            Check_index(6);
//                            RandomIndex(6);
//                            m_GameState = GameState.StateInGame;
//                        }
                    }
                    if(x>btn_exit.PositionX() && x<btn_exit.PositionX_end() && y>btn_exit.PositionY() && y<btn_exit.PositionY_end())
                    {
                        System.exit(0);
                    }
                    if(x>btn_score.PositionX() && x<btn_score.PositionX_end() && y>btn_score.PositionY() && y<btn_score.PositionY_end())
                    {
                        m_GameState=GameState.StateScore;
                    }
//                    if(x>savetimeboard.PositionX() && x<savetimeboard.PositionX_end() && y>savetimeboard.PositionY() && y<savetimeboard.PositionY_end())
//                    {
//                        StringBuilder sb = new StringBuilder();
//                        sb.append(m_sData);
//                        sb.append('\n');
//                        sb.append("" + (int) timeleft.Get_min() + ":" + (int) timeleft.Get_sec());
////                        sb.append("" + random.nextInt() + ":" + random.nextInt());
//                        m_sData = sb.toString();
//                        SaveData();
//                    }
                }
                if(m_GameState == GameState.StateLose){
                    int x, y;
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();
                    if(x>btn_gomenu1.PositionX() && x<btn_gomenu1.PositionX_end() && y>btn_gomenu1.PositionY() && y<btn_gomenu1.PositionY_end())
                    {
                        m_GameState = GameState.StateFlashScreen;
                        break;
                    }
                    if(x>btn_playnew1.PositionX() && x<btn_playnew1.PositionX_end() && y>btn_playnew1.PositionY() && y<btn_playnew1.PositionY_end())
                    {
                        if(k_iDeadSquare == 8)
                        {
                            init(3,5);

                            RandomIndex(3);
                            m_GameState = GameState.StateInGame;
                        }
                        if(k_iDeadSquare == 15)
                        {
                            init(4,10);

                            RandomIndex(4);
                            m_GameState = GameState.StateInGame;
                        }

                        if(k_iDeadSquare == 24)
                        {
                            init(5,15);
                            RandomIndex(5);
                            m_GameState = GameState.StateInGame;
                        }
//                        if(k_iDeadSquare == 35)
//                        {
//                            init(6);
//                            Check_index(6);
//                            RandomIndex(6);
//                            m_GameState = GameState.StateInGame;
//                        }
                    }
                    if(x>btn_exit.PositionX() && x<btn_exit.PositionX_end() && y>btn_exit.PositionY() && y<btn_exit.PositionY_end())
                    {
                        System.exit(0);
                    }
                    if(x>btn_score.PositionX() && x<btn_score.PositionX_end() && y>btn_score.PositionY() && y<btn_score.PositionY_end())
                    {
                        m_GameState=GameState.StateScore;
                    }
//
                }
                if (m_GameState==GameState.StateScore)
                {
                    int x, y;
                    x = (int) motionEvent.getX();
                    y = (int) motionEvent.getY();
                    if(x>btn_gomenu2.PositionX() && x<btn_gomenu2.PositionX_end() && y>btn_gomenu2.PositionY() && y<btn_gomenu2.PositionY_end())
                    {
                        m_GameState = GameState.StateFlashScreen;
                        break;
                    }
                }
        }
        return true;
    }

    ///IO
    String m_sData;
    void SaveData()
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(m_context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(m_sData);
            outputStreamWriter.close();
            Log.d("IO", "Save:" +m_sData);
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    void LoadData()
    {
        String ret = "";

        try {
            InputStream inputStream = m_context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        m_sData = ret;
        Log.d("IO", "Load:"+m_sData);
    }

}


