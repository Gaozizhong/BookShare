package cn.a1949science.www.bookshare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import cn.a1949science.www.bookshare.bean.Book_Info;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 高子忠 on 2017/1/2.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<Book_Info> list;

    public MyAdapter(Context context, List<Book_Info> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        ImageView image;
        TextView book;
        TextView writer;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        //新增一个内部类ViewHolder,用于对控件的实例进行缓存
        ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            //实例化一个布局文件
            view = inflater.inflate(R.layout.booklist_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) view.findViewById(R.id.image);
            viewHolder.book = (TextView) view.findViewById(R.id.book);
            viewHolder.writer = (TextView) view.findViewById(R.id.writer);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String bookName = list.get(i).getBookName();
        String bookWriter = list.get(i).getBookWriter();

        Glide.with(context)
                .load(list.get(i).getBookPicture().getFileUrl())
                .into(viewHolder.image);
        viewHolder.book.setText(bookName);
        viewHolder.writer.setText(bookWriter);

        return view;
    }

}

