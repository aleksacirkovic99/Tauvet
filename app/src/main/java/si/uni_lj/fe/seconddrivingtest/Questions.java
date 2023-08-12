package si.uni_lj.fe.seconddrivingtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import si.uni_lj.fe.seconddrivingtest.Model.Question;

public class Questions extends AppCompatActivity {
    private Button buttonAnsw1;
    private Button buttonAnsw2;
    private Button buttonAnsw3;
    private Button buttonAnsw4;

    //private int i = new Random().nextInt(4) + 1;

    TextView questTxt;
    TextView textview2;

    int numOfQuestions = 5;
    ArrayList<Integer> randomNumbers;
    int questionNumber;
    int totalQuest = 0;
    int correct = 0;
    int wrong = 0;
    DatabaseReference reference, ref1, ref2, ref3;
    FirebaseDatabase firebaseDatabase;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        firebaseDatabase = FirebaseDatabase.getInstance("https://second-driving-test-d0453-default-rtdb.europe-west1.firebasedatabase.app");
        ref1 = firebaseDatabase.getReference("3)a) Čas potreben za odgovor vprašanja");
        ref2 = firebaseDatabase.getReference("3)b) Vrednost odgovora");
        ref3 = firebaseDatabase.getReference("3)c) Pravilnost odgovora");
        String key = ref1.push().getKey();
        String key2 = ref2.push().getKey();
        String key3 = ref3.push().getKey();

        buttonAnsw1 = (Button) findViewById(R.id.button2);
        buttonAnsw2 = (Button) findViewById(R.id.button4);
        buttonAnsw3 = (Button) findViewById(R.id.button5);
        buttonAnsw4 = (Button) findViewById(R.id.button6);
        questTxt = (TextView) findViewById(R.id.txtQuest);

        newQuestion();

        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            String j =(String) b.get("correct");
            int k = Integer.parseInt(j);

            String Str = (String) b.get("time");
            long Start = Long.parseLong(Str);
            long End = System.currentTimeMillis();
            long timeElapsed = (End - Start);
            ref1.child(key).setValue(timeElapsed);

            if (k % 2 == 1){
                if(k == 1)
                {
                    ref2.child(key2).setValue("0");
                }
                if(k == 3)
                {
                    ref2.child(key2).setValue("1");
                }
                if(k == 5)
                {
                    ref2.child(key2).setValue("2");
                }
                if(k == 7)
                {
                    ref2.child(key2).setValue("3");
                }
                ref3.child(key3).setValue("0");
                returnToStart();
            }else if (k % 2 == 0){
                if(k == 2)
                {
                    ref2.child(key2).setValue("0");
                }
                if(k == 4)
                {
                    ref2.child(key2).setValue("1");
                }
                if(k == 6)
                {
                    ref2.child(key2).setValue("2");
                }
                if(k == 8)
                {
                    ref2.child(key2).setValue("3");
                }
                ref3.child(key3).setValue("1");
                returnToStart();
            }
        }

    }


    public void returnToStart(){
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
    }


    public ArrayList<Integer> generateArray(int count) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isFunctionRun = prefs.getBoolean("isFunctionRun", false);

        if (!isFunctionRun) {
            ArrayList<Integer> numbers = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                numbers.add(i);
            }
            Collections.shuffle(numbers, new Random());
            ArrayList<Integer> randomNumbers = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                int num = numbers.get(i);
                randomNumbers.add(num);
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFunctionRun", true);
            editor.putInt("randomNumbersSize", randomNumbers.size());
            for (int i = 0; i < randomNumbers.size(); i++) {
                editor.putInt("randomNumber" + i, randomNumbers.get(i));
            }
            editor.apply();

            return randomNumbers;
        } else {
            ArrayList<Integer> randomNumbers = new ArrayList<>();
            int size = prefs.getInt("randomNumbersSize", 0);
            for (int i = 0; i < size; i++) {
                int num = prefs.getInt("randomNumber" + i, 0);
                randomNumbers.add(num);
            }
            return randomNumbers;
        }
    }

    private void newQuestion(){
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        totalQuest = prefs.getInt("counter", 0) + 1;

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("counter", totalQuest);
        editor.apply();

        totalQuest = (totalQuest - 1)/2;

        if(totalQuest >= 10){
            Intent i2 = new Intent(Questions.this, EndingActivity.class);
            startActivity(i2);
            finish();
        }
        else{
            //randomNumbers = generateArray(numOfQuestions);
            //questionNumber = randomNumbers.get(totalQuest);
            reference = firebaseDatabase.getReference().child("Questions").child(String.valueOf(totalQuest + 1));
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Question question = snapshot.getValue(Question.class);

                    questTxt.setText(question.getQuestion());
                    buttonAnsw1.setText(question.getOption1());
                    buttonAnsw2.setText(question.getOption2());
                    buttonAnsw3.setText(question.getOption3());
                    buttonAnsw4.setText(question.getOption4());

                    final long Start = System.currentTimeMillis();
                    String str = Long.toString(Start);

                    buttonAnsw1.setOnClickListener(view -> {
                        if(buttonAnsw1.getText().toString().equals(question.getAnswer())){
                            correct++;
                            String s = "1";
                            Intent ii = new Intent(Questions.this, Questions.class);
                            ii.putExtra("correct", s);
                            ii.putExtra("time", str);
                            startActivity(ii);
                        }
                        else{
                            wrong++;
                            String s = "2";
                            Intent ii = new Intent(Questions.this, Questions.class);
                            ii.putExtra("correct", s);
                            ii.putExtra("time", str);
                            startActivity(ii);
                        }
                    });

                    buttonAnsw2.setOnClickListener(view -> {
                        if(buttonAnsw2.getText().toString().equals(question.getAnswer())){
                            correct++;
                            String s = "3";
                            Intent ii = new Intent(Questions.this, Questions.class);
                            ii.putExtra("correct", s);
                            ii.putExtra("time", str);
                            startActivity(ii);
                        }
                        else{
                            wrong++;
                            String s = "4";
                            Intent ii = new Intent(Questions.this, Questions.class);
                            ii.putExtra("correct", s);
                            ii.putExtra("time", str);
                            startActivity(ii);
                        }
                    });

                    buttonAnsw3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(buttonAnsw3.getText().toString().equals(question.getAnswer())){
                                correct++;
                                String s = "5";
                                Intent ii = new Intent(Questions.this, Questions.class);
                                ii.putExtra("correct", s);
                                ii.putExtra("time", str);
                                startActivity(ii);
                            }
                            else{
                                wrong++;
                                String s = "6";
                                Intent ii = new Intent(Questions.this, Questions.class);
                                ii.putExtra("correct", s);
                                ii.putExtra("time", str);
                                startActivity(ii);
                            }
                        }
                    });

                    buttonAnsw4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(buttonAnsw4.getText().toString().equals(question.getAnswer())){
                                correct++;
                                String s = "7";
                                Intent ii = new Intent(Questions.this, Questions.class);
                                ii.putExtra("correct", s);
                                ii.putExtra("time", str);
                                startActivity(ii);
                            }
                            else{
                                wrong++;
                                String s = "8";
                                Intent ii = new Intent(Questions.this, Questions.class);
                                ii.putExtra("correct", s);
                                ii.putExtra("time", str);
                                startActivity(ii);
                            }
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Questions.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}