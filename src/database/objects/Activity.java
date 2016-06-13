package database.objects;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;

public class Activity 
{

	private int activityID;
	private String activityName;
	private File activityFile;
	private Timestamp activityTimeStamp;
	private Date activitDeadline;
	private String activityFileName;
	
	public Activity() 
	{
		
	}

	public Activity(int activityID, String activityName, File activityFile, Timestamp activityTimeStamp,
			Date activitDeadline, String activityFileName) 
	{
		this.activityID = activityID;
		this.activityName = activityName;
		this.activityFile = activityFile;
		this.activityTimeStamp = activityTimeStamp;
		this.activitDeadline = activitDeadline;
		this.activityFileName = activityFileName;
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
	
	public Date getActivitDeadline() 
	{
		return activitDeadline;
	}
	
	public void setActivitDeadline(Date activitDeadline) 
	{
		this.activitDeadline = activitDeadline;
	}
	
	public String getActivityFileName() {
		return activityFileName;
	}
	
	public void setActivityFileName(String activityFileName) 
	{
		this.activityFileName = activityFileName;
	}
}
