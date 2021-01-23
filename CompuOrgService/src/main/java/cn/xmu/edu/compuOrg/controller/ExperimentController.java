package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.util.Common;
import cn.xmu.edu.compuOrg.model.vo.exp.ArithmeticExperimentVo;
import cn.xmu.edu.compuOrg.service.ArithmeticExpService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "实验", tags = "compuOrgExperiment")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/experiment", produces = "application/json;charset=UTF-8")
public class ExperimentController {
    private static final Logger logger = LoggerFactory.getLogger(ExperimentController.class);

    @Autowired
    private ArithmeticExpService arithmeticExpService;

    /**
     * 运算器运算
     * @author yg
     */
    @ApiOperation(value = "运算器运算", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "body", dataType = "ArithmeticExperimentVo", name = "arithmeticExperimentVo", value = "运算器输入参数", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("arithmetic_exp/operation")
    public Object operate(@RequestBody ArithmeticExperimentVo arithmeticExperimentVo) {
        logger.debug("arithmetic experiment get operation");
        return Common.decorateReturnObject(arithmeticExpService.operateResult(arithmeticExperimentVo));
    }

    @ApiOperation(value = "静态存储器读取数据", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "body", dataType = "ArithmeticExperimentVo", name = "arithmeticExperimentVo", value = "运算器输入参数", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("static_memory_exp")
    public Object readData(@RequestBody ArithmeticExperimentVo arithmeticExperimentVo) {
        logger.debug("arithmetic experiment get operation");
        return Common.decorateReturnObject(arithmeticExpService.operateResult(arithmeticExperimentVo));
    }

}