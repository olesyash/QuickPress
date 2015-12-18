package com.example.olesya.quickpress;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private Button settingsButton, startButton, recentButton;
    private Context context;
    private MyView myView;
    private TextView recentResultTextView, bestResultTextView;
    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private long bestResult = 0, currentResult = 0;
    private boolean first = true, running = false;
    private int pressedTime, levelSettings, pressedCount;
    private SharedPreferences memory;
    private  DAL dal;

    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            setEnabled(false);
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int milliseconds = (int)(millis - seconds*1000);
            seconds = seconds % 60;

            recentResultTextView.setText(String.format("%d:%03d", seconds, milliseconds));
            timerHandler.postDelayed(this, 100);
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
                if(running){
                    recentButton.setText(R.string.recentResultButtonText);
                    setEnabled(true);
                    timerHandler.removeCallbacks(timerRunnable);
                    running = false;
                }
                else{
                recentButton.setText("Current time:");
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                myView.invalidate();
                    running= true;
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
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myView.invalidate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        int level = memory.getInt("level", MIN_LEVEL);
        int complexity = memory.getInt("complexity", MIN_COMPLEXITY);
        dal.saveTimes(level, complexity, currentResult, bestResult);
    }

    @Override
    public void pressed() {
        pressedTime = memory.getInt("level", MIN_LEVEL);
        pressedCount++;
        Toast.makeText(this,""+pressedCount+" out of "+pressedTime, Toast.LENGTH_SHORT).show();

        if(pressedCount==pressedTime){
        timerHandler.removeCallbacks(timerRunnable);
        setEnabled(true);
        recentButton.setText("Recent result");
        Toast.makeText(this,"Stopped", Toast.LENGTH_LONG).show();

            currentResult = System.currentTimeMillis() - startTime;
        if(first || currentResult < bestResult)
        {
            bestResultTextView.setText(recentResultTextView.getText());
            bestResult = currentResult;
            first = false;
        }

        }
        else
            myView.invalidate();



    }
    private void setEnabled(boolean enabled){
        pressedCount=0;
        settingsButton.setEnabled(enabled);
        recentButton.setEnabled(enabled);
        if(enabled){
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
        bestResultTextView.setText("" + dal.getBestTime(level, complexity));
        recentResultTextView.setText("" + dal.getRecentTime(level, complexity));

    }
    }
