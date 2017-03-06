package cn.a1949science.www.bookshare.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class RegisterPage extends AppCompatActivity {

    Context mContext = RegisterPage.this;
    Button getCode,registerOk;
    EditText name,gender,school,dorm,Class,phoneNum,code,password,password2;
    MyCountTimer timer;
    boolean sex1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
        BmobSMS.initialize(mContext,"13d736220ecc496d7dcb63c7cf918ba7");


        findView();
        onClick();
    }

    //查找地址
    private void findView(){
        name = (EditText) findViewById(R.id.name);
        gender = (EditText) findViewById(R.id.gender);
        school = (EditText) findViewById(R.id.school);
        dorm = (EditText) findViewById(R.id.dorm);
        Class = (EditText) findViewById(R.id.Class);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        code = (EditText) findViewById(R.id.code);
        getCode = (Button) findViewById(R.id.getCode);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        registerOk = (Button) findViewById(R.id.registerOk);
    }

    //按钮效果
    class MyCountTimer extends CountDownTimer {

        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setEnabled(false);
            getCode.setText((millisUntilFinished / 1000) +"秒后重发");
        }
        @Override
        public void onFinish() {
            getCode.setEnabled(true);
            getCode.setText("重新发送验证码");
        }
    }

    //点击事件
    private void onClick(){

        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = phoneNum.getText().toString();
                //如果输入的手机号不为空的话
                if (!TextUtils.isEmpty(number)) {

                    timer = new MyCountTimer(60000, 1000);
                    timer.start();
                    //请求发送验证码
                    BmobSMS.requestSMSCode(mContext, number, "登录验证", new RequestSMSCodeListener() {

                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                getCode.setEnabled(false);
                                Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                timer.cancel();
                                getCode.setEnabled(true);
                                getCode.setText("重新发送");
                                Toast.makeText(mContext, "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }

            }
        });

        registerOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(name.getText().toString())) {
                    Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(password.getText().toString()).equals(password2.getText().toString())) {
                    Toast.makeText(mContext, "两次密码不相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length()>18||password.getText().toString().length()<6)
                {
                    Toast.makeText(mContext, "请输入6~18位密码", Toast.LENGTH_LONG).show();
                    return;
                }
                if(phoneNum.getText().toString().length()!=11)
                {
                    Toast.makeText(mContext, "请输入11位有效号码", Toast.LENGTH_LONG).show();
                    return;
                }

                //验证验证码
                BmobSMS.verifySmsCode(mContext, String.valueOf(phoneNum.getText()), String.valueOf(code.getText()),
                        new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            registerUser();

                        } else {
                            Toast.makeText(mContext, "验证失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        //选择性别
        gender.setOnClickListener(new View.OnClickListener() {
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

        //选择学校
        school.setOnClickListener(new View.OnClickListener() {
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

        //选择宿舍号
        dorm.setOnClickListener(new View.OnClickListener() {
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

        Class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final String[] classsText = {"大一","大二","大三","大四","研一","研二","研三","博士"};
                builder.setSingleChoiceItems(classsText, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Class.setText(classsText[i]);
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });


    }

    //注册
    private void registerUser(){

        //注册
        _User user = new _User();
        user.setUsername(name.getText().toString());
        user.setUsersex(sex1);
        user.setUserSchool(school.getText().toString());
        user.setUserDorm(dorm.getText().toString());
        user.setUserClass(Class.getText().toString());
        user.setMobilePhoneNumber(phoneNum.getText().toString());
        user.setPassword(password.getText().toString());
        user.setNeedReturn(false);

        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User s, cn.bmob.v3.exception.BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "注册成功！", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                } else {
                    Toast.makeText(mContext, "手机号已被注册！", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}

