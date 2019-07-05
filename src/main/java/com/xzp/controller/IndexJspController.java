package com.xzp.controller;

import com.xzp.model.Comment;
import com.xzp.model.Upvote;
import com.xzp.model.User;
import com.xzp.model.UserContent;
import com.xzp.service.*;
import com.xzp.utils.DateUtils;
import com.xzp.utils.PageHelper;
import com.xzp.utils.StringUtil;
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
public class IndexJspController extends BaseController {

    private final static Logger log = Logger.getLogger(IndexJspController.class);

    @Autowired
    private UserContentService userContentService;
    @Autowired
    private UpvoteService upvoteService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private SolrService solrService;


    @RequestMapping("/index_list")
    public String findAllList(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "pageNum", required = false) Integer pageNum,
                              @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        log.info("================进入index_list==============");
        User user = (User) getSession().getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        if (StringUtils.isNotBlank(keyword)){
            PageHelper.Page<UserContent> page = solrService.findByKeyWords(keyword,pageNum,pageSize);
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
        }else {
            PageHelper.Page<UserContent> page = findAll(pageNum, pageSize);
            model.addAttribute("page", page);
        }
        return "../index";
    }


    @RequestMapping("/loginout")
    public String exit(Model model) {
        log.info("退出登录!");
        getSession().removeAttribute("user");
        //使 Session 失效，释放资源。
        getSession().invalidate();
        return "../login";
    }


    /**
     * 点赞或者踩
     *
     * @param model
     * @param id
     * @param uid
     * @param upvote
     * @return
     */
    @RequestMapping("/upvote")
    @ResponseBody
    public Map<String, Object> upvote(Model model,
                                      @RequestParam(value = "id", required = false) long id,
                                      @RequestParam(value = "uid", required = false) Long uid,
                                      @RequestParam(value = "upvote", required = false) int upvote) {

        log.info("id=" + id + ",uid=" + uid + "upvote=" + upvote);
        Map map = new HashMap<String, Object>();
        User user = (User) getSession().getAttribute("user");

        if (user == null) {
            map.put("data", "fail");
            return map;
        }


        Upvote upvote1 = new Upvote();
        upvote1.setContentId(id); //文章ID
        upvote1.setUserId(uid);  //用户

        Upvote upvote2 = upvoteService.findByUserIdAndConId(upvote1);//找出用户与文章的评价表
        if (upvote2 != null) {
            log.info(upvote2.toString() + "============");
        }

        UserContent userContent = userContentService.findById(id); //找到文章
        if (upvote == -1) {  //踩
            if (upvote2 != null) {

                //今天已经踩了,存在
                if ("1".equals(upvote2.getDownvote())) {
                    map.put("data", "down");
                    return map;
                } else {
                    upvote2.setDownvote("1");
                    upvote2.setUpvoteTime(new Date());
                    upvote2.setIp(getClientIpAddress());
                    upvoteService.update(upvote2);
                }
            } else {
                upvote1.setDownvote("1");
                upvote1.setUpvoteTime(new Date());
                upvote1.setIp(getClientIpAddress());
                upvoteService.add(upvote1);
            }

            userContent.setDownvote(userContent.getDownvote() + upvote);
        } else {
            if (upvote2 != null) {
                if ("1".equals(upvote2.getUpvote())) {
                    map.put("data", "done");
                    return map;
                } else {
                    upvote2.setUpvote("1");
                    upvote2.setUpvoteTime(new Date());
                    upvote2.setIp(getClientIpAddress());
                    upvoteService.update(upvote2);
                }
            } else {
                upvote1.setUpvote("1");
                upvote1.setUpvoteTime(new Date());
                upvote1.setIp(getClientIpAddress());
                upvoteService.add(upvote1);
            }

            userContent.setUpvote(userContent.getUpvote() + upvote);
        }

        userContentService.updateById(userContent);  //文章更新
        map.put("data", "success");
        return map;
    }


    @RequestMapping("/reply")
    @ResponseBody
    public Map<String, Object> reply(Model model, @RequestParam(value = "content_id", required = false) Long content_id) {
        Map map = new HashMap<String, Object>();
        List<Comment> list = commentService.findAllFirstComment(content_id); // 一级评论表

        if (list != null && list.size() > 0) {
            for (Comment comment : list) {
                // 二级评论表
                List<Comment> coments = commentService.findAllChildrenComment(comment.getConId(), comment.getChildren());
                if (coments != null && coments.size() > 0) {
                    for (Comment com : coments) {
                        if (com.getById() != null) {
                            User byUser = userService.findById(com.getById());//被评论者
                            com.setByUser(byUser);
                        }
                    }
                }
                comment.setComList(coments);  //将子评论列表注入到一级评论的 comList 属性中。
            }
        }
        map.put("list", list);
        return map;
    }


    @RequestMapping("/comment")
    @ResponseBody
    public Map<String, Object> comment(Model model, @RequestParam(value = "id", required = false) Long id,
                                       @RequestParam(value = "content_id", required = false) Long content_id,
                                       @RequestParam(value = "uid", required = false) Long uid,
                                       @RequestParam(value = "by_id", required = false) Long bid,
                                       @RequestParam(value = "oSize", required = false) String oSize,
                                       @RequestParam(value = "comment_time", required = false) String comment_time,
                                       @RequestParam(value = "upvote", required = false) Integer upvote) {

        Map map = new HashMap<String, Object>();
        User user = (User) getSession().getAttribute("user");
        if (user == null) {
            map.put("data", "fail");
            return map;
        }

        //判断评论 id 是否为 null，为 null 则说明是添加评论，不为 null 则为点赞或取消赞（评论里的点赞）。
        if (id == null) {
            Date date = DateUtils.StringToDate(comment_time, "yyyy-MM-dd HH:mm:ss");

            Comment comment = new Comment();
            comment.setComContent(oSize);
            comment.setCommTime(date);
            comment.setConId(content_id);
            comment.setComId(uid);
            if (upvote == null)
                upvote = 0;

            comment.setById(bid);
            comment.setUpvote(upvote);
            User u = userService.findById(uid);
            comment.setUser(u);
            //添加评论后将评论返回，注意这里的添加评论需要返回主键 id，所以插入方法需要手写 XML
            commentService.add(comment);
            map.put("data", comment);


            //根据文章 id 查询 UserContent，将其评论数+1，然后更新 userContent
            UserContent userContent = userContentService.findById(content_id);
            Integer num = userContent.getCommentNum();
            userContent.setCommentNum(num + 1);
            userContentService.updateById(userContent);

        } else {
            //点赞
            Comment comment = commentService.findById(id);
            comment.setUpvote(upvote);
            commentService.update(comment);
        }
        return map;

    }


    @RequestMapping("/deleteComment")
    @ResponseBody
    public Map<String, Object> deleteComment(Model model, @RequestParam(value = "id", required = false) Long id, @RequestParam(value = "uid", required = false) Long uid,
                                             @RequestParam(value = "con_id", required = false) Long con_id, @RequestParam(value = "fid", required = false) Long fid) {

        int num = 0;
        Map map = new HashMap<String,Object>();
        User user = (User) getSession().getAttribute("user");

        if (user == null){
            map.put("data","fail");
        }else {


            if (user.getId().equals(uid)){
                Comment comment = commentService.findById(id);

                if (StringUtils.isBlank(comment.getChildren())){
                    if (fid != null){
                        //去掉id  //父id  ==  父评论
                        Comment fcomm = commentService.findById(fid);
                        String child = StringUtil.getString( fcomm.getChildren(), id );
                        log.info(child);
                        fcomm.setChildren( child );
                        commentService.update( fcomm );
                    }
                    commentService.deleteById(id);
                    num = num + 1;
                }else {
                    String children = comment.getChildren();
                    commentService.deleteChildrenComment(children);
                    String[] arr = children.split(",");

                    commentService.deleteById(id);

                    num = num + arr.length + 1;
                }

                UserContent content = userContentService.findById( con_id );
                if(content!=null){
                    if(content.getCommentNum() - num >= 0){
                        content.setCommentNum( content.getCommentNum() - num );
                    }else {
                        content.setCommentNum( 0 );
                    }

                    userContentService.updateById( content );
                }
                map.put( "data",content.getCommentNum() );


            }else {
                map.put( "data","no-access" );
            }

        }
        return map;

    }









    @RequestMapping("/comment_child")
    @ResponseBody
    public Map<String,Object> addCommentChild(Model model, @RequestParam(value = "id",required = false) Long id ,
                                              @RequestParam(value = "content_id",required = false) Long content_id ,
                                              @RequestParam(value = "uid",required = false) Long uid ,
                                              @RequestParam(value = "by_id",required = false) Long bid ,
                                              @RequestParam(value = "oSize",required = false) String oSize,
                                              @RequestParam(value = "comment_time",required = false) String comment_time,
                                              @RequestParam(value = "upvote",required = false) Integer upvote) {
        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            map.put( "data","fail" );
            return map;
        }

        Date date = DateUtils.StringToDate( comment_time, "yyyy-MM-dd HH:mm:ss" );

        Comment comment = new Comment();
        comment.setComContent( oSize );
        comment.setCommTime( date );
        comment.setConId( content_id );
        comment.setComId( uid );
        if(upvote==null){
            upvote = 0;
        }
        comment.setById( bid );
        comment.setUpvote( upvote );
        User u = userService.findById( uid );
        comment.setUser( u );
        commentService.add( comment );

        Comment com = commentService.findById( id );
        if(StringUtils.isBlank( com.getChildren() )){
            com.setChildren( comment.getId().toString() );
        }else {
            com.setChildren( com.getChildren()+","+comment.getId() );
        }
        commentService.update( com );
        map.put( "data",comment );

        UserContent userContent = userContentService.findById( content_id );
        Integer num = userContent.getCommentNum();
        userContent.setCommentNum( num+1 );
        userContentService.updateById( userContent );
        return map;

    }
}
