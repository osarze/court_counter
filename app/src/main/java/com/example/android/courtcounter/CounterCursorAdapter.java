package com.example.android.courtcounter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.courtcounter.data.CounterContract.CounterEntry;

/**
 * Created by OKHAMAFE EMMANUEL on 5/11/2018.
 */

public class CounterCursorAdapter extends CursorAdapter {
    public CounterCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.counter_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
//        TextView teamANameView = view.findViewById(R.id.team_a_name);
//        TextView teamBNameView = view.findViewById(R.id.team_b_name);
        TextView teamAScoreView = view.findViewById(R.id.team_a_score);
        TextView teamBScoreView = view.findViewById(R.id.team_b_score);

        // Find the columns of items attributes that we're interested in
        int teamAScoreColumnIndex = cursor.getColumnIndex(CounterEntry.COLUMN_TEAM_A_SCORE);
        int teamBScoreColumnIndex = cursor.getColumnIndex(CounterEntry.COLUMN_TEAM_B_SCORE);

        // Read the item attributes from the Cursor for the current item

        String teamAScore = cursor.getString(teamAScoreColumnIndex);
        String teamBScore = cursor.getString(teamBScoreColumnIndex);

        teamAScoreView.setText(teamAScore);
        teamBScoreView.setText(teamBScore);

    }
}
