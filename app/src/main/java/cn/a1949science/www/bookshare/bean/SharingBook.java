package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 高子忠 on 2017/5/14.
 */

public class SharingBook extends BmobObject {
    private Integer shareNum;//序号
    private Integer ownerNum;//书主序号
    private Integer bookNum;//图书序号
    private Boolean beSharing;//是否借出
    private Integer keepTime;//可借时间

    public Integer getShareNum(){return shareNum;}
    public void setShareNum(Integer shareNum){this.shareNum =  shareNum;}

    public Integer getOwnerNum(){return ownerNum;}
    public void setOwnerNum(Integer ownerNum){this.ownerNum =  ownerNum;}

    public Integer getBookNum(){return bookNum;}
    public void setBookNum(Integer BookNum){this.bookNum =  bookNum;}

    public Boolean getBeSharing() {return beSharing;}
    public void setBeSharing(Boolean beSharing){this.beSharing = beSharing;}

    public Integer getkeepTime(){return keepTime;}
    public void setkeepTime(Integer keepTime){this.keepTime =  keepTime;}

}
