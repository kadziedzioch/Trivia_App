package com.example.mytrivia.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefRepository {

    private static final String SCORE_ID = "score_id";
    public static final String HIGHEST_SCORE_ID = "highest_score";
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public PrefRepository(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SCORE_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveHighestScore(int score){
        editor.putInt(HIGHEST_SCORE_ID,score);
        editor.apply();
    }

    public int getHighestScore(){
        return sharedPreferences.getInt(HIGHEST_SCORE_ID,0);
    }







}
