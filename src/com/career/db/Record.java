package com.career.db;

import java.util.Date;

/**
 * Model bean representing DB structure
 * 
 *
 */
public class Record {
	
	
	private long mId;
	private Date mDateTime;
	private String mRecordType;
	private String mJobTitle;
	private String mJobCompany;
	private String mRecruiterName;
	private String mRecruiterCompany;
	private String mJobAddress;
	private String mJobPostcode;
	private String mComments;
	private String mGoalTitle;
	private String mGoalDesc;
	private String mGoalLocation;
	

	
	public String getGoalTitle() {
		return mGoalTitle;
	}
	public void setGoalTitle(String mGoalTitle) {
		this.mGoalTitle = mGoalTitle;
	}
	public String getGoalDesc() {
		return mGoalDesc;
	}
	public void setGoalDesc(String mGoalDesc) {
		this.mGoalDesc = mGoalDesc;
	}
	public String getGoalLocation() {
		return mGoalLocation;
	}
	public void setGoalLocation(String mGoalLocation) {
		this.mGoalLocation = mGoalLocation;
	}
	public String getRecordType() {
		return mRecordType;
	}
	public void setRecordType(String recordType) {
		mRecordType = recordType;
	}
	public String getJobTitle() {
		return mJobTitle;
	}
	public void setJobTitle(String jobTitle) {
		mJobTitle = jobTitle;
	}
	public String getJobCompany() {
		return mJobCompany;
	}
	public void setJobCompany(String jobCompany) {
		mJobCompany = jobCompany;
	}
	public String getRecruiterName() {
		return mRecruiterName;
	}
	public void setRecruiterName(String recruiterName) {
		mRecruiterName = recruiterName;
	}
	public String getRecruiterCompany() {
		return mRecruiterCompany;
	}
	public void setRecruiterCompany(String recruiterCompany) {
		mRecruiterCompany = recruiterCompany;
	}
	public String getComments() {
		return mComments;
	}
	public void setComments(String comments) {
		mComments = comments;
	}
	public long getId() {
		return mId;
	}
	public void setId(long mId) {
		this.mId = mId;
	}
	public String getJobAddress() {
		return mJobAddress;
	}
	public void setJobAddress(String jobAddress) {
		mJobAddress = jobAddress;
	}
	public String getJobPostcode() {
		return mJobPostcode;
	}
	public void setJobPostcode(String jobPostcode) {
		mJobPostcode = jobPostcode;
	}
	public Date getDateTime() {
		return mDateTime;
	}
	public void setDateTime(Date dateTime) {
		mDateTime = dateTime;
	}
}
