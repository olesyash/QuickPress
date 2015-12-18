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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SettingsActivity extends AppCompatActivity {
    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 10;
    private static final int MIN_COMPLEXITY = 0;
    private static final int MAX_COMPLEXITY = 4;

    private EditText levelTextView, complexityTextView;
    private Context context;
    private SharedPreferences memory;
    private SharedPreferences.Editor edit;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        memory = getSharedPreferences("setting", MODE_PRIVATE);

        levelTextView = (EditText)findViewById(R.id.levelValueTextView);
        complexityTextView = (EditText)findViewById(R.id.complexityValueTextView);
        saveButton = (Button)findViewById(R.id.saveButton);
        String levelString = "" + memory.getInt("level", MIN_LEVEL);
        levelTextView.setText(levelString);
        String comlexityString = ""+memory.getInt("complexity", MIN_COMPLEXITY);
        complexityTextView.setText(comlexityString);
        context = this;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
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

    private void saveData()
    {
        int level, complexity;
        edit = memory.edit();
        level = Integer.parseInt(levelTextView.getText().toString());
        complexity = Integer.parseInt(complexityTextView.getText().toString());
        if(level < MIN_LEVEL || level > MAX_LEVEL) {
            Toast.makeText(context, "Level has to be between " + MIN_LEVEL + " to " + MAX_LEVEL, Toast.LENGTH_SHORT).show();
            levelTextView.setText("" + memory.getInt("level", MIN_LEVEL));
        }
        else
        {
            edit.putInt("level", level);
        }
        if(complexity < MIN_COMPLEXITY || complexity > MAX_COMPLEXITY) {
            Toast.makeText(context, "Complexity has to be between " + MIN_COMPLEXITY + " to " + MAX_COMPLEXITY, Toast.LENGTH_SHORT).show();
            complexityTextView.setText(""+ memory.getInt("complexity", MIN_COMPLEXITY));
        }
        else
        {
            edit.putInt("complexity", complexity);
        }
        Toast.makeText(context, "Settings saved", Toast.LENGTH_SHORT).show();
        edit.apply();
    }
}
