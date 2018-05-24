package com.example.demo;

/**
 * Created by xiaowei on 2018/4/22.
 */


import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.StringBuffer;
import android.util.Log;
import android.os.AsyncTask;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONObject;

public class ShanbayAPI extends AsyncTask<String, String, Long>{
    static final String shanbayLink = "https://api.shanbay.com/bdc/search/?word=";
    private Context cxt;
    private callBack callBack;
    private String translate;
    public ShanbayAPI(Context context){
        cxt = context;
    }

    static private String request(String urlString) {
        // TODO Auto-generated method stub

        StringBuffer sb = new StringBuffer("");
        String cn_translate=null;
        HttpURLConnection connection = null;
        try{
            URL url = new URL(urlString);
            Log.i("url",urlString);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject1 = new JSONObject(sb.toString());
            cn_translate=jsonObject1.getJSONObject("data").getString("definition");
        }
        catch (java.io.IOException e) {
            // Writing exception to log
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            if (connection != null){
                connection.disconnect();
            }
        }
        Log.i("Shanbay",sb.toString());
        if (cn_translate==null){
            cn_translate="Cannot find translation...";
            return cn_translate;
        }
        cn_translate.replaceAll("\\s*","");
        return cn_translate;
    }
    @Override
    protected void onPreExecute() {
        Toast.makeText(cxt, "正在查询...",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Long doInBackground(String... params) {
        String result = request(shanbayLink+params[0].replaceAll("\\p{Punct}",""));
        translate=result;
        Log.i("translate",translate);
        publishProgress(result);
        return 0l;
    }


    @Override
    protected void onProgressUpdate(String... result) {
       //Toast.makeText(cxt, (String)result[0],
               //Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Long result) {
        try{
            //如果callBack不为空，把item参数传给WeatherShow.activity
            if (callBack != null){
                callBack.setTranslate(translate);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public String getTranslate(){
        return translate;
    }
    public void setCallBack(callBack callback){
        this.callBack=callback;
    }
    public interface callBack{
        void setTranslate(String translate);
    }
}
