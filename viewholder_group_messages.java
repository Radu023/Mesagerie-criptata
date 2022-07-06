package com.example.mesagerie_criptata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class viewholder_group_messages extends RecyclerView.Adapter<viewholder_group_messages.groups> {

    Context context;
    ArrayList<GroupMessages> list_group_messages;

    public viewholder_group_messages(Context c, ArrayList<GroupMessages> list) {
        context = c;
        list_group_messages = list;

    }


    @NonNull
    @Override
    public groups onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new groups(LayoutInflater.from(context).inflate(R.layout.group_messages_list_layout, parent, false));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull groups holder, final int position) {


            holder.name.setText(list_group_messages.get(position).getName());
            holder.message.setText(Helper.decrypt(list_group_messages.get(position).getMessage()));
            holder.date_time.setText(list_group_messages.get(position).getDate()+" "+list_group_messages.get(position).getTime());




    }

    @Override
    public int getItemCount() {
        return list_group_messages.size();
    }

    class groups extends RecyclerView.ViewHolder{

        TextView name,message,date_time;
        ImageView image;
        CardView cardView;

        public groups(View itemView){
            super(itemView);
            name  = (TextView) itemView.findViewById(R.id.sender);
            message  = (TextView) itemView.findViewById(R.id.message);
            date_time  = (TextView) itemView.findViewById(R.id.date_time);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
        }
    }




}




