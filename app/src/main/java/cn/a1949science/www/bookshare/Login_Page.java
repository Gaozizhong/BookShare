package cn.a1949science.www.bookshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class Login_Page extends AppCompatActivity {
    Context mContext = Login_Page.this;
    Button login,register;
    EditText phoneNum,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
        BmobSMS.initialize(mContext,"13d736220ecc496d7dcb63c7cf918ba7");
        setContentView(R.layout.activity_login__page);

        findView();
        onClick();
    }

    //查找地址
    private void findView(){
        phoneNum = (EditText) findViewById(R.id.phoneNum);
        password = (EditText) findViewById(R.id.password);
        login  = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
    }

    //点击事件
    private void onClick(){

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = phoneNum.getText().toString();
                String passWord = password.getText().toString();
                //非空验证
                if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(passWord)){
                    Toast.makeText(mContext, "手机号码或密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                _User user = new _User();
                user.setUsername(phone);
                user.setPassword(passWord);


                BmobUser.loginByAccount(phone,passWord,new LogInListener<_User>() {
                    @Override
                    public void done(_User user, BmobException e) {
                        if (e == null) {
                            final ProgressDialog progress = new ProgressDialog(mContext);
                            progress.setMessage("正在登录中...");
                            progress.setCanceledOnTouchOutside(false);
                            progress.show();
                            //Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                            //跳转到主页面
                            Intent it = new Intent(mContext,Home_Page.class);
                            startActivity(it);
                            finish();
                        }else {
                            Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到注册页面
                Intent it = new Intent(mContext,RegisterPage.class);
                startActivity(it);
            }
        });
    }


}
