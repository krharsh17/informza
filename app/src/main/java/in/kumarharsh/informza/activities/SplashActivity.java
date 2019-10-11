package in.kumarharsh.informza.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import in.kumarharsh.informza.R;
import in.kumarharsh.informza.models.User;

public class SplashActivity extends AppCompatActivity {

    ImageView signInButton, logo;
    TextView appName;
    CardView signInCard;
    GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 10;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        assignVariables();
        setup();

    }

    void assignVariables() {
        signInButton = findViewById(R.id.google_sign_in);
        logo = findViewById(R.id.logo);
        appName = findViewById(R.id.app_name);
        signInCard = findViewById(R.id.google_sign_in_card);
        mAuth = FirebaseAuth.getInstance();
    }

    void setup() {
        logo.setAlpha(0f);
        appName.setAlpha(0f);
        signInCard.setAlpha(0f);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.animate().alpha(1).setDuration(500);
                appName.animate().alpha(1).setDuration(500);



                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            finish();
                        }
                        logo.animate().yBy(-200);
                        appName.animate().yBy(-200);

                        signInCard.animate().alpha(1).yBy(-100).setDuration(500);


                    }
                }, 1000);
            }
        }, 1000);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.i("DEBUG", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SplashActivity.this, "Signed in successfully!!", Toast.LENGTH_LONG).show();
                            FirebaseFirestore.getInstance().collection("USERS")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){
                                                FirebaseFirestore.getInstance().collection("USERS")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .set(documentSnapshot.toObject(User.class))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SplashActivity.this, "Sign in failed :(", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            } else {
                                                FirebaseFirestore.getInstance().collection("USERS")
                                                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .set(new User(new ArrayList<String>(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                                                finish();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(SplashActivity.this, "Sign in failed :(", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    FirebaseFirestore.getInstance().collection("USERS")
                                            .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .set(new User(new ArrayList<String>(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(SplashActivity.this, "Sign in failed :(", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                            // Sign in success, update UI with the signed-in user's information
                        } else {
                            Toast.makeText(SplashActivity.this, "Sign in failed :(", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                        }

                        // ...
                    }
                });
    }
}
