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
package org.swome.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

import org.swome.core.Artefact;
import org.swome.core.ArtefactStringRepresentationFactory;
import org.swome.core.Graph;
import org.swome.core.GraphWriter;
import org.swome.core.RelationArtefactPair;
import org.swome.core.RelationStringRepresentationFactory;

public class CSVGraphWriter implements GraphWriter {
	public static final String ITEM_SEPARATOR = ";";

	@Override
	public void write(Graph _graph, File _file,
			ArtefactStringRepresentationFactory _artefactRepresentationFactory,
			RelationStringRepresentationFactory _relationRepresentationFactory)
			throws IOException {
		PrintStream _printStream = new PrintStream(_file);

		Collection<Artefact> _artefacts = _graph.artefacts();

		for (Artefact _anArtefact : _artefacts) {
			final String _source = _artefactRepresentationFactory
					.createStringRepresentationFor(_anArtefact);

			if (_graph.hasRelations(_anArtefact.getId())) {
				for (RelationArtefactPair _relationDesc : _graph
						.getRelationsFor(_anArtefact.getId())) {
					String _target = _artefactRepresentationFactory
							.createStringRepresentationFor(_relationDesc
									.getArtefact());

					String _relationRepresentation = "";

					if (_relationRepresentationFactory != null) {
						_relationRepresentation = ITEM_SEPARATOR
								+ _relationRepresentationFactory
										.createStringRepresentationFor(_relationDesc
												.getRelation());
					}

					_printStream.println(_source + ITEM_SEPARATOR + _target
							+ _relationRepresentation);
				}
			}
		}

		_printStream.close();
	}

}
