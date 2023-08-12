package si.uni_lj.fe.seconddrivingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;
import java.util.function.Function;

public class WelcomeScreen extends AppCompatActivity {
    private int x;
    TextView textview;
    String textview2;

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    private static final String PREF_CHOOSE = "pref_choose";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);


        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int totalQuest = prefs.getInt("counter", 0);
        totalQuest = totalQuest / 2;

        if(totalQuest >= 10){
            Intent i2 = new Intent(WelcomeScreen.this, EndingActivity.class);
            startActivity(i2);
            finish();
        }
        else{

            final long Start = System.currentTimeMillis();

            firebaseDatabase = FirebaseDatabase.getInstance("https://second-driving-test-d0453-default-rtdb.europe-west1.firebasedatabase.app");
            reference = firebaseDatabase.getReference("1)a) Čas zahtevka");
            String key = reference.push().getKey();

            textview = (TextView) findViewById(R.id.textTitle);
            textview2 = "T A U V E T";


            textview.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(textview2);
                }
            }, 1500);

            prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int timeToAsk = prefs.getInt(PREF_CHOOSE, 0);

            if(timeToAsk == 0){
                x = new Random().nextInt(60000) + 60000;
            } else{
                x = new Random().nextInt(20000) + 10000;
            }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(WelcomeScreen.this, MainActivity.class);
                    startActivity(i);
                    long End = System.currentTimeMillis();
                    long timeElapsed = (End - Start);
                    reference.child(key).setValue(timeElapsed);

                    finish();
                }
            }, x);

            RunAnimation();
        }
    }

    private void RunAnimation()
    {
        Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fadein);
        fadein.reset();
        textview.clearAnimation();
        textview.startAnimation(fadein);
    }
/*
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clear shared preferences
        clearSharedPreferences();
    }

    private void clearSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
*/
}

