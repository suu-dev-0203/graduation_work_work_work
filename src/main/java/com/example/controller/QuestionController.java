// [ファイルの住所] このファイルが「com/example/controller」というフォルダに置いてあることを示している
package com.example.controller;

// [道具の準備] 別の部屋にあるファイルや、Spring Bootの標準的な機能をここで使うために読み込んでいる
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody; //  データをJSON形式で返すための道具を新しく読み込んでいます

import com.example.entity.Question;
import com.example.service.QuestionService;

// このファイルが、ブラウザからのアクセスを受け付ける「コントローラー」という役割のクラスであることをシステムに登録している
@Controller
public class QuestionController {

    // データ中継部品（QuestionService）を、システムが自動的にここに接続して使える状態にする
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

    // =========================================================================
    //  【ここから新規追加】画面のJavaScriptから呼び出される本物のクイズデータ返却API
    // =========================================================================
    @GetMapping("/api/questions/chapter/{chapterId}") // ブラウザの裏側で「/api/questions/chapter/{chapterId}」にアクセスされたときに動く
    @ResponseBody // HTML画面ではなく、クイズのデータ（JSON）をそのままお行儀よく返却するマークだ
    public List<Question> getQuestionsByChapter(@PathVariable Long chapterId) {
        
        // サービス部品を呼び出して、データベースから取得した{chapterId}章のクイズの束をそのまま画面のJavaScriptへ返している
        return questionService.getQuestionsByChapter(chapterId);

    }
} 
