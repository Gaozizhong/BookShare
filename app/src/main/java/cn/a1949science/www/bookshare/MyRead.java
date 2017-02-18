package cn.a1949science.www.bookshare;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyRead extends AppCompatActivity {
    Context mContext = MyRead.this;
    ImageView before;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_read);
        findView();
        onClick();
        displayList();
    }
    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        listview = (ListView) findViewById(R.id.myBookList);
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
        BmobQuery<Book_Info> query = new BmobQuery<>();
        //查找出有ownerNum的信息
        query.addWhereExists("ownerNum");
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    final int[] bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    listview.setAdapter(new MyAdapter(mContext,list));

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Bundle data = new Bundle();
                            //利用Intent传递数据
                            //data.putString("imageid", list.get(i).getBookPicture().getFileUrl());
                            data.putInt("booknum",bookNum[i]);
                            data.putString("objectId",list.get(i).getObjectId());
                            Intent intent = new Intent(mContext, Book_detail.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        }
                    });
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
