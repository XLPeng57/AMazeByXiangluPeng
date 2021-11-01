package edu.wm.cs.cs301.xianglupeng;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import edu.wm.cs.cs301.xianglupeng.gui.AMazeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, AMazeActivity.class);
        startActivity(intent);


//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_amaze);
//
//        Spinner spinner = (Spinner) findViewById(R.id.builder_spinner);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.builder,android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//
//        Spinner spinner_driver = (Spinner) findViewById(R.id.driver_spinner);
//        ArrayAdapter<CharSequence> adapter_driver = ArrayAdapter.createFromResource(this,R.array.driver,android.R.layout.simple_spinner_item);
//        adapter_driver.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_driver.setAdapter(adapter_driver);

    }
}