package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.AES;
import cn.xmu.edu.compuOrg.model.po.StudentPo;
import cn.xmu.edu.compuOrg.model.vo.StudentRetVo;
import cn.xmu.edu.compuOrg.model.vo.UserVo;
import lombok.Data;

import java.io.Serializable;

@Data
public class Student extends User implements VoObject, Serializable {

    public Student(StudentPo studentPo){
        this.id = studentPo.getId();
        this.gender = studentPo.getGender();
        this.email = studentPo.getEmail();
        this.mobile = studentPo.getMobile();
        this.userNo = studentPo.getStudentNo();
        this.password = studentPo.getPassword();
        this.realName = studentPo.getStudentName();
    }

    public Student(UserVo studentVo){
        setGender(studentVo.getGender());
        this.userNo = studentVo.getUserNo();
        this.password = AES.encrypt(studentVo.getPassword(), AES_PASS);
        this.realName = studentVo.getStudentName();
        if(studentVo.getEmail() != null) {
            this.email = AES.encrypt(studentVo.getEmail(), AES_PASS);
        }
        if(studentVo.getMobile() != null) {
            this.mobile = AES.encrypt(studentVo.getMobile(), AES_PASS);
        }
    }

    public StudentPo createStudentPo(){
        StudentPo studentPo = new StudentPo();
        studentPo.setId(this.id);
        studentPo.setStudentNo(this.userNo);
        studentPo.setPassword(this.password);
        studentPo.setStudentName(this.realName);
        studentPo.setGender(this.gender);
        studentPo.setEmail(this.email);
        studentPo.setMobile(this.mobile);
        return studentPo;
    }

    @Override
    public Object createVo() {
        return new StudentRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
