/*************************************************************************** 
   Copyright 2015 Federico Ricca

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 ***************************************************************************/
package org.structome.impl.groovy;

import groovy.lang.GroovyClassLoader;

import java.security.CodeSigner;
import java.security.CodeSource;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.Phases;
import org.codehaus.groovy.control.SourceUnit;
import org.structome.core.ArtefactFactory;

public class GroovyClassArtefactFactory implements ArtefactFactory<GroovyClassArtefact, GroovyFileArtefact> {
	private GroovyCodeVisitor groovyCodeVisitor;

	public GroovyClassArtefactFactory(GroovyCodeVisitor _codeVisitor) {
		groovyCodeVisitor = _codeVisitor;
	}

	@Override
	public GroovyClassArtefact createArtefact(GroovyFileArtefact _groovyFile) {
		CompilerConfiguration _configuration = new CompilerConfiguration();
		CodeSigner[] certs = null;
		CodeSource _security = new CodeSource(null, (CodeSigner[]) certs);
		GroovyClassLoader _loader = new GroovyClassLoader();
		GroovyClassLoader _transformLoader = new GroovyClassLoader();

		CompilationUnit _compilationUnit = new CompilationUnit(_configuration, _security, _loader,
				_transformLoader);
		_compilationUnit.addSource(_groovyFile.getSourceFile());

		_compilationUnit.addPhaseOperation(new CompilationUnit.PrimaryClassNodeOperation() {
			@Override
			public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode)
					throws CompilationFailedException {
				groovyCodeVisitor.begin(source);
				groovyCodeVisitor.visitClass(classNode);
			}
		}, Phases.CONVERSION);

		_compilationUnit.compile(Phases.CONVERSION);

		return groovyCodeVisitor.getGroovyClassArtefact();
	}

}
