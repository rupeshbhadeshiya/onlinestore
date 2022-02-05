package com.learning.ddd.onlinestore.commons.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpUtil {

	//Jackson for JSON serialization
	private static final ObjectMapper objectMapper = new ObjectMapper(); 
	
	
	// HTTP POST
	public static Object post(String requestURI, Object requestBody, 
			Class<?> responseClass) throws IOException {
		
		return connect(requestURI, "POST", requestBody, responseClass);
	}
	
	// HTTP GET
	public static Object get(String requestURI, Class<?> responseClass) throws IOException {
		
		return connect(requestURI, "GET", null, responseClass);
	}
	
	// Search - HTTP POST with requestBody
	public static Object search(String requestURI, Object requestBody, Class<?> responseClass) throws IOException {
	
		return connect(requestURI, "POST", requestBody, responseClass);
	}
	
	// HTTP PUT
	public static Object put(String requestURI, Object requestBody, 
			Class<?> responseClass) throws IOException {
		
		return connect(requestURI, "PUT", requestBody, responseClass);
		
	}
	
	// HTTP DELETE
	public static void delete(String requestURI) throws IOException {
		
		connect(requestURI, "DELETE", null, null);
		
	}
	
	// HTTP DELETE with requestBody
	public static void delete(String requestURI, Object requestBody) throws IOException {
	
		connect(requestURI, "DELETE", requestBody, null);
	}
	
	// Generic HTTP connect function
	public static Object connect(String uri, String httpMethod, Object requestBody, 
			Class<?> responseClass) throws IOException {
		
		HttpURLConnection httpConnection = null;
		
		try {
			
			URL url = new URL(uri);
			
			System.out.println("\nRequest for HTTP " + httpMethod + " to " + url);
			
			// open connection to requested URL
			httpConnection = (HttpURLConnection) url.openConnection();
			System.out.println("HTTP URL Connection opened");

			httpConnection.setRequestMethod(httpMethod);
			
			httpConnection.setDoInput(true);		// to receive response
			
			if (httpMethod.equals("POST") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
				
				httpConnection.setDoOutput(true);	// to send request body
				
				httpConnection.setRequestProperty("Content-Type", "application/json");
				httpConnection.setRequestProperty("Content-Language", "en-US");
				
				String requestJson = objectMapper.writeValueAsString(requestBody);
				httpConnection.setRequestProperty("Content-Length", "" + requestJson.getBytes().length);
				
				try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpConnection.getOutputStream()))) {
					bw.write(requestJson);
					//bw.flush();
				} // try block takes care of closing the stream/writer
				System.out.println("request = " + requestJson);
				System.out.println("Sent " + requestJson.length() + " characters of request data");
				
//				System.out.println("Writing to HTTP URL Connection OutputStream");
//				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpConnection.getOutputStream()));
//				bw.write(requestJson);
//				bw.flush();
//				httpConnection.getOutputStream().flush();
//				System.out.println("Sent " + requestJson.length() + " characters of request data");

//				httpConnection.getOutputStream().close();
//				System.out.println("HTTP URL Connection OutputStream is now closed");
			}
			
			// connect to requested URL
			
			httpConnection.connect();
			System.out.println("connected");
			
			// read response
			
			System.out.println("response code = " + httpConnection.getResponseCode());
			
			InputStream responseStream = null;
			
			if ( (httpConnection.getResponseCode() == 201) ||
				 (httpConnection.getResponseCode() == 200) ) {
				
				responseStream = httpConnection.getInputStream();
				
			} else if (httpConnection.getResponseCode() >= 400) {
				
				responseStream = httpConnection.getErrorStream();
				
			}
			
			if (responseStream != null) {
				
				StringBuffer responseJson = new StringBuffer();
				try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream))) {
					for (String line; (line = br.readLine()) != null; ) {
						responseJson.append(line);
					}
				} // try-block will take care of closing the stream/writer
				
				if (responseJson.length() > 0) {
				
					System.out.println("Read " + responseJson.length() + " characters of response data");
					System.out.println("response = " + responseJson);
					System.out.println("responseClass = " + responseClass);
					
					return objectMapper.readValue(responseJson.toString(), responseClass);
				}
			
			}
			
			return null;
			
//			BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
//			System.out.println("Reading from HTTP URL Connection InputStream");
//			
//			StringBuffer responseJson = new StringBuffer();
//			for (String text; (text = br.readLine()) != null; ) {
//				responseJson.append(text);
//			}
//			
//			br.close();
//			System.out.println("HTTP URL Connection InputStream is now closed");
//			
//			return objectMapper.readValue(responseJson.toString(), responseClass);
			
			
		} catch (IOException e) {
			
			System.err.println("------------- error during http communication! ------------");
			e.printStackTrace();
			throw e;
			
		} finally {
			
			if (httpConnection != null) {
				
				httpConnection.disconnect();
				System.out.println("HTTP URL Connection is now closed");
				
//				try {
//
					// error! not allowed accessing OutputStream, 
					// once started reading from InputStream
//					if (httpConnection.getOutputStream() != null) {
//						httpConnection.getOutputStream().close();
//						System.out.println("HTTP URL Connection OutputStream is now closed");
//					}
					
//					if (httpConnection.getInputStream() != null) {
//						httpConnection.getInputStream().close();
//						System.out.println("HTTP URL Connection InputStream is now closed");
//					}
//
//				} catch (IOException ioEx) {
//					System.err.println("--> error while closing HTTP URL Connection InputStream");
//					ioEx.printStackTrace();
//					throw ioEx;
//					
//				} finally {
//				
//					httpConnection.disconnect();
//					System.out.println("HTTP URL Connection is now closed");
//				}
			}
		}
	}


}
