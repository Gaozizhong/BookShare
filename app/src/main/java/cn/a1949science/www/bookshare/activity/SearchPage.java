package cn.a1949science.www.bookshare.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import com.lzy.imagepicker.ui.ImageGridActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.Arrays;
import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.adapter.myAdapterRecyclerView;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.SharingBook;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;

public class SearchPage extends AppCompatActivity {
    Context mContext = SearchPage.this;
    Toolbar toolbar;
    SearchView search;
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    List<BookInfo> bookInfoList= null;
    myAdapterRecyclerView adapter;
    int number_of_pages;//页数
    int REQUEST_CODE = 5;
    int REQUEST_CAMERA_PERMISSION = 0;
    Boolean ifSearch = false;
    private int lastVisibleItem ;
    String searchText;
    Integer[] bookNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        ZXingLibrary.initDisplayOpinion(this);
        initView();
        setListener();
        displayList();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        toolbar.inflateMenu(R.menu.scan);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.action_scan) {
                    //如果有权限
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent it = new Intent(mContext, CaptureActivity.class);
                        startActivityForResult(it, REQUEST_CODE);
                    } else {//没有权限
                        ActivityCompat.requestPermissions(SearchPage.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
                    }

                }
                return true;
            }
        });
        search = (SearchView) findViewById(R.id.search);
        // 设置搜索文本监听
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String s) {
                number_of_pages = 1;
                searchText = s;
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }
                String bql ="select * from BookInfo where bookWriter='"+s+ "' or bookName ='" +s+"'";
                final BmobQuery<BookInfo> query = new BmobQuery<>();
                query.setSQL(bql);
                query.setLimit(10);
                query.doSQLQuery(new SQLQueryListener<BookInfo>() {
                    @Override
                    public void done(BmobQueryResult<BookInfo> bmobQueryResult, BmobException e) {
                        List<BookInfo> list = bmobQueryResult.getResults();
                        if (e == null) {
                            if (list != null && list.size() > 0) {
                                bookNum = new Integer[list.size()];
                                for (int i=0;i<list.size();i++) {
                                    bookNum[i] = list.get(i).getBookNum();
                                }
                            } else {
                                bookInfoList.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(mContext, "对不起，还没有这本书", Toast.LENGTH_SHORT).show();
                            }
                            ifSearch = true;
                        } else {
                            Toast.makeText(mContext, "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //查找book
                BmobQuery<SharingBook> query2 = new BmobQuery<>();
                query2.addWhereContainedIn("bookNum", Arrays.asList(bookNum));
                query2.findObjects(new FindListener<SharingBook>() {
                    @Override
                    public void done(List<SharingBook> list, BmobException e) {
                        if (e == null) {
                            final Integer[] shareNum = new Integer[list.size()];
                            for (int i=0;i<list.size();i++) {
                                shareNum[i] = list.get(i).getBookNum();
                            }

                            BmobQuery<BookInfo> query1 = new BmobQuery<>();
                            query1.addWhereContainedIn("bookNum", Arrays.asList(shareNum));
                            query1.setLimit(10);
                            query1.findObjects(new FindListener<BookInfo>() {
                                @Override
                                public void done(List<BookInfo> list2, BmobException e) {
                                    if (e == null) {
                                        bookInfoList = list2;
                                        adapter = new myAdapterRecyclerView(mContext, bookInfoList,shareNum);
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

                return true;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        recyclerView = (RecyclerView) findViewById(R.id.booklist);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void setListener() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                number_of_pages = 1;
                refreshBookList();
            }
        });

        //滑动隐藏软键盘
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
                if (ifSearch) {
                    /*String bql ="select * from BookInfo where bookWriter='"+searchText+ "' or bookName ='" +searchText+"'";
                    BmobQuery<BookInfo> query = new BmobQuery<>();
                    query.setSQL(bql);
                    query.setSkip(10 * number_of_pages);
                    query.setLimit(10);
                    number_of_pages=number_of_pages+1;
                    query.doSQLQuery(new SQLQueryListener<BookInfo>() {
                        @Override
                        public void done(BmobQueryResult<BookInfo> bmobQueryResult, BmobException e) {
                            List<BookInfo> list = bmobQueryResult.getResults();
                            if (e == null) {
                                if (list != null && list.size() > 0) {
                                    bookInfoList = list;
                                    adapter = new myAdapterRecyclerView(mContext, bookInfoList);
                                    recyclerView.setAdapter(adapter);
                                } else {
                                    bookInfoList.clear();
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(mContext, "查询成功，无数据返回", Toast.LENGTH_SHORT).show();
                                }
                                ifSearch = true;
                            } else {
                                Toast.makeText(mContext, "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });*/
                } else {
                    //查找book
                    BmobQuery<SharingBook> query = new BmobQuery<>();
                    //列表中不显示自己分享的书
                    _User bmobUser = BmobUser.getCurrentUser(_User.class);
                    Integer userNum = bmobUser.getUserNum();
                    query.addWhereNotEqualTo("ownerNum", userNum);
                    query.findObjects(new FindListener<SharingBook>() {
                        @Override
                        public void done(List<SharingBook> list, BmobException e) {
                            if (e == null) {
                                final Integer[] bookNum = new Integer[list.size()];
                                for (int i=0;i<list.size();i++) {
                                    bookNum[i] = list.get(i).getBookNum();
                                }
                                BmobQuery<BookInfo> query1 = new BmobQuery<BookInfo>();
                                query1.addWhereContainedIn("bookNum", Arrays.asList(bookNum));
                                query1.setSkip(10 * number_of_pages);
                                query1.setLimit(10);
                                number_of_pages=number_of_pages+1;
                                query1.findObjects(new FindListener<BookInfo>() {
                                    @Override
                                    public void done(List<BookInfo> list2, BmobException e) {
                                        if (e == null) {
                                            bookInfoList.addAll(list2);

                                        } else {
                                            Toast.makeText(mContext, "查询失败。"+e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(mContext, "查询失败。"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
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
                        displayList();
                        refresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //显示列表
    private void displayList() {
        //查找book
        BmobQuery<SharingBook> query = new BmobQuery<>();
        //列表中不显示自己分享的书
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        Integer userNum = bmobUser.getUserNum();
        query.addWhereNotEqualTo("ownerNum", userNum);
        query.findObjects(new FindListener<SharingBook>() {
            @Override
            public void done(List<SharingBook> list, BmobException e) {
                if (e == null) {
                    final Integer[] bookNum = new Integer[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }

                    BmobQuery<BookInfo> query1 = new BmobQuery<>();
                    query1.addWhereContainedIn("bookNum", Arrays.asList(bookNum));
                    query1.setLimit(10);
                    query1.findObjects(new FindListener<BookInfo>() {
                        @Override
                        public void done(List<BookInfo> list2, BmobException e) {
                            if (e == null) {
                                bookInfoList = list2;
                                adapter = new myAdapterRecyclerView(mContext, bookInfoList,bookNum);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(mContext, "查询失败。"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(mContext, "查询失败1。"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    //查询扫描的书
                    BmobQuery<BookInfo> query = new BmobQuery<>();
                    query.addWhereEqualTo("ISBN", result);
                    query.findObjects(new FindListener<BookInfo>() {
                        @Override
                        public void done(List<BookInfo> list, BmobException e) {
                            if (e == null) {
                                Bundle data = new Bundle();
                                //利用Intent传递数据
                                data.putInt("booknum",list.get(0).getBookNum());
                                data.putString("objectId",list.get(0).getObjectId());
                                Intent intent = new Intent(mContext, Book_detail.class);
                                intent.putExtras(data);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
                            } else {
                                Toast.makeText(mContext, "暂时没有这本书", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent it = new Intent(mContext, CaptureActivity.class);
                startActivityForResult(it, REQUEST_CODE);
            } else {
                Toast.makeText(mContext, "权限已被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
