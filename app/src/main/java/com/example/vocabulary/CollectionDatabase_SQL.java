package com.example.vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CollectionDatabase_SQL extends SQLiteOpenHelper{

        public static final String DATABASE_NAME = "Collection.db";
        public static final String TABLE_NAME = "Collection_table";
        public static final String COL_1 = "id";
        public static final String COL_2= "word";
        public static final String COL_3 = "translation";
        public static final String COL_4 = "statement";

        public CollectionDatabase_SQL(@Nullable Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME +"(id INTEGER PRIMARY KEY AUTOINCREMENT, word STRING, translation STRING, statement BOOLEAN)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public boolean insertWord(String id, String word){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_2, word);
            contentValues.put(COL_4, true);
            long result = db.insert(TABLE_NAME, COL_1 + COL_2 + COL_4, contentValues);
            if (result == -1)
                return false;
            else
                return true;
        }

        public boolean insertTranslation(String id, String translation){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_3, translation);
            contentValues.put(COL_4, true);
            long result = db.insert(TABLE_NAME, COL_1 + COL_3 + COL_4, contentValues);
            if (result == -1)
                return false;
            else
                return true;
        }

        public void updateWord(String id, String word){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_2, word);
            db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        }
        public void updateTranslation(String id, String translation){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_3, translation);
            db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        }

        public void updateCollectionState(String id, Boolean state){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_4, state);
            db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        }

        public void deleteCollection(String id){
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

        public Integer deleteData(){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME, null, new String[]{});
        }
    }

