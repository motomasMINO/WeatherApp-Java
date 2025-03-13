import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WeatherAppGUI extends JFrame {
  private JSONObject weatherData;

  public WeatherAppGUI() {
    // GUIのセットアップとタイトルの追加
    super("お天気アプリ");

    // プログラムが閉じられたら、そのプロセスを終了するようにGUIを設定する
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // GUIのサイズを設定(ピクセル)
    setSize(450, 650);

    // 画面中央にGUIをロード
    setLocationRelativeTo(null);

    // レイアウトマネージャーをnullにして、GUI内のコンポーネントを手動で配置する
    setLayout(null);

    //GUIのサイズ変更は不可
    setResizable(false);

    //GUIの背景画像パネルを追加
    BackgroundPanel backgroundPanel = new BackgroundPanel("src/Background.jpg");
    backgroundPanel.setLayout(null);
    setContentPane(backgroundPanel);

    addGuiComponents();
  }

  private void addGuiComponents() {
    // 検索フィールド
    JTextField searchTextField = new JTextField();

    // コンポーネントの位置とサイズを設定する
    searchTextField.setBounds(15, 15, 351, 45);

    // フォントスタイルとサイズを変更
    searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));

    add(searchTextField);
    
    // 天気 画像
    JLabel weatherConditionImage = new JLabel(loadImage("src/cloudy.png"));
    weatherConditionImage.setBounds(0, 125, 450, 217);
    add(weatherConditionImage); 

    // 温度 テキスト
    JLabel temperatureText = new JLabel("10℃");
    temperatureText.setBounds(0, 350, 450, 54);
    temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
    temperatureText.setForeground(Color.WHITE);

    // テキストを中央に配置する
    temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
    add(temperatureText);

    // 天候の説明
    JLabel weatherConditionDesc = new JLabel("曇り");
    weatherConditionDesc.setBounds(0, 405, 450, 36);
    weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
    weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
    weatherConditionDesc.setForeground(Color.WHITE);
    add(weatherConditionDesc);

    // 湿度 画像
    JLabel humidityImage = new JLabel(loadImage("src/humidity.png"));
    humidityImage.setBounds(15, 500, 74, 66);
    add(humidityImage);

    // 湿度 テキスト
    JLabel humidityText = new JLabel("<html><b>湿度</b> 100%</html>");
    humidityText.setBounds(90, 500, 85, 55);
    humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
    humidityText.setForeground(Color.WHITE);
    add(humidityText);

    // 風速 画像
    JLabel windspeedImage = new JLabel(loadImage("src/windspeed.png"));
    windspeedImage.setBounds(220, 500, 74, 66);
    add(windspeedImage);

    // 風速 テキスト
    JLabel windspeedText = new JLabel("<html><b>風速</b> 15km/h</html>");
    windspeedText.setBounds(310, 500, 85, 55);
    windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
    windspeedText.setForeground(Color.WHITE);
    add(windspeedText);

    // 検索ボタン
    JButton searchButton = new JButton(loadImage("src/searchButton.png"));

    // このボタンにカーソルを置くと、カーソルがハンドカーソルに変わる
    searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    searchButton.setBounds(375, 13, 47, 45);
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // ユーザーから位置情報を取得
        String userInput = searchTextField.getText().trim();

        // 入力を検証 - 空白を削除してテキストが空にならないようにする
        if(userInput.isEmpty()) {
           JOptionPane.showMessageDialog(WeatherAppGUI.this, "地名を入力してください", "入力エラー", JOptionPane.WARNING_MESSAGE);
          return;
        }

        // 天気データを取得
        weatherData = WeatherApp.getWeatherData(userInput);

        if(weatherData == null) {
          JOptionPane.showMessageDialog(
            WeatherAppGUI.this, 
            "天気データを取得できませんでした\n正しい地名を入力してください", 
              "エラー", 
                    JOptionPane.ERROR_MESSAGE);
                    return;
        }

        // GUIの更新
        String weatherCondition = (String)weatherData.get("weather_condition");
        switch(weatherCondition) {
            case "晴天":
                weatherConditionImage.setIcon(loadImage("src/clear.png"));
                break;
            case "薄い雲":
            case "雲":
            case "曇りがち":
            case "厚い雲":
                weatherConditionImage.setIcon(loadImage("src/cloudy.png"));
                break;
            case "光強度霧雨":
            case "霧雨":
            case "強い霧雨":
            case "弱い霧雨":
            case "重い強度霧雨":
            case "光強度霧雨の雨":
            case "霧雨の雨":
            case "重い強度霧雨の雨":
            case "にわかの雨と霧雨":
            case "重いにわかの雨と霧雨":
            case "にわか霧雨":
            case "小雨":
            case "適度な雨":
            case "強い雨":
            case "重い強度の雨":
            case "非常に激しい雨":
            case "極端な雨":
            case "雨氷":
            case "光強度のにわかの雨":
            case "にわかの雨":
            case "重い強度にわかの雨":
            case "不規則なにわかの雨":
            case "弱いにわか雨":
                weatherConditionImage.setIcon(loadImage("src/rain.png"));
                break;
            case "小雨と雷雨":
            case "雨と雷雨":
            case "大雨と雷雨":
            case "光雷雨":
            case "雷雨":
            case "重い雷雨":
            case "ぼろぼろの雷雨":
            case "霧雨と雷雨":
            case "重い霧雨と雷雨":
            case "雷を伴う強い雨":
            case "雷を伴う弱い雨":
                weatherConditionImage.setIcon(loadImage("src/thunderStorm.png"));
                break;
            case "小雪":
            case "雪":
            case "大雪":
            case "みぞれ":
            case "にわかみぞれ":
            case "光雨と雪":
            case "雨や雪":
            case "光のにわか雪":
            case "にわか雪":
            case "重いにわか雪":
                weatherConditionImage.setIcon(loadImage("src/snow.png"));
                break;
            case "ミスト":
            case "霧":
            case "濃霧":
            case "薄霧":
                weatherConditionImage.setIcon(loadImage("src/fog.png"));
                break;
            default:
                weatherConditionImage.setIcon(loadImage("src/cloudy.png"));
                System.out.println("未知の天気コード: " + weatherCondition);    
        }

        // 温度テキストの更新
        double temperature = ((Number) weatherData.get("temperature")).doubleValue();
        temperatureText.setText(temperature + "℃");

        // 天候テキストの更新
        weatherConditionDesc.setText(weatherCondition);

        // 湿度テキストの更新
        long humidity = ((Number) weatherData.get("humidity")).longValue();
        humidityText.setText("<html><b>湿度</b> " + humidity + "%</html>");

        // 風速テキストの更新
        double windspeed = ((Number) weatherData.get("windspeed")).doubleValue();
        windspeedText.setText("<html><b>風速</b> " + windspeed + "km/h</html>");
      }
    });
    add(searchButton);
  }

  // GUIコンポーネントで画像を作成する
  private ImageIcon loadImage(String resourcePath) {
      try {
          // 指定されたパスから画像ファイルを読み込む
          BufferedImage image = ImageIO.read(new File(resourcePath));

          // コンポーネントがレンダリング出来るように、画像アイコンを返す
          return new ImageIcon(image);
      }catch(IOException e) {
        e.printStackTrace();
      }

      System.out.println("リソースが見つかりません");
      return null;
  }
}