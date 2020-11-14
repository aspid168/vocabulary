package com.example.vocabulary;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForCollections extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public  int elements;
    String word = "";
    String translation = "";
    private List<Words> collections = new LinkedList<>();
    public Context mcontext;
    public CollectionDatabase_SQL databaseSql;

    public AdapterForCollections(Context context) {
        mcontext = context;
        databaseSql = new CollectionDatabase_SQL(mcontext);
        Cursor res = databaseSql.getElementData();
        while (res.moveToNext()){
            elements = (res.getInt(0)) + 1;
        }
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
        word = "";
        translation = "";
        boolean state = true;
        Cursor res = databaseSql.getElementData();
        while (res.moveToNext()){
            if(Integer.parseInt(res.getString(0)) == position ){
                word = res.getString(1);
                translation = res.getString(2);
                int bol = res.getInt(3);
                if(bol != 1)
                    state = false;
            }
        }
        if (state) {
            textView_w.setText(word);
            textView_t.setText(translation);
            textView_w_invisible.setVisibility(View.GONE);
            textView_t_invisible.setVisibility(View.GONE);
            textView_w.setVisibility(View.VISIBLE);
            textView_t.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            textView_w_invisible.setText(word);
            textView_t_invisible.setText(translation);
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
                elements = elements - 1;
                databaseSql.deleteCollection(Integer.toString(position));
                if(elements == 0)
                    databaseSql.deleteData();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, collections.size());
            }
        });

        textView_w.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (elements > position)
                {
                    if(databaseSql.insertWord(Integer.toString(position), s.toString()))
                    {

                    }
                    else{
                        databaseSql.updateWord(Integer.toString(position), s.toString());

                    }
                }
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
                if(databaseSql.insertTranslation(Integer.toString(position), s.toString()))
                {

                }
                else{
                    databaseSql.updateTranslation(Integer.toString(position), s.toString());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

    }

    public void add(){
        elements = elements + 1;
        notifyItemInserted(elements - 1);
    }
    @Override
    public int getItemCount() {
        return elements;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
