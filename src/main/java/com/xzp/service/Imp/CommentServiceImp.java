package com.xzp.service.Imp;

import com.xzp.dao.CommentMapper;
import com.xzp.model.Comment;
import com.xzp.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImp implements CommentService {


    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    public void setCommentMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public int add(Comment comment) {
        return commentMapper.insert(comment);
    }

    @Override
    public void update(Comment comment) {
        commentMapper.updateByPrimaryKey(comment);
    }

    @Override
    public List<Comment> findAll(Long contentId) {
        Comment comment = new Comment();
        comment.setConId(contentId);
        return commentMapper.select(comment);
    }

    @Override
    public Comment findById(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        return commentMapper.selectOne(comment);
    }

    @Override
    public List<Comment> findAllFirstComment(Long contentId) {
        Comment comment = new Comment();
        comment.setConId(contentId);
        return commentMapper.findAllFirstComment(contentId);
    }

    @Override
    public List<Comment> findAllChildrenComment(Long contentId, String children) {
        Comment comment = new Comment();
        comment.setConId(contentId);
        comment.setChildren(children);
        return commentMapper.findAllChildrenComment(contentId,children);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = new Comment();
        comment.setId(id);
        commentMapper.deleteByPrimaryKey(comment);
    }

    @Override
    public void deleteChildrenComment(String children) {
        Example example = new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        List<Object> list = new ArrayList<>();
        String[] split = children.split(",");
        for (int i = 0 ; i < split.length ; i++){
            list.add(split[i]);
        }
        criteria.andIn("id",list);
        commentMapper.deleteByExample(example);
    }

    @Override
    public void deleteByContentId(Long cid) {
        Comment comment = new Comment();
        comment.setConId(cid);
        commentMapper.delete(comment);
    }
}
