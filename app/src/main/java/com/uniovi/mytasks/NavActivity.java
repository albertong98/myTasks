package com.uniovi.mytasks;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.uniovi.mytasks.databinding.ActivityNavBinding;

public class NavActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNav.toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tareas, R.id.nav_eventos)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        View headerView = navigationView.getHeaderView(0);
        if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            TextView email_user = (TextView) headerView.findViewById(R.id.email_user);
            email_user.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        }
        if (FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null) {
            TextView name_user = (TextView) headerView.findViewById(R.id.name_user);
            name_user.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
        if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            ImageView image_user = (ImageView) headerView.findViewById(R.id.image_user);
            Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(image_user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_nav);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            this.finish();
        }
        return false;
    }
}