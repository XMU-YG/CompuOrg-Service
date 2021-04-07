package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.annotation.Audit;
import cn.xmu.edu.Core.annotation.Depart;
import cn.xmu.edu.Core.annotation.LoginUser;
import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.model.vo.*;
import cn.xmu.edu.compuOrg.model.vo.PasswordVo;
import cn.xmu.edu.compuOrg.model.vo.VerifyCodeVo;
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

@Api(value = "计组项目后端", tags = "user")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class UserController {

    private  static  final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private CompuOrgService compuOrgService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 用户登录
     * @author snow create 2021/03/27 20:30
     * @param loginVo
     * @param bindingResult
     * @param httpServletResponse
     * @return
     */
    @ApiOperation(value = "用户登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserLoginVo", name = "loginVo", value = "用户名与密码", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 507, message = "信息签名不正确"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 748, message = "Email未确认"),
    })
    @PostMapping("login")
    public Object userLogin(@Validated @RequestBody UserLoginVo loginVo,
                            BindingResult bindingResult,
                            HttpServletResponse httpServletResponse){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        ReturnObject<String> jwt = compuOrgService.userLogin(loginVo);

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
     *            modified 2021/03/27 20:42
     * @param studentVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生注册", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserVo", name = "studentVo", value = "学生注册信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserRetVo.class),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 736, message = "用户名已被注册"),
    })
    @PostMapping("student/registration")
    public Object studentSignUp(@Validated @RequestBody UserVo studentVo,
                                BindingResult bindingResult){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        if(studentVo.getVerifyCode() == null){
            return Common.getNullRetObj(new ReturnObject<>(ResponseCode.FIELD_NOTVALID), httpServletResponse);
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
     * 教师注册
     * @author snow create 2021/01/18 13:30
     * @param teacherVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师注册", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserVo", name = "teacherVo", value = "教师注册信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserRetVo.class),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 736, message = "用户名已被注册"),
    })
    @Audit
    @PostMapping("teacher/registration")
    public Object teacherSignUp(@Depart @ApiIgnore Long departId,
                                @Validated @RequestBody UserVo teacherVo,
                                BindingResult bindingResult){

        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        logger.debug("TeacherSignUp in departId: " + departId);
        ReturnObject retObj = compuOrgService.teacherSignUp(departId, teacherVo);

        if(retObj.getData() == null){
            return Common.getNullRetObj(retObj, httpServletResponse);
        }else{
            httpServletResponse.setStatus(HttpStatus.CREATED.value());
            return Common.getRetObject(retObj);
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
            @ApiResponse(code = 0, message = "成功", response = UserRetVo.class),
            @ApiResponse(code = 705, message = "无权限访问"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
            @ApiResponse(code = 736, message = "用户名已被注册"),
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
     * 用户找回密码
     * @author snow create 2021/03/27 20:49
     * @param userVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户找回密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserPasswordVo", name = "userVo", value = "用户验证身份信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
    })
    @PutMapping("password/reset")
    public Object userResetPassword(@Validated @RequestBody UserPasswordVo userVo,
                                    BindingResult bindingResult,
                                    HttpServletRequest httpServletRequest){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.userResetPassword(userVo, ip));

    }

    /**
     * 用户验证密码
     * @param userId
     * @param passwordVo
     * @return
     */
    @ApiOperation(value = "用户验证密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "PasswordVo", name = "passwordVo", value = "旧密码", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @Audit
    @PutMapping("password/verify")
    public Object userVerifyPassword(@ApiIgnore @LoginUser Long userId,
                                     @Validated @RequestBody PasswordVo passwordVo,
                                     BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        ReturnObject retObj = compuOrgService.userVerifyPassword(userId, passwordVo.getPassword());
        if(retObj.getData() == null){
            return ResponseUtil.fail(retObj.getCode(), retObj.getErrmsg());
        }else{
            return ResponseUtil.ok(retObj.getData());
        }
    }

    /**
     * 用户修改密码
     * @author snow create 2021/01/17 23:30
     *            modified 2021/01/18 13:23
     *            modified 2021/03/27 20:59
     * @param modifyPasswordVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyPasswordVo", name = "modifyPasswordVo", value = "修改密码对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 741, message = "新密码不能与旧密码相同"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("password")
    public Object userModifyPassword(@Validated @RequestBody UserModifyPasswordVo modifyPasswordVo,
                                     BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.userModifyPassword(modifyPasswordVo));
    }

    /**
     * 用户查看个人信息
     * @author snow create 2021/03/22 10:43
     *            modified 2021/03/27 21:10
     * @param userId
     * @return
     */
    @ApiOperation(value = "用户查看个人信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("information")
    public Object userGetBasicInformation(@ApiIgnore @LoginUser Long userId){
        ReturnObject retObj = compuOrgService.userGetBasicInformation(userId);
        if(retObj.getData() == null){
            return Common.decorateReturnObject(retObj);
        }
        else{
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 管理员查看系统用户
     * @author snow create 2021/04/07 08:16
     * @param departId
     * @param role
     * @param userName
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "管理员查看系统用户", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "role", value = "用户角色", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "userName", value = "用户名", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", defaultValue = "1", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "页大小", defaultValue = "5", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("information/all")
    public Object adminGetUserInformation(@ApiIgnore @Depart Long departId,
                                          @RequestParam(required = false) Byte role,
                                          @RequestParam(required = false) String userName,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "5") Integer pageSize){
        if(page < 1 || pageSize < 0){
            return Common.getNullRetObj(new ReturnObject(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        }
        return Common.getPageRetObject(compuOrgService.adminGetUserInformation(departId, role, userName, page, pageSize));
    }

    /**
     * 用户修改基本信息
     * @author snow create 2021/01/23 14:11
     * @param userId
     * @param userBasicInfoVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户修改基本信息", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserBasicInfoVo", name = "userBasicInfoVo", value = "修改信息对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 731, message = "用户名已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
    })
    @Audit
    @PutMapping("information")
    public Object studentModifyBasicInformation(@ApiIgnore @LoginUser Long userId,
                                                @Validated @RequestBody UserBasicInfoVo userBasicInfoVo,
                                                BindingResult bindingResult){
        logger.debug("StudentId: " + userId + "userInfo: " + userBasicInfoVo.toString());
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.userModifyBasicInformation(userId, userBasicInfoVo));
    }
    /**
     * 用户注册时验证邮箱
     * @author snow create 2021/03/27 22:19
     * @param emailVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户注册时验证邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "EmailVo", name = "emailVo", value = "邮箱", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("email/verify/registration")
    public Object userVerifyEmail(@Validated @RequestBody EmailVo emailVo,
                                  BindingResult bindingResult,
                                  HttpServletRequest httpServletRequest){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.userVerifyEmail("-3835", emailVo.getEmail(), ip));
    }

    /**
     * 用户验证旧邮箱
     * @author snow create 2021/01/23 16:41
     *            modified 2021/03/27 21:22
     * @param userId
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "用户验证旧邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @Audit
    @GetMapping("email/verify")
    public Object userVerifyOldEmail(@ApiIgnore @LoginUser Long userId,
                                  HttpServletRequest httpServletRequest){
        logger.debug("UserId: " + userId);
        String ip = IpUtil.getIpAddr(httpServletRequest);

        return Common.decorateReturnObject(compuOrgService.userVerifyEmail(userId, ip));
    }

    /**
     * 用户验证新邮箱
     * @author snow create 2021/03/28 21:12
     * @param userId
     * @param emailVo
     * @param bindingResult
     * @param httpServletRequest
     * @return
     */
    @ApiOperation(value = "用户验证新邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "EmailVo", name = "emailVo", value = "邮箱", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @Audit
    @PutMapping("email/verify/new")
    public Object userVerifyNewEmail(@ApiIgnore @LoginUser Long userId,
                                     @Validated @RequestBody EmailVo emailVo,
                                     BindingResult bindingResult,
                                     HttpServletRequest httpServletRequest){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.userVerifyEmail(userId.toString(), emailVo.getEmail(), ip));

    }

    /**
     * 用户修改邮箱
     * @author snow create 2021/01/23 16:58
     *            modified 2021/03/27 21:25
     * @param userVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户修改邮箱", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "UserModifyEmailVo", name = "userVo", value = "修改邮箱对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @Audit
    @PutMapping("email")
    public Object userModifyEmail(@LoginUser @ApiIgnore Long userId,
                                  @Validated @RequestBody UserModifyEmailVo userVo,
                                  BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }

        return Common.decorateReturnObject(compuOrgService.userModifyEmail(userId, userVo));
    }

    /**
     * 用户验证验证码
     * @author snow create 2021/03/27 19:35
     *            modified 2021/03/27 21:29
     * @param verifyCode
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "用户验证验证码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "VerifyCodeVo", name = "verifyCode", value = "管理员验证身份信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
    })
    @PutMapping("password/verifyCode")
    public Object userVerifyCode(@Validated @RequestBody VerifyCodeVo verifyCode,
                                 BindingResult bindingResult){
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        ReturnObject retObj = compuOrgService.userVerifyCode(verifyCode);
        if(retObj.getData() == null){
            return ResponseUtil.fail(retObj.getCode(), retObj.getErrmsg());
        }
        else{
            return ResponseUtil.ok(retObj.getData());
        }

    }
}
