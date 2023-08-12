package si.uni_lj.fe.seconddrivingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Button buttonCan;
    private Button buttonAcc;

    private static final String PREF_CHOOSE = "pref_choose";

    TextView textView2;
    private int x = 1;

    DatabaseReference reference, ref2;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final long Start = System.currentTimeMillis();

        firebaseDatabase = FirebaseDatabase.getInstance("https://second-driving-test-d0453-default-rtdb.europe-west1.firebasedatabase.app");
        reference = firebaseDatabase.getReference("2)a) Čas potreben za odgovor zahtevka");
        ref2 = firebaseDatabase.getReference("2)b) Vrednost odgovora");
        String key = reference.push().getKey();
        String key2 = ref2.push().getKey();


        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.test4000_hz_tone);
        float volume = (float) (1 - (Math.log(100 - 65) / Math.log(100)));
        mediaPlayer.setVolume(volume, volume);
        mediaPlayer.start();


        buttonCan = (Button) findViewById(R.id.button3);
        buttonCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long End = System.currentTimeMillis();
                long timeElapsed = (End - Start);
                reference.child(key).setValue(timeElapsed);
                ref2.child(key2).setValue("1");
                prefs.edit().putInt(PREF_CHOOSE, 1).apply();
                returnToStart();
            }
        });


        buttonAcc = (Button) findViewById(R.id.button);
        buttonAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long End = System.currentTimeMillis();
                long timeElapsed = (End - Start);
                reference.child(key).setValue(timeElapsed);
                ref2.child(key2).setValue("0");
                prefs.edit().putInt(PREF_CHOOSE, 0).apply();
                goToQuestions();
            }
        });

        RunAnimation();

    }

    public void returnToStart(){
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }

    public void goToQuestions(){
        Intent intent = new Intent(this, Questions.class);
        startActivity(intent);
    }


    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        textView2 = (TextView) findViewById(R.id.text3);
        textView2.clearAnimation();
        textView2.startAnimation(a);

        buttonCan.clearAnimation();
        buttonCan.startAnimation(a);

        buttonAcc.clearAnimation();
        buttonAcc.startAnimation(a);
    }



}