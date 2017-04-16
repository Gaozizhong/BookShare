package cn.a1949science.www.bookshare.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Advice_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Advice_Page extends AppCompatActivity {
    Button advBtn;
    EditText content;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice__page);
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
        advBtn = (Button) findViewById(R.id.advBtn);
        content = (EditText) findViewById(R.id.content);
    }

    //点击事件
    protected void onClick(){
        advBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(content.getText().toString())) {
                    Toast.makeText(Advice_Page.this, "请输入您的建议", Toast.LENGTH_SHORT).show();
                    return;
                }
                advBtn.setClickable(false);
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                String username = bmobUser.getUsername();
                String userPhone = bmobUser.getMobilePhoneNumber();
                Advice_Info advice = new Advice_Info();
                advice.setAdviceContent(content.getText().toString());
                advice.setUserPhone(userPhone);
                advice.setUserName(username);
                advice.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Toast.makeText(Advice_Page.this, "感谢您的建议", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Advice_Page.this, "建议失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            advBtn.setClickable(true);
                        }
                    }
                });
            }
        });


    }
}
