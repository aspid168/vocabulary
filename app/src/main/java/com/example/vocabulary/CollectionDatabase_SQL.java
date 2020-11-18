package com.example.vocabulary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.LinkedList;
import java.util.List;
import androidx.annotation.Nullable;

public class CollectionDatabase_SQL extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Collection.db";
    public static final String TABLE_NAME = "Collection_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "word";
    public static final String COL_3 = "translation";

    public CollectionDatabase_SQL(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, word STRING, translation STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertWord(String id, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, word);
        contentValues.put(COL_3, "");
        db.insert(TABLE_NAME, COL_1 + COL_2, contentValues);
    }

    public void updateWord(String id, String word, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> words = new LinkedList<>();
        words = getWordsList(words, Integer.parseInt(id));
        if (words.size() <= position){
            words.add(word);
            insertWord(id, "");
        }
        else
            words.set(position, word);
        word = "";
        for(String i : words){
            if(word.equals(""))
                word = word + i;
            else
                word = word + " " + i;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, word);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
    }

    public void updateTranslation(String id, String translation, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> translations = new LinkedList<>();
        translations = getTranslationList(translations, Integer.parseInt(id));
        if(translations.size() <= position){
            translations.add(translation);
        }
        else
            translations.set(position, translation);
        translation = "";
        for(String i : translations){
            if(translation.equals(""))
                translation = translation + i;
            else
                translation = translation + " " + i;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_3, translation);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
    }

    public void deleteCollection(String id, int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (position == 0){
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
        else {
            List<String> translations = new LinkedList<>();
            List<String> words = new LinkedList<>();
            translations = getTranslationList(translations, Integer.parseInt(id));
            words = getWordsList(words, Integer.parseInt(id));
            String translation = "";
            String word = "";
            if (words.size() <= position)
                words.add(word);
            if (translations.size() <= position)
                translations.add(translation);
            words.remove(position);
            translations.remove(position);
            for (String i : translations) {
                if (translation.equals(""))
                    translation = translation + i;
                else
                    translation = translation + " " + i;
            }
            for (String i : words) {
                if (word.equals(""))
                    word = word + i;
                else
                    word = word + " " + i;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_2, word);
            contentValues.put(COL_3, translation);
            if (word.equals("") || translation.equals(""))
                db.delete(TABLE_NAME, "id = ?", new String[]{id});
            else
                db.update(TABLE_NAME, contentValues, "id = ?", new String[]{id});
        }
    }

    public List<String> getWordsList(List<String> words, int pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        String word;
        while (res.moveToNext()) {
            if (Integer.parseInt(res.getString(0)) == pos) {
                word = res.getString(1);
                words.add("");
                int w = 0;
                for (char letter : word.toCharArray()) {
                    if (letter == ' ') {
                        w = w + 1;
                        words.add("");
                    } else
                        words.set(w, words.get(w) + letter);
                }
            }
        }
        return words;
    }

    public List<String> getTranslationList(List<String> translations, int pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        String translation;
        while (res.moveToNext()) {
            if (Integer.parseInt(res.getString(0)) == pos) {
                translation = res.getString(2);
                translations.add("");
                int t = 0;
                for (char letter : translation.toCharArray()) {
                    if (letter == ' ') {
                        t = t + 1;
                        translations.add("");
                    } else
                        translations.set(t, translations.get(t) + letter);
                }
            }
        }
        return translations;
    }
}