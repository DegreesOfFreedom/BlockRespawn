package dev.zariem.blockrespawn.objects;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

/**
 * File handling class, only load & write string
 */
public abstract class FH
{
	/**
	 * Load file to String
	 * @param path String with filename
	 * @return String file contents
	 * @throws IOException
	 */
	static String readFile(String path) throws IOException {
	  return FileUtils.readFileToString(new File(path), Charset.defaultCharset());
	}
	
	/**
	 * (Over)write file with data
	 * @param string String containing data
	 * @param path String containing filename
	 * @throws IOException
	 */
	public static void writeFile(String string, String path) throws IOException {
		FileUtils.writeStringToFile(new File(path), string, Charset.defaultCharset());
	}
	
	/**
	 * Append data to file
	 * @param string String containing data
	 * @param path String containing filename
	 * @throws IOException
	 */
	public static void appendFile(String string, String path) throws IOException {
		FileUtils.writeStringToFile(new File(path), string, Charset.defaultCharset(), true);
	}
}
