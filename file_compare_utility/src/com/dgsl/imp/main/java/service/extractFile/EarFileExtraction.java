package com.dgsl.imp.main.java.service.extractFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 */

/**
 * @author Subhasis
 *
 */
public class EarFileExtraction {

	/**
	 * @param args
	 */

	/*public static void main(String[] args) throws java.io.IOException {
		String path = "D:/temp/inspector.ear";
		extractEar(path);

	}*/
	public void extractEar(String path, String fileName, boolean pathFlag) throws IOException, FileNotFoundException {
		java.util.jar.JarFile jarfile = new java.util.jar.JarFile(new java.io.File(path)); // jar
																										// file																									// path(here
																										// sqljdbc4.jar)
		java.util.Enumeration<java.util.jar.JarEntry> enu = jarfile.entries();
		
		String fileNameWOExt = fileName.substring(0, fileName.lastIndexOf("."));
		System.out.println(fileNameWOExt);
		
		while (enu.hasMoreElements()) {
			String destdir = null;
			if(pathFlag){
				destdir= "/compare/src"/*+"/"+"IBM_BPM_"+fileNameWOExt+"_BPMPCENV.AppTarget.ear"*/;
			}else{
				destdir = "/compare/dest/"+fileNameWOExt/*+"/"+"IBM_BPM_"+fileNameWOExt+"_BPMPCENV.AppTarget.ear"*/;// abc is my destination directory
			}
			java.util.jar.JarEntry je = enu.nextElement();

			System.out.println(je.getName());
			
			java.io.File fl = new java.io.File(destdir, je.getName());
			String fileExt = getFileExtension(fl);
			System.out.println("=================="+fileExt+"==============");

			if("war".equals(fileExt)){
				System.out.println(fl.getCanonicalPath()+"========"+fl.getAbsolutePath());
				System.out.println("***********************"+fl.getParentFile().getAbsolutePath());
				String fln = fl.getName().substring(0, fl.getName().lastIndexOf("."));
				extractEar(fl.getAbsolutePath(), fl.getName(), pathFlag);
			}
			
			
			if (!fl.exists()) {
				fl.getParentFile().mkdirs();
				fl = new java.io.File(destdir, je.getName());
			}
			if (je.isDirectory()) {
				continue;
			}
			java.io.InputStream is = jarfile.getInputStream(je);
			java.io.FileOutputStream fo = new java.io.FileOutputStream(fl);
			while (is.available() > 0) {
				fo.write(is.read());
			}
			fo.close();
			is.close();
		}
	}
	 private static String getFileExtension(File file) {
	        String fileName = file.getName();
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0 && file.isFile())
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "NA";
	    }

}
