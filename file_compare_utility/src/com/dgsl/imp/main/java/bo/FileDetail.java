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
	private long fileSize;
	private String modifiedDate;
	private String createdDate;
	private String filePath;
	private String status;
	private List<CompareResults> compareResults;
	private String fileType;
	
	

	public FileDetail() {

	}

	public FileDetail(String fileName, long fileSize, String modifiedDate,
			String createdDate, String filePath, String status,
			List<CompareResults> compareResults, String fileType) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.modifiedDate = modifiedDate;
		this.createdDate = createdDate;
		this.filePath = filePath;
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
	
	

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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
	

