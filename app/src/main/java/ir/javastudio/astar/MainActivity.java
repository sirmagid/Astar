package ir.javastudio.memorygame;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import ir.javastudio.memorygame.lineartimer.LinearTimer;
import ir.javastudio.memorygame.lineartimer.LinearTimerStates;
import ir.javastudio.memorygame.lineartimer.LinearTimerView;
import ir.javastudio.memorygame.pathfinding.AStarPathFinder;
import ir.javastudio.memorygame.pathfinding.Path;
import ir.javastudio.memorygame.pathfinding.PathFindingContext;
import ir.javastudio.memorygame.pathfinding.TileBasedMap;

public class MainActivity extends AppCompatActivity implements LinearTimer.TimerListener,TileBasedMap {

    private LinearTimerView linearTimerView;
    private LinearTimer linearTimer;
    private TextView time;
    private long duration = 10 * 1000;
    private boolean boot_in=false;
    TextView text_view_timer;
    String not_have_next_image="null";
    private int boot_state=1;
    private ImageButton bootfollow;
    private static final int MAX_PATH_LENGTH = 100;

    private static final int START_X = 0;
    private static final int START_Y = 0;

    private static final int GOAL_X =5 ;
    private static final int GOAL_Y = 9;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;

    private static int[][] MAP = {
            {0,0,0,1,0,0,0,0,0,0},
            {0,0,0,1,0,0,0,1,0,0},
            {0,0,0,1,0,0,0,1,0,0},
            {0,0,0,1,0,1,0,0,0,0},
            {0,0,0,1,0,0,0,1,0,0},
            {0,0,0,1,0,0,0,1,0,0},
            {0,0,0,1,0,1,0,1,0,0},
            {0,0,0,1,0,0,0,0,1,0},
            {0,0,0,1,0,1,0,0,0,1},
            {0,0,0,0,0,0,0,1,0,1}
    };

    Button btnTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_view_timer=(TextView) findViewById(R.id.text_view_timer);
        linearTimerView = (LinearTimerView) findViewById(R.id.linearTimer);
        linearTimer = new LinearTimer.Builder()
                .linearTimerView(linearTimerView)
                .duration(duration)
                .timerListener(this)
                .progressDirection(LinearTimer.CLOCK_WISE_PROGRESSION)
                .preFillAngle(0)
                .endingAngle(360)
                .getCountUpdate(LinearTimer.COUNT_UP_TIMER, 1000)
                .build();


        bootfollow = (ImageButton) findViewById(R.id.bootfollow);
        bootfollow .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                switch (boot_state){
                    case 1:
                        try {

                            Log.d("time_control",String.valueOf(linearTimer.getState().toString()));
                            //linearTimer.restartTimer();
                            if(linearTimer.getState()== LinearTimerStates.INITIALIZED ) {
                                linearTimer.startTimer();
                                boot_state = 0;
                            }

                            if(linearTimer.getState()== LinearTimerStates.PAUSED ) {
                                linearTimer.restartTimer();
                                boot_state = 0;
                            }

                            if(linearTimer.getState()== LinearTimerStates.FINISHED ) {
                                linearTimer.restartTimer();
                                boot_state = 0;
                            }



                            text_view_timer.setVisibility(View.VISIBLE);
                            linearTimerView.setVisibility(View.VISIBLE);

                        } catch (IllegalStateException e) {
                            //e.printStackTrace();
                            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 0:
                        try {
                            boot_state=1;
                            text_view_timer.setVisibility(View.GONE);
                            linearTimerView.setVisibility(View.GONE);
                            linearTimer.pauseTimer();
                        } catch (IllegalStateException e) {
                            //e.printStackTrace();
                            //Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        boot_state=1;
                }

                Log.d("time_control",String.valueOf(boot_state));
            }
        });


        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout_tags);

        for (int i = 0; i < 10; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 10; j++) {
                btnTag = new Button(this);
                //btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
               // btnTag.setText(String.valueOf(i)+"-"+String.valueOf(j)); //"btn" + (j + 1 + (i * 10))
                btnTag.setText(String.valueOf((j  + (i * 10))));
                btnTag.setId(j  + (i * 10));
                row.addView(btnTag);
            }

            layout.addView(row);
        }

        //SimpleMap map = new SimpleMap();

        AStarPathFinder pathFinder = new AStarPathFinder(this, MAX_PATH_LENGTH, false);
        Path path = pathFinder.findPath(null, START_X, START_Y, GOAL_X, GOAL_Y);

        int length = path.getLength();
        Log.d("astar","Found path of length: " + length + ".");


        for(int i = 1; i < 10; i++) {
            for (int j = 1; j <10; j++) {

                if(MAP[i][j]==1){
                    int temp=j  + (i * 10) ;
                    Log.d("black_block",String.valueOf(temp)+"|"+String.valueOf(MAP[i][j]));
                    // btnTag[temp].
                    Button btn_tmp = (Button)findViewById(temp);
                    btn_tmp.setBackgroundColor(Color.BLACK);
                }

            }
        }

        for(int i = 0; i < length; i++) {
            Log.d("astar","Move to: " + path.getY(i) + "," + path.getX(i) + ".");
            int temp=path.getX(i)  + (path.getY(i) * 10);
           // btnTag[temp].
            Button btn_tmp = (Button)findViewById(temp);
            btn_tmp.setText("<"+String.valueOf(temp)+">");
            btn_tmp.setTextColor(Color.RED);
            btn_tmp.setBackgroundColor(Color.BLUE);

                /*if(b.getId().equals(your_id_to_check)) {
                    //DO WHAT YOU WANT
                }*/
           // }
        }

        Button btn_tmp_start = (Button)findViewById(START_X+(START_Y*10));
        btn_tmp_start.setText("<"+String.valueOf(START_X+(START_Y*10))+">");
        btn_tmp_start.setTextColor(Color.RED);
        btn_tmp_start.setBackgroundColor(Color.GREEN);

        Button btn_tmp_end = (Button)findViewById(GOAL_X+(GOAL_Y*10));
        btn_tmp_end.setText("<"+String.valueOf(GOAL_X+(GOAL_Y*10))+">");
        btn_tmp_end.setTextColor(Color.RED);
        btn_tmp_end.setBackgroundColor(Color.GREEN);



    }

    @Override
    public void animationComplete() {
        Log.i("Animation", "complete");
        //if(!not_have_next_image.equals("noview")){
            linearTimer.restartTimer();
            //ImageAsyncTask_next();
        //}else{
            // have error
            //linearTimer.pauseTimer();
          //  text_view_timer.setVisibility(View.GONE);
          //  linearTimerView.setVisibility(View.GONE);
        //}
    }
    @Override
    public void timerTick(long tickUpdateInMillis) {
        //Log.i("Time left", String.valueOf(tickUpdateInMillis));
        String formattedTime = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(tickUpdateInMillis),
                TimeUnit.MILLISECONDS.toSeconds(tickUpdateInMillis)
                        - TimeUnit.MINUTES
                        .toSeconds(TimeUnit.MILLISECONDS.toHours(tickUpdateInMillis)));

        text_view_timer.setText(formattedTime);
    }

    @Override
    public void onTimerReset() {
        text_view_timer.setText("");
    }

    @Override
    public boolean blocked(PathFindingContext ctx, int x, int y) {
        return MAP[y][x] != 0;
    }

    @Override
    public float getCost(PathFindingContext ctx, int x, int y) {
        return 1.0f;
    }

    @Override
    public int getHeightInTiles() {
        return HEIGHT;
    }

    @Override
    public int getWidthInTiles() {
        return WIDTH;
    }

    @Override
    public void pathFinderVisited(int x, int y) {}




}
