package in.kumarharsh.informza.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import in.kumarharsh.informza.R;
import in.kumarharsh.informza.models.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    ArrayList<Post> data;
    Context context;

    public PostsAdapter(Context context){
        data = new ArrayList<>();
        this.context = context;
    }

    public void setData(ArrayList<Post> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.postrecycleritem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {

        holder.postAuthor.setText(data.get(position).getAuthorName());
        holder.postText.setText(data.get(position).getPostText());

        Glide.with(context)
                .load(Uri.parse(data.get(position).getAuthorProfilePhoto()))
                .apply(new RequestOptions().circleCrop())
                .into(holder.profilePhoto);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView profilePhoto;
        public TextView postText;
        public TextView postAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePhoto = itemView.findViewById(R.id.author_profile_photo);
            postText = itemView.findViewById(R.id.post_text);
            postAuthor = itemView.findViewById(R.id.author_name);
        }
    }
}
