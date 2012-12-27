package geolog.util;

import java.util.HashMap;

/**
 * Classe per il passaggio di parametri tra attività android. Un parametro che
 * vuole essere passato tra attività, viene salvato in una hash map, e
 * recuperato dalla nuova attività
 * 
 * @author Angelo Moroni
 * 
 */
public class ParametersBridge {

	private static ParametersBridge instance = null;
	/**
	 * @uml.property  name="map"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="java.lang.Object" qualifier="key:java.lang.String java.lang.Object"
	 */
	private HashMap<String, Object> map;

	public static ParametersBridge getInstance() {
		if (instance == null)
			instance = new ParametersBridge();
		return instance;
	}

	private ParametersBridge() {
		map = new HashMap<String, Object>();
	}

	public void addParameter(String key, Object value) {
		map.put(key, value);
	}

	public Object getParameter(String key) {
		return map.get(key);
	}

	public void removeParameter(String key) {
		map.remove(key);
	}
}
