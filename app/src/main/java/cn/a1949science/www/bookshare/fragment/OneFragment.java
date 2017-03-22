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

import cn.a1949science.www.bookshare.adapter.MyAdapter;
import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.a1949science.www.bookshare.bean.Shared_Info;
import cn.a1949science.www.bookshare.bean._User;
import cn.a1949science.www.bookshare.ui.Sharing;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 高子忠 on 2017/3/18.
 */

public class OneFragment extends Fragment {
    View v;
    ListView listview;
    Integer borrowBookNum,textNum;
    String objectId,time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.info_item, container, false);
        listview = (ListView) v.findViewById(R.id.listView);
        displayList();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //显示列表
    private void displayList() {
        _User bmobUser = BmobUser.getCurrentUser(_User.class);
        //查询是否有借书人为自己的借书信息
        BmobQuery<Shared_Info> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("UserNum", bmobUser.getUserNum());
        query1.addWhereEqualTo("ifReturn", false);
        query1.findObjects(new FindListener<Shared_Info>() {
            @Override
            public void done(final List<Shared_Info> list, BmobException e) {
                if (e == null && list.size()!=0) {
                    borrowBookNum = list.get(0).getBookNum();
                    if (!list.get(0).getIfAgree() && !list.get(0).getIfLoan() && !list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //借书人刚发送借书请求
                        textNum = 1;
                    } else if (list.get(0).getIfAgree() && !list.get(0).getIfLoan() && !list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //书主同意了借书人的请求
                        textNum = 3;
                    } else if (list.get(0).getIfAgree() && list.get(0).getIfLoan() && !list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //书主已借出书
                        textNum = 5;
                    }else if (list.get(0).getIfAgree() && list.get(0).getIfLoan() && list.get(0).getIfFinish() && !list.get(0).getIfAffirm() && !list.get(0).getIfReturn()) {
                        //借书过程完成
                        time = list.get(0).getFinishAt().getDate();
                    }
                    objectId = list.get(0).getObjectId();
                    findBook();
                    //Toast.makeText(mContext, "查询成功：共" + list.get(1).getBookName() + "条数据。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "还未借书", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void findBook() {
        //查找book
        BmobQuery<Book_Info> query = new BmobQuery<>();
        query.addWhereEqualTo("BookNum", borrowBookNum);
        query.findObjects(new FindListener<Book_Info>() {
            @Override
            public void done(final List<Book_Info> list, BmobException e) {
                if (e == null) {
                    listview.setAdapter(new MyAdapter(getContext(),list));

                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Bundle data = new Bundle();
                            //利用Intent传递数据
                            data.putInt("booknum",borrowBookNum);
                            data.putInt("textNum",textNum);
                            data.putString("objectId",objectId);
                            Intent intent = new Intent(getContext(), Sharing.class);
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
