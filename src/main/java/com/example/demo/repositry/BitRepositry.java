package com.example.demo.repositry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Bitinfo;

public interface BitRepositry extends CrudRepository<Bitinfo, Integer> {


	@Query("SELECT MAX(b.currentPrice) AS highestPrice, COUNT(b) AS bidCount " +
			"FROM bitinfo b " +
			"WHERE b.goods.goods_id = :goodsId")
	Bitinfo findBidInfoSummaryByGoodsId(@Param("goodsId") int goodsId);

	@Query("SELECT * FROM bitinfo WHERE goods_id = :goodsId")
	List<Bitinfo> findByGoodsId(@Param("goodsId") int goodsId);

	@Modifying
	@Query("INSERT INTO bitinfo (goods_id, account_id, bid_time, current_price) " +
			"VALUES (:goodsId, :accountId, :bidTime, :currentPrice)")
	int insertBid(@Param("goodsId") int goodsId, @Param("accountId") int accountId,
			@Param("bidTime") LocalDateTime bidTime, @Param("currentPrice") int currentPrice);

	//最高額取得
	@Query("SELECT MAX(current_price) FROM bitinfo WHERE goods_id = :goodsId")
	Integer highPrice(@Param("goodsId")int goodsID);

	//購入履歴
	@Query("SELECT goods_id, MAX(current_price) AS current_price, MAX(bid_time) AS bid_time\n"
			+ "FROM bitinfo\n"
			+ "WHERE account_id = :account_id  -- :accountId\n"
			+ "GROUP BY goods_id;\n"
			+ "")
	List<Bitinfo> findHighestCurrentPriceByAccountId(@Param("account_id") int accountId);

}


