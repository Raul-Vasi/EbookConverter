package de.hbz.ebooks;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import nl.siegmann.epublib.util.IOUtil;

public class WebDownloader {

	String url = null;
	String outputpath = "/tmp/webdownloader";

	public WebDownloader(String url) {
		this.url = url;
	}

	public File download(String filename) {
		try {
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

	private String createDownloadLocation(String outputpath) throws MalformedURLException, IOException {
		URL myUrl = new URL(url);
		String finalDownloadLocation = outputpath + File.separator + myUrl.getHost();
		Files.createDirectories(Paths.get(finalDownloadLocation));
		return finalDownloadLocation;
	}

}
