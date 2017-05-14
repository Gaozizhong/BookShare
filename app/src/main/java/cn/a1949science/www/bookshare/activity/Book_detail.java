package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Like_Book;
import cn.a1949science.www.bookshare.bean.Read_Book;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean.SharingBook;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.color.black;

public class Book_detail extends AppCompatActivity {
    Context mContext = Book_detail.this;
    ImageView image;
    TextView introduce,bookName,writename,bookPress,publishedDate,ISBN,time,bookOwner;
    ImageButton likeBtn,readBtn;
    String objectId,introduce1,bookname1,writername1,bookPress1,publishedDate1,ISBN1,OwnerName1,time1;
    int booknum1,shareNum,OwnerNum;
    boolean ifLike=false,ifRead=false;
    Button borrowBtn;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);

        findView();
        onClick();
        detail();

    }

    private void detail() {
        Bundle bundle = this.getIntent().getExtras();
        shareNum = bundle.getInt("shareNum");
        booknum1 = bundle.getInt("booknum");
        objectId = bundle.getString("objectId");
        //显示图书信息
        BmobQuery<BookInfo> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<BookInfo>() {
            @Override
            public void done(BookInfo BookInfo, BmobException e) {
                if (e == null) {
                    introduce1 = BookInfo.getIntroduction();
                    bookname1 = BookInfo.getBookName();
                    writername1 = BookInfo.getBookWriter();
                    bookPress1 = BookInfo.getBookPress();
                    publishedDate1 = BookInfo.getPublishedDate();
                    ISBN1 = BookInfo.getISBN();
                    introduce.setText(introduce1);
                    bookName.setText(bookname1);
                    writename.setText(writername1);
                    bookPress.setText(bookPress1);
                    publishedDate.setText(publishedDate1);
                    ISBN.setText(ISBN1);
                    Glide.with(mContext)
                            .load(BookInfo.getBookImage().getFileUrl())
                            .placeholder(R.drawable.wait)
                            .into(image);
                } else {
                    Toast.makeText(Book_detail.this, "查询失败。"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //显示借书信息
        BmobQuery<SharingBook> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("shareNum", shareNum);
        query1.findObjects(new FindListener<SharingBook>() {
            @Override
            public void done(List<SharingBook> list, BmobException e) {
                if (e == null) {
                    time1 = list.get(0).getkeepTime().toString();
                    OwnerNum = list.get(0).getOwnerNum();
                    time.setText(time1+"天");
                    bookOwner.setText(String.valueOf(OwnerNum));
                    if (list.get(0).getBeShared()) {
                        borrowBtn.setText("已被借出");
                        borrowBtn.setClickable(false);
                        borrowBtn.setBackgroundColor(getResources().getColor(black));
                    }
                }else {
                    Toast.makeText(Book_detail.this, "查询失败1。"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //判断是否喜欢
        BmobQuery<Like_Book> likeQuery = new BmobQuery<>();
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        likeQuery.addWhereEqualTo("userNum", bmobUser.getUserNum());
        likeQuery.addWhereEqualTo("BookNum", booknum1);
        likeQuery.findObjects(new FindListener<Like_Book>() {
            @Override
            public void done(List<Like_Book> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        ifLike = true;
                        likeBtn.setImageResource(R.mipmap.my_favourite);
                    }
                } else {
                    Toast.makeText(mContext, "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //判断是否读过
        BmobQuery<Read_Book> readQuery = new BmobQuery<>();
        readQuery.addWhereEqualTo("userNum", bmobUser.getUserNum());
        readQuery.addWhereEqualTo("BookNum", booknum1);
        readQuery.findObjects(new FindListener<Read_Book>() {
            @Override
            public void done(List<Read_Book> list, BmobException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        ifRead = true;
                        readBtn.setImageResource(R.mipmap.seen);
                    }
                } else {
                    Toast.makeText(mContext, "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查找地址
    private void findView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        image = (ImageView) findViewById(R.id.image);
        introduce = (TextView) findViewById(R.id.introduce);
        bookName = (TextView) findViewById(R.id.bookName);
        writename = (TextView) findViewById(R.id.writename);
        bookPress = (TextView) findViewById(R.id.publishedDate);
        publishedDate = (TextView) findViewById(R.id.bookPress);
        ISBN = (TextView) findViewById(R.id.ISBN);
        time = (TextView) findViewById(R.id.time);
        bookOwner = (TextView) findViewById(R.id.bookOwner);
        likeBtn = (ImageButton)findViewById(R.id.likeBtn);
        readBtn = (ImageButton) findViewById(R.id.readBtn);
        borrowBtn = (Button) findViewById(R.id.borrowBtn);
    }

    //点击事件
    private void onClick(){

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                if (!ifLike) {
                    likeBtn.setImageResource(R.mipmap.my_favourite);
                    //添加喜欢
                    Like_Book like_book = new Like_Book();
                    like_book.setBookNum(booknum1);
                    like_book.setUserNum(bmobUser.getUserNum());
                    like_book.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                likeBtn.setClickable(false);
                                Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    ifLike = true;
                } else {
                    Toast.makeText(mContext, "请到菜单中编辑所有喜爱书籍", Toast.LENGTH_SHORT).show();
                }
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ifRead) {
                    readBtn.setImageResource(R.mipmap.seen);
                    //添加喜欢
                    _User bmobUser = BmobUser.getCurrentUser(_User.class);
                    Read_Book read_book = new Read_Book();
                    read_book.setBookNum(booknum1);
                    read_book.setUserNum(bmobUser.getUserNum());
                    read_book.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                readBtn.setClickable(false);
                                Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    ifRead = true;
                } else {
                    Toast.makeText(mContext, "请到菜单中编辑所有看过书籍", Toast.LENGTH_SHORT).show();
                }
            }
        });

        borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _User bmobUser = BmobUser.getCurrentUser(_User.class);

                if (bmobUser.getCertificationOk()) {
                    if (!bmobUser.getNeedReturn()) {
                        AlertDialog dlg = new AlertDialog.Builder(mContext)
                                .setTitle("确认借此书？")
                                .setMessage("确定后请关注书主同意情况，若同意，请电话联系书主。")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final ProgressDialog progress = new ProgressDialog(mContext);
                                        progress.setMessage("操作中...");
                                        progress.setCanceledOnTouchOutside(false);
                                        progress.show();
                                        //更新此书的状态
                                        Book_Info newBook = new Book_Info();
                                        newBook.setBeShared(true);
                                        newBook.update(objectId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    //Toast.makeText(mContext, "更新信息成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(mContext, "更新信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        //更新用户借书状态
                                        _User newUser = new _User();
                                        newUser.setNeedReturn(true);
                                        BmobUser bmobUser = BmobUser.getCurrentUser();
                                        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    //Toast.makeText(mContext, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(mContext, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        Shared_Info sharedInfo = new Shared_Info();
                                        _User bmobUser1 = BmobUser.getCurrentUser(_User.class);
                                        Integer userNum = bmobUser1.getUserNum();
                                        sharedInfo.setBookNum(booknum1);
                                        sharedInfo.setUserNum(userNum);
                                        sharedInfo.setOwnerName(OwnerName1);
                                        sharedInfo.setIfAgree(false);
                                        sharedInfo.setIfLoan(false);
                                        sharedInfo.setIfFinish(false);
                                        sharedInfo.setIfAffirm(false);
                                        sharedInfo.setIfReturn(false);
                                        sharedInfo.setIfRefuse(false);
                                        sharedInfo.save(new SaveListener<String>() {
                                            @Override
                                            public void done(String s, BmobException e) {
                                                if (e == null) {
                                                    progress.dismiss();
                                                    borrowBtn.setText("等待书主响应");
                                                    borrowBtn.setClickable(false);
                                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                    //Toast.makeText(mContext, "借书信息创建成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(mContext, "借书信息创建失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                        ifNeedReturn();

                                    }
                                })
                                .create();
                        dlg.show();
                    } else {
                        AlertDialog dlg = new AlertDialog.Builder(mContext)
                                .setTitle("你已经借过一本书了，请看完、归还后再借书吧！(可以点击左下角喜欢按钮，以便下次更快借书)")
                                .create();
                        dlg.show();
                    }
                } else{
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("请进行实名认证")
                            .setMessage("请进行实名认证")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("去认证", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent it = new Intent(mContext, Identification.class);
                                    startActivity(it);
                                    overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
                                }
                            })
                            .create();
                    dlg.show();
                }

            }
        });


    }

    //看自己是否可以借书
    private void ifNeedReturn() {
        final _User bmobUser = BmobUser.getCurrentUser(_User.class);

        //查询是否有借书人为自己的借书信息
        BmobQuery<Shared_Info> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("UserNum", bmobUser.getUserNum());
        query1.addWhereEqualTo("ifReturn", false);
        query1.addWhereEqualTo("ifRefuse", false);
        query1.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()==0) {
                    //更新用户借书状态
                    _User newUser = new _User();
                    newUser.setNeedReturn(false);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e1) {
                            if (e1 == null) {
                                Toast.makeText(mContext, "修改状态成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "修改状态失败:" + e1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (e == null && list.size()!=0){
                    //更新用户借书状态
                    _User newUser = new _User();
                    newUser.setNeedReturn(true);
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e1) {
                            if (e1 == null) {
                                Toast.makeText(mContext, "修改状态成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "修改状态失败:" + e1.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

}
