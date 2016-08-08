package org.swome.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.swome.core.ArtefactProcessor;
import org.swome.core.FileArtefact;
import org.swome.core.Structome;
import org.swome.core.SystemRepresentation;
import org.swome.util.FileCollector;

public class SystemProcessor {
	class ProcessingConfiguration {
		public String filenameRegex;
		public ArtefactProcessor<FileArtefact> processor;
	}

	private Map<String, ProcessingConfiguration> configurations = new HashMap<String, ProcessingConfiguration>();

	public Structome processFolder(String _path, SystemRepresentation _rep,
			Structome _structome) {
		File _folder = new File(_path);
		FileCollector _fileCollector = new FileCollector(_folder);

		for (String _confKey : configurations.keySet()) {
			ProcessingConfiguration _configuration = configurations
					.get(_confKey);

			Collection<File> _files = _fileCollector
					.collect(_configuration.filenameRegex);

			ArrayList<FileArtefact> _fileArtefacts = new ArrayList<FileArtefact>();

			for (File _f : _files) {
				_fileArtefacts.add(new FileArtefact(_f));
			}

			_configuration.processor.process(_fileArtefacts,
					_structome.getGraph());
		}

		return _structome;
	}

	public void registerProcesor(String _processorId, String _filenameRegex,
			ArtefactProcessor<FileArtefact> _processor) {
		ProcessingConfiguration _configuration = new ProcessingConfiguration();

		_configuration.filenameRegex = _filenameRegex;
		_configuration.processor = _processor;

		configurations.put(_processorId, _configuration);
	}
}
