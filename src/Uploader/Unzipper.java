package Uploader;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import net.lingala.zip4j.core.ZipFile;

public class Unzipper {
	String source;
	String destination;

	public Unzipper(String source, String destination) {
		this.source = source;
		this.destination = destination;
	}

	public void extract() {
		try {
			java.util.zip.ZipFile uz = new java.util.zip.ZipFile(new File(
					source));
			final Enumeration<? extends ZipEntry> entries = uz.entries();
			while (entries.hasMoreElements()) {
				final ZipEntry it = entries.nextElement();
				if (it.isDirectory()) {
					File file = new File(destination, it.getName());
					file.mkdirs();
				}
			}
			ZipFile zipFile = new ZipFile(source);
			zipFile.extractAll(destination);
		} catch (Exception e) {

		}
	}
}
