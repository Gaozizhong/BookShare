package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 高子忠 on 2016/12/4.
 */
public class Shared_Info extends BmobObject {

    //共享序号
    private Integer ShareNum;
    //借书人序号
    private Integer UserNum;
    //书主序号
    private Integer OwnerNum;
    //图书序号
    private Integer BookNum;
    //用createdAT作为分享时间，用updatedAT作为归还时间

    public Integer getShareNum(){return ShareNum;}
    public void setShareNum(String ShareNum){this.ShareNum =  Integer.parseInt(ShareNum);}

    public Integer getUserNum(){return UserNum;}
    public void setUserNum(String UserNum){this.UserNum =  Integer.parseInt(UserNum);}

    public Integer getOwnerNum(){return OwnerNum;}
    public void setOwnerNum(String OwnerNum){this.OwnerNum =  Integer.parseInt(OwnerNum);}

    public Integer getBookNum(){return BookNum;}
    public void setBookNum(String BookNum){this.BookNum =  Integer.parseInt(BookNum);}

}
