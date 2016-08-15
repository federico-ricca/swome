package org.swome.core;

import java.util.HashMap;
import java.util.Map;

public class ArtefactRegistry<T extends Artefact> {
	private Map<String, T> index = new HashMap<String, T>();

	public T findById(String id) {
		return index.get(id);
	}

	public void put(String id, T _artefact) {
		index.put(id, _artefact);
	}

}
