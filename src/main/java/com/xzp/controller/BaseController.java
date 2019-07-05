package com.xzp.controller;


import com.xzp.model.Comment;
import com.xzp.model.User;
import com.xzp.model.UserContent;
import com.xzp.service.UserContentService;
import com.xzp.service.UserService;
import com.xzp.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class BaseController {

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"};

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private UserService userService;

    /**
     * 是否登录
     * @param id
     * @return
     */
    public boolean isLogin(Long id){
        if (id != null){
            User user = userService.findById(id);
            if (user != null){
                return true;
            }
        }
        return false;
    }


    /**
     * 查找用户
     * @param id
     * @return
     */
    public User getUser(Long id){
        User user = userService.findById(id);
        return user;
    }


    /**
     * 查找文章
     * @param userId
     * @return
     */
    public List<UserContent> getUserContentList(Long userId){
        List<UserContent> list = userContentService.findByUserId(userId);
        return list;
    }

    /**
     * 查找所有文章
     * @return
     */
    public List<UserContent> getAllUserContentList(){
        List<UserContent> list = userContentService.findAll();
        return list;
    }

    /**
     * 所有文章
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelper.Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize){
        PageHelper.Page<UserContent> page = userContentService.findAll( content,pageNum ,pageSize);
        return page;
    }



    public PageHelper.Page<UserContent> findAll(Integer pageNum, Integer pageSize){
        PageHelper.Page<UserContent> page = userContentService.findAll(pageNum ,pageSize);
        return page;
    }

    /**
     * 文章
     * @param content
     * @param comment
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelper.Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize){
        PageHelper.Page<UserContent> page = userContentService.findAll( content,comment,pageNum ,pageSize);
        return page;
    }

    /**
     * 评论分页
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageHelper.Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize){
        PageHelper.Page<UserContent> page = userContentService.findAllByUpvote( content,pageNum ,pageSize);
        return page;
    }


    /**
     * 获取request
     * @return
     */
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }


    /**
     * 获取response
     * @return
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }


    /**
     * 获得会话
     * @return
     */
    public static HttpSession getSession(){
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        }catch (Exception e){}
        return session;
    }


    /**
     * 获得客户端地址
     * @return
     */
    public static String getClientIpAddress(){
        HttpServletRequest request = getRequest();

        for (String header : HEADERS_TO_TRY){
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

}
