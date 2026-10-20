package com.example.graduation_java_game;
//  このプログラムが com.example.graduation_java_game という名前の部屋に所属していることを宣言しています。

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.entity.Question;
import com.example.repository.QuestionRepository;

@Component
//  「このクラスは特別な部品だから、アプリ起動時に自動で実行してね」と Spring Boot に指示しています。
public class OriginalQuestionBankInitializer implements CommandLineRunner {
// アプリ起動直後に自動実行される仕組み（CommandLineRunner）を持った初期化クラスを定義しています。

    private final QuestionRepository questionRepository;
    //  データベースと直接通信して、クイズデータを保存したり読み出したりする専属ロボット（リポジトリ）の置き場所です。

    // コンストラクタによる依存性注入
    public OriginalQuestionBankInitializer(QuestionRepository questionRepository) {
    // この初期化クラスが生まれるときに、システムが自動的にデータベース通信ロボットをセットしてくれます。
        this.questionRepository = questionRepository;
    }

    @Override
    // 起動時に動く処理の中身を記述しています。
    public void run(String... args) throws Exception {
        // アプリが立ち上がった瞬間に、Spring Boot の指示によってこの中身が自動的に実行されます。

        //  重複登録を防ぐため、起動時に一度クイズデータベースの中身を完全に空っぽにお掃除します。
        questionRepository.deleteAll();

        //  別ファイル（OriginalQuestionBank）から、すでに用意されている第1章のクイズデータの束をありのまま取得します。
        List<Question> chapter1Questions = OriginalQuestionBank.getChapter1Questions();

        //  通信ロボットに、取得したクイズの束を丸ごと引き渡して、H2データベースへ一気に一括保存させます。
        if (chapter1Questions != null) {
            questionRepository.saveAll(chapter1Questions);
        }

        System.out.println(" [初期化ロボット] OriginalQuestionBank から正規のクイズデータを安全に読み込み、データベースへ一括格納しました。");
    }
}
