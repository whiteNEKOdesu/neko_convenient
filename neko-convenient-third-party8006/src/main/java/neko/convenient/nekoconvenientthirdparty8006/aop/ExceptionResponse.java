package neko.convenient.nekoconvenientthirdparty8006.aop;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import neko.convenient.nekoconvenientcommonbase.utils.entity.Response;
import neko.convenient.nekoconvenientcommonbase.utils.entity.ResultObject;
import neko.convenient.nekoconvenientcommonbase.utils.exception.FileDeleteFailureException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.MemberServiceException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.ProductServiceException;
import neko.convenient.nekoconvenientcommonbase.utils.exception.UserNameRepeatException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;

@RestControllerAdvice
@Slf4j
public class ExceptionResponse {
    //日志方法
    public static void exceptionLogger(Exception e){
        log.error(e.getMessage());
        for(StackTraceElement trace : e.getStackTrace()){
            log.error(trace.toString());
        }
    }

    //顶级异常处理
    @ExceptionHandler(value = Exception.class)
    public ResultObject<Object> exceptionHandler(Exception e){
        exceptionLogger(e);
        return ResultObject.unknownError();
    }

    //非法参数异常处理
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResultObject<Object> illegalArgumentExceptionHandler(IllegalArgumentException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.ILLEGAL_ARGUMENT_ERROR)
                .compact();
    }

    //文件删除失败异常处理
    @ExceptionHandler(value = FileDeleteFailureException.class)
    public ResultObject<Object> fileDeleteFailureExceptionHandler(FileDeleteFailureException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.FILE_DELETE_FAILURE_ERROR)
                .compact();
    }

    //未找到文件异常
    @ExceptionHandler(value = FileNotFoundException.class)
    public ResultObject<Object> fileNotFoundExceptionHandler(FileNotFoundException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.FILE_NOT_FOUND_ERROR)
                .compact();
    }

    //用户名重复异常
    @ExceptionHandler(value = UserNameRepeatException.class)
    public ResultObject<Object> userNameRepeatExceptionHandler(UserNameRepeatException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.USER_NAME_REPEAT_ERROR)
                .compact();
    }

    //@RequestParam参数缺少异常
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResultObject<Object> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.ILLEGAL_ARGUMENT_ERROR)
                .compact();
    }

    //@RequestPart参数缺少异常
    @ExceptionHandler(value = MissingServletRequestPartException.class)
    public ResultObject<Object> missingServletRequestPartExceptionHandler(MissingServletRequestPartException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.ILLEGAL_ARGUMENT_ERROR)
                .compact();
    }

    //token检查异常
    @ExceptionHandler(value = NotLoginException.class)
    public ResultObject<Object> tokenCheckExceptionHandler(NotLoginException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.TOKEN_CHECK_ERROR)
                .compact();
    }

    //权限不足异常
    @ExceptionHandler(value = NotPermissionException.class)
    public ResultObject<Object> weightNotEnoughExceptionHandler(NotPermissionException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.WEIGHT_NOT_ENOUGH_ERROR)
                .compact();
    }

    //请求类型不支持异常
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResultObject<Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.HTTP_REQUEST_METHOD_NOT_SUPPORTED_ERROR)
                .compact();
    }

    //角色不存在异常
    @ExceptionHandler(value = NotRoleException.class)
    public ResultObject<Object> notRoleExceptionHandler(NotRoleException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.ROLE_NOT_EXIST_ERROR)
                .compact();
    }

    //JSR303参数验证异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultObject<Object> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.ILLEGAL_ARGUMENT_ERROR)
                .compact();
    }

    //重复key异常
    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResultObject<Object> duplicateKeyExceptionHandler(DuplicateKeyException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.DUPLICATE_KEY_ERROR)
                .compact();
    }

    //member微服务远程调用异常
    @ExceptionHandler(value = MemberServiceException.class)
    public ResultObject<Object> memberServiceExceptionHandler(MemberServiceException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.MEMBER_SERVICE_ERROR)
                .compact();
    }

    //product微服务远程调用异常
    @ExceptionHandler(value = ProductServiceException.class)
    public ResultObject<Object> productServiceExceptionHandler(ProductServiceException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.PRODUCT_SERVICE_ERROR)
                .compact();
    }

    //参数格式错误异常
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResultObject<Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.ARGUMENT_ILLEGAL_FORMAT_ERROR)
                .compact();
    }

    //邮件发送错误异常
    @ExceptionHandler(value = MessagingException.class)
    public ResultObject<Object> messagingExceptionHandler(MessagingException e){
        exceptionLogger(e);
        return new ResultObject<Object>()
                .setResponseStatus(Response.EMAIL_SEND_ERROR)
                .compact();
    }
}