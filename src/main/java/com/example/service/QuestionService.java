// [ファイルの住所] このファイルが「com/example/service」というフォルダに置いてあることを示している
package com.example.service;

// [道具の準備] 別のフォルダにあるプログラムや、Javaの標準的な機能をこのファイルで使うために読み込んでいる
import java.util.List;           // クイズの「データの箱（Question）」をここで使うために読み込んでいる

import org.springframework.beans.factory.annotation.Autowired; // データの保存や取得を行う「データ操作の部品（Repository）」を読み込んでいる
import org.springframework.stereotype.Service; // 別で作った部品を、ここに自動でドッキングさせるための機能を読み込んでいる

import com.example.entity.Question; // このファイルが、データの受け渡しや加工を担当する「サービス」の役割であることを示すマーク
import com.example.repository.QuestionRepository;                         // データを複数まとめて「1つのセット（リスト）」として扱うためのJavaの機能を読み込んでいる

// このファイルが、データ処理や仲介を行う「サービス」という役割のクラスであることをシステムに登録しているマーク
@Service
public class QuestionService {

    // 先ほど作ったデータ操作の部品（QuestionRepository）を、システムが自動的にここに繋いで使える状態にしてくれるマーク
    @Autowired
    private QuestionRepository questionRepository;

    // データベースにあるすべてのクイズデータを取得して、呼び出し元（Controller）にまとめて引き渡すための処理
    public List<Question> getAllQuestions() {
        // データ操作部品にある標準機能「findAll（すべて見つける）」を使って、全クイズデータを取得してそのまま返している
        return questionRepository.findAll();
    }
}
