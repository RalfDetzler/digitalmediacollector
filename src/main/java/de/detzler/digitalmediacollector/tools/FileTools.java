/*
 * Copyright (C) 2011 A Ralf Detzler Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.detzler.digitalmediacollector.tools;

import java.io.File;

/**
 * Provides some missing Tools of the <code>File</class>.<br />
 * 
 */
public class FileTools {

	/**
	 * Returns the file extension.<br />
	 * 
	 * @param fullFilename
	 * @return the extension or an empty string, if there is no extension
	 */
	public static String extension(String fullFilename) {

		String start = fullFilename.substring(fullFilename
				.lastIndexOf(File.separator) + 1);

		String ext = (start.lastIndexOf(".") == -1) ? "" : start.substring(
				start.lastIndexOf(".") + 1, start.length()).toLowerCase();

		return ext;
	}

}
