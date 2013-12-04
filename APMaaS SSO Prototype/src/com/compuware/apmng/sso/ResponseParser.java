package com.compuware.apmng.sso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.apmng.util.StringUtils;

/**
 * The OpenAM REST APIs return data in an unconventional format; this class contains a utility method
 * which converts the output string returned by an OpenAM REST API into a JSONObject with 
 * appropriately typed attributes.
 * 
 * @author Carlos Devoto
 *
 */
public class ResponseParser {

	public static JSONObject parse (String s) throws IOException, JSONException {
		// The OpenAM REST APIs return data in an unorthodox format; this method
		// converts the output string into a JSONObject with appropriately typed attributes
		JSONObject responseObject = new JSONObject();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new StringReader(s));
			for (String line = in.readLine(); line != null;) {
				NameValuePair pair = parseResponseLine(line);
				if ("userdetails.attribute.name".equals(pair.getName())) {
					List<Object> values = new ArrayList<Object>();
					String attributeName = pair.getValue();
					line = in.readLine();
					while (line != null) {
						pair = parseResponseLine(line);
						if ("userdetails.attribute.value".equals(pair.getName())) {
							values.add(StringUtils.parseValue(pair.getValue()));
						} else {
							break;
						}
						line = in.readLine();	
					}
					int size = values.size();
					if (size > 0) {
						if (size == 1) {
							Object value = values.get(0);
							putValue(responseObject, attributeName, value); 
						} else {
							JSONArray array = new JSONArray();
							for (Object value : values) {
								putValue(array, value);
							}
							responseObject.put(attributeName, array);
						}
					}
				} else {
					Object value = StringUtils.parseValue(pair.getValue());
					putValue(responseObject, pair.getName(), value);
					line = in.readLine();
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return responseObject;
	}

	private static void putValue(JSONArray array,
			Object value) throws JSONException {
		if (value instanceof Boolean) {
		    array.put(((Boolean) value).booleanValue());
		} else if (value instanceof Long) {
		    array.put(((Long) value).longValue());
		} else if (value instanceof Double) {
		    array.put(((Double) value).doubleValue());
		} else {
			array.put(value);
		}
	}
	
	private static void putValue(JSONObject object,
			String attributeName, Object value) throws JSONException {
		if (value instanceof Boolean) {
		    object.put(attributeName, ((Boolean) value).booleanValue());
		} else if (value instanceof Long) {
		    object.put(attributeName, ((Long) value).longValue());
		} else if (value instanceof Double) {
		    object.put(attributeName, ((Double) value).doubleValue());
		} else {
			object.put(attributeName, value);
		}
	}

	private static NameValuePair parseResponseLine(String line) {
		int pos = line.indexOf('=');
		String name = line.substring(0, pos);
		String value = line.substring(pos + 1);
		NameValuePair pair = new BasicNameValuePair(name, value);
		return pair;
	}

	
	private ResponseParser () {}
}

