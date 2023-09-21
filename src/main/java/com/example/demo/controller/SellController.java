package com.example.demo.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.Domain.UserPrincipal;
import com.example.demo.Form.AccountForm;
import com.example.demo.entity.Account;
import com.example.demo.entity.Genre;
import com.example.demo.entity.Goods;
import com.example.demo.entity.GoodsList;
import com.example.demo.repositry.AccountRepositry;
import com.example.demo.service.AccountService;
import com.example.demo.service.BitinfoService;
import com.example.demo.service.GenreService;
import com.example.demo.service.GoodsListService;
import com.example.demo.service.GoodsService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/sell")//sellの場合はこのコントローラーが使用される
public class SellController {



	@Autowired
	private AccountRepositry accountRepositry; // 商品情報の登録処理を行うサービスクラスをインジェクション
	@Autowired
	private GoodsService goodsservice;

	@Autowired
	//	private @Bean // このメソッドの返り値をSpringのBeanとして登録します
	public PasswordEncoder passwordEncoder;// パスワードエンコーダー（パスワードのハッシュ化）を提供するメソッド
	@Autowired
	private BitinfoService bitinfoService ;
	@Autowired
	private GoodsListService goodsListService ;
	@Autowired
	private GenreService genreService;
	@Autowired
	private AccountService accountService;

	@GetMapping
	public String showSellPage(Model model) {


		List<Goods> dataList = goodsservice.getAllGoods(); // データベースからListに代入
		//		List<Goods> productList = new ArrayList<>();//最新の金額に変更したGoodsを入れるリスト

		for(Goods g :dataList) {
			g.setInitial_price(bitinfoService.highPrice(g));
		}
		List<Genre> genreList = genreService.getAllGenre();
		model.addAttribute("genreList", genreList);
		if(dataList!=null) {
			model.addAttribute("pickUp","～ピックアップ商品～");
			dataList=goodsservice.getRandomElements(dataList, 3);
		}
		model.addAttribute("productList", dataList);

		boolean issellMapping =true;
		model.addAttribute("issellMapping",issellMapping);
		model.addAttribute("ok",false);

		return "sell"; // 出品ページのテンプレート名
	}

	@GetMapping("/product")
	public String getProductDetail(@RequestParam int productId, Model model) {
		Goods product = goodsservice.getGoodsById(productId);



		if (product != null) { // 商品が存在する場合のみ処理を行う
			String remainingTime = calculateRemainingTime(product.getEnd_time());

			int current_price = bitinfoService.highPrice(product);
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


	@GetMapping("/sub")
	public String aaa(Model model) {

		model.addAttribute("accountForm",new AccountForm());
		return "menber";
	}

	@PostMapping("/sub")
	public String menber(@Valid @ModelAttribute AccountForm accountForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "menber"; // 入力エラーがある場合は同じページに戻る
		}

		String name = accountForm.getName();

		if (accountService.isNameAlreadyExists(name)) {
			bindingResult.rejectValue("name", "error.accountForm", "この名前はすでに使用されています");
			return "menber";
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

	//	@GetMapping("/") // ルートURL ("/") に対するGETリクエストを処理します
	//    public String redirectToIndex() {
	//    	
	//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // 現在のユーザーの認証情報を取得します
	//        if (authentication != null && authentication.isAuthenticated()) { // ユーザーがログインしている場合
	//        	
	//            return "redirect:/sell";
	//        }
	//        return "redirect:/login"; // ユーザーがログインしていない場合、"/login"にリダイレクトします
	//    }

	@GetMapping("/success")
	public String SuccessPage(@ModelAttribute("message")String message,Model model) {
		model.addAttribute("message", message);
		return "successPage";
	}

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}
	@GetMapping("/itemList")
	public String showItemList(Model model) {
		System.out.println(123465);
		// 商品データをデータベースから取得
		List<GoodsList> productList = goodsListService.goodsList();
		System.out.println(productList.get(0));
		for(int i =0; i<productList.size(); i++) {
			if(productList.get(i).getCurrent_price()==0) {
				Goods goods =goodsservice.findAllAllbygoodsID (productList.get(i).getGoods_id());

				productList.get(i).setCurrent_price(goods.getInitial_price());
			}
		}
		boolean RoleAdmin = false;
		boolean RoleYes = false;
		String genre = "全商品";

		model.addAttribute("genre",genre);
		model.addAttribute("RoleAdmin",RoleAdmin);
		model.addAttribute("RoleYes",RoleYes);
		model.addAttribute("productList",productList);

		return "itemList";
	}
	@PostMapping("/genreItemList")
	public String showGenreItemList(Model model,@RequestParam int genre_id,@AuthenticationPrincipal UserPrincipal userPrincipal,
			@RequestParam(required = false) String keyword) {
		List<GoodsList> productList;
		boolean ID=true;
		model.addAttribute("ID",ID);

		System.out.println(genre_id+"***");

		System.out.println(keyword+8);
		if (keyword != null && !keyword.isEmpty() && genre_id > 0) {
			// キーワードとジャンルIDを使用して検索クエリを実行する
			System.out.println(456);
			productList = goodsListService.searchGoodsByKeywordAndGenre(keyword, genre_id);
		} else if (keyword != null && !keyword.isEmpty()&&genre_id==0) {
			// キーワードのみを使用して検索クエリを実行する
			System.out.println(456456);
			productList = goodsListService.searchGoodsByKeyword(keyword);
		} else if (genre_id > 0) {
			// ジャンルIDのみを使用して検索クエリを実行する
			System.out.println(456456456);
			productList = goodsListService.goodsList2(genre_id);
		} else {
			// どちらも提供されない場合、デフォルトの検索処理を行うか、エラーハンドリングを行う
			System.out.println(654654654);
			productList = goodsListService.goodsList(); // デフォルトの検索処理の例
		}
		for(int i =0; i<productList.size(); i++) {
			if(productList.get(i).getCurrent_price()==0) {
				Goods goods =goodsservice.findAllAllbygoodsID (productList.get(i).getGoods_id());

				productList.get(i).setCurrent_price(goods.getInitial_price());
			}
		}

		boolean RoleAdmin = false;
		boolean RoleYes = false;

		String genre = goodsListService.genreList(genre_id);
		model.addAttribute("genre",genre);
		model.addAttribute("RoleAdmin",RoleAdmin);
		model.addAttribute("RoleYes",RoleYes);
		model.addAttribute("productList",productList);
		return "itemList";
	}



	//	@PostMapping("login1")
	//	//(@RequestParam(required = false)はリクエストパラメータの中にerrorが入っていなくてもエラーがでない
	//	public String showLogin(@RequestParam(required = false) String error) {
	//	    System.err.println("login error:" + error);
	//	    if (error != null) {
	//	        System.err.println("ログインに失敗しました。");
	//	    }
	//	    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb");
	//	    return "login1"; // ログインページにリダイレクト
	//	}


	@GetMapping("/readme")
	public String readmePage() {
		return "readme";
	}

}