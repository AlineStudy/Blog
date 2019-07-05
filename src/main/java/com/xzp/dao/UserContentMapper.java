package com.xzp.dao;

import com.xzp.model.UserContent;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserContentMapper extends Mapper<UserContent> {


    /**
     * 根据用户id查询出梦分类
     * @param userId
     * @return
     */
    List<UserContent> findCategoryByUserId(@Param("userId")Long userId);


    /**
     *  插入文章并返回主键id 返回类型只是影响行数  id在UserContent对象中
     * @param userContent
     * @return
     */
    int insertContent(UserContent userContent);


    /**
     * user_content与user连接查询
     * @param userContent
     * @return
     */
    List<UserContent> findByJoin(UserContent userContent);

}
