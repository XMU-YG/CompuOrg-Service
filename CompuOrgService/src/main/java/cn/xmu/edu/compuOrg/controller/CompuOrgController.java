package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.Core.util.Common;
import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.model.vo.LinesVo;
import cn.xmu.edu.compuOrg.service.CompuOrgService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
