package com.example.projectadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CategoryUpdate extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_update);
        toolbar = findViewById(R.id.commToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Update Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final String getCategory = getIntent().getExtras().getString("dataCategory");

        button = findViewById(R.id.btnUpdateCat);
        editText = findViewById(R.id.edtTxtCat);
        editText.setText(getCategory);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }




}
