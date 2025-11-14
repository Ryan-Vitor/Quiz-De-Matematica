package com.example.moveniba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RankingDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public RankingDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addRanking(String name) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_SCORE, 0);
        return database.insert(DatabaseHelper.TABLE_RANKING, null, values);
    }

    public Ranking getRanking(int id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_RANKING,
                null, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE));
                cursor.close();
                return new Ranking(id, name, score);
            }
            cursor.close();
        }
        return null;
    }

    public List<Ranking> getAllRankings() {
        List<Ranking> rankings = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_RANKING,
                null, null, null, null, null, DatabaseHelper.COLUMN_SCORE + " DESC");

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SCORE));
                rankings.add(new Ranking(id, name, score));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return rankings;
    }

    public int updateName(int id, String newName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, newName);
        return database.update(DatabaseHelper.TABLE_RANKING, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void updateScoreIfHigher(int id, int newScore) {
        Ranking currentRanking = getRanking(id);
        if (currentRanking != null && newScore > currentRanking.getScore()) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_SCORE, newScore);
            database.update(DatabaseHelper.TABLE_RANKING, values,
                    DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        }
    }

    public void deleteRanking(int id) {
        database.delete(DatabaseHelper.TABLE_RANKING,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
