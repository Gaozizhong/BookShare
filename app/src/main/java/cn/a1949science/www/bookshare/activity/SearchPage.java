package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.adapter.myAdapterRecyclerView;
import cn.a1949science.www.bookshare.bean.Book_Info;
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
    List<Book_Info> bookInfoList= null;
    myAdapterRecyclerView adapter;
    int number_of_pages=1;
    private int lastVisibleItem ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
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
        search = (SearchView) findViewById(R.id.search);
        // 设置搜索文本监听
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String s) {
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }
                String bql ="select * from Book_Info where BookWriter='"+s+ "' or BookName ='" +s+"'";
                BmobQuery<Book_Info> query = new BmobQuery<>();
                query.setSQL(bql);
                query.doSQLQuery(new SQLQueryListener<Book_Info>() {
                    @Override
                    public void done(BmobQueryResult<Book_Info> bmobQueryResult, BmobException e) {
                        List<Book_Info> list = bmobQueryResult.getResults();
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
                        } else {
                            Toast.makeText(mContext, "查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    //loadBookList();
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
        BmobQuery<Book_Info> query1 = new BmobQuery<>();
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
}
