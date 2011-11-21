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

import de.detzler.digitalmediacollector.tools.FileTools;

/**
 * Holds the basic information about the media file.<br />
 * 
 */
public class MediaInfo {

	protected String creationDate = "";
	protected String duration = "";
	protected String filename = "";
	protected String md5 = "";
	protected String ext = "";

	/**
	 * Returns the creation date of the media file.<br />
	 * 
	 * @return
	 */
	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Returns the duration in seconds of a video media file.<br />
	 * 
	 * @return duration in seconds
	 */
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Returns a MD5 checksum that identifies the media file.<br />
	 * 
	 * @return
	 */
	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	/**
	 * Returns the file extension of the media file.<br />
	 * 
	 * @return
	 */
	public String getExt() {
		if (ext.isEmpty()) {
			ext = FileTools.extension(this.getFilename());
		}
		
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Override
	public String toString() {
		return "MediaInfo [creationDate=" + creationDate + ", duration="
				+ duration + ", filename=" + filename + ", md5=" + md5
				+ ", ext=" + ext + "]";
	}

}
