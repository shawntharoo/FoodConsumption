package com.example.foodconsumption;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodconsumption.ui.home.HomeViewModel;
import com.example.foodconsumption.ui.profile.ProfileViewModel;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private TextView textViewUser;
    private ImageView userImage;
    private LoginButton loginButton;
    private static final String TAG = "FacebookAuthentication";
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG2 = "LoginActivity";
    private Button btnSignOut;
    private Button continueButton;

    private String loggedInUserEmail = null;

    public String getLoggedInUserEmail() {
        return loggedInUserEmail;
    }

    public void setLoggedInUserEmail(String loggedInUserEmail) {
        this.loggedInUserEmail = loggedInUserEmail;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_main);

        signInButton = findViewById(R.id.sing_in_button);
        btnSignOut = findViewById(R.id.sign_out_button);
        mCallbackManager = CallbackManager.Factory.create();
        mFirebaseAuth = FirebaseAuth.getInstance();
        textViewUser = findViewById(R.id.textViewUser);
        userImage = findViewById(R.id.userImageView);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        continueButton = findViewById(R.id.continue_button);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess " + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError " + error);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mFirebaseAuth.signOut();
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                userImage.setVisibility(View.INVISIBLE);
                textViewUser.setVisibility(View.INVISIBLE);
                continueButton.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this,"Signed out",Toast.LENGTH_LONG).show();
                btnSignOut.setVisibility(view.INVISIBLE);
            }
        });

        continueButton.setOnClickListener((view) -> {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAG, "HandleFacebookToken" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"You have successfully registered",Toast.LENGTH_LONG).show();
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Toast.makeText(LoginActivity.this,"error while registering",Toast.LENGTH_LONG).show();
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    if(user != null){
                        updateUI(user);
                    }else{
                        updateUI(null);
                    }
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "successfully signed in", Toast.LENGTH_SHORT).show();
            FireBaseGoogleAuth(acc);
        }catch (ApiException e){
            FireBaseGoogleAuth(null);
        }
    }

    private void FireBaseGoogleAuth(GoogleSignInAccount o) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(o.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"You have successfully registered",Toast.LENGTH_LONG).show();
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
//                    updateUIGoogle(user);
                    updateUI(user);
                }else{
                    Toast.makeText(LoginActivity.this,"error while registering",Toast.LENGTH_LONG).show();
//                    updateUIGoogle(null);
                    updateUI(null);
                }
            }
        });
    }


    private void updateUI(FirebaseUser user) {
        ProfileViewModel profileViewModel;
        String photoUrl = null;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if(account != null){
            userImage.setVisibility(View.VISIBLE);
            textViewUser.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
            continueButton.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.VISIBLE);
            textViewUser.setText(user.getDisplayName());
            if(user.getPhotoUrl() != null){
                photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl + "?type=large";
                Picasso.get().load(photoUrl).into(userImage);
            }
            profileViewModel = new ProfileViewModel(user.getEmail(), user.getDisplayName(), "N/A", "N/A", photoUrl, "NA", "N/A");
            databaseHelper.checkUserExistence(profileViewModel);
        }else {
            userImage.setVisibility(View.INVISIBLE);
            textViewUser.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            continueButton.setVisibility(View.INVISIBLE);
            btnSignOut.setVisibility(View.INVISIBLE);
        }
    }

//    private void updateUIGoogle(FirebaseUser user) {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        if(account != null){
//            userImage.setVisibility(View.VISIBLE);
//            textViewUser.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.VISIBLE);
//            signInButton.setVisibility(View.INVISIBLE);
//            loginButton.setVisibility(View.INVISIBLE);
//            continueButton.setVisibility(View.VISIBLE);
//
//            String personName = account.getDisplayName();
//            String personGivenName = account.getGivenName();
//            String personFamilyName = account.getFamilyName();
//            String email = account.getEmail();
//            String personId = account.getId();
//            Uri personPhoto = account.getPhotoUrl();
//            textViewUser.setText(personName);
//
//            String photoUrl = personPhoto.toString();
//            photoUrl = photoUrl + "?type=large";
//            Picasso.get().load(photoUrl).into(userImage);
//
//            Toast.makeText(LoginActivity.this,personName + email,Toast.LENGTH_LONG).show();
//            }else {
//            userImage.setVisibility(View.INVISIBLE);
//            textViewUser.setVisibility(View.INVISIBLE);
//            signInButton.setVisibility(View.VISIBLE);
//            loginButton.setVisibility(View.VISIBLE);
//            continueButton.setVisibility(View.INVISIBLE);
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null){
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
