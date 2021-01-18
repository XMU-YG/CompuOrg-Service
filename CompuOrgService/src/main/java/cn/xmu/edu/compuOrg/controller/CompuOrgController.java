package cn.xmu.edu.compuOrg.controller;

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
     * 管理员登录
     * @author snow create 2021/01/19 00:11
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
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
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


}
