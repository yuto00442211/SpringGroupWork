package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.demo.Form.BitinfoDTO;
import com.example.demo.Form.GoodsDTO;
import com.example.demo.Form.GoodsForm;
import com.example.demo.entity.Account;
import com.example.demo.entity.Bitinfo;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Genre;
import com.example.demo.entity.Goods;
import com.example.demo.entity.GoodsList;
import com.example.demo.repositry.AccountRepositry;
import com.example.demo.repositry.BitRepositry;
import com.example.demo.repositry.GenreRepository;
import com.example.demo.repositry.GoodsRepositry;
import com.example.demo.service.AccountService;
import com.example.demo.service.BitinfoService;
import com.example.demo.service.CommentService;
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
	@Autowired
	private CommentService commentService;
	@Autowired
	private GenreRepository genreRepository;
	private String master="たなか";


	//	highPrice(int goodsId)

	/**
	 * トップページに遷移します.
	 * （引数はログインしたユーザーを取得するための記述）
	 * @return top.html
	 */
	@GetMapping("/sell")
	public String showTop(Model model,@AuthenticationPrincipal UserPrincipal userPrincipal) {

		// ログインしたユーザー情報を画面に表示するために記述。
		model.addAttribute("loginUsername", userPrincipal.getUsername());
		List<Goods> productList = goodsservice.getAllGoods(); // 商品情報をデータベースからListに代入
		productList=goodsservice.getRandomElements(productList, 3);
		//入札情報を参照し、もし入札が一件でもあればTOPページの金額に代入するfor文
		for(Goods g :productList) {
			int goodsId=g.getGoods_id();
			if(bitinfoService.highPrice(goodsId)!=0) {
				g.setInitial_price(bitinfoService.highPrice(goodsId));
			}
		}
		if(productList!=null) {
			model.addAttribute("pickUp","～ピックアップ商品～");
		}
		List<Genre> genreList = genreService.getAllGenre();
		model.addAttribute("genreList", genreList);


		model.addAttribute("productList", productList);//最新金額を渡す
		boolean issellMapping =false;
		model.addAttribute("issellMapping",issellMapping);

		return "sell";
	}
	@GetMapping("/product")
	public String getProductDetail(@RequestParam int productId, Model model) {
		Goods product = goodsservice.getGoodsById(productId);

		if (product != null) { // 商品が存在する場合のみ処理を行う
			String remainingTime = calculateRemainingTime(product.getEnd_time());

			int current_price =product.getInitial_price();
			if(bitinfoService.highPrice(productId)!=0) {
				current_price=(bitinfoService.highPrice(productId));
			}


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
	public String sellItem(@RequestParam("file") MultipartFile file,@Valid @ModelAttribute GoodsForm goodsForm, BindingResult bindingResult, RedirectAttributes redirectAttributes,Model model) throws IOException {

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
		goods.setImage_number(goodsservice.saveImage(file));//画像ファイルの保存と画像名の登録を同時に行っている
		goods.setComment(goodsForm.getComment());

		// 認証情報を取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// ユーザー名を取得
		String username = authentication.getName();

		int account_id = accountService.findAccountIdByName(username);
		goods.setAccount_id(account_id);

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
	public String showItemList(Model model,@AuthenticationPrincipal UserPrincipal userPrincipal) {
		System.out.println(123465);
		// 商品データをデータベースから取得
		List<GoodsList> productList = goodsListService.goodsList();
		System.out.println(productList.get(0));
		if(master.equals(userPrincipal.getUsername())) {

			boolean RoleAdmin = true;
			boolean RoleYes = true;
			String genre = "全商品";

			model.addAttribute("genre",genre);
			model.addAttribute("RoleAdmin",RoleAdmin);
			model.addAttribute("RoleYes",RoleYes);
			model.addAttribute("productList",productList);
		}else {
			boolean RoleAdmin = false;
			boolean RoleYes = true;
			String genre = "全商品";

			model.addAttribute("genre",genre);
			model.addAttribute("RoleAdmin",RoleAdmin);
			model.addAttribute("RoleYes",RoleYes);
			model.addAttribute("productList",productList);
		}

		return "itemList";
	}

	@PostMapping("genreItemList")
	public String showGenreItemList(Model model,@RequestParam int genre_id,@AuthenticationPrincipal UserPrincipal userPrincipal) {
		List<GoodsList> productList = goodsListService.goodsList2(genre_id);

		if(master.equals(userPrincipal.getUsername())) {
			boolean RoleAdmin = true;
			boolean RoleYes = true;

			String genre = goodsListService.genreList(genre_id);
			model.addAttribute("genre",genre);
			model.addAttribute("RoleAdmin",RoleAdmin);
			model.addAttribute("RoleYes",RoleYes);
			model.addAttribute("productList",productList);
		}else {
			boolean RoleAdmin = false;
			boolean RoleYes = true;

			String genre = goodsListService.genreList(genre_id);
			model.addAttribute("genre",genre);
			model.addAttribute("RoleAdmin",RoleAdmin);
			model.addAttribute("RoleYes",RoleYes);
			model.addAttribute("productList",productList);

		}
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

	//マイページ表示
	@GetMapping("/showMypage")
	public String showMypage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		int accountId = accountService.findAccountIdByName(authentication.getName());
		System.out.println(authentication.getName());
		System.out.println(accountId);
		//入札情報
		List<Bitinfo> mypageList = bitinfoService.bidListmypage(accountId);
		// 常にtrueに設定されているが、これはテストやデバッグのためかもしれない
		boolean RoleAdmin = true;
		boolean RoleYes = true;

		// モデルにロール情報を追加
		model.addAttribute("RoleAdmin", RoleAdmin);
		model.addAttribute("RoleYes", RoleYes);

		// 入札情報が存在する場合の処理
		if (mypageList != null && !mypageList.isEmpty()) {
			List<BitinfoDTO> bitinfoDTOs = new ArrayList<>(); // DTOのリストを初期化

			for (Bitinfo bitinfo : mypageList) {
				BitinfoDTO bitinfoDTO = new BitinfoDTO();

				int goodsaccount_id = goodsservice.findAccountIdByGoodsId(bitinfo.getGoods_id());
				Goods item = goodsservice.allgoodsSelect(bitinfo.getGoods_id());

				// DTOに情報をコピー
				bitinfoDTO.setAccount_id(bitinfo.getAccount_id());
				bitinfoDTO.setBid_time(bitinfo.getBid_time());
				bitinfoDTO.setCurrent_price(bitinfo.getCurrent_price());
				bitinfoDTO.setGoods_id(bitinfo.getGoods_id());
				bitinfoDTO.setNotlook(commentService.existsUnapprovedComments(bitinfo.getGoods_id(), goodsaccount_id));
				bitinfoDTO.setTimeup(goodsservice.timeUp(LocalDateTime.now(),item.getEnd_time(),bitinfo.getGoods_id(),bitinfo));

				System.out.println(bitinfoDTO.isTimeup());


				bitinfoDTOs.add(bitinfoDTO); // DTOリストに追加

				if(bitinfoDTO.isTimeup()) {
					model.addAttribute("ok",true);
				}
			}

			model.addAttribute("mypageList", bitinfoDTOs); // モデルにDTOリストを追加
		} else {
			model.addAttribute("error", "入札している品物はありません。");
		}

		return "mypage";
	}


	//入札者側mychat表示
	@GetMapping("/mychat")
	public String showMychat(@RequestParam(name = "goods_id")  int goodsId, Model model,@RequestParam(name = "fromRedirect", required = false, defaultValue = "false") boolean fromRedirect) {
		//入札者
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int accountId = accountService.findAccountIdByName(authentication.getName());

		//出品者
		Integer accountID = goodsservice.findAccountIdByGoodsId(goodsId);

		if(accountID!=-1) {
			//取得したいid配列
			List<Integer> accountIds = Arrays.asList(accountId, accountID); // 取得したい account_id のリスト
			List<Integer> goodsaccountIdList = Arrays.asList(accountId, accountID); // 取得したい goodsaccount_id のリスト

			if (!fromRedirect) {
				// is_approvedをtrueに更新
				commentService.updateIsApprovedByGoodsIdAndAccountIds(goodsId, accountID, accountId);
			}


			//チャットリスト作成
			List<Comment> comments = commentService.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(goodsId,accountIds,goodsaccountIdList);

			//リダイレクト用インスタンス
			Comment commentForm = new Comment();

			model.addAttribute("goodsId",goodsId);
			model.addAttribute("commentForm",commentForm);
			model.addAttribute("accountId",accountId);
			model.addAttribute("isBidder",false);
			model.addAttribute("comments", comments);
			return "mychat";
		}else {
			return "mypage";
		}
	}



	@PostMapping("/sendMessage")
	public String sendMessage(@ModelAttribute Comment comment, RedirectAttributes redirectAttributes) {
		// CommentFormから情報を取得し、データベースに保存する処理を記述します。
		// エントリが保存されたら、適切なページにリダイレクトする。

		int goodsId=comment.getGoods_id();
		int accountId=comment.getAccount_id();
		String comments=comment.getComment_content();
		LocalDateTime time =LocalDateTime.now();
		int goodsaccount = goodsservice.findAccountIdByGoodsId(goodsId);

		commentService.insertComment(goodsId, accountId, time, comments,goodsaccount);

		// 保存成功メッセージやその他の情報を次のページに表示するためのオプション。
		redirectAttributes.addAttribute("goods_id", comment.getGoods_id());
		redirectAttributes.addAttribute("fromRedirect", true);


		return "redirect:/afterLogin/mychat";  // リダイレクト先を適切なページに置き換えます。
	}

	@GetMapping("addGenre")
	public String addGenre(Model model, @RequestParam("text") String text) {
		if (genreService.chkName(text)) {
			Genre genre = new Genre();
			genre.setGenreName(text);
			genreRepository.save(genre);
			model.addAttribute("successMessage", "ジャンルが正常に追加されました。");
		} else {
			model.addAttribute("errorMessage", "ジャンルが既に存在します。別の名前を試してください。");
		}
		return "mypage";
	}

	//出品リスト
	@GetMapping("/exhibition")
	public String showExhibition(Model model) {

		//アカウントID取得
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int accountId = accountService.findAccountIdByName(authentication.getName());

		List<Goods> myItemList = goodsservice.getMyItem(accountId);
		List<GoodsDTO> goodsDTOList = new ArrayList<>();


		if (myItemList != null && !myItemList.isEmpty()) {
			for (Goods item : myItemList) {
				// GoodsからGoodsDTOに情報をコピー
				GoodsDTO goodsDTO = new GoodsDTO();
				goodsDTO.setGoods_id(item.getGoods_id());
				goodsDTO.setName(item.getName());
				goodsDTO.setAccount_id(item.getAccount_id());
				goodsDTO.setEnd_time(item.getEnd_time());
				goodsDTO.setInitial_price(item.getInitial_price());


				Comment comment = commentService.lookComment(item.getGoods_id());
				if (comment != null && comment.getGoods_id()==item.getGoods_id()) {
					goodsDTO.setReadStatus("メッセージはあります。");
				} else {
					goodsDTO.setReadStatus("メッセージはありません。");  // もし必要ならデフォルトの値を設定
				}

				goodsDTOList.add(goodsDTO);
			}
		}else {
			model.addAttribute("error","出品している品物はありません。");

			return "ehibition";
		}
		model.addAttribute("goodsList", goodsDTOList);
		return "ehibition";
	}

	//商品別mailBox表示
	@GetMapping("/mailBox")
	public String showMailBox(@RequestParam(name = "goodsid") int goods_id, Model model) {

		//出品者
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int accountId = accountService.findAccountIdByName(authentication.getName());

		List<Comment> mailList = commentService.mailList(goods_id,accountId);

		model.addAttribute("mailList",mailList);		
		return"mailBox";
	}

	//出品者側mychat表示
	@GetMapping("/mychat2")
	public String showMychat2(@RequestParam(name = "goods_id") int goodsId, @RequestParam(name = "account_id") int accountID, 
			@RequestParam(name = "fromRedirect", required = false, defaultValue = "false") boolean fromRedirect, Model model) {

		//出品者
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		int accountId = accountService.findAccountIdByName(authentication.getName());

		List<Integer> accountIds = Arrays.asList(accountId, accountID); // 取得したい account_id のリスト
		List<Integer> goodsaccountIds = Arrays.asList(accountId, accountID); // 取得したい account_id のリスト

		if (!fromRedirect) {
			// is_approvedをtrueに更新
			commentService.updateIsApprovedByGoodsIdAndAccountIds(goodsId, accountID, accountId);
		}

		// チャットリスト作成
		List<Comment> comments = commentService.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc2(goodsId, accountIds,goodsaccountIds);
		//リダイレクトインスタンス
		Comment commentForm = new Comment();

		model.addAttribute("goodsaccount_id", accountID);
		model.addAttribute("goodsId", goodsId);
		model.addAttribute("commentForm", commentForm);
		model.addAttribute("accountId", accountId);
		model.addAttribute("isBidder", false);
		model.addAttribute("comments", comments);
		return "mychat2";
	}


	@PostMapping("/sendMessage2")
	public String sendMessage2(@ModelAttribute Comment comment, RedirectAttributes redirectAttributes,@RequestParam(name = "goodsaccount_id")  int goodsaccount) {
		// CommentFormから情報を取得し、データベースに保存する処理を記述します。
		// エントリが保存されたら、適切なページにリダイレクトする。

		int goodsId=comment.getGoods_id();
		int accountId=comment.getAccount_id();
		String comments=comment.getComment_content();
		LocalDateTime time =LocalDateTime.now();


		commentService.insertComment(goodsId, accountId, time, comments,goodsaccount);

		// 保存成功メッセージやその他の情報を次のページに表示するためのオプション。
		redirectAttributes.addAttribute("goods_id", comment.getGoods_id());
		redirectAttributes.addAttribute("account_id", comment.getGoodsaccount_id());
		redirectAttributes.addAttribute("fromRedirect", true);

		return "redirect:/afterLogin/mychat2";  // リダイレクト先を適切なページに置き換えます。
	}

	//入札ページ表示
	@GetMapping("/showbitMypage")
	public String showBitMypage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		int accountId = accountService.findAccountIdByName(authentication.getName());
		System.out.println(authentication.getName());
		System.out.println(accountId);

		// 入札情報を取得
		List<Bitinfo> mypageList = bitinfoService.bidListmypage(accountId);

		// 常にtrueに設定されているが、これはテストやデバッグのためかもしれない
		boolean RoleAdmin = true;
		boolean RoleYes = true;

		// モデルにロール情報を追加
		model.addAttribute("RoleAdmin", RoleAdmin);
		model.addAttribute("RoleYes", RoleYes);

		// 入札情報が存在する場合の処理
		if (mypageList != null && !mypageList.isEmpty()) {
			List<BitinfoDTO> bitinfoDTOs = new ArrayList<>(); // DTOのリストを初期化

			for (Bitinfo bitinfo : mypageList) {
				BitinfoDTO bitinfoDTO = new BitinfoDTO();

				int goodsaccount_id = goodsservice.findAccountIdByGoodsId(bitinfo.getGoods_id());
				Goods item = goodsservice.allgoodsSelect(bitinfo.getGoods_id());

				// DTOに情報をコピー
				bitinfoDTO.setAccount_id(bitinfo.getAccount_id());
				bitinfoDTO.setBid_time(bitinfo.getBid_time());
				bitinfoDTO.setCurrent_price(bitinfo.getCurrent_price());
				bitinfoDTO.setGoods_id(bitinfo.getGoods_id());
				bitinfoDTO.setNotlook(commentService.existsUnapprovedComments(bitinfo.getGoods_id(), goodsaccount_id));
				bitinfoDTO.setTimeup(goodsservice.timeUp(LocalDateTime.now(),item.getEnd_time(),bitinfo.getGoods_id(),bitinfo));
				bitinfoDTO.setName(item.getName());
				System.out.println(bitinfoDTO.isTimeup());


				bitinfoDTOs.add(bitinfoDTO); // DTOリストに追加

				if(bitinfoDTO.isTimeup()) {
					model.addAttribute("ok",true);
				}
			}

			model.addAttribute("mypageList", bitinfoDTOs); // モデルにDTOリストを追加
		} else {
			model.addAttribute("error", "入札している品物はありません。");
		}

		return "bitmypage";
	}
}









