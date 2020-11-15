package com.example.vocabulary;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForCollections extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public  int elements;
    public int pos;
    public List<String> words = new LinkedList<>();
    public List<String> translations = new LinkedList<>();
    private List<Words> collections = new LinkedList<>();
    public Context mcontext;
    public CollectionDatabase_SQL databaseSql;

    public AdapterForCollections(Context context, int pos) {
        this.pos = pos;
        mcontext = context;
        databaseSql = new CollectionDatabase_SQL(mcontext);
        Cursor res = databaseSql.getElementData();
        databaseSql.getWordsList(words, pos);
        databaseSql.getTranslationList(translations, pos);
        //todo change
//        databaseSql.deleteData();
        while (res.moveToNext()) {
              elements = (res.getInt(0)) + 1;
        }

//        word = "";
//        translation = "";
//        words.add("");
//        translations.add("");
//        while (res.moveToNext()) {
//            elements = (res.getInt(0)) + 1;
//            if (Integer.parseInt(res.getString(0)) == pos) {
//                word = res.getString(1);
//                int w = 0;
////                Log.v("QQQQQQQQQQQQQQQQQQQQQQ1", word + " " + word.toCharArray() + " " + words);
//                for (char letter : word.toCharArray()) {
////                    Log.v("QQQQQQQQQQQQQQQQQQQQQQ2", w + " " + Arrays.toString(word.toCharArray()) + " " + words);
//                    if (letter == ' ') {
//                        w = w + 1;
//                        words.add("");
//                    } else
//                        words.set(w, words.get(w) + letter);
//
//                }
//                int t = 0;
//                translation = res.getString(2);
//                Log.v("QQQQQQQQQQQQQQQQQQQQQQ1", translation + " " + translation.toCharArray() + " " + translations);
//                for (char letter : translation.toCharArray()) {
//                    Log.v("QQQQQQQQQQQQQQQQQQQQQQ2", t + " " + Arrays.toString(translation.toCharArray()) + " " + translations);
//                    if (letter == ' ') {
//                        t = t + 1;
//                        translations.add("");
//                    } else
//                        translations.set(t, translations.get(t) + letter);
//
//                }
//
//
//            }
//        }
    }


    static class CollectionsViewHolder extends RecyclerView.ViewHolder{
        private TextView word;
        private TextView translation;
        private TextView word_invisible;
        private TextView translation_invisible;
        private ImageButton delete;
        public CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            word = itemView.findViewById(R.id.editTextWord);
            translation = itemView.findViewById(R.id.editTextTranslation);
            word_invisible = itemView.findViewById(R.id.word_invisible);
            translation_invisible = itemView.findViewById(R.id.translation_invisible);
            delete = itemView.findViewById(R.id.delete_button);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_of_collection, parent, false);
        return new CollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TextView textView_w = ((CollectionsViewHolder)holder).word;
        final TextView textView_t = ((CollectionsViewHolder)holder).translation;
        final TextView textView_w_invisible = ((CollectionsViewHolder)holder).word_invisible;
        final TextView textView_t_invisible = ((CollectionsViewHolder)holder).translation_invisible;
        final ImageButton delete = ((CollectionsViewHolder)holder).delete;
        if (words.size() == 0) {
            words.add("");
            databaseSql.insertWord("0", "");
        }
        if(translations.size() == 0){
            translations.add("");
            databaseSql.insertTranslation("0", "");
        }
        boolean state = true;
        Cursor res = databaseSql.getElementData();
        while (res.moveToNext()) {
            int bol = res.getInt(3);
            if (bol != 1)
                state = false;
        }
        if (state) {
            textView_w.setText(words.get(position));
            textView_t.setText(translations.get(position));
            textView_w_invisible.setVisibility(View.GONE);
            textView_t_invisible.setVisibility(View.GONE);
            textView_w.setVisibility(View.VISIBLE);
            textView_t.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            textView_w_invisible.setText(words.get(position));
            textView_t_invisible.setText(translations.get(position));
            textView_w.setVisibility(View.GONE);
            textView_t.setVisibility(View.GONE);
            textView_w_invisible.setVisibility(View.VISIBLE);
            textView_t_invisible.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
        }

        textView_w.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseSql.updateCollectionState(Integer.toString(position), false);
                textView_w_invisible.setText(textView_w.getText().toString());
                textView_t_invisible.setText(textView_t.getText().toString());
                textView_w.setVisibility(View.GONE);
                textView_t.setVisibility(View.GONE);
                textView_w_invisible.setVisibility(View.VISIBLE);
                textView_t_invisible.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
                return true;
            }
        });
        textView_t.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseSql.updateCollectionState(Integer.toString(position), false);
                textView_w_invisible.setText(textView_w.getText().toString());
                textView_t_invisible.setText(textView_t.getText().toString());
                textView_t.setVisibility(View.GONE);
                textView_w.setVisibility(View.GONE);
                textView_t_invisible.setVisibility(View.VISIBLE);
                textView_w_invisible.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
                return true;
            }
        });

        textView_w_invisible.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseSql.updateCollectionState(Integer.toString(position), true);
                textView_w.setText(textView_w_invisible.getText().toString());
                textView_t.setText(textView_t_invisible.getText().toString());
                textView_w.setVisibility(View.VISIBLE);
                textView_t.setVisibility(View.VISIBLE);
                textView_w_invisible.setVisibility(View.GONE);
                textView_t_invisible.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                return true;
            }
        });
        textView_t_invisible.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseSql.updateCollectionState(Integer.toString(position), true);
                textView_w.setText(textView_w_invisible.getText().toString());
                textView_t.setText(textView_t_invisible.getText().toString());
                textView_t.setVisibility(View.VISIBLE);
                textView_w.setVisibility(View.VISIBLE);
                textView_t_invisible.setVisibility(View.GONE);
                textView_w_invisible.setVisibility(View.GONE);
                delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseSql.deleteData();
//                elements = elements - 1;
//                databaseSql.deleteCollection(Integer.toString(position));
//                if(elements == 0)
//                    databaseSql.deleteData();
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, collections.size());
            }
        });

        textView_w.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (elements > position)
                    databaseSql.updateWord(Integer.toString(pos), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView_t.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (elements > position)
                    databaseSql.updateTranslation(Integer.toString(position), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }
//todo fix updates collections and do smth with words and translation lists
    public void add(){
        words.add("");
        translations.add("");
        notifyItemInserted(elements);
        elements = elements + 1;
    }

    @Override
    public int getItemCount() {
        return words.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
