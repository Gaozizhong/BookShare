package cn.a1949science.www.bookshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.activity.Book_detail;
import cn.a1949science.www.bookshare.bean.BookInfo;

/**
 * Created by 高子忠 on 2017/5/19.
 */

public class listAdapter extends RecyclerView.Adapter<myAdapterRecyclerView.ViewHolder>
        implements View.OnClickListener{
    private Context context;
    private List<BookInfo> list;

    @Override
    public void onClick(View view) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View bookView;
        ImageView image;
        TextView book;
        TextView writer;

        public ViewHolder(View itemView) {
            super(itemView);
            bookView = itemView;
            image = (ImageView) itemView.findViewById(R.id.image);
            book = (TextView) itemView.findViewById(R.id.book);
            writer = (TextView) itemView.findViewById(R.id.writer);
        }
    }

    public listAdapter(Context context,List<BookInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public myAdapterRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_item, parent, false);
        final myAdapterRecyclerView.ViewHolder holder = new myAdapterRecyclerView.ViewHolder(view);
        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int position = holder.getAdapterPosition();
                BookInfo book_info = list.get(position);
                Bundle data = new Bundle();
                //利用Intent传递数据
                data.putInt("textNum",0);
                data.putInt("shareNum",shareNum[list.size()-1-position]);
                data.putInt("booknum",book_info.getBookNum());
                data.putInt("userNum",0);
                Intent intent = new Intent(context, Book_detail.class);
                intent.putExtras(data);
                context.startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);*/
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(myAdapterRecyclerView.ViewHolder holder, int position) {
        BookInfo book_info = list.get(position);
        String bookName = book_info.getBookName();
        String bookWriter = book_info.getBookWriter();
        Glide.with(context)
                .load(book_info.getBookImage().getFileUrl())
                .thumbnail(0.5f)
                .override((int)(context.getResources().getDisplayMetrics().density*120+0.5f),(int)(context.getResources().getDisplayMetrics().density*120+0.5f))
                .into(holder.image);
        holder.book.setText(bookName);
        holder.writer.setText(bookWriter);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
