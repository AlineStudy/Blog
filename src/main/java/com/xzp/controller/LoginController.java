package com.xzp.controller;


import com.xzp.model.User;
import com.xzp.service.UserService;
import com.xzp.utils.Constants;
import com.xzp.utils.MD5Util;
import com.xzp.utils.RandStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Controller
public class LoginController extends BaseController {

    private final static Logger log = Logger.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired// redis数据库操作模板
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    @Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;// mq消息模板.



    @RequestMapping("/doLogin")
    public String doLogin(Model model, @RequestParam(value = "username",required = false) String email,
                          @RequestParam(value = "password",required = false) String password,
                          @RequestParam(value = "code",required = false) String code,
                          @RequestParam(value = "telephone",required = false) String telephone,
                          @RequestParam(value = "phone_code",required = false) String phone_code,
                          @RequestParam(value = "state",required = false) String state,
                          @RequestParam(value = "pageNum",required = false) Integer pageNum ,
                          @RequestParam(value = "pageSize",required = false) Integer pageSize) {

        //判断是否是手机登录
        if(StringUtils.isNotBlank(telephone)){
            log.warn(telephone+"到底是怎么回事"+"     " + redisTemplate);
            //手机登录
            String yzm = redisTemplate.opsForValue().get(telephone);//从redis获取验证码
            if(phone_code.equals(yzm)){
                //验证码正确
                User user = userService.findByPhone(telephone);
                getSession().setAttribute("user", user);
                model.addAttribute("user", user);
                log.info("手机快捷登录成功");
                return "/personal/personal";

            }else {
                //验证码错误或过期
                model.addAttribute("error","phone_fail");
                return "../login";
            }


        }else {
            //账号登录
            if(StringUtils.isBlank(code)){
                model.addAttribute("error","fail");
                return "../login";
            }

            int b = checkValidateCode(code);
            if (b == -1) {
                model.addAttribute("error", "fail");
                return "../login";
            } else if (b == 0) {
                model.addAttribute("error", "fail");
                return "../login";
            }

            //注册时使用了 MD5 对密码进行加密，所以登陆这里也要用同样的方式对密码进行加密
            System.out.println(password);
            password = MD5Util.encodeToHex(Constants.SALT+password);
            System.out.println(password);
            User user = userService.login(email,password);
            if(user != null){
                if ("0".equals(user.getState())){
                    //未激活
                    model.addAttribute("email",email);
                    model.addAttribute("error","active");
                    return "../login";
                }
                log.info("用户登录成功!");

                //用于保存用户Session会话，保持15分钟免登录
                getSession().setAttribute( "user",user ); model.addAttribute("user",user);


                model.addAttribute("user",user);
                return "redirect:/list";
            }else {
                log.info("用户登录登录失败");
                model.addAttribute("email",email);
                model.addAttribute( "error","fail" );
                return "../login";
            }
        }

    }

    // 匹对验证码的正确性
    public int checkValidateCode(String code) {
        Object vercode = getRequest().getSession().getAttribute("VERCODE_KEY");
        if (null == vercode) {
            return -1;
        }
        if (!code.equalsIgnoreCase(vercode.toString())) {
            return 0;
        }
        return 1;
    }


    /**
     * 15分钟内免登录实现原理：用户登录成功后，将用户信息保存在 Session 中，并设置失效时间为15分钟，
     * 是指15分钟内用户没有操作浏览器，如果15分钟内操作浏览器后重新计时。
     * 用户退出登录则删除 Session 保存的信息。
     * @param model
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model){
        User user = (User) getSession().getAttribute("user");

        if (user != null){
            return "/personal/personal";
        }
        return "../login";
    }


    /**
     * 发送手机验证码
     * @param model
     * @param telephone
     * @return
     */
    @RequestMapping("/sendSms")
    @ResponseBody
    public Map<String,Object> index(Model model, @RequestParam(value = "telephone",required = false) final String telephone ) {

        Map map = new HashMap<String,Object>();
        try {  //发送验证码
            final String code = RandStringUtils.getCode();
            // 60秒 有效 redis保存验证码
            // 调用ActiveMQ jmsTemplate，发送一条消息给MQ
            redisTemplate.opsForValue().set(telephone,code,60, TimeUnit.SECONDS);
            log.debug("--------短信验证码为："+code);
            jmsTemplate.send("login_msg", new MessageCreator() {
                @Override
                // 调用ActiveMQ jmsTemplate，发送一条消息给MQ
                public Message createMessage(Session session) throws JMSException {
                    MapMessage mapMessage = session.createMapMessage();
                    mapMessage.setString("telephone",telephone);
                    mapMessage.setString("code", code);
                    return mapMessage;
                }
            });
        }catch (Exception e){
            map.put("msg",false);
        }
        map.put( "msg",true );
        return map;
    }

}
