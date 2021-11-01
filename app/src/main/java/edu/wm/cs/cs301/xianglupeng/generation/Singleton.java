package edu.wm.cs.cs301.xianglupeng.generation;

import android.content.Context;
import android.content.SharedPreferences;

import edu.wm.cs.cs301.xianglupeng.gui.GeneratingActivity;
import edu.wm.cs.cs301.xianglupeng.gui.PlayAnimationActivity;
import edu.wm.cs.cs301.xianglupeng.gui.PlayManuallyActivity;

public class Singleton {

    private static GeneratingActivity generatingActivity;
    private static Order.Builder Builder;
    private static int skillLevel;
    private static Context context;
    private static Maze maze;
    private static int seed;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    public static void setBuilder(Order.Builder builder) {
        Singleton.Builder = builder;
    }

    public static void setSkillLevel(int skillLevel) {
        Singleton.skillLevel = skillLevel;
    }

    public static void setContext(Context context) {
        Singleton.context = context;
    }

    public static Order.Builder getBuilder(){
        return Builder;
    }

    public static int getSkillLevel(int skillLevel) {
        return Singleton.skillLevel;
    }

    public static Context getContext() {
        return context;
    }



    public static void setGeneratingActivity(GeneratingActivity generatingActivity){
        Singleton.generatingActivity = generatingActivity;
    }

    public static GeneratingActivity getGeneratingActivity(){
        return Singleton.generatingActivity ;
    }



    public static void setMaze(Maze maze){
        Singleton.maze = maze;
    }

    public static Maze getMaze(){
        return maze;
    }

    public static void setSeed(int seed){
        Singleton.seed = seed;
    }

    public static int getSeed(){
        return Singleton.seed;
    }

    public static void setPlayAnimationActivity(PlayAnimationActivity playAnimationActivity) {
    }

    public static void setPreferenceAndEditor(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        Singleton.sharedPreferences = sharedPreferences;
        Singleton.editor = editor;
    }

    public static SharedPreferences getPreference() {
        return Singleton.sharedPreferences;

    }

    public static SharedPreferences.Editor getEditor() {
        return Singleton.editor;
    }
}


