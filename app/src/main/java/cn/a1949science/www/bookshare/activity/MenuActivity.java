package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.a1949science.www.bookshare.MyApplication;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.adapter.myAdapterRecyclerView;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean.SharingBook;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.widget.CircleImageView;
import cn.a1949science.www.bookshare.widget.GlideImageLoader;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;

/**
 * Created by 高子忠 on 2017/3/22.
 */

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    Context mContext = MenuActivity.this;
    Toolbar toolbar;
    DrawerLayout drawer;
    Button borrowBtn,loanBtn,shareBtn,returnBtn,receiveBtn;
    Animation animation = null;
    ImageButton shortCut,RedPoint;
    CircleImageView favicon;
    TextView nickname;
    boolean clicked  = false,ifReturn = false;
    private long exitTime = 0;
    Integer borrowBookNum,loanBookNum,textNum1,textNum2,textNum3,userNum,userNum1,shareNum,shareNum1;
    String objectId,objectId1,time;
    View mine,headerLayout;
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    private int number_of_pages=1;
    List<BookInfo> bookInfoList= null;
    myAdapterRecyclerView adapter;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem ;
    Integer[] bookNums,shareNums;
    int REQUEST_CODE = 5;
    EditText bookName,bookWriter;
    private Integer bookNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*XiaomiUpdateAgent.setCheckUpdateOnlyWifi(true);
        XiaomiUpdateAgent.setUpdateAutoPopup(false);
        XiaomiUpdateAgent.setUpdateListener(new XiaomiUpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, com.xiaomi.market.sdk.UpdateResponse updateInfo) {
                switch (updateStatus) {
                    case com.xiaomi.market.sdk.UpdateStatus.STATUS_UPDATE:
                        // 有更新， UpdateResponse为本次更新的详细信息
                        // 其中包含更新信息，下载地址，MD5校验信息等，可自行处理下载安装
                        // 如果希望 SDK继续接管下载安装事宜，可调用
                        XiaomiUpdateAgent.arrange();
                        break;
                    case com.xiaomi.market.sdk.UpdateStatus.STATUS_NO_UPDATE:
                        // 无更新， UpdateResponse为null
                        break;
                    case com.xiaomi.market.sdk.UpdateStatus.STATUS_NO_WIFI:
                        // 设置了只在WiFi下更新，且WiFi不可用时， UpdateResponse为null
                        break;
                    case com.xiaomi.market.sdk.UpdateStatus.STATUS_NO_NET:
                        // 没有网络， UpdateResponse为null
                        break;
                    case com.xiaomi.market.sdk.UpdateStatus.STATUS_FAILED:
                        // 检查更新与服务器通讯失败，可稍后再试， UpdateResponse为null
                        break;
                    case com.xiaomi.market.sdk.UpdateStatus.STATUS_LOCAL_APP_FAILED:
                        // 检查更新获取本地安装应用信息失败， UpdateResponse为null
                        break;
                    default:
                        break;
                }
            }
        });
        XiaomiUpdateAgent.update(this);*/
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "13d736220ecc496d7dcb63c7cf918ba7");
        MyApplication.setMenuActivity(this);
        //设置别名，撤销别名（alias）
        //MiPushClient.setAlias(MenuActivity.this, "demo1", null);
        //MiPushClient.unsetAlias(MainActivity.this, "demo1", null);
        //设置账号，撤销账号（account）
        //MiPushClient.setUserAccount(MenuActivity.this, "user1", null);
        //MiPushClient.unsetUserAccount(MainActivity.this, "user1", null);
        //设置标签，撤销标签（topic：话题、主题）
        //MiPushClient.subscribe(MenuActivity.this, "IT", null);
        //MiPushClient.unsubscribe(MainActivity.this, "IT", null);
        //设置接收时间（startHour, startMin, endHour, endMin）
        //MiPushClient.setAcceptTime(MenuActivity.this, 7, 0, 23, 0, null);
        //暂停和恢复推送 //MiPushClient.pausePush(MainActivity.this, null);
        //MiPushClient.resumePush(MainActivity.this, null);
        ZXingLibrary.initDisplayOpinion(this);
        findView();
        setListener();
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
            //BmobUpdateAgent.forceUpdate(mContext);
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
        //下拉刷新
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        recyclerView = (RecyclerView) findViewById(R.id.booklist);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        RedPoint= (ImageButton) findViewById(R.id.RedPoint);
        shortCut = (ImageButton) findViewById(R.id.shortcut);
        shortCut.setOnClickListener(this);
        borrowBtn = (Button) findViewById(R.id.borrowBtn);
        borrowBtn.setOnClickListener(this);
        loanBtn = (Button) findViewById(R.id.loanBtn);
        loanBtn.setOnClickListener(this);
        shareBtn = (Button) findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);
        returnBtn = (Button) findViewById(R.id.returnBtn);
        returnBtn.setOnClickListener(this);
        receiveBtn = (Button) findViewById(R.id.receiveBtn);
        receiveBtn.setOnClickListener(this);
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
                    new MoreBookList(mContext).execute();
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

    private class MoreBookList extends AsyncTask<Void, Integer, Integer> {
        private Context context;

        MoreBookList(Context context) {
            this.context = context;
        }
        //运行在UI线程中，在调用doInBackground()之前执行
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            refresh.setRefreshing(true);
        }
        //后台运行的方法，可以运行非UI线程，可以执行耗时的方法
        @Override
        protected Integer doInBackground(Void... voids) {

            return null;
        }
        //运行在ui线程中，在doInBackground()执行完毕后执行
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //查找book
            BmobQuery<SharingBook> query = new BmobQuery<>();
            //列表中不显示自己分享的书
            _User bmobUser = BmobUser.getCurrentUser(_User.class);
            Integer userNum = bmobUser.getUserNum();
            query.addWhereNotEqualTo("ownerNum", userNum);
            query.order("-createdAt");
            query.findObjects(new FindListener<SharingBook>() {
                @Override
                public void done(final List<SharingBook> list, BmobException e) {
                    if (e == null) {
                        bookNums = new Integer[list.size()];
                        for (int i=0;i<list.size();i++) {
                            bookNums[i] = list.get(i).getBookNum();
                        }
                        BmobQuery<BookInfo> query1 = new BmobQuery<>();
                        query1.addWhereContainedIn("bookNum", Arrays.asList(bookNums));
                        query1.order("-createdAt");
                        query1.setSkip(10 * number_of_pages);
                        query1.setLimit(10);
                        number_of_pages=number_of_pages+1;
                        query1.findObjects(new FindListener<BookInfo>() {
                            @Override
                            public void done(List<BookInfo> list2, BmobException e) {
                                if (e == null && list2.size() != 0) {
                                    bookInfoList.addAll(list2);
                                    adapter.notifyDataSetChanged();
                                } else if (list.size() == 0) {
                                    Toast.makeText(context, "没有更多图书了。" , Toast.LENGTH_LONG).show();
                                } else if (e != null) {
                                    Toast.makeText(context, "查询失败2。" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(context, "查询失败1。"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            refresh.setRefreshing(false);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                final String result = bundle.getString(CodeUtils.RESULT_STRING);
                //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                //查询扫描的书
                BmobQuery<BookInfo> query = new BmobQuery<>();
                query.addWhereEqualTo("ISBN", result);
                query.findObjects(new FindListener<BookInfo>() {
                    @Override
                    public void done(List<BookInfo> list, BmobException e) {
                        if (e == null) {
                            bookNum = list.get(0).getBookNum();
                            bookName.setText(list.get(0).getBookName());
                            bookWriter.setText(list.get(0).getBookWriter());
                        } else {
                            //添加书籍信息
                            BookInfo bookInfo = new BookInfo();
                            bookInfo.setISBN(result);
                            bookInfo.save(new SaveListener<String>() {
                                @Override
                                public void done(String s, BmobException e) {
                                    if (e == null) {
                                        //再次查询扫描的书
                                        BmobQuery<BookInfo> query1 = new BmobQuery<>();
                                        query1.addWhereEqualTo("ISBN", result);
                                        query1.findObjects(new FindListener<BookInfo>() {
                                            @Override
                                            public void done(List<BookInfo> list, BmobException e) {
                                                if (e == null) {
                                                    bookNum = list.get(0).getBookNum();
                                                } else {
                                                    Toast.makeText(mContext, "失败1", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        Toast.makeText(mContext, "暂时没有这本书的详细信息，请手动填写书名和作者后，继续完成共享！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
            }
        }
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
        query.addWhereEqualTo("ownerNum", bmobUser.getUserNum());
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
                            shareNum = list.get(i).getSharingBookNum();
                            objectId = list.get(i).getObjectId();
                            userNum1 = list.get(i).getUserNum();
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
        userNum = bmobUser.getUserNum();
        query1.addWhereEqualTo("UserNum", userNum);
        query1.addWhereEqualTo("ifReturn", false);
        query1.addWhereEqualTo("ifRefuse", false);
        query1.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()!=0) {
                    borrowBookNum = list.get(0).getBookNum();
                    shareNum1 = list.get(0).getSharingBookNum();
                    objectId1 = list.get(0).getObjectId();
                    //Toast.makeText(mContext, objectId1, Toast.LENGTH_SHORT).show();
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
                        borrowBtn.setTextColor(BLACK);
                    }
                    time = list.get(0).getFinishAt().getDate();
                    progress.dismiss();
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
                    query2.addWhereEqualTo("ownerNum", bmobUser2.getUserNum());
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
        BmobQuery<SharingBook> query = new BmobQuery<>();
        //列表中不显示自己分享的书
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        Integer userNum = bmobUser.getUserNum();
        query.addWhereNotEqualTo("ownerNum", userNum);
        query.order("-createdAt");
        query.findObjects(new FindListener<SharingBook>() {
            @Override
            public void done(List<SharingBook> list, BmobException e) {
                if (e == null) {
                    shareNums = new Integer[list.size()];
                    bookNums = new Integer[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNums[i] = list.get(i).getBookNum();
                        shareNums[i] = list.get(i).getShareNum();
                    }
                    BmobQuery<BookInfo> query1 = new BmobQuery<>();
                    query1.addWhereContainedIn("bookNum", Arrays.asList(bookNums));
                    query1.order("-createdAt");
                    query1.setLimit(10);
                    query1.findObjects(new FindListener<BookInfo>() {
                        @Override
                        public void done(List<BookInfo> list2, BmobException e) {
                            if (e == null) {
                                bookInfoList = list2;
                                adapter = new myAdapterRecyclerView(mContext, bookInfoList,bookNums,shareNums);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(mContext, "查询失败2。"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(mContext, "查询失败1。"+e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.setMenuActivity(null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareBtn:
                shareBook();
                break;
            case R.id.returnBtn:
                returnBook();
                break;
            case R.id.loanBtn:
                loanBook();
                break;
            case R.id.borrowBtn:
                borrowBook();
                break;
            case R.id.receiveBtn:
                receiveBook();
                break;
            case R.id.shortcut:
                shortBtn();
                break;
        }
    }

    private void shortBtn() {
        if (!clicked ) {
            unfold();
        } else {
            fold();
        }
    }

    //收回图书
    private void receiveBook() {
        fold();
        Bundle data = new Bundle();
        //利用Intent传递数据
        data.putInt("textNum",textNum3);
        data.putInt("shareNum",shareNum);
        data.putInt("booknum",loanBookNum);
        data.putInt("userNum",userNum1);
        data.putString("objectId",objectId);
        Intent intent = new Intent(mContext, Book_detail.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    //借入图书
    private void borrowBook() {
        fold();
        Bundle data = new Bundle();
        //利用Intent传递数据
        data.putInt("textNum",textNum1);
        data.putInt("shareNum",shareNum1);
        data.putInt("booknum",borrowBookNum);
        data.putInt("userNum",userNum);
        data.putString("objectId",objectId1);
        Intent intent = new Intent(mContext, Book_detail.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    //借出图书
    private void loanBook() {
        fold();
        Bundle data = new Bundle();
        //利用Intent传递数据
        data.putInt("textNum",textNum2);
        data.putInt("shareNum",shareNum);
        data.putInt("booknum",loanBookNum);
        data.putInt("userNum",userNum);
        data.putString("objectId",objectId);
        Intent intent = new Intent(mContext, Book_detail.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    //归还图书
    private void returnBook() {
        fold();
        //弹出信息框
        LayoutInflater inflater = getLayoutInflater();
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        if (bmobUser.getNeedReturn()){
            View layout = inflater.inflate(R.layout.raturning_yes, (ViewGroup) findViewById(R.id.returning_Yes_Dialog));
            TextView returnTime = (TextView) layout.findViewById(R.id.returnTime);
            if (time == null) {
                returnTime.setText("还未完成借书");
            } else {
                returnTime.setText("截止时间："+ time);
                new AlertDialog.Builder(mContext)
                        .setPositiveButton("确认还书", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                returnQuery();
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

    //归还时的查询
    private void returnQuery() {
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        //查询是否有借书人为自己的借书信息
        BmobQuery<Shared_Info> query = new BmobQuery<>();
        query.addWhereEqualTo("UserNum", bmobUser.getUserNum());
        query.addWhereEqualTo("ifReturn", ifReturn);
        query.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(List<Shared_Info> list, BmobException e) {
                if (e == null && list.size() != 0) {
                    borrowBookNum = list.get(0).getBookNum();
                    objectId = list.get(0).getObjectId();
                    shareNum = list.get(0).getSharingBookNum();
                    if (!list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        textNum1 = 3;
                    } else if (list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        textNum1 = 7;
                    }
                    Bundle data = new Bundle();
                    //利用Intent传递数据
                    data.putInt("textNum",textNum1);
                    data.putInt("shareNum",shareNum);
                    data.putInt("booknum",borrowBookNum);
                    data.putString("objectId",objectId);
                    Intent intent = new Intent(mContext, Book_detail.class);
                    intent.putExtras(data);
                    startActivity(intent);
                } else {
                    assert e != null;
                    Toast.makeText(mContext, "查询失败。"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //共享图书
    private void shareBook() {
        fold();
        //弹出信息框
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.sharing, (ViewGroup) findViewById(R.id.sharing_Dialog));

        bookName = (EditText) layout.findViewById(R.id.bookName);
        bookWriter = (EditText) layout.findViewById(R.id.bookWriter);
        final EditText shareTime = (EditText) layout.findViewById(R.id.shareTime);
        final Button bookPicture = (Button) layout.findViewById(R.id.book);
        //为上传图片按钮设置点击事件
        bookPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(it, REQUEST_CODE);
            }
        });

        new AlertDialog.Builder(mContext)
                .setTitle("图书共享")
                .setPositiveButton("确认共享", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (bookName.getText().toString().equals("") || bookWriter.getText().toString() .equals("") || shareTime.getText().toString() .equals("")) {
                            Toast.makeText(mContext, "信息不全！！！", Toast.LENGTH_SHORT).show();
                        } else {
                            new AlertDialog.Builder(mContext)
                                    .setMessage("共享此书？")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //等待框
                                            final ProgressDialog progressDialog = new ProgressDialog(mContext);
                                            progressDialog.setMessage("正在上传...");
                                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            progressDialog.setCanceledOnTouchOutside(false);
                                            progressDialog.show();

                                            //上传其他信息
                                            _User bmobUser = BmobUser.getCurrentUser(_User.class);
                                            Integer userNum = bmobUser.getUserNum();
                                            SharingBook sharingBook = new SharingBook();
                                            sharingBook.setOwnerNum(userNum);
                                            sharingBook.setBookNum(bookNum);
                                            sharingBook.setkeepTime(Integer.valueOf(shareTime.getText().toString()));
                                            sharingBook.setBeSharing(false);
                                            sharingBook.save(new SaveListener<String>() {
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


}
