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

import com.baidu.mapapi.SDKInitializer;
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

    /**
     * 设置某个活动的状态栏颜色，代码来自网络
     * @param activity 活动对象
     */
    static void setStatusBarColor(Activity activity) {
        Window window = activity.getWindow();
        //取消状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //添加Flag把状态栏设为可绘制模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.rgb(0, 150, 166));
        }
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        // 设置统一的状态栏颜色
        setStatusBarColor(this);

        // 初始化的用户数据保存在手机的sqlite数据库中
        Database.initUser();
        user = Database.getLocalUser();

        // 必须等待登录的线程结束
        final CountDownLatch l1 = new CountDownLatch(1);
        try {
            user.login(l1);
            l1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 侧边抽屉菜单（导航菜单）
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
        }
        View headerView = navView.getHeaderView(0);

        // 在导航菜单中显示用户手机号
        TextView navName = headerView.findViewById(R.id.nav_name);
        navName.setText(user.getPhone());

        // 默认选中主页
        navView.setCheckedItem(R.id.nav_home);

        // 导航菜单的点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    // 个人中心选项
                    case R.id.nav_personal:
                        intent = new Intent(MainActivity.this, UserActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        intent.putExtra("name", user.getPhone());
                        intent.putExtra("balance", Double.toString(user.getBalance()));
                        startActivity(intent);
                        break;

                    // 换电记录选项
                    case R.id.nav_record:
                        intent = new Intent(MainActivity.this, RecordActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        startActivity(intent);
                        break;

                    // 余额选项
                    case R.id.nav_balance:
                        intent = new Intent(MainActivity.this, BalanceActivity.class);
                        intent.putExtra("balance", Double.toString(user.getBalance()));
                        startActivity(intent);
                        break;

                    // 预约信息选项
                    case R.id.nav_appointment:
                        // 获取用户的预约信息
                        // TODO
                        // 先询问数据库是否有预约将会导致点击预约信息后卡顿
                        // 最好先转跳到AppointmentActivity然后判断是否有预约
                        // 如无预约转跳到NoAppointmentActivity然后将AppointmentActivity finish即可
//                        CountDownLatch latch = new CountDownLatch(1);
//                        user.loadAppointment(latch);
//                        try {
//                            latch.await();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }

                        // 根据用户当前是否有未完成预约来决定转跳的目标活动
//                        if (user.isAppointment()) {
                            intent = new Intent(MainActivity.this, AppointmentActivity.class);
                            intent.putExtra("id", Integer.toString(user.getId()));
                            startActivity(intent);
//                        } else {
//                            intent = new Intent(MainActivity.this, NoAppointmentActivity.class);
//                            startActivity(intent);
//                        }
                        break;

                    // 收藏电站选项
                    case R.id.nav_star:
                        intent = new Intent(MainActivity.this, CollectionActivity.class);
                        intent.putExtra("id", Integer.toString(user.getId()));
                        startActivity(intent);
                        break;

                    // 爱车信息选项
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

        // 创建附近电站分页面对象并设置用户对象参数，便于分页面根据用户id获取附近电站数据
        FirstFragment first = new FirstFragment();
        first.setUser(user);
        list.add(first);

        SecondFragment second = new SecondFragment();
        second.setUserId(user.getId());
        list.add(second);

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //悬浮按钮点击事件：进入个人中心
        FloatingActionButton floatingActionButton = findViewById(R.id.floating);
        floatingActionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra("id", Integer.toString(user.getId()));
                intent.putExtra("name", user.getPhone());
                intent.putExtra("balance", Double.toString(user.getBalance()));
                startActivity(intent);
            }
        });

    }

    /**
     * 实现菜单必须重写该方法
     * @param item 菜单项
     * @return true
     */
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

    /**
     * 实现菜单必须使用该方法
     * @param menu 菜单对象
     * @return true
     */
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        CharSequence queryHint = searchView.getQueryHint();
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo); // 搜索框
        return true;
    }

    /**
     * 分页面的配适器
     */
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
