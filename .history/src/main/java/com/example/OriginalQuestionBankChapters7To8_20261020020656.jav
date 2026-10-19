// [ファイルの住所] このファイルが「com.example」というフォルダの直下に置いてあることを示している
package com.example;

// [道具の準備] 別の部屋にあるファイルや、Javaの標準的な機能をここで使うために読み込んでいる
import com.example.entity.Question; // データを詰め込むための「クイズの箱（Question）」を読み込んでいる
import java.util.ArrayList;       // データを順番に並べて保存するための「可変長配列（リスト）」の機能を読み込んでいる
import java.util.List;            // 複数のデータをまとめて「リスト」として扱うためのJavaの標準インターフェースを読み込んでいる

// 第7章〜第8章のオリジナルクイズデータを一時的に保持し、提供するための管理クラス（データバンク第4弾）だ
public class OriginalQuestionBankChapters7To8 {

    // データベースに自動投入するための、第7章〜第8章のクイズデータを生成してリストで返却するメソッド（処理）だ
    public static List<Question> getQuestions() {
        // クイズデータを1問ずつ格納していくための空のリスト（入れ物）を用意している
        List<Question> list = new ArrayList<>();

        // --- 第7章：継承とインターフェースのクイズデータをリストに追加している ---
        list.add(new Question("Javaにおいて、既存のクラスを継承して新しいクラスを定義する際に使用するキーワードはどれか？", "implements", "extends", "inherits", 1));

        // --- 第8章：総合問題・実戦的な読解のクイズデータをリストに追加している ---
        list.add(new Question("Java SE 11 Bronze試験において、全問に共通して最も意識すべきプログラムの実行起点はどれか？", "mainメソッド", "コンストラクター", "インスタンス初期化ブロック", 0));

        // すべてのクイズが詰め終わったリストを、呼び出し元へ引き渡している
        return list;
    }
}
