package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static android.R.color.black;

public class Sharing extends AppCompatActivity {
    Context mContext = Sharing.this;
    ImageView image;
    TextView introduce,bookName,writename,time,bookOwner;
    ImageButton likeBtn,readBtn;
    String objectId,objectId1,introduce1,bookname1,writername1,OwnerName1,time1,phone;
    int booknum1,textNum,userNum;
    boolean ifLike=false,ifRead=false;
    Button borrowBtn;
    Toolbar toolbar;
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
        userNum = bundle.getInt("userNum");
        if (textNum == 1) {
            borrowBtn.setText("等待书主响应");
            borrowBtn.setClickable(false);
            borrowBtn.setBackgroundColor(getResources().getColor(black));
        } else if (textNum == 2) {
            //借书人信息查询
            BmobQuery<_User> query = new BmobQuery<>();
            query.addWhereEqualTo("userNum", userNum);
            query.findObjects(new FindListener<_User>() {
                @Override
                public void done(List<_User> list, BmobException e) {
                    if (e == null) {
                        AlertDialog dlg = new AlertDialog.Builder(mContext)
                                .setTitle("借书人信息")
                                .setMessage("借书人："+list.get(0).getUsername()+"\n"+"借书人电话："+list.get(0).getMobilePhoneNumber()+"\n"+
                                        "借书人地址："+list.get(0).getUserSchool()+list.get(0).getUserDorm())
                                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        refuseShare();
                                    }
                                })
                                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .create();
                        dlg.show();
                    } else {
                        Toast.makeText(mContext, "借书人信息查询失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
                                    final ProgressDialog progress = new ProgressDialog(mContext);
                                    progress.setMessage("操作中...");
                                    progress.setCanceledOnTouchOutside(false);
                                    progress.show();
                                    //完善借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfAgree(true);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                progress.dismiss();
                                                borrowBtn.setText("等待借书电话");
                                                borrowBtn.setClickable(false);
                                                borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                //Toast.makeText(mContext, "可以借出", Toast.LENGTH_SHORT).show();
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
                                Intent it = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                                startActivity(it);
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
                            .setMessage("请确认借书人点击了“完成借入”按钮以保证图书的安全")
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
                                    //完成借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfLoan(true);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                progress.dismiss();
                                                borrowBtn.setClickable(false);
                                                borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                //Toast.makeText(mContext, "书已借出", Toast.LENGTH_SHORT).show();
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
                                    final ProgressDialog progress = new ProgressDialog(mContext);
                                    progress.setMessage("操作中...");
                                    progress.setCanceledOnTouchOutside(false);
                                    progress.show();
                                    //完成借书过程
                                    Shared_Info sharedInfo = new Shared_Info();
                                    sharedInfo.setIfFinish(true);
                                    Date date = new Date(new Date().getTime() +Integer.parseInt(time1) * 24 * 60 * 60 * 1000);
                                    BmobDate now = new BmobDate(date);
                                    BmobDate now1 = BmobDate.createBmobDate("yyyy-MM-dd HH:mm:ss", now.getDate());
                                    sharedInfo.setFinishAt(now1);
                                    sharedInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                progress.dismiss();
                                                borrowBtn.setClickable(false);
                                                borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                //Toast.makeText(mContext, "完成借入", Toast.LENGTH_SHORT).show();
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
            borrowBtn.setText("确认归还");
            borrowBtn.setClickable(true);
            borrowBtn.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 AlertDialog dlg = new AlertDialog.Builder(mContext)
                                                         .setTitle("确认归还？")
                                                         .setMessage("请确认书已归还")
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
                                                                 //完成借书过程
                                                                 Shared_Info sharedInfo = new Shared_Info();
                                                                 sharedInfo.setIfAffirm(true);
                                                                 sharedInfo.update(objectId, new UpdateListener() {
                                                                     @Override
                                                                     public void done(BmobException e) {
                                                                         if (e == null) {
                                                                             progress.dismiss();
                                                                             borrowBtn.setClickable(false);
                                                                             borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                                             //Toast.makeText(mContext, "确认还书", Toast.LENGTH_SHORT).show();
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
                                    final ProgressDialog progress = new ProgressDialog(mContext);
                                    progress.setMessage("操作中...");
                                    progress.setCanceledOnTouchOutside(false);
                                    progress.show();
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
                                                            borrowBtn.setClickable(false);
                                                            borrowBtn.setBackgroundColor(getResources().getColor(black));
                                                            //Toast.makeText(mContext, "还书成功", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(mContext, "还书失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                progress.dismiss();
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
                    time.setText(time1+"天");
                    bookOwner.setText(OwnerName1);
                    objectId1 = list.get(0).getObjectId();
                    Glide.with(mContext)
                            .load(list.get(0).getBookPicture().getFileUrl())
                            .placeholder(R.drawable.wait)
                            .into(image);
                } else {
                    Toast.makeText(mContext, "查询失败。"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //查找地址
    private void findView() {
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
        time = (TextView) findViewById(R.id.time);
        bookOwner = (TextView) findViewById(R.id.bookOwner);
        likeBtn = (ImageButton)findViewById(R.id.likeBtn);
        readBtn = (ImageButton) findViewById(R.id.readBtn);
        borrowBtn = (Button) findViewById(R.id.borrowBtn);
    }

    //点击事件
    private void onClick() {

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

    //拒绝借出图书
    private void refuseShare() {
        AlertDialog dlg = new AlertDialog.Builder(mContext)
                .setTitle("确认拒绝借出？")
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
                        //完善借书过程
                        Shared_Info sharedInfo = new Shared_Info();
                        sharedInfo.setIfRefuse(true);
                        sharedInfo.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    progress.dismiss();
                                    borrowBtn.setClickable(false);
                                    borrowBtn.setBackgroundColor(getResources().getColor(black));
                                } else {
                                    Toast.makeText(mContext, "可以借出失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
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
                    }
                })
                .create();
        dlg.show();
    }
}