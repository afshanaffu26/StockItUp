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

        import com.example.stockitup.R;
        import com.google.android.material.navigation.NavigationView;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;

/**
 * This class deals with Home screen and related fragments to be set on where required.
 */
public class HomeScreenActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    FirebaseAuth mAuth;

    private Toolbar toolbar;
    private boolean isInFront;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    //private BottomNavigationView mMainNav;
//    private FrameLayout mMainFrame;
//    private HomeFragment homeFragment;
//    private CartFragment cartFragment;
//    private FeedbackFragment feedbackFragment;
//    private InviteFragment inviteFragment;
//    private OffersFragment offersFragment;
//    private OrderHistoryFragment orderHistoryFragment;
//    private SettingsFragment settingsFragment;
//    private HelpFragment helpFragment;
//    private SearchFragment searchFragment;
    private TextView drawerUsername,drawerAccount;
    private ImageView drawerImage;

    /**
     * Called when the activity will start interacting with the user
     */
    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    /**
     * Called when the activity loses foreground state, is no longer focusable or before tran
     */
    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("StockItUp");

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
       // mMainFrame=findViewById(R.id.main_frame);
        //mMainNav=findViewById(R.id.main_nav);

        View headerView = navigationView.getHeaderView(0);
        drawerImage = (ImageView) headerView.findViewById(R.id.imageView2);
        drawerUsername = (TextView) headerView.findViewById(R.id.textView3);
        drawerAccount = (TextView) headerView.findViewById(R.id.textView4);

//        homeFragment = new HomeFragment();
//        cartFragment = new CartFragment();
//        helpFragment = new HelpFragment();
//        offersFragment = new OffersFragment();
//        orderHistoryFragment = new OrderHistoryFragment();
//        inviteFragment = new InviteFragment();
//        settingsFragment = new SettingsFragment();
//        feedbackFragment = new FeedbackFragment();
//        searchFragment = new SearchFragment();
        mAuth = FirebaseAuth.getInstance();
        //setUserDetails();

        //setFragment(homeFragment);

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
                //setUserDetails();
            }

        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
//        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch(item.getItemId()){
//                    case R.id.nav_home:
//                        setFragment(homeFragment);
//                        return true;
//                    case R.id.nav_world:
//                        setFragment(searchFragment);
//                        return true;
//                    case R.id.nav_settings:
//                        setFragment(settingsFragment);
//                        return true;
//                    default:
//                        return false;
//                }            }
//        });
    }
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
//                if (!isInFront)
//                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
//                else
//                    drawerLayout.closeDrawer(GravityCompat.START);
                //setFragment(homeFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu1cart:
                //setFragment(cartFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu1history:
                //setFragment(orderHistoryFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu1offers:
                //setFragment(offersFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2settings:
                //setFragment(settingsFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2share:
                //setFragment(inviteFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2feedback:
                //setFragment(feedbackFragment);
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.menu2help:
                //setFragment(helpFragment);
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
        //drawerImage.setImageDrawable(R.drawable.menu);
        if (user != null) {
            if (user.getPhotoUrl() != null) {
                //Glide.with(this).load(user.getPhotoUrl().toString()).into(drawerImage);
            }
            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                drawerUsername.setText(displayName);
            }
            if (user.getEmail() != null) {
                String emailId = user.getEmail().toString();
                drawerAccount.setText(emailId);
            }
        }
    }
}