package com.example.education;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
public class TwitterActivity extends ListActivity {
    /** Called when the activity is first created. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        
        setListAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, this.fetchTwitterPublicTimeline()));
    }
    
    //Det här skulle behöva göras i en separat tråd, antagligen som en subaktivitet.
    //se ndroid.os.NetworkOnMainThreadException
    public ArrayList<String> fetchTwitterPublicTimeline()
    {
    	
    	HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://twitter.com/statuses/user_timeline/vogella.json");
        
        ArrayList<String> listItems = new ArrayList<String>();
        try {
            HttpResponse response = client.execute(httpGet);  //runtime error here, permission errors?
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
              HttpEntity entity = response.getEntity();
              InputStream content = entity.getContent();
              BufferedReader reader = new BufferedReader(new InputStreamReader(content));
              String line;
              while ((line = reader.readLine()) != null) {
            	  listItems.add(line);
              }
            } else {
              //Log.e(ParseJSON.class.toString(), "Failed to download file");
            }
          } catch (ClientProtocolException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        return listItems;
    }
}