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
package org.swome.impl.groovy;

import java.io.File;

import org.swome.core.Artefact;

public class GroovyFileArtefact implements Artefact {
	private File sourceFile;

	public GroovyFileArtefact(final File _f) {
		sourceFile = _f;
	}

	@Override
	public String getId() {
		return sourceFile.getAbsolutePath();
	}

	public File getSourceFile() {
		return sourceFile;
	}

}
