package cn.a1949science.www.bookshare.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 高子忠 on 2016/12/4.
 */
public class _User extends BmobUser {

//    username: 用户的用户名（必需）。
//    password: 用户的密码（必需）。
//    email: 用户的电子邮件地址（可选）。
//    emailVerified:邮箱认证状态（可选）。
//    mobilePhoneNumber：手机号码（可选）。
//    mobilePhoneNumberVerified：手机号码的认证状态（可选）

    //用户序号
    private Integer userNum;
    //用户性别
    private Boolean usersex;
    //用户学校
    private String userSchool;
    //宿舍号
    private String userDorm;
    //班级
    private String userClass;
    //学号
    private Integer studentID;
    //借书状态
    private Boolean needReturn;
    //用户昵称
    private String nickname;
    //用户头像
    private BmobFile favicon;
    //用户学生证照片
    private BmobFile StudentCard;
    //用户是否通过认证
    private Boolean certificationOk;

    public Integer getUserNum(){return userNum;}
    public void setUserNum(Integer userNum){this.userNum =  userNum;}

    public Boolean getUsersex() {return usersex;}
    public void setUsersex(Boolean usersex){this.usersex = usersex;}

    public String getUserSchool() {
        return userSchool;
    }
    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

    public String getUserDorm() {
        return userDorm;
    }
    public void setUserDorm(String userDorm) {
        this.userDorm = userDorm;
    }

    public String getUserClass() {
        return userClass;
    }
    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public Integer getStudentID(){return studentID;}
    public void setStudentID(String studentID){this.studentID =  Integer.parseInt(studentID);}

    public Boolean getNeedReturn() {return needReturn;}
    public void setNeedReturn(Boolean needReturn){this.needReturn = needReturn;}

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BmobFile getFavicon(){return favicon;}
    public void setFavicon(BmobFile favicon){this.favicon =  favicon;}

    public BmobFile getStudentCard(){return StudentCard;}
    public void setStudentCard(BmobFile StudentCard){this.StudentCard =  StudentCard;}

    public Boolean getCertificationOk() {return certificationOk;}
    public void setCertificationOk(Boolean certificationOk){this.certificationOk = certificationOk;}

}
