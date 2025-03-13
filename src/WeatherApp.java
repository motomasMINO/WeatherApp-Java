import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.net.http.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.net.URI;

public class WeatherApp {
    private static final String API_KEY = "98e8fcbaecf8001bea75ad29cbb3b4e8";

    public static JSONObject getWeatherData(String locationName) {
        try {
            // OpenWeatherMap APIのエンドポイントを構築
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + locationName +
                    "&units=metric&lang=ja&appid=" + API_KEY;

            // APIにリクエストを送信してレスポンスを取得
            String apiResponse = fetchApiResponse(urlString);
            if (apiResponse == null) {
                System.err.println("エラー: APIに接続できませんでした");
                return null;
            }

            // JSONデータを解析
            JSONParser parser = new JSONParser();
            JSONObject resultJsonObj = (JSONObject) parser.parse(apiResponse);

            // 必要なデータを抽出
            JSONObject weatherData = new JSONObject();

            JSONObject main = (JSONObject) resultJsonObj.get("main");
            if (main != null) {
                double temperature = main.containsKey("temp") ? ((Number) main.get("temp")).doubleValue() : 0.0;
                long humidity = main.containsKey("humidity") ? ((Number) main.get("humidity")).longValue() : 0;
                weatherData.put("temperature", temperature);
                weatherData.put("humidity", humidity);
            }

            Object weatherObj = resultJsonObj.get("weather");
            if (weatherObj instanceof org.json.simple.JSONArray) {
                org.json.simple.JSONArray weatherArray = (org.json.simple.JSONArray) weatherObj;
                if (!weatherArray.isEmpty() && weatherArray.get(0) instanceof JSONObject) {
                    JSONObject weather = (JSONObject) weatherArray.get(0);
                    String weatherCondition = (String) weather.getOrDefault("description", "不明");
                    weatherData.put("weather_condition", weatherCondition);
                } else {
                    weatherData.put("weather_condition", "不明");
                }
            }

            JSONObject wind = (JSONObject) resultJsonObj.get("wind");
            if (wind != null) {
                double windSpeed = wind.containsKey("speed") ? ((Number) wind.get("speed")).doubleValue() : 0.0;
                weatherData.put("windspeed", windSpeed);
            }

            return weatherData;

        } catch (Exception e) {
            System.err.println("データ処理中にエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String fetchApiResponse(String urlString) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(urlString)).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("HTTPエラー: " + response.statusCode());
                System.err.println("レスポンス: " + response.body());
                return null;
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            System.err.println("接続中にエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
