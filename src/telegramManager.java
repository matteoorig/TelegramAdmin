import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class telegramManager extends Thread{

    openStreatMapManager M;
    String formatUrl;
    long globalOffset = 554595728;
    public telegramManager(String token, openStreatMapManager c) {
        formatUrl = "https://api.telegram.org/bot" + token;
        this.M = c;
    }

    @Override
    public void run() {
        while(true){


            try {
                getUpdates();
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void getUpdates() throws IOException, ParserConfigurationException, SAXException {
        String tmpUrl =  formatUrl + "/getUpdates?offset="+ globalOffset;
        URL url = new URL(tmpUrl);
        Scanner scan = new Scanner(url.openStream());
        scan.useDelimiter("\u001a");

        String jsonString = scan.next();
        JSONObject jsonObject = new JSONObject(jsonString);//prendo file completo formattato
        JSONArray results = jsonObject.getJSONArray("result");

        if(results.length() > 0){
            for (int i = 0; i < results.length(); i++){
                globalOffset = results.getJSONObject(i).getInt("update_id");
                globalOffset =globalOffset+1;
                long unicId = 0;
                String first_name = "";
                String last_name = "";
                String text = "";

                if(results.getJSONObject(i).has("message")){
                  unicId = results.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getInt("id");
                  first_name = results.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getString("first_name");
                  last_name = results.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getString("last_name");
                  text = results.getJSONObject(i).getJSONObject("message").getString("text");
                }

                //parse text
                if(text.contains("/citta")){
                    //controllare se esiste tra gli utenti già inseriti, se esiste già modifico se no aggiungo
                    checkPresent(first_name, last_name, unicId, text);
                }
            }
        }



    }

    private void checkPresent(String nome, String cognome, long id, String pText) throws IOException, ParserConfigurationException, SAXException {
        List<String> tmpCSV = new ArrayList<>();
        String[] array = pText.split("\\s+");
        if(array.length == 1 || id == 0){
            return;
        }
        //darmi la posizione
        CCordinata cordinata = M.getPosition(array[1]);
        String tmpClient = id + "-" + nome + "-" + cognome + "-"+ array[1]+ "/" + cordinata.getLat() + "-" + cordinata.getLon();



        boolean modificato=false;
        try (Scanner scanner = new Scanner(new File("data.csv"));) {
            while (scanner.hasNextLine()) {

                String tmpLine = scanner.nextLine();
                String arrayGet[] = tmpLine.split("-");
                if(arrayGet[0].equals(""+id)){
                    tmpCSV.add(tmpClient + "\n");
                    modificato=true;
                    sendMessage(id, "La tua città "+ array[1] + " è stata modificata");
                }else{
                    tmpCSV.add(tmpLine + "\n");
                }

            }
            if(!modificato){
                tmpCSV.add(tmpClient+"\n");
                sendMessage(id, "La tua città "+ array[1] + " è stata aggiunta");
            }

        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        PrintWriter writer = new PrintWriter(new File("data.csv"));
        for (int i = 0; i < tmpCSV.size(); i++){
            writer.append(tmpCSV.get(i));
        }
        writer.close();



    }


    public void sendMessage(long id, String mess) throws IOException, InterruptedException {
        String urlString = formatUrl + "/sendMessage?chat_id=" + id + "&text=" + getEncodedString(mess);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        //ritorna ok se è stata effettuato l'invio
        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }


    public String getEncodedString(String toEncode) throws UnsupportedEncodingException {
        return URLEncoder.encode(toEncode, StandardCharsets.UTF_8.toString());
    }

}
