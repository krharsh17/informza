package in.kumarharsh.informza.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.kumarharsh.informza.PostsHandler;
import in.kumarharsh.informza.R;
import in.kumarharsh.informza.adapters.PostsAdapter;
import in.kumarharsh.informza.models.Post;

public class PostsFragment extends Fragment {

    int type;
    RecyclerView postsRecycler;
    PostsAdapter postsAdapter;
    TextView dummy;

    public PostsFragment() {
        // Required empty public constructor
    }

    public PostsFragment(int type) {
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        assignVariables();
        setup();

    }

    void assignVariables(){
        postsRecycler = getView().findViewById(R.id.posts_recycler);
    }

    void setup(){
        postsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        postsAdapter = new PostsAdapter(getActivity());
        postsRecycler.setAdapter(postsAdapter);
        if(type == 0) {
            new PostsHandler(getActivity()).fetchAllPosts()
                    .addOnFetchedListener(new PostsHandler.OnFetchedListener() {
                        @Override
                        public void onSuccess(ArrayList<Post> posts) {
                            for(Post p : posts){
                                Log.i("DEBUG", p.getId());
                            }
                            postsAdapter.setData(posts);
                            postsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.i("DEBUG", errorMessage);
                        }
                    });
        } else {
            new PostsHandler(getActivity()).fetchMyPosts()
                    .addOnFetchedListener(new PostsHandler.OnFetchedListener() {
                        @Override
                        public void onSuccess(ArrayList<Post> posts) {
                            for(Post p : posts){
                                Log.i("DEBUG", p.getId());
                            }
                            postsAdapter.setData(posts);
                            postsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Log.i("DEBUG", errorMessage);
                        }
                    });
        }
    }
}
