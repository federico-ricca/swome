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
package org.structome.util;

public class ClassNameUtils {

	public final static String[] split(final String _fqcn) {
		final String[] _name = new String[2];

		int _iIndex = _fqcn.lastIndexOf(".");

		if (_iIndex == -1) {
			_name[0] = "";
			_name[1] = _fqcn;
		} else {
			_name[0] = _fqcn.substring(0, _iIndex);
			_name[1] = _fqcn.substring(_iIndex + 1, _fqcn.length());
		}

		return _name;
	}

	public static boolean containsPackage(String _className) {
		return _className.lastIndexOf('.') != -1;
	}

	public static boolean isClassName(String _varName) {
		return _varName.matches("[A-Z].*");
	}

}
