package com.example.demo.Domain;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.Account;

public class UserPrincipal implements UserDetails {

    private Account account;  // Userオブジェクトを保持します。

    // コンストラクタでUserオブジェクトを受け取り、それをこのクラスのuserにセットします。
    public UserPrincipal(Account account) {
        this.account = account;
    }

    // ユーザーに与えられる権限を返します。ここでは全てのユーザーに"USER"という権限を与えています。
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
        
       
    }

    // Userオブジェクトのパスワードを返します。
    @Override
    public String getPassword() {
    	
        return account.getPassword();
    }

    // Userオブジェクトのユーザー名を返します。
    @Override
    public String getUsername() {
        if (account != null) {
            return account.getName();
        } else {
            return "名前がわからないよ"; // または適切なデフォルトのユーザー名を返す
        }
    }

    // アカウントが有効期限切れでないことを示すために、常にtrueを返します。
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // アカウントがロックされていないことを示すために、常にtrueを返します。
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 資格情報（ここではパスワード）が有効期限切れでないことを示すために、常にtrueを返します。
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // アカウントが有効であることを示すために、常にtrueを返します。
    @Override
    public boolean isEnabled() {
        return true;
    }

	

	public static Account getAccount() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}


