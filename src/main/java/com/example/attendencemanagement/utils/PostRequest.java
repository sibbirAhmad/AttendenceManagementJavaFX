package com.example.attendencemanagement.utils;
import javafx.concurrent.Task;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
public class PostRequest extends Task<Void> {
    String serverUrl ;
    PostRequestListener listener;
    String jsonData ;
    public PostRequest(String url,String jsonAsString,PostRequestListener listener){

        getTestData();
        this.jsonData = jsonAsString;
        this.listener = listener;
        this.serverUrl =
                "https://script.google.com/macros/s/AKfycbymxi6suZv0zOLw0ZC0QWBV6KdGjTZR9OMsYa4YyfUkSR91tdRyYcRj0BkLZefkjD7e_w/exec";


        Thread thread = new Thread(this,"Post Request");
       thread.setDaemon(true);
       thread.start();
    }

    public interface PostRequestListener {
        void onSuccess(String response);
        void onFailed(String cause);
    }
    @Override
    protected Void call() throws Exception {
        postRequest();
        return null;
    }
    private void postRequest(){
        try {
            //Todo : preparing connection
            URL url = new URL (serverUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            //Todo : Writing post request json data
            OutputStream outputStream = con.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(jsonData);
            bufferedWriter.flush();
            System.out.println("Requested json : "+jsonData);
            //---------- todo : Now Read
            int statusCode = con.getResponseCode();
            System.out.println("Request Status Code : "+statusCode);
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String result;
            while ((result = bufferedReader.readLine()) != null) {
                sb.append(result + "\n");
            }
            System.out.println("Response : \n"+sb.toString());
            if (listener!=null) listener.onSuccess(sb.toString());
            if(outputStream != null) outputStream.close();
            if(bufferedWriter!=null)  bufferedWriter.close();
            if(bufferedReader!=null)  bufferedReader.close();
            if(con!=null) con.disconnect();

        }catch (Exception e){
            if (listener!=null) listener.onFailed("Failed : "+e.getLocalizedMessage());
        }finally {

        }
    }
    private void createPresentStudentList(ArrayList<String> presentIds){
    String ids = String.valueOf(presentIds.toArray());
        System.out.println(ids);
    }
    private String getTestData(){

        String m = "{\n" +
                "  \"students\": {\n" +
                "    \"name\": \"Kamal\",\n" +
                "    \"Id\": 12345678\n" +
                "  }\n" +
                "}";

        String json = "details={\"name\":\"myname is #@83 kAMAL\",\"age\":\"20\"} ";
       return m;

    }

    @Override
    protected void done() {
        super.done();
        System.out.println("Done");
    }
}
