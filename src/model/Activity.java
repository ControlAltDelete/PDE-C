package model;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;

import service.ClientService;
import service.FileManipulation;

public class Activity 
{

	private int activityID;
	private String activityName;
	private File activityFile;
	private Timestamp activityTimeStamp;
	private Date activitDeadline;
	private String activityFilename;
	
	public Activity() 
	{
		
	}

	public Activity(int activityID, String activityName, File activityFile, Timestamp activityTimeStamp,
			Date activitDeadline, String activityFilename) 
	{
		this.activityID = activityID;
		this.activityName = activityName;
		this.activityFile = activityFile;
		this.activityTimeStamp = activityTimeStamp;
		this.activitDeadline = activitDeadline;
		this.activityFilename = activityFilename;
	}
	
	public int getActivityID() 
	{
		return activityID;
	}
	
	public void setActivityID(int activityID) 
	{
		this.activityID = activityID;
	}
	
	public String getActivityName() 
	{
		return activityName;
	}
	
	public void setActivityName(String activityName) 
	{
		this.activityName = activityName;
	}
	
	public File getActivityFile()
	{
		return activityFile;
	}
	
	public void setActivityFile(File activityFile) 
	{
		this.activityFile = activityFile;
	}
	
	public Timestamp getActivityTimeStamp() 
	{
		return activityTimeStamp;
	}
	
	public void setActivityTimeStamp(Timestamp activityTimeStamp) 
	{
		this.activityTimeStamp = activityTimeStamp;
	}
	
	public Date getActivityDeadline() 
	{
		return activitDeadline;
	}
	
	public void setActivityDeadline(Date activitDeadline) 
	{
		this.activitDeadline = activitDeadline;
	}
	
	public String getActivityFilename() {
		return activityFilename;
	}
	
	public void setActivityFilename(String activityFilename) 
	{
		this.activityFilename = activityFilename;
	}
	
	public void sendData() throws IOException
	{
	  FileManipulation fm = new FileManipulation();
	  String toBeSent = "activity," + this.activityID + "," + this.activityName + "," + fm.convertToBinary(this.activityFile) + "," + this.activityTimeStamp + "," + this.activitDeadline + "," + this.activityFilename;
	  
	  ClientService client = ClientService.getClientService();
	  client.sendDataToServer(toBeSent);
	}
}