package hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.HttpUrl;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Chatroom;

public class FetchRoomTask extends AsyncTask<String,Void,String> {

    private List<Chatroom> chatroomList=new ArrayList<Chatroom>();

    public FetchRoomTask(List<Chatroom> chatroomList){this.chatroomList=chatroomList;}

    @Override
    protected String doInBackground(String... strings) {
        String urlStr = strings[0];
        String result = "";
        result = HttpUrl.fetchData(urlStr);
        return result;
    }

    @Override
    protected void onPostExecute(String result) {

        try {
            JSONObject jsonObject1 = new JSONObject(result);
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                //取出name
                String name = jsonObject.getString("name");
                int roomid1 = jsonObject.getInt("id");
/*                Log.e("Json", name);
                Log.e("Json", String.valueOf(roomid1));*/
                Chatroom chatroom=new Chatroom(name,roomid1);
                chatroomList.add(chatroom);
            }
            Log.e("rooms add","聊天室加载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
