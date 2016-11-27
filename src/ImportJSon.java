import com.gs.collections.api.list.MutableList;
import com.gs.collections.impl.list.mutable.FastList;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
public class ImportJSon {
	public static void main(String[] args){
		Summoner jeffrey = new Summoner("jeffrey", "ShimmeringIce", "asdf");
		jeffrey.updateDivision();
	}
	public static JSONObject importFromFile(String yolo){
		JSONParser parser = new JSONParser();
		MutableList<Integer> hello = new FastList();
		try{
			File file = new File(yolo);
			Scanner sc = new Scanner(file);
			String s = "";
			while(sc.hasNextLine()){
				s += "\n" + sc.nextLine();
			}
			Object obj = parser.parse(s);
			return (JSONObject)obj;
		}
		catch(Exception e){
			e.printStackTrace();
			return new JSONObject();
		}
	}
	public static JSONObject makeRestCall(String URL, String argument){
		URL += argument;
		URL += "?api_key=RGAPI-5672c37b-e28c-4070-be0b-75966f6adfe9";
		URL url;
		HttpURLConnection urlConnection = null;
		InputStream in = null;
		String s = null;
		try {
			url = new URL(URL);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream());
			s = convertStreamToString(in);
			in.close();
		} catch (FileNotFoundException e1) {
			return null;
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		if(urlConnection != null){
			urlConnection.disconnect();
		}
		
		JSONParser parser = new JSONParser();
		Object obj;
		try{
			obj = parser.parse(s);
		}catch(Exception e){
			obj = new Object();
		}
		return(JSONObject)obj;
	}
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}
