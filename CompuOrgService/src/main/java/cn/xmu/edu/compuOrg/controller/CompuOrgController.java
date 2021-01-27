package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.annotation.Audit;
import cn.xmu.edu.Core.annotation.Depart;
import cn.xmu.edu.Core.annotation.LoginUser;
import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.model.vo.*;
import cn.xmu.edu.compuOrg.service.CompuOrgService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "计组项目后端", tags = "compuOrg")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class CompuOrgController {

    private  static  final Logger logger = LoggerFactory.getLogger(CompuOrgController.class);

    @Autowired
    private CompuOrgService compuOrgService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 检验单个实验单个连线
     * @author snow create 2021/01/12 20:23
     *            modified 2021/01/18 13:15
     * @param eid
     * @param lineVo
     * @return
     */
    @ApiOperation(value = "实验连线", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "eid", value = "实验序号", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "LineVo", name = "lineVo", value = "连线端点", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 601, message = "本次实验中不存在这样的连线"),
    })
    @PostMapping("experiment/{eid}/lines/connect")
    public Object connectLines(@PathVariable(value = "eid") Integer eid,
                               @Validated @RequestBody LineVo lineVo,
                               BindingResult bindingResult){
        if(eid < 1 || eid > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        return Common.decorateReturnObject(compuOrgService.connectLines(eid, lineVo));
    }


    /**
     * 检验单个实验所有连线
     * @author snow create 2021/01/12 21:30
     *            modified 2021/01/18 13:15
     * @param eid
     * @param linesVos
     * @return
     */
    @ApiOperation(value = "连线检验", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "eid", value = "实验序号", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "LinesVo", name = "linesVo", allowMultiple = true, value = "连线", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 602, message = "存在错误布线"),
    })
    @PostMapping("experiment/{eid}/lines/validation")
    public Object validLines(@PathVariable(value = "eid") Integer eid,
                             @Validated @RequestBody LinesVo linesVos,
                             BindingResult bindingResult){
        if(eid < 1 || eid > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            return returnObject;
        }
        return Common.decorateReturnObject(compuOrgService.validAllLines(eid, linesVos));
    }

    /**
     * 学生登录
     * @author snow create 2021/01/15 20:00
     *            modified 2021/01/18 13:18
     * @param loginVo
     * @param bindingResult
     * @param httpServletResponse
     * @return
     */
    @ApiOperation(value = "学生登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserLoginVo", name = "loginVo", value = "学号与密码", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @PostMapping("student/login")
    public Object studentLogin(@Validated @RequestBody UserLoginVo loginVo,
                               BindingResult bindingResult,
                               HttpServletResponse httpServletResponse){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject<String> jwt = compuOrgService.studentLogin(loginVo.getUserNo(), loginVo.getPassword());

        if(jwt.getData() == null){
            return ResponseUtil.fail(jwt.getCode(), jwt.getErrmsg());
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok(jwt.getData());
        }
    }

    /**
     * 学生注册
     * @author snow create 2021/01/17 21:30
     *            modified 2021/01/18 13:20
     * @param studentVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生注册", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserVo", name = "studentVo", value = "学生注册信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 731, message = "学号已被注册"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
    })
    @PostMapping("student/registration")
    public Object studentSignUp(@Validated @RequestBody UserVo studentVo,
                                BindingResult bindingResult){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject retObj = compuOrgService.studentSignUp(studentVo);

        if(retObj.getData() == null){
            return Common.getNullRetObj(retObj, httpServletResponse);
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 学生找回密码
     * @author snow create 2021/01/17 23:30
     *            modified 2021/01/18 13:22
     * @param studentVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生找回密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserPasswordVo", name = "studentVo", value = "学生验证身份信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
    })
    @PutMapping("student/password/reset")
    public Object studentResetPassword(@Validated @RequestBody UserPasswordVo studentVo,
                                       BindingResult bindingResult,
                                       HttpServletRequest httpServletRequest){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.studentResetPassword(studentVo, ip));
    }

    /**
     * 学生修改密码
     * @author snow create 2021/01/17 23:30
     *            modified 2021/01/18 13:23
     * @param modifyPasswordVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyPasswordVo", name = "modifyPasswordVo", value = "修改密码对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 741, message = "新密码不能与旧密码相同"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("student/password")
    public Object studentModifyPassword(@Validated @RequestBody UserModifyPasswordVo modifyPasswordVo,
                                        BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.studentModifyPassword(modifyPasswordVo));
    }

    /**
     * 学生修改基本信息
     * @author snow create 2021/01/23 14:11
     * @param studentId
     * @param userBasicInfoVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生修改基本信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserBasicInfoVo", name = "userBasicInfoVo", value = "修改信息对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 731, message = "学号已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
    })
    @Audit
    @PutMapping("student/information")
    public Object studentModifyBasicInformation(@ApiIgnore @LoginUser Long studentId,
                                                @Validated @RequestBody UserBasicInfoVo userBasicInfoVo,
                                                BindingResult bindingResult){
        logger.debug("StudentId: " + studentId + "userInfo: " + userBasicInfoVo.toString());
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.studentModifyBasicInformation(studentId, userBasicInfoVo));
    }

    /**
     * 学生发送验证码验证邮箱
     * @author snow create 2021/01/23 16:41
     * @param studentId
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "学生发送验证码验证邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @Audit
    @GetMapping("student/email/verify")
    public Object studentVerifyEmail(@ApiIgnore @LoginUser Long studentId,
                                     HttpServletRequest httpServletRequest){
        logger.debug("StudentId: " + studentId);
        String ip = IpUtil.getIpAddr(httpServletRequest);

        return Common.decorateReturnObject(compuOrgService.studentVerifyEmail(studentId, ip));
    }

    /**
     * 学生修改邮箱
     * @author snow create 2021/01/23 16:58
     * @param userVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生修改邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyEmailVo", name = "userVo", value = "修改邮箱对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("student/email")
    public Object studentModifyEmail(@Validated @RequestBody UserModifyEmailVo userVo,
                                        BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.studentModifyEmail(userVo));
    }

    /**
     * 教师登录
     * @author snow create 2021/01/18 13:24
     * @param loginVo
     * @param bindingResult
     * @param httpServletResponse
     * @return
     */
    @ApiOperation(value = "教师登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserLoginVo", name = "loginVo", value = "工号与密码", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @PostMapping("teacher/login")
    public Object teacherLogin(@Validated @RequestBody UserLoginVo loginVo,
                               BindingResult bindingResult,
                               HttpServletResponse httpServletResponse){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject<String> jwt = compuOrgService.teacherLogin(loginVo.getUserNo(), loginVo.getPassword());

        if(jwt.getData() == null){
            return ResponseUtil.fail(jwt.getCode(), jwt.getErrmsg());
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok(jwt.getData());
        }
    }

    /**
     * 教师注册
     * @author snow create 2021/01/18 13:30
     * @param teacherVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师注册", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserVo", name = "teacherVo", value = "教师注册信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 734, message = "工号已被注册"),
    })
    @PostMapping("teacher/registration")
    public Object teacherSignUp(@Validated @RequestBody UserVo teacherVo,
                                BindingResult bindingResult){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject retObj = compuOrgService.teacherSignUp(teacherVo);

        if(retObj.getData() == null){
            return Common.getNullRetObj(retObj, httpServletResponse);
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 教师找回密码
     * @author snow create 2021/01/18 23:00
     * @param teacherVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师找回密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserPasswordVo", name = "teacherVo", value = "教师验证身份信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
    })
    @PutMapping("teacher/password/reset")
    public Object teacherResetPassword(@Validated @RequestBody UserPasswordVo teacherVo,
                                       BindingResult bindingResult,
                                       HttpServletRequest httpServletRequest){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.teacherResetPassword(teacherVo, ip));
    }

    /**
     * 教师修改密码
     * @author snow create 2021/01/18 23:01
     * @param modifyPasswordVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyPasswordVo", name = "modifyPasswordVo", value = "修改密码对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 741, message = "新密码不能与旧密码相同"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("teacher/password")
    public Object teacherModifyPassword(@Validated @RequestBody UserModifyPasswordVo modifyPasswordVo,
                                        BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.teacherModifyPassword(modifyPasswordVo));
    }

    /**
     * 教师修改基本信息
     * @author snow create 2021/01/23 14:11
     * @param teacherId
     * @param userBasicInfoVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师修改基本信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserBasicInfoVo", name = "userBasicInfoVo", value = "修改信息对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 734, message = "工号已被注册"),
    })
    @Audit
    @PutMapping("teacher/information")
    public Object teacherModifyBasicInformation(@ApiIgnore @LoginUser Long teacherId,
                                                @Validated @RequestBody UserBasicInfoVo userBasicInfoVo,
                                                BindingResult bindingResult){
        logger.debug("StudentId: " + teacherId + "userInfo: " + userBasicInfoVo.toString());
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        return Common.decorateReturnObject(compuOrgService.teacherModifyBasicInformation(teacherId, userBasicInfoVo));
    }

    /**
     * 教师发送验证码验证邮箱
     * @author snow create 2021/01/23 17:48
     * @param teacherId
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "教师发送验证码验证邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @Audit
    @GetMapping("teacher/email/verify")
    public Object teacherVerifyEmail(@ApiIgnore @LoginUser Long teacherId,
                                     HttpServletRequest httpServletRequest){
        logger.debug("TeacherId: " + teacherId);
        String ip = IpUtil.getIpAddr(httpServletRequest);

        return Common.decorateReturnObject(compuOrgService.teacherVerifyEmail(teacherId, ip));
    }

    /**
     * 教师修改邮箱
     * @author snow create 2021/01/23 17:50
     * @param userVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师修改邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyEmailVo", name = "userVo", value = "修改邮箱对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("teacher/email")
    public Object teacherModifyEmail(@Validated @RequestBody UserModifyEmailVo userVo,
                                     BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.teacherModifyEmail(userVo));
    }

    /**
     * 管理员登录
     * @author snow create 2021/01/19 00:11
     *            modified 2021/01/19 00:45
     * @param loginVo
     * @param bindingResult
     * @param httpServletResponse
     * @return
     */
    @ApiOperation(value = "管理员登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserLoginVo", name = "loginVo", value = "管理员号与密码", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 507, message = "信息签名不正确"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 748, message = "Email未确认"),
    })
    @PostMapping("admin/login")
    public Object adminLogin(@Validated @RequestBody UserLoginVo loginVo,
                               BindingResult bindingResult,
                               HttpServletResponse httpServletResponse){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject<String> jwt = compuOrgService.adminLogin(loginVo.getUserNo(), loginVo.getPassword());

        if(jwt.getData() == null){
            return ResponseUtil.fail(jwt.getCode(), jwt.getErrmsg());
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseUtil.ok(jwt.getData());
        }
    }

    /**
     * 管理员新建管理员
     * @author snow create 2021/01/19 00:45
     * @param userVo
     * @param bindingResult
     * @param httpServletResponse
     * @return
     */
    @ApiOperation(value = "管理员新建管理员", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserVo", name = "userVo", value = "管理员信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 705, message = "无权限访问"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 735, message = "管理员号已被注册"),
    })
    @Audit
    @PostMapping("admin/new")
    public Object appendAdmin(@ApiIgnore @Depart Long departId,
                              @Validated @RequestBody UserVo userVo,
                              BindingResult bindingResult,
                              HttpServletResponse httpServletResponse){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject retObj = compuOrgService.appendAdmin(departId, userVo);

        if(retObj.getData() == null){
            return Common.getNullRetObj(retObj, httpServletResponse);
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 管理员找回密码
     * @author snow create 2021/01/23 19:29
     * @param adminVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员找回密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserPasswordVo", name = "adminVo", value = "管理员验证身份信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
    })
    @PutMapping("admin/password/reset")
    public Object adminResetPassword(@Validated @RequestBody UserPasswordVo adminVo,
                                       BindingResult bindingResult,
                                       HttpServletRequest httpServletRequest){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.adminResetPassword(adminVo, ip));
    }

    /**
     * 管理员修改密码
     * @author snow create 2021/01/23 19:30
     * @param modifyPasswordVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyPasswordVo", name = "modifyPasswordVo", value = "修改密码对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 741, message = "新密码不能与旧密码相同"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("admin/password")
    public Object adminModifyPassword(@Validated @RequestBody UserModifyPasswordVo modifyPasswordVo,
                                        BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.adminModifyPassword(modifyPasswordVo));
    }

    /**
     * 管理员修改基本信息
     * @author snow create 2021/01/23 14:11
     * @param adminId
     * @param userBasicInfoVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员修改基本信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserBasicInfoVo", name = "userBasicInfoVo", value = "修改信息对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 735, message = "管理员号已被注册"),
    })
    @Audit
    @PutMapping("admin/information")
    public Object adminModifyBasicInformation(@ApiIgnore @LoginUser Long adminId,
                                                @Validated @RequestBody UserBasicInfoVo userBasicInfoVo,
                                                BindingResult bindingResult){
        logger.debug("StudentId: " + adminId + "userInfo: " + userBasicInfoVo.toString());
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.adminModifyBasicInformation(adminId, userBasicInfoVo));
    }

    /**
     * 管理员验证邮箱
     * @author snow create 2021/01/23 19:34
     * @param adminId
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "管理员验证邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @Audit
    @GetMapping("admin/email/verify")
    public Object adminVerifyEmail(@ApiIgnore @LoginUser Long adminId,
                                     HttpServletRequest httpServletRequest){
        logger.debug("StudentId: " + adminId);
        String ip = IpUtil.getIpAddr(httpServletRequest);

        return Common.decorateReturnObject(compuOrgService.adminVerifyEmail(adminId, ip));
    }

    /**
     * 管理员修改邮箱
     * @author snow create 2021/01/23 16:58
     * @param userVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员修改邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyEmailVo", name = "userVo", value = "修改邮箱对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("admin/email")
    public Object adminModifyEmail(@Validated @RequestBody UserModifyEmailVo userVo,
                                     BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.adminModifyEmail(userVo));
    }

    /**
     * 学生获取题目
     * @author snow create 2021/01/24 15:00
     * @param experimentId
     * @param size
     * @return
     */
    @ApiOperation(value = "学生获取题目", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "experimentId", value = "实验序号", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "size", value = "题目数量", required = false, defaultValue = "5"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 800, message = "暂无更多题目"),
    })
    @Audit
    @GetMapping("student/test/{experimentId}")
    public Object generateTest(@PathVariable Long experimentId,
                                     @RequestParam(required = false, defaultValue = "5") Long size){
        logger.debug("ExperimentId: " + experimentId + ", Size: " + size);
        if(experimentId < 1 || experimentId > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        if(size <= 0){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject retObj = compuOrgService.generateTest(experimentId, size);
        if(retObj.getData() == null){
            return Common.decorateReturnObject(retObj);
        }
        else{
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 学生提交测试结果
     * @author snow create 2021/01/25 22:30
     *            modified 2021/01/25 23:45
     * @param studentId
     * @param experimentId
     * @param testVo
     * @return
     */
    @ApiOperation(value = "学生提交测试结果", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "experimentId", value = "实验序号", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "TestVo", name = "testVo", value = "题目答案列表", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("student/test/{experimentId}")
    public Object commitTest(@ApiIgnore @LoginUser Long studentId,
                             @PathVariable Long experimentId,
                             @RequestBody TestVo testVo){
        logger.debug("StudentId: " + studentId + ", ExperimentId: " + experimentId);
        if(experimentId < 1 || experimentId > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        ReturnObject returnObject = compuOrgService.commitTestResult(studentId, experimentId, testVo);
        if(returnObject.getData() == null){
            return Common.decorateReturnObject(returnObject);
        }
        else {
            return Common.getRetObject(returnObject);
        }
    }

    /**
     * 教师获取测试结果列表
     * @author snow create 2021/01/25 23:27
     * @param departId
     * @param experimentId
     * @return
     */
    @ApiOperation(value = "教师获取测试结果列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "experimentId", value = "实验序号", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 801, message = "暂无更多测试结果"),
    })
    @Audit
    @GetMapping("teacher/test/result/{experimentId}")
    public Object getTestResultList(@ApiIgnore @Depart Long departId,
                                    @PathVariable Long experimentId){
        logger.debug("DepartId: " + departId + ", ExperimentId: " + experimentId);
        if(experimentId < 1 || experimentId > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        ReturnObject retObj = compuOrgService.getTestResultListByExperimentId(departId, experimentId);
        if(retObj.getData() == null){
            return Common.decorateReturnObject(retObj);
        }
        else{
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 根据测试结果id获得测试详细
     * @author snow create 2021/01/24 15:00
     * @param userId
     * @param departId
     * @param resultId
     * @return
     */
    @ApiOperation(value = "根据测试结果id获得测试详细", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "resultId", value = "测试结果id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("test/result/{resultId}")
    public Object getTestResultById(@ApiIgnore @LoginUser Long userId,
                                    @ApiIgnore @Depart Long departId,
                                    @PathVariable Long resultId){
        logger.debug("UserId: " + userId + ", departId: " + departId + ", ExperimentId: " + resultId);
        ReturnObject retObj = compuOrgService.getTestResultDetailByTestResultId(userId, departId, resultId);
        if(retObj.getData() == null){
            return Common.decorateReturnObject(retObj);
        }
        else{
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 教师提交测试结果评分
     * @author snow create 2021/01/27 23:08
     * @param departId
     * @return
     */
    @ApiOperation(value = "教师提交测试结果评分", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "TestResultScoreVo", name = "testResultScoreVo", value = "测试结果评分", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("teacher/test/result")
    public Object commitTestResultScore(@ApiIgnore @Depart Long departId,
                                        @Validated @RequestBody TestResultScoreVo testResultScoreVo){
        logger.debug("DepartId: " + departId);
        return Common.decorateReturnObject(compuOrgService.commitTestResultScore(departId, testResultScoreVo));
    }


}
