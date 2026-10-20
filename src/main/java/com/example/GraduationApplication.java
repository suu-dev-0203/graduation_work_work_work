package com.example;
// このファイルが「com.example」というメインのフォルダ（パッケージ）の直下にあることを示しています。

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
// 別々のフォルダ（小部屋）に分かれている部品を、Spring Bootに漏れなく見つけさせるための部品を読み込んでいます。

@SpringBootApplication
// このクラスが、このWebアプリ全体の「起動スイッチ（司令塔）」になるよとパソコンに教えてあげるマークです。
@ComponentScan(basePackages = {"com.example", "com.example.graduation_java_game"})
// 【最重要】「com.example」部屋と、新しく掘った「com.example.graduation_java_game」部屋のすべての部品を一発でスキャンしてドッキングさせる設定です。
public class GraduationApplication {
// アプリ全体の起動管理を担当する、メインの起動クラスを定義しています。

    public static void main(String[] args) {
    // Javaプログラムが実行されたときに、パソコンが一番最初に呼び出す「本番の起動ボタン（メインメソッド）」です。
        SpringApplication.run(GraduationApplication.class, args);
        // この1行が実行されることで、Webサーバーが立ち上がり、新しく作った全自動初期化ロボットが裏側で自動起動します。
    }
}