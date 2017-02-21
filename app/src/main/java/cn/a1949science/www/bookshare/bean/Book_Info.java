package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Book_Info extends BmobObject {

    private Integer BookNum;//图书序号
    private Boolean BeShared;//是否借出
    private String ownerName;//书主姓名
    private _User owner;//书主
    private BmobFile BookPicture;//图书照片
    private String BookDescribe;//图书介绍
    private String BookName;//书名
    private String BookWriter;//作者
    private Integer keepTime;//可借时间

    public Integer getBookNum(){return BookNum;}
    public void setBookNum(String BookNum){this.BookNum =  Integer.parseInt(BookNum);}

    public Boolean getBeShared() {return BeShared;}
    public void setBeShared(Boolean BeShared){this.BeShared = BeShared;}

    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public _User getOwner() {
        return owner;
    }
    public void setOwner(_User owner) {
        this.owner = owner;
    }

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
