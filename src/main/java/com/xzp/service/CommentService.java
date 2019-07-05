package com.xzp.service;

import com.xzp.model.Comment;

import java.util.List;


public interface CommentService {


    /**
     * 添加评论
     * @param comment
     * @return
     */
    int add(Comment comment);


    /**
     * 更新评论
     * @param comment
     */
    void update(Comment comment);

    /**
     * 根据文章id查询所有评论
     * @param contentId
     * @return
     */
    List<Comment> findAll(Long contentId);

    /**
     * 根据id查询评论
     * @param id
     * @return
     */
    Comment findById(Long id);

    /**
     * 根据文章id查询所有父评论
     * @param contentId
     * @return
     */
    List<Comment> findAllFirstComment(Long contentId);

    /**
     * 根据文章id和子评论ids查询所有子评论
     * @param contentId
     * @param children
     * @return
     */
    List<Comment> findAllChildrenComment(Long contentId,String children);


    /**
     * 根据id删除评论
     * @param id
     */
    void deleteById(Long id);

    /**
     * 批量删除子评论
     * @param children
     */
    void deleteChildrenComment(String children);


    /**
     * 根据文章id删除评论
     * @param cid
     */
    void deleteByContentId(Long cid);
}
