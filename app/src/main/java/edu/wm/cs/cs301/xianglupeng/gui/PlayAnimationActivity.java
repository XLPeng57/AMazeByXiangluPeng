package edu.wm.cs.cs301.xianglupeng.gui;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.xianglupeng.R;
import edu.wm.cs.cs301.xianglupeng.generation.CardinalDirection;
import edu.wm.cs.cs301.xianglupeng.generation.Maze;
import edu.wm.cs.cs301.xianglupeng.generation.Singleton;

public class PlayAnimationActivity extends AppCompatActivity {


    private String robot_select;
    private String driver_select;
    private int speed;
    private int path;
    private String str_path;
    private int shortestPath;
    private String str_short;
    private int energyConsumed;
    private String str_energy;
    private Context context;
    private String LogMessage = "AnimationActivity";
    private ToggleButton Start;
    private ProgressBar energyBar;

    private MazePanel mazePanel;
    private Maze maze;
    StatePlaying playing;
    private Robot robot;
    private RobotDriver driver;
    private Handler handler = new Handler();
    MediaPlayer song;

    /**
     * Method: OnCreate
     * Create the animation page of the game
     * gets extras from previous page
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playanimation);

        context = getApplicationContext();

        Button forward = findViewById(R.id.button_front);
        Button backward = findViewById(R.id.button_backward);
        Button left = findViewById(R.id.button_left);
        Button right = findViewById(R.id.button_right);


        Intent prev = getIntent();
        robot_select = prev.getStringExtra("robot");
        driver_select = prev.getStringExtra("driver");
        Log.v(LogMessage, "Driver received from AmazeActivity: " + driver_select);

        switch (driver_select) {
            case "Wizard":
                driver = new Wizard();
                break;

            case "WallFollower":
                driver = new WallFollower();
                break;

        }

//        switch (robot_select) {
//            case "Premium":
//                robot = new ReliableRobot();
//                break;
//            case "Mediocre":
//                robot = new UnreliableRobot('1', '0', '0', '1');
//                break;
//            case "Soso":
//                robot = new UnreliableRobot('0', '1', '1', '0');
//                break;
//            case "Shaky":
//                robot = new UnreliableRobot('0', '0', '0', '0');
//                break;
//
//        }

        if (robot_select.equalsIgnoreCase("Premium")) {
            Log.v(LogMessage,"Premium");
            robot = new ReliableRobot();
            left.setBackgroundColor(Color.rgb(47,143,21));
            right.setBackgroundColor(Color.rgb(47,143,21));
            forward.setBackgroundColor(Color.rgb(47,143,21));
            backward.setBackgroundColor(Color.rgb(47,143,21));
        }
        if (robot_select.equalsIgnoreCase("Mediocre")) {
            Log.v(LogMessage,"Mediocre");
            robot = new UnreliableRobot('1', '0', '0', '1');
            left.setBackgroundColor(Color.rgb(186,32,29));
            right.setBackgroundColor(Color.rgb(186,32,29));
            forward.setBackgroundColor(Color.rgb(47,143,21));
            backward.setBackgroundColor(Color.rgb(47,143,21));
        }
        if (robot_select.equalsIgnoreCase("Soso")) {
            Log.v(LogMessage,"Soso");
            robot = new UnreliableRobot('0', '1', '1', '0');
            left.setBackgroundColor(Color.rgb(47,143,21));
            right.setBackgroundColor(Color.rgb(47,143,21));
            forward.setBackgroundColor(Color.rgb(186,32,29));
            backward.setBackgroundColor(Color.rgb(186,32,29));
        }
        if (robot_select.equalsIgnoreCase("Shaky")) {
            Log.v(LogMessage,"Shaky");
            robot = new UnreliableRobot('0', '0', '0', '0');
            left.setBackgroundColor(Color.rgb(186,32,29));
            right.setBackgroundColor(Color.rgb(186,32,29));
            forward.setBackgroundColor(Color.rgb(186,32,29));
            backward.setBackgroundColor(Color.rgb(186,32,29));

        }

        SeekBar seekbar = findViewById(R.id.seekBar4);
        seekbar.setMax(10);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                speed = progressChangedValue;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                speed = progressChangedValue;
            }
        });


        //project 7
        energyBar = findViewById(R.id.progressBar2);
        energyBar.setMax(3500);
        energyBar.setProgress(3500);

        mazePanel = findViewById(R.id.mazePanel2);
        maze = Singleton.getMaze();
        playing = new StatePlaying();

        robot.setMaze(playing);
        robot.setSensorMaze(playing);
        driver.setRobot(robot);

        playing.setRobot(robot);
        playing.setDriver(driver);

        playing.setMazeConfiguration(maze);
        playing.setPlayAnimationActivity(this);
        playing.start(mazePanel);


        path = 0;
        shortestPath = 0;
        energyConsumed = 0;
        int[] start_pos = maze.getStartingPosition();
        shortestPath = maze.getDistanceToExit(start_pos[0],start_pos[1]);


        Start = findViewById(R.id.toggleButton);
        Start.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.v(LogMessage, "Start the driver");
                    handler.post(drive);
                } else {
                    Log.v(LogMessage, "Pause the driver");
                    handler.removeCallbacks(drive);
                }
            }
        });


        song = MediaPlayer.create(PlayAnimationActivity.this,R.raw.music1);
        song.start();
    }



    private Runnable drive = new Runnable() {

        @Override
        public void run() {

            int[] closer_pos;
            int[] cur_pos;
            if (driver_select.equalsIgnoreCase("Wizard")) {
                System.out.println("paopaopao!!!");
                if (robot.getBatteryLevel() >= 3) {
                    try {
                        cur_pos = playing.getCurrentPosition();
                        closer_pos = playing.getMazeConfiguration().getNeighborCloserToExit(cur_pos[0], cur_pos[1]);
                        if (cur_pos[0] == closer_pos[0]) {
                            if (cur_pos[1] - closer_pos[1] == 1) {
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }
                                driver.Rotate(CardinalDirection.North);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                }
                                //handler.postDelayed(this, delayed);
                                robot.move(1);
                                if(robot.hasStopped()) {
                                    tolose();
                                }
                                updateEnergyBar();

                            }
                            if (cur_pos[1] - closer_pos[1] == -1) {
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }
                                driver.Rotate(CardinalDirection.South);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }

                                robot.move(1);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }

                            }
                        }
                        if (cur_pos[1] == closer_pos[1]) {
                            if (cur_pos[0] - closer_pos[0] == 1) {
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }
                                driver.Rotate(CardinalDirection.West);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }
                                robot.move(1);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }
                            }
                            if (cur_pos[0] - closer_pos[0] == -1) {
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }
                                driver.Rotate(CardinalDirection.East);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }

                                robot.move(1);
                                updateEnergyBar();
                                if(robot.hasStopped()) {
                                    tolose();
                                    return;
                                }


                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    tolose();
                    return;
                }
            }else if (driver_select.equalsIgnoreCase("WallFollower")){
                robot.startFailureAndRepairProcess(Robot.Direction.LEFT, 4000, 2000);
                try {

                    if (robot.getBatteryLevel() < 3) {
                        tolose();
                        return;
                    }
                    if (!driver.CheckLeftWall()){
                        updateEnergyBar();
                        if(!driver.CheckFrontWall()){

                            updateEnergyBar();
                            robot.rotate(Robot.Turn.RIGHT);
                            updateEnergyBar();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (robot.isAtExit()) {
                handler.removeCallbacks(this);
                Log.v(LogMessage, "Exit!!!!!");
                if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.LEFT)) {
                    robot.rotate(Robot.Turn.LEFT);
                    updateEnergyBar();
                    robot.move(1);
                    updateEnergyBar();
                    towin();

                    return;
                } else if (robot.canSeeThroughTheExitIntoEternity(Robot.Direction.RIGHT)) {
                    robot.rotate(Robot.Turn.RIGHT);
                    updateEnergyBar();

                    robot.move(1);
                    updateEnergyBar();
                    towin();

                    return;
                } else {
                    robot.move(1);
                    updateEnergyBar();
                    towin();

                    return;
                }


            }
            int delayed = 1000;
            delayed = delayed / (speed+1);
            handler.postDelayed(this, delayed);

        }
    };

    private void updateEnergyBar() {
        energyConsumed = (int) (3500 - robot.getBatteryLevel());
        energyBar.setProgress(3500 - energyConsumed);

    }




    /** Method: towin
         *  navigate to winning page
         */

        public void towin() {
            // Do something in response to button click

            energyConsumed = (int) (3500-robot.getBatteryLevel());
            path = robot.getOdometerReading();

            str_path = String.valueOf(path);
            str_short = String.valueOf(shortestPath);
            str_energy = String.valueOf(energyConsumed);

            Intent intent = new Intent(this, WinningActivity.class);
            intent.putExtra("Shortest", str_short);
            intent.putExtra("Path", str_path);
            intent.putExtra("Energy", str_energy);

            Log.v(LogMessage, "Path: " + path);
            Log.v(LogMessage, "Shortest Path: " + shortestPath);
            Log.v(LogMessage, "Energy Consumed: " + energyConsumed);

            song.release();

            Toast.makeText(context, "Path Used" + str_path + "\n Shortest Path: " + str_short + "\n Energy Consumed: " + str_energy, Toast.LENGTH_SHORT).show();
            startActivity(intent);

        }


        /** Method: tolose
         *  navigate to losing page
         */
        public void tolose() {


            energyConsumed = (int) (3500-robot.getBatteryLevel());
            path = robot.getOdometerReading();

            str_path = String.valueOf(path);
            str_short = String.valueOf(shortestPath);
            str_energy = String.valueOf(energyConsumed);

            Intent intent = new Intent(this, LosingActivity.class);

            intent.putExtra("Shortest", str_short);
            intent.putExtra("Path", str_path);
            intent.putExtra("Energy", str_energy);

            Log.v(LogMessage, "Path Used: " + path);
            Log.v(LogMessage, "Shortest Path: " + shortestPath);
            Log.v(LogMessage, "Energy Consumed: " + energyConsumed);
            song.release();

            Toast.makeText(context, "Path Used" + str_path + "\n Shortest Path: " + str_short + "\n Energy Consumed: " + str_energy, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }

        /** Method: onBackPressed
         *  Back to title page
         */
        public void onBackPressed() {
            handler.removeCallbacks(drive);
            Intent titleIntent = new Intent(this, AMazeActivity.class);
            Log.v(LogMessage, "Go to the title page");
            song.release();
            startActivity(titleIntent);
            finish();

        }



}
