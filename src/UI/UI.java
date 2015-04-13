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
import java.awt.Dimension;
import java.awt.Toolkit;

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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Uploader.ApplicationUtil;
import Uploader.Unzipper;
import Uploader.UploadMessageStatus;
import Uploader.UploadToSFTP;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UI extends JFrame implements Runnable {
	private JTextField zipPath;
	private JTextField path;
	public static JTextArea status;
	final JCheckBox googleIndia;
	final JCheckBox youTube;
	final JCheckBox google;
	JButton upload;
	JButton clear;
	public static JLabel totalUploadLabel;
	public static JLabel error;
	JLabel uploadStatus;
	public static JProgressBar progressBar;
	public static JLabel totalUploadForLabel;
	static UI frame;
	public static DefaultTableModel defaultTableModel;
	public static String msg = "";
	public static int errorCount = 0;
	public static int totalUpload = 0;
	private JTable table;
	private JLabel lblCopyright;
	private JLabel label;
	private JButton btnSave;
	public static JLabel dateAndTime;
	int screenWidth;
	int screenHeight;
	public static void main(String[] args) {
		frame = new UI();

	}

	public UI() {

		
		Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth=(int)dimension.getWidth();
		screenHeight=(int)dimension.getHeight();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(200, 50, 900, 540);
		getContentPane().setLayout(null);
		JButton buttonSelectZip = new JButton("Select Zip");
		buttonSelectZip.setForeground(Color.DARK_GRAY);
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
					zipPath.setForeground(Color.black);
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}

			}
		});
		buttonSelectZip.setBounds(39, 103, 127, 25);
		getContentPane().add(buttonSelectZip);

		zipPath = new JTextField("Choose ZIP File...");
		zipPath.setForeground(Color.LIGHT_GRAY);
		zipPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
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
					zipPath.setForeground(Color.black);
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}

			}
		});

		zipPath.setFont(new Font("Serif", Font.BOLD, 13));
		zipPath.setBounds(178, 102, 348, 27);
		getContentPane().add(zipPath);
		zipPath.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBounds(0, 0, 927, 70);
		panel.setLayout(null);
		getContentPane().add(panel);

		JLabel lblDiscas = new JLabel("DiSCAS");
		lblDiscas.setFont(new Font("Dialog", Font.BOLD, 24));
		lblDiscas.setForeground(Color.WHITE);
		lblDiscas.setBounds(392, 12, 196, 46);
		panel.add(lblDiscas);

		JButton buttonFS = new JButton("Select Folder");
		buttonFS.setForeground(Color.DARK_GRAY);
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
					path.setForeground(Color.black);
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}

			}
		});
		buttonFS.setBounds(39, 140, 127, 25);
		getContentPane().add(buttonFS);

		path = new JTextField("Select Resources Folder");
		path.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String initialPath = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(initialPath);
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int result = fc.showOpenDialog(null);
				switch (result) {
				case JFileChooser.APPROVE_OPTION:
					File file = fc.getSelectedFile();
					path.setText(file.toString());
					path.setForeground(Color.black);
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}

			}
		});
		path.setForeground(Color.LIGHT_GRAY);
		path.setFont(new Font("Serif", Font.BOLD, 13));
		path.setColumns(10);
		path.setBounds(178, 139, 348, 27);
		getContentPane().add(path);

		youTube = new JCheckBox("YouTube");
		youTube.setForeground(Color.DARK_GRAY);
		youTube.setBounds(267, 191, 99, 23);
		// getContentPane().add(youTube);

		google = new JCheckBox("Google");
		google.setForeground(Color.DARK_GRAY);
		google.setBounds(178, 191, 83, 23);
		getContentPane().add(google);

		googleIndia = new JCheckBox("Google India");
		googleIndia.setForeground(Color.DARK_GRAY);
		googleIndia.setBounds(370, 191, 145, 23);
		// getContentPane().add(googleIndia);

		upload = new JButton("Upload");
		upload.setForeground(Color.DARK_GRAY);
		upload.setBounds(178, 234, 99, 25);

		getContentPane().add(upload);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 340, 709, 149);
		// getContentPane().add(scrollPane);

		status = new JTextArea();
		status.setForeground(new Color(128, 128, 128));
		status.setFont(new Font("Serif", Font.BOLD, 13));
		scrollPane.setViewportView(status);
		status.setEditable(false);

		uploadStatus = new JLabel("Status");
		uploadStatus.setForeground(Color.DARK_GRAY);
		uploadStatus.setBounds(22, 444, 290, 15);
		getContentPane().add(uploadStatus);

		clear = new JButton("Clear");
		clear.setFont(new Font("Dialog", Font.BOLD, 11));
		clear.setForeground(new Color(0, 0, 0));
		clear.setBackground(Color.LIGHT_GRAY);
		clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				status.setText("");
				for (int i = 0; i < defaultTableModel.getRowCount(); i++) {
					for (int j = 0; j < defaultTableModel.getRowCount(); j++) {
						defaultTableModel.removeRow(j);
					}

				}
				progressBar.setValue(0);
				uploadStatus.setText("Status: ");
				totalUploadForLabel.setText("");
				error.setText("Error: 0");
			}
		});
		clear.setBounds(811, 444, 77, 15);
		getContentPane().add(clear);

		error = new JLabel("Error: 0");
		error.setForeground(Color.DARK_GRAY);
		error.setBounds(538, 444, 103, 15);
		getContentPane().add(error);

		totalUploadLabel = new JLabel("Total Upload: 0");
		totalUploadLabel.setForeground(Color.DARK_GRAY);
		totalUploadLabel.setBounds(319, 444, 164, 15);
		getContentPane().add(totalUploadLabel);

		defaultTableModel = new DefaultTableModel();
		// defaultTableModel.addColumn("SNO");
		defaultTableModel.addColumn("Upload For");
		defaultTableModel.addColumn("Process Folder");

		defaultTableModel.addColumn("File Name");
		defaultTableModel.addColumn("User Name");
		defaultTableModel.addColumn("Host");
		defaultTableModel.addColumn("Current Status");

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 296, 876, 146);
		getContentPane().add(scrollPane_1);

		table = new JTable(defaultTableModel);
		table.setFont(new Font("Serif", Font.BOLD, 12));
		table.setRowHeight(30);
		table.setEnabled(false);
		scrollPane_1.setViewportView(table);

		progressBar = new JProgressBar();
		progressBar.setBounds(644, 444, 148, 14);

		progressBar.setBorderPainted(true);
		getContentPane().add(progressBar);

		totalUploadForLabel = new JLabel("              ");
		totalUploadForLabel.setForeground(Color.GRAY);
		totalUploadForLabel.setFont(new Font("Dialog", Font.BOLD, 11));
		totalUploadForLabel.setBounds(691, 458, 70, 15);
		getContentPane().add(totalUploadForLabel);

		JPanel footer = new JPanel();
		footer.setBounds(0, 503, 927, 27);
		footer.setLayout(null);
		footer.setBackground(Color.DARK_GRAY);
		getContentPane().add(footer);

		lblCopyright = new JLabel("POWERED BY:- TTN DIGITAL");
		lblCopyright.setFont(new Font("Dialog", Font.ITALIC, 10));
		lblCopyright.setBounds(740, 5, 200, 15);
		lblCopyright.setForeground(Color.white);
		footer.add(lblCopyright);

		label = new JLabel("All Right Reserved");
		label.setFont(new Font("Dialog", Font.ITALIC, 10));
		label.setBounds(12, 5, 200, 15);
		footer.add(label);
		label.setForeground(Color.WHITE);

		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String initialPath = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(initialPath);

				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"csv", "csv");
				fc.setFileFilter(filter);
				int result = fc.showSaveDialog(null);
				int option = 0;
				switch (result) {
				case JFileChooser.APPROVE_OPTION:
					File file = fc.getSelectedFile();
					if (file.exists()) {
						option = JOptionPane.showConfirmDialog(null,
								"This file is Exist ,Overwrite it..?",
								"Message", JOptionPane.YES_NO_OPTION);
						if(option==0){
							file.delete();
							try{
								file.createNewFile();
							}catch(Exception e){}
						}
						
					}
					if (option == 0) {
						
						FileWriter csvOutputFile;
						try {
							System.out.println(file.getAbsolutePath());
							if(!((file.getAbsolutePath().lastIndexOf(".csv")-file.getAbsolutePath().length()+4)==0)){
								System.out.println(file.getAbsolutePath().lastIndexOf(".csv")-file.getAbsolutePath().length());
								String fileName=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("/")+1);
								file=new File(file.getParent()+File.separator+fileName+".csv");
								file.createNewFile();
							}
							
							csvOutputFile = new FileWriter(file.getAbsolutePath(),true);
							csvOutputFile.write(new String("Upload For,Process Folder,File Name,User Name,Host,Status\n"));
							for (int i = 0; i < table.getRowCount(); i++) {
					
								csvOutputFile.write(defaultTableModel.getValueAt(i, 0).toString() + ","
										+defaultTableModel.getValueAt(i, 1).toString()+","
										+defaultTableModel.getValueAt(i, 2).toString()+","
										+defaultTableModel.getValueAt(i, 3).toString()+","
										+defaultTableModel.getValueAt(i, 4).toString()+","
										+defaultTableModel.getValueAt(i, 5).toString()+"\n");
							}
							csvOutputFile.close();
						} catch (Exception e) {
							System.out.print(e.getMessage());
						}
						
					}
					break;
				case JFileChooser.CANCEL_OPTION:
				case JFileChooser.ERROR_OPTION:
					break;
				}
			}
		});
		btnSave.setToolTipText("Save The Log");
		btnSave.setForeground(Color.WHITE);
		btnSave.setBackground(Color.GRAY);
		btnSave.setBounds(811, 268, 77, 25);
		getContentPane().add(btnSave);
		
		dateAndTime = new JLabel("date");
		dateAndTime.setForeground(Color.DARK_GRAY);
		dateAndTime.setFont(new Font("Serif", Font.BOLD, 18));
		dateAndTime.setBounds(595, 79, 305, 70);
		getContentPane().add(dateAndTime);
		new Thread(new DateAndTime()).start();;
		upload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Thread t = new Thread(frame);
				t.start();
			}
		});

		setTitle("Discas");
		setVisible(true);
	}

	public void run() {
		UploadMessageStatus.totalUpload = 0;
		UploadMessageStatus.errorCount = 0;

		uploadStatus.setText("Status: in progress..");
		totalUploadLabel.setText("Total Upload: "
				+ UploadMessageStatus.totalUpload);
		error.setText("Error: " + UploadMessageStatus.errorCount);
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

			JOptionPane.showMessageDialog(null, "Please Select The Upload For",
					"Message", JOptionPane.INFORMATION_MESSAGE);
		} else {
			clear.setEnabled(false);
			upload.setEnabled(false);
			progressBar.setValue(0);
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
			for (int i = 0; i < index; i++) {
			
				uploadToSFTP.progressCount = 0;
				progressBar.setValue(0);
				for (File file : destinationFile.listFiles()) {
					UploadMessageStatus.uploadFor = uploadFor[i];

					totalUploadForLabel.setText((i + 1) + " of " + index);
					uploadStatus.setText("Status: Uploading on "
							+ UploadMessageStatus.uploadFor);
					if (file.isDirectory()) {

						sourceXMLFile = new File("/tmp/ddexTool/"
								+ uploadFor[i] + "/");
						destinationFileName = file.getName();
						for (File source : sourceXMLFile.listFiles()) {
							sourceXMLFileName = source.getName().substring(0,
									source.getName().lastIndexOf("."));

							if (sourceXMLFileName.equals(destinationFileName)) {
								copyXMLSourceToDestination = new File(
										file.getAbsolutePath() + "/"
												+ source.getName());

								if (copyXMLSourceToDestination.exists()) {
									copyXMLSourceToDestination.delete();
								}
								File createBatchFile;

								try {
									copyXMLSourceToDestination.createNewFile();
									int readXML;
									FileOutputStream xmlOutFile = new FileOutputStream(
											copyXMLSourceToDestination);
									FileInputStream xmlFile = new FileInputStream(
											source);
									while ((readXML = xmlFile.read()) != -1) {
										xmlOutFile.write(readXML);
									}

									uploadToSFTP
											.setUploadForConfigration(uploadFor[i]);

									UploadMessageStatus.hostName = uploadToSFTP.hostName;
									UploadMessageStatus.userName = uploadToSFTP.userName;
									UploadMessageStatus.processFolderName = file
											.getName();

									progressBar
											.setMaximum(getTotalResourceForProgressBar(destinationFile));
									UploadMessageStatus.isResourceFolderCreated = false;
									uploadToSFTP.transferToSFTP(file
											.getAbsolutePath());
									copyXMLSourceToDestination.delete();
									UploadMessageStatus.isResourceFolderCreated = true;
									uploadToSFTP.transferToSFTP(file
											.getAbsolutePath() + "/resources");
									createBatchFile = new File(
											file.getAbsolutePath()
													+ "/BatchComplete_"
													+ ApplicationUtil
															.getRemoteFolderName());
									if (createBatchFile.exists()) {
										createBatchFile.delete();
									}

									createBatchFile.createNewFile();

									uploadToSFTP.transferToSFTP(file
											.getAbsolutePath());

									createBatchFile.delete();

								} catch (Exception e) {
									JOptionPane.showMessageDialog(null,
											e.getMessage());
								}

							}
						}
					}
				}
				uploadToSFTP.progressCount = 0;
			}
			upload.setEnabled(true);
			clear.setEnabled(true);

			error.setText("Error: " + UploadMessageStatus.errorCount);
			totalUploadLabel.setText("Total Upload: "
					+ UploadMessageStatus.totalUpload);
			UploadMessageStatus.currentStaus = "Finish";
			uploadStatus.setText("Upload Status: "
					+ UploadMessageStatus.currentStaus);
		}
	}

	public static int getTotalResourceForProgressBar(File file) {
		int totalFile = 0;
		int totalTempFile = 0;
		File tempFile;
		try {
			for (File f : file.listFiles()) {
				totalTempFile += 2;
				totalFile = totalFile
						+ countFile(new File(f.getAbsolutePath()
								+ File.separator + "resources"));
				// JOptionPane.showMessageDialog(null,f.getAbsolutePath()+File.separator+"resources");
			}
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return totalFile + totalTempFile;
	}

	public static int countFile(File file) {
		int totalFile = 0;
		try {
			for (File iterate : file.listFiles()) {
				totalFile++;
			}
		} catch (Exception e) {

		}
		return totalFile;
	}
}
class DateAndTime implements Runnable{
	public void run(){
		try{
			while(true){
				UI.dateAndTime.setText(new Date().toLocaleString());
				Thread.sleep(1000);
			}
		
		}catch(Exception e){}
	}
}