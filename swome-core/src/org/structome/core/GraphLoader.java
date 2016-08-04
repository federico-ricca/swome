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

import java.io.IOException;

import org.structome.core.Graph;

public interface GraphLoader<N extends Artefact, E extends Relation, S> {

	public void loadInto(Graph<N, E> graph, ArtefactFactory<N, S> _factory,
			final RelationFactory<E, N> _relationFactory) throws IOException;

}
