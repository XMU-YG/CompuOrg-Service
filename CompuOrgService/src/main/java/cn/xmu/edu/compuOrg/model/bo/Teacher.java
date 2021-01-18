package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.AES;
import cn.xmu.edu.compuOrg.model.po.TeacherPo;
import cn.xmu.edu.compuOrg.model.vo.TeacherRetVo;
import cn.xmu.edu.compuOrg.model.vo.UserVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author snow create 2021/01/18 10:51
 */
@Data
public class Teacher extends User implements VoObject, Serializable {

    public Teacher(TeacherPo teacherPo){
        this.id = teacherPo.getId();
        this.gender = teacherPo.getGender();
        this.email = teacherPo.getEmail();
        this.mobile = teacherPo.getMobile();
        this.userNo = teacherPo.getTeacherNo();
        this.password = teacherPo.getPassword();
        this.realName = teacherPo.getTeacherName();
    }

    public Teacher(UserVo studentVo){
        setGender(studentVo.getGender());
        this.userNo = studentVo.getUserNo();
        this.password = AES.encrypt(studentVo.getPassword(), AES_PASS);
        this.realName = studentVo.getRealName();
        if(studentVo.getEmail() != null) {
            this.email = AES.encrypt(studentVo.getEmail(), AES_PASS);
        }
        if(studentVo.getMobile() != null) {
            this.mobile = AES.encrypt(studentVo.getMobile(), AES_PASS);
        }
    }

    public TeacherPo createTeacherPo(){
        TeacherPo teacherPo = new TeacherPo();
        teacherPo.setId(this.id);
        teacherPo.setTeacherNo(this.userNo);
        teacherPo.setPassword(this.password);
        teacherPo.setTeacherName(this.realName);
        teacherPo.setGender(this.gender);
        teacherPo.setEmail(this.email);
        teacherPo.setMobile(this.mobile);
        return teacherPo;
    }

    @Override
    public Object createVo() {
        return new TeacherRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
