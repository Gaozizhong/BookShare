package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Edit_Detail_Info extends AppCompatActivity implements View.OnClickListener{

    Context mContext = Edit_Detail_Info.this;
    TextView phoneNum,Class,school,gender,name,editok;
    View editPhone,editClass,editSchool,editGender,editname;
    boolean sex1;
    Toolbar toolbar;
    _User bmobUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail_info);
        findView();
        onClick();
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
        editok = (TextView)findViewById(R.id.text);
        editok.setText("完成");
        editok.setOnClickListener(this);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        Class = (TextView) findViewById(R.id.Class);
        school = (TextView) findViewById(R.id.school);
        gender = (TextView) findViewById(R.id.gender);
        name = (TextView) findViewById(R.id.name);
        editPhone = findViewById(R.id.editPhone);
        editClass = findViewById(R.id.editClass);
        editSchool = findViewById(R.id.editSchool);
        editGender = findViewById(R.id.editGender);
        editname = findViewById(R.id.editname);
        bmobUser = BmobUser.getCurrentUser(_User.class);
        phoneNum.setText(bmobUser.getMobilePhoneNumber());
        Class.setText(bmobUser.getUserClass());
        school.setText(bmobUser.getUserSchool());
        if (bmobUser.getUsersex()) {
            gender.setText("男");
            sex1 = true;
        } else {
            gender.setText("女");
            sex1 = false;
        }
        name.setText(bmobUser.getUsername());
    }

    //点击事件
    protected void onClick(){

        editPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText phone = new EditText(mContext);
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请输入")
                        .setView(phone)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                phoneNum.setText(phone.getText().toString());
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
            }
        });
        editClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final String[] classText = {"大一","大二","大三","大四","研一","研二","研三","博士"};
                builder.setSingleChoiceItems(classText, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Class.setText(classText[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        editSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final String[] schoolText = {"河北工业大学——北辰校区","河北工业大学——红桥校区"};
                builder.setSingleChoiceItems(schoolText, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        school.setText(schoolText[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        editGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final String[] sex = {"男","女"};
                builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (sex[i].equals("男")) {
                            sex1 = true;
                        } else {
                            sex1 = false;
                        }
                        gender.setText(sex[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        editname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText name1 = new EditText(mContext);
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("请输入姓名")
                        .setView(name1)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                name.setText(name1.getText().toString());
                            }
                        })
                        .setNegativeButton("取消",null);
                builder.show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editOk:
                updateInfo();
                break;
        }
    }

    private void updateInfo() {
        //更新数据
        _User newUser = new _User();
        if (bmobUser.getMobilePhoneNumber() != phoneNum.getText().toString()) {
            newUser.setMobilePhoneNumber(phoneNum.getText().toString());
        }
        if (!Objects.equals(bmobUser.getUsername(), name.getText().toString())) {
            newUser.setUsername(name.getText().toString());
        }
        newUser.setUserClass(Class.getText().toString());
        newUser.setUserSchool(school.getText().toString());
        newUser.setUsersex(sex1);
        BmobUser bmobUser = BmobUser.getCurrentUser();
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                } else {
                    Toast.makeText(mContext, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
