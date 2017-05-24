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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

public class Identification2 extends AppCompatActivity implements View.OnClickListener{
    Context mContext = Identification2.this;
    Toolbar toolbar;
    RelativeLayout update;
    Button updateOk;
    private com.lzy.imagepicker.ImagePicker imagePicker;
    String picturePath="";
    BmobFile bmobFile;
    ImageView idCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification2);
        imagePicker = com.lzy.imagepicker.ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
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
        update = (RelativeLayout) findViewById(R.id.updateImg);
        update.setOnClickListener(this);
        updateOk = (Button) findViewById(R.id.updateOk);
        updateOk.setOnClickListener(this);
        idCard = (ImageView) findViewById(R.id.idCard);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.updateImg:
                updateImg();
                break;
            case R.id.updateOk:
                updateFile();
                break;
        }
    }

    private void updateImg() {
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = 300;
        Integer height = 200;
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(1200);
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
                        .override((int)(mContext.getResources().getDisplayMetrics().density*300+0.5f),(int)(mContext.getResources().getDisplayMetrics().density*200+0.5f))
                        .into(idCard);

            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //上传图片
    private void updateFile() {
        if (TextUtils.isEmpty(picturePath)) {
            Toast.makeText(mContext, "图片不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
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
                                    newUser.setStudentCard(bmobFile);
                                    BmobUser bmobUser = BmobUser.getCurrentUser();
                                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(mContext, "学生证上传成功", Toast.LENGTH_SHORT).show();
                                                finish();
                                                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                                            } else {
                                                Toast.makeText(mContext, "学生证上传失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(mContext, "学生证上传失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
