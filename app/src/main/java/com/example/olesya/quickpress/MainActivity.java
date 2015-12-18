package com.example.olesya.quickpress;

import android.content.Context;
import android.content.Intent;
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
    private Button settingsButton, startButton, recentButton;
    private Context context;
    private MyView myView;
    private TextView timerTextView, bestResultTextView;
    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private long bestResult = 0;
    private boolean first = true;
    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int milliseconds = (int)(millis - seconds*1000);
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%03d", seconds, milliseconds));
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
        timerTextView = (TextView) findViewById(R.id.recentResultTextView);
        recentButton = (Button) findViewById(R.id.recentResultButton);
        bestResultTextView = (TextView)findViewById(R.id.bestResultTextView);


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
                recentButton.setText("Current time:");
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                myView.invalidate();
            }
        });


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
    public void pressed() {
        timerHandler.removeCallbacks(timerRunnable);
        recentButton.setText("Recent result");
        Toast.makeText(this,"Stopped", Toast.LENGTH_LONG).show();
        long millis = System.currentTimeMillis() - startTime;
        if(first || millis < bestResult)
        {
            bestResultTextView.setText(timerTextView.getText());
            bestResult = millis;
            first = false;
        }


    }
}
