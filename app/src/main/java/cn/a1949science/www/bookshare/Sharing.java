package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.color.black;

public class Sharing extends AppCompatActivity {

    Context mContext = Sharing.this;
    ImageView before,image,phoneBtn;
    TextView introduce,bookName,writename,time,bookOwner;
    ImageButton likeBtn,readBtn;
    String objectId,objectId1,introduce1,bookname1,writername1,OwnerName1,time1,phone;
    int booknum1,textNum;
    boolean ifLike=false,ifRead=false;
    Button borrowBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);

        findView();
        detail();
        onClick();
    }

    private void detail() {
        Bundle bundle = this.getIntent().getExtras();
        booknum1 = bundle.getInt("booknum");
        textNum = bundle.getInt("textNum");
        objectId = bundle.getString("objectId");
        if (textNum == 1) {
            borrowBtn.setText("等待书主响应");
            borrowBtn.setClickable(false);
            borrowBtn.setBackgroundColor(getResources().getColor(black));
        } else if (textNum == 2) {
            borrowBtn.setText("可以借出");
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("确认可以借出？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    borrowBtn.setText("等待借书电话");
                                    borrowBtn.setClickable(false);
                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
                                    //完成借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfAgree(true);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(mContext, "可以借出", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "可以借出失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .create();
                    dlg.show();
                }
            });
        } else if (textNum == 3) {
            borrowBtn.setText("联系书主");
            borrowBtn.setClickable(true);
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //查找owner
                    BmobQuery<_User> query = new BmobQuery<>();
                    query.addWhereEqualTo("username", OwnerName1);
                    //列表中不显示自己分享的书
                    query.findObjects(new FindListener<_User>() {
                        @Override
                        public void done(final List<_User> list, BmobException e) {
                            if (e == null) {
                                phone = list.get(0).getMobilePhoneNumber();

                                AlertDialog dlg = new AlertDialog.Builder(mContext)
                                        .setTitle("确认拨打电话？")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        })
                                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent it = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phone));
                                                startActivity(it);
                                            }
                                        })
                                        .create();
                                dlg.show();
                                //Toast.makeText(mContext, phone, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else if (textNum == 4) {
            borrowBtn.setText("书已借出");
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("确认已经借出？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    borrowBtn.setClickable(false);
                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
                                    //完成借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfLoan(true);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(mContext, "书已借出", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "书已借出失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .create();
                    dlg.show();
                }
            });
        } else if (textNum == 5) {
            borrowBtn.setText("完成借入");
            borrowBtn.setClickable(true);
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("确认完成借入？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    borrowBtn.setClickable(false);
                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
                                    //完成借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfFinish(true);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(mContext, "完成借入", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "完成借入失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .create();
                    dlg.show();
                }
            });
        } else if (textNum == 6) {
            borrowBtn.setText("确认还书");
            borrowBtn.setClickable(true);
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 AlertDialog dlg = new AlertDialog.Builder(mContext)
                                                         .setTitle("确认还书？")
                                                         .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {

                                                             }
                                                         })
                                                         .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                             @Override
                                                             public void onClick(DialogInterface dialogInterface, int i) {
                                                                 borrowBtn.setClickable(false);
                                                                 borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                                 //完成借书过程
                                                                 Shared_Info sharedInfo = new Shared_Info();
                                                                 sharedInfo.setIfAffirm(true);
                                                                 sharedInfo.update(objectId, new UpdateListener() {
                                                                     @Override
                                                                     public void done(BmobException e) {
                                                                         if (e == null) {
                                                                             Toast.makeText(mContext, "确认还书", Toast.LENGTH_SHORT).show();
                                                                         } else {
                                                                             Toast.makeText(mContext, "确认还书失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                         }
                                                                     }
                                                                 });
                                                             }
                                                         })
                                                         .create();
                                                 dlg.show();
                                             }
                                         });
        } else if (textNum == 7) {
            borrowBtn.setText("完成归还");
            borrowBtn.setClickable(true);
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("完成归还？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    borrowBtn.setClickable(false);
                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
                                    //完成借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfReturn(true);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                //更新此书的状态
                                                Book_Info newBook = new Book_Info();
                                                newBook.setBeShared(false);
                                                newBook.update(objectId1, new UpdateListener() {
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
                                                newUser.setNeedReturn(false);
                                                BmobUser bmobUser = BmobUser.getCurrentUser();
                                                newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            Toast.makeText(mContext, "还书成功", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(mContext, "还书失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                //Toast.makeText(mContext, "完成归还", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "完成归还失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .create();
                    dlg.show();
                }
            });
        }
        textNum = 0;
        final BmobQuery<Book_Info> query = new BmobQuery<>();
        query.addWhereEqualTo("BookNum",booknum1);
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(List<Book_Info> list, BmobException e) {
                if (e == null) {
                    introduce1 = list.get(0).getBookDescribe();
                    bookname1 = list.get(0).getBookName();
                    writername1 = list.get(0).getBookWriter();
                    time1 = list.get(0).getkeepTime().toString();
                    OwnerName1 = list.get(0).getOwnerName();
                    introduce.setText(introduce1);
                    bookName.setText(bookname1);
                    writename.setText(writername1);
                    time.setText(time1);
                    bookOwner.setText(OwnerName1);
                    objectId1 = list.get(0).getObjectId();
                } else {
                    Toast.makeText(mContext, "查询失败。"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查找地址
    private void findView() {
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
    private void onClick() {
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
                if (!ifLike) {
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
                if (!ifRead) {
                    readBtn.setImageResource(R.mipmap.seen);
                    ifRead = true;
                } else {
                    readBtn.setImageResource(R.mipmap.not_seen);
                    ifRead = false;
                }
            }
        });

    }
}
