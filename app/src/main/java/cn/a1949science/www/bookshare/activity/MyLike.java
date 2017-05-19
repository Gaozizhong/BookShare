package cn.a1949science.www.bookshare.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
import cn.a1949science.www.bookshare.adapter.myAdapterRecyclerView;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Like_Book;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyLike extends AppCompatActivity {
    Context mContext = MyLike.this;
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    int number_of_pages=1;
    List<BookInfo> bookInfoList= null;
    listAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    TextView count;
    Integer[] bookNums;
    String countText;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);

        findView();
        displayList();
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
        BmobQuery<Like_Book> query1 = new BmobQuery<>();
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        query1.addWhereEqualTo("userNum",bmobUser.getUserNum());
        query1.findObjects(new FindListener<Like_Book>() {
            @Override
            public void done(final List<Like_Book> list, BmobException e) {
                if (e == null) {
                    countText = list.size() + "本";
                    count.setText(countText);
                    bookNums = new Integer[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNums[i] = list.get(i).getBookNum();
                    }
                    BmobQuery<BookInfo> query1 = new BmobQuery<>();
                    query1.addWhereContainedIn("bookNum", Arrays.asList(bookNums));
                    query1.setLimit(10);
                    query1.findObjects(new FindListener<BookInfo>() {
                        @Override
                        public void done(List<BookInfo> list2, BmobException e) {
                            if (e == null) {
                                bookInfoList = list2;
                                adapter = new listAdapter(mContext, bookInfoList,bookNums);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(mContext, "查询失败2。"+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    /*AlertDialog dlg = new AlertDialog.Builder(mContext)
                            .setTitle("删除此条信息？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //删除此Like_Book
                                    Like_Book book = new Like_Book();
                                    book.setObjectId(object);
                                    book.delete(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                displayList();
                                                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            })
                            .create();
                    dlg.show();*/

                }else {
                    Toast.makeText(mContext, "查询失败1。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
