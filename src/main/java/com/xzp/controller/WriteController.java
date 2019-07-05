package com.xzp.controller;

import com.xzp.model.User;
import com.xzp.model.UserContent;
import com.xzp.service.CommentService;
import com.xzp.service.SolrService;
import com.xzp.service.UpvoteService;
import com.xzp.service.UserContentService;
import com.xzp.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jnlp.UnavailableServiceException;
import java.util.Date;

@Controller
public class WriteController extends BaseController {

    private final static Logger log = Logger.getLogger(WriteController.class);
    @Autowired
    private UserContentService userContentService;


    @Autowired
    private SolrService solrService;



    /**
     * 进入writedream
     * @param model
     * @return
     */
    @RequestMapping("/writedream")
    public String writedream(Model model,@RequestParam(value = "cid",required = false) Long cid) {
        User user = (User) getSession().getAttribute("user");
        if(cid!=null){
            UserContent content = userContentService.findById(cid);
            model.addAttribute("cont",content);
        }
        model.addAttribute("user", user);
        return "write/writedream";
    }



    /**
     * 写文章
     * @param model
     * @param id
     * @param category
     * @param txtT_itle
     * @param content
     * @param private_dream
     * @return
     */
    @RequestMapping("/doWritedream")
    public String doWritedream(Model model, @RequestParam(value = "id", required = false) String id,
                               @RequestParam(value = "category", required = false) String category,
                               @RequestParam(value = "cid",required = false)Long cid,
                               @RequestParam(value = "txtT_itle", required = false) String txtT_itle,
                               @RequestParam(value = "content", required = false) String content,
                               @RequestParam(value = "private_dream", required = false) String private_dream) {

        log.info( "进入写梦Controller" );
        User user = (User) getSession().getAttribute("user");
        if (user == null){
            //未登录
            model.addAttribute("error","请先登录!");
            return "../login";
        }
        UserContent userContent = new UserContent();

        if (cid != null){
            userContent =userContentService.findById(cid);
        }

        userContent.setCategory(category);
        userContent.setContent(content);
        userContent.setRptTime(new Date());
        String imgUrl = user.getImgUrl();

        if (StringUtils.isBlank(imgUrl)){
            userContent.setImgUrl( "/images/icon_m.jpg" );
        }else {
            userContent.setImgUrl(imgUrl);
        }
        if("on".equals( private_dream )){
            userContent.setPersonal( "1" );
        }else{
            userContent.setPersonal( "0" );
        }

        userContent.setTitle(txtT_itle);
        userContent.setUserId(user.getId());
        userContent.setNickName(user.getNickName());

        //在添加和更新文章之后分别同步 Solr 索引库信息。注意记得注入 SolrService 对象
        if(cid ==null){
            userContent.setUpvote( 0 );
            userContent.setDownvote( 0 );
            userContent.setCommentNum( 0 );
            userContentService.addContent( userContent );
            solrService.addUserContent(userContent);

        }else {
            userContentService.updateById(userContent);
            solrService.updateUserContent(userContent);
        }

        model.addAttribute("content",userContent);

        return "write/writesuccess";
    }


    /**
     * 根据文章id查看文章
     * @param model
     * @param cid
     * @return
     */
    @RequestMapping("/watch")
    public String watchContent(Model model,@RequestParam(value = "cid",required = false)Long cid){
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            //未登录
            model.addAttribute( "error","请先登录！" );
            return "../login";
        }

        UserContent userContent = userContentService.findById(cid);
        model.addAttribute("cont",userContent);
        return "personal/watch";
    }






}
