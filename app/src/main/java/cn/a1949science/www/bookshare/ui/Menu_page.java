package cn.a1949science.www.bookshare.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;

public class Menu_page extends AppCompatActivity {

    Context mContext = Menu_page.this;
    View userInfo,shared,read,like, advice,refrush;
    ImageView before,favicon;
    Button logout;
    TextView nickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
        findView();
        onClick();
        display();
    }

    //初始化信息
    private void display() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        bmobUser.getObjectId();
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.getObject(bmobUser.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    nickname.setText(user.getNickname());
                    Glide.with(mContext)
                            .load(user.getFavicon().getFileUrl())
                            .override((int)(mContext.getResources().getDisplayMetrics().density*60+0.5f),(int)(mContext.getResources().getDisplayMetrics().density*60+0.5f))
                            .placeholder(R.drawable.wait)
                            .into(favicon);
                } else {
                    Toast.makeText(mContext, "昵称、头像显示失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        favicon = (ImageView) findViewById(R.id.favicon);
        nickname = (TextView) findViewById(R.id.nickname);
        userInfo = findViewById(R.id.userInfo);
        shared = findViewById(R.id.shared);
        read = findViewById(R.id.read);
        like = findViewById(R.id.like);
        advice = findViewById(R.id.advice);
        refrush= findViewById(R.id.refrush);
        logout = (Button) findViewById(R.id.logout);
    }

    //点击事件
    protected void onClick(){

        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,User_Info.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });

        shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,My_Book_List.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,MyRead.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,MyLike.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });

        advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,Advice_Page.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });

        refrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*BmobUpdateAgent.initAppVersion();
                BmobUpdateAgent.setUpdateOnlyWifi(true);
                BmobUpdateAgent.update(mContext);*/
                Toast.makeText(mContext, "过两天就能用了！！！", Toast.LENGTH_SHORT).show();

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
                                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                            }
                        })
                        .create();
                dlg.show();
            }
        });

    }
}
