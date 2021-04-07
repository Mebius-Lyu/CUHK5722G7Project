package hk.edu.cuhk.ie.iems5722.a4_1155152375.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.MenuItemCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hk.edu.cuhk.ie.iems5722.a4_1155152375.Adapter.MsgAdapter;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask.FetchMessageHistoryTask;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask.FetchMessageTask;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask.PostMessageTask;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.HttpUrl;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.R;
import hk.edu.cuhk.ie.iems5722.a4_1155152375.entities.Msg;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity implements AbsListView.OnScrollListener{

    private ListView msgListView;
    private EditText inputText;
    private MsgAdapter adapter;
    private final List<Msg> msgList= new ArrayList<>();
    private int ChatroomId;
    private String ChatroomName;
    private Menu mOptionsMenu;
    private Socket socket;
    public int total_page;
    private int current_page=1;
    private static final String TAG = "ChatActivity";



    // Set Backbutton
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        // Refresh
        if(item.getItemId() == R.id.chat_refresh){
            setRefreshActionButtonState();
            // 重新加载当前页面。最好之后可以改成只刷新listview的部分
            finish();
            Intent intent = new Intent();
            intent.putExtra("ChatroomName",ChatroomName);
            intent.putExtra("ChatroomId1",ChatroomId);
            intent.setClass(this, ChatActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    // refresh button
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    // Refresh animation
    private void setRefreshActionButtonState() {
        if(mOptionsMenu == null) {
            return;
        }

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.chat_refresh);

        if(refreshItem != null) {
            if(true) {
                MenuItemCompat.setActionView(refreshItem,
                R.layout.actionbar_indeterminate_progress);
            } else {
                MenuItemCompat.setActionView(refreshItem, null);
            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);





        Intent intent = getIntent();
        ChatroomId = intent.getIntExtra("ChatroomId1",0);
        ChatroomName = intent.getStringExtra("ChatroomName");
        Log.e("当前聊天室id: ", String.valueOf(ChatroomId));
        Toast.makeText(this,"Here is Chatroom:"+ChatroomName+"\r\n"+"Please wait, Pages are Loading",Toast.LENGTH_SHORT).show();

        try{
            socket = IO.socket("http://3.135.240.182:8001");
            JSONObject jsondata = new JSONObject();
            jsondata.put("chatroom_id",ChatroomId);

            socket.on(Socket.EVENT_CONNECT, onConnectSuccess);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_DISCONNECT, onDisConnect);
            socket.on("Messages", onNewMessage);
            socket.emit("join",jsondata);
            socket.connect();
        } catch (URISyntaxException | JSONException e) {
            e.printStackTrace();
        }


        this.setTitle(ChatroomName);
        adapter=new MsgAdapter(ChatActivity.this,R.layout.chatlistview,msgList);
        inputText= findViewById(R.id.input_text);
        Button send = findViewById(R.id.send);

        msgListView= findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);


// 设置返回按钮
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        //时间显示
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= new Date();
        String timestr = sdf.format(date);
        JSONObject json = new JSONObject(); //json 是 json

        //消息发送
        send.setOnClickListener(v -> {
            String content=inputText.getText().toString(); // 获取编辑框内容
            if (!"".equals(content)){  //编辑框内容不为空
                //消息主体
                Msg msg=new Msg(0,ChatroomId,1155152375,"Lyu Chenghao",content,timestr);

                String json_string= HttpUrl.convertJson(json,msg);
                String URL3="http://3.135.240.182/api/a3/send_message";
                PostMessageTask task = new PostMessageTask(json_string);
                task.execute(URL3);

                msgList.add(msg);// 将消息加入msgList
                adapter.notifyDataSetChanged();//There is a new message refresh
                msgListView.setSelection(msgList.size());//Select the last one
                inputText.setText("");
                Toast.makeText(this,"Message sent",Toast.LENGTH_LONG).show();
            }
            else{Toast.makeText(ChatActivity.this, "Empty input", Toast.LENGTH_SHORT).show();}
        });

        //获取URL
        String URL1="http://3.135.240.182/api/a3/get_messages?chatroom_id="+ChatroomId+"&page=1";
        Log.e("URL1",URL1);
        FetchMessageTask task = new FetchMessageTask(msgList);
        task.execute(URL1);

        getTotal_page();
        msgListView.setOnScrollListener(this);


    }


    private final Emitter.Listener onDisConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            R.string.disconnect, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private final Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(() -> {
                Log.e(TAG, "Error connecting");
                Toast.makeText(getApplicationContext(),
                        R.string.error_connect, Toast.LENGTH_SHORT).show();
            });
        }
    };



    private final Emitter.Listener onConnectSuccess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(),
                    R.string.connect, Toast.LENGTH_SHORT).show());
        }
    };


    private final Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call (Object...args) {
            try{
                    Log.e("","new mewwage start notification");
                    JSONObject data = new JSONObject((String) args[0]);
                    final String text = data.getString("message");
                    runOnUiThread(() -> {
                        String CHANNEL_ID ="Notification";
                        int notificationId = 1;
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ChatActivity.this, CHANNEL_ID)
                                .setSmallIcon(R.mipmap.ic_launcher_round)
                                .setContentTitle("New Chatroom Notification")
                                .setContentText(text)
                                .setWhen(System.currentTimeMillis())
                                .setAutoCancel(true)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                       NotificationManagerCompat notificationManager = NotificationManagerCompat.from(ChatActivity.this);
                        notificationManager.notify(notificationId, builder.build()) ;
                    }) ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    } ;


    @Override
    protected void onDestroy() {
        JSONObject jsondata = new JSONObject();
        try {
            jsondata.put("chatroom_id",ChatroomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("leave",jsondata);
        socket.disconnect();
        socket.off();
        super.onDestroy();
    }


    private void getTotal_page(){
        new Thread(() -> {
            String result;
            result = HttpUrl.fetchData("http://3.135.240.182/api/a3/get_messages?chatroom_id="+ChatroomId+"&page=1");
            try {
                JSONObject json = new JSONObject(result);//{"status","data"}
                Log.e("jsonConvert successful", "消息从String->json转义成功");
                String status = json.getString("status");
                JSONObject data = json.getJSONObject("data"); //{"current_page","messages","total_pages"}

                total_page = data.getInt("total_pages");
                Log.e("total", String.valueOf(total_page));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    // 监听滑动状态的变化
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    // 监听屏幕滚动的item的数量
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            Log.e("******************", "********");
//            Log.e("visibleItemCount", String.valueOf(visibleItemCount));
//            Log.e("firstVisibleItem", String.valueOf(firstVisibleItem));
//            Log.e("totalItemCount", String.valueOf(totalItemCount));
//            Log.e("******************", "********");
//            Log.e("-----------", "-----------");
//
//
//
//        Log.e("******************", "********");
//        Log.e("%%%%%%total%%%%%%", String.valueOf(total_page));
        if(firstVisibleItem == 0) {
                Log.e("现在页码： ", String.valueOf(current_page));
//                int total_page=137;
                if (current_page <= total_page) {
                        current_page++;
                        Log.e("现在页码： ", String.valueOf(current_page));
//                        Toast.makeText(this,"Loading Page: " +(current_page-1)+"\r\n"+"Total Page: "+total_page+"\r\n"+"Please move slowly",Toast.LENGTH_SHORT).show();
                        String URL1 = "http://3.135.240.182/api/a3/get_messages?chatroom_id=" + ChatroomId + "&page=" + current_page;
                        Log.e("URL1", URL1);
                        FetchMessageHistoryTask task = new FetchMessageHistoryTask(msgList);
                        task.execute(URL1);
                } else {
                        Toast.makeText(this,"Pages all loaded"+"\r\n"+"Total "+total_page+" pages",Toast.LENGTH_SHORT).show();
                    }
        }

        }
}


