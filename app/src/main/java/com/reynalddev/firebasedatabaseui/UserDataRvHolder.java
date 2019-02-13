package com.reynalddev.firebasedatabaseui;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class UserDataRvHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView emailview, nameview;
    CheckBox checkBox;
    CardView userCardView ;
    UserItemClickListenner userItemClickListenner;

    public void setUserItemClickListenner(UserItemClickListenner userItemClickListenner) {
        this.userItemClickListenner = userItemClickListenner;
    }

    public UserDataRvHolder(@NonNull View itemView) {
        super(itemView);
        userCardView = (CardView)itemView.findViewById(R.id.userCardView);
        checkBox = (CheckBox)itemView.findViewById(R.id.deleteCheckBox);
        emailview = (TextView)itemView.findViewById(R.id.EmailView);
        nameview = (TextView)itemView.findViewById(R.id.NameView);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        userItemClickListenner.onClick(v, getAdapterPosition());
    }
}
