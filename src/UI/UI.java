package UI;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JLabel;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.filechooser.FileNameExtensionFilter;

import Uploader.ApplicationUtil;
import Uploader.Unzipper;
import Uploader.UploadToSFTP;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UI extends JFrame implements Runnable{
	private JTextField zipPath;
	private JTextField path;
	public static JTextArea status;
	final JCheckBox googleIndia;
	final JCheckBox youTube;
	final JCheckBox google;
	JButton upload;
	public static JLabel totalUploadLabel;
	public static JLabel error;
	JLabel uploadStatus;
	static UI frame;
	public static String msg="";
	public static int errorCount=0;
	public static int totalUpload=0;
	public static void main(String[] args) {
	frame = new UI();

	}

	public UI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(300, 200, 733, 520);
		getContentPane().setLayout(null);

		JButton buttonSelectZip = new JButton("Select Zip");
		buttonSelectZip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String initialPath = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(initialPath);
				// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Zip", "zip");
				fc.setFileFilter(filter);
				int result = fc.showOpenDialog(null);
				switch (result) {
				case JFileChooser.APPROVE_OPTION:
					File file = fc.getSelectedFile();
					zipPath.setText(file.toString());
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}

			}
		});
		buttonSelectZip.setBounds(115, 118, 127, 25);
		getContentPane().add(buttonSelectZip);

		zipPath = new JTextField();
		zipPath.setFont(new Font("Serif", Font.BOLD, 13));
		zipPath.setBounds(254, 117, 348, 27);
		getContentPane().add(zipPath);
		zipPath.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 733, 70);
		panel.setLayout(null);
		getContentPane().add(panel);

		JLabel lblDiscas = new JLabel("DiSCAS");
		lblDiscas.setFont(new Font("Dialog", Font.BOLD, 24));
		lblDiscas.setForeground(Color.WHITE);
		lblDiscas.setBounds(331, 12, 196, 46);
		panel.add(lblDiscas);

		JButton buttonFS = new JButton("Select Folder");
		buttonFS.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String initialPath = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(initialPath);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = fc.showOpenDialog(null);
				switch (result) {
				case JFileChooser.APPROVE_OPTION:
					File file = fc.getSelectedFile();
					path.setText(file.toString());
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}

			}
		});
		buttonFS.setBounds(115, 155, 127, 25);
		getContentPane().add(buttonFS);

		path = new JTextField();
		path.setFont(new Font("Serif", Font.BOLD, 13));
		path.setColumns(10);
		path.setBounds(254, 154, 348, 27);
		getContentPane().add(path);

		youTube = new JCheckBox("YouTube");
		youTube.setBounds(354, 206, 99, 23);
		getContentPane().add(youTube);

		google = new JCheckBox("Google");
		google.setBounds(265, 206, 83, 23);
		getContentPane().add(google);

		googleIndia = new JCheckBox("Google India");
		googleIndia.setBounds(457, 206, 145, 23);
		getContentPane().add(googleIndia);

		upload = new JButton("Upload");
		upload.setBounds(262, 259, 117, 25);

		getContentPane().add(upload);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 340, 709, 149);
		getContentPane().add(scrollPane);
		
		status = new JTextArea();
		status.setForeground(new Color(128, 128, 128));
		status.setFont(new Font("Serif", Font.BOLD, 13));
		scrollPane.setViewportView(status);
		status.setEditable(false);
		
		uploadStatus = new JLabel("Status");
		uploadStatus.setBounds(21, 320, 290, 15);
		getContentPane().add(uploadStatus);
		
		JButton clear = new JButton("Clear");
		clear.setFont(new Font("Dialog", Font.BOLD, 11));
		clear.setForeground(new Color(0, 0, 0));
		clear.setBackground(Color.LIGHT_GRAY);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				status.setText("");
			}
		});
		clear.setBounds(653, 319, 68, 16);
		getContentPane().add(clear);
		
		error = new JLabel("Error: 0");
		error.setBounds(532, 320, 103, 15);
		getContentPane().add(error);
		
		totalUploadLabel = new JLabel("Total Upload: 0");
		totalUploadLabel.setBounds(265, 320, 164, 15);
		getContentPane().add(totalUploadLabel);
		upload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Thread t=new Thread(frame);
				t.start();
			}
		});

		setTitle("Discas");
		setVisible(true);
	}
	public void run()
	{
		totalUpload=0;
		errorCount=0;
		
		uploadStatus.setText("Status: in progress..");
		totalUploadLabel.setText("Total Upload: "+totalUpload);
		error.setText("Error: "+errorCount);
		boolean isSelectedUploader = false;
		String[] uploadFor = new String[50];
		int index = 0;
		if (google.isSelected()) {
			uploadFor[index] = new String("Google");
			isSelectedUploader = true;
			index++;
		}
		if (youTube.isSelected()) {
			uploadFor[index] = new String("YouTube");
			isSelectedUploader = true;
			index++;
		}
		if (googleIndia.isSelected()) {
			uploadFor[index] = new String("GoogleIndia");
			isSelectedUploader = true;
			index++;
		}
		if (!isSelectedUploader) {

			JOptionPane.showMessageDialog(null,
					"Please Select The Upload For", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			upload.setEnabled(false);
			String zip = zipPath.getText();
			String destination = "/tmp/ddexTool/";
			Unzipper uz = new Unzipper(zip, destination);
			uz.extract();

			String folderPath = path.getText();// Resource Folder

			UploadToSFTP uploadToSFTP = new UploadToSFTP();
			File sourceXMLFile = new File("/tmp/ddexTool/");
	
			File destinationFile = new File(folderPath); 
					
			String sourceXMLFileName = "";
			String destinationFileName = "";
			File copyXMLSourceToDestination = null;
			for (int i=0;i<index;i++) {
				
				for (File file : destinationFile.listFiles()) {
					uploadStatus.setText("Status: Uploading on "+uploadFor[i]);
					if (file.isDirectory()) {
						sourceXMLFile = new File("/tmp/ddexTool/"+uploadFor[i]+"/"); 
						destinationFileName = file.getName();
						for (File source : sourceXMLFile.listFiles()) {
							sourceXMLFileName = source.getName().substring(0,source.getName().lastIndexOf("."));
							
							if (sourceXMLFileName.equals(destinationFileName)) {
								copyXMLSourceToDestination = new File(file.getAbsolutePath() + "/"+ source.getName());
								
								if (copyXMLSourceToDestination.exists()) {
									copyXMLSourceToDestination.delete();
								}
								File createBatchFile;
								
								try {
									copyXMLSourceToDestination.createNewFile();
									int readXML;
									FileOutputStream xmlOutFile = new FileOutputStream(copyXMLSourceToDestination);
									FileInputStream xmlFile=new FileInputStream(source);	
									while ((readXML = xmlFile.read()) != -1) {
										xmlOutFile.write(readXML);
									}
										
									createBatchFile = new File(file.getAbsolutePath()+ "/BatchComplete_"+ ApplicationUtil.getRemoteFolderName());
									if (createBatchFile.exists()) {
										createBatchFile.delete();
									}
									createBatchFile.createNewFile();
									uploadToSFTP.setUploadForConfigration(uploadFor[i]);
									
									uploadToSFTP.transferToSFTP(file.getAbsolutePath());
									
									uploadToSFTP.transferToSFTP(file.getAbsolutePath() + "/resources");
									createBatchFile.delete();
									copyXMLSourceToDestination.delete();
								} catch (Exception e) {
									JOptionPane.showMessageDialog(null,e.getMessage());
								}

							}
						}
					}
				}
			
			}
			upload.setEnabled(true);
			error.setText("Error: "+errorCount);
			totalUploadLabel.setText("Total Upload: "+totalUpload);
			uploadStatus.setText("Upload Status: Finish");
		}
	}
}
