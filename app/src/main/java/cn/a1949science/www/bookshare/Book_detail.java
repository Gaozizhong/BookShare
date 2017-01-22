package cn.a1949science.www.bookshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class Book_detail extends AppCompatActivity {

    Context mContext = Book_detail.this;
    ImageView before;
    ListView listview;
    ImageButton likeBtn;
    String introduce,bookname,writername,OwnerName;
    public String bookowner;
    int time,booknum,OwnerNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book__info);

        findView();
        onClick();
        displayList();

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

        /*LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.bookinfo, (ViewGroup) findViewById(R.id.book_info_item));
        final View Owner = (View) layout.findViewById(R.id.owner);
        Owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Book_detail.this, "查询书主。", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    //显示列表
    private void displayList() {
        Bundle bundle = this.getIntent().getExtras();
        booknum = bundle.getInt("booknum");
        final String objectId = bundle.getString("objectId");
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        //查找book
        final BmobQuery<Book_Info> query = new BmobQuery<>();

        /*query.addWhereEqualTo("owner",bmobUser);
        query.order("-updatedAt");
        query.include("owner");
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(List<Book_Info> list, BmobException e) {
                if (e == null) {
                    Toast.makeText(Book_detail.this, "查询112233。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Book_detail.this, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        query.getObject(objectId, new QueryListener<Book_Info>() {
            @Override
            public void done(Book_Info book_info, BmobException e) {
                if (e == null) {
                    introduce = book_info.getBookDescribe();
                    bookname = book_info.getBookName();
                    writername = book_info.getBookWriter();
                    time = book_info.getkeepTime();
                    OwnerNum = book_info.getOwnerNum();
                    OwnerName = book_info.getOwnerName();
                    _User Owner = book_info.getOwner();
                    //OwnerName = Owner.getUsername();
                    //Toast.makeText(Book_detail.this, OwnerName, Toast.LENGTH_SHORT).show();
                    BmobFile image = book_info.getBookPicture();

                    List<Map<String, Object>> listItems = new ArrayList<>();
                    for(int i=0;i<1;i++) {
                        Map<String, Object> listItem = new HashMap<>();
                        listItem.put("imageid", image);
                        listItem.put("introduce", introduce);
                        listItem.put("bookname", bookname);
                        listItem.put("writername", writername);
                        listItem.put("time", time);
                        listItem.put("bookowner",OwnerName);
                        listItems.add(listItem);
                    }
                    //创建一个SimpleAdapter
                    SimpleAdapter simpleAdapter = new SimpleAdapter(Book_detail.this, listItems,
                            R.layout.bookinfo,
                            new String[] {"imageid","introduce","bookname","writername","time","bookowner"},
                            new int[] {R.id.image,R.id.introduce,R.id.bookName,R.id.writename,R.id.time,R.id.bookOwner});
                    //为ListView设置Adapter
                    listview.setAdapter(simpleAdapter);

                    //listview.setAdapter(new BookAdapter(mContext,book_info));
                } else {
                    Toast.makeText(Book_detail.this, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
