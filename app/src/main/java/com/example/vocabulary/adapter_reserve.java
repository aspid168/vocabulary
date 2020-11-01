package com.example.vocabulary;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class adapter_reserve extends RecyclerView.Adapter<adapter_reserve.ElementViewHolder> {
    private List<String> elements = new LinkedList<>();
    private List<Boolean> state = new LinkedList<>();

    public static class ElementViewHolder extends RecyclerView.ViewHolder {
        private TextView elem_edit;
        private TextView elem_text;
        private ImageButton elem_delete;
        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
            elem_edit = itemView.findViewById(R.id.edit);
            elem_text = itemView.findViewById(R.id.text);
            elem_delete = itemView.findViewById(R.id.delete);
        }
    }

    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element, parent, false);
        return new ElementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ElementViewHolder holder, final int position) {
        final TextView e_edit = holder.elem_edit;
        final TextView e_text = holder.elem_text;
        final ImageButton e_delete = holder.elem_delete;
        e_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (elements.size() >= 1) {
                    elements.set(position, e_edit.getText().toString());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (state.get(position)) {
            e_edit.setText(elements.get(position));
            e_text.setVisibility(View.GONE);
            e_edit.setVisibility(View.VISIBLE);
            e_delete.setVisibility(View.VISIBLE);
        }
        else{
            e_text.setText(elements.get(position));
            e_edit.setVisibility(View.GONE);
            e_text.setVisibility(View.VISIBLE);
            e_delete.setVisibility(View.GONE);
        }

        e_text.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
//                MainActivity mainActivity = new MainActivity();
                elements.clear();
                state.clear();
//                notifyDataSetChanged();
////                holder.recyclerViewCollections.setHasFixedSize(true);
//                holder.recyclerViewCollections.setLayoutManager(new LinearLayoutManager(mcontext));
//                holder.recyclerViewCollections.addItemDecoration(new DividerItemDecoration(mcontext, LinearLayoutManager.VERTICAL));
//                adapterForCollections = new AdapterForCollections();
//                holder.recyclerViewCollections.setAdapter(adapterForCollections);]
//                mainActivity.setAdapter(true);

//                adapterForCollections.add_item();
            }
        });

        e_edit.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                state.set(position, false);
                e_text.setText(elements.get(position));
                e_edit.setVisibility(View.GONE);
                e_text.setVisibility(View.VISIBLE);
                e_delete.setVisibility(View.GONE);
                return true;
            }
        });

        e_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                state.set(position, true);
                e_edit.setText(elements.get(position));
                e_text.setVisibility(View.GONE);
                e_edit.setVisibility(View.VISIBLE);
                e_delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        e_delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Log.v("pos", position + " " + elements.get(position));
                elements.remove(position);
                state.remove(position);
                notifyItemRangeChanged(position, elements.size());
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public void add(int pos){
        elements.add("");
        state.add(true);
        notifyItemInserted(pos);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
