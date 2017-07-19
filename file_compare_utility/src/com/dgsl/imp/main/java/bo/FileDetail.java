/**
 * 
 */
package com.dgsl.imp.main.java.bo;

import java.io.File;
import java.util.List;

import com.dgsl.imp.main.java.service.result.CompareResults;

/**
 * @author subhasis.swain
 *
 */
public class FileDetail {

	private String fileName;
	private String filePath;
	private long fileSize;
	private String status;
	private List<CompareResults> compareResults;
	private String fileType;

	public FileDetail() {

	}

	public FileDetail(String fileName, String filePath, long fileSize,
			String status, List<CompareResults> compareResults, String fileType) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.fileSize = fileSize;
		this.status = status;
		this.compareResults = compareResults;
		this.fileType = fileType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<CompareResults> getCompareResults() {
		return compareResults;
	}

	public void setCompareResults(List<CompareResults> compareResults) {
		this.compareResults = compareResults;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFileExtension(File file) {
	        String fileName = file.getName();
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0 && file.isFile())
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "NA";
	    }

}
	

