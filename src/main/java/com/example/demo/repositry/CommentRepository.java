package com.example.demo.repositry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
	
	 @Query("SELECT * FROM comment WHERE goods_id = :goodsId")
	    List<Comment> findByGoodsId(@Param("goodsId") int goodsId);
	 
	 @Query("SELECT * FROM Comment c WHERE c.goods_id = :goodsId AND c.account_id IN (:accountIds) ORDER BY c.comment_time ASC\n"
	 		+ "")
	 List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(@Param("goodsId") int goodsId, @Param("accountIds") List<Integer> accountIds);

	 
	 @Modifying
	 @Query(value = "INSERT INTO comment (goods_id, account_id, comment_time, comment_content) VALUES (:goods_id, :account_id, :comment_time, :comment_content)")
	 int insertComment(@Param("goods_id") int goodsId, @Param("account_id") int accountId, @Param("comment_time") LocalDateTime commentTime, @Param("comment_content") String comment);
}
