package cn.a1949science.www.bookshare.fragment;

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

import java.util.List;

import cn.a1949science.www.bookshare.MyAdapter;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.ui.Book_detail;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 高子忠 on 2017/3/18.
 */

public class ThreeFragment extends Fragment {
    View v;
    ListView listview;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.info_item3, container, false);
        listview = (ListView) v.findViewById(R.id.listView3);
        displayList();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //显示列表
    private void displayList() {
        //查找book
        BmobQuery<Book_Info> query = new BmobQuery<>();
        //查找出有ownerNum的信息
        query.addWhereEqualTo("BeShared", false);
        //列表中不显示自己分享的书
        /*_User bmobUser = BmobUser.getCurrentUser(_User.class);
        String username = bmobUser.getUsername();
        query.addWhereNotEqualTo("ownerName", username);*/
        query.setLimit(50);
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    //Toast.makeText(mContext, "查询成功：共" + list.size() + "条数据。", Toast.LENGTH_SHORT).show();
                    final int[] bookNum = new int[list.size()];
                    for (int i=0;i<list.size();i++) {
                        bookNum[i] = list.get(i).getBookNum();
                    }
                    listview.setAdapter(new MyAdapter(getContext(),list));

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Bundle data = new Bundle();
                            //利用Intent传递数据
                            //data.putString("imageid", list.get(i).getBookPicture().getFileUrl());
                            data.putInt("booknum",bookNum[i]);
                            data.putString("objectId",list.get(i).getObjectId());
                            Intent intent = new Intent(getContext(), Book_detail.class);
                            intent.putExtras(data);
                            startActivity(intent);
                        }
                    });
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "查询失败。", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
