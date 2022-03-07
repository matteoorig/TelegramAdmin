import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class telegramManager {

    private static BufferedReader in;
    String formatUrl;

    public telegramManager(String token) {
        formatUrl = "https://api.telegram.org/bot" + token;
    }

    public void getUpdates() throws IOException {
        formatUrl = formatUrl + "/getUpdates";
        URL url = new URL(formatUrl);
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = in.readLine()) != null) {

            System.out.println(line);
        }


    }


/*
    public void tempJson() throws IOException {
        InputStream inputStream = new URL(formatUrl).openStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        StringBuilder stringBuilder = new StringBuilder();
        int cp;
        while((cp = bufferedReader.read()) != 1){
            stringBuilder.append((char) cp);
        }
        String jsonText = stringBuilder.toString();

    }

 */
}
