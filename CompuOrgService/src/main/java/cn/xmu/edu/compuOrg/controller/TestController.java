package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.annotation.Audit;
import cn.xmu.edu.Core.annotation.Depart;
import cn.xmu.edu.Core.annotation.LoginUser;
import cn.xmu.edu.Core.util.Common;
import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.model.bo.TestResult;
import cn.xmu.edu.compuOrg.model.vo.*;
import cn.xmu.edu.compuOrg.service.CompuOrgService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

@Api(value = "计组项目后端", tags = "test")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/test", produces = "application/json;charset=UTF-8")
public class TestController {

    private  static  final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private CompuOrgService compuOrgService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 学生获取题目
     * @author snow create 2021/01/24 15:00
     *            modified 2021/03/25 11:00
     * @param studentId
     * @param departId
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
            @ApiResponse(code = 0, message = "成功", response = TestRetVo.class),
            @ApiResponse(code = 800, message = "暂无更多题目"),
    })
    @Audit
    @GetMapping("{experimentId}")
    public Object generateTest(@ApiIgnore @LoginUser Long studentId, @ApiIgnore @Depart Long departId,
                               @PathVariable Long experimentId, @RequestParam(required = false, defaultValue = "5") Long size){
        logger.debug("ExperimentId: " + experimentId + ", Size: " + size);
        if(experimentId < 1 || experimentId > 5){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        if(size <= 0){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject retObj = compuOrgService.generateTest(studentId, departId, experimentId, size);
        if(retObj.getData() == null){
            return Common.decorateReturnObject(retObj);
        }
        else{
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 教师新增测试题目
     * @author snow create 2021/01/28 10:26
     *            modified 2021/01/28 13:33
     * @param departId
     * @param topicVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "教师新增测试题目", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "TopicVo", name = "topicVo", value = "题目详情", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = TopicRetVo.class),
    })
    @Audit
    @PostMapping("topic")
    public Object appendNewTopic(@ApiIgnore @Depart Long departId,
                                 @Validated @RequestBody TopicVo topicVo,
                                 BindingResult bindingResult){
        logger.debug("DepartId: " + departId + ", Topic: " + topicVo.toString());
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        ReturnObject retObj = compuOrgService.appendTopic(departId, topicVo);
        if (retObj.getData() != null){
            return Common.getRetObject(retObj);
        }
        else {
            return Common.decorateReturnObject(retObj);
        }
    }

    /**
     * 教师删除测试题目
     * @author snow create 2021/01/28 13:51
     * @param departId
     * @param topicId
     * @return
     */
    @ApiOperation(value = "教师删除测试题目", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "topicId", value = "题目id", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @DeleteMapping("topic/{topicId}")
    public Object removeTopic(@ApiIgnore @Depart Long departId,
                              @PathVariable Long topicId){
        logger.debug("DepartId: " + departId + ", TopicId: " + topicId);
        return Common.decorateReturnObject(compuOrgService.removeTopic(departId, topicId));
    }

    /**
     * 教师修改测试题目
     * @author snow create 2021/01/28 13:53
     * @param departId
     * @param topicId
     * @param topicVo
     * @return
     */
    @ApiOperation(value = "教师修改测试题目", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "topicId", value = "题目id", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "TopicVo", name = "topicVo", value = "题目详情", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PutMapping("topic/{topicId}")
    public Object modifyTopic(@ApiIgnore @Depart Long departId,
                                 @PathVariable Long topicId,
                                 @RequestBody TopicVo topicVo){
        logger.debug("DepartId: " + departId + ", TopicId: " + topicId + ", Topic: " + topicVo.toString());
        if(topicVo.getExperimentId() != null && (topicVo.getExperimentId() < 1 || topicVo.getExperimentId() > 5)){
            return Common.getNullRetObj(new ReturnObject(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        }
        ReturnObject retObj = compuOrgService.modifyTopic(departId, topicId, topicVo);
        return Common.decorateReturnObject(retObj);
    }

    /**
     * 教师获取题目列表
     * @author snow create 2021/01/28 14:35
     *            modified 2021/04/11 21:23
     * @param departId
     * @param experimentId
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "教师获取题目列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "experimentId", value = "实验序号", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "页大小", required = false),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @GetMapping("topic")
    public Object getTopicList(@ApiIgnore @Depart Long departId,
                                    @RequestParam(required = false) Long experimentId,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "2147483647") Integer pageSize){
        logger.debug("DepartId: " + departId + ", ExperimentId: " + experimentId);
        if(page < 1 || pageSize < 0){
            return Common.getNullRetObj(new ReturnObject(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        }
        if(experimentId != null && (experimentId < 1 || experimentId > 5)){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        return Common.getPageRetObject(compuOrgService.getTopicList(departId, experimentId, page, pageSize));
    }

    /**
     * 学生提交测试结果
     * @author snow create 2021/01/25 22:30
     *            modified 2021/01/25 23:45
     *            modified 2021/01/28 13:28
     * @param studentId
     * @param testVo
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "学生提交测试结果", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "body", dataType = "TestVo", name = "testVo", value = "题目答案列表", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = TestResult.class),
    })
    @Audit
    @PostMapping("result")
    public Object commitTest(@ApiIgnore @LoginUser Long studentId,
                             @ApiIgnore @Depart Long departId,
                             @Validated @RequestBody TestVo testVo,
                             BindingResult bindingResult){
        logger.debug("StudentId: " + studentId + ", TestVo: " + testVo);
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(returnObject != null){
            return returnObject;
        }
        ReturnObject retObj = compuOrgService.commitTestResult(studentId, departId, testVo);
        if(retObj.getData() == null){
            return Common.decorateReturnObject(retObj);
        }
        else {
            return Common.getRetObject(retObj);
        }
    }

    /**
     * 获取测试结果列表
     * @author snow create 2021/01/25 23:27
     *            modified 2021/01/28 12:00
     *            modified 2021/01/28 13:37
     *            modified 2021/03/25 10:31
     * @param departId
     * @param userId
     * @param experimentId
     * @param studentId
     * @param modified
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "获取测试结果列表", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "experimentId", value = "实验序号", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "studentId", value = "学生id", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "bool", name = "modified", value = "是否已批改", required = false),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "page", value = "页码", defaultValue = "1", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "pageSize", value = "页大小", defaultValue = "5", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 801, message = "暂无更多测试结果"),
    })
    @Audit
    @GetMapping("result")
    public Object getTestResultList(@ApiIgnore @Depart Long departId,
                                    @ApiIgnore @LoginUser Long userId,
                                    @RequestParam(required = false) Long experimentId,
                                    @RequestParam(required = false) Long studentId,
                                    @RequestParam(required = false) Boolean modified,
                                    @RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "5") Integer pageSize){
        logger.debug("DepartId: " + departId + ", UserId: " + userId + ", ExperimentId: " + experimentId + ", StudentId: " + studentId);
        if(page < 1 || pageSize < 0){
            return Common.getNullRetObj(new ReturnObject(ResponseCode.FIELD_NOTVALID), httpServletResponse);
        }
        if(experimentId != null && (experimentId < 1 || experimentId > 5)){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST));
        }
        return Common.getPageRetObject(compuOrgService.getTestResultList(departId, userId, experimentId, studentId, modified, page, pageSize));
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
            @ApiResponse(code = 0, message = "成功", response = TestResult.class),
    })
    @Audit
    @GetMapping("result/{resultId}")
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
     * 学生根据实验序号获得测试结果详情
     * @author snow create 2021/03/25 10:57
     * @param studentId
     * @param departId
     * @param experimentId
     * @return
     */
    @ApiOperation(value = "学生根据实验序号获得测试结果详情", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "experimentId", value = "实验序号", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = TestResult.class),
    })
    @Audit
    @GetMapping("result/experiment/{experimentId}")
    public Object getTestResultByExperimentId(@ApiIgnore @LoginUser Long studentId,
                                              @ApiIgnore @Depart Long departId,
                                              @PathVariable Long experimentId){
        ReturnObject retObj = compuOrgService.getTestResultDetailByExperimentId(studentId, departId, experimentId);
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
     *            modified 2021/04/18 20:21
     * @param departId 角色id
     * @param testResultId 测试结果id
     * @param testResultScoreVo 评分Vo
     * @return 操作结果
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
    @PutMapping("result/{testResultId}")
    public Object commitTestResultScore(@ApiIgnore @Depart Long departId,
                                        @PathVariable Long testResultId,
                                        @Validated @RequestBody TestResultScoreVo testResultScoreVo){
        logger.debug("DepartId: " + departId);
        return Common.decorateReturnObject(compuOrgService.commitTestResultScore(departId, testResultId, testResultScoreVo));
    }


}
