/**
 * 
 */
package com.dgsl.imp.main.java.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import main.utility.Constants;

import com.dgsl.imp.main.java.bo.FileDetail;
import com.dgsl.imp.main.java.service.extractFile.EarFileExtraction;
import com.dgsl.imp.main.java.service.report.ReportGenerator;

/**
 * @author subhasis.swain
 *
 */
public class CompareDirectory extends AbstractCompare {

	// This can be any folder locations which you want to compare
	/*
	 * File dir1 = new File("D:\\temp"); File dir2 = new File("D:\\temp1");
	 */

	private static int COUNT = 0;
	private static int FOLDERFLAG1 = 1;
	private static int FOLDERFLAG2 = 1;
	static File srcPath = null;
	static File dePath = null;
	FileDetail fileDetail = new FileDetail();
	//

	List<String> earfile1 = new LinkedList<String>();
	List<String> earfile2 = new LinkedList<String>();;

	HashMap<String, File> folderMap1 = new HashMap<String, File>();
	HashMap<String, File> folderMap2 = new HashMap<String, File>();
	
	static Set<FileDetail> fileDetails = new HashSet<FileDetail>();

	// For generating excel report. the difference between files.
	

	// InputStream input1 =
	// getClass().getResourceAsStream("/file_compare_utility/src/com/dgsl/imp/main/resources/config.properties");

	public static void main(String... args) {
		CompareDirectory compare = new CompareDirectory();
		ReportGenerator autofilter = new ReportGenerator();
		

		// Read the path from properties file
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(Constants.PATH.url());

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("sourcePath"));
			System.out.println(prop.getProperty("destinationPath"));

			srcPath = new File(prop.getProperty("sourcePath"));
			dePath = new File(prop.getProperty("destinationPath"));

			 compare.getDiff(srcPath, dePath);
			 autofilter.createReport(fileDetails);

		//	compare.getDiff(new File("/compare/src"), new File("/compare/dest"));
			// File("/compare/dest"));
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void getDiff(File srcPath, File dePath) throws IOException {
		// Getting List of files from the source and destination
		File[] fileList1 = srcPath.listFiles();
		File[] fileList2 = dePath.listFiles();
		Arrays.sort(fileList1);
		Arrays.sort(fileList2);

		HashMap<String, File> srcmap;
		EarFileExtraction earFileExtraction = null;
		int j = 0;
		int k = 0;

		for (File file : fileList1) {
			String fileType = fileDetail.getFileExtension(file);
			String name = file.getName();
			if (srcPath.toString().equalsIgnoreCase("/compare/src/")
					&& file.isDirectory() && earfile1.contains(name)) {
				fileList1[j].setLastModified(0000);
			}

			if (FOLDERFLAG1 == 1) {
				setFolderName1(file);
			}

			if ("ear".equals(fileType)) {
				System.out.println("hello");
				earFileExtraction = new EarFileExtraction();
				earFileExtraction.extractEar(file.getAbsolutePath(),
						file.getName(), true);

				// earfile1.add(fileNameWOExt);
				// ++COUNT;
			}
			++j;
		}
		++FOLDERFLAG1;

		for (File file : fileList2) {
			String fileType = fileDetail.getFileExtension(file);
			String name = file.getName();
			if (dePath.toString().equalsIgnoreCase("/compare/src/")
					&& file.isDirectory() && earfile2.contains(name)) {
				fileList2[k].setLastModified(0000);
			}

			if (FOLDERFLAG2 == 1) {
				setFolderName2(file);
			}

			if ("ear".equals(fileType)) {
				earFileExtraction = new EarFileExtraction();
				earFileExtraction.extractEar(file.getAbsolutePath(),
						file.getName(), false);

				// earfile2.add(fileNameWOExt);
				// ++COUNT;
			}
			++k;
		}
		++FOLDERFLAG2;

		srcmap = new HashMap<String, File>();
		for (int i = 0; i < fileList1.length; i++) {
			srcmap.put(fileList1[i].getName(), fileList1[i]);
		}

		compareNow(fileList2, srcmap);
		if (COUNT == 0) {
			++COUNT;
			getDiff(new File("/compare/src/"), new File("/compare/dest/"));
		}

	}

	/**
	 * @param file
	 * @param fileNameWOExt
	 * @return
	 */
	private void setFolderName2(File file) {
		String fileNameWOExt;
		if (file.isDirectory()) {
			fileNameWOExt = file.getName();
			folderMap2.put(fileNameWOExt, file);
		}
	}

	/**
	 * @param file
	 */
	private void setFolderName1(File file) {
		String fileNameWOExt;
		if (file.isDirectory()) {
			fileNameWOExt = file.getName();
			folderMap1.put(fileNameWOExt, file);
		}
	}

	// comparing the directory structure and files
	public void compareNow(File[] fileArr, HashMap<String, File> srcmap)
			throws IOException {

		for (int i = 0; i < fileArr.length; i++) {
			String fName = fileArr[i].getName();
			String fileTypeDest = fileDetail.getFileExtension(fileArr[i]);

			File srcfComp = srcmap.get(fName);
			srcmap.remove(fName);

			if (fileArr[i].lastModified() != 0000 || srcfComp != null
					|| srcfComp.lastModified() != 0000) {

				if (srcfComp != null) {
					if (srcfComp.isDirectory()) {
						// TO DO: CHECK THE CREATION DATE, SIZE
						getDiff(fileArr[i], srcfComp);

					} else {
						System.out.println(fileArr[i].getName());

						String cSum1 = checksum(fileArr[i]);
						String cSum2 = checksum(srcfComp);
						if (!cSum1.equals(cSum2)) {
							System.out.println(fileArr[i].getName() + "\t\t"
									+ "different");

							FileDetail fileDetail = new FileDetail();
							fileDetail.setFileName(fileArr[i].getName());
							fileDetail.setFilePath(fileArr[i].getParent());
							fileDetail.setStatus("Modified");
							fileDetails.add(fileDetail);

							// autofilter.createReport(fileArr[i].getName(),
							// fileArr[i].getParent(), "Modified");
						} else {
							if (fileArr[i].lastModified() == srcfComp
									.lastModified()) {
								System.out.println(fileArr[i].getName()
										+ "\t\t" + "identical");
							}
						}
					}
				} else if ("ear".equals(fileTypeDest)) {
					String fileNameWOExtDest = fileArr[i].getName().substring(
							0, fileArr[i].getName().lastIndexOf("."));
					Set<String> names = new HashSet<String>();
					names.addAll(folderMap1.keySet());
					for (String name : names)
						if (name.contains(fileNameWOExtDest)) {
							System.out.println("success dest Folder");
							getDiff(new File(srcPath + "/" + name), new File(
									"/compare/dest" + "/" + fileNameWOExtDest));
							earfile2.add(fileNameWOExtDest);
						}

				}

				else {
					System.out.println(fileArr[i].getName() + "\t\t"
							+ "only in " + fileArr[i].getParent());
					FileDetail fileDetail = new FileDetail();
					fileDetail.setFileName(fileArr[i].getName());
					fileDetail.setFilePath(fileArr[i].getParent());
					fileDetail.setStatus("New");
					fileDetails.add(fileDetail);

				}
			}
		}
		Set<String> set = srcmap.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String n = it.next();
			File fileFrmMap = srcmap.get(n);

			if (fileFrmMap != null || fileFrmMap.lastModified() != 0000) {
				String fileTypeSrc = fileDetail.getFileExtension(fileFrmMap);
				if ("ear".equals(fileTypeSrc)) {
					String fileNameWOExtSrc = fileFrmMap.getName().substring(0,
							fileFrmMap.getName().lastIndexOf("."));
					for (String name : folderMap2.keySet())
						if (name.contains(fileNameWOExtSrc)) {
							System.out.println("success src Folder");
							getDiff(new File("/compare/src" + "/"
									+ fileNameWOExtSrc), new File(dePath + "/"
									+ name));
							earfile1.add(fileNameWOExtSrc);
						}
				} else {
					System.out.println(fileFrmMap.getName() + "\t\t"
							+ "only in " + fileFrmMap.getParent());

					FileDetail fileDetail = new FileDetail();
					fileDetail.setFileName(fileFrmMap.getName());
					fileDetail.setFilePath(fileFrmMap.getParent());
					fileDetail.setStatus("New");
					fileDetails.add(fileDetail);
				}
			}

		}
	}

	/*
	 * public List<FileDetail> traverseDirectory(File dir) { File[] list =
	 * dir.listFiles(); List<FileDetail> fileDetails = new
	 * ArrayList<FileDetail>(); for(int k=0;k<list.length;k++) {
	 * if(list[k].isDirectory()) { traverseDirectory(list[k]); } else {
	 * System.out.println(list[k].getName() +"\t\t"+"only in "+
	 * list[k].getParent()); //fileDetails.add(fileDetail); } FileDetail
	 * fileDetail = new FileDetail(); fileDetail.setFileName(list[k].getName());
	 * fileDetail.setFilePath(list[k].getParent()); fileDetail.setStatus("New");
	 * fileDetails.add(fileDetail);
	 * 
	 * } return fileDetails; }
	 */

	public String checksum(File file) {
		try {
			InputStream fin = new FileInputStream(file);
			java.security.MessageDigest md5er = MessageDigest
					.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int read;
			do {
				read = fin.read(buffer);
				if (read > 0)
					md5er.update(buffer, 0, read);
			} while (read != -1);
			fin.close();
			byte[] digest = md5er.digest();
			if (digest == null)
				return null;
			String strDigest = "0x";
			for (int i = 0; i < digest.length; i++) {
				strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16)
						.substring(1).toUpperCase();
			}
			return strDigest;
		} catch (Exception e) {
			return null;
		}
	}

}
