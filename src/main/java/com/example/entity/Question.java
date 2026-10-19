// このファイルが「com.example.entity」という名前の部屋（フォルダ）にあることを宣言しています
package com.example.entity;

// データベースとJavaをガチッと繋ぐためのJPAという便利な部品（仕組み）を読み込んでいます
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


// 【重要】このクラスがデータベースの「テーブル（保管庫）」の箱になるよ！とパソコンに教えてあげるマークです
@Entity
public class Question {

    // 【重要】これがクイズ問題の「出席番号（主キー）」になるよ！という目印です
    @Id
    // データを保存したときに、1番、2番、3番…とパソコンが自動で番号を被らないように割り振ってくれる設定です
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId; // クイズごとに自動で付く、世界に一つだけのID番号を入れる隙間です
    
    private String questionText;     // 「Javaの特徴は？」といった、クイズの問題文（文字列）を入れる隙間です
    private String choiceA;          // 選択肢Aの文章（文字列）を入れる隙間です
    private String choiceB;          // 選択肢Bの文章（文字列）を入れる隙間です
    private String choiceC;          // 選択肢Cの文章（文字列）を入れる隙間です
    private int correctChoiceIndex;  // 正解がどれかを「数字」で覚える隙間です（0ならA、1ならB、2ならCが正解）

    // 【引数なしのコンストラクター】
    // データベースからデータを読み込むときに、Javaの裏側のシステムが自動で使う「中身が空っぽの初期化スイッチ」です。
    // 自分で何かを書く必要はありませんが、これがないとJPAというロボットがエラーを起こして動かなくなります。
    public Question() {}

    // 【データを手動で入れるためのコンストラクター】
    // 開発者が「新しい問題を作ってデータベースに入れたいな」というときに、手動で文字や正解番号を詰め込むための起動スイッチです。
    public Question(String questionText, String choiceA, String choiceB, String choiceC, int correctChoiceIndex) {
        // 左側のこのクラスの隙間（this.〜）に対して、右側の新しく持ってきたデータ（引数）をガチッと代入して保存しています
        this.questionText = questionText;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.choiceC = choiceC;
        this.correctChoiceIndex = correctChoiceIndex;
    }

    // 【Getter（ゲッター）メソッド】
    // 隙間に隠されているデータを、画面（HTML）や他のプログラムから「中身を見せて！」と安全に呼び出すための専用の窓口です。
    public Long getQuestionId() { return questionId; }               // ID番号を外に返して見せる窓口です
    public String getQuestionText() { return questionText; }         // 問題文の文字を外に返して見せる窓口です
    public String getChoiceA() { return choiceA; }                   // 選択肢Aの文字を外に返して見せる窓口です
    public String getChoiceB() { return choiceB; }                   // 選択肢Bの文字を外に返して見せる窓口です
    public String getChoiceC() { return choiceC; }                   // 選択肢Cの文字を外に返して見せる窓口です
    public int getCorrectChoiceIndex() { return correctChoiceIndex; } // 正解の数字を外に返して見せる窓口です
}
