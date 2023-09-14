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

	public List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc( int goodsId,List<Integer> accountIds,List<Integer> accountaccountIds){

		List<Comment> comments = commentRepository.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(goodsId,accountIds,accountaccountIds);

		return comments ;
	}

	public int insertComment(int goodsId,int accountId,LocalDateTime commentTime,String comment,int goodsaccount) {
		int x = commentRepository.insertComment(goodsId, accountId, commentTime, comment,goodsaccount);
		return x;
	}

	public Comment lookComment(int goodsId) {

		Comment comment = commentRepository.findLatestCommentsByGoodsId(goodsId);

		return comment ;
	}

	public List<Comment>mailList(int goodsId,int accountId){
		System.out.println("h"+goodsId);
		List<Comment> mailList = commentRepository.findLatestCommentByGoodsId(goodsId,accountId);
		return mailList;
	}

	public List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc2( int goodsId,List<Integer> accountIds,List<Integer> accountaccountIds){

		List<Comment> comments = commentRepository.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc2(goodsId,accountIds,accountaccountIds);

		return comments ;
	}
	
	public int updateIsApprovedByGoodsIdAndAccountIds(int goodsId,int accountId, int goodsaccountId) {
        return commentRepository.updateIsApprovedByGoodsIdAndAccountIds(goodsId, accountId, goodsaccountId);
    }
	
	public boolean existsUnapprovedComments(int goodsId,  int accountId) {
		
		boolean xxx = commentRepository.existsUnapprovedComments(goodsId, accountId);
		
		return xxx;
	}
} 
