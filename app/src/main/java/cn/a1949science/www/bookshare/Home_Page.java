package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.a1949science.www.bookshare.R.layout.activity_home__page;


public class Home_Page extends AppCompatActivity {
    Context mContext = Home_Page.this;
    FrameLayout next_layout;
    Button shareBtn,returnBtn;
    Animation animation = null;
    ImageButton shortCut;
    ImageView searchImg;
    ImageView menuImg;
    ListView listview;
    boolean clicked  = false;
    private Uri fileUri;
    // 拍照
    private static final int PHOTO_REQUEST_CAREMA = 1;
    // 从相册选取照片
    private static final int PHOTO_REQUEST_GALLERY = 2;
    String picturePath="";
    //定义一个保存图片的File变量
    private File imageFile = null;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_home__page);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");

        findView();
        onClick();
        displayList();

    }

    //查找地址
    private void findView() {
        next_layout = (FrameLayout) findViewById(R.id.home__page);
        shortCut = (ImageButton) findViewById(R.id.shortcut);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        menuImg = (ImageView) findViewById(R.id.menuImg);
        listview = (ListView) findViewById(R.id.booklist);
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
                                picturePath = Environment.getExternalStoragePublicDirectory
                                        (Environment.DIRECTORY_PICTURES).toString();
                                //利用当前时间组合成一个不会重复的文件名
                                String fname = "p"+ System.currentTimeMillis() +".jpg";
                                //按前面的路径和文件名创建Uri对象
                                imageFile = new File(picturePath,fname);
                                fileUri = Uri.fromFile(imageFile);
                                //激活相机
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
            //Toast.makeText(mContext, picturePath, Toast.LENGTH_SHORT).show();
            cursor.close();

        } else if (requestCode == PHOTO_REQUEST_CAREMA && data != null) {

            Toast.makeText(mContext, picturePath, Toast.LENGTH_SHORT).show();
        }
    }

    //点击事件
    private void onClick() {
        //分享按钮
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
                                        || bookWriter.getText().toString() .equals("") || shareTime.getText().toString() .equals("")||picturePath.equals("")) {
                                    Toast.makeText(mContext, "信息不全！！！", Toast.LENGTH_SHORT).show();
                                } else {
                                    final BmobFile bmobFile = new BmobFile(new File(picturePath));
                                    new AlertDialog.Builder(mContext)
                                            .setMessage("上传此图片？")
                                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //等待框
                                                    final ProgressDialog progressDialog = new ProgressDialog(mContext);
                                                    progressDialog.setMessage("正在上传...");
                                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                                    progressDialog.show();
                                                    //上传图片
                                                    bmobFile.uploadblock(new UploadFileListener() {
                                                        @Override
                                                        public void done(BmobException e) {
                                                            if (e == null) {
                                                                picturePath = "";
                                                                progressDialog.dismiss();
                                                                //上传其他信息
                                                                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                                                                Integer usernum = bmobUser.getUserNum();
                                                                String username = bmobUser.getUsername();
                                                                Book_Info book = new Book_Info();
                                                                book.setOwnerNum(usernum.toString());
                                                                book.setOwnerName(username);
                                                                book.setBookName(bookName.getText().toString());
                                                                book.setBookDescribe(describe.getText().toString());
                                                                book.setBookWriter(bookWriter.getText().toString());
                                                                book.setkeepTime(shareTime.getText().toString());
                                                                book.setBookPicture(bmobFile);
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
                                                            } else {
                                                                Toast.makeText(mContext, "上传失败" , Toast.LENGTH_SHORT).show();
                                                                progressDialog.dismiss();
                                                            }
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Toast.makeText(mContext, "请重新填写分享信息", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .show();
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

        //归还按钮
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出信息框
                LayoutInflater inflater = getLayoutInflater();
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                //Toast.makeText(mContext, bmobUser.getNeedReturn().toString(), Toast.LENGTH_SHORT).show();
                if (bmobUser.getNeedReturn()){
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
                }else {
                    View layout = inflater.inflate(R.layout.returning_no, (ViewGroup) findViewById(R.id.raturning_No_Dialog));
                    new AlertDialog.Builder(mContext)
                            .setPositiveButton("我要借书", new DialogInterface.OnClickListener() {
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
    }

    //显示列表
    private void displayList() {
        //查找book
        BmobQuery<Book_Info> query = new BmobQuery<>();
        //查找出有ownerNum的信息
        query.addWhereExists("ownerNum");
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    final int[] bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    listview.setAdapter(new MyAdapter(mContext,list));

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Bundle data = new Bundle();
                            //利用Intent传递数据
                            //data.putString("imageid", list.get(i).getBookPicture().getFileUrl());
                            data.putInt("booknum",bookNum[i]);
                            data.putString("objectId",list.get(i).getObjectId());
                            Intent intent = new Intent(mContext, Book_detail.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        }
                    });
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        //点击两次返回键退出程序
        if(System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
