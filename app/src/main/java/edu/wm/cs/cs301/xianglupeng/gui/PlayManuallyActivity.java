package edu.wm.cs.cs301.xianglupeng.gui;



import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.xianglupeng.R;
import edu.wm.cs.cs301.xianglupeng.generation.Maze;
import edu.wm.cs.cs301.xianglupeng.generation.Singleton;

public class PlayManuallyActivity extends AppCompatActivity {


    private MazePanel mazePanel;
    private Maze maze;
    StatePlaying playing;
    public int path;
    public int shortestPath;
    public int energyConsumed;
    private String str_energy;
    private Context context;
    private ImageButton Left, Right, Forward;
    private ToggleButton Wall, Map, Solution;
    private Button zoomIn, zoonOut;
    private String LogMessage = "ManuallyActivity";
    MediaPlayer song;

    /** Method: OnCreate
     *  Create the manually playing page of the game
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmanually);

        context = getApplicationContext();



        //project 7
        mazePanel = findViewById(R.id.mazePanel);
        maze = Singleton.getMaze();
        playing = new StatePlaying();
        playing.setMazeConfiguration(maze);
        playing.setPlayManuallyActivity(this);
        playing.setMazePanel(mazePanel);
        playing.start(mazePanel);

        path = 0;
        int[] start_pos = maze.getStartingPosition();
        shortestPath = maze.getDistanceToExit(start_pos[0],start_pos[1]);
        energyConsumed = 0;

        Wall = findViewById(R.id.toggleButton3);
        Wall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean bool) {
                if(bool){
                    Log.v(LogMessage, "Show walls");
                    playing.keyDown(Constants.UserInput.ToggleFullMap, 1);
//                    Toast.makeText(getApplicationContext(), "Wall Button on", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.v(LogMessage, "Hide walls");
                    playing.keyDown(Constants.UserInput.ToggleFullMap, 1);
//                    Toast.makeText(getApplicationContext(), "Wall Button off", Toast.LENGTH_LONG).show();
                }
            }
        });




        Map = findViewById(R.id.toggleButton4);
        Map.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean bool) {
                if(bool){
                    Log.v(LogMessage, "Show walls");
                    playing.keyDown(Constants.UserInput.ToggleLocalMap, 1);
//                    Toast.makeText(getApplicationContext(), "Wall Button on", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.v(LogMessage, "Hide walls");
                    playing.keyDown(Constants.UserInput.ToggleLocalMap, 1);
//                    Toast.makeText(getApplicationContext(), "Wall Button off", Toast.LENGTH_LONG).show();
                }
            }
        });


        Solution = findViewById(R.id.toggleButton2);
        Solution.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean bool) {
                if(bool){
                    Log.v(LogMessage, "Show walls");
                    playing.keyDown(Constants.UserInput.ToggleSolution, 1);
//                    Toast.makeText(getApplicationContext(), "Solution Button on", Toast.LENGTH_LONG).show();
                }
                else{
                    Log.v(LogMessage, "Hide walls");
                    playing.keyDown(Constants.UserInput.ToggleSolution, 1);
//                    Toast.makeText(getApplicationContext(), "Solution Button off", Toast.LENGTH_LONG).show();
                }
            }
        });

        Forward = findViewById(R.id.imageButton2);
        Forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LogMessage, "Going forward.");
                playing.keyDown(Constants.UserInput.Up,1);
                energyConsumed = energyConsumed + 6;
                path++;
            }
        });


        Left = findViewById(R.id.imageButton);
        Left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LogMessage, "Turning left.");
                playing.keyDown(Constants.UserInput.Left,1);
                energyConsumed = energyConsumed + 3;
            }
        });

        Right = findViewById(R.id.imageButton3);
        Right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LogMessage, "Turning right.");
                playing.keyDown(Constants.UserInput.Right,1);
                energyConsumed = energyConsumed + 3;
            }
        });

        zoomIn = findViewById(R.id.button);
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LogMessage, "Zoom In.");
                playing.keyDown(Constants.UserInput.ZoomIn,1);
            }
        });

        zoonOut = findViewById(R.id.button2);
        zoonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(LogMessage, "Zoom In.");
                playing.keyDown(Constants.UserInput.ZoomOut,1);
            }
        });

        song = MediaPlayer.create(PlayManuallyActivity.this,R.raw.music1);
        song.start();
    }

    /** Method: sendMessage
     *  navigate to winning page
     */
    public void towin() {
        // Do something in response to button click
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("Shortest", String.valueOf(shortestPath));
        intent.putExtra("Path", String.valueOf(path));
        intent.putExtra("Energy", String.valueOf(energyConsumed));
        Log.v(LogMessage,"Path Used: " + path);
        Log.v(LogMessage,"Shortest Path: " + shortestPath);
        song.release();
//        Toast.makeText(context, "Path Used" + path + "\n Shortest Path: " + shortestPath, Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();

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






}
