package id.sam.submiss2bfaa.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
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
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item, viewGroup, false);
        return new UserViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        userViewHolder.bind(mData.get(position));
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
