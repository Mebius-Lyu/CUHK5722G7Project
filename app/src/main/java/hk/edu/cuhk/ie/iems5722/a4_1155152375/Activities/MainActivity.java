package hk.edu.cuhk.ie.iems5722.a4_1155152375.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.R;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.Adapter.ChatroomAdapter;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask.FetchRoomTask;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Chatroom;

public class MainActivity extends AppCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final List<Chatroom> chatroomList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setTitle("IEMS5722");

        ChatroomAdapter adapter = new ChatroomAdapter(MainActivity.this, R.layout.roomlistview, chatroomList);
        ListView chatroomListView = findViewById(R.id.room_list_view); // set the view position
        chatroomListView.setAdapter(adapter); // set adapter
        //set click event
        chatroomListView.setOnItemClickListener((arg0, view, position, id) -> {
            Intent intent = new Intent();
            //pass the param chatroom id to the next activity.
            intent.setClass(MainActivity.this, ChatActivity.class);
            intent.putExtra("ChatroomId1",chatroomList.get(position).getId());
            intent.putExtra("ChatroomName", chatroomList.get(position).getName());
            startActivity(intent);
        });

        String URL1="http://3.135.240.182/api/a3/get_chatrooms";
        FetchRoomTask task = new FetchRoomTask(chatroomList);
        task.execute(URL1);
        //Thread way
/*        new Thread(new Runnable() {
            public void run() {
                String result = "";
                result = HttpUrl.fetchData("http://3.135.234.121/api/a2/get_chatrooms");
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    Log.e("Json", result);
                    JSONArray jsonArray = jsonObject1.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        //取出name
                        String name = jsonObject.getString("name");
                        int id = jsonObject.getInt("id");
                        Log.e("Json", name);
                        Log.e("Json", String.valueOf(id));
                        Chatroom chatroom=new Chatroom(name,id);
                        chatroomList.add(chatroom);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } ).start() ;*/
    }

}


