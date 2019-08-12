package com.nuoquan;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.github.tobato.fastdfs.exception.FdfsServerException;

/**
 * @author jerrio
 * 全局异常统一处理，返回值 和Controller的返回规则一样
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	
	// 这个异常好像捕获不到...
    @ExceptionHandler(FdfsServerException.class)
    public void fdfsException(Throwable t){
    	System.out.println("错误码：2，错误信息：找不到节点或文件");
    }
}