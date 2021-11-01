package edu.wm.cs.cs301.xianglupeng.gui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.xianglupeng.R;

public class AMazeActivity extends AppCompatActivity {


    private Intent intent;
    private String builder;
    private Boolean hasRoom;
    private int level;
    private String str_level;
    private Context context;
    private Spinner spinner;
    private Spinner spinner_room;
    private String LogMessage = "AMazeActivity";
    private ToggleButton music;
    MediaPlayer song;



    /** Method: OnCreate
     *  Create the title page of the game, show welcome message, asks users for input
     *  to determine builder, level and whether to have room in maze
     *  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amaze);

        context = getApplicationContext();

        spinner = findViewById(R.id.builder_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.builder,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner_room = findViewById(R.id.room_spinner);
        ArrayAdapter<CharSequence> adapter_room = ArrayAdapter.createFromResource(this,R.array.room,android.R.layout.simple_spinner_item);
        adapter_room.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_room.setAdapter(adapter_room);

        SeekBar seekbar = findViewById(R.id.seekBar);
        seekbar.setMax(16);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
                level = progressChangedValue;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(AMazeActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();
                level = progressChangedValue;
            }
        });


        music = findViewById(R.id.toggleButton5);
        song = MediaPlayer.create(AMazeActivity.this,R.raw.gen);
        song.start();

        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && song != null) {
                    song.pause();
                } else {
                    song.start();
                }
            }
        });



    }


    /** Method: exploreMode
     * Navigate to maze generating page and passes inputs to next activity
     * Generate a new maze
     * @param view
     */
    public void exploreMode(View view) {

        str_level = String.valueOf(level);
        builder = spinner.getSelectedItem().toString();
        if (0 == spinner_room.getSelectedItemPosition()){
            hasRoom = true;
        }else{
            hasRoom = false;
        }



        intent = new Intent(this, GeneratingActivity.class);
        intent.putExtra("Builder",builder);
        intent.putExtra("Room", hasRoom);
        intent.putExtra("level", str_level);
        intent.putExtra("Mode", "explore");
        Log.v(LogMessage,"Explore Mode.");
        Log.v(LogMessage,"Builder " + builder);
        Log.v(LogMessage,"Room" + hasRoom.toString());
        Log.v(LogMessage,"Level" + level);
//        Toast.makeText(context, "Explore \nLevel: " + level + "\nBuilder: " + builder + "\nHas Room: " + hasRoom.toString(), Toast.LENGTH_SHORT).show();
        song.release();

        startActivity(intent);

    }

    /** Method: revisitMode
     * Navigate to maze generating page and passes inputs to next activity
     * Load a stored maze
     * @param view
     */
    public void revisitMode(View view) {

        str_level = String.valueOf(level);
        builder = spinner.getSelectedItem().toString();
        if (0 == spinner_room.getSelectedItemPosition()){
            hasRoom = true;
        }else{
            hasRoom = false;
        }

        intent = new Intent(this, GeneratingActivity.class);
        intent.putExtra("Builder",builder);
        intent.putExtra("Room", hasRoom);
        intent.putExtra("level", str_level);
        intent.putExtra("Mode", "revisit");
        Log.v(LogMessage,"Revisit Mode.");
        Log.v(LogMessage,"Builder " + builder);
        Log.v(LogMessage,"Room" + hasRoom.toString());
        Log.v(LogMessage,"Level" + level);
//        Toast.makeText(context, "Revisit \nLevel: " + level + "\nBuilder: " + builder + "\nHas Room: " + hasRoom.toString(), Toast.LENGTH_SHORT).show();
        song.release();
        startActivity(intent);

    }

}
