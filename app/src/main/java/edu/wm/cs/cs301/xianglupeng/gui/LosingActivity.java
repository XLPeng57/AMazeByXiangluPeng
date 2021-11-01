package edu.wm.cs.cs301.xianglupeng.gui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.xianglupeng.R;

public class LosingActivity extends AppCompatActivity {


    private String LogMessage = "LosingActivity";
    MediaPlayer song;

    /** Method: OnCreate
     *  Create the losing page for the game,
     *  get extras from previous activity
     *  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);


        Intent prevIntent = getIntent();
        String path = prevIntent.getStringExtra("Path");
        String ShortestPath = prevIntent.getStringExtra("Shortest");
        String energyCon = prevIntent.getStringExtra("Energy");

        TextView shortest = findViewById(R.id.shortest);
        TextView pathUsed = findViewById(R.id.pathused);
        TextView energy = findViewById(R.id.energy);
        shortest.setText(ShortestPath);
        pathUsed.setText(path);
        energy.setText(energyCon);

        song = MediaPlayer.create(LosingActivity.this,R.raw.lose);
        song.start();

    }

    /** Method: onBackPressed
     *  Back to title page
     */
    public void onBackPressed(){
        Intent intent = new Intent(this, AMazeActivity.class);
        Log.v(LogMessage, "Go to the title page");
        song.release();
        startActivity(intent);
        finish();
    }


    /** Method: Back
     *  Back to title page, play again
     */
    public void back(View view){
        Intent intent = new Intent(this, AMazeActivity.class);
        Log.v(LogMessage, "Go to the title page");
        song.release();
        startActivity(intent);
        finish();
    }
}
