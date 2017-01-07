package cn.a1949science.www.bookshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class Book_detail extends AppCompatActivity {

    ImageView before;
    ListView listview;
    ImageButton likeBtn;
    String introduce,bookname,writername;
    public String bookowner;
    int time,booknum,OwnerNum;
    private String[] introduces = new String[]
            {"    一个老是受班上同学欺负的瘦弱小男孩，因为拥有一种特殊能力而强大：他能偷别人的影子。"};
    private String[] times=new String[]
            {"7天"};
    private String[] bookowners=new String[]
            {" 高子中"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);

        findView();
        onClick();

        displayList();

        //Toast.makeText(Book_detail.this, booknum, Toast.LENGTH_SHORT).show();




    }
    //查找地址
    private void findView(){
        before = (ImageView) findViewById(R.id.before);
        listview = (ListView) findViewById(R.id.bookInfo);
        likeBtn = (ImageButton)findViewById(R.id.likeBtn);
    }

    //点击事件
    protected void onClick(){
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
        Bundle bundle = this.getIntent().getExtras();
        booknum = bundle.getInt("booknum");
        final String objectId = bundle.getString("objectId");
        //查找book
        final BmobQuery<Book_Info> query = new BmobQuery<>();
        query.getObject(objectId, new QueryListener<Book_Info>() {
            @Override
            public void done(Book_Info book_info, BmobException e) {
                if (e == null) {
                    introduce = book_info.getBookDescribe();
                    bookname = book_info.getBookName();
                    writername = book_info.getBookWriter();
                    time = book_info.getkeepTime();
                    OwnerNum = book_info.getOwnerNum();

                    BmobQuery<_User> query1 = new BmobQuery<>();
                    //查找userNum为OwnerNum的用户
                    query1.addWhereEqualTo("userNum",OwnerNum);
                    query1.findObjects(new FindListener<_User>() {
                        @Override
                        public void done(List<_User> list1, BmobException e) {
                            if (e == null) {
                                bookowner= list1.get(0).getUsername();
                            } else {
                                Toast.makeText(Book_detail.this, "查询书主失败。", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    BmobFile image = book_info.getBookPicture();

                    List<Map<String, Object>> listItems = new ArrayList<>();
                    for(int i=0;i<times.length;i++) {
                        Map<String, Object> listItem = new HashMap<>();
                        listItem.put("imageid", image);
                        listItem.put("introduce", introduce);
                        listItem.put("bookname", bookname);
                        listItem.put("writername", writername);
                        listItem.put("time", time);
                        listItem.put("bookowner",OwnerNum);
                        listItems.add(listItem);
                    }
                    //创建一个SimpleAdapter
                    SimpleAdapter simpleAdapter = new SimpleAdapter(Book_detail.this, listItems,
                            R.layout.bookinfo,
                            new String[] {"imageid","introduce","bookname","writername","time","bookowner"},
                            new int[] {R.id.image,R.id.introduce,R.id.bookName,R.id.writename,R.id.time,R.id.bookOwner});
                    //为ListView设置Adapter
                    listview.setAdapter(simpleAdapter);
                } else {
                    Toast.makeText(Book_detail.this, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
