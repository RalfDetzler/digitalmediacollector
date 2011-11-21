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
import java.util.Map;

import static org.junit.Assert.*;
import org.junit.Test;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifTool.Tag;

/**
 * Unit test for simple App.
 */
public class AppTest {
	


	/**
	 * Rigourous Test :-)
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testApp() throws IllegalArgumentException, SecurityException, IOException {

		
		File image = new File("src/test/resources/048.bmp");// path to some image
		ExifTool tool = new ExifTool();

		Map<Tag, String> valueMap = tool.getImageMeta(image,Tag.DATE_TIME_ORIGINAL);

		System.out.println(valueMap);
		
		assertTrue(true);
	}
}
