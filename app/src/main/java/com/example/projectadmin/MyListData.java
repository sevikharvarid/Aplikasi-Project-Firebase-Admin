package com.example.projectadmin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.login.widget.ProfilePictureView.TAG;

public class MyListData extends Fragment implements RecyclerViewAdapter.dataListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<Question> dataQuestion;
    private Button searchBtn;
    private EditText searchEdt;

    private FirebaseAuth auth;

    public static MyListData newInstance() {
        return new MyListData();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_list_data, container, false);


        recyclerView = view.findViewById(R.id.datalist);
        auth = FirebaseAuth.getInstance();
        searchBtn = view.findViewById(R.id.search_button);
        searchEdt = view.findViewById(R.id.search_text);
        MyRecyclerView();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence search = searchEdt.getText().toString().toLowerCase();
                GetDataBySearch(search.toString());
            }

        });
        GetData();

        return view;
    }

    private void GetDataBySearch(final String search) {
        reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.push().getKey();
        reference.child("Question")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataQuestion = new ArrayList<>();
                        Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) dataSnapshot.getValue();
                        if (map != null) {
                            for (String key : map.keySet()) {
                                Map<String, ArrayList<String>> map2 = (Map<String, ArrayList<String>>) map.get(key);
                                Gson gson = new Gson();
                                String test = gson.toJson(map2);
                                Question question = new Question();
                              //  question.setKey();
                                question = gson.fromJson(test, Question.class);
                                if (question.getDescription().toLowerCase().contains(search)) {
                                    dataQuestion.add(question);

                                }


                            }
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataQuestion, getContext(), MyListData.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });

    }

    private void GetData() {
        reference = FirebaseDatabase.getInstance().getReference();
        String key = reference.push().getKey();
        reference.child("Question")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataQuestion = new ArrayList<>();
                        Map<String, ArrayList<String>> map = (Map<String, ArrayList<String>>) dataSnapshot.getValue();

                        if (map != null) {
                            for (String key : map.keySet()) {
                                Map<String, ArrayList<String>> map2 = (Map<String, ArrayList<String>>) map.get(key);
                                Gson gson = new Gson();
                                String test = gson.toJson(map2);
                                Question question = new Question();
                                question = gson.fromJson(test, Question.class);
                                dataQuestion.add(question);
                                Log.e("Nilai Field : ", test);

                            }
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(dataQuestion, getContext(), MyListData.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails() + " " + databaseError.getMessage());
                    }
                });
    }

    private void MyRecyclerView() {
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 0));

    }


    @Override
    public void onDeleteData(Question data, int position) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference1 = database.getReference().child("Jawaban");
        final DatabaseReference reference2 = database.getReference();
        if (reference1 != null) {
            reference1
                    .child(data.getId())
                    .removeValue();
        }
        Query query = reference2.child("Question").orderByChild("id").equalTo(data.getId());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot Snapshot: dataSnapshot.getChildren()) {
                    Snapshot.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
        }

    }