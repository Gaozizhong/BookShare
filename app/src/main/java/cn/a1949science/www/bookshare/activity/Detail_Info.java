package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class Detail_Info extends AppCompatActivity implements View.OnClickListener{

    Context mContext = Detail_Info.this;
    TextView phoneNum,Class,school,gender,name,edit;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__info);

        findView();
        showDetailInfo();

    }
    //查找地址
    private void findView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        edit = (TextView)findViewById(R.id.text);
        edit.setText("编辑");
        edit.setOnClickListener(this);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        Class = (TextView) findViewById(R.id.Class);
        school = (TextView) findViewById(R.id.school);
        gender = (TextView) findViewById(R.id.gender);
        name = (TextView) findViewById(R.id.name);

    }

    private void showDetailInfo() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        bmobUser.getObjectId();
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.getObject(bmobUser.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    phoneNum.setText(user.getMobilePhoneNumber());
                    Class.setText(user.getUserClass());
                    school.setText(user.getUserSchool());
                    if (user.getUsersex()) {
                        gender.setText("男");
                    } else {
                        gender.setText("女");
                    }
                    name.setText(user.getUsername());
                } else {
                    Toast.makeText(mContext, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text:
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                if (bmobUser.getCertificationOk()) {
                    Intent it = new Intent(mContext, Edit_Detail_Info.class);
                    startActivity(it);
                    overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
                } else {
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("请先进行实名认证")
                            .setMessage("请进行实名认证")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent it = new Intent(mContext, Identification.class);
                                    startActivity(it);
                                    overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
                                }
                            })
                            .create();
                    dlg.show();
                }
                break;
        }
    }
}
