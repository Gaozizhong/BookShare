package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 高子忠 on 2017/4/30.
 */

public class WriterInfo extends BmobObject {
    private Integer writerNum;//作者序号
    private String writerName;//作者姓名
    private String writerNation;//作者国籍
    private String introduction;//作者简介

    public Integer getWriterNum(){return writerNum;}
    public void setWriterNum(Integer writerNum){this.writerNum =  writerNum;}

    public String getWriterName() {
        return writerName;
    }
    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getWriterNation() {
        return writerNation;
    }
    public void setWriterNation(String writerNation) {
        this.writerNation = writerNation;
    }

    public String getIntroduction() {
        return introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

}
