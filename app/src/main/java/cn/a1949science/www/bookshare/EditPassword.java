package cn.a1949science.www.bookshare;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class EditPassword extends AppCompatActivity {
    Context mContext = EditPassword.this;
    ImageView before;
    EditText oldPassword,newPassword1,newPassword2;
    Button EditOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
        BmobSMS.initialize(mContext,"13d736220ecc496d7dcb63c7cf918ba7");
        findView();
        onClick();
    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword1 = (EditText) findViewById(R.id.newPassword1);
        newPassword2 = (EditText) findViewById(R.id.newPassword2);
        EditOk = (Button) findViewById(R.id.EditOk);
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

        EditOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bmob.initialize(mContext, "13d736220ecc496d7dcb63c7cf918ba7");
                if (TextUtils.isEmpty(oldPassword.getText().toString())) {
                    Toast.makeText(mContext, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPassword1.getText().toString())) {
                    Toast.makeText(mContext, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPassword2.getText().toString())) {
                    Toast.makeText(mContext, "新密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((oldPassword.getText().toString()).equals(newPassword1.getText().toString())) {
                    Toast.makeText(mContext, "新旧密码不能相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!(newPassword1.getText().toString()).equals(newPassword2.getText().toString())) {
                    Toast.makeText(mContext, "两次密码不相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newPassword1.getText().toString().length()>18||newPassword1.getText().toString().length()<6)
                {
                    Toast.makeText(mContext, "请输入6~18位密码", Toast.LENGTH_LONG).show();
                    return;
                }
                //修改密码
                BmobUser.updateCurrentUserPassword(oldPassword.getText().toString(), newPassword1.getText().toString(),
                        new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "密码修改失败"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void Editing() {

    }
}
