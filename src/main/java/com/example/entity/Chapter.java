package com.example.entity; // このファイルが「entity」フォルダにあることを示しています

// データベース（H2 DB）とJavaを連携させるための仕組み（JPA）を読み込んでいます
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // 「これはデータベースのテーブルと連動するクラスですよ」という合図です
@Table(name = "chapters") // データベース内でのテーブル名を「chapters」に指定しています
public class Chapter {

    @Id // この項目が、データを1件ずつ識別するための「主キー（プライマリキー）」になります
    @GeneratedValue(strategy = GenerationType.IDENTITY) // データが追加されると、1, 2, 3... と自動で番号を増やしてくれます
    private Long id; // 章の識別番号（ID）を入れる変数です

    private String name; // 章の名前（例：「変数」「条件分岐」など）を入れる変数です

    // --- コンストラクタ（クラスの初期化ルール） ---
    
    // 引数（ひきすう）なしのコンストラクタ
    // JPAという仕組みが裏側で自動的にデータを作るために「絶対に必要」な空っぽのルールです
    public Chapter() {
    }

    // 引数ありのコンストラクタ
    // 新しい章を作るときに、最初から名前をセットして作りやすくするためのルールです
    // 例：new Chapter("演算子") のように使います
    public Chapter(String name) {
        this.name = name;
    }

    // --- ゲッター ・ セッター（変数の中身を安全に読み書きするための部品） ---

    // idを外から手に入れるための部品（ゲッター）
    public Long getId() {
        return id;
    }

    // idを外から新しく書き換えるための部品（セッター）
    public void setId(Long id) {
        this.id = id;
    }

    // 章の名前を外から手に入れるための部品（ゲッター）
    public String getName() {
        return name;
    }

    // 章の名前を外から新しく書き換えるための部品（セッター）
    public void setName(String name) {
        this.name = name;
    }
}
