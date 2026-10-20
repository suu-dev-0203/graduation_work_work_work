// [ファイルの住所] このファイルが「com.example」というフォルダの直下に置いてあることを示している
package com.example;

// [道具の準備] 別の部屋にあるファイルや、Javaの標準的な機能をここで使うために読み込んでいる
import java.util.ArrayList; // データを詰め込むための「クイズの箱（Question）」を読み込んでいる
import java.util.List;       // データを順番に並べて保存するための「可変長配列（リスト）」の機能を読み込んでいる

import com.example.entity.Question;            // 複数のデータをまとめて「リスト」として扱うためのJavaの標準インターフェースを読み込んでいる

// 第2章〜第4章のオリジナルクイズデータを一時的に保持し、提供するための管理クラス（データバンク第2弾）だ
public class OriginalQuestionBankChapters2To4 {

    // データベースに自動投入するための、第2章〜第4章のクイズデータを生成してリストで返却するメソッド（処理）だ
    public static List<Question> getQuestions() {
        // クイズデータを1問ずつ格納していくための空のリスト（入れ物）を用意している
        List<Question> list = new ArrayList<>();

        // --- 第2章：条件分岐（if文 / switch文）のクイズデータをリストに追加している ---
        list.add(new Question("if文の条件式において、結果が正しい（真）のときに実行されるブロックを示すキーワードはどれか？", "then", "else", "（キーワードなし。波括弧 {} のみ）", 2));
        list.add(new Question("if文の条件が成立しなかった場合に、別の処理を実行させるためのキーワードはどれか？", "else", "except", "otherwise", 0));

        // --- 第3章：繰り返し処理（for文 / while文）のクイズデータをリストに追加している ---
        list.add(new Question("for文の構文（初期化式; 条件式; 変化式）において、各式を区切るために使用する記号はどれか？", "カンマ ,", "セミコロン ;", "コロン :", 1));

        // --- 第4章：配列のオブジェクトのクイズデータをリストに追加している ---
        list.add(new Question("配列の要素数を取得するために使用する、配列オブジェクトのプロパティ名はどれか？", "size()", "length", "count", 1));

        // すべてのクイズが詰め終わったリストを、呼び出し元へ引き渡している
        return list;
    }
}
