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

    // すべての商品リストを取得するメソッド
    public List<GoodsList> getAllGoodsList() {
        List<GoodsList> goodsList = new ArrayList<>();
        // 実際の処理は未実装のため、空のリストを返します
        return goodsList;
    }

    // 商品リストを取得するメソッド（入札額と入札回数が最も高い商品）
    public List<GoodsList> goodsList() {
        List<GoodsList> goodsList = goodsListRepository.findGoodsWithHighestBidAndBidCount();
        return goodsList;
    }

    // ジャンル別の商品リストを取得するメソッド
    public List<GoodsList> goodsList2(int genre_id) {
        List<GoodsList> goodsList;
        if (genre_id == 0) {
            goodsList = goodsListRepository.findGoodsWithHighestBidAndBidCount();
        } else {
            goodsList = goodsListRepository.findGoodsByGenre(genre_id);
        }
        return goodsList;
    }

    // ジャンル名を取得するメソッド
    public String genreList(int genre_ID) {
        String genre = genreRepository.getGenreName(genre_ID);
        System.out.println(genre + "6");
        return genre;
    }

    // キーワードとジャンルを指定して商品を検索するメソッド
    public List<GoodsList> searchGoodsByKeywordAndGenre(String keyword, int genre_id) {
        List<GoodsList> list = goodsListRepository.searchGoodsByKeywordAndGenre(keyword, genre_id);
        return list;
    }

    // キーワードで商品を検索するメソッド
    public List<GoodsList> searchGoodsByKeyword(String keyword) {
        System.out.println(keyword);
        List<GoodsList> list = goodsListRepository.searchGoodsByKeyword(keyword);
        return list;
    }

    // キーワードとジャンルを指定して商品を検索するメソッド（特定のユーザーの商品検索）
    public List<GoodsList> searchGoodsByKeywordAndGenre1(String keyword, int genre_id, int accountId) {
        List<GoodsList> list = goodsListRepository.searchGoodsByKeywordAndGenre1(keyword, genre_id, accountId);
        return list;
    }

    // キーワードで商品を検索するメソッド（特定のユーザーの商品検索）
    public List<GoodsList> searchGoodsByKeyword1(String keyword, int accountId) {
        System.out.println(keyword);
        List<GoodsList> list = goodsListRepository.searchGoodsByKeyword1(keyword, accountId);
        return list;
    }

    // 特定のユーザーの商品リストを取得するメソッド（入札額と入札回数が最も高い商品）
    public List<GoodsList> goodsList1(int accountId) {
        List<GoodsList> goodsList = goodsListRepository.findGoodsWithHighestBidAndBidCount1(accountId);
        return goodsList;
    }

    // 特定のユーザーの商品リストを取得するメソッド（ジャンル別）
    public List<GoodsList> goodsList21(int genre_id, int accountId) {
        List<GoodsList> goodsList;
        if (genre_id == 0) {
            goodsList = goodsListRepository.findGoodsWithHighestBidAndBidCount1(accountId);
        } else {
            goodsList = goodsListRepository.findGoodsByGenre1(genre_id, accountId);
        }
        return goodsList;
    }
}
