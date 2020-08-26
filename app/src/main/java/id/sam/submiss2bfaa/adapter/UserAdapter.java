package id.sam.submiss2bfaa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import id.sam.submiss2bfaa.DetailActivity;
import id.sam.submiss2bfaa.R;
import id.sam.submiss2bfaa.model.UserItems;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<UserItems> mData = new ArrayList<>();

    public void setData(ArrayList<UserItems> items){
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        final UserViewHolder userViewHolder = new UserViewHolder(mView);

        userViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(userViewHolder.itemView.getContext(), mData.get(userViewHolder.getAdapterPosition()).getUsername(),Toast.LENGTH_SHORT).show();
                Context context = mView.getContext();
                int position = userViewHolder.getAdapterPosition();
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("detail", mData.get(position).getUsername());
                context.startActivity(intent);
            }
        });
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        holder.bind(mData.get(position));
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(holder.itemView.getContext(), mData.get(holder.getAdapterPosition()).getUsername(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtUsername;
        TextView txtUrl;
        CircleImageView imgAvatar;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtUrl = itemView.findViewById(R.id.txtUrl);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }

        void bind(UserItems userItems){
            txtUsername.setText(userItems.getUsername());
            txtUrl.setText(userItems.getUrl());
            if (userItems.getAvatar() != null && !userItems.getAvatar().isEmpty()) {
                Picasso.get().load(userItems.getAvatar()).transform(new CropCircleTransformation()).into(imgAvatar);
            }
        }
    }
}
