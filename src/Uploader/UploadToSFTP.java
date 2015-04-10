package Uploader;
import UI.UI;
import java.io.File;

import java.io.FileReader;
import java.util.Properties;



import com.jcraft.jsch.UserInfo;

import org.apache.commons.vfs.*;
import org.apache.commons.vfs.impl.StandardFileSystemManager;
import org.apache.commons.vfs.provider.sftp.SftpFileSystemConfigBuilder;

public class UploadToSFTP {

	String hostName;
	String userName;
	String password;
	String port;
	String remoteFilePath;
	String keyFilePath;

	public boolean transferToSFTP(String localFilePath) {

		upload(hostName, userName, password, localFilePath, port,remoteFilePath, keyFilePath);
		return true;
	}

	public static void upload(String hostName, String username,String password, String localFilePath, String port,String remoteFilePath, String keyFilePath) {

		File uploadDir = new File(localFilePath);
		String resourceParentFolderName;
		StandardFileSystemManager manager = new StandardFileSystemManager();
	
		for (File file : uploadDir.listFiles()) {
			try {
				UI.status.setText(UI.status.getText()+"\n"+file.getName()+" Under Process for "+username+"\n");
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
				} else {
					
					remoteFile = manager.resolveFile(
							createConnectionString(hostName, username,password, port,remoteFilePath + "/" + uploadDir.getName()+ "/" + file.getName()),
							createDefaultOptions(keyFilePath));
				
				}
				
				remoteFile.createFolder();
				remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
				UI.status.setText(UI.status.getText()+"\n"+file.getName()+" Successfull Upload on "+username+"\n");
				UI.totalUpload++;
				UI.totalUploadLabel.setText("Total Upload: "+UI.totalUpload);
			} catch (Exception e) {
				UI.errorCount++;
				UI.error.setText("Error: "+UI.errorCount);
				UI.status.setText(UI.status.getText()+"\n"+e.getMessage()+"\n");

			} finally {
				manager.close();
			}
		}
	}

	public static String createConnectionString(String hostName,String username, String password, String port, String remoteFilePath) {
		return "sftp://" + username + "@" + hostName + "/" + remoteFilePath;
	}

	public static FileSystemOptions createDefaultOptions(String keyFilePath) throws FileSystemException {

        FileSystemOptions opts = new FileSystemOptions();

        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
        File[] keyFile = new File[1];
        keyFile[0]=new File(keyFilePath);
        
        SftpFileSystemConfigBuilder.getInstance().setIdentities(opts,keyFile );
  
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
       
        SftpFileSystemConfigBuilder.getInstance().setUserInfo(opts, new SftpUserInfo());
       
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 10000);//10 Second
        return opts;
    }

	public void setUploadForConfigration(String uploadFor) throws Exception {
		File curDir = new File("");
		FileReader file = new FileReader(curDir.getAbsolutePath() + File.separator+"DiSCASConfig"+File.separator + "sftp-file");
		Properties prop = new Properties();
		prop.load(file);
		if (uploadFor.equals("Google")) {
			hostName = prop.getProperty("googleHostName");
			userName = prop.getProperty("googleUserName");
			password = prop.getProperty("googlePassword");
			port = prop.getProperty("googlePort");
			remoteFilePath = prop.getProperty("googleRemoteFilePath") + "/"
					+ ApplicationUtil.getRemoteFolderName();
			keyFilePath = new File("").getAbsolutePath() + File.separator+"DiSCASConfig"+File.separator
					+ prop.getProperty("googleKeyFileName");

		} else if (uploadFor.equals("YouTube")) {
			hostName = prop.getProperty("youTubeHostName");
			userName = prop.getProperty("youTubeUserName");
			password = prop.getProperty("youTubePassword");
			port = prop.getProperty("youTubePort");
			remoteFilePath = prop.getProperty("youTubeRemoteFilePath") + "/"
					+ ApplicationUtil.getRemoteFolderName();
			keyFilePath = new File("").getAbsolutePath() + File.separator+"DiSCASConfig"+File.separator
					+ prop.getProperty("youTubeKeyFileName");

		} else if (uploadFor.equals("GoogleIndia")) {
			hostName = prop.getProperty("googleIndiaHostName");
			userName = prop.getProperty("googleIndiaUserName");
			password = prop.getProperty("googleIndiaPassword");
			port = prop.getProperty("googleIndiaPort");
			remoteFilePath = prop.getProperty("googleIndiaRemoteFilePath")
					+ "/" + ApplicationUtil.getRemoteFolderName();
			keyFilePath = new File("").getAbsolutePath() + File.separator+"DiSCASConfig"+File.separator
					+ prop.getProperty("googleIndiaKeyFileName");
		} else {
			hostName = prop.getProperty("hostName");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			port = prop.getProperty("port");
			remoteFilePath = prop.getProperty("remoteFilePath") + "/"
					+ ApplicationUtil.getRemoteFolderName();
			keyFilePath = new File("").getAbsolutePath() + File.separator+"DiSCASConfig"+File.separator
					+ prop.getProperty("keyFileName");
		}
		// println("Uploading on: "+userName+"@"+hostName+"--------------------");
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