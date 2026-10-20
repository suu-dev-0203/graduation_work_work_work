package com.example.graduation_java_game;
// このプログラムが com.example.graduation_java_game という部屋に所属していることを宣言している。

import java.util.List;
// 複数のデータをまとめて1つの束として扱うための List という部品を取り込んでいる。

import org.springframework.boot.CommandLineRunner;
// アプリ起動時に特定の処理を全自動で1度だけ実行させるための CommandLineRunner という仕組みを取り込んでいる。
import org.springframework.stereotype.Component;
// このクラスを「自動起動する部品」として Spring Boot に登録するための @Component アノテーションを取り込んでいる。

import com.example.entity.Question;
// 【超重要】「com.example.entity」部屋に住んでいる、新しく拡張したクイズの箱（Question）を呼び出して使えるようにしている。
import com.example.repository.QuestionRepository;
// 【超重要】「com.example.repository」部屋に住んでいる、優秀な通信ロボット（QuestionRepository）を呼び出して使えるようにしている。

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

		// オリジナル問題集から、全8章260問の「種データ」をすべてまとめて取得する
		List<OriginalQuestionBank.ChapterSeed> seeds = OriginalQuestionBank.chapters();
		// データバンクファイルを呼び出し、全8章の章名とそれぞれに紐づくクイズ問題の束をすべて集めて seeds に格納している。

		// 章（Chapter）の束を1つずつ取り出して処理する
		for (OriginalQuestionBank.ChapterSeed chapterSeed : seeds) {
		// 全8章が入った seeds の中から、「第1章」「第2章」...と、順番に1章分のデータを取り出してループ処理を行う。

			// その章に紐づいているクイズ問題の束を取得する
			List<OriginalQuestionBank.BankQuestion> bankQuestions = chapterSeed.questions();
			// 取り出した章のデータから、その章に含まれているすべてのクイズ問題（20〜35問程度）をまとめて取得している。

			// クイズ問題を1問ずつ取り出して、データベース保存用のエンティティへ変換して保存する
			for (OriginalQuestionBank.BankQuestion bq : bankQuestions) {
			// 取得したクイズ問題の束から、本物のクイズデータを「1問ずつ」順番に取り出してループ処理を行う。

				// データベースのテーブル（QUESTION）の形状に適合したエンティティ「Question」を新しく生成する
				Question question = new Question(
				// 拡張された10個の引数を受け取る「新しいコンストラクタの形状」に合わせてデータをガチッと流し込んでいる。
						bq.questionText(),
						// 箱の1番目：データバンクから取得した「クイズの問題文」をセットしている。
						bq.codeBlock(),
						// 箱の2番目：画面に黒枠で表示するための「ソースコード（プログラムコード）」をセットしている。
						bq.choices().get(0),
						// 箱の3番目：シャッフル済みの選択肢リストから、0番目の位置にある「選択肢A」の文字をセットしている。
						bq.choices().get(1),
						// 箱の4番目：シャッフル済みの選択肢リストから、1番目の位置にある「選択肢B」の文字をセットしている。
						bq.choices().get(2),
						// 箱の5番目：シャッフル済みの選択肢リストから、2番目の位置にある「選択肢C」の文字をセットしている。
						bq.choices().get(3),
						// 箱の6番目：シャッフル済みの選択肢リストから、3番目の位置にある「選択肢D」の文字をセットしている。
						bq.choices().get(4),
						// 箱の7番目：シャッフル済みの選択肢リストから、4番目の位置にある「選択肢E」の文字をセットしている。
						bq.correctChoiceIndex(),
						// 箱の8番目：0〜4の数字で表される、シャッフル後の正確な「正解の番号（インデックス）」をセットしている。
						bq.detailedExplanation(),
						// 箱の9番目：【超重要】各選択肢の合否理由や復習範囲がまとめられた「詳細な解説文（detailedExplanation）」をセットしている。
						bq.timeLimitSeconds()
						// 箱の10番目：ゲームの緊張感を演出する、1問あたりの「制限時間（秒数）」をセットしている。
				);

				// 通信ロボットに、完成したクイズの箱を渡してデータベースへ保存させる
				questionRepository.save(question);
				// リポジトリの save 命令を実行し、新しく作ったクイズ1問のデータを H2 データベースへしっかりと書き込んで記憶させる。
			}
		}
	}
}