package cn.xmu.edu.compuOrg.controller;

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
            @ApiResponse(code = 601, message = "连线端点不存在"),
    })
    @PostMapping("experiment/{eid}/lines")
    public Object connectLines(@PathVariable(value = "eid") Integer eid, @RequestBody LinesVo linesVo){
        logger.debug("experimentId: " + eid + ", lines: " + linesVo.toString());
        if(eid < 1 || eid > 5){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(linesVo.getEndA() == null || linesVo.getEndB() == null){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        return compuOrgService.connectLines(eid, linesVo);
    }


}
