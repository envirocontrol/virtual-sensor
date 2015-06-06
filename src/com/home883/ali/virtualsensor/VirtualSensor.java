package com.home883.ali.virtualsensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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
				json.put("temperature", temp) ;
				json.put("humidity", humidity) ;
				
				sendJson( json );
				
				System.out.println(json.toString());
				
				previousTime = currentTime ;
			}
		}
		
	}
	
	private void sendJson( JSONObject json )
	{
		URL url ;
		HttpURLConnection conn = null ;
		BufferedReader br = null ;
		
		try {
			url = new URL ("http://envirocontrol/logger/post.php") ;
			conn = (HttpURLConnection)url.openConnection() ;
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();
			
			OutputStreamWriter output;
			output = new OutputStreamWriter(conn.getOutputStream());
			output.write(json.toString());
			output.flush();
			
			System.out.println(Integer.toString(HttpURLConnection.HTTP_OK) + " : " + Integer.toString(conn.getResponseCode()));
			StringBuilder sb = new StringBuilder() ;
			InputStream inputStream = conn.getInputStream() ;
			
			if ( inputStream != null )
			{
				br = new BufferedReader( new InputStreamReader( inputStream ));
				String line = null ;
				while ((line = br.readLine()) != null)
				{
					sb.append(line + "\n") ;
				}
				
				br.close();
				
				System.out.println("Response: " + sb.toString());
			}
			else
				System.out.println("No Response: " + conn.getResponseMessage());
		} catch (Exception e)
		{
			e.getStackTrace();
			System.out.println(e.toString());
		} finally{
            if (conn != null) {
                conn.disconnect();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    e.getStackTrace() ;
                    System.out.println(e.toString());
                }
            }
				
		}  
	}

}
