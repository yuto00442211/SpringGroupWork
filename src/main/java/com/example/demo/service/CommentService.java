package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Comment;
import com.example.demo.repositry.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByGoodsId(int goods_id) {
        return commentRepository.findByGoodsId(goods_id);
    }
    
    public List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc( int goodsId,List<Integer> accountIds){
    	
    	List<Comment> comments = commentRepository.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(goodsId,accountIds);
    	
    	return comments ;
    }
    
    public int insertComment(int goodsId,int accountId,LocalDateTime commentTime,String comment) {
		int x = commentRepository.insertComment(goodsId, accountId, commentTime, comment);
		return x;
	}
}
