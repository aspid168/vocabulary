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
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForCollections extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public int pos;
    public List<String> words = new LinkedList<>();
    public List<String> translations = new LinkedList<>();
    public Context mcontext;
    public CollectionDatabase_SQL databaseSql;
    public List<Boolean> state = new LinkedList<>();
    public Boolean statement_add = true;


    public AdapterForCollections(Context context, int pos) {

        this.pos = pos;
        mcontext = context;
        databaseSql = new CollectionDatabase_SQL(mcontext);
        words = databaseSql.getWordsList(words, pos);
        translations = databaseSql.getTranslationList(translations, pos);
        while (words.size() < translations.size())
            words.add("");
        while (translations.size() < words.size())
            translations.add("");
        while (state.size() < words.size())
            state.add(false);
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
        if (state.get(position)) {
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
                if(!textView_t.getText().toString().isEmpty() && !textView_w.getText().toString().isEmpty()){
                    statement_add = true;
                    state.set(position, false);
                    databaseSql.updateWord(Integer.toString(pos), textView_w.getText().toString(), position);
                    databaseSql.updateTranslation(Integer.toString(pos), textView_t.getText().toString(), position);
                    textView_w_invisible.setText(textView_w.getText().toString());
                    textView_t_invisible.setText(textView_t.getText().toString());
                    textView_w.setVisibility(View.GONE);
                    textView_t.setVisibility(View.GONE);
                    textView_w_invisible.setVisibility(View.VISIBLE);
                    textView_t_invisible.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                }
                else{
                    Toast warning = Toast.makeText(mcontext, "Please enter text", Toast.LENGTH_SHORT);
                    warning.show();
                }
                return true;
            }
        });
        textView_t.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!textView_t.getText().toString().isEmpty() && !textView_w.getText().toString().isEmpty()){
                    statement_add = true;
                    state.set(position, false);
                    databaseSql.updateWord(Integer.toString(pos), textView_w.getText().toString(), position);
                    databaseSql.updateTranslation(Integer.toString(pos), textView_t.getText().toString(), position);
                    textView_w_invisible.setText(textView_w.getText().toString());
                    textView_t_invisible.setText(textView_t.getText().toString());
                    textView_t.setVisibility(View.GONE);
                    textView_w.setVisibility(View.GONE);
                    textView_t_invisible.setVisibility(View.VISIBLE);
                    textView_w_invisible.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                }
                else{
                    Toast warning = Toast.makeText(mcontext, "Please enter text", Toast.LENGTH_SHORT);
                    warning.show();
                }
                return true;
            }
        });

        textView_w_invisible.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                state.set(position, true);
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
                state.set(position, true);
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
    }

    public void add(){
        if(statement_add) {
            words.add("");
            translations.add("");
            state.add(true);
            notifyItemInserted(words.size());
            statement_add = false;
        }
        else
        {
            Toast warning = Toast.makeText(mcontext, "Please enter text", Toast.LENGTH_SHORT);
            warning.show();
        }
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
