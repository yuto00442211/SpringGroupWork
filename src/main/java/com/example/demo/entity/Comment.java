package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    private int goods_id; // 商品ID
    private int account_id; // アカウントID
    private Integer comment_number; // コメント番号
    private String comment_content; // コメント内容
    private LocalDateTime comment_time; // コメントのタイムスタンプ
    private Boolean is_approved = false; // 承認フラグ（デフォルトはfalse）

    // ゲッターとセッターを追加
}
