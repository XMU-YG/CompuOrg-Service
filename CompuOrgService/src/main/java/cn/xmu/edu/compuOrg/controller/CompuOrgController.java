package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.util.*;
import cn.xmu.edu.compuOrg.model.vo.*;
import cn.xmu.edu.compuOrg.service.CompuOrgService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     * @author snow 2021/01/12 20:23
     * @param eid
     * @param linesVo
     * @return
     */
    @ApiOperation(value = "实验连线", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "eid", value = "实验序号", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "LinesVo", name = "linesVo", value = "连线端点", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 601, message = "本次实验中不存在这样的连线"),
    })
    @PostMapping("experiment/{eid}/lines/connect")
    public Object connectLines(@PathVariable(value = "eid") Integer eid, @RequestBody LinesVo linesVo){
        logger.debug("experimentId: " + eid + ", lines: " + linesVo.toString());
        if(eid < 1 || eid > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        if(linesVo.getEndA() == null || linesVo.getEndB() == null){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        return Common.decorateReturnObject(compuOrgService.connectLines(eid, linesVo));
    }


    /**
     * 检验单个实验所有连线
     * @param eid
     * @param linesVos
     * @return
     */
    @ApiOperation(value = "连线检验", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "eid", value = "实验序号", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "LinesVo", name = "linesVos", allowMultiple = true, value = "连线", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 602, message = "存在错误布线"),
    })
    @PostMapping("experiment/{eid}/lines/validation")
    public Object validLines(@PathVariable(value = "eid") Integer eid, @RequestBody List<LinesVo> linesVos){
        logger.debug("experimentId: " + eid + ", lines: " + linesVos.toString());
        if(eid < 1 || eid > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        return Common.decorateReturnObject(compuOrgService.validAllLines(eid, linesVos));
    }

    /**
     * 学生登录
     * @author snow create 2021/01/15 20:00
     * @param loginVo
     * @param httpServletResponse
     * @return
     */
    @ApiOperation(value = "学生登录", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "StudentLoginVo", name = "loginVo", value = "学号与密码", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
    })
    @PostMapping("student/login")
    public Object studentLogin(@RequestBody StudentLoginVo loginVo,
                               HttpServletResponse httpServletResponse){

        if(loginVo.getStudentNo() == null || loginVo.getPassword() == null){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }

        ReturnObject<String> jwt = compuOrgService.studentLogin(loginVo.getStudentNo(), loginVo.getPassword());

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
     * @param studentVo
     * @return
     */
    @ApiOperation(value = "学生注册", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "StudentVo", name = "studentVo", value = "学生注册信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 731, message = "学号已被注册"),
            @ApiResponse(code = 732, message = "邮箱已被注册"),
            @ApiResponse(code = 733, message = "电话已被注册"),
    })
    @PostMapping("student/registration")
    public Object studentSignUp(@RequestBody StudentVo studentVo){

        if(studentVo.getStudentNo() == null || studentVo.getPassword() == null){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
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
     * @param studentVo
     * @return
     */
    @ApiOperation(value = "学生找回密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "StudentResetPasswordVo", name = "studentVo", value = "学生验证身份信息", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 702, message = "用户被禁止登录"),
            @ApiResponse(code = 745, message = "与系统预留的邮箱不一致"),
    })
    @PutMapping("student/password/reset")
    public Object studentResetPassword(@RequestBody StudentResetPasswordVo studentVo,
                                       HttpServletRequest httpServletRequest){

        if(studentVo.getStudentNo() == null || studentVo.getEmail() == null){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }

        String ip = IpUtil.getIpAddr(httpServletRequest);
        return Common.decorateReturnObject(compuOrgService.studentResetPassword(studentVo, ip));
    }

    /**
     * 学生修改密码
     * @author snow create 2021/01/17 23:30
     * @param modifyPasswordVo
     * @return
     */
    @ApiOperation(value = "学生修改密码", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "body", dataType = "StudentModifyPasswordVo", name = "modifyPasswordVo", value = "修改密码对象", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 700, message = "用户名不存在或者密码错误"),
            @ApiResponse(code = 741, message = "新密码不能与旧密码相同"),
            @ApiResponse(code = 750, message = "验证码不正确或已过期"),
    })
    @PutMapping("student/password")
    public Object studentModifyPassword(@RequestBody StudentModifyPasswordVo modifyPasswordVo){

        if(modifyPasswordVo.getStudentNo() == null ||
                modifyPasswordVo.getVerifyCode() == null ||
                modifyPasswordVo.getPassword() == null){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }

        return Common.decorateReturnObject(compuOrgService.studentModifyPassword(modifyPasswordVo));
    }



}
