package com.example; // このファイルが「com.example」というフォルダにあることを示しています

// プログラムを動かすために必要なSpring Bootの標準機能（部品）を読み込んでいます
import org.springframework.boot.CommandLineRunner; // 先ほど作った「Chapter（データの入れ物）」を使うための読み込みです
import org.springframework.boot.SpringApplication; // 先ほど作った「ChapterRepository（データ操作の職人）」を使うための読み込みです
import org.springframework.boot.autoconfigure.SpringBootApplication; // 「アプリ起動時に特定の処理を実行する」ための仕組みです
import org.springframework.context.annotation.Bean;

import com.example.entity.Chapter;
import com.example.repository.ChapterRepository; // 「このメソッドが作る部品をSpringに管理させてね」という目印です

// このクラスが、このWebアプリ全体の「起動スイッチ」になります
@SpringBootApplication
public class GraduationApplication {

    // Javaプログラムが最初に実行するメインメソッドです
    public static void main(String[] args) {
        // この1行が実行されることで、Webサーバーが裏側で自動的に立ち上がります
        SpringApplication.run(GraduationApplication.class, args);
    }

    // アプリが立ち上がった瞬間に、自動でテストデータをデータベースに保存する設定です
    @Bean
    public CommandLineRunner initData(ChapterRepository chapterRepository) {
        // ここに書いた処理が、サーバー起動直後に1回だけ自動実行されます
        return args -> {
            // 1. 保存したい章のデータを、Javaのプログラム上で3件作成（用意）します
            Chapter ch1 = new Chapter("第1章：変数とデータ型");
            Chapter ch2 = new Chapter("第2章：条件分岐（if文）");
            Chapter ch3 = new Chapter("第3章：繰り返し処理（for文）");

            // 2. 先ほど作ったRepository（データ操作の職人）のsaveメソッドを使って、H2データベースに保存します
            chapterRepository.save(ch1);
            chapterRepository.save(ch2);
            chapterRepository.save(ch3);

            // 3. データの保存がここまでエラーなく進んだことを確認するための、確認用メッセージです
            System.out.println("【システムログ】初期データの投入が100%完了しました！！！");
        };
    }
}
