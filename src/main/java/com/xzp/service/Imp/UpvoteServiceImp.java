package com.xzp.service.Imp;

import com.xzp.dao.UpvoteMapper;
import com.xzp.model.Upvote;
import com.xzp.service.UpvoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UpvoteServiceImp implements UpvoteService {


    private UpvoteMapper upvoteMapper;

    @Autowired
    public void setUpvoteMapper(UpvoteMapper upvoteMapper) {
        this.upvoteMapper = upvoteMapper;
    }

    @Override
    public Upvote findByUserIdAndConId(Upvote upvote) {
        return upvoteMapper.selectOne(upvote);
    }

    @Override
    public int add(Upvote upvote) {
        return upvoteMapper.insert(upvote);
    }

    @Override
    public Upvote getByUserId(Upvote upvote) {
        return upvoteMapper.selectOne(upvote);
    }

    @Override
    public void update(Upvote upvote) {
        upvoteMapper.updateByPrimaryKey(upvote);
    }

    @Override
    public void deleteByContentId(Long cid) {
        Upvote upvote = new Upvote();
        upvote.setContentId(cid);
        upvoteMapper.delete(upvote);
    }
}
