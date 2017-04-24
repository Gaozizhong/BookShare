package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;

import java.io.File;
import java.util.ArrayList;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.widget.GlideImageLoader;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class User_Info extends AppCompatActivity {
    Context mContext = User_Info.this;
    ImageView favicon;
    TextView nickname;
    String picturePath="";
    private com.lzy.imagepicker.ImagePicker imagePicker;
    BmobFile bmobFile;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        findView();
        display();

    }

    //初始化页面
    private void display() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        bmobUser.getObjectId();
        BmobQuery<_User> query = new BmobQuery<_User>();
        query.getObject(bmobUser.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    nickname.setText(user.getNickname());
                    Glide.with(mContext)
                            .load(user.getFavicon().getFileUrl())
                            .override((int)(mContext.getResources().getDisplayMetrics().density*50+0.5f),(int)(mContext.getResources().getDisplayMetrics().density*50+0.5f))
                            .into(favicon);
                } else {
                    Toast.makeText(mContext, "昵称、头像显示失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == com.lzy.imagepicker.ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(com.lzy.imagepicker.ImagePicker.EXTRA_RESULT_ITEMS);
                picturePath = images.get(0).path;
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        updateFile();
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

    //地址查询
    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        nickname = (TextView) findViewById(R.id.nickname);
        favicon = (ImageView) findViewById(R.id.favicon);
    }

    public void goToDetailInfo(View view) {
        Intent it = new Intent(this,Detail_Info.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }
    public void goToEditPassword(View view) {
        Intent it = new Intent(this,EditPassword.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }
    public void goToEdit(View view) {
        Intent it = new Intent(this,EditNickname.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
    }
    public void goToFavicon(View view) {
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
}
