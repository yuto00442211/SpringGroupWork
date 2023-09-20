package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Bitinfo;
import com.example.demo.entity.Goods;
import com.example.demo.repositry.BitRepositry;

@Service
public class BitinfoService {

    private final BitRepositry bitRepository;

    public BitinfoService(BitRepositry bitRepository) {
        this.bitRepository = bitRepository;
    }

    // 商品IDを指定して入札情報を取得するメソッド
    public Bitinfo bidList(int goodsId) {
        Bitinfo bidList = bitRepository.findBidInfoSummaryByGoodsId(goodsId);
        return bidList;
    }

    // 商品IDを指定して入札情報をリストで取得するメソッド
    public List<Bitinfo> findByGoods(int productId) {
        List<Bitinfo> bidList = bitRepository.findByGoodsId(productId);
        return bidList;
    }

    // 入札情報を新しく挿入するメソッド
    public int insertBid(int goodsId, int accountId, LocalDateTime bidTime, int currentPrice) {
        int x = bitRepository.insertBid(goodsId, accountId, bidTime, currentPrice);
        return x;
    }

    // 商品に関連する最高額を取得するメソッド
    public Integer highPrice(Goods g) {
        Integer price = bitRepository.highPrice(g.getGoods_id()); // Integer型を使用し、nullを受け入れる
        if (price != null) {
            return price; // priceがnullでない場合はそのまま返す
        } else {
            return g.getInitial_price(); // もしくは適切なデフォルト値を返す
        }
    }

    // 商品IDを指定してユーザーの最高額入札情報を取得するメソッド
    public List<Bitinfo> bidListMypage(int goodsId) {
        List<Bitinfo> bidList = bitRepository.findHighestCurrentPriceByAccountId(goodsId);
        return bidList;
    }
}
