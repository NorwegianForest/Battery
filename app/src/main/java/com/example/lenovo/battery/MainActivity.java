package com.example.lenovo.battery;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.battery.Database;
import com.battery.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;


public class MainActivity extends AppCompatActivity {

    private User user;
    private DrawerLayout drawerLayout;
    private List<Fragment> list;
    private String[] titles = {"附近电站","电站推荐","电站地图"};

    static void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0, 150, 166));
        }
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor(this);

        Database.initUser();
        user = Database.getLocalUser();
        final CountDownLatch l1 = new CountDownLatch(1);
        try {
            user.login(l1);
            l1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        }
        View headerView = navView.getHeaderView(0);
        TextView navName = headerView.findViewById(R.id.nav_name);
        navName.setText(user.getPhone());
        navView.setCheckedItem(R.id.nav_home);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_personal:
                        intent = new Intent(MainActivity.this, UserActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        intent.putExtra("name", user.getPhone());
                        intent.putExtra("balance", Double.toString(user.getBalance()));
                        startActivity(intent);
                        break;
                    case R.id.nav_record:
                        intent = new Intent(MainActivity.this, RecordActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        startActivity(intent);
                        break;
                    case R.id.nav_balance:
                        intent = new Intent(MainActivity.this, BalanceActivity.class);
                        intent.putExtra("balance", Double.toString(user.getBalance()));
                        startActivity(intent);
                        break;
                    case R.id.nav_appointment:
                        CountDownLatch latch = new CountDownLatch(1);
                        user.loadAppointment(latch);
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (user.isAppointment()) {
                            intent = new Intent(MainActivity.this, AppointmentActivity.class);
                            intent.putExtra("id", Integer.toString(user.getId()));
                            startActivity(intent);
                        } else {
                            intent = new Intent(MainActivity.this, NoAppointmentActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case R.id.nav_star:
                        intent = new Intent(MainActivity.this, CollectionActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        startActivity(intent);
                        break;
                    case R.id.nav_vehicle:
                        intent = new Intent(MainActivity.this, MyVehicleActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        startActivity(intent);
                        break;
                        default:break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        ViewPager viewPager = findViewById(R.id.vpager);
        TabLayout tabLayout = findViewById(R.id.tab);
        list=new ArrayList<>();

        FirstFragment first = new FirstFragment();
        first.setUser(user);
        list.add(first);

        SecondFragment second = new SecondFragment();
        second.setUserId(user.getId());
        list.add(second);

        list.add(new ThirdFragment());

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton floatingActionButton = findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,UserActivity.class);
                intent.putExtra("id", Integer.toString(user.getId()));
                intent.putExtra("name", user.getPhone());
                intent.putExtra("balance", Double.toString(user.getBalance()));
                startActivity(intent);
            }
        });//悬浮按钮点击事件：进入个人中心

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        CharSequence queryHint = searchView.getQueryHint();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);//搜索框
        return true;
    }

    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm){
            super(fm);
        }
        public Fragment getItem(int position){
            return list.get(position);
        }
        @Override
        public int getCount() {
            return list.size();
        }
        public CharSequence getPageTitle(int position){
            return titles[position];
        }
    }
}
