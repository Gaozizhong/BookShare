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

    //书主是否同意
    private Boolean ifAgree;
    //书主是否同意
    private Boolean ifLoan;
    //书主是否同意
    private Boolean ifFinish;
    //图书是否归还
    private Boolean ifReturn;

    public Integer getShareNum(){return ShareNum;}
    public void setShareNum(Integer ShareNum){this.ShareNum =  ShareNum;}

    public Integer getUserNum(){return UserNum;}
    public void setUserNum(Integer UserNum){this.UserNum =  UserNum;}

    public Integer getOwnerNum(){return OwnerNum;}
    public void setOwnerNum(Integer OwnerNum){this.OwnerNum =  OwnerNum;}

    public Integer getBookNum(){return BookNum;}
    public void setBookNum(Integer BookNum){this.BookNum = BookNum;}

    public Boolean getIfAgree(){return ifAgree;}
    public void setIfAgree(Boolean ifAgree){this.ifAgree =  ifAgree;}

    public Boolean getIfLoan(){return ifLoan;}
    public void setIfLoan(Boolean ifLoan){this.ifLoan =  ifLoan;}

    public Boolean getIfFinish(){return ifFinish;}
    public void setIfFinish(Boolean ifFinish){this.ifFinish =  ifFinish;}

    public Boolean getIfReturn(){return ifReturn;}
    public void setIfReturn(Boolean ifReturn){this.ifReturn =  ifReturn;}

}
