import javax.swing.*;

public class App {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
           // weather app guiの表示
           new WeatherAppGUI().setVisible(true);

           //System.out.println(WeatherApp.getLocationData("東京"));

           //System.out.println(WeatherApp.getCurrentTime());
         }
    });
  }
}