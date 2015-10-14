package de.hbz.ebooks;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * 
 * @author Raul Vasi
 *
 */
public class Main {

	/**
	 * @param args
	 *            Absolute Pfade.<br/>
	 *            args[0] Absoluter Pfad und Dateiname der zu konvertierenden
	 *            <code>HTML</code>. args[1] Absoluter Pfad, Zielpfad der
	 *            konvertierten <i>epub</i>.
	 */
	public static void main(String[] args) {

		if (args.length == 3) {
			switch (args[0]) {
			case "-local":
				eBookConverter(args[1], args[2]);
				break;
			case "-web":
				DateFormat dfmt = new SimpleDateFormat("yyyyMMddhhmmss");
				webDownloader(args[1], args[2], dfmt.format(new Date()) + "WebDownload");
				break;
			}

		} else {
			switch (args[0]) {
			case "-local":
				eBookConverter(args[1], args[2]);
				break;
			case "-web":
				webDownloader(args[1], args[2], args[3]);
				break;
			}
		}

	}

	static void eBookConverter(String source, String outputfile) {
		try {
			EbookConverter conv = new EbookConverter(source);
			conv.convert(outputfile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void webDownloader(String url, String outputfile, String filename) {
		WebDownloader dwnl = new WebDownloader(url);
		File downloadLocation = dwnl.download(filename);
		eBookConverter(downloadLocation.getAbsolutePath(), outputfile);
	}

}
