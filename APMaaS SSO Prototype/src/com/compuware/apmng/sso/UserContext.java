package com.compuware.apmng.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * An object which stores the user attributes retrieved from OpenAM.
 * 
 * @author Carlos Devoto
 *
 */
public class UserContext {
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	public static Builder builder () {
		return new Builder();
	}
	
	public static Builder builder (JSONObject json) {
		return new Builder(json);
	}

	private UserContext (Builder builder) {
		this.attributes.putAll(builder.attributes);
	}
	
	public Set<String> getAttributeNames () {
		return this.attributes.keySet();
	}
	
	public Set<Map.Entry<String, Object>> getAttributes() {
		return this.attributes.entrySet();
	}
	
	public Object getAttribute (String name) {
		return this.attributes.get(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name, Class<T> clazz) {
		return (T) this.attributes.get(name);
	}
	
	public boolean hasAttribute(String name) {
		return this.attributes.containsKey(name);
	}
	
	public boolean isEmpty() {
		return this.attributes.isEmpty();
	}
	
	public int size () {
		return this.attributes.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserContext other = (UserContext) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserContext: " + attributes;
	}



	public static class Builder {
		private Map<String, Object> attributes = new HashMap<String, Object>();
		
		private Builder () {}
		
		private Builder (JSONObject json) {
			for (@SuppressWarnings("unchecked")
			Iterator<String> it = json.keys(); it.hasNext();) {
				String key = it.next();
				try {
					Object value = json.get(key);
					if (value instanceof JSONArray) {
						JSONArray array = (JSONArray) value;
						List<Object> list = new ArrayList<Object>();
						value = list;
						int length = array.length();
						for (int i = 0; i < length; i++) {
							list.add(array.get(i));
						}
					}
					attributes.put(key, value);
				} catch (JSONException e) {
					// This should never happen!
				}
				
			}
		}
		
		public Builder setAttribute(String name, Object value) {
			this.attributes.put(name, value);
			return this;
		}
		
		public UserContext build () {
			return new UserContext(this);
		}
		
	}
}
