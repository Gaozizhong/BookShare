package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class Menu_page extends AppCompatActivity {

    Context mContext = Menu_page.this;
    View userInfo,shared,read,like, advice;
    ImageView before;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
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
        logout = (Button) findViewById(R.id.logout);
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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dlg = new AlertDialog.Builder(mContext)
                        .setTitle("确认退出？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                BmobUser.logOut();
                                BmobUser currentUser = BmobUser.getCurrentUser();
                                Intent intent = new Intent(Menu_page.this, Login_Page.class);
                                //清空源来栈中的Activity，新建栈打开相应的Activity
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .create();
                dlg.show();
            }
        });

    }
}
