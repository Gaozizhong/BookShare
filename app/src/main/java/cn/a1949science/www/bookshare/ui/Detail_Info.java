package cn.a1949science.www.bookshare.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class Detail_Info extends AppCompatActivity {

    Context mContext = Detail_Info.this;
    ImageView before;
    TextView dorm,phoneNum,Class,school,gender,name,edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__info);

        findView();
        onClick();
        showDetailInfo();

    }
    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        dorm = (TextView) findViewById(R.id.dorm);
        phoneNum = (TextView) findViewById(R.id.phoneNum);
        Class = (TextView) findViewById(R.id.Class);
        school = (TextView) findViewById(R.id.school);
        gender = (TextView) findViewById(R.id.gender);
        name = (TextView) findViewById(R.id.name);
        edit = (TextView)findViewById(R.id.edit);
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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,Edit_Detail_Info.class);
                startActivity(it);
            }
        });
    }

    private void showDetailInfo() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        bmobUser.getObjectId();
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.getObject(bmobUser.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    dorm.setText(user.getUserDorm());
                    phoneNum.setText(user.getMobilePhoneNumber());
                    Class.setText(user.getUserClass());
                    school.setText(user.getUserSchool());
                    if (user.getUsersex()) {
                        gender.setText("男");
                    } else {
                        gender.setText("女");
                    }
                    name.setText(user.getUsername());
                } else {
                    Toast.makeText(mContext, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
