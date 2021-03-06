package de.hbz.ebooks;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nl.siegmann.epublib.util.IOUtil;

/**
 * @author Raul Vasi
 *
 */
public class WebDownloader {

	String url = null;
	String outputpath = "/tmp/";
	Properties prop = new Properties();

	/**
	 * Konstruktor- Hier wird dir URL Adresse angenommen.
	 * 
	 * @param url
	 */
	public WebDownloader(String url) {
		this.url = url;
	}

	/**
	 * Das downloaden der kompletten Webseite über die URL. Die URL samt
	 * Resourcen wir in einem Document Objekt gespeichert. Das Document Objekt
	 * wird in einem {@link ByteArrayInputStream} geschrieben und anschliessend
	 * mit {@linkplain IOUtils} in Dateien angelegt.
	 * 
	 * @param filename
	 * @return downloadLocation
	 */
	public File download(String filename) {
		try {
			readprop();

			String downloadLocation = createDownloadLocation(outputpath);
			Document doc = Jsoup.connect(url).get();
			Elements img = doc.getElementsByTag("img");

			ByteArrayInputStream bs = new ByteArrayInputStream(doc.outerHtml().getBytes("UTF-8"));

			if (filename.endsWith(".html")) {
				IOUtil.copy(bs, new FileOutputStream(new File(downloadLocation + File.separator + filename)));
			} else {
				IOUtil.copy(bs, new FileOutputStream(new File(downloadLocation + File.separator + filename + ".html")));
			}

			for (Element element : img) {
				String src = element.absUrl("src");
				Response resultImageResponse = Jsoup.connect(src).ignoreContentType(true).execute();
				File file = new File(downloadLocation + "/" + element.attr("src"));
				Files.createDirectories(Paths.get(file.getParent()));
				FileOutputStream fileOutStram = (new FileOutputStream(file));
				fileOutStram.write(resultImageResponse.bodyAsBytes());
				fileOutStram.close();
			}
			return new File(downloadLocation);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
	}

	private String readprop() {
		try {
			BufferedInputStream buf = new BufferedInputStream(new FileInputStream(new File(
					Thread.currentThread().getContextClassLoader().getResource("config.properties").getPath())));
			prop.load(buf);
			buf.close();
			if (prop.getProperty("outputpath").length() == 0) {
				return outputpath;
			} else {
				outputpath = prop.getProperty("outputpath");
				return outputpath;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private String createDownloadLocation(String outputpath) throws MalformedURLException, IOException {
		URL myUrl = new URL(url);
		String finalDownloadLocation = outputpath + File.separator + myUrl.getHost();
		Files.createDirectories(Paths.get(finalDownloadLocation));
		return finalDownloadLocation;
	}

}
