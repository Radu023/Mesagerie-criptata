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

import de.hdodenhof.circleimageview.CircleImageView;


public class viewholder_friends extends RecyclerView.Adapter<viewholder_friends.friends> {

    Context context;
    ArrayList<Contacts> list_contacts;


    public viewholder_friends(Context c, ArrayList<Contacts> list) {
        context = c;
        list_contacts = list;
    }


    @NonNull
    @Override
    public friends onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new friends(LayoutInflater.from(context).inflate(R.layout.users_display_layout, parent, false));
    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull friends holder, final int position) {

        holder.userName.setText(list_contacts.get(position).getName());
        holder.userStatus.setText(list_contacts.get(position).getStatus());
        Picasso.get().load(list_contacts.get(position).getImage()).placeholder(R.drawable.loading).into(holder.profileImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent profileIntent = new Intent(context, activity_profile.class);
                profileIntent.putExtra("user_id", list_contacts.get(position).getUid());
                context.startActivity(profileIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list_contacts.size();
    }

    class friends extends RecyclerView.ViewHolder{

        TextView userName,userStatus;
        CircleImageView profileImage;
        CardView cardView;

        public friends(View itemView){
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userStatus = itemView.findViewById(R.id.user_status);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
        }
    }



}



