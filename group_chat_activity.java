package com.example.mesagerie_criptata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class group_chat_activity extends AppCompatActivity {
    private ImageButton SendMessageButton;
    private EditText userMessageInput;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef,GroupNameRef,GroupMessageKeyRef;
    private String currentGroupName,currentUserID,currentUserName,currentDate,currentTime;
    RecyclerView recyclerView;
    viewholder_group_messages viewholder_group_messages;
    ArrayList<GroupMessages> list_group_messages=new ArrayList<>();

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            currentGroupName = extras.getString("groupName");
        }





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        GroupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);
        getSupportActionBar().setTitle(currentGroupName);
        recyclerView = (RecyclerView) findViewById(R.id.group_list_messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(group_chat_activity.this));
        viewholder_group_messages=new viewholder_group_messages(group_chat_activity.this,list_group_messages);
        recyclerView.setAdapter(viewholder_group_messages);
        SendMessageButton = (ImageButton) findViewById(R.id.send_message_button);
        userMessageInput = (EditText) findViewById(R.id.input_group_message);

        GetUserInfo();


        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                try {
                    SaveMessageInfoToDatabase();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                userMessageInput.setText("");

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        list_group_messages.clear();
        GroupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
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
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
       //
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext())
        {

            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();

            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();
            Log.i("Detect",chatTime);
            list_group_messages.add(new GroupMessages(chatDate,chatMessage,chatName,chatTime));

        }
        viewholder_group_messages.notifyDataSetChanged();
      //  recyclerView.setAdapter(viewholder_group_messages);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveMessageInfoToDatabase() throws UnsupportedEncodingException {
       String message = userMessageInput.getText().toString();
       String messageKEY = GroupNameRef.push().getKey();
        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(this, "Va rugam scrieti un mesaj...", Toast.LENGTH_SHORT).show();
        }
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate = currentDateFormat.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
        currentTime = currentTimeFormat.format(calForTime.getTime());

        HashMap<String, Object> groupMessageKey = new HashMap<>();
        GroupNameRef.updateChildren(groupMessageKey);

        GroupMessageKeyRef = GroupNameRef.child(messageKEY);

        HashMap<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("name", currentUserName);
        messageInfoMap.put("message", Helper.encrypt(message));
        messageInfoMap.put("date", currentDate);
        messageInfoMap.put("time", currentTime);
        GroupMessageKeyRef.updateChildren(messageInfoMap);
    }

    private void GetUserInfo()
    {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("nickname").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }








}
