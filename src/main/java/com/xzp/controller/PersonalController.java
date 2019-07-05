package com.xzp.controller;


import com.xzp.model.User;
import com.xzp.model.UserContent;
import com.xzp.model.UserInfo;
import com.xzp.service.*;
import com.xzp.utils.Constants;
import com.xzp.utils.DateUtils;
import com.xzp.utils.MD5Util;
import com.xzp.utils.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PersonalController extends BaseController {

    //日志
    private final static Logger log = Logger.getLogger(PersonalController.class);

    @Autowired
    private UserContentService userContentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private SolrService solrService;


    /**
     * 初始胡个人主页数据
     *
     * @param model
     * @param id
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public String findList(Model model, @RequestParam(value = "id", required = false) String id,
                           @RequestParam(value = "pageNum", required = false) Integer pageNum,
                           @RequestParam(value = "pageSize", required = false) Integer pageSize,
                           @RequestParam(value = "manage", required = false) String manage) {


        //找到用户
        User user = (User) getSession().getAttribute("user");
        //这两个对象用来进行查询的SQL对象参数
        UserContent content = new UserContent();
        UserContent userContent = new UserContent();

        if (user != null) {
            model.addAttribute("user", user);
            content.setUserId(user.getId());
            userContent.setUserId(user.getId());
        } else return "../login";

        log.info("初始化个人主页信息!");

        if (StringUtils.isNotBlank(manage)) {
            model.addAttribute("manage", manage);
        }

        //查询分类
        List<UserContent> categorys = userContentService.findCategoryByUserId(user.getId());
        model.addAttribute("categorys", categorys);

        for (UserContent c : categorys) {
            System.out.println(c.getCategory() + "=======================" + c.getNickName());
        }

        System.out.println("11111111111111111111111111111111111111111111111111111111111111111111111111");

        //发布的文章，不含私密
        content.setPersonal("0");
        pageSize = 4; //每页默认显示四个
        PageHelper.Page<UserContent> page = findAll(content, pageNum, pageSize);
        model.addAttribute("page", page);

        //查询私密梦
        userContent.setPersonal("1");
        PageHelper.Page page2 = findAll(userContent, pageNum, pageSize);
        model.addAttribute("page2", page2);


        //查询热点文章
        UserContent userContent1 = new UserContent();
        userContent1.setPersonal("0");
        PageHelper.Page<UserContent> hotPage = findAllByUpvote(userContent1, pageNum, pageSize);
        model.addAttribute("hotPage", hotPage);


        return "personal/personal";

    }


    /**
     * 根据分类名称查询所有文章
     *
     * @param model
     * @param category
     * @return
     */
    @RequestMapping("/findByCategory")
    @ResponseBody
    public Map<String, Object> findByCategory(Model model, @RequestParam(value = "category", required = false) String category,
                                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map map = new HashMap<String, String>();
        User user = (User) getSession().getAttribute("user");
        if (null == user) {
            map.put("pageCate", "fail");
            return map;
        }

        pageSize = 4;
        PageHelper.Page<UserContent> pageCate = userContentService.findByCategory(category, user.getId(), pageNum, pageSize);
        map.put("pageCate", pageCate);
        return map;
    }


    /**
     * 根据用户id查询私密梦
     *
     * @param model
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPersonal")
    @ResponseBody
    public Map<String, Object> findPersonal(Model model, @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        Map map = new HashMap<String, String>();
        User user = (User) getSession().getAttribute("user");
        if (user == null) {
            map.put("page2", "fail");
            return map;
        }

        pageSize = 4;
        PageHelper.Page<UserContent> page = userContentService.findPersonal(user.getId(), pageNum, pageSize);
        map.put("page2", page);
        return map;
    }


    /**
     * 根据文章id删除文章
     *
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/deleteContent")
    public String deleteContent(Model model, @RequestParam(value = "cid", required = false) Long cid) {

        User user = (User) getSession().getAttribute("user");
        if (user == null) {
            return "../login";
        }
        try {
            solrService.deleteById(cid);
            commentService.deleteByContentId(cid);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        try {

            upvoteService.deleteByContentId(cid);
        } catch (Exception e) {
            System.out.println("错误2222");
        }
        userContentService.deleteById(cid);
        return "redirect:/list?manage=manage";
    }


    @RequestMapping("/profile")
    public String profile(Model model) {
        User user = (User) getSession().getAttribute("user");
        if (user == null) {
            return "../login";
        }

        UserInfo userInfo = userInfoService.findByUserId(user.getId());
        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        return "personal/profile";
    }


    /**
     * 保存个人头像
     *
     * @param model
     * @param url
     * @return
     */
    @RequestMapping("/saveImage")
    @ResponseBody
    public Map<String, Object> saveImage(Model model, @RequestParam(value = "url", required = false) String url) {
        Map map = new HashMap<String, Object>();
        User user = (User) getSession().getAttribute("user");
        user.setImgUrl(url);
        userService.update(user);
        map.put("msg", "success");
        return map;
    }


    /**
     * 保存用户信息
     *
     * @param model
     * @param name
     * @param nickName
     * @param sex
     * @param address
     * @param birthday
     * @return
     */
    @RequestMapping("/saveUserInfo")
    public String saveUserInfo(Model model, @RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "nick_name", required = false) String nickName,
                               @RequestParam(value = "sex", required = false) String sex,
                               @RequestParam(value = "address", required = false) String address,
                               @RequestParam(value = "birthday", required = false) String birthday) {

        User user = (User) getSession().getAttribute("user");
        if (user == null) {
            return "../login";
        }

        UserInfo userInfo = userInfoService.findByUserId(user.getId());
        boolean flag = false;
        if (userInfo == null) {
            userInfo = new UserInfo();
        } else {
            flag = true;
        }

        userInfo.setName(name);
        userInfo.setAddress(address);
        userInfo.setSex(sex);
        Date bir = DateUtils.StringToDate(birthday, "yyyy-MM-dd");
        userInfo.setBirthday(bir);
        userInfo.setUserId(user.getId());

        if (!flag)
            userInfoService.add(userInfo);
        else
            userInfoService.update(userInfo);

        user.setNickName(nickName);
        userService.update(user);

        model.addAttribute("user", user);
        model.addAttribute("userInfo", userInfo);
        return "personal/profile";
    }


    /**
     * 账户管理
     *
     * @param model
     * @return
     */
    @RequestMapping("/repassword")
    public String repassword(Model model) {
        User user = (User) getSession().getAttribute("user");
        model.addAttribute("user", user);
        if (user != null) {
            model.addAttribute("user", user);
            return "personal/repassword";
        }
        return "../login";
    }


    @RequestMapping("/updatePassword")
    public String updatePassword(Model model, @RequestParam(value = "old_password", required = false) String oldPassword,
                                 @RequestParam(value = "password", required = false) String password) {

        User user = (User) getSession().getAttribute("user");

        if (user!=null){
            oldPassword = MD5Util.encodeToHex(Constants.SALT + oldPassword);
            if (user.getPassword().equals(oldPassword)){
                password = MD5Util.encodeToHex(Constants.SALT + password);
                user.setPassword(password);
                userService.update(user);
                model.addAttribute("message", "success");
            }else
                model.addAttribute("message", "fail");

        }
        model.addAttribute("user",user);
        return "personal/passwordSuccess";

    }
}
