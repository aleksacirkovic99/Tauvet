package si.uni_lj.fe.seconddrivingtest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EndingActivity extends AppCompatActivity {

    DatabaseReference ref1;
    FirebaseDatabase firebaseDatabase;

    TextView textView;

    private static final String PREF_START_TIME = "pref_start_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);

        textView = (TextView) findViewById(R.id.textView);

        firebaseDatabase = FirebaseDatabase.getInstance("https://second-driving-test-d0453-default-rtdb.europe-west1.firebasedatabase.app");
        ref1 = firebaseDatabase.getReference("4) Čas potreben za odgovoriti na vseh vprašanj");
        String key = ref1.push().getKey();

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        long startTime = prefs.getLong(PREF_START_TIME, 0);

        if (startTime != 0) {
            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            String time = formatTime(timeElapsed);
            ref1.child(key).setValue(time);
            textView.setText("Time elapsed: " + time + " ms");
        } else {
            textView.setText("Start time not found in SharedPreferences");
        }

        prefs.edit().clear().apply();
        this.finishAffinity();
    }


    public static String formatTime(long millis) {
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        long milliseconds = millis % 1000;
        return String.format("%02d:%02d.%03d", minutes, seconds, milliseconds);
    }


}
