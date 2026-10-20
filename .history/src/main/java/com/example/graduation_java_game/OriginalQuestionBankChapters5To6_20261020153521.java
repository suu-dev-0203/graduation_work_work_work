// [ファイルの住所] このファイルが「com.example」というフォルダの直下に置いてあることを示している
package com.example;

// [道具の準備] 別の部屋にあるファイルや、Javaの標準的な機能をここで使うために読み込んでいる
import java.util.ArrayList; // データを詰め込むための「クイズの箱（Question）」を読み込んでいる
import java.util.List;       // データを順番に並べて保存するための「可変長配列（リスト）」の機能を読み込んでいる

import com.example.entity.Question;            // 複数のデータをまとめて「リスト」として扱うためのJavaの標準インターフェースを読み込んでいる

// 第5章〜第6章のオリジナルクイズデータを一時的に保持し、提供するための管理クラス（データバンク第3弾）だ
public class OriginalQuestionBankChapters5To6 {

    // データベースに自動投入するための、第5章〜第6章のクイズデータを生成してリストで返却するメソッド（処理）だ
    public static List<Question> getQuestions() {
        // クイズデータを1問ずつ格納していくための空のリスト（入れ物）を用意している
        List<Question> list = new ArrayList<>();

        // --- 第5章：ループ制御（break / continue）とメソッドの定義のクイズデータをリストに追加している ---
        list.add(new Question("ループ処理を完全に中断し、そのループから抜け出すために使用するキーワードはどれか？", "return", "continue", "break", 2));
        list.add(new Question("現在のループ処理の残りの記述をスキップし、次の繰り返しへ進めるキーワードはどれか？", "break", "continue", "skip", 1));

        // --- 第6章：クラスの定義とオブジェクトの生成（インスタンス化）のクイズデータをリストに追加している ---
        list.add(new Question("クラスをもとにオブジェクト（インスタンス）を新しく生成する際に使用するキーワードはどれか？", "new", "create", "make", 0));

        // すべてのクイズが詰め終わったリストを、呼び出し元へ引き渡している
        return list;
    }
}
