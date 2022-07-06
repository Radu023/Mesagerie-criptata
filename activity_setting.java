package com.example.mesagerie_criptata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
//import com.theartofdev.edmodo.cropper.CropImage;
//import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class activity_setting extends AppCompatActivity {

    private DatabaseReference RootRef;
    private FirebaseAuth mAuth;
    private String currentUserID;
    private Button UpdateAccountSettings;
    private EditText nickname, userStatus;
    private CircleImageView userProfileImage;
    private static final int GalleryPick = 1;
    private StorageReference UserProfileImagesRef;
    private ProgressDialog loadingBar;
    boolean flag_image_select=false,already_uploaded_image=false;
    private Uri path;
    Bitmap profile_btimap=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        UserProfileImagesRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        InitializeFields();

        UpdateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             if(already_uploaded_image){
                 UpdateSettings();
             }
             else{
                 uploadFile();
             }
            }
        });

        RetrieveUserInfo();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GalleryPick);
            }
        });
    }


    private void UpdateSettings() {
        String _nickname = nickname.getText().toString();
        String setStatus = userStatus.getText().toString();
        if (TextUtils.isEmpty(_nickname)) {
            Toast.makeText(this, "Va rugam introduceti numele....", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(setStatus)) {
            Toast.makeText(this, "Va rugam introduceti un status....", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("nickname", _nickname);
            profileMap.put("status", setStatus);

            RootRef.child("Users").child(currentUserID).updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    //    Helper.stopLoader();
                        SendUserToMainActivity();

                        Toast.makeText(activity_setting.this, "Profil actualizat cu succes...", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = task.getException().toString();
                        Toast.makeText(activity_setting.this, "Eroare: " + message, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void RetrieveUserInfo() {
        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("nickname") && (dataSnapshot.hasChild("image")))) {
                  //  String retrieveUserName = dataSnapshot.child("name").toString();
                    String retrieveNickName = dataSnapshot.child("nickname").getValue(String.class);
                    String retrieveStatus = dataSnapshot.child("status").getValue(String.class);
                    String retrieveProfilePhoto = dataSnapshot.child("image").getValue(String.class);

                    nickname.setText(retrieveNickName);
                    userStatus.setText(retrieveStatus);
                    Picasso.get().load(retrieveProfilePhoto).placeholder(R.drawable.loading).into(userProfileImage);
                    already_uploaded_image=true;
                } else if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("nickname"))) {
                    String retrieveUserName = dataSnapshot.child("nickname").getValue().toString();
                    String retrievesStatus = dataSnapshot.child("status").getValue().toString();

                    nickname.setText(retrieveUserName);
                    userStatus.setText(retrievesStatus);
                    already_uploaded_image=false;
                } else {
                    nickname.setVisibility(View.VISIBLE);
                    already_uploaded_image=false;

                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(activity_setting.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void InitializeFields() {
        UpdateAccountSettings = (Button) findViewById(R.id.update_settings_button);
        nickname = (EditText) findViewById(R.id.nick_name);
        userStatus = (EditText) findViewById(R.id.set_profile_status);
        userProfileImage = (CircleImageView) findViewById(R.id.set_profile_image);

        loadingBar = new ProgressDialog(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Setari cont");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null && data.getData() != null) {
            path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                userProfileImage.setImageBitmap(bitmap);
                flag_image_select=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void uploadFile() {


        if (!flag_image_select) {
            Toast.makeText(activity_setting.this,"Va rugam selectati o imagine",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            Helper.showLoader(activity_setting.this,"Va rugam asteptati . . .");
            StorageReference sRef = UserProfileImagesRef.child(currentUserID + ".jpg");
            sRef.putFile(path).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl = uri;
                            RootRef.child("Users").child(currentUserID).child("image")
                                    .setValue(downloadUrl.toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(activity_setting.this, "Imaginea a fost salvata in baza de date", Toast.LENGTH_SHORT).show();

                                                UpdateSettings();
                                            } else {
                                                String message = task.getException().toString();
                                                Toast.makeText(activity_setting.this, "Eroare: " + message, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Helper.stopLoader();
                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(activity_setting.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(activity_setting.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        return true;
    }

}

