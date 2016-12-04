package cn.a1949science.www.bookshare.SQL_table;

import cn.bmob.v3.BmobObject;

/**
 * Created by 高子忠 on 2016/12/4.
 */
public class Advice_Info extends BmobObject {

    //反馈人序号
    private Integer UserNum;
    //反馈序号
    private Integer AdviceNum;
    //反馈内容
    private Integer AdviceContent;
    //用createdAT代替反馈时间

    public Integer getUserNum(){return UserNum;}
    public void setUserNum(String UserNum){this.UserNum =  Integer.parseInt(UserNum);}

    public Integer getAdviceNum(){return AdviceNum;}
    public void setAdviceNum(String AdviceNum){this.AdviceNum =  Integer.parseInt(AdviceNum);}

    public Integer getAdviceContent(){return AdviceContent;}
    public void setAdviceContent(String AdviceContent){this.AdviceContent =  Integer.parseInt(AdviceContent);}
}
