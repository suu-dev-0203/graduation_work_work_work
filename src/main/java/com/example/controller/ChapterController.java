package com.example.controller; // このファイルが「controller」フォルダにあることを示しています

import java.util.List; // 章のデータ（入れ物）を使うために読み込んでいます

import org.springframework.stereotype.Controller; // 先ほど作った仲介役（Service）を呼び出すために読み込んでいます
import org.springframework.ui.Model; // 「これは画面をコントロールする部品ですよ」という合図です
import org.springframework.web.bind.annotation.GetMapping; // JavaのデータをHTML（画面）に送り届けるための「運び屋」の仕組みです

import com.example.entity.Chapter; // ブラウザからの通信を受け取るための仕組みです
import com.example.service.ChapterService; // 複数のデータを「リスト（束）」として扱うためのJavaの標準機能です

@Controller // Spring Bootに、このクラスを画面切り替えの司令塔として登録します
public class ChapterController {

    // 仲介役（Service）をここに用意します
    private final ChapterService chapterService;

    // コンストラクタ：アプリ起動時に、先ほど作ったChapterServiceとこのコントローラーを自動で合体させます
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    // ブラウザで「http://localhost:8080/chapters」にアクセスされたときに動く処理です
    @GetMapping("/chapters")
    public String showChapterList(Model model) { // 引数に「Model（データ運び屋）」を追加しました！
        
        // 1. 仲介役（Service）に頼んで、データベースからすべての章データを集めてもらいます
        List<Chapter> chapters = chapterService.getAllChapters();
        
        // 2. 運び屋（Model）に、「chapters」という名前をつけて、集めた章データの束を載します
        // これにより、次のステップでHTML側からこのデータを自由に読み出せるようになります
        model.addAttribute("chapters", chapters);
        
        // 3. 「chapters.html」という画面を表示してね、と命令します（その際、運び屋が一緒にデータを届けます）
        return "chapters";
    }
}
