package com.example.demo.Form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class AccountForm {
    @NotBlank(message = "名前を入力してください")
    private String name;

    @NotBlank(message = "住所を入力してください")
    private String address;

    @NotBlank(message = "電話番号を入力してください")
    private String tel;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "正しいメールアドレスの形式で入力してください")
    private String mail;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    private String password;

}