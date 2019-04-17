package com.ta.slk.sistemlayanankegiatan;

import com.ta.slk.sistemlayanankegiatan.Fragments.*;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminContent extends AppCompatActivity {
    private BottomNavigationView mNavigationView;
    private FrameLayout mFrameLayout;

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

        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_admin_activity:
                        setFragment(activitiesFragment);
                        return true;

                    case R.id.nav_admin_group:
                        setFragment(groupsFragment);
                        return true;

                    case R.id.nav_admin_member:
                        setFragment(membersFragment);
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
