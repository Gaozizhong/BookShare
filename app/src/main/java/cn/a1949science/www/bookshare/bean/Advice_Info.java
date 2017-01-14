package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 高子忠 on 2016/12/4.
 */
public class Advice_Info extends BmobObject {

    //反馈人序号
    private String userName;
    //反馈人电话
    private String userPhone;
    //反馈序号
    private Integer AdviceNum;
    //反馈内容
    private String AdviceContent;
    //用createdAT代替反馈时间

    public String getUserName(){return userName;}
    public void setUserName(String userName){this.userName =userName;}

    public String getUserPhone(){return userPhone;}
    public void setUserPhone(String userPhone){this.userPhone =userPhone;}

    public Integer getAdviceNum(){return AdviceNum;}
    public void setAdviceNum(String AdviceNum){this.AdviceNum =  Integer.parseInt(AdviceNum);}

    public String getAdviceContent(){return AdviceContent;}
    public void setAdviceContent(String AdviceContent){this.AdviceContent = AdviceContent;}
}
