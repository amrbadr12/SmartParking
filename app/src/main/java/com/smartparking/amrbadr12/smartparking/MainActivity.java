package com.smartparking.amrbadr12.smartparking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Toolbar primaryToolbar;
    private Toolbar cardviewToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Button parkButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        primaryToolbar= (Toolbar) findViewById(R.id.primary_toolbar);
        cardviewToolbar=(Toolbar)findViewById(R.id.card_view_toolbar);
        parkButton = (Button) findViewById(R.id.floatingActionButton);
        setSupportActionBar(primaryToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle("");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.menu_profile:
                        break;
                    case R.id.menu_faq:
                        break;
                    case R.id.menu_settings:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout != null)
                    mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
