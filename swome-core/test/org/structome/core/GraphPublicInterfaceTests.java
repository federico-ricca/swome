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

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.structome.impl.SimpleArtefact;
import org.structome.impl.SimpleGraph;
import org.structome.impl.SimpleRelation;
import org.structome.impl.SimpleRelationFactory;

public class GraphPublicInterfaceTests {

	/*
	 * This test is designed to drive the public interface of Graph, Artefact,
	 * Relation and GraphLoader.
	 * 
	 * Test a graph of the form:
	 * 
	 * A -> B A -> C B -> D D -> E
	 */
	@Test
	public void testLoadGraph() throws IOException {
		GraphLoader<SimpleArtefact, SimpleRelation, String> _graphLoader = new GraphLoader<SimpleArtefact, SimpleRelation, String>() {
			@Override
			public void loadInto(Graph<SimpleArtefact, SimpleRelation> graph,
					ArtefactFactory<SimpleArtefact, String> _factory,
					RelationFactory<SimpleRelation, SimpleArtefact> _relationFactory) throws IOException {
				SimpleArtefact _A = _factory.createArtefact("A");
				graph.addArtefact(_A);

				SimpleArtefact _B = _factory.createArtefact("B");
				graph.addArtefact(_B);

				SimpleArtefact _C = _factory.createArtefact("C");
				graph.addArtefact(_C);

				SimpleArtefact _D = _factory.createArtefact("D");
				graph.addArtefact(_D);

				SimpleArtefact _E = _factory.createArtefact("E");
				graph.addArtefact(_E);

				graph.addRelation(_relationFactory.createRelation(_A, _B), _A, _B);
				graph.addRelation(_relationFactory.createRelation(_A, _C), _A, _C);
				graph.addRelation(_relationFactory.createRelation(_B, _D), _B, _D);
				graph.addRelation(_relationFactory.createRelation(_D, _E), _D, _E);
			}
		};

		ArtefactFactory<SimpleArtefact, String> _factory = new ArtefactFactory<SimpleArtefact, String>() {

			@Override
			public SimpleArtefact createArtefact(String _artefactRepresentation) {
				return new SimpleArtefact(_artefactRepresentation);
			}
		};

		Graph<SimpleArtefact, SimpleRelation> graph = new SimpleGraph<SimpleArtefact, SimpleRelation>();

		_graphLoader.loadInto(graph, _factory, new SimpleRelationFactory());

		assertEquals(5, graph.artefacts().size());
		assertEquals(2, graph.getRelationsFor("A").size());
	}
}
