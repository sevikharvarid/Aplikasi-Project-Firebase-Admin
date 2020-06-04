package com.example.faq_chatapp_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.suggestedevents.ViewOnClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCategory extends AppCompatActivity {

    private Button btnAdd,btnOut;
    private TextView btnShow,email,questionTotal;
    private EditText text;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        text = findViewById(R.id.edt);
        questionTotal = findViewById(R.id.questionTotal);
        email = findViewById(R.id.emailAdmin);
        btnAdd = findViewById(R.id.btnUpdate);
        btnOut = findViewById(R.id.btnSignout);
        btnShow = findViewById(R.id.btnShowQuestion);
        databaseReference = FirebaseDatabase.getInstance().getReference("SpinnerData");
        //email.setText();
        //questionTotal.setText();
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddCategory.this,MainFeed.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textdata = text.getText().toString().trim();
                databaseReference.push().setValue(textdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        text.setText("");
                        Toast.makeText(AddCategory.this,"New Category Inserted",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(AddCategory.this,act2.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

            }
        });


    }

}
