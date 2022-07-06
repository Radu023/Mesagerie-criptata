package com.example.mesagerie_criptata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class activity_find_friends extends AppCompatActivity {
    viewholder_friends adapter; //adapter pentru incadrare in real estate-ul ecranului
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;
    ArrayList<Contacts> list_contacts=new ArrayList<>();
    private String currentUserID;
    FirebaseAuth mAuth;
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        //preluare date din Firebase
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        getSupportActionBar().setTitle("Adauga contacte");
        currentUserID = mAuth.getCurrentUser().getUid();
        fetch_friends(); //apelare metoda

    }

    @Override
    protected void onStart() {
        super.onStart();

        //constructor lasat gol

    }


    private void fetch_friends() {
        Helper.showLoader(activity_find_friends.this, "Va rugam asteptati . . .");
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_contacts.clear(); // eliberarea listei de contacte
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String uid = postSnapshot.child("uid").getValue(String.class);
                    String _nickname = postSnapshot.child("nickname").getValue(String.class);
                    String image = postSnapshot.child("image").getValue(String.class);
                    String status = postSnapshot.child("status").getValue(String.class);
                    if(currentUserID.equals(uid)){
                        // structura decizionala lasata goala, deoarece avem nevoie doar de else
                    }
                    else{
                        list_contacts.add(new Contacts(uid,_nickname,image,status));
                    }

                }


                if (list_contacts.size() > 0) {
                    viewholder_friends menu_adapter = new viewholder_friends(activity_find_friends.this, list_contacts);
                    FindFriendsRecyclerList.setAdapter(menu_adapter);
                    Helper.stopLoader();
                } else {
                    Helper.stopLoader();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

