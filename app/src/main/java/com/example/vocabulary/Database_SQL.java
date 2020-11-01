package com.example.vocabulary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database_SQL extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Vocabulary.db";
    public static final String TABLE_NAME = "Vocabulary_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "collection";
    public static final String COL_3 = "words";
    public static final String COL_4 = "translation";
    public static final String COL_5 = "statement_for_w";
    public static final String COL_6 = "statement_for_t";
    public Database_SQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, collection STRING, words STRING, translation STRING, statement_for_w BOOLEAN, statement_for_t BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
