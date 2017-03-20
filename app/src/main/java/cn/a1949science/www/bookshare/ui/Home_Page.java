package cn.a1949science.www.bookshare.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.a1949science.www.bookshare.MyAdapter;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static cn.a1949science.www.bookshare.R.layout.activity_home__page;


public class Home_Page extends AppCompatActivity {
    Context mContext = Home_Page.this;
    FrameLayout next_layout;
    RelativeLayout Background;
    Button borrowBtn,loanBtn,shareBtn,returnBtn,receiveBtn,handleBtn;
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
    // 剪切照片
    private static final int PHOTO_REQUEST_CUT = 3;
    String picturePath="";
    //定义一个保存图片的File变量
    private File imageFile = null;
    private long exitTime = 0;
    private ImageView headIcon;
    Integer borrowBookNum,loanBookNum,textNum1,textNum2,textNum3,userNum;
    String objectId,time;

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
        Background = (RelativeLayout) findViewById(R.id.Background);
        next_layout = (FrameLayout) findViewById(R.id.home__page);
        shortCut = (ImageButton) findViewById(R.id.shortcut);
        searchImg = (ImageView) findViewById(R.id.searchImg);
        menuImg = (ImageView) findViewById(R.id.menuImg);
        listview = (ListView) findViewById(R.id.booklist);
        borrowBtn = (Button) findViewById(R.id.borrowBtn);
        loanBtn = (Button) findViewById(R.id.loanBtn);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        returnBtn = (Button) findViewById(R.id.returnBtn);
        receiveBtn = (Button) findViewById(R.id.receiveBtn);
        handleBtn = (Button) findViewById(R.id.handleBtn);
    }

    //选择图书图片
    private void selectBookPicture() {
        //打开相册，选择一张图片
        Intent intent1 = new Intent(Intent.ACTION_PICK);
        intent1.setType("image/*");
        startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
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
        }
    }
    //点击事件
    private void onClick() {
        //分享按钮
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fold();
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
                shareTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                        final String[] sharetime = {"1","2","3","4","5","6","7"};
                        builder.setSingleChoiceItems(sharetime, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                shareTime.setText(sharetime[i]);
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                });
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
                                                    progressDialog.setCanceledOnTouchOutside(false);
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
                                                                String username = bmobUser.getUsername();
                                                                Book_Info book = new Book_Info();
                                                                book.setOwner(bmobUser);
                                                                book.setOwnerName(username);
                                                                book.setBookName(bookName.getText().toString());
                                                                book.setBookDescribe(describe.getText().toString());
                                                                book.setBookWriter(bookWriter.getText().toString());
                                                                book.setkeepTime(shareTime.getText().toString());
                                                                book.setBeShared(false);
                                                                book.setBookPicture(bmobFile);
                                                                book.save(new SaveListener<String>() {
                                                                    @Override
                                                                    public void done(String s, BmobException e) {
                                                                        if (e == null) {
                                                                            Toast.makeText(mContext, "图书共享成功", Toast.LENGTH_SHORT).show();
                                                                            displayList();
                                                                        } else {
                                                                            Toast.makeText(mContext, "图书共享失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                                progressDialog.dismiss();
                                                            } else {
                                                                Toast.makeText(mContext, "上传失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                fold();
                //弹出信息框
                LayoutInflater inflater = getLayoutInflater();
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                if (bmobUser.getNeedReturn()){
                    View layout = inflater.inflate(R.layout.raturning_yes, (ViewGroup) findViewById(R.id.returning_Yes_Dialog));
                    TextView returnTime = (TextView) layout.findViewById(R.id.returnTime);
                    returnTime.setText("截止时间："+ time);
                    new AlertDialog.Builder(mContext)
                            .setPositiveButton("确认还书", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    _User bmobUser = BmobUser.getCurrentUser(_User.class);
                                    //查询是否有借书人为自己的借书信息
                                    BmobQuery<Shared_Info> query = new BmobQuery<>();
                                    query.addWhereEqualTo("UserNum", bmobUser.getUserNum());
                                    query.addWhereEqualTo("ifReturn", false);
                                    query.findObjects(new FindListener<Shared_Info>() {
                                        @Override
                                        public void done(final List<Shared_Info> list, BmobException e) {
                                            if (e == null && list.size() != 0) {
                                                borrowBookNum = list.get(0).getBookNum();
                                                if (!list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                                                    textNum1 = 3;
                                                } else if (list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                                                    textNum1 = 7;
                                                }
                                                objectId = list.get(0).getObjectId();

                                                Bundle data = new Bundle();
                                                //利用Intent传递数据
                                                data.putInt("booknum",borrowBookNum);
                                                data.putInt("textNum",textNum1);
                                                data.putString("objectId",objectId);
                                                Intent intent = new Intent(mContext, Sharing.class);
                                                intent.putExtras(data);
                                                startActivity(intent);
                                                //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                                            } else {
                                                //Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                                                borrowBtn.setClickable(false);
                                            }
                                        }
                                    });
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

        loanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fold();
                Bundle data = new Bundle();
                //利用Intent传递数据
                data.putInt("booknum",loanBookNum);
                data.putInt("textNum",textNum2);
                data.putString("objectId",objectId);
                data.putInt("userNum",userNum);
                Intent intent = new Intent(mContext, Sharing.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fold();
                Bundle data = new Bundle();
                //利用Intent传递数据
                data.putInt("booknum",borrowBookNum);
                data.putInt("textNum",textNum1);
                data.putString("objectId",objectId);
                Intent intent = new Intent(mContext, Sharing.class);
                intent.putExtras(data);
                startActivity(intent);
                //Toast.makeText(mContext, 123123 , Toast.LENGTH_SHORT).show();
            }
        });

        receiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fold();
                Bundle data = new Bundle();
                //利用Intent传递数据
                data.putInt("booknum",loanBookNum);
                data.putInt("textNum",textNum3);
                data.putString("objectId",objectId);
                Intent intent = new Intent(mContext, Sharing.class);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        handleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fold();
                Intent it = new Intent(mContext,HandleBook.class);
                startActivity(it);
            }
        });

        assert shortCut != null;
        shortCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked ) {
                    unfold();
                } else {
                    fold();
                }
            }
        });

        assert searchImg != null;
        searchImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked ) {
                    fold();
                }
                Toast.makeText(mContext, "过两天就能用了！！！", Toast.LENGTH_SHORT).show();
            }
        });

        assert menuImg != null;
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked ) {
                    fold();
                }
                Intent intent = new Intent(mContext, Menu_page.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                //overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
            }
        });

    }
    //折叠按钮
    private void fold() {
        shareBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        returnBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        loanBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        borrowBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        receiveBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        handleBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);

        displayList();
        animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_return);
        animation.setFillAfter(true);
        shortCut.startAnimation(animation);
        clicked = !clicked;
    }
    //展开按钮
    private void unfold() {
        shareBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        returnBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        loanBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        borrowBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        receiveBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        handleBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        loanBtn.setClickable(false);
        borrowBtn.setClickable(false);
        receiveBtn.setClickable(false);

        returnBtn.setTextColor(BLUE);
        shareBtn.setTextColor(BLUE);
        loanBtn.setTextColor(BLACK);
        borrowBtn.setTextColor(BLACK);
        receiveBtn.setTextColor(BLACK);

        queryShareInfo();

        animation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_add);
        animation.setFillAfter(true);
        shortCut.startAnimation(animation);
        clicked = !clicked;
    }
    //查询ShareInfo
    private void queryShareInfo() {

        final ProgressDialog progress = new ProgressDialog(mContext);
        progress.setMessage("正在查询信息...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        //查询是否有书主为自己的借书信息
        BmobQuery<Shared_Info> query = new BmobQuery<>();
        query.addWhereEqualTo("ownerName", bmobUser.getUsername());
        query.addWhereEqualTo("ifAffirm", false);
        query.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()!=0) {
                    //选择书
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    final String[] booklist=new String[list.size()];
                    for (int i=0;i<list.size();i++) {
                        booklist[i]="借出第" + (i + 1) + "本书";
                    }
                    builder.setSingleChoiceItems(booklist, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            loanBookNum = list.get(i).getBookNum();
                            if (!list.get(i).getIfAgree() && !list.get(i).getIfLoan() && !list.get(i).getIfFinish() && !list.get(i).getIfAffirm() && !list.get(i).getIfReturn()) {
                                //书主处理借书请求
                                textNum2 = 2;
                                loanBtn.setClickable(true);
                                loanBtn.setTextColor(BLUE);
                            } else if (list.get(i).getIfAgree() && !list.get(i).getIfLoan() && !list.get(i).getIfFinish() && !list.get(i).getIfAffirm() && !list.get(i).getIfReturn()) {
                                //书主确认书已借出
                                textNum2 = 4;
                                loanBtn.setClickable(true);
                                loanBtn.setTextColor(BLUE);
                            } else if (list.get(i).getIfAgree() && list.get(i).getIfLoan() && list.get(i).getIfFinish() && !list.get(i).getIfAffirm() && !list.get(i).getIfReturn()) {
                                textNum3 = 6;
                                receiveBtn.setClickable(true);
                                receiveBtn.setTextColor(BLUE);
                            }
                            objectId = list.get(i).getObjectId();
                            userNum = list.get(i).getUserNum();
                            //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    //Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                    borrowBtn.setTextColor(BLACK);
                }
            }
        });

        //查询是否有借书人为自己的借书信息
        BmobQuery<Shared_Info> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("UserNum", bmobUser.getUserNum());
        query1.addWhereEqualTo("ifReturn", false);
        query1.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()!=0) {
                    borrowBookNum = list.get(0).getBookNum();
                    if (!list.get(0).getIfAgree() && !list.get(0).getIfLoan() && !list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //借书人刚发送借书请求
                        textNum1 = 1;
                        borrowBtn.setClickable(true);
                        borrowBtn.setTextColor(BLUE);
                    } else if (list.get(0).getIfAgree() && !list.get(0).getIfLoan() && !list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //书主同意了借书人的请求
                        textNum1 = 3;
                        borrowBtn.setClickable(true);
                        borrowBtn.setTextColor(BLUE);
                    } else if (list.get(0).getIfAgree() && list.get(0).getIfLoan() && !list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //书主已借出书
                        textNum1 = 5;
                        borrowBtn.setClickable(true);
                        borrowBtn.setTextColor(BLUE);
                    }else if (list.get(0).getIfAgree() && list.get(0).getIfLoan() && list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //借书过程完成
                        time = list.get(0).getFinishAt().getDate();
                        borrowBtn.setTextColor(BLACK);
                    }
                    objectId = list.get(0).getObjectId();
                    progress.dismiss();
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    progress.dismiss();
                    //Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //显示列表
    private void displayList() {
        //查找book
        BmobQuery<Book_Info> query = new BmobQuery<>();
        //查找出有ownerNum的信息
        query.addWhereEqualTo("BeShared", false);
        //列表中不显示自己分享的书
        /*_User bmobUser = BmobUser.getCurrentUser(_User.class);
        String username = bmobUser.getUsername();
        query.addWhereNotEqualTo("ownerName", username);*/
        query.setLimit(50);
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    //Toast.makeText(mContext, "查询成功：共" + list.size() + "条数据。", Toast.LENGTH_SHORT).show();
                    final int[] bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    listview.setAdapter(new MyAdapter(mContext,list));

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (clicked ) {
                                fold();
                            }
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
    //两次点击突出应用
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
