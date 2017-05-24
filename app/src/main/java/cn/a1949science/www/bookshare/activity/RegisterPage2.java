package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.*;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.widget.CircleImageView;
import cn.a1949science.www.bookshare.widget.GlideImageLoader;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class RegisterPage2 extends AppCompatActivity implements View.OnClickListener{
    Context mContext = RegisterPage2.this;
    Toolbar toolbar;
    TextView title;
    CircleImageView favicon;
    EditText nickname;
    Button registerOk;
    private com.lzy.imagepicker.ImagePicker imagePicker;
    String picturePath="";
    BmobFile bmobFile;
    BmobUser bmobUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page2);
        imagePicker = com.lzy.imagepicker.ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        initView();
    }

    private void initView() {
        bmobUser = BmobUser.getCurrentUser();
        title = (TextView) findViewById(R.id.title);
        title.setText("注册");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        nickname = (EditText) findViewById(R.id.nickname);
        favicon = (CircleImageView) findViewById(R.id.favicon);
        favicon.setOnClickListener(this);
        registerOk = (Button) findViewById(R.id.registerOk);
        registerOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favicon:
                updateFavicon();
                break;
            case R.id.registerOk:
                register();
                break;
        }
    }

    private void register() {
        if (TextUtils.isEmpty(nickname.getText().toString())) {
            nickname.setText(bmobUser.getMobilePhoneNumber());
        }if (TextUtils.isEmpty(picturePath)) {
            Toast.makeText(mContext, "头像不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        updateFile();
        //更新数据
        _User newUser = new _User();
        newUser.setNickname(nickname.getText().toString());
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Intent it = new Intent(mContext,Identification.class);
                    startActivity(it);
                    finish();
                    overridePendingTransition(R.anim.slide_left_out,R.anim.slide_right_in);
                } else {
                    Toast.makeText(mContext, "注册失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateFavicon() {
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = 200;
        Integer height = 200;
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(800);
        imagePicker.setOutPutY(800);

        Intent intent1 = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent1, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == com.lzy.imagepicker.ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(com.lzy.imagepicker.ImagePicker.EXTRA_RESULT_ITEMS);
                picturePath = images.get(0).path;
                Glide.with(mContext)
                        .load(picturePath)
                        .thumbnail(0.5f)
                        .override((int)(mContext.getResources().getDisplayMetrics().density*80+0.5f),(int)(mContext.getResources().getDisplayMetrics().density*80+0.5f))
                        .into(favicon);
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //上传图片
    private void updateFile() {
        //用Luban压缩图片
        Luban.get(mContext)
                .load(new File(picturePath))
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        bmobFile = new BmobFile(file);
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    //更新数据
                                    _User newUser = new _User();
                                    newUser.setFavicon(bmobFile);
                                    BmobUser bmobUser = BmobUser.getCurrentUser();
                                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(mContext, "头像修改成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(mContext, "头像修改失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "头像上传失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }).launch();

    }
}
