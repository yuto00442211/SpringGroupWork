package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
