package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.util.AES;
import lombok.Data;

/**
 * @author snow create 2021/01/18 11:45
 */
@Data
public class User {
    public static String AES_PASS = "CompuOrg2021/01/17";

    protected Long id;
    protected Byte gender;
    protected String email;
    protected String mobile;
    protected String userNo;
    protected String password;
    protected String realName;

    public String getDecryptEmail(){
        if(this.email != null) {
            return AES.decrypt(this.email, AES_PASS);
        }
        return null;
    }

    public String getDecryptMobile(){
        if(this.mobile != null) {
            return AES.decrypt(this.mobile, AES_PASS);
        }
        return null;
    }

    public String getGender(){
        if(this.gender == (byte)0){
            return "女";
        }
        else if(this.gender == (byte)1){
            return "男";
        }
        return null;
    }
    public void setGender(String gender){
        if(gender.equals("男")){
            this.gender = (byte)1;
        }
        else if(gender.equals("女")){
            this.gender = (byte)0;
        }
    }
}
