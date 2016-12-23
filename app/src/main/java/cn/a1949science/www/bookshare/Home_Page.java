package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.R.attr.data;
import static cn.a1949science.www.bookshare.R.id.book;
import static cn.a1949science.www.bookshare.R.id.bookName;
import static cn.a1949science.www.bookshare.R.id.bookPicture;
import static cn.a1949science.www.bookshare.R.id.bookWriter;
import static cn.a1949science.www.bookshare.R.id.name;
import static cn.a1949science.www.bookshare.R.id.shareTime;
import static cn.a1949science.www.bookshare.R.layout.activity_home__page;


public class Home_Page extends AppCompatActivity {
    public Context home = Home_Page.this;
    Context mContext = Home_Page.this;
    FrameLayout next_layout;
    Button shareBtn,returnBtn;
    Animation animation = null;
    ImageButton shortCut;
    ImageView searchImg;
    ImageView menuImg;
    ListView list;
    boolean clicked  = false;
    private String[] books = new String[]
            {"偷影子的人", "岛上书店", "摆渡人", "大唐李白·少年游",
                    "大唐李白·凤凰台", "大唐李白·将进酒"};
    private String[] writers = new String[]
            {"[法]马克·李维", "[美]加布瑞埃拉·泽文", "[英]克莱尔·麦克福尔",
                    "[中]张大春", "[中]张大春", "[中]张大春"};
    private int[] imageIds = new int[]
            {R.drawable.touyingzideren, R.drawable.daoshangshudian, R.drawable.baiduren,
                    R.drawable.datanglibai1, R.drawable.datanglibai2, R.drawable.datanglibai3};
    private Uri fileUri;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final String PHOTO_FILE_NAME = "book_photo.jpg";
    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home__page);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");

        findView();
        onClick();


        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < books.length; i++) {
            Map<String, Object> listItem = new HashMap<>();
            listItem.put("imageName", imageIds[i]);
            listItem.put("bookName", books[i]);
            listItem.put("writerName", writers[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.booklist_item,
                new String[]{"imageName", "bookName", "writerName"},
                new int[]{R.id.image, R.id.book, R.id.writer});

        //为ListView设置Adapter
        assert list != null;
        list.setAdapter(simpleAdapter);

    }
    //查找地址
    private void findView() {
        next_layout = (FrameLayout) findViewById(R.id.home__page);
        shortCut = (ImageButton) findViewById(R.id.shortcut);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        menuImg = (ImageView) findViewById(R.id.menuImg);
        list = (ListView) findViewById(R.id.booklist);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        returnBtn = (Button) findViewById(R.id.returnBtn);
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
                            //进入相机前判断下sdcard是否可用，可用进行存储
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                //获取系统的公用图像文件路径
                                String dir = Environment.getExternalStoragePublicDirectory
                                        (Environment.DIRECTORY_PICTURES).toString();
                                //利用当前时间组合成一个不会重复的文件名
                                String fname = "p"+ System.currentTimeMillis() +".jpg";
                                //按前面的路径和文件名创建Uri对象
                                fileUri = Uri.parse("file://" + dir + "/" + fname);
                                //激活相机
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                //将uri加到拍照Intent的额外数据中
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                            } else {
                                Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }).create();
        dlg.show();
    }

    //上传图书图片
    private void updateBookPicture(String picturePath) {
        final BmobFile bmobFile = new BmobFile(new File(picturePath));
        new AlertDialog.Builder(mContext)
                .setMessage("上传此图片？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bmobFile.uploadblock(new UploadFileListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(mContext, "上传成功:" /*+ bmobFile.getFileUrl()*/, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "上传失败:" /*+ e.getMessage()*/, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(mContext, "请重新选择图片", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

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
            updateBookPicture(picturePath);

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            Uri tookImage = data.getData();
            //Toast.makeText(mContext, tookImage.toString(), Toast.LENGTH_SHORT).show();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(tookImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Toast.makeText(mContext, picturePath, Toast.LENGTH_SHORT).show();
            cursor.close();
            //updateBookPicture(picturePath);
        }
    }


    //点击事件
    private void onClick() {
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出信息框
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.sharing, (ViewGroup) findViewById(R.id.sharing_Dialog));

                final Button bookPicture = (Button) layout.findViewById(R.id.bookPicture);
                //为上传图片按钮设置点击事件
                bookPicture.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectBookPicture();
                    }
                });
                final EditText bookName = (EditText) layout.findViewById(R.id.bookName);
                final EditText describe = (EditText) layout.findViewById(R.id.describe);
                final EditText bookWriter = (EditText) layout.findViewById(R.id.bookWriter);
                final EditText shareTime = (EditText) layout.findViewById(R.id.shareTime);
                new AlertDialog.Builder(mContext)
                        .setTitle("图书共享")
                        .setPositiveButton("确认共享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (bookName.getText().toString().equals("")  || describe.getText().toString().equals("")
                                        || bookWriter.getText().toString() .equals("") || shareTime.getText().toString() .equals("")) {
                                    Toast.makeText(mContext, "信息不全！！！", Toast.LENGTH_SHORT).show();
                                } else {
                                    _User bmobUser = BmobUser.getCurrentUser(_User.class);
                                    Integer username = bmobUser.getUserNum();
                                    Book_Info book = new Book_Info();
                                    book.setOwnerNum(username.toString());
                                    //book.setBookPicture(picturePath);
                                    book.setBookName(bookName.getText().toString());
                                    book.setBookDescribe(describe.getText().toString());
                                    book.setBookWriter(bookWriter.getText().toString());
                                    book.setkeepTime(shareTime.getText().toString());
                                    book.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(mContext, "图书共享成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "图书共享失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setView(layout)
                        .show();
            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出信息框
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.raturning_yes, (ViewGroup) findViewById(R.id.returning_Yes_Dialog));

                new AlertDialog.Builder(mContext)
                        .setPositiveButton("确认还书", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setView(layout)
                        .setTitle("图书归还")
                        .show();

            }
        });

        assert shortCut != null;
        shortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
                returnBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
                if (!clicked ) {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_add);
                } else {
                    animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_return);
                }
                animation.setFillAfter(true);
                shortCut.startAnimation(animation);
                clicked = !clicked;

                next_layout.setClickable(!clicked);
                next_layout.setBackgroundColor(clicked ? Color.parseColor("#ddffffff") : Color.parseColor("#ebecf0"));

            }
        });

        assert searchImg != null;
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "过两天就能用了！！！", Toast.LENGTH_SHORT).show();
            }
        });

        assert menuImg != null;
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Menu_page.class);
                startActivity(intent);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle data = new Bundle();
                /**
                 *利用Intent传递数据
                 */
                data.putInt("imageid", imageIds[i]);
                data.putString("bookname", books[i]);
                data.putString("writername", writers[i]);
                Intent intent = new Intent(mContext, Book_detail.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });
    }
}
