package com.example.foodconsumption;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You will be redirected to the MealLog BOT", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String url = "https://bot.dialogflow.com/e2c74796-37c0-4623-a991-daf6705cfced";
                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
            View headerView = navigationView.getHeaderView(0);
            TextView navUsername = (TextView) headerView.findViewById(R.id.user_name);
        TextView navUseremail = (TextView) headerView.findViewById(R.id.email);
        ImageView userImage = (ImageView) headerView.findViewById(R.id.userImage);
        String photoUrl = user.getPhotoUrl().toString();
        photoUrl = photoUrl + "?type=large";
        Picasso.get().load(photoUrl).into(userImage);
            navUsername.setText(user.getDisplayName().toString());
        navUseremail.setText(user.getEmail().toString());
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_scan, R.id.nav_chart, R.id.nav_foodbase)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        ActionBar bar = getSupportActionBar();
        switch(item.getItemId()){
            case R.id.action_red:
                bar.setBackgroundDrawable(new ColorDrawable(Color.RED));
                Toast.makeText(this, "Color RED selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_green:
                bar.setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                Toast.makeText(this, "Color GREEN selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_blue:
                bar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                Toast.makeText(this, "Color BLUE selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}