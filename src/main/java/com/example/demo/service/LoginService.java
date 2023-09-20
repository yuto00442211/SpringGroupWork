package com.example.demo.service;


	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Domain.UserPrincipal;
import com.example.demo.entity.Account;
import com.example.demo.repositry.LoginRepositry;

	@Service
	public class LoginService implements UserDetailsService {

		@Autowired
		private LoginRepositry loginRepository;
		
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			Account account = loginRepository.findByName(username);
			
	           //バリデーションチェック　本記事の趣旨とは異なるので実装の詳しい説明は省略。
			if(account == null) {
				throw new UsernameNotFoundException("その名前は登録されていません");
			}
	return new UserPrincipal(account);
		}
	}


