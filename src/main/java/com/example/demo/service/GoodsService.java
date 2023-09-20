package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Bitinfo;
import com.example.demo.entity.Goods;
import com.example.demo.repositry.GoodsRepositry;

@Service
public class GoodsService {
    private final GoodsRepositry goodsRepository;
    @Autowired
    private BitinfoService bitinfoService;
    public String uploadDirectory = System.getProperty("user.dir") + "\\images\\"; // ファイルを保存するディレクトリのパス

    public GoodsService(GoodsRepositry goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    // 特定の商品IDに対応する商品を取得
    public Goods getGoodsById(int goodsId) {
        return goodsRepository.findById(goodsId).orElse(null); // 商品が存在しない場合はnullを返す
    }

    // すべての商品を取得
    public List<Goods> getAllGoods() {
        List<Goods> goodsList = new ArrayList<>();
        goodsRepository.findAll().forEach(goodsList::add);
        return goodsList;
    }

    // 出品者を除くピックアップ商品を取得
    public List<Goods> getAllGoods2(int accountId) {
        List<Goods> goodsList = new ArrayList<>();
        goodsRepository.findAllAll(accountId);
        return goodsList;
    }

    // 商品の現在価格を取得
    public int GetNowPrice(Goods g) {
        return bitinfoService.highPrice(g);
    }

    // 前のファイル名を取得
    public String getImageName(int goods_Id) {
        String imageName = goodsRepository.getImageName(goods_Id);
        return imageName;
    }

    // 商品IDからアカウントIDを取得
    public int findAccountIdByGoodsId(int goodsId) {
        Integer x = goodsRepository.findAccountIdByGoodsId(goodsId);

        if (x == null) {
            x = -1;
        }

        return x;
    }

    // 出品アイテムを取得（出品者を除く）
    public List<Goods> getMyItem(int accountId) {
        List<Goods> myItem = goodsRepository.getMyItem(accountId);
        return myItem;
    }

    // 商品IDから商品情報を取得
    public Goods allgoodsSelect(int goodsId) {
        Goods item = goodsRepository.allgoodsSelect(goodsId);
        return item;
    }

    // オークション終了判定
    public boolean timeUp(LocalDateTime nowTime, LocalDateTime endTime, Goods g, Bitinfo mybit) {
        boolean xxx = nowTime.isAfter(endTime);

        if (xxx) {
            if (mybit.getCurrent_price() == bitinfoService.highPrice(g)) {
            } else {
                xxx = false;
            }
        }
        return xxx;
    }

    // 画像を保存
    public String saveImage(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();

        // ファイルを指定のディレクトリに保存
        File destinationFile = new File(uploadDirectory + originalFileName);
        file.transferTo(destinationFile);

        return originalFileName; // 元のファイル名をそのまま返す
    }

    // ランダムな要素を指定数取得
    public List<Goods> getRandomElements(List<Goods> list, int count) {
        List<Goods> randomElements = new ArrayList<>();

        if (list == null || list.isEmpty() || count <= 0) {
            return randomElements; // 空のリストを返す
        }

        int size = list.size();
        if (size <= count) {
            return new ArrayList<>(list); // リストのサイズが指定数以下の場合はすべての要素を返す
        } else {
            Random random = new Random();
            while (randomElements.size() < count) {
                int randomIndex = random.nextInt(size);
                Goods randomElement = list.get(randomIndex);
                if (!randomElements.contains(randomElement)) {
                    randomElements.add(randomElement);
                }
            }
            return randomElements; // ランダムに選択された要素が入ったリストを返す
        }
    }

    // 商品の出品者のアカウントIDを取得
    public int getGoodsAccountID(int goodsId) {
        return goodsRepository.getGoodsAccountID(goodsId);
    }
}
