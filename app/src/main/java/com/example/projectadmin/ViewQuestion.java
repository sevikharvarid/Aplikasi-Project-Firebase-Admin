package com.example.projectadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.text.TextUtils.isEmpty;

public class ViewQuestion extends AppCompatActivity {

    TextView title, description, category;
    EditText replyText;
    Button replyButton, changeReplyButton;
    int editingAnswerID, editingQuestionID;
    private Answer editingAnswer;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private DatabaseReference getRef;
    private FirebaseListAdapter firebaseListAdapter;
    DatabaseReference database;
    private Toolbar toolbar;
    private String username, nip;
    private admin Admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_question);
        toolbar = findViewById(R.id.commToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Question");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        category = findViewById(R.id.category);
        replyButton = findViewById(R.id.addReply);
        changeReplyButton = findViewById(R.id.changeReply);
        replyText = findViewById(R.id.replyText);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ListView answerList = findViewById(R.id.answerList);

        final String getID = getIntent().getExtras().getString("question_id");
        getData();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getRef = database.child("Jawaban").child(getID);
        FirebaseListOptions<Answer> options = new FirebaseListOptions.Builder<Answer>()
                .setQuery(getRef,
                        Answer.class)
                .setLayout(R.layout.answer_layout)
                .build();
        firebaseListAdapter = new FirebaseListAdapter<Answer>(options) {
            @Override
            protected void populateView(View v, final Answer model, final int position) {
                TextView answer = v.findViewById(R.id.txtAnswer);
                TextView author = v.findViewById(R.id.txtAnswerBy);
                answer.setText(model.getDescription());
                author.setText("by " + model.getAuthor());

                ImageButton btnEdit = v.findViewById(R.id.btnEditAnswer);
                ImageButton btnDelete = v.findViewById(R.id.btnDeleteAnswer);

                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference dbRef2 = firebaseListAdapter.getRef(position);
                        dbRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                editingAnswer = dataSnapshot.getValue(Answer.class);
                                replyText.setText(dataSnapshot.child("description").getValue().toString());
                                changeReplyButton.setVisibility(View.VISIBLE);
                                replyButton.setVisibility(View.INVISIBLE);
                                changeReplyButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseDatabase database1 = FirebaseDatabase.getInstance();
                                        DatabaseReference dbRef = database1.getReference()
                                                .child("Jawaban")
                                                .child(getID)
                                                .child(dataSnapshot.getKey());
                                        editingAnswer.setDescription(replyText.getText().toString().trim());
                                        dbRef.setValue(editingAnswer);
                                        replyText.setText("");
                                        replyButton.setVisibility(View.VISIBLE);

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference dbRef = firebaseListAdapter.getRef(position);
                        dbRef.removeValue();
                    }
                });


            }
        };

        answerList.setAdapter(firebaseListAdapter);


        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyButton.setVisibility(View.INVISIBLE);
                // String getUserID = auth.getCurrentUser().getUid();
                String key = database.child("Question").push().getKey();
                // String getPrimaryKey = getIntent().getExtras().getString("getPrimaryKey");
                String textAnswer = replyText.getText().toString();
                if (isEmpty(textAnswer)) {
                    Toast.makeText(ViewQuestion.this, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show();
                } else {
                    String query = FirebaseDatabase.getInstance().getReference("Admin").child("Email").toString();
                    /*if (query == auth.getCurrentUser().getEmail()) {
                        username = FirebaseDatabase.getInstance().getReference("Admin").child("name").toString();
                        nip = FirebaseDatabase.getInstance().getReference("Admin").child("nip").toString();
                    }

                     */
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference getReference;
                    getReference = database.getReference().child("Jawaban").child(getID).push();
                    Admin = new admin();
                    Answer answer = new Answer(Admin.getName(), Admin.getNip(), auth.getCurrentUser().getUid(), replyText.getText().toString(), auth.getCurrentUser().getEmail());
                    getReference.setValue(answer).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            replyButton.setVisibility(View.VISIBLE);
                            replyText.setText("");
                            Toast.makeText(ViewQuestion.this, "Post Answer Succeed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ViewQuestion.this, "Post Answer Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getData() {
        final String getCategory = getIntent().getExtras().getString("dataCategory");
        final String getTitle = getIntent().getExtras().getString("dataAuthor");
        final String getDesc = getIntent().getExtras().getString("dataDescription");
        category.setText(getCategory);
        title.setText(getTitle);

        description.setText(getDesc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseListAdapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        firebaseListAdapter.stopListening();
    }
}
