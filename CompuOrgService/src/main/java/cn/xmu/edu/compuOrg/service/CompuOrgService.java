package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.dao.TestDao;
import cn.xmu.edu.compuOrg.dao.UserDao;
import cn.xmu.edu.compuOrg.model.bo.TestResult;
import cn.xmu.edu.compuOrg.model.bo.Topic;
import cn.xmu.edu.compuOrg.model.bo.TopicAnswer;
import cn.xmu.edu.compuOrg.model.bo.User;
import cn.xmu.edu.compuOrg.model.po.TestResultPo;
import cn.xmu.edu.compuOrg.model.po.TopicPo;
import cn.xmu.edu.compuOrg.model.po.UserPo;
import cn.xmu.edu.compuOrg.model.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    private static final String registrationTitle = "【计算机组成原理平台】注册验证通知";
    private static final String verifyEmailTitle = "【计算机组成原理平台】邮箱验证通知";
    private static final String resetPasswordEmailTitle = "【计算机组成原理平台】重置密码通知";

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestDao testDao;

    /**
     * 用户登录
     * @author snow create 2021/03/27 20:36
     * @param loginVo
     * @return
     */
    public ReturnObject userLogin(UserLoginVo loginVo){
        ReturnObject<User> retObj = userDao.findUserByUserName(loginVo.getUserName());
        if(retObj.getData() == null){
            return retObj;
        }
        User user = retObj.getData();
        if(user.isSignatureBeenModify()){
            return new ReturnObject<>(ResponseCode.RESOURCE_FALSIFY);
        }
        if(user.getEmailVerify() == (byte)0){
            return new ReturnObject<>(ResponseCode.EMAIL_NOT_VERIFIED);
        }
        String password = AES.encrypt(loginVo.getPassword(), User.AES_PASS);
        if(!password.equals(user.getPassword())){
            return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
        String jwt = new JwtHelper().createToken(user.getId(), Byte.toUnsignedLong(user.getRole()), jwtExpireTime);
        if(adminDepartId.byteValue() == user.getRole()){
                jwt = "adm" + jwt;
        }
        else if(teacherDepartId.byteValue()  == user.getRole()){
            jwt = "tea" + jwt;
        }
        else if(studentDepartId.byteValue() == user.getRole()){
            jwt = "stu" + jwt;
        }
        else{
            jwt = "unk" + jwt;
        }
        return new ReturnObject<>(jwt);
    }

    /**
     * 学生注册
     * @author snow create 2021/01/17 21:40
     *            modified 2021/03/27 20:47
     * @param studentVo
     * @return
     */
    public ReturnObject studentSignUp(UserVo studentVo){
        Long keys = userDao.getUserIdByVerifyCode(studentVo.getVerifyCode());
        if(keys == null){
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        User student = new User(studentVo);
        student.setRole(studentDepartId.byteValue());
        student.setSignature(student.createSignature());
        logger.debug(student.toString());
        return userDao.insertUser(student);
    }

    /**
     * 教师注册
     * @author snow create 2021/01/18 13:28
     *            modified 2021/03/27 21:09
     * @param teacherVo
     * @return
     */
    public ReturnObject teacherSignUp(Long departId, UserVo teacherVo){
        if(!teacherDepartId.equals(departId) && !adminDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        User teacher = new User(teacherVo);
        teacher.setRole(teacherDepartId.byteValue());
        teacher.setSignature(teacher.createSignature());
        return userDao.insertUser(teacher);
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
        User admin = new User(adminVo);
        admin.setRole(adminDepartId.byteValue());
        admin.setSignature(admin.createSignature());
        return userDao.insertUser(admin);
    }

    /**
     * 用户申请重置密码
     * @author snow create 2021/01/18 22:43
     *            modified 2021/01/23 17:23
     *            modified 2021/03/27 20:51
     * @param userVo
     * @param ip
     * @return
     */
    public ReturnObject userResetPassword(UserPasswordVo userVo, String ip){
        ReturnObject retObj = userDao.findUserByUserName(userVo.getUserName());
        if(retObj.getData() == null){
            return retObj;
        }
        User user = (User) retObj.getData();
        logger.debug("Pass: " + userVo.getEmail() + ", Store: " + user.getDecryptEmail());
        if(!userVo.getEmail().equals(user.getDecryptEmail())){
            return new ReturnObject(ResponseCode.EMAIL_WRONG);
        }

        if(userDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            String verifyCode = VerifyCode.generateVerifyCode(6);
            userDao.putVerifyCodeIntoRedis(verifyCode, user.getId().toString());
            String emailContent = "您正在【计算机组成原理平台】进行找回密码，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";
            return sendVerifyCode(resetPasswordEmailTitle, emailContent, userVo.getEmail());
        }
        else{
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }

    }

    /**
     * 用户验证密码
     * @author snow create 2021/03/22 22:21
     *            modified 2021/03/27 20:58
     * @param userId
     * @param oldPassword
     * @return
     */
    public ReturnObject userVerifyPassword(Long userId, String oldPassword){
        ReturnObject<User> retObj = userDao.findUserById(userId);
        if(retObj.getData() == null){
            return retObj;
        }
        User user = retObj.getData();
        if(AES.encrypt(oldPassword, User.AES_PASS).equals(user.getPassword())){
            String verifyCode = VerifyCode.generateVerifyCode(6);
            userDao.putVerifyCodeIntoRedis(verifyCode, user.getId().toString());
            return new ReturnObject(verifyCode);
        }
        else{
            return new ReturnObject(ResponseCode.AUTH_INVALID_ACCOUNT);
        }
    }

    /**
     * 用户验证验证码
     * @author snow create 2021/03/27 21:30
     * @param verifyCodeVo
     * @return
     */
    public ReturnObject userVerifyCode(VerifyCodeVo verifyCodeVo){
        Long userId = userDao.getUserIdByVerifyCode(verifyCodeVo.getVerifyCode());
        if(userId == null){
            System.out.println("Can't find anything in redis with: " + verifyCodeVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        String verifyKey = VerifyCode.generateVerifyCode(6) + LocalDateTime.now();
        userDao.putVerifyCodeIntoRedis(verifyKey, userId.toString());
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(verifyCodeVo.getVerifyCode());
        return new ReturnObject(verifyKey);
    }

    /**
     * 用户修改密码
     * @author snow create 2021/01/18 00:26
     *            modified 2021/01/23 16:56
     *            modified 2021/03/27 21:00
     * @param modifyPasswordVo
     * @return
     */
    public ReturnObject userModifyPassword(UserModifyPasswordVo modifyPasswordVo){
        Long userId = userDao.getUserIdByVerifyCode(modifyPasswordVo.getVerifyCode());
        if(userId == null){
            System.out.println("Can't find anything in redis with: " + modifyPasswordVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        ReturnObject<User> retObj= userDao.findUserById(userId);
        if(retObj.getData() == null){
            return retObj;
        }
        User user = retObj.getData();
        String password = AES.encrypt(modifyPasswordVo.getPassword(), User.AES_PASS);
        if(password.equals(user.getPassword())){
            return new ReturnObject(ResponseCode.PASSWORD_SAME);
        }
        user.setPassword(password);
        user.setSignature(user.createSignature());
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(modifyPasswordVo.getVerifyCode());
        return userDao.updateUserInformation(user);
    }

    /**
     * 用户查找个人信息
     * @author snow create 2021/03/22 10:52
     *            modified 2021/03/27 21:11
     * @param userId
     * @return
     */
    public ReturnObject userGetBasicInformation(Long userId){
        return userDao.findUserById(userId);
    }

    /**
     * 管理员查看用户信息
     * @author snow create 2021/04/07 08:37
     * @param departId
     * @param role
     * @param userName
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> adminGetUserInformation(Long departId, Byte role, String userName, Integer page, Integer pageSize){
        if(!adminDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        PageHelper.startPage(page, pageSize);
        PageInfo<UserPo> userPo = userDao.findUsersInCondition(role, userName);
        if(userPo == null){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        List<VoObject> userInfos = userPo.getList().stream().map(User::new).filter(User::authentic).collect(Collectors.toList());

        PageInfo<VoObject> retObj = new PageInfo<>(userInfos);
        retObj.setPages(userPo.getPages());
        retObj.setPageNum(userPo.getPageNum());
        retObj.setPageSize(userPo.getPageSize());
        retObj.setTotal(userPo.getTotal());

        return new ReturnObject<>(retObj);
    }

    /**
     * 用户修改基础信息
     * @author snow create 2021/01/23 14:07
     *            modified 2021/01/23 17:00
     *            modified 2021/03/27 21:17
     * @param userId
     * @param userBasicInfoVo
     * @return
     */
    public ReturnObject userModifyBasicInformation(Long userId, UserBasicInfoVo userBasicInfoVo){
        ReturnObject<User> retObj = userDao.findUserById(userId);
        if (retObj.getData() == null){
            return retObj;
        }
        User user = retObj.getData();
        if(userBasicInfoVo.getUserName() != null){
            if(userBasicInfoVo.getUserName().equals(user.getUserName())){
                return new ReturnObject(ResponseCode.USER_NAME_SAME);
            }
            if(userDao.isUserNameAlreadyExist(userBasicInfoVo.getUserName())) {
                return new ReturnObject(ResponseCode.USER_NAME_REGISTERED);
            }
        }
        if(userBasicInfoVo.getMobile() != null){
            if(userBasicInfoVo.getMobile().equals(user.getDecryptMobile())){
                return new ReturnObject(ResponseCode.MOBILE_SAME);
            }
            if(userDao.isMobileAlreadyExist(AES.encrypt(userBasicInfoVo.getMobile(), User.AES_PASS))) {
                return new ReturnObject(ResponseCode.MOBILE_REGISTERED);
            }
        }
        user.updateUserInfo(userBasicInfoVo);
        return userDao.updateUserInformation(user);
    }

    /**
     * 用户验证旧邮箱
     * @author snow create 2021/01/23 16:32
     *            modified 2021/01/23 19:22
     *            modified 2021/03/27 21:23
     * @param userId
     * @param ip
     * @return
     */
    public ReturnObject userVerifyEmail(Long userId, String ip){
        ReturnObject<User> retObj = userDao.findUserById(userId);
        if(retObj.getData() == null){
            return retObj;
        }
        User user = retObj.getData();
        if(userDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            logger.debug("Ok!");
            String verifyCode = VerifyCode.generateVerifyCode(6);
            logger.debug("VerifyCode: " + verifyCode);
            userDao.putVerifyCodeIntoRedis(verifyCode, userId.toString());
            String emailContent = "您正在【计算机组成原理平台】进行邮箱验证，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";
            logger.debug(emailContent);
            return sendVerifyCode(verifyEmailTitle, emailContent, user.getDecryptEmail());
        }
        else{
            logger.debug("busy try!");
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }
    }

    /**
     * 用户验证邮箱
     * @author snow create 2021/03/27 22:25
     *            modified 2021/03/28 21:15
     *            modified 2021/04/06 15:15
     * @param userId
     * @param email
     * @param ip
     * @return
     */
    public ReturnObject userVerifyEmail(String userId, String email, String ip){
        if(userDao.isAllowRequestForVerifyCode(ip)) {
            //生成验证码
            logger.debug("Ok!");
            String verifyCode = VerifyCode.generateVerifyCode(6);
            logger.debug("VerifyCode: " + verifyCode);
            userDao.putVerifyCodeIntoRedis(verifyCode, userId);
            String emailContent, title;
            if("-3835".equals(userId)){
                if(userDao.isEmailAlreadyExist(email)){
                    return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
                }
                title = registrationTitle;
                emailContent = "您正在【计算机组成原理平台】进行注册，您的验证码为：" + verifyCode + "，请于5分钟内完成注册！";
            }
            else{
                title = verifyEmailTitle;
                emailContent = "您正在【计算机组成原理平台】进行邮箱验证，您的验证码为：" + verifyCode + "，请于5分钟内完成验证！";
            }
//            logger.debug(emailContent);
            return sendVerifyCode(title, emailContent, email);
        }
        else{
            logger.debug("busy try!");
            return new ReturnObject(ResponseCode.AUTH_USER_FORBIDDEN);
        }

    }

    /**
     * 用户修改邮箱
     * @author snow create 2021/01/23 16:57
     *            modified 2021/03/27 21:25
     *            modified 2021/03/28 21:28
     * @param userVo
     * @return
     */
    public ReturnObject userModifyEmail(Long userId, UserModifyEmailVo userVo){
        Long redisValue = userDao.getUserIdByVerifyCode(userVo.getVerifyCode());
        if(redisValue == null){
            System.out.println("Can't find anything in redis with: " + userVo.getVerifyCode());
            return new ReturnObject(ResponseCode.VERIFY_CODE_EXPIRE);
        }
        ReturnObject<User> retObj= userDao.findUserById(userId);
        if(retObj.getData() == null){
            return retObj;
        }
        User user = retObj.getData();
        String email = AES.encrypt(userVo.getEmail(), User.AES_PASS);
        if(email.equals(user.getEmail())){
            return new ReturnObject(ResponseCode.EMAIL_SAME);
        }
        if(userDao.isEmailAlreadyExist(email)){
            return new ReturnObject(ResponseCode.EMAIL_REGISTERED);
        }
        user.setEmail(email);
        user.setSignature(user.createSignature());
        userDao.disableVerifyCodeAfterSuccessfullyModifyPassword(userVo.getVerifyCode());
        return userDao.updateUserInformation(user);
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
    public ReturnObject sendVerifyCode(String title, String content, String toEmailAddress){
        try{

            //发送邮件
            SendEmail.sendEmail(toEmailAddress, title, content);
            return new ReturnObject(ResponseCode.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 随机生成测试题目
     * @author snow create 2021/01/28 16:00
     *            modified 2021/03/25 11:08
     * @param studentId
     * @param departId
     * @param experimentId
     * @param size
     * @return
     */
    public ReturnObject generateTest(Long studentId, Long departId, Long experimentId, Long size){
        ReturnObject retObj = getTestResultDetailByExperimentId(studentId, departId, experimentId);
        logger.error(retObj.getCode().toString());
        if(retObj.getData() != null){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
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
     * 教师删除题目
     * @author snow create 2021/01/28 13:48
     * @param departId
     * @param topicId
     * @return
     */
    public ReturnObject removeTopic(Long departId, Long topicId){
        if(studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        return testDao.deleteTopic(topicId);
    }

    /**
     * 教师修改题目
     * @author snow create 2021/01/28 13:50
     * @param departId
     * @param topicId
     * @param topicVo
     * @return
     */
    public ReturnObject modifyTopic(Long departId, Long topicId, TopicVo topicVo){
        if(studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        Topic topic = new Topic(topicVo);
        topic.setId(topicId);
        return testDao.alterTopic(topic);

    }

    /**
     * 教师查询题目列表
     * @author snow create 2021/01/28 14:34
     * @param departId
     * @param experimentId
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getTopicList(Long departId, Long experimentId,
                                                         Integer page, Integer pageSize){
        if(studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        PageHelper.startPage(page, pageSize);
        PageInfo<TopicPo> topicPos = testDao.findTopicList(experimentId);
        if(topicPos == null){
            return new ReturnObject<>(ResponseCode.NO_MORE_TOPIC);
        }
        List<VoObject> topicList = topicPos.getList().stream().map(Topic::new).filter(Topic::authentic).collect(Collectors.toList());

        PageInfo<VoObject> retObj = new PageInfo<>(topicList);
        retObj.setPages(topicPos.getPages());
        retObj.setPageNum(topicPos.getPageNum());
        retObj.setPageSize(topicPos.getPageSize());
        retObj.setTotal(topicPos.getTotal());

        return new ReturnObject<>(retObj);
    }

    /**
     * 学生提交测试结果
     * @author snow create 2021/01/25 22:25
     *            modified 2021/01/25 23:43
     *            modified 2021/01/28 13:27
     *            modified 2021/04/07 17:40
     * @param studentId
     * @param departId
     * @param testVo
     * @return
     */
    public ReturnObject commitTestResult(Long studentId, Long departId, TestVo testVo){
        ReturnObject retObj = getTestResultDetailByExperimentId(studentId, departId, testVo.getExperimentId());
        if(retObj.getData() != null){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        TestResult testResult = new TestResult();
        testResult.setStudentId(studentId);
        testResult.setExperimentId(testVo.getExperimentId());
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
     *            modified 2021/03/25 10:24
     * @param departId
     * @param userId
     * @param experimentId
     * @param studentId
     * @param modified
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getTestResultList(Long departId, Long userId,
                                                        Long experimentId, Long studentId, Boolean modified,
                                                        Integer page, Integer pageSize){
        if(studentDepartId.equals(departId) && !userId.equals(studentId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        PageHelper.startPage(page, pageSize);
        PageInfo<TestResultPo> testResultPo = testDao.findTestResult(experimentId, studentId, modified);
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
     * 根据实验序号获得测试结果详情
     * @author snow create 2021/03/21 18:18
     * @param studentId
     * @param departId
     * @param experimentId
     * @return
     */
    public ReturnObject getTestResultDetailByExperimentId(Long studentId, Long departId, Long experimentId){
        if(!studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        }
        ReturnObject<TestResult> retObj = testDao.findTestResultByExperimentId(studentId, experimentId);
        if(retObj.getData() == null){
            return retObj;
        }
        TestResult testResult = retObj.getData();
        testResult.setTopicAnswers(testDao.findTopicAnswerByTestResultId(testResult.getId()));
        return new ReturnObject(testResult);
    }

    /**
     * 教师提交测试结果评分
     * @author snow create 2021/01/27 23:03
     *            modified 2021/04/18 20:02
     * @param departId 角色id
     * @param testResultId 测试结果id
     * @param testResultScore 得分与评语
     * @return 操作结果
     */
    @Transactional
    public ReturnObject commitTestResultScore(Long departId, Long testResultId, TestResultScoreVo testResultScore){
        if(studentDepartId.equals(departId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        Integer totalScore = 0;
        ReturnObject retObj;
        for(TopicAnswerScoreVo topicAnswerScore : testResultScore.getTopicAnswerScores()){
            totalScore += topicAnswerScore.getScore();
            retObj = testDao.updateTopicAnswerScore(topicAnswerScore.getTopicAnswerId(),
                    topicAnswerScore.getScore(), topicAnswerScore.getComment());
            if(retObj.getCode() != ResponseCode.OK){
                return retObj;
            }
        }
        return testDao.updateTestResultScore(testResultId, totalScore);
    }
}
