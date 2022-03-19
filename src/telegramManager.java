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

    public void addEvent(String citta, int r, String text ) throws IOException, ParserConfigurationException, SAXException, InterruptedException {
        List<String> tmpCSV = getDataCsv("db.csv");

        PrintWriter writer = new PrintWriter(new File("db.csv"));
        for(int i = 0; i<tmpCSV.size(); i++){
            writer.append(tmpCSV.get(i));
        }
        writer.append(citta + "-" + r + "-" + text);
        writer.close();

        sendEvents(tmpCSV);
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






    public void sendEvents(List<String> db ) throws IOException, ParserConfigurationException, SAXException, InterruptedException {

        //posizione evento e raggio e testo
        //CCordinata => mi faccio dare lat1 | long1
        //RAGGIO = raggio dell'evento                                       es. 4km
        //for(per ogni user in data.csv)
        //  prendo lat2 | long2 | ID
        //  DISTANZA = ottengo la distanza in km tra lat1 | long1 e lat2 | long2       es. 12Km
        //  se DISTANZA <= RAGGIO
        //    (evento vicino a te!)
        //    invio a ID il testo

        //--------------------------------//

        //per ogni evento presente nel db
        for(int I = 0; I< db.size(); I++){

            //posizione evento e raggio e testo
            String[] arrayTmp = db.get(I).split("-");               //Mariano-7-ciao!

            String citta = arrayTmp[0];
            int raggio = Integer.parseInt(arrayTmp[1]);
            String testo = arrayTmp[2];

            //CCordinata => mi faccio dare lat1 | long1
            CCordinata cittaEv = M.getPosition(citta);

            //for(per ogni user in data.csv)
            List<String> users = getDataCsv("dat.csv");
            for (int i = 0; i < users.size(); i++){

                //  prendo lat2 | long2 | ID
                String[] arrayUser = users.get(i).split("/");       //387630778-Matteo-Origgi-Saronno/45.625675201416016-9.037328720092773

                String[] arrayUser1 = arrayUser[0].split("-");
                long idUser = Long.parseLong(arrayUser1[0]);

                String[] arrayUser2 = arrayUser[1].split("-");
                double lat = Double.parseDouble(arrayUser2[0]);
                double lon = Double.parseDouble(arrayUser2[1]);

                //  DISTANZA = ottengo la distanza in km tra lat1 | long1 e lat2 | long2
                int distance = M.distance(cittaEv.getLat(), cittaEv.getLon(), lat, lon);

                //  se DISTANZA <= RAGGIO
                //    invio a ID il testo
                if(distance <= raggio){
                    sendMessage(idUser, testo);
                }


            }

        }




    }

    public List<String> getDataCsv(String path){
        List<String> tmpCSV = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(path));) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine()+ "\n";
                tmpCSV.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tmpCSV;
    }










}
