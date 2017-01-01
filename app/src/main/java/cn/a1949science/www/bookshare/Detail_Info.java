package cn.a1949science.www.bookshare;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class Detail_Info extends AppCompatActivity {

    Context mContext = Detail_Info.this;
    ImageView before;
    TextView dorm,phoneNum,Class,school,gender,name;
    View editDorm,editPhone,editClass,editSchool,editGender,editname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__info);

        findView();
        onClick();
        showDetailInfo();

    }
    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        dorm = (TextView) findViewById(R.id.dorm);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        Class = (TextView) findViewById(R.id.Class);
        school = (TextView) findViewById(R.id.school);
        gender = (TextView) findViewById(R.id.gender);
        name = (TextView) findViewById(R.id.name);
        editDorm = findViewById(R.id.editDorm);
        editPhone = findViewById(R.id.editPhone);
        editClass = findViewById(R.id.editClass);
        editSchool = findViewById(R.id.editSchool);
        editGender = findViewById(R.id.editGender);
        editname = findViewById(R.id.editname);
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
        editDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "123", Toast.LENGTH_SHORT).show();
            }
        });
        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final String[] classText = {"大一","大二","大三","大四","研一","研二","研三","博士"};
                builder.setSingleChoiceItems(classText, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setPositiveButton("确认修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(mContext, classText[i], Toast.LENGTH_SHORT).show();
                        //更新数据
                        *//*_User newUser = new _User();
                        newUser.setUserClass(editClassText);
                        BmobUser bmobUser = BmobUser.getCurrentUser();
                        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(mContext, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*//*
                    }
                });
                builder.setNegativeButton("取消修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();*/
            }
        });
        editSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "123", Toast.LENGTH_SHORT).show();
            }
        });
        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "123", Toast.LENGTH_SHORT).show();
            }
        });
        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "123", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showDetailInfo() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        bmobUser.getObjectId();
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.getObject(bmobUser.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    dorm.setText(user.getUserDorm());
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
}
