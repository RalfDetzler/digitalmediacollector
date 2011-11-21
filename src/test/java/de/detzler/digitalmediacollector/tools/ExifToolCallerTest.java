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

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests the ExifToolCaller class.<br />
 * 
 */
public class ExifToolCallerTest {

	@Test
	public void testJpeg() throws Exception {

		String jpegExample = "src/test/resources/048.jpg";

		ExifToolCaller exifToolCaller = new ExifToolCaller(jpegExample);
		Map<String, String> res = exifToolCaller.queryExifInfo();
		Assert.assertNotNull(res);
		Assert.assertEquals(60, res.size());

		Assert.assertTrue(res.containsKey(ExifToolCaller.DATE_TIME_ORIGINAL));
		Assert.assertEquals("src/test/resources/048.jpg", exifToolCaller
				.getExifInfo().getFilename());
		Assert.assertEquals("jpg", exifToolCaller.getExifInfo().getExt());
		Assert.assertEquals("b045a235708d8a4ac0b8a9e1b3976581", exifToolCaller
				.getExifInfo().getMd5());
		Assert.assertEquals("2011-11-10 17.46.10", exifToolCaller.getExifInfo()
				.getCreationDate());

		System.out.println(exifToolCaller.getExifInfo());

	}
}
