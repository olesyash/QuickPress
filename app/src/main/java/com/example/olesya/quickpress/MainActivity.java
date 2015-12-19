package com.example.olesya.quickpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;


public class MainActivity extends AppCompatActivity implements GameInterface{
    private static final int MIN_LEVEL = 1;
    private static final int MIN_COMPLEXITY = 0;
    private static final int DB_ERROR = -1;
    private Button settingsButton, startButton, recentButton;
    private Context context;
    private MyView myView;
    private TextView recentResultTextView, bestResultTextView;
    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private long bestResult = 0, currentResult = 0;
    private boolean first, running = false;
    private int pressedTime, pressedCount;
    private SharedPreferences memory;
    private  DAL dal;

    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            setEnabled(false);
            timerHandler.postDelayed(this, 100);
            long millis = (long)(System.currentTimeMillis() - startTime);
            recentResultTextView.setText(formatStringTime(millis));
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        settingsButton = (Button)findViewById(R.id.settingsButton);
        startButton = (Button)findViewById(R.id.startButton);
        myView = (MyView)findViewById(R.id.myView);
        recentResultTextView = (TextView) findViewById(R.id.recentResultTextView);
        recentButton = (Button) findViewById(R.id.recentResultButton);
        bestResultTextView = (TextView)findViewById(R.id.bestResultTextView);
        memory = getSharedPreferences("setting", MODE_PRIVATE);

        dal = new DAL(context);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    recentButton.setText(R.string.recentResultButtonText);
                    setEnabled(true);
                    timerHandler.removeCallbacks(timerRunnable);
                    running = false;
                } else {
                    myView.bttn_pressed = true;
                    recentButton.setText("Current time:");
                    startTime = System.currentTimeMillis();
                    timerHandler.postDelayed(timerRunnable, 0);
                    myView.invalidate();
                    running = true;
                }
            }
        });

        restoreResults();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(context, SettingsActivity.class);
            startActivityForResult(intent, 1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restoreResults();

    }


    @Override
    protected void onStop(){
        super.onStop();
        int level = memory.getInt("level", MIN_LEVEL);
        int complexity = memory.getInt("complexity", MIN_COMPLEXITY);
        dal.saveTimes(level, complexity, currentResult, bestResult);
    }

    @Override
    public void pressed() {
        pressedTime = memory.getInt("level", MIN_LEVEL);
        pressedCount++;

        if(pressedCount == pressedTime)
        {
        timerHandler.removeCallbacks(timerRunnable);
            currentResult = System.currentTimeMillis() - startTime;

            recentResultTextView.setText(formatStringTime(currentResult));
            if( first|| (currentResult < bestResult))
        {
            bestResult = currentResult;

            bestResultTextView.setText(formatStringTime(bestResult));
            first = false;
        }
            recentButton.setText("Recent result");
            setEnabled(true);

            int level = memory.getInt("level", MIN_LEVEL);
            int complexity = memory.getInt("complexity", MIN_COMPLEXITY);
            dal.saveTimes(level, complexity, currentResult, bestResult);
            bestResult = dal.getBestTime(level, complexity);
            currentResult = dal.getRecentTime(level, complexity);
            Log.e("DB debug", "bestResult after saving" + bestResult);
            Log.e("DB debug", "currentResult after saving" + currentResult);
        }
        else
            myView.invalidate();


    }
    private void setEnabled(boolean enabled){
        settingsButton.setEnabled(enabled);
        recentButton.setEnabled(enabled);
        if(enabled){
            pressedCount = 0;
            startButton.setText(R.string.start);
            running = false;
        }
        else {
            startButton.setText(R.string.stop);
        }
    }

    private void restoreResults()
    {
        int level = memory.getInt("level", MIN_LEVEL);
        int complexity = memory.getInt("complexity", MIN_COMPLEXITY);
        bestResult = dal.getBestTime(level, complexity);
        currentResult = dal.getRecentTime(level, complexity);
        Log.e("DB debug", "bestResult" + bestResult);
        Log.e("DB debug", "currentResult" + currentResult);
        if(bestResult == DB_ERROR || currentResult == DB_ERROR) {
            first = true;
            Log.e("DB debug", "first" + first);
            bestResult = 0;
            currentResult = 0;
        }

            bestResultTextView.setText(formatStringTime(bestResult));
            recentResultTextView.setText(formatStringTime(currentResult));


    }

    private String formatStringTime(long millis)
    {
        int seconds = (int) (millis / 1000);
        int milliseconds = (int)(millis - seconds*1000);
        seconds = seconds % 60;
        return String.format("%d:%03d", seconds, milliseconds);
    }
    }
