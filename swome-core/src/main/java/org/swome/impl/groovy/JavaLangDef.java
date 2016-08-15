package org.swome.impl.groovy;

import java.util.HashMap;
import java.util.Map;

public class JavaLangDef {

	public static Map<String, String> getImports() {
		Map<String, String> _map = new HashMap<String, String>();
		
		_map.put("String", "java.lang.String");
		_map.put("Exception", "java.lang.Exception");
		_map.put("Object", "java.lang.Object");
		
		return _map;
	}

}
