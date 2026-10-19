// このファイルが「com.example」という名前のフォルダの直下にあることを示しています
package com.example;

// 【インポート文】プログラムを動かすために必要なSpring Bootの標準機能（部品）を読み込んでいます
import org.springframework.boot.CommandLineRunner; // 「アプリ起動直後にこの中の処理を1回だけ自動実行して！」という命令の部品です
import org.springframework.boot.SpringApplication; // Webサーバーを立ち上げるためのSpring Bootの中心部品です
import org.springframework.boot.autoconfigure.SpringBootApplication; // アプリの初期設定を全自動で行ってくれるお守りです
import org.springframework.context.annotation.Bean; // 「このメソッドが作る部品をSpringに管理させてね」という目印です

import com.example.entity.Chapter;             // 「章データ（Chapter）」の入れ物（設計図）です
import com.example.entity.Question;            // 新しく作った「クイズの箱（Question）」の入れ物（設計図）です
import com.example.repository.ChapterRepository;   // 章データをデータベースに保存したり操作したりする職人（ロボット）です
import com.example.repository.QuestionRepository;  // クイズデータをデータベースに保存したり操作したりする職人（ロボット）です

// このクラスが、このWebアプリ全体の「起動スイッチ」になります
@SpringBootApplication
public class GraduationApplication {

    // Javaプログラムが最初に実行するメインメソッドです
    public static void main(String[] args) {
        // この1行が実行されることで、どこつぶの時のようにWebサーバーが裏側で自動的に立ち上がります
        SpringApplication.run(GraduationApplication.class, args);
    }

    // アプリが立ち上がった瞬間に、自動でテストデータをデータベースに保存する設定です
    @Bean
    public CommandLineRunner initData(ChapterRepository chapterRepository, QuestionRepository questionRepository) {
        // ↑ 引数の部分に「QuestionRepository questionRepository」を新しく仲間入りさせて、クイズロボットを呼び出しています
        
        // ここに書いた処理が、サーバー起動直後に1回だけ自動実行されます
        return args -> {
            
            // ===== 1. [前回の大成功コード] 章データの自動投入 =====
            // 1. 保存したい章のデータを、Javaのプログラム上で3件作成（用意）します
            Chapter ch1 = new Chapter("第1章：変数とデータ型");
            Chapter ch2 = new Chapter("第2章：条件分岐（if文）");
            Chapter ch3 = new Chapter("第3章：繰り返し処理（for文）");

            // 2. 先ほど作ったRepository（データ操作の職人）のsaveメソッドを使って、H2データベースに保存します
            chapterRepository.save(ch1);
            chapterRepository.save(ch2);
            chapterRepository.save(ch3);


            // ===== 2. [今回の新機能] クイズデータの自動投入（どこつぶ方式でまずは1問だけ！） =====
            // 1. 保存したいクイズのデータ（前作の第1章の第1問目の本物データ）をJavaのプログラム上で1件作成（用意）します
            Question q1 = new Question(
                "Javaのプログラムの実行方法を最も適切に説明しているものはどれですか？", // 問題文
                "JavaはソースコードをOSが直接実行する。",                     // 選択肢A
                "Javaはソースコードをバイトコードにコンパイルし、JVMで実行する。", // 選択肢B（これが正解！）
                "Javaはソースコードを必ずC言語へ変換して実行する。",             // 選択肢C
                1 // 正解の番号（0=A, 1=B, 2=C なので、1はBの「バイトコードにコンパイルしJVMで実行」が正解という意味）
            );

            // 2. クイズ専用ロボット（QuestionRepository）のsaveメソッドを使って、H2データベースに保存します
            questionRepository.save(q1);


            // 3. すべてのデータの保存がここまでエラーなく進んだことを確認するための、確認用メッセージです
            System.out.println("【システムログ】章データ3件とクイズデータ1問の投入が100%完了しました！！！");
        };
    }
}
