package com.example.controller; // このファイルが「controller」フォルダにあることを示しています

import org.springframework.stereotype.Controller; // 「これは画面をコントロールする部品ですよ」というSpring Bootへの合図です
import org.springframework.web.bind.annotation.GetMapping; // ブラウザからの「画面を見せて！」という通信を受け取るための仕組みです

@Controller // Spring Bootに、このクラスを画面切り替えの司令塔として登録します
public class ChapterController {

    // ブラウザで「http://localhost:8080/chapters」にアクセスされたときに動く処理です
    @GetMapping("/chapters")
    public String showChapterList() {
        // 次のステップで作る「chapters.html」という画面を表示してね、という命令です
        return "chapters";
    }
}
