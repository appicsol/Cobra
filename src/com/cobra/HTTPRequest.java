package com.cobra;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EventListener;

import android.os.AsyncTask;

public class HTTPRequest extends AsyncTask<String, Void, String> {
	private HTTPRequestListener listener;

	public static interface HTTPRequestListener extends EventListener {
    	public void onDataRecieved(String data);
    }

	public HTTPRequest(HTTPRequestListener listener) {
		this.listener = listener;
	}
	
	protected String doInBackground(String... urls) {
		String webpage = "";
		
		URL url;
		try {
			url = new URL(urls[0]);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		}
		
		HttpURLConnection urlConnection;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
			return "";
		}
		
		try {
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			webpage = readStream(in);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}
		
		return webpage;
    }

    protected void onPostExecute(String data) {
    	listener.onDataRecieved(data);
    }
    
    private static String readStream(InputStream is) {
    	try {
    		ByteArrayOutputStream bo = new ByteArrayOutputStream();
    		int i = is.read();
    		while (i != -1) {
    			bo.write(i);
    			i = is.read();
    		}
    		return bo.toString();
    	} catch (IOException e) {
    		return "";
    	}
    }
}


