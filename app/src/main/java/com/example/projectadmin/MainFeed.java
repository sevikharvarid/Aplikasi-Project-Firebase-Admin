package com.example.projectadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainFeed extends AppCompatActivity {


    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private ImageView profileIcon;
    private ListCategory listCategory;
    private MyListData myListData;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_feed);
        toolbar = findViewById(R.id.includedToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.common_toolbar);


        myListData = new MyListData();
        bottomNavigationView = findViewById(R.id.mainNavBar);
        frameLayout = findViewById(R.id.navFrame);
        listCategory = new ListCategory();

        changeFragment(myListData);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navBtnHome:
                        changeFragment(myListData);
                        return true;
                    case R.id.navBtnListCategory:
                        changeFragment(listCategory);
                        return true;
                    case R.id.navBtnAddCategory:
                        Intent i = new Intent(MainFeed.this, AddCategory.class);
                        startActivity(i);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOut:

                FirebaseAuth.getInstance().signOut();
                Intent out = new Intent(MainFeed.this, LoginActivity.class);

                out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Confirm Exit")
                .setIcon(R.drawable.ic_exit_to_app_black_24dp)
                .setMessage("Are you sure want to exit ?")
                .setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialogBuilder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        changeFragment(myListData);
    }

    public void ChangeToHome() {
        changeFragment(myListData);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
    }

    private void changeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.navFrame, fragment);
        fragmentTransaction.commit();

    }
}
