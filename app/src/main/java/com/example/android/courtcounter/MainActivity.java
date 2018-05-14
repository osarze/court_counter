package com.example.android.courtcounter;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.courtcounter.data.CounterContract.CounterEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int COUNTER_LOADER = 0;

    CounterCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_list);

        ListView scoresListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items
        View emptyView = findViewById(R.id.empty_view);
        scoresListView.setEmptyView(emptyView);

        mCursorAdapter = new CounterCursorAdapter(this, null);
        scoresListView.setAdapter(mCursorAdapter);

        scoresListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri currentCounterUri = ContentUris.withAppendedId(CounterEntry.CONTENT_URI, id);
                confirmDialogue(id);
                return true;
            }
        });
        getSupportLoaderManager().initLoader(COUNTER_LOADER, null, this);

    }

    private void insertDummyData(){
        ContentValues values = new ContentValues();
        values.put(CounterEntry.COLUMN_TEAM_A_SCORE, 45);
        values.put(CounterEntry.COLUMN_TEAM_B_SCORE, 3);
        values.put(CounterEntry.COLUMN_DATE, 346789);

        Uri newUri = getContentResolver().insert(CounterEntry.CONTENT_URI, values);
    }

    private void confirmDialogue(final long itemId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.confirm_delete_text);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri currentCounterUri = ContentUris.withAppendedId(CounterEntry.CONTENT_URI, itemId);
                int rowsDeleted = getContentResolver().delete(currentCounterUri, null, null);

                if(rowsDeleted > 0){
                    Toast.makeText(MainActivity.this, getString(R.string.delete_success_text), Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(MainActivity.this, getString(R.string.delete_error_text), Toast.LENGTH_SHORT).show();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Item Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_dummy_data:
                insertDummyData();
                return true;

            case R.id.new_score_counter:
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteScores(View v ){
        Log.i("ID ", String.valueOf(v.getId()));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                CounterEntry._ID,
                CounterEntry.COLUMN_TEAM_A_SCORE,
                CounterEntry.COLUMN_TEAM_B_SCORE,
                CounterEntry.COLUMN_DATE
        };
        return new CursorLoader(this, CounterEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
