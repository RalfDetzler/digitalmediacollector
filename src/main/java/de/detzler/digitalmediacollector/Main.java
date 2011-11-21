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
package de.detzler.digitalmediacollector;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.detzler.digitalmediacollector.tools.ExifToolCaller;
import de.detzler.digitalmediacollector.tools.FileTools;

public class Main extends DigitalMediaCollector {

	public final static Logger LOGGER = Logger.getLogger(Main.class.getName());

	private String target = "";

	public Main(String mediaKey, String startDir, String targetDir)
			throws IOException {
		
		super(mediaKey, startDir);
		setCalcMd5(false);
		this.target = targetDir;
	}

	/**
	 * Sets the log level for the console.<br>
	 */
	public static void setupLogger() {

		ConsoleHandler ch = new ConsoleHandler();
		ch.setLevel(Level.FINEST);
		Main.LOGGER.addHandler(ch);
		Main.LOGGER.setLevel(Level.FINEST);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		LOGGER.info("Start");

		if (args.length < 3) {
			System.err
					.println("usage: "
							+ Main.class.getName()
							+ "'media key' 'start directory, e.g. c:\\' 'target directory, e.g. e:\\Bilder_und_Videos\\");
			System.exit(-1);
		}

		Main mediaImporter = new Main(args[0], args[1], args[2]);
		mediaImporter.process();

		LOGGER.info("Ready.");
	}

	@Override
	protected void processFile(File file) throws Exception {

		ExifToolCaller exifToolCaller = new ExifToolCaller(
				file.getAbsolutePath(), this.isCalcMd5());

		 exifToolCaller.queryExifInfo();
		MediaInfo mediaInfo = exifToolCaller.getExifInfo();

		System.out.println(mediaInfo.md5 + "\t" + mediaInfo.creationDate + "\t"
				+ getMediaKey() + "\t" + getVolumeInfo() + "\t"
				+ mediaInfo.filename);

		String newFileName = calcNewFileName(mediaInfo);

		if (!checkFileExistsInTarget(newFileName)) {

			System.out.println("copy " + newFileName);
			// dest.setLastModified(src.lastModified());
			File f = new File(newFileName);

			// directory anlegen
			String parent = f.getParent();
			File dir = new File(parent);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			// LOGGER.info(executeSystemCommand("cmd /c mkdir " + parent)
			// .toString());

			String name = f.getCanonicalPath();

			String string = executeSystemCommand(
					"cmd /c copy \"" + mediaInfo.filename + "\" " + name)
					.toString();
			if (string.contains("The system cannot find the file specified")) {
				LOGGER.severe("CANNOT PROCESS  copy " + mediaInfo.filename + " "
						+ name);
			}

			// f.setLastModified(src.lastModified());

		} else {
			System.out.println(newFileName + " already exists");
		}
	}

	public List<String> executeSystemCommand(String command) {

		BufferedReader input = null;
		List<String> output = new ArrayList<String>();

		try {
			String line;

			LOGGER.finest(command);
			Process p = Runtime.getRuntime().exec(command);
			input = new BufferedReader(new InputStreamReader(
					p.getInputStream(), "UTF-8"));
			while ((line = input.readLine()) != null) {
				output.add(line);
			}

		}

		catch (IOException err) {
			LOGGER.severe(err.getLocalizedMessage());
			LOGGER.severe(Arrays.toString(err.getStackTrace()));

		}

		finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.severe(e.getLocalizedMessage());
					LOGGER.severe(Arrays.toString(e.getStackTrace()));
				}
			}
		}

		return output;
	}

	private String calcNewFileName(MediaInfo mediaInfo) throws IOException {

		String date = mediaInfo.creationDate;
		if (date.isEmpty()) {
			throw new NullPointerException("File " + mediaInfo
					+ " has no creation date. cannot process");
		}
		date = date.replaceAll(":", "-");
		date = date.replaceAll("\\.", "-");
		date = date.replaceAll(" ", "_");
		// 01234567890
		// 2011-10-11 12.45.37
		String year = date.substring(0, 4);
		String month = date.substring(5, 7);
		String ext = FileTools.extension(mediaInfo.filename);
		String filename = this.target + "\\" + year + "\\" + year + "-" + month
				+ "\\" + date + "." + ext;

		return filename;
	}

	private boolean checkFileExistsInTarget(String filename) {
		File f = new File(filename);
		if (f.exists()) {
			return true;
		}
		return false;
	}

}
