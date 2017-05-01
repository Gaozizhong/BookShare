package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.adapter.myAdapterRecyclerView;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.widget.CircleImageView;
import cn.a1949science.www.bookshare.widget.GlideImageLoader;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;

/**
 * Created by 高子忠 on 2017/3/22.
 */

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Context mContext = MenuActivity.this;
    Toolbar toolbar;
    DrawerLayout drawer;
    Button borrowBtn,loanBtn,shareBtn,returnBtn,receiveBtn;
    Animation animation = null;
    ImageButton shortCut,RedPoint;
    CircleImageView favicon;
    TextView nickname;
    boolean clicked  = false;
    String picturePath="";
    private long exitTime = 0;
    Integer borrowBookNum,loanBookNum,textNum1,textNum2,textNum3,userNum;
    String objectId,time;
    View mine,headerLayout;
    BmobFile bmobFile;
    private com.lzy.imagepicker.ImagePicker imagePicker;
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    int number_of_pages=1;
    List<Book_Info> bookInfoList= null;
    myAdapterRecyclerView adapter;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
        bmobUpdate();
        findView();
        setListener();
        onClick();
        displayList();
        display();
        ifNeedReturn();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext,User_Info.class);
                startActivity(it);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
            }
        });
        nickname = (TextView) headerLayout.findViewById(R.id.nickname);
        favicon = (CircleImageView) headerLayout.findViewById(R.id.favicon);
    }

    //自动更新
    private void bmobUpdate() {
        //只在wifi下更新
        BmobUpdateAgent.setUpdateOnlyWifi(true);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                if (i == UpdateStatus.Yes) {
                    BmobUpdateAgent.update(mContext);
                }else if(i == UpdateStatus.No){
                    Toast.makeText(mContext, "版本无更新", Toast.LENGTH_SHORT).show();
                }else if(i==UpdateStatus.IGNORED){
                    Toast.makeText(mContext, "该版本已被忽略更新", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }//点击两次返回键退出程序
        else if(System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(mContext, SearchPage.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        }
        return super.onOptionsItemSelected(item);
    }
    //侧滑菜单中的点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            Intent it = new Intent(mContext,My_Book_List.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        } else if (id == R.id.read) {
            Intent it = new Intent(mContext,MyRead.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        } else if (id == R.id.like) {
            Intent it = new Intent(mContext,MyLike.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        } else if (id == R.id.advice) {
            Intent it = new Intent(mContext,Advice_Page.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        } else if (id == R.id.refrush) {
            BmobUpdateAgent.forceUpdate(mContext);
        } else if (id == R.id.quit) {
            AlertDialog dlg = new AlertDialog.Builder(mContext)
                    .setTitle("确认退出？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BmobUser.logOut();
                            Intent intent = new Intent(mContext, Login_Page.class);
                            //清空源来栈中的Activity，新建栈打开相应的Activity
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
                        }
                    })
                    .create();
            dlg.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //查找地址
    private void findView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());
        //下拉刷新
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        recyclerView = (RecyclerView) findViewById(R.id.booklist);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        RedPoint= (ImageButton) findViewById(R.id.RedPoint);
        shortCut = (ImageButton) findViewById(R.id.shortcut);
        borrowBtn = (Button) findViewById(R.id.borrowBtn);
        loanBtn = (Button) findViewById(R.id.loanBtn);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        returnBtn = (Button) findViewById(R.id.returnBtn);
        receiveBtn = (Button) findViewById(R.id.receiveBtn);
        mine = findViewById(R.id.mine);
    }

    private void setListener() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                number_of_pages = 1;
                refreshBookList();
            }
        });

        //recyclerview滚动监听来实现加载更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
                // 滑动状态停止并且剩余两个item时自动加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem +2>=mLayoutManager.getItemCount()) {
                    loadBookList();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取加载的最后一个可见视图在适配器的位置。
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    private void loadBookList() {
        refresh.setRefreshing(true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //查找book
                BmobQuery<Book_Info> query1 = new BmobQuery<>();
                //查找出有ownerNum的信息
                query1.addWhereEqualTo("BeShared", false);
                //列表中不显示自己分享的书
                _User bmobUser = BmobUser.getCurrentUser(_User.class);
                String username = bmobUser.getUsername();
                query1.addWhereNotEqualTo("ownerName", username);
                query1.setSkip(10 * number_of_pages);
                query1.setLimit(10);
                number_of_pages=number_of_pages+1;
                query1.findObjects(new FindListener<Book_Info>() {
                    @Override
                    public void done(final List<Book_Info> list, BmobException e) {
                        if (e == null) {
                            bookInfoList.addAll(list);
                        } else {
                            Toast.makeText(mContext, "查询失败。" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        refresh.setRefreshing(false);
                    }
                });
            }
        }.start();
    }

    //下拉刷新
    private void refreshBookList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        display();
                        displayList();
                        refresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
    //选择图书图片
    private void selectBookPicture() {
        imagePicker.setImageLoader(new GlideImageLoader());
        imagePicker.setMultiMode(false);
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
        Integer width = 200;
        Integer height = 300;
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
        height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, getResources().getDisplayMetrics());
        imagePicker.setFocusWidth(width);
        imagePicker.setFocusHeight(height);
        imagePicker.setOutPutX(800);
        imagePicker.setOutPutY(1200);

        Intent intent1 = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent1, 100);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                picturePath = images.get(0).path;
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //点击事件
    private void onClick()  {
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

                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            }).launch();


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


    }
    //折叠按钮
    private void fold() {
        shareBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        returnBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        loanBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        borrowBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);
        receiveBtn.setVisibility(!clicked ? View.VISIBLE : View.GONE);

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
        query.addWhereEqualTo("ifRefuse", false);
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
                    borrowBtn.setTextColor(BLACK);
                }
            }
        });

        //查询是否有借书人为自己的借书信息
        BmobQuery<Shared_Info> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("UserNum", bmobUser.getUserNum());
        query1.addWhereEqualTo("ifReturn", false);
        query1.addWhereEqualTo("ifRefuse", false);
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
    //显示头像、昵称、小红点
    private void display() {
        BmobUser bmobUser = BmobUser.getCurrentUser(_User.class);
        BmobQuery<_User> query = new BmobQuery<>();
        query.getObject(bmobUser.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null) {
                    nickname.setText(user.getNickname());
                    Glide.with(mContext)
                            .load(user.getFavicon().getFileUrl())
                            .override((int)(mContext.getResources().getDisplayMetrics().density*60+0.5f),(int)(mContext.getResources().getDisplayMetrics().density*60+0.5f))
                            .centerCrop()
                            .into(favicon);
                } else {
                    Toast.makeText(mContext, "请补充昵称、头像", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final _User bmobUser2 = BmobUser.getCurrentUser(_User.class);
        //查询是否有借书人为自己的借书信息
        BmobQuery<Shared_Info> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("UserNum", bmobUser2.getUserNum());
        query1.addWhereEqualTo("ifFinish", false);
        query1.addWhereEqualTo("ifRefuse", false);
        query1.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()!=0) {
                    RedPoint.setVisibility(View.VISIBLE);
                } else {
                    //查询是否有书主为自己的借书信息
                    BmobQuery<Shared_Info> query2 = new BmobQuery<>();
                    query2.addWhereEqualTo("ownerName", bmobUser2.getUsername());
                    query2.addWhereEqualTo("ifAgree", false);
                    query2.addWhereEqualTo("ifRefuse", false);
                    query2.findObjects(new FindListener<Shared_Info>() {
                        @Override
                        public void done(final List<Shared_Info> list, BmobException e) {
                            if (e == null && list.size()!=0) {
                                RedPoint.setVisibility(View.VISIBLE);
                            } else {
                                RedPoint.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });

    }
    //显示列表
    private void displayList() {
        //查找book
        BmobQuery<Book_Info> query1 = new BmobQuery<>();
        //查找出有ownerNum的信息
        query1.addWhereEqualTo("BeShared", false);
        //列表中不显示自己分享的书
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        String username = bmobUser.getUsername();
        query1.addWhereNotEqualTo("ownerName", username);
        query1.setLimit(10);
        query1.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    bookInfoList = list;
                    adapter = new myAdapterRecyclerView(mContext, bookInfoList);
                    recyclerView.setAdapter(adapter);

                } else {
                    Toast.makeText(mContext, "查询失败。"+e.getMessage(), Toast.LENGTH_LONG).show();
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
                                //Toast.makeText(mContext, "修改状态成功", Toast.LENGTH_SHORT).show();
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
                                //Toast.makeText(mContext, "修改状态成功", Toast.LENGTH_SHORT).show();
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
