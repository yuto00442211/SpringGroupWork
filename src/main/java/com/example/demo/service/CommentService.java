package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Comment;
import com.example.demo.repositry.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // 商品IDを指定して、その商品に関連するコメントを取得するメソッド
    public List<Comment> getCommentsByGoodsId(int goods_id) {
        return commentRepository.findByGoodsId(goods_id);
    }

    // 商品ID、アカウントIDリストを指定して、指定したアカウントが関与したコメントをコメント時間の昇順で取得するメソッド
    public List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(
            int goodsId, List<Integer> accountIds, List<Integer> accountaccountIds) {
        List<Comment> comments = commentRepository.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc(
                goodsId, accountIds, accountaccountIds);
        return comments;
    }

    // コメントを新しく挿入するメソッド
    public int insertComment(int goodsId, int accountId, LocalDateTime commentTime,
                             String comment, int goodsaccount) {
        int x = commentRepository.insertComment(goodsId, accountId, commentTime, comment, goodsaccount);
        return x;
    }

    // 商品IDを指定して、その商品に関連する最新のコメントを取得するメソッド
    public Comment lookComment(int goodsId) {
        Comment comment = commentRepository.findLatestCommentsByGoodsId(goodsId);
        return comment;
    }

    // 商品IDとアカウントIDを指定して、指定したアカウントが関与した最新のコメントリストを取得するメソッド
    public List<Comment> mailList(int goodsId, int accountId) {
        List<Comment> mailList = commentRepository.findLatestCommentByGoodsId(goodsId, accountId);
        return mailList;
    }

    // 商品ID、アカウントIDリストを指定して、指定したアカウントが関与したコメントをコメント時間の昇順で取得するメソッド
    public List<Comment> findByGoodsIdAndAccountIdInOrderByCommentTimeAsc2(
            int goodsId, List<Integer> accountIds, List<Integer> accountaccountIds) {
        List<Comment> comments = commentRepository.findByGoodsIdAndAccountIdInOrderByCommentTimeAsc2(
                goodsId, accountIds, accountaccountIds);
        return comments;
    }

    // 商品IDとアカウントIDを指定して、コメントの承認状態を更新するメソッド
    public int updateIsApprovedByGoodsIdAndAccountIds(int goodsId, int accountId, int goodsaccountId) {
        return commentRepository.updateIsApprovedByGoodsIdAndAccountIds(goodsId, accountId, goodsaccountId);
    }

    // 指定した商品に未承認のコメントが存在するかどうかを確認するメソッド
    public boolean existsUnapprovedComments(int goodsId, int accountId) {
        boolean xxx = commentRepository.existsUnapprovedComments(goodsId, accountId);
        return xxx;
    }
}
