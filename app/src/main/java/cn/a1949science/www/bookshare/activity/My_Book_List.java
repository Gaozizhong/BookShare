package cn.a1949science.www.bookshare.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.a1949science.www.bookshare.adapter.MyAdapter;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class My_Book_List extends AppCompatActivity {
    Context mContext = My_Book_List.this;
    ImageView before;
    ListView listview;
    TextView count;
    int[] bookNum;
    String countText;

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
        count = (TextView) findViewById(R.id.count);
    }

    //点击事件
    private void onClick(){
        assert before != null;
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
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
                    countText = list.size() + "本";
                    count.setText(countText);
                    listview.setAdapter(new MyAdapter(mContext,list));

                    listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                    });

                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
