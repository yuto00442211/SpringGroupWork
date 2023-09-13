package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Goods;
import com.example.demo.repositry.GoodsRepositry;

@Service
public class GoodsService {
	private final GoodsRepositry goodsRepository;
	private BitinfoService bitinfoService ;
	 public String uploadDirectory=System.getProperty("user.dir")+"\\images\\"; // ファイルを保存するディレクトリのパス

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
	
	  public String saveImage(MultipartFile file) throws IOException {
          String originalFileName = file.getOriginalFilename();

          // ファイルを指定のディレクトリに保存
          File destinationFile = new File(uploadDirectory + originalFileName);
          file.transferTo(destinationFile);

          return originalFileName; // 元のファイル名をそのまま返す
      
  }
	  
      
	  
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

}
