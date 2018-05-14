package com.example.android.courtcounter;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.courtcounter.data.CounterContract.CounterEntry;

public class CounterActivity extends AppCompatActivity{
    private int teamAScore = 0;
    private int teamBScore = 0;
    private Button mSaveButton;
    private TextView mTeamAScoreView;
    private TextView mTeamBScoreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);
        displayForTeamA(teamAScore);

        mTeamAScoreView = findViewById(R.id.team_a_score);
        mTeamBScoreView = findViewById(R.id.team_b_score);
        mSaveButton = findViewById(R.id.save_scores);

        mSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveScores();
            }
        });
    }

    public void displayForTeamA(int score){
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForTeamB(int score){
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    public int getTeamAScore(){
        return  teamAScore;
    }

    public int getTeamBScore(){
        return teamBScore;
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

    public void saveScores(){
        //TODO if the activty have not been close let it update the existing scores
        String teamAScoreInput = mTeamAScoreView.getText().toString().trim();
        String teamBScoreInput = mTeamAScoreView.getText().toString().trim();

        int teamAScore = Integer.parseInt(teamAScoreInput);
        int teamBScore = Integer.parseInt(teamBScoreInput);

        ContentValues values = new ContentValues();

        values.put(CounterEntry.COLUMN_TEAM_A_SCORE, teamAScore);
        values.put(CounterEntry.COLUMN_TEAM_B_SCORE, teamBScore);
        values.put(CounterEntry.COLUMN_DATE, 65656);

        Uri newUri = getContentResolver().insert(CounterEntry.CONTENT_URI, values);

        if (newUri == null){
            Toast.makeText(this, getString(R.string.counter_score_save_error), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getString(R.string.counter_score_save_success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_counter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_save_and_exit:
                saveScores();
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
