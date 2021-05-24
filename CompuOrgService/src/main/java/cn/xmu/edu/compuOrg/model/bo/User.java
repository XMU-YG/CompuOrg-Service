package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.AES;
import cn.xmu.edu.Core.util.Common;
import cn.xmu.edu.Core.util.SHA256;
import cn.xmu.edu.compuOrg.model.po.UserPo;
import cn.xmu.edu.compuOrg.model.vo.UserBasicInfoVo;
import cn.xmu.edu.compuOrg.model.vo.UserRetVo;
import cn.xmu.edu.compuOrg.model.vo.UserVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author snow create 2021/01/18 11:45
 */
@Data
public class User implements VoObject, Serializable {
    public static String AES_PASS = "CompuOrg2021/01/17";

    protected Long id;
    protected Byte gender;
    private Byte role;
    protected String email;
    private Byte emailVerify;
    protected String mobile;
    private Byte mobileVerify;
    protected String userNo;
    private String userName;
    protected String password;
    protected String realName;
    private String signature;

    public User(UserPo userPo){
        this.id = userPo.getId();
        this.role = userPo.getRole();
        this.email = userPo.getEmail();
        this.mobile = userPo.getMobile();
        this.gender = userPo.getGender();
        this.userName = userPo.getUserName();
        this.password = userPo.getPassword();
        this.realName = userPo.getRealName();
        this.emailVerify = userPo.getEmailVerify();
        this.mobileVerify = userPo.getMobileVerify();
        this.signature = userPo.getSignature();
    }

    public User(UserVo userVo){
        this.gender = userVo.getGender();
        this.userName = userVo.getUserName();
        this.realName = userVo.getRealName();
        this.email = AES.encrypt(userVo.getEmail(), AES_PASS);
        this.mobile = AES.encrypt(userVo.getMobile(), AES_PASS);
        this.password = AES.encrypt(userVo.getPassword(), AES_PASS);
        this.emailVerify = (byte)1;
        this.mobileVerify = (byte)0;
    }

    public UserPo createUserPo(){
        UserPo userPo = new UserPo();
        userPo.setId(this.id);
        userPo.setRole(this.role);
        userPo.setEmail(this.email);
        userPo.setMobile(this.mobile);
        userPo.setGender(this.gender);
        userPo.setUserName(this.userName);
        userPo.setPassword(this.password);
        userPo.setRealName(this.realName);
        userPo.setSignature(this.signature);
        userPo.setEmailVerify(this.emailVerify);
        userPo.setMobileVerify(this.mobileVerify);
        return userPo;
    }

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

    /**
     * 通过userBasicInfo中非空的属性更新属性值
     * @author snow create 2021/01/23 13:53
     * @param userBasicInfoVo
     */
    public void updateUserInfo(UserBasicInfoVo userBasicInfoVo){
        if(userBasicInfoVo.getUserName() != null){
            this.userName = userBasicInfoVo.getUserName();
        }
        if(userBasicInfoVo.getMobile() != null){
            this.mobile = AES.encrypt(userBasicInfoVo.getMobile(), AES_PASS);
        }
        if(userBasicInfoVo.getGender() != null){
            this.gender = userBasicInfoVo.getGender();
        }
        if(userBasicInfoVo.getRealName() != null){
            this.realName = userBasicInfoVo.getRealName();
        }
        this.signature = createSignature();
    }

    /**
     * 生成签名
     * @author snow create 2021/01/19 00:25
     * @return
     */
    public String createSignature(){
        StringBuilder signature = Common.concatString("-", this.userName, this.password, this.role.toString(), this.email, this.mobile);
        return SHA256.getSHA256(signature.toString());
    }

    /**
     * 判断签名是否被篡改
     * @author snow create 2021/01/19 00:26
     * @return
     */
    public Boolean isSignatureBeenModify(){
        if(this.signature.equals(createSignature())){
            return false;
        }
        return true;
    }

    public Boolean authentic(){
        return !isSignatureBeenModify();
    }

    @Override
    public Object createVo() {
        return new UserRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
