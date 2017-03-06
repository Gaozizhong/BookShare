package cn.a1949science.www.bookshare.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class User_Info extends AppCompatActivity {
    Context mContext = User_Info.this;
    ImageView before;
    TextView nickname;
    // 拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    // 从相册选取照片
    private static final int PHOTO_REQUEST_GALLERY = 2;
    // 剪切照片
    private static final int PHOTO_REQUEST_CUT = 3;
    String picturePath="";
    //定义一个保存图片的File变量
    private File imageFile = null;
    private Uri fileUri;
    private ImageView headIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__info);

        findView();
        onClick();
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
                } else {
                    Toast.makeText(mContext, "昵称显示失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        //上传头像
        final BmobFile bmobFile = new BmobFile(new File(picturePath));
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

    //选择图书图片
    private void selectBookPicture() {
        final String[] items = {"相册","拍照"};
        AlertDialog dlg = new AlertDialog.Builder(mContext)
                .setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // 这里item是根据选择的方式，
                        if (item == 0) {
                            //打开相册，选择一张图片
                            Intent intent1 = new Intent(Intent.ACTION_PICK);
                            intent1.setType("image/*");
                            startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
                        } else {
                            //激活相机
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //进入相机前判断下sdcard是否可用，可用进行存储
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                //获取系统的公用图像文件路径
                                picturePath = Environment.getExternalStoragePublicDirectory
                                        (Environment.DIRECTORY_PICTURES).toString();
                                //利用当前时间组合成一个不会重复的文件名
                                String fname = "p"+ System.currentTimeMillis() +".jpg";
                                //按前面的路径和文件名创建Uri对象
                                imageFile = new File(picturePath,fname);
                                fileUri = Uri.fromFile(imageFile);
                                //将uri加到拍照Intent的额外数据中
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                //保存照片的质量
                                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                                //启动相机拍照
                                startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                            } else {
                                Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).create();
        dlg.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY && data != null) {

            Uri selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();

        } else if (requestCode == PHOTO_REQUEST_CAREMA && data != null) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                crop(Uri.fromFile(imageFile));
            } else {
                Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PHOTO_REQUEST_CUT&& data != null) {
            final Bitmap bitmap = data.getParcelableExtra("data");
            headIcon.setImageBitmap(bitmap);
            // 保存图片到internal storage
            FileOutputStream outputStream;
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                // out.close();
                // final byte[] buffer = out.toByteArray();
                // outputStream.write(buffer);
                outputStream = mContext.openFileOutput("_head_icon.jpg",
                        Context.MODE_PRIVATE);
                out.writeTo(outputStream);
                out.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            if (imageFile != null && imageFile.exists())
                imageFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        //裁剪框比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        //图片格式
        intent.putExtra("outputFormat", "JPEG");
        //取消人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //点击事件
    private void onClick() {
        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //地址查询
    private void findView() {
        before = (ImageView) findViewById(R.id.before);
        nickname = (TextView) findViewById(R.id.nickname);
    }

    public void goToDetailInfo(View view) {
        Intent it = new Intent(this,Detail_Info.class);
        startActivity(it);
    }
    public void goToEditPassword(View view) {
        Intent it = new Intent(this,EditPassword.class);
        startActivity(it);
    }

    public void goToEdit(View view) {
        Intent it = new Intent(this,EditNickname.class);
        startActivity(it);
    }

    public void goToFavicon(View view) {
        selectBookPicture();
    }
}
