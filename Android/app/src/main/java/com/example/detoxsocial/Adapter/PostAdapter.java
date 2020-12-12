package com.example.detoxsocial.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.detoxsocial.Model.Post;
import com.example.detoxsocial.Model.User;
import com.example.detoxsocial.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post = mPost.get(position);

        Glide.with(mContext).load(post.getPostImage()).into(holder.postImage);

        if(post.getCaption().equals("")){
            holder.caption.setVisibility(View.GONE);
        }
        else {
            holder.caption.setVisibility(View.VISIBLE);
            holder.caption.setText(post.getCaption());
        }

        publisherInfo(holder.imageProfile,holder.username,post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageProfile, postImage,like,comment;
        public TextView username, likes, caption, comments;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.home_profileImage);
            postImage = itemView.findViewById(R.id.home_postImage);
            like = itemView.findViewById(R.id.home_like);
            comment = itemView.findViewById(R.id.home_comment);
            username = itemView.findViewById(R.id.home_username);
            likes = itemView.findViewById(R.id.home_likesCount);
            caption = itemView.findViewById(R.id.home_caption);
            comments = itemView.findViewById(R.id.comments);
        }
    }

    private void publisherInfo(final ImageView imageProfile, final TextView username, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Glide.with(mContext).load(user.getImageUrl()).into(imageProfile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
