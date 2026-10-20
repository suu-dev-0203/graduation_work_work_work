package com.example.graduation_java_game;
// このプログラムが com.example.graduation_java_game という部屋に所属していることを宣言している。

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.entity.Question;
import com.example.repository.QuestionRepository;
// 【重要】「com.example.repository」部屋に住んでいる、優秀な通信ロボット（QuestionRepository）を呼び出している。

@Component
// 「このクラスは特別な部品だから、アプリ起動時に自動で見つけて管理してね」と Spring Boot に指示している。
public class OriginalQuestionBankInitializer implements CommandLineRunner {
// アプリ起動直後に自動実行される仕組み（CommandLineRunner）を持った初期化クラスを定義している。

	private final QuestionRepository questionRepository;
	// データベースと直接通信して、クイズデータを保存したり読み出したりする専属ロボット（リポジトリ）の置き場所を用意している。

	// コンストラクタによる依存性注入
	public OriginalQuestionBankInitializer(QuestionRepository questionRepository) {
	// この初期化クラスが生まれるときに、上で用意したデータベース通信ロボット（リポジトリ）をガチッとセットしている。
		this.questionRepository = questionRepository;
		// 外部から届いた本物の通信ロボットを、このクラスの専用ロボットとして登録している。
	}

	@Override
	// CommandLineRunner インターフェースで決められている「起動時に動く処理」の中身を、自分たちの仕様で書き換えている。
	
	public void run(String... args) throws Exception {
	// アプリが立ち上がった瞬間に、Spring Boot の指示によってこの中身が自動的に実行される。

		// データベースがすでに空ではない（データがすでに入っている）場合は、二重登録を防ぐために処理をスキップする
		if (questionRepository.count() > 0) {
		// 通信ロボットを使って「現在データベースにクイズが何問あるか」を数え、1問でもあれば（0より大きければ）分岐に入る。
			return;
			// すでにデータが存在するので、これ以上何もせずにここで安全に処理を終了（ストップ）する。
		}

		// 実物のデータバンクから、第1章のクイズデータの束をありのまま取得する
		List<Question> chapter1Questions = OriginalQuestionBank.getChapter1Questions();
		// 「com.example.OriginalQuestionBank」の「getChapter1Questions()」を呼び出し、本物の5問のクイズの束を取得している。

		// 通信ロボットに、取得したクイズの束を丸ごと渡してデータベースへ一括保存させる
		questionRepository.saveAll(chapter1Questions);
		// リポジトリの「saveAll」という超能力を使い、5問のクイズデータを H2 データベースへ一気に全自動で書き込んで記憶させる。
	}
}