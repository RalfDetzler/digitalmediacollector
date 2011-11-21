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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.detzler.digitalmediacollector.MediaInfo;

/**
 * Calls the exiftool and extracts the required info.<br />
 * 
 * Requires, that the exiftool is in the path. Runs only under Windows.
 * 
 */
public class ExifToolCaller {

	public static final String EXIFTOOL_EXE = "exiftool.exe";

	public final static String FILE_MODIFICATION_DATE = "File Modification Date/Time :";
	public final static String DATE_TIME_ORIGINAL = "Date/Time Original";

	private MediaInfo mediaInfo = new MediaInfo();

	public ExifToolCaller(String filename) throws Exception {
		this.mediaInfo.setFilename(filename);
		this.mediaInfo.setMd5(new Checksum(new File(filename)).getChecksum());
	}

	public ExifToolCaller(String filename, boolean calcMd5) throws Exception {
		this.mediaInfo.setFilename(filename);
		if (calcMd5) {
			this.mediaInfo.setMd5(new Checksum(new File(filename))
					.getChecksum());
		}
	}

	/**
	 * Reads the exif data with the exiftool.<br />
	 * Stores the exif data in <code>mediaInfo</code>.
	 * 
	 * @return the output of the exiftool command, an empty list, when nothing
	 *         was found.
	 */
	public Map<String, String> queryExifInfo() {

		Map<String, String> output = new HashMap<String, String>(90);
		String command = EXIFTOOL_EXE + "  \"" + mediaInfo.getFilename() + "\"";
		BufferedReader input = null;
		try {

			Process p = Runtime.getRuntime().exec(command);

			input = new BufferedReader(
					new InputStreamReader(p.getInputStream()));

			String line;
			while ((line = input.readLine()) != null) {

				String name = line.substring(0, 32).trim();
				String value = line.substring(33).trim();

				if (output.containsKey(name)) {
					String oldValue = output.get(name);
					System.out.println("Duplicate Key in Exif: " + name + "="
							+ oldValue + " ----> New value = " + value);
				}
				output.put(name, value);

				// File Modification Date/Time : 2011:10:11 12:45:42+02:00
				// Date/Time Original : 2011:10:11 12:45:37+00:00
				if (line.startsWith("Date/Time Original")
						|| line.startsWith("File Modification Date/Time")) {
					String[] parts = line.split(":");
					String[] secs = parts[5].split("\\+");
					mediaInfo.setCreationDate(parts[1].trim() + "-" + parts[2]
							+ "-" + parts[3] + "." + parts[4] + "." + secs[0]);
				}

				// Duration : 6.11 s
				if (line.startsWith("Duration")) {
					String[] parts = line.split(":");
					mediaInfo.setDuration(parts[1].trim());
				}
			}

		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (mediaInfo.getCreationDate().isEmpty()) {
			System.out.println(mediaInfo.getFilename()
					+ " has no creation date. Cannot process.");
			File f = new File(mediaInfo.getFilename());
			long lastmodified = f.lastModified();
			Date lm = new Date(lastmodified);
			String lasmod = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss")
					.format(lm);

			mediaInfo.setCreationDate(lasmod);
		}

		return output;
	}

	public MediaInfo getExifInfo() {
		return mediaInfo;
	}

	public void setExifInfo(MediaInfo mediaInfo) {
		this.mediaInfo = mediaInfo;
	}

	public String getFilename() {
		return mediaInfo.getFilename();
	}

}
