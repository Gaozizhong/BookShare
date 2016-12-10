package cn.a1949science.www.bookshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.a1949science.www.bookshare.SQL_table._User;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

public class Login_Page extends AppCompatActivity {

    Context mContext = Login_Page.this;
    Button getCode,login,register;
    EditText phoneNum,code;
    MyCountTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BmobSMS.initialize(mContext,"13d736220ecc496d7dcb63c7cf918ba7");
        setContentView(R.layout.activity_login__page);

        findView();
        onClick();
    }

    //查找地址
    private void findView(){
        getCode = (Button) findViewById(R.id.getCode);
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        code = (EditText) findViewById(R.id.code);
        login  = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
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

                    getCode.setEnabled(false);

                    timer = new MyCountTimer(60000, 1000);
                    timer.start();
                    //请求发送验证码
                    BmobSMS.requestSMSCode(mContext, number, "登录验证", new RequestSMSCodeListener() {

                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, "验证码发送成功", Toast.LENGTH_SHORT).show();
                            } else {
                                timer.cancel();
                                getCode.setEnabled(true);
                                getCode.setText("重新发送验证码");
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "请输入手机号码", Toast.LENGTH_SHORT).show();
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //验证验证码
                BmobSMS.verifySmsCode(mContext, String.valueOf(phoneNum.getText()), String.valueOf(code.getText()), new VerifySMSCodeListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //跳转到Home_Page
                                    Intent it = new Intent(mContext,Home_Page.class);
                                    startActivity(it);

                                } else {
                                    Toast.makeText(mContext, "验证失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                });

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BmobUser bu = new BmobUser();
                bu.setUsername("小高");
                bu.setPassword("123456");
                bu.setMobilePhoneNumber("13582601738");
                //注意：不能用save方法进行注册
                bu.signUp(new SaveListener<_User>() {
                    @Override
                    public void done(_User user, cn.bmob.v3.exception.BmobException e) {
                        if (e == null) {
                            Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "注册失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}
