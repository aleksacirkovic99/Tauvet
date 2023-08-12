package si.uni_lj.fe.seconddrivingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class StartingActivity extends AppCompatActivity {

    private static final String PREF_START_TIME = "pref_start_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long startTime = System.currentTimeMillis();
        prefs.edit().putLong(PREF_START_TIME, startTime).apply();


        Intent i = new Intent(StartingActivity.this, WelcomeScreen.class);
        startActivity(i);
        finish();


    }
}