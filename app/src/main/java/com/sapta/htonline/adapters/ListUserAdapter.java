package com.sapta.htonline.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sapta.htonline.R;
import com.sapta.htonline.service.model.User;

import java.util.List;

/**
 * Created by Dimas Maulana on 5/26/17.
 * Email : araymaulana66@gmail.com
 */

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.UserViewHolder> {
    private List<User> dataList;

    public ListUserAdapter(Context context, List<User> dataList) {
        this.dataList = dataList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rec_list_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = dataList.get(position);
        holder.txtUser.setText(user.name);

        Log.d("BICARA", user.talkingState+"");
        if(user.talkingState == 1) {
            holder.imgMicOn.setVisibility(View.VISIBLE);
        } else {
            holder.imgMicOn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgMicOn;
        private TextView txtUser;
        private CardView cardView;
        private View view;

        public UserViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            txtUser = itemView.findViewById(R.id.txt_user);
            imgMicOn = itemView.findViewById(R.id.img_mic_on);
        }

    }

}