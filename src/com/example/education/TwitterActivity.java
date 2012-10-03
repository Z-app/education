package com.example.education;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
public class TwitterActivity extends ListActivity {
	
	/*
	 * Testing some stuff with Git. /Linus
	 * 
	 * 
	 */
	private ArrayAdapter<String> myArrayAdapter;
    /** Called when the activity is first created. */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        myArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);
        setListAdapter(myArrayAdapter);
        new DownloadTwitterFeed().execute();
    }
    private class DownloadTwitterFeed extends AsyncTask {

		@Override
		protected Object doInBackground(Object... params) {
	    	StringBuilder builder = new StringBuilder();
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
	            	  builder.append(line);
	            	 
	              }
	              content.close();
	            } else {
	              //Log.e(ParseJSON.class.toString(), "Failed to download file");
	            }
	          } catch (ClientProtocolException e) {
	            e.printStackTrace();
	          } catch (IOException e) {
	            e.printStackTrace();
	          }

			try {
				JSONArray jsonArray = new JSONArray(builder.toString());

		        for (int i = 0; i<jsonArray.length(); i++){
		        	listItems.add(jsonArray.getJSONObject(i).getString("text"));
		        }
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return listItems;
		}
		
		@Override
		protected void onPostExecute(Object result){
			ArrayList<String> tweets = (ArrayList<String>) result;
			for(String tweet : tweets){
				myArrayAdapter.add(tweet);
			}
			
		}
		
		
		
    }
    
    
    
    //Det här skulle behöva göras i en separat tråd, antagligen som en subaktivitet.
    //se ndroid.os.NetworkOnMainThreadException
    /**
     * DEPRICATED
     * Was used to fetch a ArrayList.
     * @return
     */
/*    public ArrayList<String> fetchTwitterPublicTimeline(){
    	
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
    }*/
}