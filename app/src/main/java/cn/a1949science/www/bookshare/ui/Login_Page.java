package cn.a1949science.www.bookshare.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class Login_Page extends AppCompatActivity {
    Context mContext = Login_Page.this;
    Button login,register;
    EditText phoneNum,password;
    CheckBox pass_input;
    SharedPreferences sp;
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
        pass_input = (CheckBox) findViewById(R.id.pass_input);
        sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
        if (sp.getBoolean("pass_input", false)) {
            phoneNum.setText(sp.getString("phoneNum",null));
            password.setText(sp.getString("password",null));
            pass_input.setChecked(true);
        }
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

                //记住密码
                boolean CheckBoxLogin = pass_input.isChecked();
                //按钮被选中，下次进入时显示账号和密码
                if (CheckBoxLogin) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("phoneNum", phone);
                    editor.putString("password", passWord);
                    editor.putBoolean("pass_input", true);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("phoneNum", null);
                    editor.putString("password", null);
                    editor.putBoolean("pass_input", false);
                    editor.commit();
                }

                login.setClickable(false);

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
                            login.setClickable(true);
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
