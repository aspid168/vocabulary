package com.example.vocabulary;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForElements extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> elements = new LinkedList<>();
    private List<Boolean> state_1 = new LinkedList<>();

    public AlertDialog.Builder builder;
    public Context mcontext;
    public Database_SQL databaseSql;
    public AdapterForElements(Context context) {
        mcontext = context;
        databaseSql = new Database_SQL(mcontext);
        builder =  new AlertDialog.Builder(mcontext);
        builder.setCancelable(true);
        Cursor res = databaseSql.getElementData();
        while (res.moveToNext()){
            elements.add(res.getInt(0), res.getString(1));
        }
    }

    public boolean st = true;

    private List<Words> collections = new LinkedList<>();
    private List<Boolean> state_2 = new LinkedList<>();



    static class ElementsViewHolder extends RecyclerView.ViewHolder {

        private TextView elem_edit;
        private TextView elem_text;
        private ImageButton elem_delete;
        public ElementsViewHolder(@NonNull View itemView) {
            super(itemView);
            elem_edit = itemView.findViewById(R.id.edit);
            elem_text = itemView.findViewById(R.id.text);
            elem_delete = itemView.findViewById(R.id.delete);

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
        if (st){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element, parent, false);
            return new ElementsViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_of_collection, parent, false);
            return new CollectionsViewHolder(view);
        }
    }
    

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(st) {
            final TextView e_edit = ((ElementsViewHolder) holder).elem_edit;
            final TextView e_text = ((ElementsViewHolder) holder).elem_text;
            final ImageButton e_delete = ((ElementsViewHolder) holder).elem_delete;
//            String element_from_db = "";
//            Cursor res = databaseSql.getElementData();
//            while (res.moveToNext()){
//                if(Integer.parseInt(res.getString(0)) == position ){
//                    element_from_db = res.getString(1);
//                }
//            }

//            if (state_1.get(position)) {
//                e_edit.setText(element_from_db);
//                e_text.setVisibility(View.GONE);
//                e_edit.setVisibility(View.VISIBLE);
//                e_delete.setVisibility(View.VISIBLE);
//            } else {
//                e_text.setText(element_from_db);
//                e_edit.setVisibility(View.GONE);
//                e_text.setVisibility(View.VISIBLE);
//                e_delete.setVisibility(View.GONE);
//            }

            e_text.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    elements.clear();
                    state_1.clear();
                    notifyDataSetChanged();
                    st = false;
                }
            });

            e_edit.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
//                    databaseSql.deleteData();
//                    builder.setMessage(element_from_db);
//                    builder.show();
                    state_1.set(position, false);
//                    e_text.setText(elements.get(position));
                    e_edit.setVisibility(View.GONE);
                    e_text.setVisibility(View.VISIBLE);
                    e_delete.setVisibility(View.GONE);
                    return true;
                }
            });

            e_text.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    state_1.set(position, true);
//                    e_edit.setText(elements.get(position));
                    e_text.setVisibility(View.GONE);
                    e_edit.setVisibility(View.VISIBLE);
                    e_delete.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            e_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    databaseSql.deleteData();
                    try {
                        elements.remove(position);
                        state_1.remove(position);
                    }catch (IndexOutOfBoundsException ignored){

                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, elements.size());
                }
            });

            e_edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (elements.size() > position)
                    {
//                        elements.set(position, s.toString());
                        if(databaseSql.insertElement(Integer.toString(position), s.toString()))
                        {

                        }
                        else
                            databaseSql.updateElement(Integer.toString(position), s.toString());

//                        if (!elements.get(position).equals("") && elements.size() == position + 1)
//                            ((MainActivity)mcontext).buttonVisibilityForElements(true);
//                        else
//                            ((MainActivity)mcontext).buttonVisibilityForElements(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
        else{
            final TextView textView_w = ((CollectionsViewHolder)holder).word;
            final TextView textView_t = ((CollectionsViewHolder)holder).translation;
            final TextView textView_w_invisible = ((CollectionsViewHolder)holder).word_invisible;
            final TextView textView_t_invisible = ((CollectionsViewHolder)holder).translation_invisible;
            final ImageButton delete = ((CollectionsViewHolder)holder).delete;
            final Words w;
            w = collections.get(position);

            if (state_2.get(position)) {
                textView_w.setText(w.getWord());
                textView_t.setText(w.getTranslation());
                textView_w_invisible.setVisibility(View.GONE);
                textView_t_invisible.setVisibility(View.GONE);
                textView_w.setVisibility(View.VISIBLE);
                textView_t.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            } else {
                textView_w_invisible.setText(w.getWord());
                textView_t_invisible.setText(w.getTranslation());
                textView_w.setVisibility(View.GONE);
                textView_t.setVisibility(View.GONE);
                textView_w_invisible.setVisibility(View.VISIBLE);
                textView_t_invisible.setVisibility(View.VISIBLE);
                delete.setVisibility(View.GONE);
            }

            textView_w.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (collections.size() > position)
                    {
                        w.setWord(s.toString());
                        collections.set(position, w);
                        if (!w.getWord().equals("") && collections.size() == position + 1)
                            ((MainActivity)mcontext).buttonVisibilityForWords(true);
                        else
                            ((MainActivity)mcontext).buttonVisibilityForWords(false);
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
                    if (collections.size() > position)
                    {
                        w.setTranslation(s.toString());
                        collections.set(position, w);
                        if (!w.getTranslation().equals("") && collections.size() == position + 1)
                            ((MainActivity)mcontext).buttonVisibilityForTranslation(true);
                        else
                            ((MainActivity)mcontext).buttonVisibilityForTranslation(false);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

            textView_w.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    state_2.set(position, false);
                    textView_w_invisible.setText(w.getWord());
                    textView_t_invisible.setText(w.getTranslation());
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
                    state_2.set(position, false);
                    textView_t_invisible.setText(w.getTranslation());
                    textView_w_invisible.setText(w.getWord());
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
                    state_2.set(position, true);
                    textView_w.setText(w.getWord());
                    textView_t.setText(w.getTranslation());
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
                    state_2.set(position, true);
                    textView_t.setText(w.getTranslation());
                    textView_w.setText(w.getWord());
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
                    try {
                        collections.remove(position);
                        state_2.remove(position);
                    }catch (IndexOutOfBoundsException ignored){

                    }
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, collections.size());
                }
            });
            }
    }

    @Override
    public int getItemCount() {
        if (st)
            return elements.size();
        else
            return collections.size();
    }

    public void add(){

        if (st) {
            elements.add("");
            state_1.add(true);
            notifyItemInserted(elements.size() - 1);

        }
        else{
            collections.add(new Words());
            state_2.add(true);
            notifyItemInserted(collections.size() - 1);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (st)
            return position;
        return position + 100;
    }
}
