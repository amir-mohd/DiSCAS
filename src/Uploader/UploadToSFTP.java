package Uploader;
import UI.UI;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;




import com.jcraft.jsch.UserInfo;

import org.apache.commons.vfs.*;
import org.apache.commons.vfs.impl.StandardFileSystemManager;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;

public class UploadToSFTP {

	public String hostName;
	public String userName;
	public String password;
	public String port;
	public String remoteFilePath;
	public String keyFilePath;
	public static int progressCount=0;
	public boolean transferToSFTP(String localFilePath) {

		upload(hostName, userName, password, localFilePath, port,remoteFilePath, keyFilePath);
		return true;
	}

	public static void upload(String hostName, String username,String password, String localFilePath, String port,String remoteFilePath, String keyFilePath) {

		File uploadDir = new File(localFilePath);
		String resourceParentFolderName;
		StandardFileSystemManager manager = new StandardFileSystemManager();
	/*	int totalFileInFolder=0;
		int progressCount=0;
		totalFileInFolder = uploadDir.listFiles().toString().length();
		UI.progressBar.setMaximum(totalFileInFolder);*/
		for (File file : uploadDir.listFiles()) {
			try {
				
				
				UI.defaultTableModel.addRow(new Object[] {
						UploadMessageStatus.uploadFor,
						UploadMessageStatus.processFolderName,
						"",
						UploadMessageStatus.userName,
						UploadMessageStatus.hostName
				});
				
				
				UI.status.setText(UI.status.getText()+"\n"+file.getName()+" Under Process for "+username+"\n");
				UI.defaultTableModel.setValueAt(file.getName(), UploadMessageStatus.totalProcessFile, 2);
				UI.defaultTableModel.setValueAt("In Process", UploadMessageStatus.totalProcessFile, 5);
				manager.init();
				FileObject localFile = manager.resolveFile(file.getAbsolutePath());
				FileObject remoteFile=null;
			
				if (uploadDir.getName().equals("resources")) {
					
					resourceParentFolderName = new File(uploadDir.getParent()).getName();
			
					remoteFile = manager.resolveFile(createConnectionString(hostName,username,password,port,
									remoteFilePath + "/"
											+ resourceParentFolderName + "/"
											+ uploadDir.getName() + "/"
											+ file.getName()),
							createDefaultOptions(keyFilePath));
				} 
				else if(!UploadMessageStatus.isResourceFolderCreated) 
				{
					
					remoteFile = manager.resolveFile(
							createConnectionString(hostName, username,password, port,remoteFilePath + "/" + uploadDir.getName()+ "/" + file.getName()),
							createDefaultOptions(keyFilePath));
						
				}
				else if(UploadMessageStatus.isResourceFolderCreated&&file.getName().equals("resources")){
					UI.defaultTableModel.removeRow(UploadMessageStatus.totalProcessFile--);
					continue;
				}
				else{
					remoteFile = manager.resolveFile(
							createConnectionString(hostName, username,password, port,remoteFilePath + "/" + uploadDir.getName()+ "/" + file.getName()),
							createDefaultOptions(keyFilePath));
				}
				remoteFile.createFolder();
				remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
				UI.defaultTableModel.setValueAt("Success", UploadMessageStatus.totalProcessFile, 5);
				UI.status.setText(UI.status.getText()+"\n"+file.getName()+" Successfull Upload on "+username+"\n");
				UploadMessageStatus.totalUpload++;
				UI.totalUploadLabel.setText("Total Upload: "+UploadMessageStatus.totalUpload);
			} catch (Exception e) {
				UI.defaultTableModel.setValueAt("Error:--> "+e.getMessage(), UploadMessageStatus.totalProcessFile, 5);
				UploadMessageStatus.errorCount++;
				UI.error.setText("Error: "+UploadMessageStatus.errorCount);
				UI.status.setText(UI.status.getText()+"\n"+e.getMessage()+"\n");

			} finally {
				manager.close();
				UploadMessageStatus.totalProcessFile++;
				UI.progressBar.setValue(++progressCount);
			}
		}
	}

	public static String createConnectionString(String hostName,String username, String password, String port, String remoteFilePath) {
		return "sftp://" + username + "@" + hostName+":"+port + "/" + remoteFilePath;
	}

	public static FileSystemOptions createDefaultOptions(String keyFilePath) throws FileSystemException {

        FileSystemOptions opts = new FileSystemOptions();

        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
        File[] keyFile = new File[1];
        keyFile[0]=new File(keyFilePath);
        
        SftpFileSystemConfigBuilder.getInstance().setIdentities(opts,keyFile );
  
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
       
        SftpFileSystemConfigBuilder.getInstance().setUserInfo(opts, new SftpUserInfo());
       
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 5*60*1000);//10 Second
        return opts;
    }

	public void setUploadForConfigration(String uploadFor) throws Exception {
		File curDir = new File("/tmp/dd");
		FileReader file = new FileReader(curDir.getAbsolutePath()+File.separator + "sftp-file");
		Properties prop = new Properties();
		prop.load(file);
		if (uploadFor.equals("Google")) {
			hostName = prop.getProperty("googleHostName");
			userName = prop.getProperty("googleUserName");
			password = prop.getProperty("googlePassword");
			port = prop.getProperty("googlePort");
			remoteFilePath = prop.getProperty("googleRemoteFilePath") + "/"
					+ ApplicationUtil.getRemoteFolderName();
			keyFilePath = new File("/tmp/dd").getAbsolutePath() +File.separator
					+ prop.getProperty("googleKeyFileName");

		} 
	}
	public  void downloadKey(String fileName){
		
		 String hostName="qa3.intelligrape.net";
		 String userName="discas_google";
		 String password="igdefault";
		 String port="22";
		 String remoteFilePath="home/discas_google/";
		StandardFileSystemManager manager = new StandardFileSystemManager();
			File localTmpFile=new File("/tmp/dd/"+fileName);
			if(localTmpFile.exists()){
				try {
					localTmpFile.createNewFile();
				} catch (IOException e) {
				
					e.printStackTrace();
				}
			}
		 	String localFilePath=localTmpFile.getAbsolutePath();
		 	String keyFilePath=new File("").getAbsolutePath()+"/DiSCASConfig/discas_google.pem";
		 
	        try {
	            manager.init();

	            FileObject localFile = manager.resolveFile(localFilePath);
	    
	            FileObject remoteFile = manager.resolveFile(createConnectionString(hostName, userName, password, port ,remoteFilePath)+"/"+fileName, createDefaultOptions(keyFilePath));

	            localFile.copyFrom(remoteFile, Selectors.SELECT_SELF);

	            System.out.println("File download success");
	        } catch (Exception e) {
	            System.out.println(e.getMessage());
	        } finally {
	            manager.close();
	        }	
		}
}

class SftpUserInfo implements UserInfo {

	@Override
	public String getPassphrase() {
		return "";
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public boolean promptPassphrase(String arg) {
		return true;
	}

	@Override
	public boolean promptYesNo(String s) {
		return false;
	}

	@Override
	public void showMessage(String s) {
			
	}

	public boolean promptPassword(String arg) {
		return false;
	}
}