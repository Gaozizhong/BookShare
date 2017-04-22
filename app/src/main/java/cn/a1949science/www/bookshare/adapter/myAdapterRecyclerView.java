package cn.a1949science.www.bookshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.a1949science.www.bookshare.R;
import cn.a1949science.www.bookshare.bean.Book_Info;

/**
 * Created by 高子忠 on 2017/4/22.
 */

public class myAdapterRecyclerView extends RecyclerView.Adapter<myAdapterRecyclerView.ViewHolder>{
    private Context context;
    private List<Book_Info> list;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView book;
        TextView writer;

        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            book = (TextView) itemView.findViewById(R.id.book);
            writer = (TextView) itemView.findViewById(R.id.writer);
        }
    }

    public myAdapterRecyclerView(Context context,List<Book_Info> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booklist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book_Info book_info = list.get(position);
        String bookName = book_info.getBookName();
        String bookWriter = book_info.getBookWriter();
        Glide.with(context)
                .load(book_info.getBookPicture().getFileUrl())
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
