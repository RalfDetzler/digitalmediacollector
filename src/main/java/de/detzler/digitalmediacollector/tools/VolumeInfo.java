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
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Creates a volume info for a drive.<br />
 * Works currently only with Windows.
 */
public class VolumeInfo {

	private String drive = "unknown";
	private String serial = "unknown";
	private String name = "unknown";

	public VolumeInfo(String driveLetter) throws IOException {

		String cmd = "dir " + driveLetter + ":\\*.*";

		java.lang.ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", cmd);
		java.lang.Process p = pb.start();

		BufferedReader r = new BufferedReader(new InputStreamReader(
				p.getInputStream()));

		String line;
		while ((line = r.readLine()) != null) {
			// System.out.println(line);
			if (line.startsWith(" Volume in drive")) {
				drive = line.substring(17, 18);
				name = line.substring(22);
			}
			if (line.startsWith(" Datentr")) {
				drive = line.substring(25, 26);
				name = line.substring(32);
			}

			if (line.startsWith(" Volume Serial Number is")) {
				serial = line.substring(25);
			}
			if (line.startsWith(" Volumeseriennummer")) {
				serial = line.substring(21);
			}
		}

		r.close();
	}

	/**
	 * @return the drive
	 */
	public String getDrive() {
		return drive;
	}

	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "VolumeInfo [drive=" + drive + ", serial=" + serial + ", name="
				+ name + "]";
	}

}
