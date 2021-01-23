package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.dao.*;
import cn.xmu.edu.compuOrg.model.bo.Admin;
import cn.xmu.edu.compuOrg.model.bo.Student;
import cn.xmu.edu.compuOrg.model.bo.Teacher;
import cn.xmu.edu.compuOrg.model.bo.User;
import cn.xmu.edu.compuOrg.model.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CompuOrgService {

    @Value("${CompuOrgService.student.login.jwtExpire}")
    private Integer jwtExpireTime;

    @Value("${CompuOrgService.admin.departId}")
    private Long adminDepartId;

    @Value("${CompuOrgService.student.departId}")
    private Long studentDepartId;

    @Value("${CompuOrgService.teacher.departId}")
    private Long teacherDepartId;

    private static final String resetPasswordEmailTitle = "【计算机组成原理平台】重置密码通知";

    @Autowired
    private ExperimentLinesDao experimentLinesDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private TeacherDao teacherDao;

    /**
     * 检验实验单个连线
     * @author snow create 2021/01/12 20:26
     * @param experimentId
     * @param lineVo
     * @return
     */
    public ReturnObject connectLines(Integer experimentId, LineVo lineVo){
        if(experimentLinesDao.validSingleConnection(experimentId, lineVo)){
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
     * @param linesVo
     * @return
     */
    public ReturnObject validAllLines(Integer experimentId, LinesVo linesVo){
        if(experimentLinesDao.validAllLines(experimentId, linesVo)){
            return new ReturnObject(ResponseCode.OK);
        }
        else {
            return new ReturnObject(ResponseCode.LINE_CONNECT_ERROR);
        }
    }

    /**
     * 用户登录
     * @author snow create 2021/01/18 12:56
     * @param password
     * @param departId
     * @param retObj
     * @return
     */
    public ReturnObject<String> userLogin(String password, Long departId, ReturnObject retObj){
        if(retObj.getData() == null){
            return retObj;
        }
        User user = (User) retObj.getData();
        password = AES.encrypt(password, User.AES_PASS);
        if(user == null || !password.equals(user.getPassword())){
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        String jwt = new JwtHelper().createToken(user.getId(), departId, jwtExpireTime);
        return new ReturnObject<>(jwt);
    }

    /**
     * 管理员登录
     * @author snow create 2021/01/19 00:28
     *            modified 2021/01/19 00:43
     * @param adminNo
     * @param password
     * @return
     */
    public ReturnObject<String> adminLogin(String adminNo, String password){
        ReturnObject retObj = adminDao.findAdminBySno(adminNo);
        if(retObj.getData() == null){
            return retObj;
        }
        Admin admin = (Admin) retObj.getData();
        if(admin.isSignatureBeenModify()){
            return new ReturnObject<>(ResponseCode.RESOURCE_FALSIFY);
        }
        if(admin.getEmailVerify() == (byte)0){
            return new ReturnObject<>(ResponseCode.EMAIL_NOT_VERIFIED);
        }
        password = AES.encrypt(password, User.AES_PASS);
        if(admin == null || !password.equals(admin.getPassword())){
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        String jwt = new JwtHelper().createToken(admin.getId(), adminDepartId, jwtExpireTime);
        return new ReturnObject<>(jwt);
    }

    /**
     * 学生登录
     * @author snow create 2021/01/17 18:45
     * @param studentNo
     * @param password
     * @return
     */
    public ReturnObject<String> studentLogin(String studentNo, String password){
        return userLogin(password, studentDepartId, studentDao.findStudentBySno(studentNo));
    }

    /**
     * 教师登录
     * @author snow create 2021/01/18 12:59
     * @param teacherNo
     * @param password
     * @return
     */
    public ReturnObject<String> teacherLogin(String teacherNo, String password){
        return userLogin(password, teacherDepartId, teacherDao.findTeacherBySno(teacherNo));
    }

    /**
     * 学生注册
     * @author snow create 2021/01/17 21:40
     * @param studentVo
     * @return
     */
    public ReturnObject studentSignUp(UserVo studentVo){
        Student student = new Student(studentVo);
        return studentDao.insertStudent(student);
    }

    /**
     * 教师注册
     * @author snow create 2021/01/18 13:28
     * @param teacherVo
     * @return
     */
    public ReturnObject teacherSignUp(UserVo teacherVo){
        Teacher teacher = new Teacher(teacherVo);
        return teacherDao.insertTeacher(teacher);
    }

    /**
     * 用户申请重置密码
     * @author snow 2021/01/18 22:43
     * @param retObj
     * @param email
     * @param ip
     * @return
     */
    public ReturnObject userResetPassword(ReturnObject retObj, String email, String ip){
        if(retObj.getData() == null){
            return retObj;
        }
        User user = (User) retObj.getData();
        System.out.println("Pass: " + email + ", Store: " + user.getDecryptEmail());
        if(!email.equals(user.getDecryptEmail())){
            return new ReturnObject(ResponseCode.EMAIL_WRONG);
        }

        if(userDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            String verifyCode = VerifyCode.generateVerifyCode(6);
            studentDao.putVerifyCodeIntoRedis(verifyCode, user.getId().toString());
            sendVerifyCode(verifyCode, email);
            return new ReturnObject(ResponseCode.OK);
        }
        else{
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }

    }

    /**
     * 学生申请重置密码
     * @author snow create 2021/01/17 23:25
     * @param studentVo
     * @param ip
     * @return
     */
    public ReturnObject studentResetPassword(UserPasswordVo studentVo, String ip){
        return userResetPassword(studentDao.findStudentBySno(studentVo.getUserNo()), studentVo.getEmail(), ip);
    }

    /**
     * 教师申请重置密码
     * @author snow create 2021/01/18 22:46
     * @param teacherVo
     * @param ip
     * @return
     */
    public ReturnObject teacherResetPassword(UserPasswordVo teacherVo, String ip){
        return userResetPassword(teacherDao.findTeacherBySno(teacherVo.getUserNo()), teacherVo.getEmail(), ip);
    }

    /**
     * 学生修改密码
     * @author snow create 2021/01/18 00:26
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject studentModifyPassword(UserModifyPasswordVo modifyPasswordVo){
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
        if(!modifyPasswordVo.getUserNo().equals(student.getUserNo())){
            System.out.println("Pass: " + modifyPasswordVo.getUserNo() + ", Store: " + student.getUserNo());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String password = AES.encrypt(modifyPasswordVo.getPassword(), Student.AES_PASS);
        if(password.equals(student.getPassword())){
            return new ReturnObject(ResponseCode.PASSWORD_SAME);
        }
        student.setPassword(password);
        studentDao.disableVerifyCodeAfterSuccessfullyModifyPassword(modifyPasswordVo.getVerifyCode());
        return studentDao.updateStudentPassword(student);
    }

    /**
     * 教师修改密码
     * @author snow create 2021/01/18 22:59
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject teacherModifyPassword(UserModifyPasswordVo modifyPasswordVo){
        Long teacherId = teacherDao.getStudentIdByVerifyCode(modifyPasswordVo.getVerifyCode());
        if(teacherId == null){
            System.out.println("Can't find anything in redis with: " + modifyPasswordVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        ReturnObject retObj= teacherDao.findTeacherById(teacherId);
        if(retObj.getData() == null){
            return retObj;
        }
        Teacher teacher = (Teacher) retObj.getData();
        if(!modifyPasswordVo.getUserNo().equals(teacher.getUserNo())){
            System.out.println("Pass: " + modifyPasswordVo.getUserNo() + ", Store: " + teacher.getUserNo());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String password = AES.encrypt(modifyPasswordVo.getPassword(), Student.AES_PASS);
        if(password.equals(teacher.getPassword())){
            return new ReturnObject(ResponseCode.PASSWORD_SAME);
        }
        teacher.setPassword(password);
        teacherDao.disableVerifyCodeAfterSuccessfullyModifyPassword(modifyPasswordVo.getVerifyCode());
        return teacherDao.updateTeacherPassword(teacher);
    }

    /**
     * 学生修改基础信息
     * @author snow create 2021/01/23 14:07
     * @param studentId
     * @param userBasicInfoVo
     * @return
     */
    public ReturnObject studentModifyBasicInformation(Long studentId, UserBasicInfoVo userBasicInfoVo){
        return studentDao.updateStudentInfo(studentId, userBasicInfoVo);
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
}
