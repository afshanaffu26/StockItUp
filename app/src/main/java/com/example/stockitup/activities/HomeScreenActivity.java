package com.example.stockitup.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stockitup.fragments.HelpFragment;
import com.example.stockitup.fragments.OrderHistoryFragment;
import com.example.stockitup.fragments.FeedbackFragment;
import com.example.stockitup.fragments.HomeFragment;
import com.example.stockitup.R;
import com.example.stockitup.fragments.InviteFragment;
import com.example.stockitup.fragments.OffersFragment;
import com.example.stockitup.fragments.SettingsFragment;
import com.example.stockitup.utils.AppConstants;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This class deals with Home screen and related fragments to be set on where required.
 */
public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private HomeFragment homeFragment;
    private FeedbackFragment feedbackFragment;
    private InviteFragment inviteFragment;
    private OffersFragment offersFragment;
    private OrderHistoryFragment orderHistoryFragment;
    private SettingsFragment settingsFragment;
    private HelpFragment helpFragment;
    private TextView drawerUsername,drawerAccount;
    private ImageView drawerImage;

    /**
     * Overriding Android device back
     * This method does nothing
     * */
    @Override
    public void onBackPressed() {
        //do Nothing
    }

    /**
     *  Called when the activity is starting.
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        String appName = AppConstants.APP_NAME;
        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(appName);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        drawerImage = (ImageView) headerView.findViewById(R.id.imageView2);
        drawerUsername = (TextView) headerView.findViewById(R.id.textView3);
        drawerAccount = (TextView) headerView.findViewById(R.id.textView4);

        homeFragment = new HomeFragment();
        helpFragment = new HelpFragment();
        offersFragment = new OffersFragment();
        orderHistoryFragment = new OrderHistoryFragment();
        inviteFragment = new InviteFragment();
        settingsFragment = new SettingsFragment();
        feedbackFragment = new FeedbackFragment();

        mAuth = FirebaseAuth.getInstance();

        setUserDetails();
        setFragment(homeFragment);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        ){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setUserDetails();
            }

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    /***
     * set fragment to view
     * @param fragment the fragment to be set
     * */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    /**
     * This method is used to switch between fragments
     * @param item Instance og menu item clicked
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        switch(id){
            case R.id.menu1home:
                setFragment(homeFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu1history:
                setFragment(orderHistoryFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu1offers:
                setFragment(offersFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2settings:
                setFragment(settingsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2share:
                setFragment(inviteFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2feedback:
                setFragment(feedbackFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2help:
                setFragment(helpFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu3logout:
                userLogout();
                break;
        }
        return true;
    }

    /**
     * This method is used to logout the  logged in user
     */
    private void userLogout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent i= new Intent(this,LoginActivity.class);
        startActivity(i);
    }

    /**
     * This method is used to set name and profile photo in navigation drawer
     */
    private void setUserDetails() {
        FirebaseUser user = mAuth.getCurrentUser();
        drawerUsername.setText("");
        drawerAccount.setText("");
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(drawerImage);
            }
            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                drawerUsername.setText(displayName);
            }
            if (user.getEmail() != null) {
                String emailId = user.getEmail();
                drawerAccount.setText(emailId);
            }
        }
    }
}