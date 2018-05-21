package requests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.text.html.HTMLEditorKit.Parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public abstract class Request {
	
	private static final String USER_AGENT = "Mozilla/5.0";
	private static final String BASE_IP = "https://minesweeper-multiplayer.herokuapp.com/";
	/**
	 * Performs an HTTP GET request
	 * 
	 * @param s_url URL as a String
	 * 
	 * @return body of the reqponse
	 * @throws Exception 
	 */
	
	public static String get(String s_url) throws Exception{
		URL url = new URL(s_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		conn.setRequestProperty("User-Agent", USER_AGENT);
		
		//int responseCode = conn.getResponseCode();
		//System.out.println("\nSending 'GET' request to URL : " + url.toString().split("\\?")[0]);
		//System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		StringBuffer resp = new StringBuffer();
		
		while((line = in.readLine()) != null){
			resp.append(line);
		}
		in.close();
		return resp.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static String post(String s_url, boolean[][][] bs) throws Exception{
		URL url = new URL(s_url);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("content-type", "application/json");
		conn.setDoOutput(true);
		OutputStream out = conn.getOutputStream();
		JSONObject sendObj = new JSONObject();
		JSONArray arr = new JSONArray();
		for(int i = 0; i < bs.length; i++){
			arr.add(new JSONArray());
			for(int j = 0; j < bs[0].length; j++){
				((JSONArray)arr.get(i)).add(new JSONArray());
				for(int n = 0; n < bs[0][0].length; n++){
					((JSONArray)((JSONArray)arr.get(i)).get(j)).add(bs[i][j][n]);
				}
			}
		}
		sendObj.put("map", arr);
		out.write(sendObj.toJSONString().getBytes());
		out.flush();
		out.close();
		int responseCode = conn.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			return (response.toString());
		} else {
			return ("POST request did not work");
		}
		
	}
	
	public static boolean sendMap(int code, String usernm, boolean[][][] bs) throws Exception {
		String retVal = post(BASE_IP + "/updateMap/" + code + "/" + usernm, bs);
		//System.out.println(retVal);
		return retVal == "Success";
	}
	
	public static boolean connect(int code, String name) throws Exception {
		String retVal = get(BASE_IP + "/register/" + code + "/" + name);
		//System.out.println(retVal);
		return retVal == "Success";
	}
	
	public static boolean died(int code, String name) throws Exception{
		return get(BASE_IP + "/died/" + code + "/" + name).equals("Success");
	}
	
	public static boolean close(int code, String name) throws Exception{
		String retVal = get(BASE_IP + "/unregister/" + code + "/" + name);
		//System.out.println(retVal);
		return retVal =="Success";
	}
	
	public static Status status(int code, String user) throws Exception{
		JSONParser parse = new JSONParser();
		JSONObject obj = (JSONObject) parse.parse(get(BASE_IP + "/status/" + code + "?user=" + user));
		return new Status(
				(String)obj.get("player"), 
				(boolean)obj.get("full"), 
				(boolean)obj.get("turn"),
				(boolean)obj.get("wait"), 
				(boolean)obj.get("dead"), 
				(boolean)obj.get("requestRestart"),
				(boolean)obj.get("restart"));
	}
	
	public static boolean[][][] getMap(int code, String user) throws Exception{
		String ret = get(BASE_IP + "/getMap/" + code + "/" + user);
		JSONParser parse = new JSONParser();
		JSONObject obj = (JSONObject) parse.parse(ret);
		JSONArray arr = (JSONArray)obj.get("map");

		if(arr == null){
			System.err.println("Map not found yet");
			return null;
		}
		
		boolean[][][] aout = new boolean[arr.size()][((JSONArray)arr.get(0)).size()][((JSONArray)((JSONArray)arr.get(0)).get(0)).size()];
		for(int i = 0; i < aout.length; i++){
			for(int j = 0; j < aout[0].length; j++){
				for(int n = 0; n < aout[0][0].length; n++){
					aout[i][j][n] = (boolean) (((JSONArray)((JSONArray)arr.get(i)).get(j)).get(n));
				}
			}
		}
		return aout;
	}
	
	public static int restart(int code, String user) throws Exception{
		String ret = get(BASE_IP + "/restart/" + code + "/" + user);
		return ret.equals("Success") ? 1: ret.equals("Restarting") ? 2: 0;
	}
}
