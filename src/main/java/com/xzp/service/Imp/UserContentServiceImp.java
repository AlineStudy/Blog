package com.xzp.service.Imp;

import com.xzp.dao.CommentMapper;
import com.xzp.dao.UserContentMapper;
import com.xzp.model.Comment;
import com.xzp.model.UserContent;
import com.xzp.service.UserContentService;
import com.xzp.utils.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class UserContentServiceImp implements UserContentService {

    @Autowired
    private UserContentMapper userContentMapper;
    @Autowired
    private CommentMapper commentMapper;



    @Override
    public PageHelper.Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize) {
        //分页查询
        System.out.println("第"+pageNum+"页");
        System.out.println("每页显示: " + pageSize + "条");
        PageHelper.startPage(pageNum,pageSize);  //开始分页
        List<UserContent> list = userContentMapper.findByJoin(content);
        PageHelper.Page endPage = PageHelper.endPage();//分页结束
        List<UserContent> result = endPage.getResult();
        return endPage;
    }

    @Override
    public PageHelper.Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize) {
        //分页查询
        System.out.println("第"+pageNum+"页");
        System.out.println("每页显示："+pageSize+"条");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list =  userContentMapper.select( content );

        List<Comment> comments = commentMapper.select( comment );

        PageHelper.Page endPage = PageHelper.endPage();//分页结束
        List<UserContent> result = endPage.getResult();
        return endPage;
    }

    @Override
    public PageHelper.Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize) {
        Example e = new Example(UserContent.class);
        e.setOrderByClause("upvote DESC");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.selectByExample(e);
        PageHelper.Page endPage = PageHelper.endPage();//分页结束
        return endPage;
    }



    @Override
    public PageHelper.Page<UserContent> findAll(Integer pageNum, Integer pageSize) {
        System.out.println("第"+pageNum+"页");
        System.out.println("每页显示："+pageSize+"条");
        //分页查询
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list =  userContentMapper.findByJoin(null);
        PageHelper.Page endPage = PageHelper.endPage();//分页结束
        return endPage;
    }



    @Override
    public PageHelper.Page<UserContent> findByCategory(String category, Long userId, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();

        if (StringUtils.isBlank(category) && !"null".equals(category)){
            userContent.setCategory(category);
        }
        userContent.setUserId(userId);
        userContent.setPersonal("0");
        PageHelper.startPage(pageNum,pageSize);
        userContentMapper.select(userContent);
        PageHelper.Page endPage = PageHelper.endPage();
        return endPage;
    }

    @Override
    public void addContent(UserContent content) {
        userContentMapper.insertContent(content);
    }

    @Override
    public List<UserContent> findByUserId(Long userId) {
        UserContent userContent = new UserContent();
        userContent.setUserId(userId);
        List<UserContent> list = userContentMapper.select(userContent);
        return list;
    }

    @Override
    public List<UserContent> findAll() {
        return userContentMapper.select(null);
    }

    @Override
    public UserContent findById(Long id){
        UserContent userContent = new UserContent();
        userContent.setId(id);
        List<UserContent> list = userContentMapper.findByJoin(userContent);
        if(list!=null && list.size()>0){
            return list.get(0);
        }else {
            return null;
        }
    }

    @Override
    public void updateById(UserContent content) {
        userContentMapper.updateByPrimaryKeySelective(content);
    }

    @Override
    public List<UserContent> findCategoryByUserId(Long userId) {
        return userContentMapper.findCategoryByUserId(userId);
    }

    @Override
    public PageHelper.Page<UserContent> findPersonal(Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        userContent.setUserId(uid);
        userContent.setPersonal("1");
        PageHelper.startPage(pageNum,pageSize);
        userContentMapper.select(userContent);
        PageHelper.Page endPage = PageHelper.endPage();
        return endPage;
    }

    @Override
    public void deleteById(Long cid) {
        userContentMapper.deleteByPrimaryKey(cid);
    }
}
