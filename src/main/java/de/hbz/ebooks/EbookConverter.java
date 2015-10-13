package de.hbz.ebooks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;

/**
 * Der <i>EbookConverter</i> konvertiert <code>HTML</code> Seiten in
 * <code>Epubs</code>
 * <p>
 * 
 * @author Raul Vasi
 *
 */
public class EbookConverter {

	Book book = new Book();
	String source;

	/**
	 * 
	 * 
	 * @param absolutePathToHtmlDir
	 *            Der abosolute lokale Pfad zur <code>HTML</code> Seite als
	 *            {@code String}. Beispiel: <blockquote>
	 * 
	 * 
	 *            EbookConverter conv = new EbookConverter("/home/user/HTML");;
	 * 
	 * 
	 *            </blockquote>
	 *            <p>
	 */
	public EbookConverter(String absolutePathToHtmlDir) {
		source = absolutePathToHtmlDir;
	}

	// --------------------------------------------------------------------------------------------------------------------
	void createBook(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					createBook(files[i]);
				} else {
					addToBook(files[i]);
				}
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	void addToBook(File file) {
		try {
			String filename = file.toString();
			if (filename.endsWith("html")) {
				addChapter(file);
			} else {
				addResource(file);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	void addResource(File file) {
		try {
			InputStream input = new FileInputStream(file);
			Path base = Paths.get(source);
			String filePathName = base.relativize(Paths.get(file.getAbsolutePath())).toString();
			book.addResource(new Resource(input, filePathName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	void addChapter(File file) {
		try {
			InputStream in = new FileInputStream(file);
			String filePathName = "chapter1.html";
			book.getMetadata().addTitle("Test");
			book.addSection("Chapter 1", new Resource(in, filePathName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// --------------------------------------------------------------------------------------------------------------------
	/**
	 * Konvertiert alle gesamelten Daten/META-Daten zu einem <i>epub</i>.
	 * 
	 * @param targetFile
	 *            Den Absoluten Pfad und Namen der zu erzeugenden Datei.
	 * 
	 * @return Denn Ausgangspfad der erzeugten Epub.
	 */
	public File convert(String targetFile) {
		try {
			File destination = new File(targetFile);
			createBook(new File(source));
			new EpubWriter().write(book, new FileOutputStream(destination));
			return destination;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
// --------------------------------------------------------------------------------------------------------------------