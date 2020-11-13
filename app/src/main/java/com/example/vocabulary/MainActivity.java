package com.example.vocabulary;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity{
    Database_SQL myDB;
    private FloatingActionButton addButton;
    private RecyclerView recyclerViewForElements;
    private AdapterForElements adapterForElements;

    Boolean btnviselememt = true;
    Boolean btnviswords = false;
    Boolean btnvistranslation = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        addButton = findViewById(R.id.add_button);

        myDB = new Database_SQL(this);

        recyclerViewForElements = findViewById(R.id.recyclerViewForCollections);
        recyclerViewForElements.setHasFixedSize(true);
        recyclerViewForElements.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewForElements.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        final Toast warning = Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT);
        adapterForElements = new AdapterForElements(this);
        recyclerViewForElements.setAdapter(adapterForElements);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (btnviselememt || adapterForElements.getItemCount() == 0 || (btnviswords && btnvistranslation)){
                    buttonVisibilityForElements(true);//false
                    buttonVisibilityForWords(false);
                    buttonVisibilityForTranslation(false);
                    adapterForElements.add();
                }
                else
                    warning.show();
            }
        });

    }

    public void buttonVisibilityForElements(boolean statement){
        btnviselememt = statement;
    }
    public void buttonVisibilityForWords(boolean word){
        btnviswords = word;
    }
    public void buttonVisibilityForTranslation(boolean translation){
        btnvistranslation = translation;
    }
}