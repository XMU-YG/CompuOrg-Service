package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.AES;
import cn.xmu.edu.Core.util.SHA256;
import cn.xmu.edu.Core.util.Common;
import cn.xmu.edu.compuOrg.model.po.AdminPo;
import cn.xmu.edu.compuOrg.model.vo.AdminRetVo;
import cn.xmu.edu.compuOrg.model.vo.UserVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author snow create 2021/01/18 23:48
 */
@Data
public class Admin extends User implements VoObject, Serializable {

    private Byte emailVerify;
    private String signature;

    public Admin(AdminPo adminPo){
        this.id = adminPo.getId();
        this.gender = adminPo.getGender();
        this.email = adminPo.getEmail();
        this.mobile = adminPo.getMobile();
        this.userNo = adminPo.getAdminNo();
        this.password = adminPo.getPassword();
        this.realName = adminPo.getAdminName();
        this.signature = adminPo.getSignature();
        this.emailVerify = adminPo.getEmailVerify();
    }

    public Admin(UserVo adminVo){
        this.emailVerify = (byte)0;
        setGender(adminVo.getGender());
        this.userNo = adminVo.getUserNo();
        this.password = AES.encrypt(adminVo.getPassword(), AES_PASS);
        this.realName = adminVo.getRealName();
        if(adminVo.getEmail() != null) {
            this.email = AES.encrypt(adminVo.getEmail(), AES_PASS);
        }
        if(adminVo.getMobile() != null) {
            this.mobile = AES.encrypt(adminVo.getMobile(), AES_PASS);
        }
        this.signature = createSignature();
    }

    public AdminPo createAdminPo(){
        AdminPo adminPo = new AdminPo();
        adminPo.setId(this.id);
        adminPo.setAdminNo(this.userNo);
        adminPo.setPassword(this.password);
        adminPo.setAdminName(this.realName);
        adminPo.setGender(this.gender);
        adminPo.setEmail(this.email);
        adminPo.setMobile(this.mobile);
        adminPo.setSignature(this.signature);
        adminPo.setEmailVerify(this.emailVerify);
        return adminPo;
    }

    /**
     * 生成签名
     * @author snow create 2021/01/19 00:25
     * @return
     */
    public String createSignature(){
        StringBuilder signature = Common.concatString("-", this.userNo, this.password, this.email, this.mobile);
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

    @Override
    public Object createVo() {
        return new AdminRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
