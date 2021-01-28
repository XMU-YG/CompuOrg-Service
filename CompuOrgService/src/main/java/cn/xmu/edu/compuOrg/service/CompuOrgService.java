package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.dao.*;
import cn.xmu.edu.compuOrg.model.bo.*;
import cn.xmu.edu.compuOrg.model.po.TestResultPo;
import cn.xmu.edu.compuOrg.model.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompuOrgService {

    private  static  final Logger logger = LoggerFactory.getLogger(CompuOrgService.class);

    @Value("${CompuOrgService.student.login.jwtExpire}")
    private Integer jwtExpireTime;

    @Value("${CompuOrgService.admin.departId}")
    private Long adminDepartId;

    @Value("${CompuOrgService.student.departId}")
    private Long studentDepartId;

    @Value("${CompuOrgService.teacher.departId}")
    private Long teacherDepartId;

    private static final String verifyEmailTitle = "【计算机组成原理平台】邮箱验证通知";
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

    @Autowired
    private TestDao testDao;

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
     * 管理员新建管理员
     * @author snow create 2021/01/23 18:44
     * @param departId
     * @param adminVo
     * @return
     */
    public ReturnObject appendAdmin(Long departId, UserVo adminVo){
        if(!adminDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        Admin admin = new Admin(adminVo);
        return adminDao.insertAdmin(admin);
    }

    /**
     * 用户申请重置密码
     * @author snow create 2021/01/18 22:43
     *            modified 2021/01/23 17:23
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
        logger.debug("Pass: " + email + ", Store: " + user.getDecryptEmail());
        if(!email.equals(user.getDecryptEmail())){
            return new ReturnObject(ResponseCode.EMAIL_WRONG);
        }

        if(userDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            String verifyCode = VerifyCode.generateVerifyCode(6);
            studentDao.putVerifyCodeIntoRedis(verifyCode, user.getId().toString());
            String emailContent = "您正在【计算机组成原理平台】进行找回密码，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";
            sendVerifyCode(resetPasswordEmailTitle, emailContent, email);
            return new ReturnObject(ResponseCode.OK);
        }
        else{
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }

    }

    /**
     * 管理员申请重置密码
     * @author snow create 2021/01/23 19:13
     * @param adminVo
     * @param ip
     * @return
     */
    public ReturnObject adminResetPassword(UserPasswordVo adminVo, String ip){
        return userResetPassword(adminDao.findAdminBySno(adminVo.getUserNo()), adminVo.getEmail(), ip);
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
     * 管理员修改密码
     * @author snow create 2021/01/23 19:16
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject adminModifyPassword(UserModifyPasswordVo modifyPasswordVo){
        Long adminId = userDao.getUserIdByVerifyCode(modifyPasswordVo.getVerifyCode());
        if(adminId == null){
            System.out.println("Can't find anything in redis with: " + modifyPasswordVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        ReturnObject retObj = adminDao.findAdminById(adminId);
        if(retObj.getData() == null){
            return retObj;
        }
        Admin admin = (Admin) retObj.getData();
        if(!modifyPasswordVo.getUserNo().equals(admin.getUserNo())){
            System.out.println("Pass: " + modifyPasswordVo.getUserNo() + ", Store: " + admin.getUserNo());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String password = AES.encrypt(modifyPasswordVo.getPassword(), Student.AES_PASS);
        if(password.equals(admin.getPassword())){
            return new ReturnObject(ResponseCode.PASSWORD_SAME);
        }
        admin.setPassword(password);
        admin.setSignature(admin.createSignature());
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(modifyPasswordVo.getVerifyCode());
        return adminDao.updateAdminInformation(admin);
    }

    /**
     * 学生修改密码
     * @author snow create 2021/01/18 00:26
     *            modified 2021/01/23 16:56
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject studentModifyPassword(UserModifyPasswordVo modifyPasswordVo){
        Long studentId = studentDao.getUserIdByVerifyCode(modifyPasswordVo.getVerifyCode());
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
        return studentDao.updateStudentInformation(student);
    }

    /**
     * 教师修改密码
     * @author snow create 2021/01/18 22:59
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject teacherModifyPassword(UserModifyPasswordVo modifyPasswordVo){
        Long teacherId = teacherDao.getUserIdByVerifyCode(modifyPasswordVo.getVerifyCode());
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
        return teacherDao.updateTeacherInformation(teacher);
    }

    /**
     * 管理员修改基础信息
     * @author snow create 2021/01/23 19:18
     * @param adminId
     * @param userBasicInfoVo
     * @return
     */
    public ReturnObject adminModifyBasicInformation(Long adminId, UserBasicInfoVo userBasicInfoVo){
        if(userBasicInfoVo.getUserNo() != null && adminDao.isAdminNoAlreadyExist(userBasicInfoVo.getUserNo())){
            return new ReturnObject(ResponseCode.TEACHER_NO_REGISTERED);
        }
        if(userBasicInfoVo.getMobile() != null && adminDao.isMobileAlreadyExist(AES.encrypt(userBasicInfoVo.getMobile(), User.AES_PASS))){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        ReturnObject retObj = adminDao.findAdminById(adminId);
        if (retObj.getData() == null){
            return retObj;
        }
        Admin admin = (Admin) retObj.getData();
        admin.updateUserInfo(userBasicInfoVo);
        admin.setSignature(admin.createSignature());
        return adminDao.updateAdminInformation(admin);
    }

    /**
     * 学生修改基础信息
     * @author snow create 2021/01/23 14:07
     *            modified 2021/01/23 17:00
     * @param studentId
     * @param userBasicInfoVo
     * @return
     */
    public ReturnObject studentModifyBasicInformation(Long studentId, UserBasicInfoVo userBasicInfoVo){
        if(userBasicInfoVo.getUserNo() != null && studentDao.isStudentNoAlreadyExist(userBasicInfoVo.getUserNo())){
            return new ReturnObject(ResponseCode.STUDENT_NO_REGISTERED);
        }
        if(userBasicInfoVo.getMobile() != null && studentDao.isMobileAlreadyExist(AES.encrypt(userBasicInfoVo.getMobile(), User.AES_PASS))){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        ReturnObject retObj = studentDao.findStudentById(studentId);
        if (retObj.getData() == null){
            return retObj;
        }
        Student student = (Student)retObj.getData();
        student.updateUserInfo(userBasicInfoVo);
        return studentDao.updateStudentInformation(student);
    }

    /**
     * 教师修改基础信息
     * @author snow create 2021/01/23 17:54
     * @param teacherId
     * @param userBasicInfoVo
     * @return
     */
    public ReturnObject teacherModifyBasicInformation(Long teacherId, UserBasicInfoVo userBasicInfoVo){
        if(userBasicInfoVo.getUserNo() != null && teacherDao.isTeacherNoAlreadyExist(userBasicInfoVo.getUserNo())){
            return new ReturnObject(ResponseCode.TEACHER_NO_REGISTERED);
        }
        if(userBasicInfoVo.getMobile() != null && teacherDao.isMobileAlreadyExist(AES.encrypt(userBasicInfoVo.getMobile(), User.AES_PASS))){
            return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
        }
        ReturnObject retObj = teacherDao.findTeacherById(teacherId);
        if (retObj.getData() == null){
            return retObj;
        }
        Teacher teacher = (Teacher) retObj.getData();
        teacher.updateUserInfo(userBasicInfoVo);
        return teacherDao.updateTeacherInformation(teacher);
    }

    /**
     * 用户验证邮箱
     * @author snow create 2021/01/23 16:32
     *            modified 2021/01/23 19:22
     * @param retObj
     * @param userId
     * @param ip
     * @return
     */
    public ReturnObject userVerifyEmail(ReturnObject retObj, Long userId, String ip){
        if(retObj.getData() == null){
            return retObj;
        }
        User user = (User) retObj.getData();
        if(userDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            logger.debug("Ok!");
            String verifyCode = VerifyCode.generateVerifyCode(6);
            logger.debug("VerifyCode: " + verifyCode);
            studentDao.putVerifyCodeIntoRedis(verifyCode, userId.toString());
            String emailContent = "您正在【计算机组成原理平台】进行邮箱验证，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";
            logger.debug(emailContent);
            sendVerifyCode(verifyEmailTitle, emailContent, user.getDecryptEmail());
            return new ReturnObject(ResponseCode.OK);
        }
        else{
            logger.debug("busy try!");
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }
    }

    /**
     * 管理员验证邮箱
     * @author snow create 2021/01/23 19:23
     * @param adminId
     * @param ip
     * @return
     */
    public ReturnObject adminVerifyEmail(long adminId, String ip){
        return userVerifyEmail(adminDao.findAdminById(adminId), adminId, ip);
    }

    /**
     * 学生验证邮箱
     * @author snow create 2021/01/23 16:33
     *            modified 2021/01/23 19:23
     * @param studentId
     * @param ip
     * @return
     */
    public ReturnObject studentVerifyEmail(long studentId, String ip){
        return userVerifyEmail(adminDao.findAdminById(studentId), studentId, ip);
    }

    /**
     * 教师验证邮箱
     * @author snow create 2021/01/23 16:34
     *            modified 2021/01/23 19:23
     * @param teacherId
     * @param ip
     * @return
     */
    public ReturnObject teacherVerifyEmail(long teacherId, String ip){
        return userVerifyEmail(adminDao.findAdminById(teacherId), teacherId, ip);
    }

    /**
     * 管理员修改邮箱
     * @author snow create 2021/01/23 19:26
     * @param userVo
     * @return
     */
    public ReturnObject adminModifyEmail(UserModifyEmailVo userVo){
        Long adminId = userDao.getUserIdByVerifyCode(userVo.getVerifyCode());
        if(adminId == null){
            System.out.println("Can't find anything in redis with: " + userVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String email = AES.encrypt(userVo.getEmail(), User.AES_PASS);
        if(adminDao.isEmailAlreadyExist(email)){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        ReturnObject retObj= adminDao.findAdminById(adminId);
        if(retObj.getData() == null){
            return retObj;
        }
        Admin admin = (Admin) retObj.getData();
        admin.setEmail(email);
        admin.setEmailVerify((byte)0);
        admin.setSignature(admin.createSignature());
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(userVo.getVerifyCode());
        return adminDao.updateAdminInformation(admin);
    }

    /**
     * 学生修改邮箱
     * @author snow create 2021/01/23 16:57
     * @param userVo
     * @return
     */
    public ReturnObject studentModifyEmail(UserModifyEmailVo userVo){
        Long studentId = userDao.getUserIdByVerifyCode(userVo.getVerifyCode());
        if(studentId == null){
            System.out.println("Can't find anything in redis with: " + userVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String email = AES.encrypt(userVo.getEmail(), User.AES_PASS);
        if(studentDao.isEmailAlreadyExist(email)){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        ReturnObject retObj= studentDao.findStudentById(studentId);
        if(retObj.getData() == null){
            return retObj;
        }
        Student student = (Student)retObj.getData();
        student.setEmail(email);
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(userVo.getVerifyCode());
        return studentDao.updateStudentInformation(student);
    }

    /**
     * 教师修改邮箱
     * @author snow create 2021/01/23 17:47
     * @param userVo
     * @return
     */
    public ReturnObject teacherModifyEmail(UserModifyEmailVo userVo){
        Long teacherId = userDao.getUserIdByVerifyCode(userVo.getVerifyCode());
        if(teacherId == null){
            System.out.println("Can't find anything in redis with: " + userVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String email = AES.encrypt(userVo.getEmail(), User.AES_PASS);
        if(studentDao.isEmailAlreadyExist(email)){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        ReturnObject retObj= teacherDao.findTeacherById(teacherId);
        if(retObj.getData() == null){
            return retObj;
        }
        Teacher teacher = (Teacher) retObj.getData();
        teacher.setEmail(email);
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(userVo.getVerifyCode());
        return teacherDao.updateTeacherInformation(teacher);
    }

    /**
     * 发送验证码
     * @author snow create 2021/01/17 22:52
     *            modified 2021/01/23 17:16
     * @param title
     * @param content
     * @param toEmailAddress
     * @return
     */
    public Boolean sendVerifyCode(String title, String content, String toEmailAddress){
        try{

            //发送邮件
            SendEmail.sendEmail(toEmailAddress, title, content);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 随机生成测试题目
     * @param experimentId
     * @param size
     * @return
     */
    public ReturnObject generateTest(Long experimentId, Long size){
        return testDao.getTest(experimentId, size);
    }

    /**
     * 教师增加题目
     * @author snow create 2021/01/28 10:22
     * @param departId
     * @param topicVo
     * @return
     */
    public ReturnObject appendTopic(Long departId, TopicVo topicVo){
        if(studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        Topic topic = new Topic(topicVo);
        return testDao.insertTopic(topic);
    }

    /**
     * 学生提交测试结果
     * @author snow create 2021/01/25 22:25
     *            modified 2021/01/25 23:43
     * @param studentId
     * @param experimentId
     * @param testVo
     * @return
     */
    public ReturnObject commitTestResult(Long studentId, Long experimentId, TestVo testVo){
        TestResult testResult = new TestResult();
        testResult.setStudentId(studentId);
        testResult.setExperimentId(experimentId);
        if (testDao.insertTestResult(testResult)) {
            List<TopicAnswer> topicAnswers = new ArrayList<>();
            for (TopicAnswerVo topicAnswerVo : testVo.getTopicAnswerVos()) {
                TopicAnswer topicAnswer = new TopicAnswer(topicAnswerVo);
                topicAnswer.setTestResultId(testResult.getId());
                if (testDao.insertTopicAnswer(topicAnswer)) {
                    topicAnswers.add(topicAnswer);
                }
                else {
                    return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
                }
            }
            testResult.setTopicAnswers(topicAnswers);
            return new ReturnObject(testResult);
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 获取测试结果列表
     * @author snow create 2021/01/25 23:15
     *            modified 2021/01/28 12:43
     * @param departId
     * @param userId
     * @param experimentId
     * @param studentId
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getTestResultList(Long departId, Long userId,
                                                        Long experimentId, Long studentId,
                                                        Integer page, Integer pageSize){
        if(studentDepartId.equals(departId) && !userId.equals(studentId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        PageHelper.startPage(page, pageSize);
        PageInfo<TestResultPo> testResultPo = testDao.findTestResultByExperimentId(experimentId, studentId);
        if(testResultPo == null){
            return new ReturnObject<>(ResponseCode.AUTH_NEED_LOGIN);
        }
        List<VoObject> testResultBrief = testResultPo.getList().stream().map(TestResult::new).filter(TestResult::authentic).collect(Collectors.toList());

        PageInfo<VoObject> retObj = new PageInfo<>(testResultBrief);
        retObj.setPages(testResultPo.getPages());
        retObj.setPageNum(testResultPo.getPageNum());
        retObj.setPageSize(testResultPo.getPageSize());
        retObj.setTotal(testResultPo.getTotal());

        return new ReturnObject<>(retObj);
    }

    /**
     * 根据测试结果id获得测试结果详情
     * @author snow create 2021/01/25 23:24
     * @param userId
     * @param departId
     * @param testResultId
     * @return
     */
    public ReturnObject getTestResultDetailByTestResultId(Long userId, Long departId, Long testResultId){
        ReturnObject retObj = testDao.findTestResultById(testResultId);
        if(retObj.getData() != null){
            TestResult testResult = (TestResult)retObj.getData();
            if(studentDepartId.equals(departId) && !userId.equals(testResult.getStudentId())){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
            testResult.setTopicAnswers(testDao.findTopicAnswerByTestResultId(testResultId));
            return new ReturnObject(testResult);
        }
        return retObj;
    }

    /**
     * 教师提交测试结果评分
     * @author snow create 2021/01/27 23:03
     * @param departId
     * @param testResultScore
     * @return
     */
    public ReturnObject commitTestResultScore(Long departId, TestResultScoreVo testResultScore){
        if(studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        Integer totalScore = 0;
        ReturnObject retObj = null;
        for(TopicAnswerScoreVo topicAnswerScore : testResultScore.getTopicAnswerScores()){
            totalScore += topicAnswerScore.getScore();
            retObj = testDao.updateTopicAnswerScore(topicAnswerScore.getTopicAnswerId(), topicAnswerScore.getScore());
            if(retObj.getCode() != ResponseCode.OK){
                return retObj;
            }
        }
        return testDao.updateTestResultScore(testResultScore.getTestResultId(), totalScore);
    }
}
