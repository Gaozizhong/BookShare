package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 高子忠 on 2016/12/6.
 */
public class Like_Book extends BmobObject {

    //用户序号
    private Integer userNum;
    //图书序号
    private Integer BookNum;


    public Integer getUserNum(){return userNum;}
    public void setUserNum(Integer userNum){this.userNum =  userNum;}

    public Integer getBookNum(){return BookNum;}
    public void setBookNum(Integer BookNum){this.BookNum =  BookNum;}

}
