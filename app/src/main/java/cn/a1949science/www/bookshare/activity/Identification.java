package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.widget.GlideImageLoader;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class Identification extends AppCompatActivity implements View.OnClickListener{
    Context mContext = Identification.this;
    Toolbar toolbar;
    Button updateImage,afterThat,certificationOk;
    EditText name,gender,school,Class;
    boolean sex1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        updateImage = (Button) findViewById(R.id.updateImage);
        updateImage.setOnClickListener(this);
        afterThat = (Button) findViewById(R.id.afterThat);
        afterThat.setOnClickListener(this);
        certificationOk = (Button) findViewById(R.id.certificationOk);
        certificationOk.setOnClickListener(this);
        name = (EditText) findViewById(R.id.name);
        gender = (EditText) findViewById(R.id.gender);
        gender.setOnClickListener(this);
        school = (EditText) findViewById(R.id.school);
        school.setOnClickListener(this);
        Class = (EditText) findViewById(R.id.Class);
        Class.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateImage:
                Intent intent = new Intent(mContext, Identification2.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
                break;
            case R.id.afterThat:
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                break;
            case R.id.certificationOk:
                certification();
                break;
            case R.id.gender:
                //选择性别
                selectGender();
                break;
            case R.id.school:
                //选择学校
                selectSchool();
                break;
            case R.id.Class:
                //选择年级
                selectClass();
                break;
        }
    }

    private void selectClass() {
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

    private void selectSchool() {
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

    private void selectGender() {
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

    private void certification() {
        if (TextUtils.isEmpty(name.getText().toString())) {
            Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(gender.getText().toString())) {
            Toast.makeText(mContext, "性别不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(school.getText().toString())) {
            Toast.makeText(mContext, "学校不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Class.getText().toString())) {
            Toast.makeText(mContext, "年级不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //更新数据
        BmobUser bmobUser = BmobUser.getCurrentUser();
        _User newUser = new _User();
        newUser.setUsername(name.getText().toString());
        newUser.setUsersex(sex1);
        newUser.setUserSchool(school.getText().toString());
        newUser.setUserClass(Class.getText().toString());
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(mContext, "实名认证成功，请等待后台验证", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);

                } else {
                    Toast.makeText(mContext, "实名认证失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
