// [ファイルの住所] このファイルが「com/example/service」というフォルダに置いてあることを示している
package com.example.service;

// [道具の準備] 別のフォルダにあるプログラムや、Javaの標準的な機能をこのファイルで使うために読み込んでいる
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Question;
import com.example.repository.QuestionRepository;

// このファイルが、データ処理や仲介を行う「サービス」という役割のクラスであることをシステムに登録しているマーク
@Service
public class QuestionService {

    // 先ほど作ったデータ操作の部品（QuestionRepository）を、システムが自動的にここに繋いで使える状態にしてくれるマーク
    @Autowired
    private QuestionRepository questionRepository;

    //  ① もともとあった処理：データベースにあるすべてのクイズデータを取得する
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    //  ② 【新規追加】指定された章のID（chapterId）に紐づく、本物のクイズデータだけを絞り込んで取得する処理
    public List<Question> getQuestionsByChapter(Long chapterId) {
        // ※ 最優先仕様（Source of Truth）に従い、リポジトリが用意している章IDでの検索機能を呼び出します
        return questionRepository.findByChapter_ChapterId(chapterId);
    }
}
