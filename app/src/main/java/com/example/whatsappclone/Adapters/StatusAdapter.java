package com.example.whatsappclone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.Activities.FragStatus;
import com.example.whatsappclone.Activities.MainActivity;
import com.example.whatsappclone.ModalClasses.Status;
import com.example.whatsappclone.ModalClasses.UserStatus;
import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.RecyclerviewStatusBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder>{

    FragmentManager contextFrag;
    Context context;
    ArrayList<UserStatus> userStatuses;

    public StatusAdapter(Context context,ArrayList<UserStatus> userStatuses){
        this.context = context;
        this.userStatuses = userStatuses;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_status,parent,false);
        return new StatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

        UserStatus userStatus = userStatuses.get(position);

       Status lastStatus = userStatus.getStatuses().get(userStatus.getStatuses().size() - 1);

       Glide.with(context).load(lastStatus.getImageUrl()).into(holder.recyclerviewStatusBinding.ivCircularImage);

        holder.recyclerviewStatusBinding.circularStatusView.setPortionsCount(userStatus.getStatuses().size());

        holder.recyclerviewStatusBinding.tvStatusName.setText(userStatus.getName());

        holder.recyclerviewStatusBinding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyStory> myStories = new ArrayList<>();

                for (Status status : userStatus.getStatuses()){
                    myStories.add(new MyStory(status.getImageUrl()));
                }

                long date = userStatus.getLastUpdated();
                DateFormat dateFormat = new SimpleDateFormat("hh:mm:ss");
                String strDate = dateFormat.format(date);

                new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName()) // Default is Hidden
                        .setSubtitleText(strDate) // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return userStatuses.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        RecyclerviewStatusBinding recyclerviewStatusBinding;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerviewStatusBinding = RecyclerviewStatusBinding.bind(itemView);

        }
    }
}
