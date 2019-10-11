package in.kumarharsh.informza;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;

import in.kumarharsh.informza.models.Post;
import in.kumarharsh.informza.models.User;

public class PostsHandler {

    Context context;
    OnFetchedListener onFetchedListener;
    OnCreatedListener onCreatedListener;

    public PostsHandler(Context context) {
        this.context = context;
    }

    public void addOnFetchedListener(OnFetchedListener onFetchedListener) {
        this.onFetchedListener = onFetchedListener;
    }

    public void addOnCreatedListener(OnCreatedListener onCreatedListener) {
        this.onCreatedListener = onCreatedListener;
    }

    public PostsHandler fetchAllPosts() {
        FirebaseFirestore.getInstance().collection("POSTS").orderBy("time", Query.Direction.DESCENDING).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Post> posts = new ArrayList<>();
                        for (DocumentSnapshot ds : queryDocumentSnapshots.getDocuments()) {
                            posts.add(ds.toObject(Post.class));
                        }
                        onFetchedListener.onSuccess(posts);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFetchedListener.onFailure(e.getMessage());
            }
        });
        return this;
    }

    public PostsHandler fetchMyPosts() {
        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final User me = documentSnapshot.toObject(User.class);
                        final ArrayList<Post> posts = new ArrayList<>();
                        for (String s : me.getMyPosts()) {
                            FirebaseFirestore.getInstance().collection("POSTS")
                                    .document(s)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            posts.add(documentSnapshot.toObject(Post.class));
                                            if (posts.size() == me.getMyPosts().size()) {
                                                Log.i("DEBUG", "MY POSTS FETCHED");
                                                onFetchedListener.onSuccess(posts);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            onFetchedListener.onFailure(e.getMessage());
                                        }
                                    });
                        }
                    }
                });
        return this;
    }

    public PostsHandler createPost(final Post post) {
        FirebaseFirestore.getInstance().collection("POSTS")
                .document(post.getId())
                .set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseFirestore.getInstance().collection("USERS")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        final User user = documentSnapshot.toObject(User.class);
                                        ArrayList<String> posts = user.getMyPosts();
                                        posts.add(post.getId());
                                        user.setMyPosts(posts);

                                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                onCreatedListener.onSuccess();
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        onCreatedListener.onFailure(e.getMessage());
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        onCreatedListener.onFailure(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onCreatedListener.onFailure(e.getMessage());
                    }
                });
        return this;
    }

    public interface OnFetchedListener {
        void onSuccess(ArrayList<Post> posts);

        void onFailure(String errorMessage);
    }

    public interface OnCreatedListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}
