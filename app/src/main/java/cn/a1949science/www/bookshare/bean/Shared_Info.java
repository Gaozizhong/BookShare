package cn.a1949science.www.bookshare.bean;

import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

/**
 * Created by 高子忠 on 2016/12/4.
 */
public class Shared_Info extends BmobObject {

    //共享序号
    private Integer ShareNum;
    //借书人序号
    private Integer UserNum;
    //书主序号
    private Integer ownerNum;
    //图书序号
    private Integer BookNum;
    //用createdAT作为分享时间，用z最后的updatedAT作为归还时间
    //书主是否同意
    private Boolean ifAgree;
    //书主是否借出
    private Boolean ifLoan;
    //借书过程是否完成
    private Boolean ifFinish;
    //是否确认还书
    private Boolean ifAffirm;
    //图书是否归还
    private Boolean ifReturn;
    //书主是否拒绝
    private Boolean ifRefuse;
    //借书完成时间
    private BmobDate finishAt;

    public Integer getShareNum(){return ShareNum;}
    public void setShareNum(Integer ShareNum){this.ShareNum =  ShareNum;}

    public Integer getUserNum(){return UserNum;}
    public void setUserNum(Integer UserNum){this.UserNum =  UserNum;}

    public Integer getOwnerNum(){return ownerNum;}
    public void setOwnerNum(Integer ownerNum){this.ownerNum =  ownerNum;}

    public Integer getBookNum(){return BookNum;}
    public void setBookNum(Integer BookNum){this.BookNum = BookNum;}

    public Boolean getIfAgree(){return ifAgree;}
    public void setIfAgree(Boolean ifAgree){this.ifAgree =  ifAgree;}

    public Boolean getIfLoan(){return ifLoan;}
    public void setIfLoan(Boolean ifLoan){this.ifLoan =  ifLoan;}

    public Boolean getIfFinish(){return ifFinish;}
    public void setIfFinish(Boolean ifFinish){this.ifFinish =  ifFinish;}

    public Boolean getIfAffirm(){return ifAffirm;}
    public void setIfAffirm(Boolean ifAffirm){this.ifAffirm =  ifAffirm;}

    public Boolean getIfReturn(){return ifReturn;}
    public void setIfReturn(Boolean ifReturn){this.ifReturn =  ifReturn;}

    public Boolean getIfRefuse(){return ifRefuse;}
    public void setIfRefuse(Boolean ifRefuse){this.ifRefuse =  ifRefuse;}

    public BmobDate getFinishAt(){return finishAt;}
    public void setFinishAt(BmobDate finishAt){this.finishAt =  finishAt;}

}
