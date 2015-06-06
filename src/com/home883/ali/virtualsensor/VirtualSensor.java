package com.home883.ali.virtualsensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.time.Clock;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.net.*;

import org.json.simple.JSONObject;

public class VirtualSensor {

	public static void main(String[] args) {
		VirtualSensor vs = new VirtualSensor() ;

	}
	
	VirtualSensor()
	{
		float temp = 20 ;
		float humidity = 20 ;
		long previousTime = 0 ;
		long currentTime = 0 ;
		
		while ( true )
		{
			currentTime = System.currentTimeMillis() ;
			if ( currentTime > previousTime + 60000 )
			{
				JSONObject json = new JSONObject() ;
				
				
				if ( 0.5 + 1> Math.random() )
				{
					GregorianCalendar gc = new GregorianCalendar() ;
					System.out.println( gc.get(Calendar.AM_PM));
					
					if ( gc.get(Calendar.AM_PM) == 0 )
					{
						temp += 0.1 ;
						humidity += 0.1 ;
					}
					else
					{
						temp -= 0.1 ;
						humidity -= 0.1 ;
					}
					
				}
				
				json.put("id", "virtual-sensor");
				json.put("temp", temp) ;
				json.put("humidity", humidity) ;
				
				System.out.println(currentTime);
				System.out.println(json.toString());
				
				previousTime = currentTime ;
			}
		}
		
	}
	
	private void sendJson( JSONObject json )
	{
		URL url ;
		HttpURLConnection conn = null ;
		
		try {
			url = new URL ("http://localhost/post.php") ;
			conn = (HttpURLConnection)url.openConnection() ;
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();
			
			OutputStreamWriter wr;
			wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();
			
			StringBuilder sb = new StringBuilder() ;
			int httpResult = conn.getResponseCode() ;
			
			if ( httpResult == HttpURLConnection.HTTP_OK )
			{
				BufferedReader br = new BufferedReader( new InputStreamReader( conn.getInputStream(), "utf-8"));
				String line = null ;
				while ((line = br.readLine()) != null)
				{
					sb.append(line + "\n") ;
				}
				
				br.close();
				
				System.out.println("Response: " + sb.toString());
			}
			else
				System.out.println(conn.getResponseMessage());
		} catch (Exception e)
		{
			e.getStackTrace();
		}
				
		
	}

}
