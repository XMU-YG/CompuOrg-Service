package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.dao.ExperimentLinesDao;
import cn.xmu.edu.compuOrg.dao.StudentDao;
import cn.xmu.edu.compuOrg.model.bo.Student;
import cn.xmu.edu.compuOrg.model.vo.LinesVo;
import cn.xmu.edu.compuOrg.model.vo.StudentModifyPasswordVo;
import cn.xmu.edu.compuOrg.model.vo.StudentResetPasswordVo;
import cn.xmu.edu.compuOrg.model.vo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompuOrgService {

    @Value("${CompuOrgService.student.login.jwtExpire}")
    private Integer jwtExpireTime;

    @Value("${CompuOrgService.student.departId}")
    private Long studentDepartId;

    private static final String resetPasswordEmailTitle = "【计算机组成原理平台】重置密码通知";

    @Autowired
    private ExperimentLinesDao experimentLinesDao;

    @Autowired
    private StudentDao studentDao;

    /**
     * 检验实验单个连线
     * @author snow create 2021/01/12 20:26
     * @param experimentId
     * @param linesVo
     * @return
     */
    public ReturnObject connectLines(Integer experimentId, LinesVo linesVo){
        if(experimentLinesDao.validSingleConnection(experimentId, linesVo)){
            return new ReturnObject(ResponseCode.OK);
        }
        else{
            return new ReturnObject(ResponseCode.LINE_ENDS_NOT_VALID);
        }
    }

    /**
     * 校验实验所有连线
     * @author snow create 2021/01/12 20:50
     * @param experimentId
     * @param linesVos
     * @return
     */
    public ReturnObject validAllLines(Integer experimentId, List<LinesVo> linesVos){
        if(experimentLinesDao.validAllLines(experimentId, linesVos)){
            return new ReturnObject(ResponseCode.OK);
        }
        else {
            return new ReturnObject(ResponseCode.LINE_CONNECT_ERROR);
        }
    }

    /**
     * 学生登录
     * @author snow create 2021/01/17 18:45
     * @param studentNo
     * @param password
     * @return
     */
    public ReturnObject<String> studentLogin(String studentNo, String password){
        ReturnObject retObj = studentDao.findStudentBySno(studentNo);
        if(retObj.getData() == null){
            return retObj;
        }
        Student student = (Student)retObj.getData();
        password = AES.encrypt(password, Student.AES_PASS);
        if(student == null || !password.equals(student.getPassword())){
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        String jwt = new JwtHelper().createToken(student.getId(), studentDepartId, jwtExpireTime);
        return new ReturnObject<>(jwt);
    }

    /**
     * 学生注册
     * @author snow create 2021/01/17 21:40
     * @param studentVo
     * @return
     */
    public ReturnObject studentSignUp(StudentVo studentVo){
        Student student = new Student(studentVo);
        return studentDao.insertStudent(student);
    }

    /**
     * 学生申请重置密码
     * @author snow create 2021/01/17 23:25
     * @param studentVo
     * @param ip
     * @return
     */
    public ReturnObject studentResetPassword(StudentResetPasswordVo studentVo, String ip){

        ReturnObject retObj= studentDao.findStudentBySno(studentVo.getStudentNo());
        if(retObj.getData() == null){
            return retObj;
        }
        Student student = (Student)retObj.getData();
        System.out.println("Pass: " + studentVo.getEmail() + ", Store: " + student.getDecryptEmail());
        if(!studentVo.getEmail().equals(student.getDecryptEmail())){
            return new ReturnObject(ResponseCode.EMAIL_WRONG);
        }

        if(studentDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            String verifyCode = VerifyCode.generateVerifyCode(6);
            studentDao.putVerifyCodeIntoRedis(verifyCode, student.getId().toString());
            sendVerifyCode(verifyCode, studentVo.getEmail());
            return new ReturnObject(ResponseCode.OK);
        }
        else{
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }
    }

    /**
     * 发送验证码
     * @author snow create 2021/01/17 22:52
     * @param verifyCode
     * @param toEmailAddress
     * @return
     */
    public Boolean sendVerifyCode(String verifyCode, String toEmailAddress){
        try{

            //邮件内容
            String emailContent = "您正在【计算机组成原理平台】进行找回密码，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";

            //发送邮件
            SendEmail.sendEmail(toEmailAddress, resetPasswordEmailTitle, emailContent);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 学生修改密码
     * @author snow create 2021/01/18 00:26
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject studentModifyPassword(StudentModifyPasswordVo modifyPasswordVo){
        Long studentId = studentDao.getStudentIdByVerifyCode(modifyPasswordVo.getVerifyCode());
        if(studentId == null){
            System.out.println("Can't find anything in redis with: " + modifyPasswordVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        ReturnObject retObj= studentDao.findStudentById(studentId);
        if(retObj.getData() == null){
            return retObj;
        }
        Student student = (Student)retObj.getData();
        if(!modifyPasswordVo.getStudentNo().equals(student.getStudentNo())){
            System.out.println("Pass: " + modifyPasswordVo.getStudentNo() + ", Store: " + student.getStudentNo());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String password = AES.encrypt(modifyPasswordVo.getPassword(), Student.AES_PASS);
        if(password.equals(student.getPassword())){
            return new ReturnObject(ResponseCode.PASSWORD_SAME);
        }
        student.setPassword(password);
        return studentDao.updateStudentPassword(student);
    }
}
