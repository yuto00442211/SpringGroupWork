package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.GoodsList;
import com.example.demo.repositry.GoodsListRepositry;

@Service
public class GoodsListService {
	
	 private GoodsListRepositry goodsListRepository;

	public GoodsListService(GoodsListRepositry goodsListRepository) {
	        this.goodsListRepository = goodsListRepository;
	    }
 
	public List<GoodsList> getAllGoodsList() {
        List<GoodsList> goodsList = new ArrayList<>();
        
        
        return goodsList;
    }
	
	public List<GoodsList>goodsList(){
		
		List<GoodsList> goodsList = goodsListRepository.findGoodsWithHighestBidAndBidCount();
		
		return goodsList;
		
	}
	
	public List<GoodsList> goodsList2(int genre_id){
		List<GoodsList> goodsList;
		if(genre_id==0) {
			goodsList = goodsListRepository.findGoodsWithHighestBidAndBidCount();
		}else {
			goodsList = goodsListRepository.findGoodsByGenre(genre_id);
		}
		return goodsList;
	}
	
	//ジャンルが何か判定と設定
		public String genreList(int genre_ID) {
			String genre="";
			switch (genre_ID) {
			case 0: genre = "全商品";break;
			case 1: genre = "家電製品";break;
			case 2: genre = "服";break;
			}
			return genre;
		}

}
