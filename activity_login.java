package com.example.mesagerie_criptata;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class activity_login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btn_login;
    private EditText txt_email, txt_password;
    private TextView txt_signup, txt_forget;
    private DatabaseReference UsersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //accesare date Firebase
        mAuth = FirebaseAuth.getInstance();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        btn_login = (Button) findViewById(R.id.btn_signin);
        txt_email = (EditText) findViewById(R.id.txt_email);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_signup = (TextView) findViewById(R.id.txt_signup);
        txt_forget = (TextView) findViewById(R.id.txt_forget);
        txt_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //declarare intent
                Intent registerIntent = new Intent(activity_login.this,activity_forget_password.class);
                startActivity(registerIntent);
            }
        });
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(activity_login.this,activity_signup.class);
                startActivity(registerIntent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txt_email.getText().toString();
                String password = txt_password.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(activity_login.this,"Va rugam introduceti mail-ul",Toast.LENGTH_SHORT).show();
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(activity_login.this,"Va rugam introduceti parola",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Helper.showLoader(activity_login.this,"Va rugam asteptati . . .");
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                String deviceToken = "";
                                UsersRef.child(currentUserId).child("device_Token").setValue(deviceToken).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Helper.stopLoader();
                                            SendUserToMainActivity();
                                            Toast.makeText(activity_login.this, "Autentificare cu succes...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                                Helper.stopLoader();
                                String message = task.getException().toString();
                                Toast.makeText(activity_login.this, "Eroare : " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });


    }


    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            SendUserToMainActivity();
        }
    }




    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(activity_login.this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    private void SendUserToRegisterActivity() {

    }
}