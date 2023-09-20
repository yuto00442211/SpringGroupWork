package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.repositry.AccountRepositry;

@Service
public class AccountService {

    @Autowired
    private AccountRepositry accountRepositry;

    // 名前を指定してアカウントIDを取得するメソッド
    public int findAccountIdByName(String name) {
        return accountRepositry.findAccountIdByName(name);
    }

    // 指定した名前のアカウントがすでに存在するかどうかを確認するメソッド
    public boolean isNameAlreadyExists(String name) {
        return accountRepositry.countByName(name) > 0;
    }

    // アカウントIDを指定して名前を取得するメソッド
    public String findNameById(int accountId) {
        return accountRepositry.findNameById(accountId);
    }

    // アカウントの残高を取得するメソッド
    public int lookMoney(int accountId) {
        return accountRepositry.lookMoney(accountId);
    }

    // アカウントIDを使用してアカウント情報を取得するカスタムクエリメソッド
    public Account findAccountIdByID(int accountId) {
        return accountRepositry.findAccountIdByID(accountId);
    }

    // アカウントの残高をアップデートするメソッド
    public void updateMoney(int accountId, int money) {
        accountRepositry.updateMoney(accountId, money);
    }
}
