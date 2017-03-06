package cn.a1949science.www.bookshare.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class Edit_Detail_Info extends AppCompatActivity {

    Context mContext = Edit_Detail_Info.this;
    ImageView before;
    TextView dorm,phoneNum,Class,school,gender,name,editok;
    View editDorm,editPhone,editClass,editSchool,editGender,editname;
    boolean sex1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__detail__info);
        findView();
        onClick();
    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        editok = (TextView) findViewById(R.id.editOk);
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

        editok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //更新数据
                _User newUser = new _User();
                newUser.setUserDorm(dorm.getText().toString());
                newUser.setMobilePhoneNumber(phoneNum.getText().toString());
                newUser.setUserClass(Class.getText().toString());
                newUser.setUserSchool(school.getText().toString());
                newUser.setUsersex(sex1);
                newUser.setUsername(name.getText().toString());
                BmobUser bmobUser = BmobUser.getCurrentUser();
                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(mContext, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        editDorm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                final String[] dromText = {"西一","西二","西三","西四","西五",
                        "东一","东二","东三","东四", "东五","东六", "东七","东八","东九",
                        "其他"};
                builder1.setSingleChoiceItems(dromText, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dorm.setText(dromText[i]);

                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                        final String[] drom2Text = {"A","B"};
                        builder2.setSingleChoiceItems(drom2Text, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dorm.setText(dorm.getText().toString()+drom2Text[i]);

                                final AlertDialog.Builder builder3 = new AlertDialog.Builder(mContext);
                                final String[] drom3Text = {"1","2","3","4","5"};
                                builder3.setSingleChoiceItems(drom3Text, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dorm.setText(dorm.getText().toString()+drom3Text[i]);

                                        final AlertDialog.Builder builder4 = new AlertDialog.Builder(mContext);
                                        final String[] drom4Text = {"01","02","03","04","05","06","07","08","09","10"
                                                ,"11","12","13","14","15","16","17","18","19","20"
                                                ,"21","22","23","24","25","26","27","28","29","30"
                                                ,"31","32","33","34","35","36","37","38","39","40"
                                                ,"41","42","43","44","45","46","47","48","49","50"
                                                ,"51","52","53","54","55","56","57","58","59","60"
                                                ,"61","62","63","64","65","66","67","68","69","70"};
                                        builder4.setSingleChoiceItems(drom4Text, 0, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dorm.setText(dorm.getText().toString()+drom4Text[i]);
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder4.show();

                                        dialogInterface.dismiss();
                                    }
                                });
                                builder3.show();

                                dialogInterface.dismiss();
                            }
                        });
                        builder2.show();

                        dialogInterface.dismiss();
                    }
                });
                builder1.show();
            }
        });

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

}
