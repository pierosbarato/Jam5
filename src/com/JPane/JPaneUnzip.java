package com.JPane;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

// =============================================================================
public class JPaneUnzip {
	private final static int BUFFER_SIZE = 2048;
	private final static String ZIP_FILE = JPane.temp + "ZipDocumenti.zip";
	private final static String DESTINATION_DIRECTORY = JPane.temp + "Fatture/in";
	private final static String ZIP_EXTENSION = ".zip";

	// =========================================================================
	public static void main(String[] args) {
		
		unzipDir();
		System.exit(0);
		
		System.out.println("--- Unzip: " + ZIP_FILE);
		JPaneUnzip unzip = new JPaneUnzip();

		
		if (unzip.unzipToFile(ZIP_FILE, DESTINATION_DIRECTORY)) {
			System.out.println("--- UnzippedDir: " + DESTINATION_DIRECTORY);
		} else {
			System.err.println(
				"--- Error extracting to directory: " + DESTINATION_DIRECTORY);
		}
	}
	
	// =========================================================================
	static void unzipDir() {
		String dir = JPane.temp + "Fatture/inZip/";
		String type = "file";
		String filter = "";
		int jLine = 0;
		System.out.println("--- UnzippedDir: " +dir);
		File f = new File(dir);
		File[] allSubFiles = f.listFiles();
		JPaneUnzip unzip = new JPaneUnzip();
		for (File file : allSubFiles) {
			{
				String val = file.getName().toString();
				if(!val.endsWith(".zip")
				 &&!val.endsWith(".ZIP")) continue;
				
				System.out.println("--- Unzip: " +  dir + val);
				
				if (unzip.unzipToFile(dir + val, DESTINATION_DIRECTORY)) {
					System.out.println("--- UnzippedDir: " + DESTINATION_DIRECTORY);
				} else {
					System.err.println(
						"--- Error extracting to directory: " + DESTINATION_DIRECTORY);
				}
			}
		}
	}

	// =========================================================================
	public boolean unzipToFile(String srcZipFileName, String destDirectoryName) {
		try {
			BufferedInputStream bufIS = null;
			// create the destination directory structure (if needed)
			File destDirectory = new File(destDirectoryName);
			destDirectory.mkdirs();

			// open archive for reading
			File file = new File(srcZipFileName);
			ZipFile zipFile = new ZipFile(file, ZipFile.OPEN_READ);

			// for every zip archive entry do
			Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();
			while (zipFileEntries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
				System.out.println("---\tExtracting: " + entry);

				// create destination file
				File destFile = new File(destDirectory, entry.getName());

				// create parent directories if needed
				File parentDestFile = destFile.getParentFile();
				parentDestFile.mkdirs();

				if (!entry.isDirectory()) {
					bufIS = new BufferedInputStream(zipFile.getInputStream(entry));
					int currentByte;

					// buffer for writing file
					byte data[] = new byte[BUFFER_SIZE];

					// write the current file to disk
					FileOutputStream fOS = new FileOutputStream(destFile);
					BufferedOutputStream bufOS = new BufferedOutputStream(fOS, BUFFER_SIZE);

					while ((currentByte = bufIS.read(data, 0, BUFFER_SIZE)) != -1) {
						bufOS.write(data, 0, currentByte);
					}

					// close BufferedOutputStream
					bufOS.flush();
					bufOS.close();

					// recursively unzip files
					if (entry.getName().toLowerCase().endsWith(ZIP_EXTENSION)) {
						String zipFilePath = 
//								destDirectory.getPath() + 
								DESTINATION_DIRECTORY + 
								File.separatorChar + entry.getName();

						unzipToFile(zipFilePath
//								,zipFilePath.substring(0, zipFilePath.length() - ZIP_EXTENSION.length())
								,DESTINATION_DIRECTORY
						);

						System.out.println("---\tDelete: " + zipFilePath);
						File f = new File(zipFilePath);
				        f.delete();
					}
				}
			}
			bufIS.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}