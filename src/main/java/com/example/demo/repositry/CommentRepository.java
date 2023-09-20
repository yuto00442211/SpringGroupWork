package com.example.demo.repositry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Integer> {
	
	// 商品IDを指定して、その商品に関連するコメントを取得するカスタムクエリ
	@Query("SELECT * FROM comment WHERE goods_id = :goodsId")
	List<Comment> findByGoodsId(@Param("goodsId") int goodsId);
	
	// 商品IDとアカウントIDリストを指定して、指定したアカウントが関与したコメントをコメント時間の昇順で取得するカスタムクエリ
	@Query("SELECT *\n"
			+ "FROM Comment c\n"
			+ "WHERE c.goods_id = :goodsId\n"
			+ "AND c.account_id IN (:accountIdList)\n"
			+ "AND c.goodsaccount_id IN (:goodsaccountIdList)\n"
			+ "ORDER BY c.comment_time ASC;")
	List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(
	    @Param("goodsId") int goodsId, 
	    @Param("accountIdList") List<Integer> accountIdList,
	    @Param("goodsaccountIdList") List<Integer> goodsaccountIdList);

	// コメントを新しく挿入するカスタムクエリ
	@Modifying
	@Query(value = "INSERT INTO comment (goods_id, account_id, comment_time, comment_content,goodsaccount_id) VALUES (:goods_id, :account_id, :comment_time, :comment_content,:goodsaccount_id)")
	int insertComment(@Param("goods_id") int goodsId, @Param("account_id") int accountId, @Param("comment_time") LocalDateTime commentTime, @Param("comment_content") String comment,@Param("goodsaccount_id")int goodsaccount);
	
	// 出品側の未読コメントの最新コメントを取得するカスタムクエリ
	@Query("SELECT * FROM Comment WHERE goods_id = :goods_id ORDER BY comment_time ASC LIMIT 1")
	Comment findLatestCommentsByGoodsId(@Param("goods_id") int goodsId);

	// アイテム別のメールボックスを取得するカスタムクエリ
	@Query("SELECT c.account_id, c.goods_id, c.comment_number, c.comment_content, c.comment_time, c.is_approved\n"
			+ "FROM comment c\n"
			+ "WHERE c.goods_id = :goods_id\n"
			+ "AND (c.account_id, c.comment_time) IN (\n"
			+ "    SELECT account_id, MAX(comment_time) AS max_comment_time\n"
			+ "    FROM comment\n"
			+ "    WHERE goods_id = :goods_id\n"
			+ "    AND account_id <> :account_id\n" // account_idが指定されたもの以外を取得
			+ "    GROUP BY account_id);")
	List<Comment> findLatestCommentByGoodsId(@Param("goods_id") int goodsId, @Param("account_id") int account_id);

	// 出品者側のコメントを取得するカスタムクエリ
	@Query("SELECT *\n"
			+ "FROM Comment c\n"
			+ "WHERE c.goods_id = :goodsId\n"
			+ "AND c.account_id IN (:accountIdList)\n"
			+ "AND c.goodsaccount_id IN (:goodsaccountIdList)\n"
			+ "ORDER BY c.comment_time ASC;")
	List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc2(
	    @Param("goodsId") int goodsId, 
	    @Param("accountIdList") List<Integer> accountIdList,
	    @Param("goodsaccountIdList") List<Integer> goodsaccountIdList);

	// 出品者側のコメントを新しく挿入するカスタムクエリ
	@Modifying
	@Query(value = "INSERT INTO comment (goods_id, account_id, comment_time, comment_content,goodsaccount_id) VALUES (:goods_id, :account_id, :comment_time, :comment_content,:goodsaccount_id)")
	int insertCommenm2(@Param("goods_id") int goodsId, @Param("account_id") int accountId, @Param("comment_time") LocalDateTime commentTime, @Param("comment_content") String comment,@Param("goodsaccount_id")int goodsaccount);

	// コメントの承認状態を更新するカスタムクエリ
	@Modifying
	@Query("UPDATE comment SET is_approved = true WHERE goods_id=:goodsId AND account_id=:accountId AND goodsaccount_id=:goodsaccountId")
	int updateIsApprovedByGoodsIdAndAccountIds(@Param("goodsId") int goodsId, @Param("accountId") int accountId, @Param("goodsaccountId") int goodsaccountId);

	// 入札側の未承認コメントが存在するかを確認するカスタムクエリ
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Comment c WHERE c.goods_id = :goods_id AND c.account_id = :account_id AND c.is_approved = false")
    Boolean existsUnapprovedComments(@Param("goods_id") int goodsId, @Param("account_id") int accountId);
}
