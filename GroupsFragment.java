package com.example.mesagerie_criptata;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupsFragment extends Fragment
{
    private View groupFragmentView;
    private ListView list_view;
    private viewholder_groups adapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();
    private DatabaseReference GroupRef;
    RecyclerView recyclerView;

    public GroupsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");
        recyclerView = (RecyclerView) groupFragmentView.findViewById(R.id.group_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));




        adapter=new viewholder_groups(getActivity(),list_of_groups);
        recyclerView.setAdapter(adapter);
        RetrieveAndDisplayGroups();

        return groupFragmentView;
    }


    private void RetrieveAndDisplayGroups()
    {
        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext())
                {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                list_of_groups.clear();
                list_of_groups.addAll(set);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
