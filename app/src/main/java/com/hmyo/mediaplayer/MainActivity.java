package com.hmyo.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;//Media Player referansı
    private Button playButton;
    private SeekBar musicSeekBar;

    private Runnable runnable;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mediaPlayer = new MediaPlayer();//Media Player nesnesi oluşturduk
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.muzik); // Media Player'a mp3 tanımlandı

        playButton = (Button) findViewById(R.id.playButton);
        musicSeekBar = (SeekBar)findViewById(R.id.musicSeekBar);
        musicSeekBar.setMax(mediaPlayer.getDuration());

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
               //if (mediaPlayer != null && mediaPlayer.isPlaying()) { // mediaPlayer null değil ve çalışıyorsa
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    musicSeekBar.setProgress(currentPosition);
               //}
                handler.postDelayed(this, 100); // Runnable'ı 1 saniye sonra tekrar çağır
            }
        };

        handler.post(runnable);



        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    pauseMusic();
                }else{
                    playMusic();
                }

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int sure = mp.getDuration();
                String sureString = String.valueOf(sure / 1000);
                Toast.makeText(getApplicationContext(),"Müzik Süresi = " + sureString, Toast.LENGTH_LONG).show();
                playButton.setText("Play");
            }
        });





    }

    @Override
    protected void onDestroy() {

        if(mediaPlayer !=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();
    }

    private void pauseMusic(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
            playButton.setText("Play");
            handler.removeCallbacks(runnable); // Seek bar güncellemesini durdur
        }


    }
    private void playMusic(){
        if(mediaPlayer!=null){
            mediaPlayer.start();
            playButton.setText("Pause");
            handler.post(runnable); // Seek bar güncellemesini başlat
        }
    }



}