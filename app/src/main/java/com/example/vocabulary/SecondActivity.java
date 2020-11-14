package com.example.vocabulary;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SecondActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private RecyclerView recyclerViewForElements;
    private AdapterForCollections adapterForCollections;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        addButton = findViewById(R.id.add_button);
        recyclerViewForElements = findViewById(R.id.recyclerViewForCollections);
        recyclerViewForElements.setHasFixedSize(true);
        recyclerViewForElements.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewForElements.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterForCollections = new AdapterForCollections(this);
        recyclerViewForElements.setAdapter(adapterForCollections);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                adapterForCollections.add();
            }
        });
    }
}
