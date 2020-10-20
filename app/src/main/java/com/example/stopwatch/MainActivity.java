package com.example.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {
    int hr=0,min=0,sec=0,centiSec=0;
    boolean play = true;
    TextView tvStatus;
    FloatingActionButton playButton;
    FloatingActionButton stopButton;
    CounterTask cTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tvStatus);
        playButton = findViewById(R.id.playButton);
        stopButton = findViewById(R.id.stopButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play){
                    cTask = new CounterTask();
                    cTask.execute();
                    play = false;
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }else{
                    cTask.cancel(true);
                    play=true;
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cTask!=null)
                cTask.cancel(true);
                tvStatus.setText("00:00.00");
                playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                play=true;
                hr=min=sec=centiSec=0;
            }
        });
    }

    class CounterTask extends AsyncTask<Integer,Integer,Void>{
        @Override
        protected Void doInBackground(Integer... integers) {
            while(!isCancelled()){
                waitOneCentiSec();
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            centiSec++;

            if(centiSec==100){
                sec++;
                centiSec=0;
            }

            if(sec==60){
                min++;
                sec=0;
            }

            if(min==60){
                hr++;
                min=0;
            }

            String str="";

            if(hr>0){
                if(hr<10)
                str+="0"+hr+":";
                else str+=hr+":";
            }

            if(min<10){
                str+="0"+min;
            }else str+=min;

            if(sec<10){
                str+=":0"+sec;
            }else str+=":"+sec;

            if(centiSec<10){
                str+=".0"+centiSec;
            }else str+="."+centiSec;

            if(!isCancelled())
            tvStatus.setText(str);
        }

        protected void waitOneCentiSec(){
            long start = System.currentTimeMillis();
            while(System.currentTimeMillis()<start+10);
        }
    }
}