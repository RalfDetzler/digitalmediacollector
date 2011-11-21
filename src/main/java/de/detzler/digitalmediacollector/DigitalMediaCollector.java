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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.detzler.digitalmediacollector.tools.ExifToolCaller;
import de.detzler.digitalmediacollector.tools.FileTools;
import de.detzler.digitalmediacollector.tools.VolumeInfo;

public class DigitalMediaCollector {

	private List<File> files = new ArrayList<File>(1000);
	private String skipDirs = "$Recycle.Bin;Bilder_und_Videos;.git;.svn;tmp;Program Files (x86);Program Files;Windows;ProgramData;sappcadm;.dtc;eclipse;myfaces;.metadata;.jdi;.hudson;.jenkins;.m2;DCs;AppData;workspace;bin";
	private String fileExtensionFilter = "'jpg';'mts'";
	private String mediaKey = "";
	private String startDir = "";
	private VolumeInfo volumeInfo;
	private boolean calcMd5 = true;

	public DigitalMediaCollector(String mediaKey, String startDir) throws IOException {
		this.mediaKey = mediaKey;
		this.startDir = startDir;
		volumeInfo = new VolumeInfo(new File(startDir).getAbsolutePath()
				.substring(0, 1));
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.err.println("usage: " + DigitalMediaCollector.class.getName()
					+ "'media key' 'start directory'");
			System.exit(-1);
		}

		DigitalMediaCollector ftw = new DigitalMediaCollector(args[0], args[1]);
		ftw.process();
	}

	public void process() throws Exception {
		treeWalk(new File(startDir));
	}

	public void treeWalk(File file) throws Exception {

		if (file.isDirectory()) {
			walkDirectory(file);
		} else {
			walkFile(file);
		}
	}

	private void walkFile(File file) throws Exception {

		String extension =  FileTools.extension(file.getAbsolutePath());
		if (extension.isEmpty()) {
			return;
		}
		if (!fileExtensionFilter.contains("'" + extension + "'")) {
			return;
		}

		files.add(file);
		processFile(file);
	}

	protected void processFile(File file) throws Exception {

		ExifToolCaller exifToolCaller = new ExifToolCaller(
				file.getAbsolutePath(), calcMd5);

		exifToolCaller.queryExifInfo();
		MediaInfo mediaInfo = exifToolCaller.getExifInfo();

		System.out.println(mediaInfo.md5 + "\t" + mediaInfo.creationDate + "\t"
				+ mediaKey + "\t" + volumeInfo + "\t" + mediaInfo.filename);
	}

	

	private void walkDirectory(File file) throws Exception {

		String name = file.getName();
		if (name.isEmpty()) {
			name = file.getPath();
		}
		if (skipDirs.contains(name)) {
			// System.out.println("Skipping " + name);
			// System.out.print(".");
			return;
		}
		if (name.startsWith(".")) {
			// System.out.println("Skipping " + name);
			// System.out.print(".");
			return;
		}

		// System.out.println("Processing directory " + file.getAbsolutePath());
		// System.out.print(".");
		File[] children = file.listFiles();
		if (children == null) {
			return;
		}

		for (int i = 0; i < children.length; i++) {
			treeWalk(children[i]);
		}

	}

	public List<File> getFiles() {
		return files;
	}

	public boolean isCalcMd5() {
		return calcMd5;
	}

	public void setCalcMd5(boolean calcMd5) {
		this.calcMd5 = calcMd5;
	}

	public String getSkipDirs() {
		return skipDirs;
	}

	public void setSkipDirs(String skipDirs) {
		this.skipDirs = skipDirs;
	}

	public String getFileExtensionFilter() {
		return fileExtensionFilter;
	}

	public void setFileExtensionFilter(String fileExtensionFilter) {
		this.fileExtensionFilter = fileExtensionFilter;
	}

	public String getMediaKey() {
		return mediaKey;
	}

	public void setMediaKey(String mediaKey) {
		this.mediaKey = mediaKey;
	}

	public String getStartDir() {
		return startDir;
	}

	public void setStartDir(String startDir) {
		this.startDir = startDir;
	}

	public VolumeInfo getVolumeInfo() {
		return volumeInfo;
	}

	public void setVolumeInfo(VolumeInfo volumeInfo) {
		this.volumeInfo = volumeInfo;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}

}
