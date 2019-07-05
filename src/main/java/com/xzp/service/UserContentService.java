package com.xzp.service;

import com.xzp.model.Comment;
import com.xzp.model.UserContent;
import com.xzp.utils.PageHelper;

import java.util.List;

public interface UserContentService {


    /**
     * 查询所有Content并分页
     * @param content
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageHelper.Page<UserContent> findAll(UserContent content,Integer pageNum,Integer pageSize);
    PageHelper.Page<UserContent> findAll(UserContent content, Comment comment,Integer pageNum,Integer pageSize);
    PageHelper.Page<UserContent> findAllByUpvote(UserContent content,Integer pageNum,Integer pageSize);


    /**
     * 根据发布时间倒排序并分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageHelper.Page<UserContent> findAll(Integer pageNum,Integer pageSize);


    PageHelper.Page<UserContent> findByCategory(String category,Long userId,Integer pageNum,Integer pageSize);


    /**
     * 添加文章
     * @param content
     */
    void addContent(UserContent content);

    /**
     * 根据用户id查询文章集合
     * @param userId
     * @return
     */
    List<UserContent> findByUserId(Long userId);

    /**
     * 查询所有文章
     * @return
     */
    List<UserContent> findAll();

    /**
     * 根据文章id查询文章
     * @param id
     * @return
     */
    UserContent findById(Long id);


    /**
     * 根据文章id更新文章
     * @param content
     */
    void updateById(UserContent content);


    /**
     * 根据用户id查出梦分类
     * @param userId
     * @return
     */
    List<UserContent> findCategoryByUserId(Long userId);







    /**
     * 根据用户id查询所有文章私密并分页
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageHelper.Page<UserContent> findPersonal(Long uid ,Integer pageNum, Integer pageSize);


    /**
     * 根据文章id删除文章
     * @param cid
     */
    void deleteById(Long cid);




}
