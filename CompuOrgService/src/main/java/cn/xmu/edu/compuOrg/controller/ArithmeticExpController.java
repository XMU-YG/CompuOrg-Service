package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.util.Common;
import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentVo;
import cn.xmu.edu.compuOrg.model.vo.LinesVo;
import cn.xmu.edu.compuOrg.service.ArithmeticExpService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "运算器实验", tags = "compuOrgExperiment/ArithmeticExp")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/ArithmeticExp", produces = "application/json;charset=UTF-8")
public class ArithmeticExpController {
    private static final Logger logger = LoggerFactory.getLogger(ArithmeticExpController.class);

    @Autowired
    private ArithmeticExpService arithmeticExpService;

    @ApiOperation(value = "运算", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = false),
            @ApiImplicitParam(paramType = "body", dataType = "ArithmeticExperimentVo", name = "arithmeticExperimentVo", value = "运算器输入参数", required = true),

    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("arithmetic_exp/operation")
    public Object connectLines(@RequestBody ArithmeticExperimentVo arithmeticExperimentVo) {
        logger.debug("arithmetic experiment get operation");
        return Common.decorateReturnObject(arithmeticExpService.operateResult(arithmeticExperimentVo));
    }

}
