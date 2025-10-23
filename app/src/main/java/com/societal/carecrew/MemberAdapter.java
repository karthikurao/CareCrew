package com.societal.carecrew;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private List<String> memberIds;

    public MemberAdapter(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        String memberId = memberIds.get(position);

        // Fetch member details from Firebase
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(memberId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                    if (name != null) {
                        holder.memberNameTextView.setText(name);
                    } else {
                        holder.memberNameTextView.setText("Unknown User");
                    }

                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(holder.itemView.getContext())
                                .load(profileImageUrl)
                                .placeholder(R.drawable.ic_profile)
                                .into(holder.memberImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                holder.memberNameTextView.setText("Unknown User");
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberIds.size();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        ImageView memberImageView;
        TextView memberNameTextView;

        public MemberViewHolder(View itemView) {
            super(itemView);
            memberImageView = itemView.findViewById(R.id.memberImageView);
            memberNameTextView = itemView.findViewById(R.id.memberNameTextView);
        }
    }
}
