package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPassword extends AppCompatActivity implements
        View.OnClickListener{
    Context mContext = ForgetPassword.this;
    Button editOk, getCode;
    EditText phoneNum,code,password,password2;
    MyCountTimer timer;
    Toolbar toolbar;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        title = (TextView) findViewById(R.id.title);
        title.setText("忘记密码");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        code = (EditText) findViewById(R.id.code);
        getCode = (Button) findViewById(R.id.getCode);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        editOk = (Button) findViewById(R.id.editOk);
        getCode.setOnClickListener(this);
        editOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getCode:
                getCodeNum();
                break;
            case R.id.editOk:
                editPassword();
                break;
        }
    }

    private void getCodeNum() {
        String number = phoneNum.getText().toString();
        //如果输入的手机号不为空的话
        if (number.length()==11) {
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

    private void editPassword() {
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
        //验证验证码
        BmobUser.resetPasswordBySMSCode(String.valueOf(code.getText()), String.valueOf(password.getText()), new UpdateListener() {
            @Override
            public void done(cn.bmob.v3.exception.BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "密码重置成功", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                } else {
                    Toast.makeText(mContext, "密码重置失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //按钮效果
    private class MyCountTimer extends CountDownTimer {

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
}
