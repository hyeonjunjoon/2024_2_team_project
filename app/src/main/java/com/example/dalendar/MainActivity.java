package com.example.dalendar;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.page_1) {
                    transferTo(CalendarFragment.newInstance("param1", "param2"));
                    return true;
                }

//                if (itemId == R.id.page_2) {
//                    transferTo(MusicFragment.newInstance("param1", "param2"));
//                    return true;
//                }
//
//                if (itemId == R.id.page_3) {
//                    transferTo(PlacesFragment.newInstance("param1", "param2"));
//                    return true;
//                }
//
//                if (itemId == R.id.page_4) {
//                    transferTo(NewsFragment.newInstance("param1", "param2"));
//                    return true;
//                }

                //일치하는 페이지가 없으면 false 반환
                return false;
            }
        });

        bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                //아무 일도 안 함 (무지성 새로고침 개념의 창 띄우기 방지)
            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, CalendarFragment.newInstance("param1", "param2"))
                .commit();

    } //onCreated


    private void transferTo(Fragment fragment){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}