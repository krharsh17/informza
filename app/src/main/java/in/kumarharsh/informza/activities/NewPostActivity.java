package in.kumarharsh.informza.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

import in.kumarharsh.informza.PostsHandler;
import in.kumarharsh.informza.R;
import in.kumarharsh.informza.models.Post;

public class NewPostActivity extends AppCompatActivity {

    ImageView profilePhoto;
    TextView authorName;
    EditText postText;
    CardView postButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        assignVariables();
        setup();
    }

    void assignVariables(){
        profilePhoto = findViewById(R.id.new_post_profile_photo);
        authorName = findViewById(R.id.new_post_author_name);
        postText = findViewById(R.id.new_post_text);
        postButton = findViewById(R.id.create_new_post);
    }

    void setup(){
        Glide.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .apply(new RequestOptions().circleCrop())
                .into(profilePhoto);

        authorName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(postText.getText().toString().length()!=0){
                    postButton.animate().scaleX(0f).setDuration(500);
                    postButton.animate().scaleY(0f).setDuration(500);
                    Toast.makeText(NewPostActivity.this, "Creating post...", Toast.LENGTH_SHORT).show();
                    new PostsHandler(NewPostActivity.this)
                            .createPost(new Post(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                                    Calendar.getInstance().getTime().toString(),
                                    postText.getText().toString(),
                                    "POST" + System.currentTimeMillis(),
                                    FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString()))
                            .addOnCreatedListener(new PostsHandler.OnCreatedListener() {
                                @Override
                                public void onSuccess() {
                                    Toast.makeText(NewPostActivity.this, "Post added successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(String errorMessage) {

                                    Toast.makeText(NewPostActivity.this, "Post could not be added!", Toast.LENGTH_SHORT).show();
                                    postButton.animate().scaleX(1.0f).setDuration(500);
                                    postButton.animate().scaleY(1.0f).setDuration(500);
                                }
                            });
                }
            }
        });

    }
}
