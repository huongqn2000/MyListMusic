package com.example.myappmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //    ListView listView_casi;
    ArrayList<Song> arraySong;
    TextView txtTitle,TxtTime;
    Button btnPlay,btnPause,btnChonBai;
    int position = 0;
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        AnhXa();
        AddSong();
        mediaPlayer = MediaPlayer.create(MainActivity.this, arraySong.get(position).getFile());
        txtTitle.setText(arraySong.get(position).getTitle());
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
            }
        });
        btnChonBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                startActivityForResult(new Intent(this, Activity_Song.class), position);
            }
        });
    }

    private void AddSong() {
        arraySong = new ArrayList<>();
        arraySong.add(new Song("Bài 1",R.raw.bai-1));
        arraySong.add(new Song("Bài 2",R.raw.bai-2));
        arraySong.add(new Song("Bài 3",R.raw.bai-3));
        arraySong.add(new Song("Bài 4",R.raw.bai-4));
        arraySong.add(new Song("Bài 5",R.raw.bai-5));
        arraySong.add(new Song("Bài 6",R.raw.bai-6));
    }

    private void AnhXa() {
        txtTitle = (TextView) findViewById(R.id.textviewTitle);
        TxtTime = (TextView) findViewById(R.id.textviewTime);
        btnPlay = (Button) findViewById(R.id.Play);
        btnPause = (Button) findViewById(R.id.Pause);
        btnChonBai = (Button) findViewById(R.id.chonbai);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == position && resultCode == RESULT_OK){

            int resID = data.getIntExtra(Activity_Song.EXTRA_RES_ID_MUSIC, 0);
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(this, resID);

            Time time = new Time(0);
            time.setTime(mediaPlayer.getDuration());

            Resources resources = getResources();
            Uri uri = new Uri.Builder()
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(resources.getResourcePackageName(resID))
                    .appendPath(resources.getResourceTypeName(resID))
                    .appendPath(resources.getResourceEntryName(resID))
                    .build();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, uri);
            txtTitle.setText(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        }
    }
}
