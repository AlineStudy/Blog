package com.xzp.service;

import com.xzp.model.UserContent;
import com.xzp.utils.PageHelper;

public interface SolrService {


    /**
     * 根据关键字搜索文章并且分页
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageHelper.Page<UserContent> findByKeyWords(String keyword, Integer pageNum, Integer pageSize);


    /**
     * 添加文章到SOlr索引库中
     * @param userContent
     */
    void addUserContent(UserContent userContent);

    /**
     * 跟新索引库
     * @param userContent
     */
    void updateUserContent(UserContent userContent);


    /**
     * 根据索引id删除索引库
     * @param id
     */
    void deleteById(Long id);

}
