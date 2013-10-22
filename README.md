# TradeInfo

TradeInfo は村人の取引状況を抽出し、閲覧する Bukkit プラグインです。


## ビルド

NetBeans 7.4 以降で開発しています。Eclipse での開発は別途変換をする必要があります。


## 導入方法

1. project_plugin/dist に生成される jar ファイルを Bukkit サーバの Plugins にコピーしてください。
2. 初回起動時に Bukkit サーバの Plugins/TradeInfo ディレクトリに config.yml が作成されます。output の設定項目に JSON ファイルの出力先を記述してください。この出力先は HTTP サーバから参照可能なものにしてください。
3. project_viewer ディレクトリ (nbproject ディレクトリは不要です) を HTTP サーバから参照可能なディレクトリへコピーしてください。
4. 同じディレクトリの config.js をコメントを参考にして書き換えてください。
5. Bukkit サーバにて /reload を実行します。
6. Web 上から先ほどコピーしたアドレスへアクセスします。


## 著作権

このプラグインは表示部分に Fugue Icons を使用しております。ライセンス: CC BY 3.0

Fugue Icons

(C) 2013 Yusuke Kamiyamane. All rights reserved.

These icons are licensed under a Creative Commons
Attribution 3.0 License.
<http://creativecommons.org/licenses/by/3.0/>
