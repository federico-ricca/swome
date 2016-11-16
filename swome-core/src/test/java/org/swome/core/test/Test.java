package org.swome.core.test;

import org.swome.impl.java.groovyparser.GroovyClassArtefactFactory;
import org.swome.impl.java.groovyparser.GroovyCodeVisitor;

public class Test {
	public void run() {
		Object a = new GroovyClassArtefactFactory(new GroovyCodeVisitor());
	}
}
