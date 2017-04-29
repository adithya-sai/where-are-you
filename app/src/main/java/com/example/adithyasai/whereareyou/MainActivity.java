package com.example.adithyasai.whereareyou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment=new CreateGroup();
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame,fragment);
                ft.commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_menu_1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        String dest="33.40,-111.89";
        Intent intent = new Intent(this,CurrentLocationBackground.class);
        Bundle extras =new Bundle();
        SharedPreferences sp = getSharedPreferences("MyPrefs",MODE_WORLD_READABLE);
        String user=sp.getString("userKey","default");
        String authKey=sp.getString("authKey","default");
        String groupId=sp.getString("event_id","default");
        String latitude=sp.getString("latitude","default");
        String longitude=sp.getString("longitude","default");
        latitude=latitude.replace(" ","");
        longitude=longitude.replace(" ","");
        dest=latitude+","+longitude;
        extras.putString("groupId",groupId);
        extras.putString("destination",dest);
        extras.putString("user",user);
        extras.putString("authKey",authKey);
        intent.putExtras(extras);
        System.out.println(Double.parseDouble(latitude)+" "+Double.parseDouble(longitude));
        if(Double.parseDouble(latitude)!=0.0 && Double.parseDouble(longitude)!=0.0)
        {
            System.out.println("Starting service");
            startService(intent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    public void displaySelectedScreen(int itemId){
        Fragment fragment = null;
        switch(itemId){
            case R.id.nav_menu_1:
                fragment=new CurrentGroup();
                break;
            case R.id.nav_menu_2:
                fragment=new CreateGroup();
                break;
            case R.id.nav_menu_3:
                fragment=new ManageInvitation();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
