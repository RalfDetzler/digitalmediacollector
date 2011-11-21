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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Calculate a checksum for a given file.<br />
 * 
 * Uses the MD5 message digest.
 */
public class Checksum {

	private String checksum;

	public Checksum(File inputFile) throws NoSuchAlgorithmException,
			IOException {

		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		InputStream inputStream = new FileInputStream(inputFile);
		byte[] buffer = new byte[1024];
		int numRead;

		do {
			numRead = inputStream.read(buffer);
			if (numRead > 0) {
				messageDigest.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		inputStream.close();

		byte[] digest = messageDigest.digest();

		checksum = "";
		for (int i = 0; i < digest.length; i++) {
			checksum += Integer.toString((digest[i] & 0xff) + 0x100, 16)
					.substring(1);
		}

	}

	public String getChecksum() {
		return checksum;
	}

}
