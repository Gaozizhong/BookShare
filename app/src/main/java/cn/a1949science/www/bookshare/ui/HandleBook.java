package cn.a1949science.www.bookshare.ui;

import android.app.TabActivity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.fragment.FourFragment;
import cn.a1949science.www.bookshare.fragment.OneFragment;
import cn.a1949science.www.bookshare.fragment.ThreeFragment;
import cn.a1949science.www.bookshare.fragment.TwoFragment;

public class HandleBook extends AppCompatActivity {
    Context mContext = HandleBook.this;
    TabLayout tabLayout;
    ViewPager viewPager;

    private String[] title = new String[]{"借入", "借出", "归还", "收书"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_book);

        findView();
        initView();
        initEvents();
    }

    private void initEvents() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == tabLayout.getTabAt(0)) {
                    viewPager.setCurrentItem(0);
                } else if (tab == tabLayout.getTabAt(1)) {
                    viewPager.setCurrentItem(1);
                } else if (tab == tabLayout.getTabAt(2)) {
                    viewPager.setCurrentItem(2);
                }else if (tab == tabLayout.getTabAt(3)){
                    viewPager.setCurrentItem(3);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //查找地址
    private void findView() {
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpapger);
    }


    private void initView() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                if (position == 1) {
                    return new TwoFragment();
                } else if (position == 2) {
                    return new ThreeFragment();
                }else if (position==3){
                    return new FourFragment();
                }
                return new OneFragment();
            }

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });

        tabLayout.setupWithViewPager(viewPager);
    }

}
