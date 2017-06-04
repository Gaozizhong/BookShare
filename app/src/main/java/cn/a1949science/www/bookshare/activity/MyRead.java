package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.adapter.listAdapter;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Read_Book;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyRead extends AppCompatActivity {
    Context mContext = MyRead.this;
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    private int lastVisibleItem,number_of_pages,selectNum=2;
    List<BookInfo> bookInfoList= null;
    listAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    TextView count;
    Integer[] bookNums;
    String[] objects;
    String countText;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_read);
        findView();
        setListener();
        displayList();
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
            BmobQuery<Read_Book> query = new BmobQuery<>();
            _User bmobUser = BmobUser.getCurrentUser(_User.class);
            query.addWhereEqualTo("userNum",bmobUser.getUserNum());
            query.order("-createdAt");
            query.findObjects(new FindListener<Read_Book>() {
                @Override
                public void done(final List<Read_Book> list, BmobException e) {
                    if (e == null) {
                        countText = list.size() + "本";
                        count.setText(countText);
                        objects = new String[list.size()];
                        bookNums = new Integer[list.size()];
                        for (int i=0;i<list.size();i++) {
                            bookNums[i] = list.get(i).getBookNum();
                            objects[i] = list.get(i).getObjectId();
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
                    }else {
                        Toast.makeText(mContext, "查询失败1。"+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        displayList();
                        refresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    //查找地址
    private void findView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
            }
        });
        //下拉刷新
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);
        recyclerView = (RecyclerView) findViewById(R.id.booklist);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        count = (TextView) findViewById(R.id.count);
    }


    //显示列表
    private void displayList() {
        //查找book
        BmobQuery<Read_Book> query = new BmobQuery<>();
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        query.addWhereEqualTo("userNum",bmobUser.getUserNum());
        query.order("-createdAt");
        query.findObjects(new FindListener<Read_Book>() {
            @Override
            public void done(final List<Read_Book> list, BmobException e) {
                if (e == null) {
                    countText = list.size() + "本";
                    count.setText(countText);
                    objects = new String[list.size()];
                    bookNums = new Integer[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNums[i] = list.get(i).getBookNum();
                        objects[i] = list.get(i).getObjectId();
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
                                adapter = new listAdapter(mContext, bookInfoList,objects,selectNum);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(mContext, "查询失败2。"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(mContext, "查询失败1。"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
