package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.color.black;

public class Book_detail extends AppCompatActivity {

    Context mContext = Book_detail.this;
    ImageView before,image;
    TextView introduce,bookName,writename,time,bookOwner;
    ImageButton likeBtn,readBtn;
    String objectId,introduce1,bookname1,writername1,OwnerName1,time1;
    int booknum1,OwnerNum1;
    boolean ifLike=false,ifRead=false;
    Button borrowBtn;
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
        booknum1 = bundle.getInt("booknum");
        objectId = bundle.getString("objectId");
        final BmobQuery<Book_Info> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<Book_Info>() {
            @Override
            public void done(Book_Info book_info, BmobException e) {
                if (e == null) {
                    introduce1 = book_info.getBookDescribe();
                    bookname1 = book_info.getBookName();
                    writername1 = book_info.getBookWriter();
                    time1 = book_info.getkeepTime().toString();
                    OwnerName1 = book_info.getOwnerName();
                    introduce.setText(introduce1);
                    bookName.setText(bookname1);
                    writename.setText(writername1);
                    time.setText(time1);
                    bookOwner.setText(OwnerName1);
                } else {
                    Toast.makeText(Book_detail.this, "查询失败。"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        image = (ImageView) findViewById(R.id.image);
        introduce = (TextView) findViewById(R.id.introduce);
        bookName = (TextView) findViewById(R.id.bookName);
        writename = (TextView) findViewById(R.id.writename);
        time = (TextView) findViewById(R.id.time);
        bookOwner = (TextView) findViewById(R.id.bookOwner);
        likeBtn = (ImageButton)findViewById(R.id.likeBtn);
        readBtn = (ImageButton) findViewById(R.id.readBtn);
        borrowBtn = (Button) findViewById(R.id.borrowBtn);
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

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ifLike == false) {
                    likeBtn.setImageResource(R.mipmap.my_favourite);
                    ifLike = true;
                } else {
                    likeBtn.setImageResource(R.mipmap.favourite);
                    ifLike = false;
                }
            }
        });

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ifRead == false) {
                    readBtn.setImageResource(R.mipmap.seen);
                    ifRead = true;
                } else {
                    readBtn.setImageResource(R.mipmap.not_seen);
                    ifRead = false;
                }
            }
        });

        borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                if (!bmobUser.getNeedReturn()) {
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("确认借此书？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    borrowBtn.setText("等待书主响应");
                                    borrowBtn.setClickable(false);
                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
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

                                    //查找书主序号
                                    BmobQuery<_User> query = new BmobQuery<>();
                                    //查找出有ownerNum的信息
                                    query.addWhereEqualTo("username", bookOwner);
                                    query.findObjects(new FindListener<_User>() {
                                        @Override
                                        public void done(final List<_User> list, BmobException e) {
                                            if (e == null) {
                                                OwnerNum1=list.get(0).getUserNum();
                                                //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    sharedInfo.setOwnerNum(OwnerNum1);
                                    sharedInfo.setIfAgree(false);
                                    sharedInfo.setIfLoan(false);
                                    sharedInfo.setIfFinish(false);
                                    sharedInfo.setIfReturn(false);
                                    sharedInfo.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                //Toast.makeText(mContext, "借书信息创建成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "借书信息创建失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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


            }
        });

    }

}
