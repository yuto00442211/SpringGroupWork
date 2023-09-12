package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Goods;
import com.example.demo.repositry.GoodsRepositry;

@Service
public class GoodsService {
	private final GoodsRepositry goodsRepository;
	private BitinfoService bitinfoService ;


	public GoodsService(GoodsRepositry goodsRepository) {
		this.goodsRepository = goodsRepository;
	}

	public Goods getGoodsById(int goodsId) {
		return goodsRepository.findById(goodsId).orElse(null); // 商品が存在しない場合はnullを返す
	}


	public List<Goods> getAllGoods() {
		List<Goods> goodsList = new ArrayList<>();
		goodsRepository.findAll().forEach(goodsList::add);
		return goodsList;
	}

	public int GetNowPrice(int goodsId) {
		return bitinfoService.highPrice(goodsId);

	}

	//前のファイル名取得
	public String getImageName(int goods_Id) {

		String imageName = goodsRepository.getImageName(goods_Id);


		return imageName;
	}

	public int findAccountIdByGoodsId(int goodsId) {
		
		int x = goodsRepository.findAccountIdByGoodsId(goodsId);

		return x;
	}
	
	
}
