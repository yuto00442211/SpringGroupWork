package com.example.demo.repositry;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Account;

public interface AccountRepositry extends CrudRepository<Account, Integer> {

    // 名前を使用してアカウントIDを取得するカスタムクエリメソッド
    @Query("SELECT account_id FROM Account WHERE name = :name")
    int findAccountIdByName(@Param("name") String name);

    // 指定した名前のアカウントがすでに存在するかどうかを確認するカスタムクエリメソッド
    @Query("SELECT COUNT(a) FROM Account a WHERE a.name = :name")
    int countByName(@Param("name") String name);

    // アカウントIDを指定して名前を取得するカスタムクエリメソッド
    @Query("SELECT a.name FROM Account a WHERE a.account_id = :account_id")
    String findNameById(@Param("account_id") int account_id);

    // アカウントの残高を取得するカスタムクエリメソッド
    @Query("SELECT a.money FROM Account a WHERE a.account_id = :accountId")
    int lookMoney(@Param("accountId") int accountId);

    // アカウントIDを使用してアカウント情報を取得するカスタムクエリメソッド
    @Query("SELECT * FROM Account WHERE account_id = :account_id")
    Account findAccountIdByID(@Param("account_id") int accountId);

    // アカウントの残高をアップデートするメソッド
    @Modifying
    @Query("UPDATE account SET money = :money WHERE account_id = :account_id")
    void updateMoney(@Param("account_id") int accountId, @Param("money") int money);
}
