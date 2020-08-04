package com.example.projectadmin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private TextView registersignin;
    private Button register;
    private EditText name, email, nip, password1, password2;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        registersignin = findViewById(R.id.regsignin);
        name = findViewById(R.id.nametxt);
        email = findViewById(R.id.emailtxt);
        nip = findViewById(R.id.niptxt);
        password1 = findViewById(R.id.passwordtxt1);
        password2 = findViewById(R.id.passwordtxt2);

        register = findViewById(R.id.signupbtn);
        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerAdmin();

                if (validate() && registerAdmin()) {
                    final String admin_name = name.getText().toString().trim();
                    final String admin_email = email.getText().toString().trim();
                    final String admin_nip = nip.getText().toString().trim();
                    String admin_password = password1.getText().toString().trim();
                    firebaseAuth.createUserWithEmailAndPassword(admin_email, admin_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        admin Admin = new admin(
                                                admin_name,
                                                admin_nip,
                                                admin_email
                                        );
                                        FirebaseDatabase.getInstance().getReference("Admin")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(Admin);
                                        sendEmail();

                                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));


                                    } else {
                                        Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();


        registersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SignupActivity.this, LoginActivity.class);

                startActivity(i);
            }
        });

    }

    private Boolean validate() {
        Boolean result = false;

        String nam = name.getText().toString();
        String ema = email.getText().toString();
        String ni = nip.getText().toString();
        String pass = password1.getText().toString();
        String pass2 = password2.getText().toString();

        if (nam.isEmpty() || ema.isEmpty() || ni.isEmpty() || pass.isEmpty() || pass2.isEmpty()) {
            Context context = getApplicationContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastroot = inflater.inflate(R.layout.emptyfield_toast, null);
            Toast customToast = new Toast(context);

            customToast.setView(customToastroot);
            customToast.setDuration(Toast.LENGTH_LONG);
            customToast.show();

        } else {

            result = true;
        }

        return result;
    }

    private boolean registerAdmin() {
        Boolean result = false;

        String nam = name.getText().toString();
        String ema = email.getText().toString();
        String ni = nip.getText().toString();
        String pass = password1.getText().toString();
        String pass2 = password2.getText().toString();

        if (pass.length() < 6) {
            password1.setError("Password should be at least 6 characters long");
            password1.requestFocus();
            return false;
        }

        if (pass2.length() < 6) {
            password2.setError("Password should be at least 6 characters long");
            password2.requestFocus();
            return false;
        } else {
            result = true;
        }

        return result;
    }

    private void sendEmail() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(SignupActivity.this, "Successfully registered, Verification email sent", Toast.LENGTH_LONG).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(SignupActivity.this, "Verification email hasn't been sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
