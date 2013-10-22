/* 読み取る JSON ファイルのパス
 * 
 * Plugin ディレクトリの config.yml と同じ場所を指すようにしてください
 * こちらは HTTP サーバから参照できるパスである必要があります
 */
var output_json = 'output.json';

/* Dynmap へのパス
 * 
 * 指定すると座標が Dynmap へのリンクとして機能します
 * クエリ文字列として結合します。ドメイン、ポート、ワールド名、マップタイプは環境に合わせてください
 */
// var dynmap_addr = 'http://example.com:8123/?worldname=world&mapname=flat&zoom=4';