package com.example.vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.GregorianCalendar;

import androidx.annotation.Nullable;

public class Database_SQL extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Vocabulary.db";
    public static final String TABLE_NAME = "Vocabulary_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "collection";
    public static final String COL_3 = "word";
    public static final String COL_4 = "translation";
    public static final String COL_5 = "statement_for_w";
    public static final String COL_6 = "statement_for_t";

    public Database_SQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
//        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, collection STRING, word STRING, translation STRING, statement_for_w BOOLEAN, statement_for_t BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertElement(String id, String collection){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, collection);

        long result = db.insert(TABLE_NAME, COL_1 + COL_2, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public void updateElement(String id, String collection){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, collection);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
    }
    public void insertCollection(String id, String word, String translation, Boolean statement_for_w, Boolean statement_for_t){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_3, word);
        contentValues.put(COL_4, translation);
        contentValues.put(COL_5, statement_for_w);
        contentValues.put(COL_6, statement_for_t);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] {id});
    }

    public Integer deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, new String[]{});
    }

    public Cursor getElementData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
//        Cursor res = db.rawQuery(TABLE_NAME, new String[]{COL_1, COL_2});
        return res;
    }
}
