package pl.marcinwroblewski.e_miasto;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marcin Wróblewski on 14.10.2016.
 */

public class Requests {

	private String login, password;
	private static final String API_URL = "http://188.137.38.116:5000/api";
	
	public Requests(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public Requests() {}
	
	
	
	
	public static String createUser(String email, String password) {
		 HttpURLConnection connection = null;

		 String data = "email=" + email + "&password=" + password;
		 System.out.println(data);
		 
		 
		  try {
		    //Create connection
		    URL url = new URL(API_URL + "/user/new");
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		            "application/x-www-form-urlencoded");
		    connection.setRequestProperty("Content-Length", 
		            Integer.toString(data.getBytes().length));
		    
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
			  connection.setDoInput(true);

		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(data);
		    wr.close();

              Log.d("Response", connection.getResponseCode() + " : " + connection.getResponseMessage());

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		  } catch (Exception e) {
		    e.printStackTrace();
		    return null;
		  } finally {
		    if (connection != null) {
		      connection.disconnect();
		    }
		  }
	}

    public String getBasicAuth() {
        return "Basic " + Base64.encodeToString((login + ":" + password).getBytes(), Base64.NO_WRAP);
    }
	
	public String addInterests(int[] ids) {
		
		HttpURLConnection connection = null;
		
		String idsString = "[";
		
		for(int id : ids) {
			idsString += String.valueOf(id) + ",";
		}
		
		idsString = idsString.substring(0, idsString.length() - 1) + "]";
		String data = "interests=" + idsString;
		System.out.println(data);
		 
		 
	  try {
		    //Create connection
		    URL url = new URL(API_URL + "/user/current/interest/update");
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		    connection.setRequestProperty("Content-Type", 
		            "application/x-www-form-urlencoded");
		    connection.setRequestProperty("Content-Length", 
		            Integer.toString(data.getBytes().length));
		    
		    
		    connection.setUseCaches(false);
		    connection.setDoOutput(true);
	
		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(data);
		    wr.close();
	
		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
	  } catch (Exception e) {
		  e.printStackTrace();
		  return null;
	  } finally {
	    if (connection != null) {
	      connection.disconnect();
	    }
	  }
		
	}
	
	public String addComplain(String title, String description, float lon, float lat, File... images) throws IOException {
		
		MultipartUtility multipartUtil = new MultipartUtility(
				API_URL + "/complain/new", "UTF-8", getBasicAuth());

		multipartUtil.addFormField("title", title);
		multipartUtil.addFormField("description", description);
		multipartUtil.addFormField("longitude", String.valueOf(lon));
		multipartUtil.addFormField("latitude", String.valueOf(lat));
		for(int i = 0; i < images.length; i++) {
			multipartUtil.addFilePart("image" + i, images[i]);
			Log.d("Multipart util", "Adding to form " + images[i].getAbsolutePath());
		}
		
		return (multipartUtil.finish()).toString();
	}
	
	public String getAllParties() {
		
		HttpURLConnection connection = null;
		 
		try {
		    //Create connection
		    URL url = new URL(API_URL + "/party");
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		   
		    connection.setUseCaches(false);
		   
		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		  	return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
	    }
	  }
	}

    public String getCurrentUser() throws IOException {

        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(API_URL + "/user/current");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", getBasicAuth());

            connection.setUseCaches(false);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();

			switch (connection.getResponseCode()) {
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					return "Błędne dane";
				default:
					return "Nie udało się nawiązać połączenia z serwerem. Proszę spróbować ponownie później. (" + connection.getResponseCode() + ")";
			}
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

	public String getPersonalizedParties() throws IOException {
		
		HttpURLConnection connection = null;
		 
		try {
		    //Create connection
		    URL url = new URL(API_URL + "/party/personalized");
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		   
		    connection.setUseCaches(false);
		   
		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		  	return String.valueOf(connection.getResponseCode());
		} finally {
			if (connection != null) {
				connection.disconnect();
	    }
	  }
	}
	
	public String getParty(String id) {
		
		HttpURLConnection connection = null;
		 
		try {
		    //Create connection
		    URL url = new URL(API_URL + "/party/" + id);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		   
		    connection.setUseCaches(false);
		   
		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		  	return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
	    }
	  }
	}
	
	
	public String getAllInterest() {
		
		HttpURLConnection connection = null;
		 
		try {
		    //Create connection
		    URL url = new URL(API_URL + "/interest");
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		   
		    connection.setUseCaches(false);
		   
		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		  	return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
	    }
	  }
	}
	
	public String getAllComplains() throws IOException {
		
		HttpURLConnection connection = null;
		 
		try {
		    //Create connection
		    URL url = new URL(API_URL + "/complain");
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		   
		    connection.setUseCaches(false);

            Log.d("All complains", "Response: " + connection.getResponseCode() + " : " + connection.getResponseMessage());

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		  	return String.valueOf(connection.getResponseCode());
		} finally {
			if (connection != null) {
				connection.disconnect();
	    }
	  }
	}
	
	public String getComplain(String id) {
		
		HttpURLConnection connection = null;
		 
		try {
		    //Create connection
		    URL url = new URL(API_URL + "/complain/" + id);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("GET");
		    connection.setRequestProperty("Authorization", getBasicAuth());
		   
		    connection.setUseCaches(false);
		   
		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
		    rd.close();
		    return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
		  	return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
	    }
	  }
	}
}
