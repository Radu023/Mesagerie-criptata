package com.example.mesagerie_criptata;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class viewholder_groups extends RecyclerView.Adapter<viewholder_groups.groups> {

    Context context;
    ArrayList<String> list_group;


    public viewholder_groups(Context c, ArrayList<String> list) {
        context = c;
        list_group = list;
    }


    @NonNull
    @Override
    public groups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new groups(LayoutInflater.from(context).inflate(R.layout.groups_list_layout, parent, false));
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull groups holder, final int position) {

        holder.name.setText(list_group.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentGroupName = list_group.get(position);
                Intent groupChatIntent = new Intent(context, group_chat_activity.class);
                groupChatIntent.putExtra("groupName" , currentGroupName);

                context.startActivity(groupChatIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_group.size();
    }

    class groups extends RecyclerView.ViewHolder{

        TextView name,price;
        ImageView image;
        CardView cardView;

        public groups(View itemView){
            super(itemView);
            name  = (TextView) itemView.findViewById(R.id.group_name);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
        }
    }



}



