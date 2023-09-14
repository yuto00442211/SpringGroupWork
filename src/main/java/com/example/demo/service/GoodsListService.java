package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.GoodsList;
import com.example.demo.repositry.GenreRepository;
import com.example.demo.repositry.GoodsListRepositry;

@Service
public class GoodsListService {
	
	 private GoodsListRepositry goodsListRepository;
	 
	 @Autowired
	private GenreRepository genreRepository;


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
	
	//ジャンルが何か判定
		public String genreList(int genre_ID) {
			
			String genre =genreRepository.getGenreName(genre_ID);
			System.out.println(genre+"6");
		
			return genre;
		}
		
		//ジャンルとキーワードを指定して
		public List<GoodsList>searchGoodsByKeywordAndGenre(String keyword, int genre_id){
			
			 List<GoodsList>list = goodsListRepository.searchGoodsByKeywordAndGenre(keyword, genre_id);
			
			return list;
		}
		
		//キーワード検索
		public List<GoodsList>searchGoodsByKeyword(String keyword){
			
			System.out.println(keyword);
			
			 List<GoodsList>list = goodsListRepository.searchGoodsByKeyword(keyword);
			
			return list;
		}
}
