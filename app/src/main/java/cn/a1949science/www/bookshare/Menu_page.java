package cn.a1949science.www.bookshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Menu_page extends AppCompatActivity {

    Context mContext = Menu_page.this;
    View userInfo,shared,read,like, advice;
    ImageView before;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        findView();
        onClick();
    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        userInfo = (View) findViewById(R.id.userInfo);
        shared = (View) findViewById(R.id.shared);
        read = (View) findViewById(R.id.read);
        like = (View) findViewById(R.id.like);
        advice = (View) findViewById(R.id.advice);
    }

    //点击事件
    protected void onClick(){

        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,User_Info.class);
                startActivity(it);
            }
        });

        shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,My_Book_List.class);
                startActivity(it);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,My_Book_List.class);
                startActivity(it);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,My_Book_List.class);
                startActivity(it);
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,Advice_Page.class);
                startActivity(it);
            }
        });

    }
}
