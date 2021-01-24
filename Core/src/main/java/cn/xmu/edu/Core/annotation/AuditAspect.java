package cn.xmu.edu.Core.annotation;

import cn.xmu.edu.Core.util.JwtHelper;
import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ResponseUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author mingqiu
 * @date 2020/6/26 14:16
 *      modifiedBy Ming Qiu 2020/11/3 22:59
 *
 */
@Aspect
@Component
public class AuditAspect {

    //注入Service用于把日志保存数据库

    private  static  final Logger logger = LoggerFactory.getLogger(AuditAspect. class);

    //Controller层切点
    @Pointcut("@annotation(cn.xmu.edu.Core.annotation.Audit)")
    public void auditAspect() {
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param joinPoint 切点
     */
    @Before("auditAspect()")
    public void doBefore(JoinPoint joinPoint) {
    }

    //配置controller环绕通知,使用在方法aspect()上注册的切入点
    @Around("auditAspect()")
    public Object around(JoinPoint joinPoint){
        logger.debug("around: begin joinPoint = "+ joinPoint);
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String token = request.getHeader(JwtHelper.LOGIN_TOKEN_KEY);
        if (token == null){
//            什么也不做
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseUtil.fail(ResponseCode.AUTH_NEED_LOGIN);
        }

        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        Long userId = null;
        Long departId = null;
        if (null != userAndDepart){
            userId = userAndDepart.getUserId();
            departId = userAndDepart.getDepartId();
        }


        logger.debug("around: userId ="+userId+" departId="+departId);
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseUtil.fail(ResponseCode.AUTH_NEED_LOGIN);
        }

        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Object param = args[i];
            Annotation[] paramAnn = annotations[i];
            if (paramAnn.length == 0){
                continue;
            }

            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为LoginUser.class
                if (annotation.annotationType().equals(LoginUser.class)) {
                    //校验该参数，验证一次退出该注解
                    args[i] = userId;
                }
                if (annotation.annotationType().equals(Depart.class)) {
                    //校验该参数，验证一次退出该注解
                    args[i] = departId;
                }
            }
        }

        Object obj = null;
        try {
            obj = ((ProceedingJoinPoint) joinPoint).proceed(args);
        } catch (Throwable e) {

        }
        return obj;
    }
}
