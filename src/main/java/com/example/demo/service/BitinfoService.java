package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Bitinfo;
import com.example.demo.repositry.BitRepositry;

@Service
public class BitinfoService {

	private final BitRepositry bitRepository;

	public BitinfoService(BitRepositry bitRepository) {
		this.bitRepository = bitRepository;
	}

	public Bitinfo bidList(int goodsId){

		Bitinfo bidList = bitRepository.findBidInfoSummaryByGoodsId(goodsId);

		return bidList;

	}
	
	public List<Bitinfo> findByGoods(int productId) {
		List<Bitinfo> bidList = bitRepository.findByGoodsId(productId);
        
        return bidList;
    }

	public int insertbid(int goodsId,int accountId,LocalDateTime bidTime,int currentPrice) {
		int x = bitRepository.insertBid(goodsId, accountId, bidTime, currentPrice);
		return x;
	}
	
	 //最高額取得
	public int highPrice(int goodsId) {
	    Integer price = bitRepository.highPrice(goodsId); // Integer型を使用し、nullを受け入れる
	    if (price != null) {
	        return price; // priceがnullでない場合はそのまま返す
	    } else {
	        return 0; // もしくは適切なデフォルト値を返す
	    }
	}
	

	public List<Bitinfo> bidListmypage(int goodsId){

		List<Bitinfo> bidList = bitRepository.findHighestCurrentPriceByAccountId(goodsId);

		return bidList;

	}
	
		
}