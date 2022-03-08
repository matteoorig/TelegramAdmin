import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class telegramManager extends Thread{

    String formatUrl;
    public telegramManager(String token) {
        formatUrl = "https://api.telegram.org/bot" + token;
    }

    @Override
    public void run() {
        while(true){



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
        Scanner scan = new Scanner(url.openStream());
        scan.useDelimiter("\u001a");

        String jsonString = scan.next();
        JSONObject jsonObject = new JSONObject(jsonString);//prendo file completo formattato

        JSONArray results = jsonObject.getJSONArray("result");
        if(results.length() > 0){
            for (int i = 0; i < results.length(); i++){
                //int update_id = jsonObject.getJSONArray("result").getJSONObject(i).getInt("update_id");

                long unicId = results.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getInt("id");
                String first_name = results.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getString("first_name");
                String last_name = results.getJSONObject(i).getJSONObject("message").getJSONObject("chat").getString("last_name");


                if(results.getJSONObject(i).getJSONObject("message").getString("text").equals("/"))
                try (PrintWriter writer = new PrintWriter(new File("data.csv"))) {

                    StringBuilder sb = new StringBuilder();
                    sb.append(unicId+"-"+first_name+"-"+last_name);
                    sb.append('\n');

                    writer.write(sb.toString());
                    writer.close();

                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }



    }


    public void writedata(String data){
        try (PrintWriter writer = new PrintWriter(new File("data.csv"))) {

            StringBuilder sb = new StringBuilder();
            sb.append("id");
            sb.append('\n');

            writer.write(sb.toString());
            writer.close();
            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
    public void readCSVFile(){
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("test.csv"));) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(records.toString());
    }
    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
