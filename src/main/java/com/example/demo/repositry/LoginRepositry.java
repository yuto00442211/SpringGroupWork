package com.example.demo.repositry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Account;

/**
 * ユーザー情報を扱うリポジトリクラス.
 *
 */
@Repository
public class LoginRepositry {
	@Autowired
	private NamedParameterJdbcTemplate template;//名前付きパラメータを使用できる。通常は「?」ここではnameという名前付きパラメータを使ってる

	/**
	 * Userオブジェクトを生成するローマッパー.
	 */
	//Accountクラスにデータベースの行をマッピング
	private static final RowMapper<Account> USER_ROW_MAPPER = new BeanPropertyRowMapper<>(Account.class);

	/**
	 * メールアドレスをもとにユーザーを検索します.
	 * 
	 * @param email メールアドレス
	 * @return ユーザー情報
	 */
	
	//指定された名前に基づいてデータベースから行を取得するためのSQLクエリを構築し、JdbcTemplateを使用して実行
	//取得された結果セットをAccountオブジェクトのリストにマッピング
	public Account findByName(String username) {
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT account_id,name,address,tel,mail,password FROM account WHERE name = :name"); // スペースとプレースホルダの位置を修正
	    SqlParameterSource param = new MapSqlParameterSource().addValue("name", username);
	    List<Account> userList = template.query(sql.toString(), param, USER_ROW_MAPPER);
	    if (userList.size() == 0) {
	        return null;
	    }
	    return userList.get(0);
	}
}