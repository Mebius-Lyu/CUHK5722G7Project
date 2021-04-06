package hk.edu.cuhk.ie.iems5722.a4_1155152375.FetchTask;

import android.os.AsyncTask;

import java.io.IOException;

import static hk.edu.cuhk.ie.iems5722.a4_1155152375.HttpUrl.postData;

public class PostMessageTask extends AsyncTask<String, Void, Void> {

    private final String json_string;

    public PostMessageTask(String json_string){this.json_string=json_string;}

    @Override
    protected Void doInBackground(String... strings) {
        try {
            postData(strings[0],json_string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

    }
}
