import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

class telegramManager extends Thread{

    String formatUrl;
    public telegramManager(String token) {
        formatUrl = "https://api.telegram.org/bot" + token;
    }

    @Override
    public void run() {
        while(true){


            try {
                getUpdates();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUpdates() throws IOException {
        String tmpUrl =  formatUrl + "/getUpdates";
        URL url = new URL(tmpUrl);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = in.readLine()) != null) {

            System.out.println(line);
        }
    }
}
