package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Domain.UserPrincipal;
import com.example.demo.Form.AccountForm;
import com.example.demo.Form.BitForm;
import com.example.demo.Form.GoodsForm;
import com.example.demo.entity.Account;
import com.example.demo.entity.Bitinfo;
import com.example.demo.entity.Genre;
import com.example.demo.entity.Goods;
import com.example.demo.entity.GoodsList;
import com.example.demo.repositry.AccountRepositry;
import com.example.demo.repositry.BitRepositry;
import com.example.demo.repositry.GoodsRepositry;
import com.example.demo.service.AccountService;
import com.example.demo.service.BitinfoService;
import com.example.demo.service.GenreService;
import com.example.demo.service.GoodsListService;
import com.example.demo.service.GoodsService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/afterLogin")///afterLoginのリクエストがあればこのコントローラーで処理される
public class AfterController {

	@Autowired
	private AccountRepositry accountRepositry; // 商品情報の登録処理を行うサービスクラスをインジェクション


	@Autowired
	//	private @Bean // このメソッドの返り値をSpringのBeanとして登録します
	public PasswordEncoder passwordEncoder;// パスワードエンコーダー（パスワードのハッシュ化）を提供するメソッド

	@Autowired
	private GoodsRepositry goodsRepositry;  
	@Autowired
	private GoodsService goodsservice;
	@Autowired
	private BitRepositry bitRepositry;  
	@Autowired
	private GoodsListService goodsListService ;
	@Autowired
	private BitinfoService bitinfoService ;
	@Autowired
	private AccountService accountService ;
	@Autowired
	private GenreService genreService;

	
//	highPrice(int goodsId)
	
	/**
	 * トップページに遷移します.
	 * （引数はログインしたユーザーを取得するための記述）
	 * @return top.html
	 */
	@GetMapping("/sell")
	public String showTop(Model model,@AuthenticationPrincipal UserPrincipal userPrincipal) {
////////////////////////////////////////////
		int count=0;
		// ログインしたユーザー情報を画面に表示するために記述。
		model.addAttribute("loginUsername", userPrincipal.getUsername());
		List<Goods> dataList = goodsservice.getAllGoods(); // データベースからListに代入
//		List<Goods> productList = new ArrayList<>();//最新の金額に変更したGoodsを入れるリスト
		
		for(Goods g :dataList) {
			int goodsId=g.getGoods_id();
			if(bitinfoService.highPrice(goodsId)!=0) {
			g.setInitial_price(bitinfoService.highPrice(goodsId));
			}
//			productList.add(g);
		}
		
		List<Genre> genreList = genreService.getAllGenre();
		model.addAttribute("genreList", genreList);

		
		model.addAttribute("productList", dataList);
////////////////////////////////////////////
		return "sell";
	}
	@GetMapping("/product")
	public String getProductDetail(@RequestParam int productId, Model model) {
		Goods product = goodsservice.getGoodsById(productId);

		if (product != null) { // 商品が存在する場合のみ処理を行う
			String remainingTime = calculateRemainingTime(product.getEnd_time());
			
			int current_price = bitinfoService.highPrice(productId);
			model.addAttribute("current_price", current_price);

			model.addAttribute("product", product);
			model.addAttribute("remainingTime", remainingTime);

			return "item"; // 商品詳細ページのビュー名
		} else {
			// 商品が存在しない場合の処理をここに記述
			return "error-page"; // 例えば、エラーページにリダイレクトする
		}
	}
	// AfterControllerクラス内にcalculateRemainingTimeメソッドを追加
	private String calculateRemainingTime(LocalDateTime endTime) {
		LocalDateTime now = LocalDateTime.now();

		if (now.isAfter(endTime)) {
			return "オークション終了";
		}

		Duration duration = Duration.between(now, endTime);

		long days = duration.toDays();
		long hours = duration.toHoursPart();
		long minutes = duration.toMinutesPart();

		return String.format("残り時間: %d日 %d時間 %d分", days, hours, minutes);
	}



	@GetMapping("create")
	public String bbb(Model model) {
		
		List<Genre> genreList = genreService.getAllGenre();
		model.addAttribute("genreList", genreList);
		
		model.addAttribute("goodsForm",new GoodsForm());
		System.out.println("x");
		return "exhibit";
	}
	@PostMapping("create")
	public String sellItem(@RequestParam("file") MultipartFile file,@Valid @ModelAttribute GoodsForm goodsForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) {

		// goodsFormの内容を使ってデータベースに出品情報を登録する処理を呼び出す
		// goodsService.registerGoods(goodsForm);
		if (bindingResult.hasErrors()) {

			model.addAttribute("goodsForm", new GoodsForm());
			System.out.println("yyyyyyyyyyyyyyyyyyyyyyy");
			return "exhibit"; // 入力エラーがある場合は同じページに戻る
		} 
		System.out.println("あああああ");

		Goods goods =new Goods();
		goods.setName(goodsForm.getName());
		goods.setGenre_id(goodsForm.getGenre_id());
		goods.setInitial_price(goodsForm.getInitial_price());
		goods.setStart_time(LocalDateTime.now());
		goods.setEnd_time(goodsForm.getEndtime());
		//////
		String filePath = file.getOriginalFilename();
		goods.setImage_number(filePath);
		System.out.println(filePath);
		//////
//		System.out.println(goodsForm.getImagenumber());
		goods.setComment(goodsForm.getComment());
		goodsRepositry.save(goods);
		List<String> errorList = new ArrayList<String>();
		for (ObjectError error : bindingResult.getAllErrors()) {
			errorList.add(error.getDefaultMessage());
		}
		model.addAttribute("validationError", errorList);
		redirectAttributes.addFlashAttribute("message", "商品登録が完了しました");

		return "redirect:/afterLogin/success"; // 出品完了後、
	}
	@GetMapping("/success")
	public String SuccessPage(@ModelAttribute("message")String message,Model model) {
		model.addAttribute("message", message);
		return "successPage2";
	}

	/**
	 * コンテンツページに遷移します.
	 * 
	 * @return content.html
	 */
	@GetMapping("/sub")
	public String aaa(Model model) {

		model.addAttribute("accountForm",new AccountForm());
		return "menber";
	}

	@PostMapping("sub")
	public String menber(@Valid @ModelAttribute AccountForm accountForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "menber"; // 入力エラーがある場合は同じページに戻る
		}  



		Account account = new Account();

		account.setName(accountForm.getName());
		account.setAddress(accountForm.getAddress());
		account.setTel(accountForm.getTel());
		account.setMail(accountForm.getMail());
		account.setPassword(passwordEncoder.encode(accountForm.getPassword()));
		accountRepositry.save(account);
		redirectAttributes.addFlashAttribute("message", "登録が完了しました");
		return "redirect:/sell/success"; // 登録完了後にトップーページにリダイレクト



	}
	@GetMapping("/auction")
	public String showAuction(@RequestParam int productId,Model model) {
		Goods product = goodsservice.getGoodsById(productId);
		List<Bitinfo>bidInfo = bitinfoService.findByGoods(productId);
		 model.addAttribute("BitForm", new BitForm());
		if (product != null&&bidInfo != null) { // 商品が存在する場合のみ処理を行う
			String remainingTime = calculateRemainingTime(product.getEnd_time());
			 // 入札情報を金額の大きい順にソート
	        bidInfo.sort(Comparator.comparing(Bitinfo::getCurrent_price).reversed());


			model.addAttribute("product", product);
			model.addAttribute("remainingTime", remainingTime);
			model.addAttribute("bidInfo", bidInfo);

			return "auction"; // 商品詳細ページのビュー名
		} else {
			// 商品が存在しない場合の処理をここに記述
			return "error-page"; // 例えば、エラーページにリダイレクトする
		}

	}


	@PostMapping("/auction")
	public String placeBid(@RequestParam int productId, @Valid @ModelAttribute BitForm BitForm,
	        BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam String username, Model model) {
	    if (bindingResult.hasErrors()) {
	        model.addAttribute("BitForm", new BitForm());
	        return "auction";
	    }
	   

	    int x = accountService.findAccountIdByName(username);
	    Goods product = goodsservice.getGoodsById(productId);
	    model.addAttribute("product", product);
	   
	    model.addAttribute("BitForm", new BitForm());
	    if (product != null) {
	        int a = bitinfoService.highPrice(productId);

	        int bidAmount = BitForm.getBidAmount();
	        model.addAttribute("bidAmount", bidAmount); // bidAmountをコンテキストに追加

	        if (bidAmount <= a) {
	            model.addAttribute("error", true); // エラーがある場合、エラー変数を設定
	            
	            return "redirect:/afterLogin/auction?productId="+productId;// エラーがある場合、入札ページに戻る
	        }
	    }
	    model.addAttribute("BitForm", new BitForm());
		
		System.out.println(LocalDateTime.now());
		System.out.println(BitForm.getBidAmount());
		
		int goodsId = productId;
		int accountId=x;
		LocalDateTime bidTime = LocalDateTime.now();
		int currentPrice = BitForm.getBidAmount();
		// 入札情報を作成
		
		// 新しい入札情報を保存
		int o =bitinfoService.insertbid(goodsId, accountId, bidTime, currentPrice);
		System.out.println(o);

		// 商品テーブルの価格を更新
		//goodsRepositry.save(product);

		//成功メッセージをリダイレクト先に渡す
		redirectAttributes.addFlashAttribute("message", "入札が完了しました");
		System.out.println(BitForm.getBidAmount());

		return "redirect:/afterLogin/sell"; // 入札完了後、トップページにリダイレクト
	}
	
	
	//-------------------------------------------------------------
		@GetMapping("itemList")
		public String showItemList(Model model) {
			System.out.println(123465);
			// 商品データをデータベースから取得
			List<GoodsList> productList = goodsListService.goodsList();
			System.out.println(productList.get(0));

			boolean RoleAdmin = true;
			boolean RoleYes = true;
			String genre = "全商品";

			model.addAttribute("genre",genre);
			model.addAttribute("RoleAdmin",RoleAdmin);
			model.addAttribute("RoleYes",RoleYes);
			model.addAttribute("productList",productList);

			return "itemList";
		}

		@PostMapping("genreItemList")
		public String showGenreItemList(Model model,@RequestParam int genre_id) {
			List<GoodsList> productList = goodsListService.goodsList2(genre_id);


			boolean RoleAdmin = true;
			boolean RoleYes = true;

			String genre = goodsListService.genreList(genre_id);
			model.addAttribute("genre",genre);
			model.addAttribute("RoleAdmin",RoleAdmin);
			model.addAttribute("RoleYes",RoleYes);
			model.addAttribute("productList",productList);
			return "itemList";
		}

		//アップデート
		@GetMapping("/update")
		public String showUpdate(@RequestParam int goods_id, Model model) {
			Goods updateGoods = goodsservice.getGoodsById(goods_id);

			model.addAttribute("updateGoods",updateGoods);
			return "update";
		}

		@PostMapping("/updateGood")
		public String updateGood(
		@ModelAttribute("updateGoods") Goods updatedgoods,
		@RequestParam(name = "image", required = false) MultipartFile imageFile) {


			// 更新前の画像ファイル名を取得
			String oldImageName = goodsservice.getImageName(updatedgoods.getGoods_id());

			// 画像ファイル名をIDと連結した名前に設定
			String newImageName;// = updatedQuestion.getId() + "_" + (imageFile != null ? imageFile.getOriginalFilename() : oldImageName);

			if (imageFile == null) {
				newImageName = goodsservice.getImageName(updatedgoods.getGoods_id()); // ファイルが選択されていない場合の処理
				System.out.println(newImageName);
			} else {
				// 画像ファイル名をIDと連結した名前に設定
				newImageName = updatedgoods.getGoods_id() + "_" + imageFile.getOriginalFilename();
			}


			updatedgoods.setImage_number(newImageName);
			goodsRepositry.save(updatedgoods);

			// 画像を保存するディレクトリパスを指定
			String uploadDir = "src/main/resources/static/image"; // 画像保存先ディレクトリのパス

			// 画像を保存する処理
			if (imageFile != null && !imageFile.isEmpty()) {
				try {
					// 旧画像ファイルを削除
					if (oldImageName != null) {
						Path oldImagePath = Paths.get(uploadDir, oldImageName);
						Files.deleteIfExists(oldImagePath);
					}
					byte[] bytes = imageFile.getBytes();
					Path imagePath = Paths.get(uploadDir, newImageName);
					Files.write(imagePath, bytes);

				} catch (IOException e) {
					// エラーハンドリング
					e.printStackTrace();
				}
			}
			return "redirect:/afterLogin/itemList"; // 問題一覧ページへリダイレクト
			
		}

			//デリート
			@GetMapping("/delete")
			public String showDelete(@RequestParam int goods_id, Model model) {
				Goods goods = goodsservice.getGoodsById(goods_id);

				model.addAttribute("goods",goods);
				return "delete";
			}

			// 問題削除処理（POSTメソッド）
			@PostMapping("/deleteGoods/{id}")
			public String deleteQuestion(@PathVariable Integer id) {
				goodsRepositry.deleteById(id); // 問題を削除するサービスの呼び出し
				return "redirect:/afterLogin/itemList"; // 問題一覧ページへリダイレクト
			}

			@GetMapping("/showMypage")
			public String showMypage() {
				return "mypage";
			}
		}



