package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 高子忠 on 2017/4/30.
 */

public class BookInfo {
    private Integer bookNum;//图书序号
    private String bookName;//书名
    private String bookWriter;//作者
    private String translator;//译者
    private String introduction;//图书介绍
    private BmobFile bookImage;//图书照片
    private String bookSort;//图书分类
    private Integer ISBN;//图书ISBN
    private String bookPress;//图书出版社
    private String publishedDate;//图书出版时间
    private Integer bookVeision;//图书版本

    public Integer getBookNum(){return bookNum;}
    public void setBookNum(Integer bookNum){this.bookNum =  bookNum;}

    public String getBookName() {
        return bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookWriter() {
        return bookWriter;
    }
    public void setBookWriter(String bookWriter) {
        this.bookWriter = bookWriter;
    }

    public String getTranslator() {
        return translator;
    }
    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public BmobFile getBookImage(){return bookImage;}
    public void setBookImage(BmobFile bookImage){this.bookImage =  bookImage;}

    public String getBookSort() {
        return bookSort;
    }
    public void setBookSort(String bookSort) {
        this.bookSort = bookSort;
    }

    public Integer getISBN(){return ISBN;}
    public void setISBN(Integer ISBN){this.ISBN =  ISBN;}

    public String getBookPress() {
        return bookPress;
    }
    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public String getPublishedDate() {
        return publishedDate;
    }
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Integer getBookVeision(){return bookVeision;}
    public void setBookVeision(Integer bookVeision){this.bookVeision =  bookVeision;}

}
