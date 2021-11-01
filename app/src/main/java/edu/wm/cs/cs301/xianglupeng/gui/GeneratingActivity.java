package edu.wm.cs.cs301.xianglupeng.gui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import edu.wm.cs.cs301.xianglupeng.R;
import edu.wm.cs.cs301.xianglupeng.generation.Factory;
import edu.wm.cs.cs301.xianglupeng.generation.Maze;
import edu.wm.cs.cs301.xianglupeng.generation.MazeFactory;
import edu.wm.cs.cs301.xianglupeng.generation.Order;
import edu.wm.cs.cs301.xianglupeng.generation.Singleton;

public class GeneratingActivity extends AppCompatActivity implements Order {

    private MazePanel mazePanel;
    private Maze maze;
    private Spinner spinner;
    private ProgressBar progressBar;
    private TextView textView;
    private Intent intent;
    private Context context;
    private String robot;
    private String builder;
    private boolean room;
    private String level;
    private int skillLevel;
    private String LogMessage = "GeneratingActivity";
    private int generateProgress;
    private String mode;
    private int seed_tosave;
    private int seed;
    MediaPlayer song;

    private StatePlaying playing = new StatePlaying();


    protected static Factory factory;
    private Order.Builder Builder;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    /** Method: onCreate
     *  Create generating page of the game, asks for users input
     *  to determine driver and robot
     * */

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        Intent getIntent = getIntent();
        builder = getIntent.getStringExtra("Builder");
        room = getIntent.getBooleanExtra("Room",true);
        level = getIntent.getStringExtra("level");
        mode = getIntent.getStringExtra("Mode");



        // Project 7 added

        context = getApplicationContext();
        Singleton.setContext(getApplicationContext());
        Singleton.setGeneratingActivity(this);

        Builder = Order.Builder.DFS;
        factory = new MazeFactory();
        generateProgress = 0;

        skillLevel = Integer.parseInt(level);
        //skillLevel = 1;


        if(builder.equals("DFS")){
            Builder = Order.Builder.DFS;
        }
        if(builder.equals("Prim")){
            Builder = Order.Builder.Prim;
        }
        if(builder.equals("Eller")){
            Builder = Order.Builder.Eller;
        }

        Random rand = new Random();
		seed = rand.nextInt();

		Singleton.setSeed(seed);
        Singleton.setSkillLevel(skillLevel);
        Singleton.setBuilder(Builder);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String load = skillLevel+builder+room;
        if(mode.equalsIgnoreCase("revisit")){
            if (sharedPref != null && sharedPref.contains(load)){
                seed = sharedPref.getInt(load,0);
//                seed = seed_saved;
                Singleton.setSeed(seed);
                System.out.println("zhaodaole!");
                System.out.println("revisit: "+ seed);

            }else{
//                Toast.makeText(context, "No Loaded Maze Found.", Toast.LENGTH_SHORT).show();
            }
        }


        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        textView = findViewById(R.id.progress);

        spinner = findViewById(R.id.spinner_robot);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.robot, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        mazePanel = new MazePanel(this);
        mazePanel.setGeneratingActivity(this);
        mazePanel.Start();

        maze = Singleton.getMaze();
        playing.setMazeConfiguration(maze);
        seed_tosave = Singleton.getSeed();

        String save = skillLevel+builder+room;
        editor.putInt(save,seed_tosave);
        editor.apply();


        song = MediaPlayer.create(GeneratingActivity.this,R.raw.gen);
        song.start();

//        editor.putInt("Level", skillLevel);
//        editor.putString("Builder", builder);
//        editor.putString("Room", String.valueOf(room));
//        editor.putInt("Seed", seed);




//
//        new Thread(new Runnable() {
//
//            int progressStatus = 0;
//
//            public void run() {
//
//
////                mazePanel.setGeneratingActivity(Singleton.getGeneratingActivity());
////                mazePanel.genActivityStart();
//
//                while (progressStatus < 100) {
//                    progressStatus++;
//                    try {
//                        Thread.sleep(20);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    // Update the progress bar
//                    handler.post(new Runnable() {
//                        public void run() {
//                            progressBar.setProgress(progressStatus);
//                            textView.setText("Generating your maze: " + progressStatus + "%");
//                        }
//                    });
//
//                }
//                updateProgress(progressStatus);
//                handleMessage(1);
//            }
//
//
//        }).start();

    }

    public void start(MazePanel panel) {


        if (panel == null){
            Log.v("gen_start","State gen: WARNING, start() has panel == null");
        }
        this.mazePanel = panel;
        generateProgress = 0;

        assert null != factory : "Controller.init: factory must be present";

        factory.order(this);
    }

//    /** Method: NavigateToManualMode
//     *  navigate to manully playing page.
//     */
//    public void NavigateToManualMode(){
//        intent = new Intent(this, PlayManuallyActivity.class);
//        Log.v(LogMessage,"Manual Mode");
//        Toast.makeText(context, "Manual Mode", Toast.LENGTH_SHORT).show();
//        startActivity(intent);
//        finish();
//
//    }
//
//    /** Method: NavigateToDriverMode
//     *  navigate to driver playing page.
//     */
//    public void NavigateToDriverMode(){
//
//        robot = spinner.getSelectedItem().toString();
//        intent = new Intent(this, PlayAnimationActivity.class);
//        intent.putExtra("robot",robot);
//        Log.v(LogMessage,"Robot" + robot);
//        Log.v(LogMessage,"Driver Mode");
//        Toast.makeText(context, "Driver Mode" + "\nRobot: " + robot , Toast.LENGTH_SHORT).show();
//        startActivity(intent);
//        finish();
//
//    }


    /** Method: manual
     *  If the generating progress reaches 100%, navigate to manually playing activity
     */
    public void manual(View view){
        if (progressBar.getProgress() == 100){
            intent = new Intent(this, PlayManuallyActivity.class);
            Log.v(LogMessage,"Manual Mode");
//            Toast.makeText(context, "Manual Mode", Toast.LENGTH_SHORT).show();
            song.release();
            startActivity(intent);
            finish();
        }

    }

    /** Method: wizard
     *  If the generating progress reaches 100%, navigate to animation activity
     */
    public void driver_wizard(View view){
        if (progressBar.getProgress() == 100){
            robot = spinner.getSelectedItem().toString();
            intent = new Intent(this, PlayAnimationActivity.class);
            intent.putExtra("robot",robot);
            intent.putExtra("driver", "Wizard");
            Log.v(LogMessage,"Robot" + robot);
            Log.v(LogMessage,"Wizard");
//            Toast.makeText(context, "Wizard" + "\nRobot: " + robot , Toast.LENGTH_SHORT).show();
            song.release();
            startActivity(intent);
            finish();


        }
    }

    /** Method: wallfollower
     *  If the generating progress reaches 100%, navigate to animation activity
     */
    public void driver_wall(View view){
        if (progressBar.getProgress() == 100){
            robot = spinner.getSelectedItem().toString();
            intent = new Intent(this, PlayAnimationActivity.class);
            intent.putExtra("robot",robot);
            intent.putExtra("driver", "WallFollower");
            Log.v(LogMessage,"Robot" + robot);
            Log.v(LogMessage,"WallFollower");
//            Toast.makeText(context, "WallFollower" + "\nRobot: " + robot , Toast.LENGTH_SHORT).show();
            song.release();
            startActivity(intent);
            finish();


        }
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


    /**
     * Gives the required skill level, range of values 0,1,2,...,15
     */
    @Override
    public int getSkillLevel() {
        return skillLevel;
    }

    /**
     * Gives the requested builder algorithm, possible values
     * are listed in the Builder enum type.
     */
    @Override
    public Builder getBuilder() {
        return Builder;
    }

    /**
     * Describes if the ordered maze should be perfect, i.e. there are
     * no loops and no isolated areas, which also implies that
     * there are no rooms as rooms can imply loops
     */
    @Override
    public boolean isPerfect() {
        if (room){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Gives the seed that is used for the random number generator
     * used during the maze generation.
     */
    @Override
    public int getSeed() {
        return seed;
    }



public Handler handler = new Handler() {
    public void handleMessage(@NonNull Message msg) {

        int percent = GeneratingActivity.this.generateProgress;

        switch (msg.what) {
            case 0:
                Log.v(LogMessage, String.format("progress: <%s>.", percent));
                progressBar.setProgress(percent);
                textView.setText("Generating your maze: " + percent  + "%");
                break;
            case 1:
                Log.v(LogMessage, "Generation complete.");
                progressBar.setProgress(percent);
                textView.setText("Generating your maze: " + percent + "%");
                break;
        }
    }
};




    /**
     * Delivers the produced maze.
     * This method is called by the factory to provide the
     * resulting maze as a MazeConfiguration.
     *
     * @param mazeConfig
     */
    @Override
    public void deliver(Maze mazeConfig) {
        Singleton.setMaze(mazeConfig);
    }

    /**
     * Provides an update on the progress being made on
     * the maze production. This method is called occasionally
     * during production, there is no guarantee on particular values.
     * Percentage will be delivered in monotonously increasing order,
     * the last call is with a value of 100 after delivery of product.
     *
     * @param percentage
     */
    @Override
    public void updateProgress(int percentage) {

        if (this.generateProgress < percentage && percentage <= 100) {
            this.generateProgress = percentage;

            if (this.generateProgress < 100) {
                handler.sendEmptyMessage(0);
            } else if (this.generateProgress >= 100) {
                handler.sendEmptyMessage(1);
            }

        }

    }
}

