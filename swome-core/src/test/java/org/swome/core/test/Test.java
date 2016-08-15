package org.swome.core.test;

import org.swome.impl.groovy.GroovyClassArtefactFactory;
import org.swome.impl.groovy.GroovyCodeVisitor;

public class Test {
	public void run() {
		Object a = new GroovyClassArtefactFactory(new GroovyCodeVisitor());
	}
}
