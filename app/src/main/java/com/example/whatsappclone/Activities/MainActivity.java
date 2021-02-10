package com.example.whatsappclone.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.databinding.ActivityMainBinding;
import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    FragChats fragChats;
    FragStatus fragStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        fragChats = new FragChats();
        fragStatus = new FragStatus();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.AddFragments(fragChats,"Chats");
        viewPagerAdapter.AddFragments(fragStatus,"Status");
        activityMainBinding.viewPager.setAdapter(viewPagerAdapter);

        activityMainBinding.tabLayout.setupWithViewPager(activityMainBinding.viewPager);   //to work tabs with fragment set tab layout
        activityMainBinding.tabLayout.getTabAt(0).setIcon(R.drawable.chat);
        activityMainBinding.tabLayout.getTabAt(1).setIcon(R.drawable.status);

        BadgeDrawable badgeDrawableExplore = activityMainBinding.tabLayout.getTabAt(0).getOrCreateBadge();
        badgeDrawableExplore.setVisible(true);
        badgeDrawableExplore.setMaxCharacterCount(3); //this will show 99+ if the number set is greater than 99
        badgeDrawableExplore.setNumber(10);

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void AddFragments(Fragment frag,String t)
        {
            fragmentArrayList.add(frag);
            title.add(t);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }
    }





    //this is for the menu on the right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this, "Search Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.group:
                Toast.makeText(this, "Groups Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.profile:
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}