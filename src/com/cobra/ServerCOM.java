package com.cobra;

import com.cobra.HTTPRequest.HTTPRequestListener;

public class ServerCOM {
	
	public static void requestPage(String url, HTTPRequestListener callback) {
		new HTTPRequest(callback).execute(url);
	}
}
