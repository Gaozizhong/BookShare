package cn.a1949science.www.bookshare.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import cn.a1949science.www.bookshare.MyAdapter;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.ui.Book_detail;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;

/**
 * Created by 高子忠 on 2017/3/18.
 */

public class TwoFragment extends Fragment {
    View v;
    ListView listview;
    Integer borrowBookNum,loanBookNum,textNum1,textNum2,textNum3,userNum;
    String objectId,time;
    int[] bookNum;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.info_item2, container, false);
        listview = (ListView) v.findViewById(R.id.listView2);
        displayList2();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //显示列表
    private void displayList2() {

        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        //查询是否有书主为自己的借书信息
        BmobQuery<Shared_Info> query = new BmobQuery<>();
        query.addWhereEqualTo("ownerName", bmobUser.getUsername());
        query.addWhereEqualTo("ifAffirm", false);
        query.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()!=0) {
                    bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    findBook();
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "无书借出。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findBook() {
        //查找book
        BmobQuery<Book_Info> query1 = new BmobQuery<>();
        query1.addWhereContainedIn("BookNum", Arrays.asList(bookNum));
        query1.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    //Toast.makeText(mContext, "查询成功：共" + list.size() + "条数据。", Toast.LENGTH_SHORT).show();
                    listview.setAdapter(new MyAdapter(getContext(),list));

                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "查询失败111111222222。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
