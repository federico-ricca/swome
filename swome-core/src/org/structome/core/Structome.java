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
package org.structome.core;

import java.util.HashMap;
import java.util.Map;

public class Structome {
	private Map<String, Graph<? extends Artefact, ? extends Relation>> graphs = new HashMap<String, Graph<? extends Artefact, ? extends Relation>>();

	public void addGraph(String _id, Graph<? extends Artefact, ? extends Relation> _graph) {
		graphs.put(_id, _graph);
	}

	public Graph<? extends Artefact, ? extends Relation> getGraph(String _id) {
		return graphs.get(_id);
	}

}
