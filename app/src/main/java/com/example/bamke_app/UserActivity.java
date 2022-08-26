package com.example.bamke_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class UserActivity extends AppCompatActivity {
    TextView saludoTextView;
    ImageButton logoutbtn;
    ImageButton btnStartPause;
    ImageButton btnStop;
    ImageButton btnReset;
    long segundos;
    private TextView tiempo;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    long mTimeLeftInMillis = segundos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        logoutbtn = findViewById(R.id.logoutbtn);
        saludoTextView = findViewById(R.id.mensaje);
        tiempo = findViewById(R.id.tiempo);
        btnStartPause= findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        RadioButton rbtn1 = findViewById(R.id.rbtn1);
        RadioButton rbtn2 = findViewById(R.id.rbtn2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user!=null){
            saludoTextView.setVisibility(View.VISIBLE);
        }

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserActivity.this,MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });



        /*
        Acontinuacion se codifican los metodos para realizar el conteo regresivo
         del pomodoro
         */

        rbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segundos = 900;
                updateCountDownText();

            }
        });


        rbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                segundos = 1200;
                updateCountDownText();
            }
        });
        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        updateCountDownText();
    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                btnStartPause.setVisibility(View.INVISIBLE);
                btnStop.setVisibility(View.VISIBLE);

            }
        }.start();

        mTimerRunning = true;
        btnStartPause.setImageDrawable(Drawable.createFromPath("mipmap-xhdpi/pause.png"));
        btnStop.setVisibility(View.INVISIBLE);
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        btnStartPause.setImageDrawable(Drawable.createFromPath("mipmap-xhdpi/play.png"));
        btnStop.setVisibility(View.VISIBLE);
    }

    private void resetTimer() {
        mTimeLeftInMillis = segundos;
        updateCountDownText();
        btnStop.setVisibility(View.INVISIBLE);
        btnStartPause.setVisibility(View.VISIBLE);
    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        tiempo.setText(timeLeftFormatted);
    }
}