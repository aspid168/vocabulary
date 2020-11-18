package com.example.vocabulary;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AdapterForElements extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public  int elements;
    String element_from_db = "";
    public Context mcontext;
    public ElementDatabase_SQL databaseSql;
    public CollectionDatabase_SQL collectionDatabaseSql;

    public AdapterForElements(Context context) {
        mcontext = context;
        databaseSql = new ElementDatabase_SQL(mcontext);
        Cursor res = databaseSql.getElementData();
        while (res.moveToNext()){
            elements = (res.getInt(0)) + 1;
        }
        collectionDatabaseSql = new CollectionDatabase_SQL(mcontext);
    }

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

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element, parent, false);
        return new ElementsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final TextView e_edit = ((ElementsViewHolder) holder).elem_edit;
        final TextView e_text = ((ElementsViewHolder) holder).elem_text;
        final ImageButton e_delete = ((ElementsViewHolder) holder).elem_delete;
        element_from_db = "";
        boolean state = true;
        Cursor res = databaseSql.getElementData();
        while (res.moveToNext()){
            if(Integer.parseInt(res.getString(0)) == position ){
                element_from_db = res.getString(1);
                int bol = res.getInt(2);
                if(bol != 1)
                    state = false;
            }
        }

        if (state) {
            e_edit.setText(element_from_db);
            e_text.setVisibility(View.GONE);
            e_edit.setVisibility(View.VISIBLE);
            e_delete.setVisibility(View.VISIBLE);
        } else {
            e_text.setText(element_from_db);
            e_edit.setVisibility(View.GONE);
            e_text.setVisibility(View.VISIBLE);
            e_delete.setVisibility(View.GONE);
        }

        e_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, SecondActivity.class);
                intent.putExtra("element", position);
                mcontext.startActivity(intent);
            }
        });

        e_edit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!e_edit.getText().toString().isEmpty()){
                    databaseSql.updateElement(Integer.toString(position), e_edit.getText().toString());
                    databaseSql.updateElementState(Integer.toString(position), false);
                    e_text.setText(e_edit.getText().toString());
                    e_edit.setVisibility(View.GONE);
                    e_text.setVisibility(View.VISIBLE);
                    e_delete.setVisibility(View.GONE);
                }
                else{
                    Toast warning = Toast.makeText(mcontext, "Please enter text", Toast.LENGTH_SHORT);
                    warning.show();
                }
                return true;
            }
        });

        e_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                databaseSql.updateElementState(Integer.toString(position), true);
                e_edit.setText(e_text.getText().toString());
                e_text.setVisibility(View.GONE);
                e_edit.setVisibility(View.VISIBLE);
                e_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        e_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                elements = elements - 1;
                if(elements == 0)
                    databaseSql.deleteData();
                databaseSql.deleteElement(Integer.toString(position));
                collectionDatabaseSql.deleteCollection(Integer.toString(position), 0);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, elements);
            }
        });

        e_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                databaseSql.updateElement(Integer.toString(position), s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void add(){
        databaseSql.insertElement(Integer.toString(elements), "");
        notifyItemInserted(elements);
        elements = elements + 1;
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
