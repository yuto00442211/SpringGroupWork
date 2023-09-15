package com.example.demo.entity;


import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  @Id
    
   
    private int account_id;
   
    private String name;
    
   
    private String address;
    
  
    private String tel;
    
    
    private String mail;
    
    
    private String password;
    
    
    private int money;
   
}
