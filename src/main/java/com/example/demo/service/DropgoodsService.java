package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Dropgoods;
import com.example.demo.repositry.DropgoodsRepository;

@Service
public class DropgoodsService {
	private final DropgoodsRepository dropgoodsRepository;
	@Autowired
		public DropgoodsService(DropgoodsRepository dropgoodsRepository) {
		this.dropgoodsRepository = dropgoodsRepository;
	}
	
	//accountidの人が落札した商品をListにして返す
	public List<Dropgoods> getMyDropItem(int accountId){
		
		List<Dropgoods>myItem = dropgoodsRepository.getMyDropItem(accountId);
		
		return myItem;
	}
	
	//goods_idから全取得
	public Dropgoods allgoodsSelect(int goodsId) {
		
		Dropgoods item = dropgoodsRepository.allgoodsSelect(goodsId);
		
		return item;
	}
	
}
