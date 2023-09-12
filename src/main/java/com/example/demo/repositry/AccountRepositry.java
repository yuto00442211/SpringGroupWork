package com.example.demo.repositry;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Account;


public interface AccountRepositry extends CrudRepository<Account, Integer> {
	
	// nameを使用してaccount_idを取得するカスタムクエリメソッド
    @Query("SELECT account_id FROM Account WHERE name = :name")
    int findAccountIdByName(@Param("name") String name);
    
    @Query("SELECT COUNT(a) FROM Account a WHERE a.name = :name")
    int countByName(@Param("name") String name);

}
