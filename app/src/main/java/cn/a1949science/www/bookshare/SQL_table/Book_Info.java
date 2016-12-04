package cn.a1949science.www.bookshare.SQL_table;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Book_Info extends BmobObject {

    private Integer BookNum;//图书序号
    private Integer ownerNum;//书主序号
    private BmobFile BookPicture;//图书照片
    private String BookDescribe;//图书介绍
    private String BookName;//书名
    private String BookWriter;//作者
    private Integer keepTime;//可借时间

    public Integer getBookNum(){return BookNum;}
    public void setBookNum(String BookNum){this.BookNum =  Integer.parseInt(BookNum);}

    public Integer getOwnerNum(){return ownerNum;}
    public void setOwnerNum(String ownerNum){this.ownerNum =  Integer.parseInt(ownerNum);}

    public BmobFile getBookPicture(){return BookPicture;}
    public void setBookPicture(BmobFile BookPicture){this.BookPicture =  BookPicture;}

    public String getBookName() {
        return BookName;
    }
    public void setBookName(String BookName) {
        this.BookName = BookName;
    }

    public String getBookDescribe() {
        return BookDescribe;
    }
    public void setBookDescribe(String BookDescribe) {
        this.BookDescribe = BookDescribe;
    }

    public String getBookWriter() {
        return BookWriter;
    }
    public void setBookWriter(String BookWriter) {
        this.BookWriter = BookWriter;
    }

    public Integer getkeepTime(){return keepTime;}
    public void setkeepTime(String keepTime){this.keepTime =  Integer.parseInt(keepTime);}

}
