// [ファイルの住所] このファイルが「com/example/controller」というフォルダに置いてあることを示している
package com.example.controller;

// [道具の準備] 別の部屋にあるファイルや、Spring Bootの標準的な機能をここで使うために読み込んでいる
import java.util.List;           // クイズの「データの箱（Question）」をここで使うために読み込んでいる

import org.springframework.beans.factory.annotation.Autowired;   // データの仲介を行う「サービス（QuestionService）」を読み込んでいる
import org.springframework.stereotype.Controller; // 部品を自動で接続するための機能を読み込んでいる
import org.springframework.ui.Model; // このファイルが画面切り替えを担当する「コントローラー」であることを示すマークだ
import org.springframework.web.bind.annotation.GetMapping;           // 画面（HTML）にデータを引き渡すための「荷台（Model）」の機能を読み込んでいる

import com.example.entity.Question; // 特定のURLへのアクセスを受け付けるための機能を読み込んでいる
import com.example.service.QuestionService;                         // 複数のデータをまとめて「リスト」として扱うためのJavaの機能を読み込んでいる

// このファイルが、ブラウザからのアクセスを受け付ける「コントローラー」という役割のクラスであることをシステムに登録している
@Controller
public class QuestionController {

    // 先ほど作ったデータ中継部品（QuestionService）を、システムが自動的にここに接続して使える状態にする
    @Autowired
    private QuestionService questionService;

    // ユーザーがブラウザで「http://localhost:8080/questions」にアクセスしたときに、以下の処理を実行する
    @GetMapping("/questions")
    public String showQuestions(Model model) { // 引数の「Model」は、画面へ持っていくデータを乗せるための荷台だ
        
        // サービス部品の「getAllQuestions」を呼び出して、データベースから全クイズデータを取得する
        List<Question> questions = questionService.getAllQuestions();

        // 荷台（model）に「"questions"」という名前をつけて、取得したクイズデータのリストを載せている
        model.addAttribute("questions", questions);

        // 画面表示を担当するファイルである「questions.html」を呼び出すように、文字を返している
        return "questions";
    }
}
