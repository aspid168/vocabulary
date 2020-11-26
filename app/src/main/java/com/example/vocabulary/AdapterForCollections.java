package com.example.vocabulary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForCollections extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int pos;
    private List<String> words = new LinkedList<>();
    private List<String> translations = new LinkedList<>();
    private final Context mcontext;
    private final CollectionDatabase_SQL databaseSql;
    private final List<Boolean> state = new LinkedList<>();
    private final List<Boolean> statement_add = new LinkedList<>();


    public AdapterForCollections(Context context, int pos) {
        this.pos = pos;
        mcontext = context;
        databaseSql = new CollectionDatabase_SQL(mcontext);
        words = databaseSql.getWordsList(words, pos);
        translations = databaseSql.getTranslationList(translations, pos);
        if(words.size() == 0 || translations.size() == 0)
            statement_add.add(true);
        while (words.size() < translations.size())
            words.add("");
        while (translations.size() < words.size())
            translations.add("");
        while (state.size() < words.size())
            state.add(false);
        while (statement_add.size() < words.size())
            statement_add.add(true);
    }

    static class CollectionsViewHolder extends RecyclerView.ViewHolder{
        private final TextView word;
        private final TextView translation;
        private final TextView word_invisible;
        private final TextView translation_invisible;
        private final ImageButton delete;
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
                    statement_add.set(position, true);
                    state.set(position, false);
                    words.set(position, textView_w.getText().toString());
                    translations.set(position, textView_t.getText().toString());
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
                    statement_add.set(position, true);
                    state.set(position, false);
                    words.set(position, textView_w.getText().toString());
                    translations.set(position, textView_t.getText().toString());
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
                statement_add.set(position, false);
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
                statement_add.set(position, false);
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
                words.remove(position);
                translations.remove(position);
                state.remove(position);
                statement_add.remove(position);
                if(!textView_t.getText().toString().isEmpty() && !textView_w.getText().toString().isEmpty()){
                    databaseSql.deleteCollection(Integer.toString(pos), position);
                }
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, words.size());
            }
        });
    }

    public void add(){
        if(statement_add.get(statement_add.size() - 1) || words.size() == 0) {
            words.add("");
            if(words.size() == 1)
                statement_add.remove(0);
            statement_add.add(false);
            translations.add("");
            state.add(true);
            notifyItemInserted(words.size());
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