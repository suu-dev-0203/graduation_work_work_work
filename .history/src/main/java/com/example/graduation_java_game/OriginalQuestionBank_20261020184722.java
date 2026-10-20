// [ファイルの住所] このファイルが「com.example」というフォルダの直下に置いてあることを示している
package com.example.graduation_java_game;


// [道具の準備] 別の部屋にあるファイルや、Javaの標準的な機能をここで使うために読み込んでいる
import java.util.ArrayList; // データを詰め込むための「クイズの箱（Question）」を読み込んでいる
import java.util.List;       // データを順番に並べて保存するための「可変長配列（リスト）」の機能を読み込んでいる

import com.example.entity.Question;            // 複数のデータをまとめて「リスト」として扱うためのJavaの標準インターフェースを読み込んでいる

// 全8章・260問のオリジナルクイズデータを一時的に保持し、提供するための管理クラス（データバンク）だ
public class OriginalQuestionBank {

    // データベースに自動投入するための、第1章の全クイズデータを生成してリストで返却するメソッド（処理）だ
    public static List<Question> getChapter1Questions() {
        // クイズデータを1問ずつ格納していくための空のリスト（入れ物）を用意している
        List<Question> list = new ArrayList<>();

        // リストに対して、新しいクイズのデータを1問ずつ作成して追加（add）していく
        // 各行は「問題文」「選択肢A」「選択肢B」「選択肢C」「正解インデックス（0=A, 1=B, 2=C）」の構成になっている
        list.add(new Question("Javaのソースコードをコンパイルした後に生成されるファイルはどれか？", ".exeファイル", ".classファイル", ".javaファイル", 1));
        list.add(new Question("Javaプログラムを実行する仮想マシンの名称はどれか？", "JVM", "JDK", "JRE", 0));
        list.add(new Question("Javaにおいて、1行コメントを表す記号はどれか？", "/*", "#", "//", 2));
        list.add(new Question("データ型「int」が表すことができる値の種類はどれか？", "整数", "浮動小数点数", "単一の文字", 0));
        list.add(new Question("真偽値（trueまたはfalse）を保持するために使用するデータ型はどれか？", "int", "String", "boolean", 2));

        // すべてのクイズが詰め終わったリストを、呼び出し元へ引き渡している
        return list;
    }
}
