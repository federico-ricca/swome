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
package org.swome.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFileReader {

	public interface LineVisitor {
		public void visit(final String _line, final int _lineNumber);
	}

	private File textFile;

	public TextFileReader(final String _filename) {
		textFile = new File(_filename);
	}

	public TextFileReader(File _file) {
		textFile = _file;
	}

	public void readWith(LineVisitor _visitor) throws IOException {
		BufferedReader _reader = new BufferedReader(new FileReader(textFile));

		String _line = _reader.readLine();

		try {
			int _lineNumber = 0;

			while (_line != null) {
				_visitor.visit(_line, _lineNumber);

				_line = _reader.readLine();
				_lineNumber++;
			}
		} finally {
			_reader.close();
		}
	}
}