package com.example.barcodescanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barcodescanner.Data.User;
import com.example.barcodescanner.R;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> list;

    public UserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_users, parent, false);

        return new UserAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = list.get(position);
        holder.Email.setText(user.getEmail());
        holder.Password.setText(user.getPassword());
        holder.TopBadge.setText(user.getTopBadge());
        holder.Uid.setText(user.getUid());
        holder.Username.setText(user.getUsername());
        holder.dateofbirth.setText(user.getDateofbirth());
        holder.imageuri.setText(user.getImageuri());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Email, Password, TopBadge, Uid, Username, dateofbirth, imageuri;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Email = itemView.findViewById(R.id.useremail);

            Password = itemView.findViewById(R.id.userpass);
            TopBadge = itemView.findViewById(R.id.top);
            Uid = itemView.findViewById(R.id.userid);
            Username = itemView.findViewById(R.id.username);
            dateofbirth = itemView.findViewById(R.id.userdate);
            imageuri = itemView.findViewById(R.id.uri);
            imageuri.setVisibility(View.GONE);
            TopBadge.setVisibility(View.GONE);
        }

    }
}