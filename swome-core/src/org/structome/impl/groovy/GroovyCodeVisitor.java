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

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.control.SourceUnit;

public class GroovyCodeVisitor extends ClassCodeVisitorSupport {
	private SourceUnit sourceUnit;
	private GroovyClassArtefact groovyClassArtefact;

	public final void begin(SourceUnit _sourceUnit) {
		sourceUnit = _sourceUnit;
		groovyClassArtefact = new GroovyClassArtefact();
	}

	@Override
	protected final SourceUnit getSourceUnit() {
		return sourceUnit;
	}

	public final GroovyClassArtefact getGroovyClassArtefact() {
		return groovyClassArtefact;
	}

}
