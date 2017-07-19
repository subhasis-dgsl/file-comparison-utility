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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.swing.plaf.metal.MetalIconFactory.FolderIcon16;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.record.formula.functions.Count;

import main.utility.Constants;

import com.dgsl.imp.main.java.bo.FileDetail;
import com.dgsl.imp.main.java.service.compareContents.CompareContents;
import com.dgsl.imp.main.java.service.extractFile.EarFileExtraction;
import com.dgsl.imp.main.java.service.report.ReportGenerator;


/**
 * @author subhasis.swain
 *
 */
public class CompareDirectory extends AbstractCompare {

	 // This can be any folder locations which you want to compare
	/*File dir1 = new File("D:\\temp");
	File dir2 = new File("D:\\temp1");*/

	private static int COUNT = 0;
	static File srcPath = null;
	static File dePath = null;
	ReportGenerator autofilter = null;
	FileDetail fileDetail = new FileDetail();
	
	List<String> earfile1 = new LinkedList<String>();
	List<String> earfile2 = new LinkedList<String>();;
	
	HashMap<String, File> folderMap1 = new HashMap<String, File>();
	HashMap<String, File> folderMap2 = new HashMap<String, File>();

	public static void main(String... args) {
		CompareDirectory compare = new CompareDirectory();

		// Read the path from properties file
		Properties prop = new Properties();
		InputStream input = null;

		try {

			input = new FileInputStream(
					Constants.PATH.url());

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			System.out.println(prop.getProperty("sourcePath"));
			System.out.println(prop.getProperty("destinationPath"));
			
			srcPath = new File(prop.getProperty("sourcePath"));
			dePath = new File(prop.getProperty("destinationPath"));
			
			compare.getDiff(srcPath, dePath);
			
		//	compare.getDiff(new File("/compare/src"), new File("/compare/dest"));
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
	
	public void getDiff(File srcPath, File dePath) throws IOException
	{
		//Getting List of files from the source and destination
		File[] fileList1 = srcPath.listFiles();
		File[] fileList2 = dePath.listFiles();
		Arrays.sort(fileList1);
		Arrays.sort(fileList2);
		
		HashMap<String, File> srcmap;
		
		EarFileExtraction earFileExtraction = null;
		HashMap<String, File> earFileMap;
	
		earFileMap = new HashMap<String, File>();
		for(File file:fileList1){
			String fileType = fileDetail.getFileExtension(file);
			String fileNameWOExt = null;
			if(file.isDirectory()){
			fileNameWOExt = file.getName();
			}else if(file.isFile()){
			fileNameWOExt = file.getName().substring(0, file.getName().lastIndexOf("."));
			}
			
			if(file.isDirectory()){
				folderMap1.put(fileNameWOExt, file);
			}
			if("ear".equals(fileType)){
				System.out.println("hello");
				earFileExtraction = new EarFileExtraction();
				earFileExtraction.extractEar(file.getAbsolutePath(),file.getName(), true);

				
				System.out.println(fileNameWOExt);
				earfile1.add(fileNameWOExt);
			//	++COUNT;
			}
		}
		
		for(File file:fileList2){
			String fileType = fileDetail.getFileExtension(file);
			System.out.println(file.getName());
			String fileNameWOExt = null;
			if(file.isDirectory()){
			fileNameWOExt = file.getName();
			}else if(file.isFile()){
			fileNameWOExt = file.getName().substring(0, file.getName().lastIndexOf("."));
			}
			
			if(file.isDirectory()){
				folderMap2.put(fileNameWOExt, file);
			}
			
			if("ear".equals(fileType)){
				System.out.println("hello");
				earFileExtraction = new EarFileExtraction();
				earFileExtraction.extractEar(file.getAbsolutePath(),file.getName(), false);
				
				System.out.println(fileNameWOExt);
				earfile2.add(fileNameWOExt);
			//	++COUNT;
			}
		}
		
			srcmap = new HashMap<String, File>();
			for(int i=0;i<fileList1.length;i++)
			{
				srcmap.put(fileList1[i].getName(),fileList1[i]);
			}
			
			compareNow(fileList2, srcmap);
		
	}
	
	public void compareNow(File[] fileArr, HashMap<String, File> srcmap) throws IOException
	{
		List<FileDetail> fileDetails = new ArrayList<FileDetail>();
		autofilter = new ReportGenerator();
		for(int i=0;i<fileArr.length;i++)
		{
			String fName = fileArr[i].getName();
			String fileTypeDest = fileDetail.getFileExtension(fileArr[i]);
				File srcfComp = srcmap.get(fName);
			srcmap.remove(fName);
			if(srcfComp!=null)
			{
				if(srcfComp.isDirectory())
				{
				//	getDiff(fileArr[i], srcfComp);
					
				}
				else
				{
				System.out.println(fileArr[i].getName());
				//	FileUtils.copyDirectory(fileArr[i].getParentFile(), destDir);
				
					

					String cSum1 = checksum(fileArr[i]);
					String cSum2 = checksum(srcfComp);
					if(!cSum1.equals(cSum2))
					{
						System.out.println(fileArr[i].getName()+"\t\t"+ "different");
						
						FileDetail fileDetail = new FileDetail();
						fileDetail.setFileName(fileArr[i].getName());
						fileDetail.setFilePath(fileArr[i].getParent());
						fileDetail.setStatus("Modified");
						fileDetails.add(fileDetail);
						
					//	autofilter.createReport(fileArr[i].getName(), fileArr[i].getParent(), "Modified");
					}
					else
					{
						if(fileArr[i].lastModified() == srcfComp.lastModified()){
						System.out.println(fileArr[i].getName()+"\t\t"+"identical");
						}/*else{
							System.out.println(fileArr[i].getName()+"\t\t"+ "different");
							
							FileDetail fileDetail = new FileDetail();
							fileDetail.setFileName(fileArr[i].getName());
							fileDetail.setFilePath(fileArr[i].getParent());
							fileDetail.setStatus("Modified");
							fileDetails.add(fileDetail);
							
						//	autofilter.createReport(fileArr[i].getName(), fileArr[i].getParent(), "Modified");
							
						}*/
					}
					}
				}
			else if("ear".equals(fileTypeDest)){
				String fileNameWOExtDest = fileArr[i].getName().substring(0, fileArr[i].getName().lastIndexOf("."));
				Set<String> names=new HashSet<String>();
				names.addAll(folderMap1.keySet());
				for(String name:names)
				if(name.contains(fileNameWOExtDest)){
					System.out.println("success dest Folder");
					getDiff(new File(srcPath+"/"+name),new File("/compare/dest"+"/"+fileNameWOExtDest));
				}
				
			}
			
			else
			{
					System.out.println(fileArr[i].getName()+"\t\t"+"only in "+fileArr[i].getParent());
					FileDetail fileDetail = new FileDetail();
					fileDetail.setFileName(fileArr[i].getName());
					fileDetail.setFilePath(fileArr[i].getParent());
					fileDetail.setStatus("New");
					fileDetails.add(fileDetail);
				
			}
		}
		Set<String> set = srcmap.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext())
		{
			String n = it.next();
			File fileFrmMap = srcmap.get(n);
		
				System.out.println(fileFrmMap.getName() +"\t\t"+"only in "+ fileFrmMap.getParent());
				
				FileDetail fileDetail = new FileDetail();
				fileDetail.setFileName(fileFrmMap.getName());
				fileDetail.setFilePath(fileFrmMap.getParent());
				fileDetail.setStatus("New");
				fileDetails.add(fileDetail);
				
				
				String fileTypeSrc = fileDetail.getFileExtension(fileFrmMap);
				if("ear".equals(fileTypeSrc)){
					String fileNameWOExtSrc = fileFrmMap.getName().substring(0, fileFrmMap.getName().lastIndexOf("."));
					for(String name:folderMap2.keySet())
					if(name.contains(fileNameWOExtSrc)){
						System.out.println("success src Folder");
						getDiff(new File("/compare/src"+"/"+fileNameWOExtSrc),new File(dePath+"/"+name));
					}
				}
				
			
		}
		
		autofilter.createReport(fileDetails);
		
	}
	
	/*public List<FileDetail> traverseDirectory(File dir)
	{
		File[] list = dir.listFiles();
		List<FileDetail> fileDetails = new ArrayList<FileDetail>();
		for(int k=0;k<list.length;k++)
		{
			if(list[k].isDirectory())
			{
				traverseDirectory(list[k]);
			}
			else
			{
				System.out.println(list[k].getName() +"\t\t"+"only in "+ list[k].getParent());
				//fileDetails.add(fileDetail);
			}
			FileDetail fileDetail = new FileDetail();
			fileDetail.setFileName(list[k].getName());
			fileDetail.setFilePath(list[k].getParent());
			fileDetail.setStatus("New");
			fileDetails.add(fileDetail);
			
		}
		return fileDetails;
	}*/
	
	public String checksum(File file) 
	{
		try 
		{
		    InputStream fin = new FileInputStream(file);
		    java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
		    byte[] buffer = new byte[1024];
		    int read;
		    do 
		    {
		    	read = fin.read(buffer);
		    	if (read > 0)
		    		md5er.update(buffer, 0, read);
		    } while (read != -1);
		    fin.close();
		    byte[] digest = md5er.digest();
		    if (digest == null)
		      return null;
		    String strDigest = "0x";
		    for (int i = 0; i < digest.length; i++) 
		    {
		    	strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
		    }
		    return strDigest;
		} 
		catch (Exception e) 
		{
		    return null;
		}


		/*
		 * try { compare.getDiff(compare.dir1,compare.dir2); } catch(IOException
		 * ie) { ie.printStackTrace(); } }
		 * 
		 * public void getDiff(File dirA, File dirB) throws IOException { File[]
		 * fileList1 = dirA.listFiles(); File[] fileList2 = dirB.listFiles();
		 * Arrays.sort(fileList1); Arrays.sort(fileList2); HashMap<String, File>
		 * map1; if(fileList1.length < fileList2.length) { map1 = new
		 * HashMap<String, File>(); for(int i=0;i<fileList1.length;i++) {
		 * map1.put(fileList1[i].getName(),fileList1[i]); }
		 * 
		 * compareNow(fileList2, map1); } else { map1 = new HashMap<String,
		 * File>(); for(int i=0;i<fileList2.length;i++) {
		 * map1.put(fileList2[i].getName(),fileList2[i]); }
		 * compareNow(fileList1, map1); } }
		 * 
		 * public void compareNow(File[] fileArr, HashMap<String, File> map)
		 * throws IOException { autofilter = new ReportGenerator();
		 * List<FileDetails> fileDetails = new ArrayList<FileDetails>(); for(int
		 * i=0;i<fileArr.length;i++) { String fName = fileArr[i].getName(); File
		 * fComp = map.get(fName); map.remove(fName); if(fComp!=null) {
		 * if(fComp.isDirectory()) { getDiff(fileArr[i], fComp); } else { String
		 * cSum1 = checksum(fileArr[i]); String cSum2 = checksum(fComp);
		 * if(!cSum1.equals(cSum2)) {
		 * System.out.println(fileArr[i].getName()+"\t\t"+ "different");
		 * 
		 * FileDetails fileDetail = new FileDetails();
		 * fileDetail.setFileName(fileArr[i].getName());
		 * fileDetail.setFilePath(fileArr[i].getParent());
		 * fileDetail.setStatus("Modified"); fileDetails.add(fileDetail);
		 * 
		 * // autofilter.createReport(fileArr[i].getName(),
		 * fileArr[i].getParent(), "Modified"); } else {
		 * if(fileArr[i].lastModified() == fComp.lastModified()){
		 * System.out.println(fileArr[i].getName()+"\t\t"+"identical"); }else{
		 * System.out.println(fileArr[i].getName()+"\t\t"+ "different");
		 * 
		 * FileDetails fileDetail = new FileDetails();
		 * fileDetail.setFileName(fileArr[i].getName());
		 * fileDetail.setFilePath(fileArr[i].getParent());
		 * fileDetail.setStatus("Modified"); fileDetails.add(fileDetail);
		 * 
		 * // autofilter.createReport(fileArr[i].getName(),
		 * fileArr[i].getParent(), "Modified");
		 * 
		 * } } } } else { if(fileArr[i].isDirectory()) {
		 * traverseDirectory(fileArr[i]); } else {
		 * System.out.println(fileArr[i].
		 * getName()+"\t\t"+"only in "+fileArr[i].getParent()); FileDetails
		 * fileDetail = new FileDetails();
		 * fileDetail.setFileName(fileArr[i].getName());
		 * fileDetail.setFilePath(fileArr[i].getParent());
		 * fileDetail.setStatus("New"); fileDetails.add(fileDetail); } } }
		 * Set<String> set = map.keySet(); Iterator<String> it = set.iterator();
		 * while(it.hasNext()) { String n = it.next(); File fileFrmMap =
		 * map.get(n); map.remove(n); if(fileFrmMap.isDirectory()) {
		 * traverseDirectory(fileFrmMap); } else {
		 * System.out.println(fileFrmMap.getName() +"\t\t"+"only in "+
		 * fileFrmMap.getParent());
		 * 
		 * FileDetails fileDetail = new FileDetails();
		 * fileDetail.setFileName(fileFrmMap.getName());
		 * fileDetail.setFilePath(fileFrmMap.getParent());
		 * fileDetail.setStatus("New"); fileDetails.add(fileDetail);
		 * 
		 * // autofilter.createReport(fileFrmMap.getName(),
		 * fileFrmMap.getParent(), "New"); } }
		 * 
		 * autofilter.createReport(fileDetails);
		 * 
		 * }
		 * 
		 * public void traverseDirectory(File dir) { File[] list =
		 * dir.listFiles(); for(int k=0;k<list.length;k++) {
		 * if(list[k].isDirectory()) { traverseDirectory(list[k]); } else {
		 * System.out.println(list[k].getName() +"\t\t"+"only in "+
		 * list[k].getParent()); } } }
		 * 
		 * public String checksum(File file) { try { InputStream fin = new
		 * FileInputStream(file); java.security.MessageDigest md5er =
		 * MessageDigest.getInstance("MD5"); byte[] buffer = new byte[1024]; int
		 * read; do { read = fin.read(buffer); if (read > 0)
		 * md5er.update(buffer, 0, read); } while (read != -1); fin.close();
		 * byte[] digest = md5er.digest(); if (digest == null) return null;
		 * String strDigest = "0x"; for (int i = 0; i < digest.length; i++) {
		 * strDigest += Integer.toString((digest[i] & 0xff) + 0x100,
		 * 16).substring(1).toUpperCase(); } return strDigest; } catch
		 * (Exception e) { return null; }
		 */}

}
