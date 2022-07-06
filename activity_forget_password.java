package com.example.mesagerie_criptata;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class activity_forget_password extends AppCompatActivity {

    EditText email_ediitext;
    Button btn_recover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        email_ediitext=findViewById(R.id.txt_email); // initializare editarii

        btn_recover=findViewById(R.id.btn_recover);
        btn_recover.setOnClickListener(new View.OnClickListener() { // pentru recover mail click event
            @Override
            public void onClick(View view) {

                String email=email_ediitext.getText().toString();
                if(email.equals("")){ // verificam daca s-a introdus adresa
                    Toast.makeText(activity_forget_password.this,"Va rugam introduceti adresa de mail...",Toast.LENGTH_LONG).show();

                }
                else{
                    Helper.showLoader(activity_forget_password.this,"Va rugam asteptati . . .");
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email) // folosim firebase Auth Password reset function pentru a trimite un mail la adresa inregistrata cu un link de resetare
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) { // daca s-a trimis mail-ul cu succes, trimitem un mesaj toast
                                        Toast.makeText(activity_forget_password.this, "Email trimis la "+email, Toast.LENGTH_SHORT).show();
                                        Helper.stopLoader();
                                    }
                                    else{ // daca nu merge, trimitem un mesaj tip exception si inchidem dialogul
                                        Toast.makeText(activity_forget_password.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        Helper.stopLoader();
                                    }
                                }
                            });
                }

            }
        });
    }
}