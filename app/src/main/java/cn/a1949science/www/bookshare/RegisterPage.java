package cn.a1949science.www.bookshare;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                //验证验证码
                BmobSMS.verifySmsCode(mContext, String.valueOf(phoneNum.getText()), String.valueOf(code.getText()),
                        new VerifySMSCodeListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            registerUser();
                            finish();

                        } else {
                            Toast.makeText(mContext, "验证失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    //注册
    private void registerUser(){

        if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!(password.getText().toString()).equals(password2.getText().toString())) {
            Toast.makeText(mContext, "两次密码不能相同", Toast.LENGTH_SHORT).show();
            return;
        }
        if(phoneNum.getText().toString().length()!=11)
        {
            Toast.makeText(mContext, "请输入11位有效号码", Toast.LENGTH_LONG).show();
            return;
        }


        //注册
        _User user = new _User();
        user.setUsername(name.getText().toString());
        user.setUserSchool(school.getText().toString());
        user.setUserDorm(dorm.getText().toString());
        user.setUserClass(Class.getText().toString());
        user.setMobilePhoneNumber(phoneNum.getText().toString());
        user.setPassword(password.getText().toString());

        user.signUp(new SaveListener<_User>() {
            @Override
            public void done(_User s, cn.bmob.v3.exception.BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "注册成功！", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    return;
                }
            }
        });

    }
}

