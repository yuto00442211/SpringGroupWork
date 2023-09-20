package com.example.demo.repositry;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Dropgoods;

public interface DropgoodsRepository  extends CrudRepository<Dropgoods, Integer>{
	  //落札商品リスト作成
    @Query("select * FROM Dropgoods WHERE account_id = :account_id")
    List<Dropgoods> getMyDropItem(@Param("account_id") int accountId);
    //追加
    @Query("select * From goods where goods_id = :goodsId")
    Dropgoods allgoodsSelect(@Param("goodsId") int goodsId);
	
}
