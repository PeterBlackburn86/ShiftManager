package com.peterblackburn.shiftmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import com.peterblackburn.shiftmanager.Calendar.CalendarFactory;
import com.peterblackburn.shiftmanager.Fragments.CalendarFragment;
import com.peterblackburn.shiftmanager.Fragments.FragmentHelper;
import com.peterblackburn.shiftmanager.Fragments.HomeFragment;
import com.peterblackburn.shiftmanager.Fragments.TemplateFragment;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private FragmentHelper _fragHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(ShiftApplication.getInstance().isDarkTheme())
            setTheme(R.style.Theme_MaterialDark);
        else
            setTheme(R.style.Theme_MaterialLight);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fragHelper = FragmentHelper.getInstance(getSupportFragmentManager(), this);

        //SETUP NAVIGATION DRAWER AND TOGGLE BUTTON
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        NavigationView navigationView;
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if(!_fragHelper.loadLastFragment()) {
            _fragHelper.loadFragment(HomeFragment.getInstance());
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void updateMonth(View view) {
        switch (view.getId()) {
            case R.id.prevMonthImg:
                CalendarFactory.getInstance().decreaseMonth();
                break;
            case R.id.nextMonthImg:
                CalendarFactory.getInstance().increaseMonth();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                _fragHelper.loadFragment(HomeFragment.getInstance());
                break;
            case R.id.nav_calendar:
                _fragHelper.loadFragment(CalendarFragment.getInstance());
                break;
            case R.id.nav_templates:
                _fragHelper.loadFragment(TemplateFragment.getInstance());
                break;
            case R.id.nav_settings:
//                _fragHelper.loadFragment(PreferenceFragment.getInstance());
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        switch (s) {
            case "pref_dark_theme":
                recreate();
                break;
        }
    }
}
