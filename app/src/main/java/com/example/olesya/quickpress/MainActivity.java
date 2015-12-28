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
import android.os.Handler;


public class MainActivity extends AppCompatActivity implements GameInterface{
    //Define constants
    private static final int MIN_LEVEL = 1, MIN_COMPLEXITY = 0;
    private static final int DELAY = 100 ;
    private static final int DB_ERROR = -1, INIT_TIME = 0;
    private static final int THOUSAND = 1000, MINUTE = 60;
    //Buttons
    private Button settingsButton, startButton, recentButton, bestButton;
    //Text Views
    private TextView recentResultTextView, bestResultTextView;
    //Define variables
    private Context context;
    private MyView myView;
    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private long bestResult = 0, currentResult = 0;
    private boolean first, running = false;
    private int requiredNumOfClicks, pressedCount;
    private SharedPreferences memory;
    private DAL dal;
    private int level,complexity;

    //Define thread for the timer. Will sleep for DELAY time = 100ms
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            setEnabled(false); //Disable buttons
            timerHandler.postDelayed(this, DELAY); // go to sleep
            long millis = (long)(System.currentTimeMillis() - startTime); //Calculate the time that passed
            recentResultTextView.setText(formatStringTime(millis)); //Use Internal function to show time
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Initialize variables
        context = this;
        memory = getSharedPreferences("setting", MODE_PRIVATE);
        dal = new DAL(context); //Create dal object to use DB
        initViews(); //Initialize Views
        getLevelAndComplexity(); //Get level and complexity from shared memory
        voidAddListeners(); //Use private function to set Listeners to Buttons
        restoreResults(); //Read saved results from DB

    }
    //Function used to get level and complexity from shared memory
    private void getLevelAndComplexity(){
        level = memory.getInt("level", MIN_LEVEL);
        complexity = memory.getInt("complexity", MIN_COMPLEXITY);
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
            Intent intent = new Intent(context, SettingsActivity.class); //Goto settings Activity
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myView.bttn_pressed = false;
        myView.invalidate();
        restoreResults();
    }

    @Override
    protected void onPause(){
        super.onPause();
        setEnabled(true);
    }

    @Override
    public void pressed() {
        //if running = true - the game started
        if(running){
            requiredNumOfClicks = memory.getInt("level", MIN_LEVEL);
            pressedCount++;

            //Check if number of clicks is the required number
            if(pressedCount == requiredNumOfClicks)
            {
                timerHandler.removeCallbacks(timerRunnable); //Stop the timer
                currentResult = System.currentTimeMillis() - startTime; //Calculate the time that passed

                recentResultTextView.setText(formatStringTime(currentResult));
                if( first|| (currentResult < bestResult)) //Update best result if got better result
                {
                    bestResult = currentResult;
                    bestResultTextView.setText(formatStringTime(bestResult));
                    first = false;
                }
                getLevelAndComplexity();
                dal.saveTimes(level, complexity, currentResult, bestResult); //Save in DB the results
                recentButton.setText(R.string.recentResultButtonText); //Change the button string
                setEnabled(true); //Enable buttons
            }
            else { //Still playing
                myView.invalidate(); //Generate new places for square and circles
        }
        }
    }

    //Functions to enable or disable buttons and update their string depending on game state
    private void setEnabled(boolean enabled){
        settingsButton.setEnabled(enabled);
        bestButton.setEnabled(enabled);
        if(enabled){  //  Game state = hold - waiting for user to start playing
            pressedCount = 0; //Reset clicks counter
            startButton.setText(R.string.start);
            timerHandler.removeCallbacks(timerRunnable);
            running = false;
        }
        else { // Games state = playing
            startButton.setText(R.string.stop);
        }
    }

    //Function to restore results from DB and show them
    private void restoreResults()
    {
        getLevelAndComplexity();
        bestResult = dal.getBestTime(level, complexity);
        currentResult = dal.getRecentTime(level, complexity);

        if(bestResult == DB_ERROR || currentResult == DB_ERROR) { //One of the lines missing in DB
            first = true; //Flag that shows us it's the first time for current game (level+complexity)
            bestResult = INIT_TIME;
            currentResult = INIT_TIME;
        }
        //Update text views to show the results
            bestResultTextView.setText(formatStringTime(bestResult));
            recentResultTextView.setText(formatStringTime(currentResult));
    }

    //Function to format time to string in following format: ss:xxx xxx = ms
    private String formatStringTime(long millis)
    {
        int seconds = (int) (millis / THOUSAND);
        int milliseconds = (int)(millis - seconds*THOUSAND);
        seconds = seconds % MINUTE;
        return String.format("%d:%03d", seconds, milliseconds);
    }

    //Function to initialize views
    private void initViews()
    {
        settingsButton = (Button)findViewById(R.id.settingsButton);
        startButton = (Button)findViewById(R.id.startButton);
        myView = (MyView)findViewById(R.id.myView);
        recentResultTextView = (TextView) findViewById(R.id.recentResultTextView);
        recentButton = (Button) findViewById(R.id.recentResultButton);
        bestResultTextView = (TextView)findViewById(R.id.bestResultTextView);
        bestButton = (Button)findViewById(R.id.bestResultButton);
    }

    //Functions to add Listeners to Buttons
    private void voidAddListeners()
    {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If settings button pressed - > goto settings Activity
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If start button pressed check status
                if (running) { //if status = playing and start pressed, stop the game
                    recentButton.setText(R.string.recentResultButtonText); //Update string Button to "Recent Result"
                    setEnabled(true); //Enable buttons
                } else { //if status = hold -> start game
                    myView.bttn_pressed = true;
                    recentButton.setText(R.string.current_time); //Update string Button to "Current Result"
                    startTime = System.currentTimeMillis(); //Save start time to calculate running time later
                    timerHandler.postDelayed(timerRunnable, INIT_TIME); // Start timer
                    myView.invalidate(); //Refresh the game canvas
                    running = true;
                }
            }
        });
        
        bestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If Best Time button pressed, reset the best time
                first = true;
                bestResultTextView.setText(formatStringTime(INIT_TIME)); //Set to zero
                //Update in DB the suitable line to Best Time = 0
                getLevelAndComplexity();
                dal.saveTimes(level, complexity, currentResult, DB_ERROR);
            }
        });
    }
}

