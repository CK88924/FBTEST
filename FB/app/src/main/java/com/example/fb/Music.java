package com.example.fb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class Music extends AppCompatActivity {
 private MediaPlayer mediaPlayer = new MediaPlayer();
 private Button stop_music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        setTitle("音樂");
        play_music();

        stop_music = (Button)findViewById(R.id.stop_music);
        stop_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){ mediaPlayer.pause(); }
            }
        });

    }//onCreate()

    private void  play_music() {
        AssetManager assetManager = this.getApplicationContext().getAssets();
        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = assetManager.openFd("01.wav");
        }//try1()
        catch (IOException e) {
            e.printStackTrace();
        }//catch1()

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),fileDescriptor.getLength());
        }//try2()

        catch (IOException e) {
            e.printStackTrace();
        }//catch2()

        try {
            mediaPlayer.prepare();
        }//try3()
        catch (IOException e) {
            e.printStackTrace();
        }//catch3()

        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }//play_music
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }
}