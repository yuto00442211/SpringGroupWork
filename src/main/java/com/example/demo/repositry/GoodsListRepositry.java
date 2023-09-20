package com.example.demo.repositry;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.GoodsList;

public interface GoodsListRepositry extends CrudRepository<GoodsList, Integer> {

    // 入札額と入札回数が最も高い商品リストを取得するクエリ
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " + // LEFT JOINで結合
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " + // GROUP BYで指定
           "ORDER BY current_price DESC")
    List<GoodsList> findGoodsWithHighestBidAndBidCount();

    // ジャンル別の商品リストを取得するクエリ
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " + // LEFT JOINで結合
           "WHERE g.genre_id = :genre_id " + // ジャンルIDをパラメータで指定
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " + // GROUP BYで指定
           "ORDER BY current_price DESC")
    List<GoodsList> findGoodsByGenre(@Param("genre_id") int genre_id);

    // キーワードとジャンルを指定して商品を検索するクエリ
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
           "WHERE g.genre_id = :genre_id AND g.name LIKE '%' || :name || '%' " + // ジャンルとキーワードをパラメータで指定
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
           "ORDER BY current_price DESC")
    List<GoodsList> searchGoodsByKeywordAndGenre(@Param("name") String name, @Param("genre_id") int genre_id);
    
    // キーワードで商品を検索するクエリ
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
           "WHERE g.name LIKE '%' || :name || '%' " + // キーワードであいまい検索
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
           "ORDER BY current_price DESC")
    List<GoodsList> searchGoodsByKeyword(@Param("name") String name);
    
    // 特定のユーザーの商品リストを取得するクエリ（入札額と入札回数が最も高い商品）
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
           "WHERE g.account_id <> :account_id " + // 特定のユーザー以外の商品を対象
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
           "ORDER BY current_price DESC")
    List<GoodsList> findGoodsWithHighestBidAndBidCount1(@Param("account_id") int account_id);

    // 特定のユーザーの商品リストを取得するクエリ（ジャンル別）
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
           "WHERE g.genre_id = :genre_id AND g.account_id <> :account_id " + // ジャンルと特定のユーザーを指定
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
           "ORDER BY current_price DESC")
    List<GoodsList> findGoodsByGenre1(@Param("genre_id") int genre_id, @Param("account_id") int account_id);

    // キーワードとジャンルを指定して商品を検索するクエリ（特定のユーザーの商品検索）
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
           "WHERE g.genre_id = :genre_id AND g.name LIKE '%' || :name || '%' " + // ジャンルとキーワードを指定
           "AND g.account_id <> :account_id " + // 特定のユーザー以外の商品を対象
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
           "ORDER BY current_price DESC")
    List<GoodsList> searchGoodsByKeywordAndGenre1(@Param("name") String name, @Param("genre_id") int genre_id, @Param("account_id") int account_id);
    
    // キーワードで商品を検索するクエリ（特定のユーザーの商品検索）
    @Query("SELECT g.goods_id AS goods_id, g.name AS name, COALESCE(MAX(b.current_price), 0) AS current_price, " +
           "COALESCE(COUNT(b.account_id), 0) AS bidcount, " +
           "g.end_time AS end_time, g.image_number AS image_number " +
           "FROM goods g " +
           "LEFT JOIN bitinfo b ON g.goods_id = b.goods_id " +
           "WHERE g.name LIKE '%' || :name || '%' " + // キーワードであいまい検索
           "AND g.account_id <> :account_id " + // 特定のユーザー以外の商品を対象
           "GROUP BY g.goods_id, g.name, g.end_time, g.image_number " +
           "ORDER BY current_price DESC")
    List<GoodsList> searchGoodsByKeyword1(@Param("name") String name, @Param("account_id") int account_id);
}
