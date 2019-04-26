package com.ta.slk.sistemlayanankegiatan;

import com.bumptech.glide.Glide;
import com.ta.slk.sistemlayanankegiatan.Fragments.*;
import com.ta.slk.sistemlayanankegiatan.Method.Application;
import com.ta.slk.sistemlayanankegiatan.Method.Session;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminContent extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FrameLayout mFrameLayout;
    private Toolbar toolbar;
    private ActivitiesFragment activitiesFragment;
    private GroupsFragment groupsFragment;
    private MembersFragment membersFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_content);

        mNavigationView = findViewById(R.id.admin_nav);
        mFrameLayout = findViewById(R.id.admin_frame);

        activitiesFragment = new ActivitiesFragment();
        groupsFragment = new GroupsFragment();
        membersFragment = new MembersFragment();

        setFragment(activitiesFragment);
//        getTimeAgo();

        toolbar = findViewById(R.id.my_toolbar);
//        Drawable drawable;
//        SharedPreferences preferences = this.getSharedPreferences("login",MODE_PRIVATE);
//        String url = preferences.getString("photo",null);
//        ImageView imageView = findViewById(R.id.img_visi);
//        Glide.with(getApplicationContext()).load(url).into(imageView);
//        drawable = imageView.getDrawable();
        toolbar.setNavigationIcon(R.drawable.common_google_signin_btn_icon_dark_normal);

        toolbar.setTitle("Kegiatan");
        setSupportActionBar(toolbar);

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_admin_activity:
                        setFragment(activitiesFragment);
                        toolbar.setTitle("Kegiatan");
                        setSupportActionBar(toolbar);
                        return true;

                    case R.id.nav_admin_group:
                        setFragment(groupsFragment);
                        toolbar.setTitle("Group");
                        setSupportActionBar(toolbar);
                        return true;

                    case R.id.nav_admin_member:
                        setFragment(membersFragment);
                        toolbar.setTitle("Member");
                        setSupportActionBar(toolbar);
                        return true;

                        default:
                            return false;
                }
            }
        });
    }
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.admin_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_activity:
                Intent intent = new Intent(getApplicationContext(), AddInvitation.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getTimeAgo() {
        String input = "1970-01-09 17:11:23.104";
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
        Date date = null;
        try {
            date = parser.parse(input);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);

        Log.d("date", formattedDate.toString());
    }
}
