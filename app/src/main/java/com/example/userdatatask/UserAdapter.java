package com.example.userdatatask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserView> {
    List<UserModel> userModels;
    Context context;

    public UserAdapter(List<UserModel> userModels, Context context) {
        this.userModels = userModels;
        this.context = context;
    }

    @NonNull
    @Override
    public UserView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.user_list, parent, false);
        return new UserAdapter.UserView(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserView holder, int position) {

        holder.userId.setText(userModels.get(position).getUserId());
        holder.id.setText(userModels.get(position).getId());
        holder.title.setText(userModels.get(position).getTitle());
        holder.body.setText(userModels.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    public class UserView extends RecyclerView.ViewHolder {
        TextView userId,id,title,body;
        public UserView(@NonNull View itemView) {
            super(itemView);
            userId=itemView.findViewById(R.id.user_id);
            id=itemView.findViewById(R.id.id);
            title=itemView.findViewById(R.id.title);
            body=itemView.findViewById(R.id.body);
        }
    }
}
