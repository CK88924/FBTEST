package com.example.fb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Date;

public class Fcm extends AppCompatActivity {
    private Button sent;
    private EditText data;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int id = 0;
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fcm);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("DATA");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if( snapshot.exists() ){
                    id = (int) snapshot.getChildrenCount();
                }//if()

            }//onDataChange()

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }//onCancelled()
        });//ValueEventListener()

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                String data_str = data.getText().toString();
                if(data_str.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Please enter data",Toast.LENGTH_LONG).show();
                }//if(空值)
                else{
                    notification();
                }//else()
            }//onChildAdded()

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }//onChildChanged()

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }//onChildRemoved()

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }// onChildMoved()

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }//onCancelled()
        });//ChildEventListener()

        data = (EditText) findViewById(R.id.data);
        sent = (Button)findViewById(R.id.sent);
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data_str = data.getText().toString();
                if(data_str.isEmpty() ){
                    Toast.makeText(getApplicationContext(),"Please enter data",Toast.LENGTH_LONG).show();
                }//if(空值)
                else{
                    Data a_data  = new Data(data.getText().toString());
                    databaseReference.child(String.valueOf(id+1)).setValue(a_data);
                }//else()

            }//onClick()
        });//btn_sent

    }//onCreate()

    public class Data {

        private  String data;




        public Data(String data){
            this.data = data;

        }

        public String getData() {
            return this.data = data;
        }
    }//Class Data()

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
    PendingIntent pendingIntent_function(){
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, Space.class), 0);
                mediaPlayer.stop();
                return pendingIntent;
    }//pendingIntent_function()

    private  void  notification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel  = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }//if()
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }//if(正在播放)

        play_music();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"n")
                .setContentTitle("New Notification")
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setAutoCancel(true)//點擊是否自動消失
                .setWhen(System.currentTimeMillis())//時間戳記：setWhen()方法提供在訊息欄上顯示系統時間
                //.setContentIntent(pendingIntent_function())//點擊事件
                //.setDeleteIntent(pendingIntent_function())滑動刪除事件
                .setContentText("New Notification Added:");
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        int code = (int) (new Date().getTime()/1000);
        notificationManagerCompat.notify(code,builder.build());


    }//notification()

    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
    }


}//main()