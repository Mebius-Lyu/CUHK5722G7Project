package hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.HttpUrl;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Msg;

public class FetchMessageHistoryTask extends AsyncTask<String,Void,String> {

    private  List<Msg> msgList=new ArrayList<Msg>();


    public FetchMessageHistoryTask(List<Msg> msgList){this.msgList=msgList;}


    @Override
    protected String doInBackground(String... strings) {
        String urlStr = strings[0];
        String result = "";
        result = HttpUrl.fetchData(urlStr);

        return result;
    }


    public int total_pages;
    int current_page;

    @Override
    protected void onPostExecute(String result) {

        try {
            JSONObject json = new JSONObject(result);//{"status","data"}
//            Log.e("jsonConvert successful", "消息从String->json转义成功");
            String status = json.getString("status");
            JSONObject data = json.getJSONObject("data"); //{"current_page","messages","total_pages"}

            total_pages= data.getInt("total_pages");
            current_page = data.getInt("current_page");

            Log.e("total page ", String.valueOf(total_pages));


            //message
            JSONArray messages = data.getJSONArray("messages");
            if(status.equals("OK")){
                for (int i = 0; i < messages.length(); i++) {
                    int chatroom_id = messages.getJSONObject(i).getInt("chatroom_id");
                    int user_id = messages.getJSONObject(i).getInt("user_id");
                    String name = messages.getJSONObject(i).getString("name");
                    String message = messages.getJSONObject(i).getString("message");
                    String messages_time = messages.getJSONObject(i).getString("message_time");

                    if(Objects.equals(name, "Lyu Chenghao")){ //my own messages
                        Msg msg0=new Msg(0,chatroom_id,user_id,name,message,messages_time);
                        msgList.add(0,msg0);
                    }else{
                        Msg msg1=new Msg(1,chatroom_id,user_id,name,message,messages_time);
                        msgList.add(0,msg1);
                    }
                }
//                Log.e("messsages add","消息加载成功");
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    public void getCurrentpage(int current_page){
        this.current_page=current_page;
    }

}
