package com.nuoquan.service;

/**
 * 微信业务 业务接口类
 * @author cy
 */
public interface WeChatService {
 
    /**
     * 获取微信access_token
     * @return
     */
    public String getWeChatAccessToken();
    
    /**
     * 对文本进行安全检测
     * @param content
     * @return true: 合法 false: 非法
     */
    public boolean msgSecCheck(String content);
}