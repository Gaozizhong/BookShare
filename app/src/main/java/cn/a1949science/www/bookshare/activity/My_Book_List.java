package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import cn.a1949science.www.bookshare.adapter.MyAdapter;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.adapter.myAdapterRecyclerView;
import cn.a1949science.www.bookshare.bean.BookInfo;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.SharingBook;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class My_Book_List extends AppCompatActivity {
    Context mContext = My_Book_List.this;
    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    int number_of_pages=1;
    List<BookInfo> bookInfoList= null;
    myAdapterRecyclerView adapter;
    private LinearLayoutManager mLayoutManager;
    TextView count;
    Integer[] bookNums,shareNums;
    String countText;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__book__list);

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
        BmobQuery<SharingBook> query = new BmobQuery<>();
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        //查找出本用户发布的所有书籍
        query.addWhereEqualTo("ownerNum", bmobUser.getUserNum());
        query.order("-createdAt");
        query.findObjects(new FindListener<SharingBook>() {
            @Override
            public void done(List<SharingBook> list, BmobException e) {
                if (e == null) {
                    countText = list.size() + "本";
                    count.setText(countText);
                    shareNums = new Integer[list.size()];
                    bookNums = new Integer[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNums[i] = list.get(i).getBookNum();
                        shareNums[i] = list.get(i).getShareNum();
                    }
                    BmobQuery<BookInfo> query1 = new BmobQuery<>();
                    query1.addWhereContainedIn("bookNum", Arrays.asList(bookNums));
                    query1.setLimit(10);
                    query1.order("-createdAt");
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




                   /* listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final String object = list.get(i).getObjectId();
                            final String url = list.get(i).getBookPicture().getUrl();
                            AlertDialog dlg = new AlertDialog.Builder(mContext)
                                    .setTitle("删除此书？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    })
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //删除此书的BookInfo
                                            Book_Info book = new Book_Info();
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
                                            //删除此书的图片
                                            BmobFile file = new BmobFile();
                                            file.setUrl(url);
                                            file.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {
                                                    if (e == null) {
                                                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    })
                                    .create();
                            dlg.show();
                            return true;
                        }
                    });*/



    }
}
