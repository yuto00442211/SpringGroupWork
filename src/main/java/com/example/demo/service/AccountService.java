package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.repositry.AccountRepositry;


@Service
public class AccountService {


	@Autowired
	private AccountRepositry accountRepositry;

	public int findAccountIdByName(String name) {
		return accountRepositry.findAccountIdByName(name);
	}
	public boolean isNameAlreadyExists(String name) {
		return accountRepositry.countByName(name) > 0;
	}

	public String findNameById(int accountId) {
		return accountRepositry.findNameById(accountId);
	}

	public int lookMoney(int accountId) {
		return accountRepositry.lookMoney(accountId);
	}

	//IDを使用して全てを取得するカスタムクエリメソッド
	public Account findAccountIdByID(int acoountId) {
		return accountRepositry.findAccountIdByID(acoountId);
	}

	//account_idのmoneyをアップデートするメソッド
	public void updateMoney(int accountId,int money){

		accountRepositry.updateMoney(accountId, money);
	}


}
