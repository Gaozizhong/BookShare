package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by 高子忠 on 2016/12/4.
 */
public class MyUser extends BmobObject {

//    username: 用户的用户名（必需）。
//    password: 用户的密码（必需）。
//    email: 用户的电子邮件地址（可选）。
//    emailVerified:邮箱认证状态（可选）。
//    mobilePhoneNumber：手机号码（可选）。
//    mobilePhoneNumberVerified：手机号码的认证状态（可选）

    //用户序号
    private Integer userNum;
    //借书状态
    private Boolean needReturn;
    //用户姓名
    private String username;
    //用户姓名
    private String password;

    public Integer getUserNum(){return userNum;}
    public void setUserNum(String userNum){this.userNum =  Integer.parseInt(userNum);}

    public Boolean getNeedReturn() {return needReturn;}
    public void setNeedReturn(Boolean needReturn){this.needReturn = needReturn;}

    public String getUsername() {return username;}
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {return password;}
    public void setPassword(String password) {
        this.password = password;
    }
}
