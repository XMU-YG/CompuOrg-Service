package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.compuOrg.service.CompuOrgService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Api(value = "计组项目后端", tags = "compuOrg")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class CompuOrgController {

    @Autowired
    private CompuOrgService compuOrgService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * @author snow
     */
    @ApiOperation(value = "测试", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "authorization", value = "token", required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 801, message = "订单状态禁止"),
    })
    @PutMapping("test")
    public Object confirmOrderReceived( ){
        return "{message: \"Hello World!\"}";
    }


}
