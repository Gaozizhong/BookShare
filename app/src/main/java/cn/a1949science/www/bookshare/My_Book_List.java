package cn.a1949science.www.bookshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class My_Book_List extends AppCompatActivity {
    Context mContext = My_Book_List.this;
    ImageView before;
    ListView listview;
    TextView count;
    int[] bookNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__book__list);

        findView();
        onClick();
        displayList();

    }

    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        listview = (ListView) findViewById(R.id.myBookList);
        count = (TextView) findViewById(R.id.content);
    }

    //点击事件
    private void onClick(){
        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    //显示列表
    private void displayList() {
        //查找book
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        BmobQuery<Book_Info> query = new BmobQuery<>();
        //查找出本用户发布的所有书籍
        query.addWhereEqualTo("owner", bmobUser);
        query.order("-updatedAt");
        // 希望在查询书籍信息的同时也把发布人的信息查询出来
        query.include("owner");
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    listview.setAdapter(new MyAdapter(mContext,list));

                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //查找我正在被人借的book
        String name = bmobUser.getUsername();
        BmobQuery<Shared_Info> query1 = new BmobQuery<Shared_Info>();
        //查找出本用户发布的所有书籍
        query1.addWhereEqualTo("ownerName", name);
        query1.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null) {
                    bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
