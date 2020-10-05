package com.hb.isblog.handler;

import com.hb.isblog.entity.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常拦截器
 * 标识了状态码的时候就不拦截，如资源找不到异常
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    //记录异常日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String DEFAULT_ERROR_VIEW = "error"; // 定义错误显示页，error.html
    private static final Integer INIT_ERROR_CODE = 500; // 定义初始错误码


    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        //记录错误日志
        logger.error("Request URL : {},Exception : {}",request.getRequestURL(),e);
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        ModelAndView mv = new ModelAndView(DEFAULT_ERROR_VIEW);
        int errorCode = INIT_ERROR_CODE;
        // 可以自己定制错误分类对象信息
        ErrorInfo errorInfo = new ErrorInfo();
        // 对错误类型进行分类
        String header = request.getHeader("content-type");
        if(header != null && header.contains("json")){
            errorCode = 300; // json异常
        } else if (e instanceof ArithmeticException) {
            errorCode = 100; // 算术异常
        } else if (e instanceof NullPointerException) {
            errorCode = 200; // 空指针异常
        } else {
            errorCode = 999; // 其他异常
        }
        // 对错误码进行判断
        switch (errorCode) {
            case 100:
                errorInfo.setCode(errorCode); // 将错误码传递过去
                break;
            case 200:
                errorInfo.setCode(errorCode); // 将错误码传递过去
                break;
            case 300:
                errorInfo.setCode(errorCode); // 将错误码传递过去
                break;
            case 500:
                errorInfo.setCode(INIT_ERROR_CODE);  // 将错误码传递过去
                break;
            case 999:
                errorInfo.setCode(INIT_ERROR_CODE);  // 将错误码传递过去
                break;
            default:
                errorInfo.setCode(1000); // 将错误码传递过去
                break;
        }
        errorInfo.setMessage(e.getMessage());// 将异常对象传递过去
        errorInfo.setUrl(request.getRequestURL().toString());// 获得请求的路径
        mv.addObject("errorInfo", errorInfo);
        mv.setViewName("error/"+DEFAULT_ERROR_VIEW);
        return mv;
    }
}
