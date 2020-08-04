package com.example.projectadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddCategory extends AppCompatActivity {

    private Button btnAdd;
    private TextView btnShow, email, btnCat, questionTotal, username;
    private EditText text;
    private Uri filePath;
    DatabaseReference databaseReference;
    CircleImageView profileImageView;
    int TAKE_IMAGE_CODE = 10001;
    int GET_IMAGE_CODE = 10002;
    FirebaseAuth auth;
    Context context;
    Toolbar toolbar;
    private ListCategory listCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        toolbar = findViewById(R.id.includedToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.common_toolbar);
        listCategory = new ListCategory();
        text = findViewById(R.id.edt);
        profileImageView = findViewById(R.id.profileImageView);
        btnAdd = findViewById(R.id.btnUpdate);
        btnShow = findViewById(R.id.btnShowQuestion);
        btnCat = findViewById(R.id.btnShowCategory);
        username = findViewById(R.id.getname);
        auth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Admin");
        ref.child(auth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        username.setText(dataSnapshot.child("name").getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("SpinnerData");
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddCategory.this, MainFeed.class);
                startActivity(i);
            }
        });
        btnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(listCategory);
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(profileImageView);
            }
        }

    }

    private void changeFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.navFrame, fragment);
        fragmentTransaction.commit();

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
                Intent out = new Intent(AddCategory.this, LoginActivity.class);

                out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                out.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textdata = text.getText().toString().trim();
                if (textdata.isEmpty()) {
                    Toast.makeText(AddCategory.this, "Kolom Kategori harap di isi", Toast.LENGTH_SHORT).show();
                    text.requestFocus();
                } else {
                    databaseReference.push().setValue(textdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            text.setText("");
                            Toast.makeText(AddCategory.this, "New Category Inserted", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void ImageOnClick(View view) {
        SelectImage(AddCategory.this);
    }

    public void SelectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    HandleImageOnCapture();
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickPhoto.setType("image/*");
                    startActivityForResult(pickPhoto, GET_IMAGE_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void HandleImageOnCapture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_IMAGE_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_IMAGE_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    profileImageView.setImageBitmap(bitmap);
                    handleUp(bitmap);
            }
        } else if (requestCode == GET_IMAGE_CODE && data != null && data.getData() != null) {
            switch (resultCode) {
                case RESULT_OK:
                    filePath = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    profileImageView.setImageBitmap(bitmap);
                    handleUp(bitmap);
            }
        }
    }

    private void handleUp(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("profileimages")
                .child(uid + " .jpeg");
        reference.putBytes(baos.toByteArray())
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        getDownloadUrl(reference);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("On Failure : ", e.getCause().toString());
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e("On Success : ", uri.toString());
                        setUserProfileUrl(uri);
                        String imageReference = uri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Admin");
                        reference.child(auth.getUid()).child("imageUrl").setValue(imageReference);
                    }
                });
    }

    private void setUserProfileUrl(Uri uri) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        user.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddCategory.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddCategory.this, "Profile Image Failed..... ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}