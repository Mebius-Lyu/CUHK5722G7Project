package hk.edu.cuhk.ie.iems5722.a4_1155152375;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Msg;

public class HttpUrl {
    public static String fetchData(String urlStr) {
        String result = "";
        InputStream is = null;
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            //start the query
            conn.connect();
            int response = conn.getResponseCode(); // 200 if successfull
            is = conn.getInputStream();
            // Convert the InputStream into a string
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result += line;
            }

            if (response == HttpURLConnection.HTTP_OK) {
                Log.e("response success", "成功获取url内容");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(is!=null){
                try {

                    is.close();//Close the InputStream when done
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        return result;
    }

    public static String convertJson(JSONObject json, Msg msg){

        try {
            json.put("chatroom_id",msg.getChatroom_id());
            json.put("user_id",msg.getUser_id());
            json.put("name",msg.getUsername());
            json.put("message",msg.getMessage_only());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(!json.toString().isEmpty()){
            Log.e("json success", "字符串生成json成功");
        }
        return json.toString();

    }

    public static void postData(String url, String json_string) throws IOException {
        URL url1=new URL(url);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        //// POST请求
        OutputStream os = conn.getOutputStream();

        // 读取响应
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        Uri.Builder builder = new Uri.Builder();


        // build the param using ArrayList objects para_name and para_values
        String name = null,message = null,message_time = null;
        int user_id = 0,chatroom_id = 0;

        try {
            JSONObject jsontext = new JSONObject(json_string);
            chatroom_id = jsontext.getInt("chatroom_id");
            user_id = jsontext.getInt("user_id");
            name = jsontext.getString("name");
            message = jsontext.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.appendQueryParameter("chatroom_id", String.valueOf(chatroom_id));
        builder.appendQueryParameter("user_id", String.valueOf(user_id));
        builder.appendQueryParameter("name", name);
        builder.appendQueryParameter("message", message);

        String query = builder.build().getEncodedQuery();


        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        int responseCode = conn.getResponseCode();
        Log.e("responseCode", String.valueOf(responseCode));
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Log.e("response success", "消息上传成功");
        }
    }

    public static void postSocket(String url, String json_string) throws IOException {
        URL url1=new URL(url);
        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        //// POST请求
        OutputStream os = conn.getOutputStream();

        // 读取响应
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        Uri.Builder builder = new Uri.Builder();


        // build the param using ArrayList objects para_name and para_values
        String name = null,message = null,message_time = null;
        int user_id = 0,chatroom_id = 0;

        try {
            JSONObject jsontext = new JSONObject(json_string);
            chatroom_id = jsontext.getInt("chatroom_id");
            user_id = jsontext.getInt("user_id");
            name = jsontext.getString("name");
            message = jsontext.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        builder.appendQueryParameter("chatroom_id", String.valueOf(chatroom_id));
        builder.appendQueryParameter("user_id", String.valueOf(user_id));
        builder.appendQueryParameter("name", name);
        builder.appendQueryParameter("message", message);

        String query = builder.build().getEncodedQuery();


        writer.write(query);
        writer.flush();
        writer.close();
        os.close();

        int responseCode = conn.getResponseCode();
        Log.e("responseCode", String.valueOf(responseCode));
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Log.e("response success", "消息上传成功");
        }
    }

}
