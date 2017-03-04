package cn.a1949science.www.bookshare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Like_Book;
import cn.a1949science.www.bookshare.bean._User;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MyLike extends AppCompatActivity {
    Context mContext = MyLike.this;
    ImageView before;
    ListView listview;
    String countText;
    TextView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like);

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
            }
        });

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
                    final int[] bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }

                    BmobQuery<Book_Info> query = new BmobQuery<>();
                    query.addWhereContainedIn("BookNum", Arrays.asList(bookNum));
                    query.findObjects(new FindListener<Book_Info>() {
                        @Override
                        public void done(List<Book_Info> list2, BmobException e) {
                            if (e == null) {
                                Toast.makeText(mContext, list2.toString(), Toast.LENGTH_SHORT).show();
                                listview.setAdapter(new MyAdapter(mContext, list2));
                                //长按删除此条信息
                                listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        final String object = list.get(i).getObjectId();
                                        AlertDialog dlg = new AlertDialog.Builder(mContext)
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

                            } else {
                                Toast.makeText(mContext, "查询失败。", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(mContext, "查询失败1。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
