package com.example.projectadmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.login.widget.ProfilePictureView.TAG;

public class ListCategory extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<Category> dataCategory;
    private ArrayList<String> categories;
    private Button searchBtn;
    private EditText searchEdt;

    private FirebaseAuth auth;
    private EditText getSearchEdt;
    private ListView listView;
    private ArrayList<String> categoryList = new ArrayList<>();
    private Dialog myDialog;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_category, container, false);
        getSearchEdt = view.findViewById(R.id.search_text);

        getSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }

        });


        final ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.category_item,
                R.id.category,
                categoryList);
        listView = view.findViewById(R.id.datalist);
        listView.setAdapter(categoryAdapter);
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SpinnerData");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                categoryList.add(value);
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(adapterView.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                /*
                                Bundle bundle = new Bundle();
                                bundle.putString("dataCategory",listView.getItemAtPosition(position).toString());
                                Intent intent = new Intent(view.getContext(), CategoryUpdate.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Update : " + listView.getItemAtPosition(position), Toast.LENGTH_SHORT).show();


                                 */
                                break;
                            case 1:
                                /*
                                final DatabaseReference database = FirebaseDatabase.getInstance().getReference("SpinnerData");
                                database.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                                            String value = item.getValue().toString();
                                            ArrayList<String> list = new ArrayList<>();
                                            list.add(value);
                                            Log.e("Value : ", value);
                                            Log.e("Value at Position : ", listView.getItemAtPosition(position).toString());
                                            for(int i =0;i<list.size();i++){
                                                if (list.get(i)==listView.getItemAtPosition(position)) {
                                                    database.getRef().removeValue();
                                                    Log.e("List At I : ",list.get(i));
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


                                 */
                                break;

                        }
                    }
                });
                alert.create();
                alert.show();

            }
        });
        return view;
    }

    private void filter(String text) {
        for (String item : categoryList) {
            if (item.toLowerCase().toString().equals(text)) {
                categoryList.add(item);
            }
        }
    }

    private void getData() {

    }
}