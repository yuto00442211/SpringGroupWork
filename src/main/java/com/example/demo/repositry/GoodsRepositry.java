package com.example.demo.repositry;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Goods;

public interface GoodsRepositry extends CrudRepository<Goods, Integer>{

	//旧ファイル名取得
		@Query("select image_number From goods where goods_id =:goods_id")
		String getImageName(@Param("goods_id") int goods_id);
		
		
		// goods_idに対応するaccount_idを取得するクエリ
	    @Query("SELECT g.account_id FROM Goods g WHERE g.goods_id = :goods_id")
	    Integer findAccountIdByGoodsId(@Param("goods_id") int goodsId);
		
	  //出品中リスト作成
	    @Query("select * FROM Goods WHERE account_id = :account_id")
	    List<Goods> getMyItem(@Param("account_id") int accountId);
	    
	    //追加
	    @Query("select * From goods where goods_id = :goodsId")
	    Goods allgoodsSelect(@Param("goodsId") int goodsId);
		
}

