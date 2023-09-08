package com.example.demo.Domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.example.demo.entity.Account;

public class domain extends User {
	
private final  Account account;  //ログインに使用するユーザー情報をもたせる。
       //コンストラクタにユーザー情報と権限付与を行うためのリストを渡す。
	public domain(Account account, Collection<GrantedAuthority> authorityList) {
		super(account.getName(), account.getPassword(), authorityList);
		this.account =account;
	}

	public Account getUserEntity() {
		return account;
	}
	
}