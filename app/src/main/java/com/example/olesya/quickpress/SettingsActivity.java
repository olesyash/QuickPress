package com.example.olesya.quickpress;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {
    static final int MAX_LEVEL = 10;

    TextView levelTextView, complexityTextView;
    Context context;
    SharedPreferences memory;
    SharedPreferences.Editor edit;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        memory = getSharedPreferences("setting", MODE_PRIVATE);
        edit = memory.edit();
        levelTextView = (TextView)findViewById(R.id.levelValueTextView);
        complexityTextView = (TextView)findViewById(R.id.complexityValueTextView);
        saveButton = (Button)findViewById(R.id.saveButton);
        levelTextView.setText(""+memory.getInt("level",1));
        complexityTextView.setText(""+memory.getInt("complexity",0));
        context = this;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.putInt("level", Integer.parseInt(levelTextView.getText().toString()));
                edit.putInt("complexity", Integer.parseInt(complexityTextView.getText().toString()));
                edit.apply();
            }
        });

        levelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
