package de.hbz.ebooks;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

@SuppressWarnings("javadoc")
/**
 * 
 * @author Raul Vasi
 *
 */
public class TestWebDownloader {

	@Test
	public void testClass() throws FileNotFoundException, IOException {

		WebDownloader dwnl = new WebDownloader("http://www.tutorialspoint.com/java/java_string_substring.htm");
		dwnl.download("p.html");
	}

}
