package com.example.vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.GregorianCalendar;

import androidx.annotation.Nullable;

public class ElementDatabase_SQL extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Elements.db";
    public static final String TABLE_NAME = "Elements_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "collection";
    public static final String COL_3 = "statement";

    public ElementDatabase_SQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, collection STRING, statement BOOLEAN)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertElement(String id, String element){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, element);
        contentValues.put(COL_3, true);
        long result = db.insert(TABLE_NAME, COL_1 + COL_2 + COL_3, contentValues);
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
        db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id});
    }

    public void updateElementState(String id, Boolean state){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_3, state);
        db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id});
    }

    public void deleteElement(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        db.delete(TABLE_NAME, "id = ?", new String[]{id});
        int Id = Integer.parseInt(id) - 1;
        cursor.move(Integer.parseInt(id));
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Integer.toString(Id));
        db.update(TABLE_NAME,contentValues, COL_1 + "= ?", new String[]{Integer.toString(Id)});
        while (cursor.moveToNext())
        {
            Id = Id + 1;
            contentValues = new ContentValues();
            contentValues.put(COL_1, Integer.toString(Id));
            db.update(TABLE_NAME,contentValues, COL_1 + "= ?", new String[]{Integer.toString(Id + 1)});
        }
    }

    public Cursor getElementData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

//todo delete in final version

    public Integer deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, new String[]{});
    }
}
