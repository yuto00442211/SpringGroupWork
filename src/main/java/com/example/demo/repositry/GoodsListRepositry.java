package com.example.demo.repositry;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.GoodsList;

public interface GoodsListRepositry extends CrudRepository<GoodsList, Integer> {

	@Query("SELECT g.goods_id AS goods_id, g.name AS name,COALESCE(MAX(b.current_price), 0) AS current_price, " +
		       "COALESCE(COUNT(b.account_id), 0) AS bidcount, " + // COUNTを使用して集約関数に変更
		       "g.end_time AS end_time, g.image_number AS image_number " +
		       "FROM goods g " +
		       "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " + // LEFT JOINで結合
		       "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " + // GROUP BYで指定
		       "ORDER BY current_price DESC")
		List<GoodsList> findGoodsWithHighestBidAndBidCount();

	@Query("SELECT g.goods_id AS goods_id, g.name AS name,COALESCE(MAX(b.current_price), 0) AS current_price, " +
		       "COALESCE(COUNT(b.account_id), 0) AS bidcount, " + // COUNTを使用して集約関数に変更
		       "g.end_time AS end_time, g.image_number AS image_number " +
		       "FROM goods g " +
		       "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " + // LEFT JOINで結合
		       "WHERE g.genre_id = :genre_id " +
		       "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " + // GROUP BYで指定
		       "ORDER BY current_price DESC")
    List<GoodsList> findGoodsByGenre(@Param("genre_id") int genre_id);
	
	@Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
		       "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
		       "g.end_time AS end_time, g.image_number AS image_number " +
		       "FROM goods g " +
		       "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
		       "WHERE g.genre_id = :genre_id AND g.name LIKE '%' || :name || '%' " + // :name パラメータをバインド
		       "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
		       "ORDER BY current_price DESC")
		List<GoodsList> searchGoodsByKeywordAndGenre(@Param("name") String name, @Param("genre_id") int genre_id);
	
	@Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
		       "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
		       "g.end_time AS end_time, g.image_number AS image_number " +
		       "FROM goods g " +
		       "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
		       "WHERE g.name LIKE '%' || :name || '%' " + // nameであいまい検索
		       "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
		       "ORDER BY current_price DESC")
	List<GoodsList> searchGoodsByKeyword(@Param("name") String name);


	
}
