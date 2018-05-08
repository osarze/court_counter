package com.example.android.courtcounter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int teamAScore = 0;
    private int teamBScore = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayForTeamA(teamAScore);
    }

    public void displayForTeamA(int score){
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTeamB(int score){
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Increase Point by 3
     * @param v
     */
    public void addThreeForTeamA(View v){
        teamAScore += 3;
        displayForTeamA(teamAScore);
    }

    public void addTwoForTeamA(View v){
        teamAScore += 2;
        displayForTeamA(teamAScore);
    }

    public void addOneForTeamA(View v){
        teamAScore += 1;
        displayForTeamA(teamAScore);
    }

    public void addThreeForTeamB(View v){
        teamBScore += 3;
        displayForTeamB(teamBScore);
    }

    public void addTwoForTeamB(View v){
        teamBScore += 2;
        displayForTeamB(teamBScore);
    }

    public void addOneForTeamB(View v){
        teamBScore += 1;
        displayForTeamB(teamBScore);
    }

    public void resetScores(View v){
        teamAScore = 0;
        teamBScore = 0;
        displayForTeamA(teamAScore);
        displayForTeamB(teamAScore);
    }
}
