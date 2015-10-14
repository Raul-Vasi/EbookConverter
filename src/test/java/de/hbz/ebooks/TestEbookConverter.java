package de.hbz.ebooks;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;

@SuppressWarnings({ "javadoc" })
/**
 * 
 * @author Raul Vasi
 *
 */
public class TestEbookConverter {

	@Test
	public void testClass() {
		String inputDirectory = Thread.currentThread().getContextClassLoader().getResource("epub").getPath();
		EbookConverter conv = new EbookConverter(inputDirectory);
		File result = conv.convert("/tmp/rauls.epub");
		result.deleteOnExit();
		Assert.assertEquals(true, result.exists());
	}

	@Test
	public void testMain_local() {
		String localFlag = "-local";
		String inPath = Thread.currentThread().getContextClassLoader().getResource("epub").getPath();
		String outPath = "/tmp/r.epub";
		String[] sArray = new String[3];
		sArray[0] = localFlag;
		sArray[1] = inPath;
		sArray[2] = outPath;
		Main.main(sArray);
		File file = new File(outPath);
		file.deleteOnExit();
		Assert.assertEquals(true, file.exists());
	}

	@Test
	public void testMain_web() {
		String localFlag = "-web";
		String inPath = "https://www.jvrb.org/past-issues/11.2014/4075/fulltext/fedoraxml_body";
		String outPath = "/tmp/r.epub";
		String[] sArray = new String[3];
		sArray[0] = localFlag;
		sArray[1] = inPath;
		sArray[2] = outPath;
		Main.main(sArray);
		File file = new File(outPath);
		file.deleteOnExit();
		Assert.assertEquals(true, file.exists());
	}

	@Test
	public void test_ebook_result() {
		// Dieser Test soll das geschriebene ebook wieder einlesen und checken,
		// ob die Ressourcen aus dem Eingangsverzeichnis alle da sind.
		try {
			String inputDirectory = Thread.currentThread().getContextClassLoader().getResource("epub").getPath();
			EbookConverter conv = new EbookConverter(inputDirectory);
			File result = conv.convert("/tmp/rauls.epub");
			result.deleteOnExit();
			// ....

			/// -------
			// ByteArrayOutputStream out = new ByteArrayOutputStream();
			// new EpubWriter().write(conv.book, out);
			// byte[] epubData = out.toByteArray();

			// ----------
			// hier direkt vom File result lesen

			Book readBook = new EpubReader().readEpub(new FileInputStream(result));

			System.out.println(readBook.getTitle());

			// 1. Step: Ressourcen printen (Schleife über alle)
			System.out.println(readBook.getResources().getAll());

			// 2. Step: Überprüfen, ob alle da sind (getByHref("Epublib – ajava
			// epub library_files/init.js"))
			System.out.println(readBook.getResources().getByHref("Epublib – a java epub library_files/ga.js"));
			System.out.println(readBook.getResources()
					.getByHref("Epublib – a java epub library_files/200px-Epub_logo_color-109x150.png"));
			System.out.println(
					readBook.getResources().getByHref("Epublib – a java epub library_files/200px-Epub_logo_color.png"));
			System.out.println(readBook.getResources().getByHref(
					"Epublib – a java epub library_files/Alices-Adventures-in-Wonderland_2011-01-30_18-17-30-300x181.png"));
			System.out.println(
					readBook.getResources().getByHref("Epublib – a java epub library_files/external-tracking.js"));
			System.out.println(readBook.getResources().getByHref("Epublib – a java epub library_files/init.js"));
			System.out.println(
					readBook.getResources().getByHref("Epublib – a java epub library_files/jquery-migrate.js"));
			System.out.println(readBook.getResources().getByHref("Epublib – a java epub library_files/jquery.js"));
			System.out.println(readBook.getResources().getByHref("Epublib – a java epub library_files/style.css"));
			System.out.println(
					readBook.getResources().getByHref("Epublib – a java epub library_files/wp-emoji-release.js"));

			// ----------------ASSERTS------------------

			Assert.assertEquals(true,
					readBook.getResources().getByHref("Epublib – a java epub library_files/ga.js") != null);
			Assert.assertEquals(true, readBook.getResources()
					.getByHref("Epublib – a java epub library_files/200px-Epub_logo_color-109x150.png") != null);
			Assert.assertEquals(true, readBook.getResources()
					.getByHref("Epublib – a java epub library_files/200px-Epub_logo_color.png") != null);
			Assert.assertEquals(true, readBook.getResources().getByHref(
					"Epublib – a java epub library_files/Alices-Adventures-in-Wonderland_2011-01-30_18-17-30-300x181.png") != null);
			Assert.assertEquals(true, readBook.getResources()
					.getByHref("Epublib – a java epub library_files/external-tracking.js") != null);
			Assert.assertEquals(true,
					readBook.getResources().getByHref("Epublib – a java epub library_files/init.js") != null);
			Assert.assertEquals(true,
					readBook.getResources().getByHref("Epublib – a java epub library_files/jquery-migrate.js") != null);
			Assert.assertEquals(true,
					readBook.getResources().getByHref("Epublib – a java epub library_files/jquery.js") != null);
			Assert.assertEquals(true,
					readBook.getResources().getByHref("Epublib – a java epub library_files/style.css") != null);
			Assert.assertEquals(true, readBook.getResources()
					.getByHref("Epublib – a java epub library_files/wp-emoji-release.js") != null);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testAll() throws FileNotFoundException, IOException {
		Properties prop = new Properties();
		WebDownloader dwnl = new WebDownloader("http://www.tutorialspoint.com/java/java_string_substring.htm");
		File dir = dwnl.download("tmp.html");
		dir.deleteOnExit();
		EbookConverter conv = new EbookConverter(dir.getAbsolutePath());
		BufferedInputStream buf = new BufferedInputStream(new FileInputStream(
				new File(Thread.currentThread().getContextClassLoader().getResource("config.properties").getPath())));
		prop.load(buf);
		buf.close();
		File result = conv.convert(prop.getProperty("outputpath") + "/r.epub");
		result.deleteOnExit();
	}

	@Test
	public void testDownloader() {
		WebDownloader dwnl = new WebDownloader("http://www.tutorialspoint.com/java/java_string_substring.htm");
		File dir = dwnl.download("tmp.html");
		dir.deleteOnExit();
	}
}
