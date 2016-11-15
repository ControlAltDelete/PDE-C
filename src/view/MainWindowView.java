package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fife.rsta.ac.java.rjc.parser.Main;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.GutterIconInfo;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;

import controller.EventController;
import model.ErrorMessage;
import model.Feedback;
import service.ClientService;
import service.Parsers;

import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ScrollPaneConstants;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;


public class MainWindowView
{

	private JFrame frame;
	private RSyntaxTextArea editorPane;
	private ArrayList<Integer> breakpoints;
	public static ArrayList<GutterIconInfo> breakpoints2;
	private Path filePath;
	private Path feedbackFilePath;
	private boolean fileModified;
	private final String appName = "PDE-C";
	private String fileName;
	public static JTextArea errorLog;
	public static JTextArea debugLog;
	public static JTextArea feedbackLog;
	private JMenuItem addBreakItem, delBreakItem, delallBreakItem;
	private JButton breakpointButton, delbreakpointButton, delallbreakpointButton;
	
	private static String studentIdNum;
	
	private ArrayList<String> codeHistory = new ArrayList<String>();
	
	// for api purposes
	
	private JToolBar coreToolbar;
	private JMenuBar menuBar;
	private JSplitPane horizontalPane;
	private JSplitPane verticalPane;
	private JScrollPane feedbackScroll;
	private JTabbedPane tabbedHorizontalPane;
	private JTabbedPane tabbedVerticalPane;
	private FeedbackHistory feedbackHistory;
	private static JButton sendButton;
	
	private int fontSize = 16;
	private int minFont = 12;
	private int maxFont = 72;
	private String fontStyle;
	private static MainWindowView instance = null;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args)
//	{
//		EventQueue.invokeLater(new Runnable()
//		{
//			public void run()
//			{
//				try
//				{
//				  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//					MainWindowView window = getInstance();
//					window.frame.setVisible(true);
//				} catch (Exception e)
//				{
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	private MainWindowView()
	{
	  	filePath = null;
		initialize();
	}
	
	public static MainWindowView getInstance()
	{
	  if (instance == null)
	  {
		instance = new MainWindowView();
	  }
	  
	  return instance;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		breakpoints = new ArrayList<Integer>();
		breakpoints2 = new ArrayList<GutterIconInfo>();
		fileModified = false;
		fileName = "new file";
	  	feedbackFilePath = null;
	  	
	  	feedbackScroll = new JScrollPane();
		tabbedVerticalPane = new JTabbedPane();
		feedbackHistory = new FeedbackHistory();
		studentIdNum = "1";
			
		try
		{
		  if (Files.exists(Paths.get("resources/activity.txt")))
		  {
			Files.delete(Paths.get("resources/activity.txt"));
		  }
		}
		
		catch(Exception ex)
		{
		  ex.printStackTrace();
		}
		
		
		frame = new JFrame(appName + " - new file");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
			  int confirmed = JOptionPane.showConfirmDialog(null, 
			        "Are you sure you want to exit the program?", "",
			        JOptionPane.YES_NO_OPTION);

			      if (confirmed == JOptionPane.YES_OPTION) 
			      {
			    	System.exit(0);
			      }
			}
		});
		frame.setBounds(100, 100, 650, 425);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
		
		final JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter cFilter = new FileNameExtensionFilter(
		     "C Source (*.c)", "c");
		fileChooser.setFileFilter(cFilter);
		
		EventController eventController = EventController.getEventController();
        
		editorPane = new RSyntaxTextArea();
		fontStyle = editorPane.getFont().getFamily();
		editorPane.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				fileModified = true;
				if(fileModified)
				{
					frame.setTitle(appName + " - " + fileName + " *");
				}
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				fileModified = true;
				if(fileModified)
				{
					frame.setTitle(appName + " - " + fileName + " *");
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}
		});
		Parsers p = new Parsers();
		
		editorPane.addParser(p);

		editorPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
		editorPane.setCodeFoldingEnabled(true);
		editorPane.setFont(new Font(fontStyle, Font.PLAIN, fontSize));
		RTextScrollPane scrollPane = new RTextScrollPane(editorPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setIconRowHeaderEnabled(true);
		JComponent.setDefaultLocale(null);
		scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.revalidate();
		Gutter gut = scrollPane.getGutter();
		
		Font monospace = new Font(fontStyle, Font.PLAIN, fontSize);
		for(int i = 0; i < gut.getComponentCount(); i++)
		{
			gut.getComponent(i).setFont(monospace);
		}
		gut.setBookmarkingEnabled(true);
		
		coreToolbar = new JToolBar();
		coreToolbar.setFloatable(false);
		coreToolbar.setRollover(true);
		JButton newButton = new JButton("");
		newButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if (editorPane.getText().equals(""))
				{
					editorPane.setText("");
					//eventController.deleteDontTouch();
				}
				
				else
				{
					int confirmed = JOptionPane.showConfirmDialog(null, "Create new file?", "", JOptionPane.YES_NO_OPTION);
				
					if (confirmed == JOptionPane.YES_OPTION) 
					{
						editorPane.setText("");
						//eventController.deleteDontTouch();
						feedbackFilePath = null;
						filePath = null;
						fileName = "new file";
						frame.setTitle(appName + " - " + fileName);
				    	eventController.quietlydeleteallbreakpoint(gut, breakpoints);
				    	errorLog.setText("");
						feedbackHistory.getContainer().removeAll();
						feedbackHistory.updateUI();
					}
				}
			}
		});
		newButton.setToolTipText("New");
		URL newfile = Main.class.getResource("/newFile.png");
		newButton.setIcon(new ImageIcon(newfile));
		newButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JButton openButton = new JButton("");
		openButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			  {
				int confirmed = JOptionPane.showConfirmDialog(null, "Open new file?", "", JOptionPane.YES_NO_OPTION);

			    if (confirmed == JOptionPane.YES_OPTION) 
			    {
			      filePath = eventController.openFile(frame, editorPane);
			      System.out.println(filePath.toString());
			
			      if (filePath != null)
			      {
			    	  
					  errorLog.setText("");
			    	  //Open Feedback File/////////////////////
				      feedbackFilePath = eventController.getFeedbackFile(filePath);
				      feedbackHistory.getContainer().removeAll();
					  feedbackHistory.readFile(feedbackFilePath, editorPane);
					  feedbackHistory.updateUI();
					  /////////////////////////////////////////;
			    	  fileName = filePath.getFileName().toString();
			    	  eventController.quietlydeleteallbreakpoint(gut, breakpoints);
			      }
			    }
			  }
		});
		URL openfile = Main.class.getResource("/openfile.png");
		openButton.setIcon(new ImageIcon(openfile));
		openButton.setToolTipText("Open");
		openButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JButton saveButton = new JButton("");
		saveButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
			  if (filePath != null)
			  {
				eventController.saveFile(frame, editorPane, filePath, fileModified);
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
			  }
			  
			  else
			  {
				filePath = eventController.saveAsFile(frame, editorPane, fileModified);
				fileName = filePath.getFileName().toString();
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
			  }	  
			}
		});
		saveButton.setBorder(null);
		
	    errorLog = new JTextArea (5,20);
	    debugLog = new JTextArea (5,20);
	    errorLog.setEditable (false); // set textArea non-editable
	    debugLog.setEditable(false);
	    JScrollPane cL = new JScrollPane (errorLog);
	    JScrollPane dL = new JScrollPane (debugLog);
		frame.setVisible(true);
		
		JButton recoverCode = new JButton("");
		recoverCode.setText("Recover Code");
		recoverCode.setToolTipText("Recovers Code Based on Selected Compile History");
		recoverCode.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		recoverCode.setEnabled(false);
		
		JButton removeHistory = new JButton("");
		removeHistory.setText("Remove History");
		removeHistory.setToolTipText("Removes Selected Compilation History");
		removeHistory.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		removeHistory.setEnabled(false);
		
		JButton clearHistory = new JButton("");
		clearHistory.setText("Clear History");
		clearHistory.setToolTipText("Clears all Compilation History");
		clearHistory.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		clearHistory.setEnabled(false);
		
		
		JButton compileButton = new JButton("");
		compileButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
			  
			  if (filePath != null)
			  {
				filePath = eventController.compile(frame, editorPane, filePath, errorLog); 
				JOptionPane.showMessageDialog(null, "Compilation complete", "Compile Code", JOptionPane.PLAIN_MESSAGE);
			  }
			  
			  else if (editorPane.getText().trim().equals(("")))
		      {
				filePath = eventController.compile(frame, editorPane, filePath, errorLog); 
				
				if (filePath != null)
				{
				  JOptionPane.showMessageDialog(null, "Compilation complete", "Compile Code", JOptionPane.PLAIN_MESSAGE);
				  fileName = filePath.getFileName().toString();
				  frame.setTitle(appName + " - " + fileName);
				  fileModified = false;
				}
			  }
			  
			  else
			  {
				filePath = eventController.saveAsFile(frame, editorPane, fileModified);
				fileName = filePath.getFileName().toString();
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
			  }
			  	
			}
		});
		
//		compileButton.setVisible(false);
		
		JButton compilerunButton = new JButton("");
		compilerunButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) {
				
				if (filePath != null)
				  {
					eventController.saveFile(frame, editorPane, filePath, fileModified);
					frame.setTitle(appName + " - " + fileName);
					fileModified = false;
				  }
				
				else if (editorPane.getText().trim().equals(""))
				{
					  filePath = eventController.compile(frame, editorPane, filePath, errorLog);
					  fileModified = true;
					  fileName = filePath.getFileName().toString();
					  frame.setTitle(appName + " - " + fileName);
				}
				  
				  else
				  {
					filePath = eventController.saveAsFile(frame, editorPane, fileModified);
					fileName = filePath.getFileName().toString();
					frame.setTitle(appName + " - " + fileName);
					fileModified = false;
				  }	  
				
				filePath = eventController.compile(frame, editorPane, filePath, errorLog);
				
				///////////////////////Feedback History Prototype////////////////
				Feedback feedback = new Feedback(errorLog.getText(), editorPane.getText());
				feedbackHistory.addFeedback(feedback, filePath, editorPane);
				feedbackHistory.updateUI();
				//Save Feedback File///////////////////////
				feedbackHistory.saveFile(feedbackHistory.getFeedback(), filePath);
				///////////////////////////////////////////
				//feedbackScroll.getVerticalScrollBar().setValue(feedbackScroll.getVerticalScrollBar().getMaximum());
				/////////////////////////////////////////////////////////////////
				eventController.runProgram(filePath);
			}
		});
		
		sendButton = new JButton("Send C File");
		sendButton.setToolTipText("Send source code");
		URL send = Main.class.getResource("/send.png");
		sendButton.setIcon(new ImageIcon(send));
		sendButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		sendButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
			  eventController.checkIfResourceExists();
				eventController.sendSrcCode(errorLog, filePath);
			}
		});
		
		if (studentIdNum.equals("0"))
		{
		  sendButton.setVisible(false);
		}
		
		
		JButton downloadButton = new JButton("Download");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
			  eventController.checkIfResourceExists();
			  eventController.downloadActivity();
			}
		});
		
		URL download = Main.class.getResource("/download.png");
		downloadButton.setToolTipText("Download Activities");
		downloadButton.setIcon(new ImageIcon(download));
		downloadButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		URL save = Main.class.getResource("/save.png");
		saveButton.setIcon(new ImageIcon(save));
		saveButton.setToolTipText("Save");
		saveButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		URL compile = Main.class.getResource("/compile.png");
		compileButton.setIcon(new ImageIcon(compile));
		compileButton.setToolTipText("Compile");
		compileButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		URL compileandrun = Main.class.getResource("/compileandrun.png");
		compilerunButton.setIcon(new ImageIcon(compileandrun));
		compilerunButton.setToolTipText("Compile and Run");
		compilerunButton.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		JButton debugButton = new JButton("");
		URL debug = Main.class.getResource("/debug.png");
		debugButton.setIcon(new ImageIcon(debug));
		debugButton.setToolTipText("Debug");
		debugButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		breakpointButton = new JButton("");
		URL breakpoint = Main.class.getResource("/breakpoint.png");
		breakpointButton.setIcon(new ImageIcon(breakpoint));
		breakpointButton.setToolTipText("Add Breakpoints");
		breakpointButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
		delbreakpointButton = new JButton("");
		URL delbreakpoint = Main.class.getResource("/delbreakpoint.png");
		delbreakpointButton.setIcon(new ImageIcon(delbreakpoint));

		
		delbreakpointButton.setToolTipText("Delete Breakpoints");
		delbreakpointButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		delbreakpointButton.setEnabled(false);
		
		delallbreakpointButton = new JButton("");
		URL delallbreakpoint = Main.class.getResource("/delallbreakpoint.png");
		delallbreakpointButton.setIcon(new ImageIcon(delallbreakpoint));
		delallbreakpointButton.setToolTipText("Delete All Breakpoints");
		delallbreakpointButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		delallbreakpointButton.setEnabled(false);
		
		breakpointButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				eventController.addbreakpoint(frame, gut, breakpoints);
				if(breakpoints.size() > 0) {
					delbreakpointButton.setEnabled(true);
					delallbreakpointButton.setEnabled(true);
				}
			}
		});
		
		delbreakpointButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				eventController.deletebreakpoint(frame, gut, breakpoints);
				if(breakpoints.size() == 0) {
					delbreakpointButton.setEnabled(false);
					delallbreakpointButton.setEnabled(false);
				}
			}
		});
		
		delallbreakpointButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				eventController.deleteallbreakpoint(gut, breakpoints);
				if(breakpoints.size() == 0) {
					delbreakpointButton.setEnabled(false);
					delallbreakpointButton.setEnabled(false);
				}
			}
		});
		
		JButton stepOverButton = new JButton("");
		URL stepOver = Main.class.getResource("/stepOver.png");
		stepOverButton.setIcon(new ImageIcon(stepOver));
		stepOverButton.setToolTipText("Step Over");
		stepOverButton.setEnabled(false);
		stepOverButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JButton resumeButton = new JButton("");
		URL resume = Main.class.getResource("/resume.png");
		resumeButton.setIcon(new ImageIcon(resume));
		resumeButton.setToolTipText("Resume");
		resumeButton.setEnabled(false);
		resumeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JButton stopButton = new JButton("");
		URL stop = Main.class.getResource("/stop.png");
		stopButton.setIcon(new ImageIcon(stop));
		stopButton.setToolTipText("Stop Debugging");
		stopButton.setEnabled(false);
		stopButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JButton fontUpButton = new JButton("");
		URL fontUp = Main.class.getResource("/fontUp.png");
		fontUpButton.setIcon(new ImageIcon(fontUp));
		fontUpButton.setToolTipText("Increase Font Size");
		fontUpButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				
		JButton fontDownButton = new JButton("");
		URL fontDown = Main.class.getResource("/fontDown.png");
		fontDownButton.setIcon(new ImageIcon(fontDown));
		fontDownButton.setToolTipText("Decrease Font Size");
		fontDownButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		fontUpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fontSize < maxFont) {
					Font f = new Font(fontStyle, Font.PLAIN, fontSize+=4);
					scrollPane.setFont(f);
					for(int i = 0; i < gut.getComponentCount(); i++)
					{
						gut.getComponent(i).setFont(f);
					}
					editorPane.setFont(f);
					if (fontSize == maxFont) {
						fontUpButton.setEnabled(false);
					}
				}
				fontDownButton.setEnabled(true);
			}
		});
		
		fontDownButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fontSize > minFont) {
					Font f = new Font(fontStyle, Font.PLAIN, fontSize-=4);
					scrollPane.setFont(f);
					for(int i = 0; i < gut.getComponentCount(); i++)
					{
						gut.getComponent(i).setFont(f);
					}
					editorPane.setFont(f);
					if (fontSize == minFont) {
						fontDownButton.setEnabled(false);
					}
				}
				fontUpButton.setEnabled(true);
			}
		});
		
		recoverCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				int confirmed = JOptionPane.showConfirmDialog(null, "Overwrite current code in editor?", "Recover Code", JOptionPane.YES_NO_OPTION);
			    if (confirmed == JOptionPane.YES_OPTION) 
			    {
			    	editorPane.setText(codeHistory.get(tabbedVerticalPane.getSelectedIndex()));
			    }
			}
		});
		
		removeHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove the selected History?", "Remove Code History", JOptionPane.YES_NO_OPTION);
			    if (confirmed == JOptionPane.YES_OPTION) 
			    {
					codeHistory.remove(tabbedVerticalPane.getSelectedIndex());
					tabbedVerticalPane.removeTabAt(tabbedVerticalPane.getSelectedIndex());
					if (tabbedVerticalPane.getTabCount() == 0)
					{
						recoverCode.setEnabled(false);
						removeHistory.setEnabled(false);
						clearHistory.setEnabled(false);
					}
			    }		
			}
		});
		
		clearHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove all History?", "Remove All Code History", JOptionPane.YES_NO_OPTION);
			    if (confirmed == JOptionPane.YES_OPTION) 
			    {
					codeHistory.clear();
					tabbedVerticalPane.removeAll();
					recoverCode.setEnabled(false);
					removeHistory.setEnabled(false);
					clearHistory.setEnabled(false);
			    }	
			}
		});
		
		coreToolbar.add(newButton);
		coreToolbar.add(openButton);
		coreToolbar.add(saveButton);
		coreToolbar.addSeparator();
		coreToolbar.add(compileButton);
		coreToolbar.add(compilerunButton);
		coreToolbar.addSeparator();
		coreToolbar.add(debugButton);
		coreToolbar.add(stepOverButton);
		coreToolbar.add(resumeButton);
		coreToolbar.add(stopButton);
		coreToolbar.addSeparator();
		coreToolbar.add(breakpointButton);
		coreToolbar.add(delbreakpointButton);
		coreToolbar.add(delallbreakpointButton);
		coreToolbar.addSeparator();
		coreToolbar.add(fontUpButton);
		coreToolbar.add(fontDownButton);
		coreToolbar.addSeparator();
		coreToolbar.add(sendButton);
		coreToolbar.addSeparator();
		coreToolbar.add(downloadButton);
		coreToolbar.addSeparator();
		
		/////////////////////////////////////////////////////////////////////////

		///////////////////////////////////////////////////////////////////////////
		menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem newFileItem = new JMenuItem("New", KeyEvent.VK_N);
		newFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{ 
			  if (editorPane.getText().equals(""))
				{
					editorPane.setText("");
					//eventController.deleteDontTouch();
				}
				
				else
				{
					int confirmed = JOptionPane.showConfirmDialog(null, "Create new file?", "", JOptionPane.YES_NO_OPTION);
				
					if (confirmed == JOptionPane.YES_OPTION) 
					{
						editorPane.setText("");
						//eventController.deleteDontTouch();
						feedbackFilePath = null;
						filePath = null;
						fileName = "new file";
						frame.setTitle(appName + " - " + fileName);
				    	eventController.quietlydeleteallbreakpoint(gut, breakpoints);
				    	errorLog.setText("");
						feedbackHistory.getContainer().removeAll();
						feedbackHistory.updateUI();
					}
				}
			}
		});
		newFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		
		JMenuItem openFileItem = new JMenuItem("Open");
		openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openFileItem.addActionListener(new ActionListener() 
		{
		  public void actionPerformed(ActionEvent e) 
		  {
			int confirmed = JOptionPane.showConfirmDialog(null, "Open new file?", "", JOptionPane.YES_NO_OPTION);

		    if (confirmed == JOptionPane.YES_OPTION) 
		    {
		      errorLog.setText("");
		      //Open Feedback File/////////////////////
		      feedbackFilePath = eventController.getFeedbackFile(filePath);
		      feedbackHistory.getContainer().removeAll();
			  feedbackHistory.readFile(feedbackFilePath, editorPane);
			  feedbackHistory.updateUI();
			  /////////////////////////////////////////
		      filePath = eventController.openFile(frame, editorPane);
			  fileName = filePath.getFileName().toString();
		    }
		  }
		});
		
		
		JMenuItem saveFileItem = new JMenuItem("Save");
		saveFileItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
//			  eventController.saveFile(frame, editorPane, filePath, fileModified);
//			  frame.setTitle(appName + " - " + fileName);
//			  fileModified = false;
			  
			  if (filePath != null)
			  {
				eventController.saveFile(frame, editorPane, filePath, fileModified);
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
			  }
			  
			  else
			  {
				filePath = eventController.saveAsFile(frame, editorPane, fileModified);
				if (filePath != null)
				{
					fileName = filePath.getFileName().toString();
					frame.setTitle(appName + " - " + fileName);
					fileModified = false;
				}
			  }	  
			}
		});
		
		saveFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		JMenuItem saveAsFileItem = new JMenuItem("Save As...");
		
		saveAsFileItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
			  filePath = eventController.saveAsFile(frame, editorPane, fileModified);
			}
		});
		
		JMenuItem exitFileItem = new JMenuItem("Exit");
		exitFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
			  int confirmed = JOptionPane.showConfirmDialog(null, 
		        "Are you sure you want to exit the program?", "",
		        JOptionPane.YES_NO_OPTION);

		      if (confirmed == JOptionPane.YES_OPTION) 
		      {
		        frame.dispose();
		      }
			}
		});
		
		JMenu paths = new JMenu("Settings");
		
		JMenuItem settingsGccFileItem = new JMenuItem("GCC path");
		
		settingsGccFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{ 
			  eventController.changeSettingsGcc(frame);
			  eventController.savePathSettings();
			}
		});
		
		paths.add(settingsGccFileItem);
		
		JMenuItem settingsGdbFileItem = new JMenuItem("GDB path");
		
		settingsGdbFileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{ 
			  eventController.changeSettingsGdb(frame);
			  eventController.savePathSettings();
			}
		});
		
		paths.add(settingsGdbFileItem);
		
		JMenuItem settingsIpFileItem = new JMenuItem("IP Address of server");
		
		settingsIpFileItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e)
		  {
			eventController.changeIPSettings();
		  }
		});
		
		paths.add(settingsIpFileItem);
		exitFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);
		JMenuItem undoEditItem = new JMenuItem("Undo", KeyEvent.VK_U);
		undoEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		JMenuItem redoEditItem = new JMenuItem("Redo", KeyEvent.VK_R);
		redoEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		JMenuItem cutEditItem = new JMenuItem("Cut", KeyEvent.VK_T);
		cutEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		JMenuItem copyEditItem = new JMenuItem("Copy", KeyEvent.VK_C);
		copyEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		JMenuItem pasteEditItem = new JMenuItem("Paste", KeyEvent.VK_P);
		pasteEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		JMenuItem findEditItem = new JMenuItem("Find...", KeyEvent.VK_F);
		findEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		JMenuItem selectAllEditItem = new JMenuItem("Select All", KeyEvent.VK_A);
		selectAllEditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		JMenu buildMenu = new JMenu("Build");
		buildMenu.setMnemonic(KeyEvent.VK_B);
		
		JMenuItem compileBuildItem = new JMenuItem("Compile");
		
		compileBuildItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
			  if (filePath != null)
			  {
				filePath = eventController.compile(frame, editorPane, filePath, errorLog); 
				JOptionPane.showMessageDialog(null, "Compilation complete", "Compile Code", JOptionPane.PLAIN_MESSAGE);
			  }
			  
			  else if (editorPane.getText().trim().equals(("")))
		      {
				filePath = eventController.compile(frame, editorPane, filePath, errorLog); 
				
				if (filePath != null)
				{
				  JOptionPane.showMessageDialog(null, "Compilation complete", "Compile Code", JOptionPane.PLAIN_MESSAGE);
				  fileName = filePath.getFileName().toString();
				  frame.setTitle(appName + " - " + fileName);
				  fileModified = false;
				}
			  }
			  
			  else
			  {
				filePath = eventController.saveAsFile(frame, editorPane, fileModified);
				fileName = filePath.getFileName().toString();
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
			  }
			  	
			}
		});
		
		compileBuildItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));
		
		JMenuItem debugBuildItem = new JMenuItem("Debug", KeyEvent.VK_D);
		
		debugBuildItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				eventController.debugToggler(frame, newButton, newFileItem, openButton, 
						openFileItem, saveButton, saveFileItem, saveAsFileItem, 
						compileButton, compilerunButton, compileBuildItem, debugButton, 
						debugBuildItem, stepOverButton, resumeButton, stopButton);
				eventController.debugActual2(frame, editorPane, filePath, newButton, 
						newFileItem, openButton, openFileItem, saveButton, saveFileItem, 
						saveAsFileItem, compileButton, compilerunButton, compileBuildItem, 
						debugButton, debugBuildItem, stepOverButton, resumeButton, 
						stopButton, editorPane, scrollPane, addBreakItem, delBreakItem, 
						delallBreakItem, breakpointButton, delbreakpointButton, delallbreakpointButton, breakpoints);
			}
		});
		
		debugButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				eventController.debugToggler(frame, newButton, newFileItem, openButton, 
						openFileItem, saveButton, saveFileItem, saveAsFileItem, 
						compileButton, compilerunButton, compileBuildItem, debugButton,
						debugBuildItem, stepOverButton, resumeButton, stopButton);
				eventController.debugActual2(frame, editorPane, filePath, newButton, 
						newFileItem, openButton, openFileItem, saveButton, saveFileItem, 
						saveAsFileItem, compileButton, compilerunButton, compileBuildItem, 
						debugButton, debugBuildItem, stepOverButton, resumeButton, 
						stopButton, editorPane, scrollPane, addBreakItem, delBreakItem, 
						delallBreakItem, breakpointButton, delbreakpointButton, delallbreakpointButton, breakpoints);
			}
		});
		
		debugBuildItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, 0));
		
		addBreakItem = new JMenuItem("Add Breakpoint...");
		
		addBreakItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				eventController.addbreakpoint(frame, gut, breakpoints);	
				if(breakpoints.size() > 0) {
					delbreakpointButton.setEnabled(true);
					delallbreakpointButton.setEnabled(true);
				}
			}
		});
		delBreakItem = new JMenuItem("Remove Breakpoint...");
		delBreakItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				eventController.deletebreakpoint(frame, gut, breakpoints);
				if(breakpoints.size() == 0) {
					delbreakpointButton.setEnabled(false);
					delallbreakpointButton.setEnabled(false);
				}
			}
		});
		delallBreakItem = new JMenuItem("Remove all Breakpoint...");
		delallBreakItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				eventController.deleteallbreakpoint(gut, breakpoints);
				if(breakpoints.size() == 0) {
					delbreakpointButton.setEnabled(false);
					delallbreakpointButton.setEnabled(false);
				}
			}
		});
		
		
		JMenuItem manageBreakpointItem = new JMenuItem("Manage Breakpoints...");
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic(KeyEvent.VK_H);
		JMenuItem helpHelpItem = new JMenuItem("Help Contents", KeyEvent.VK_H);
		helpHelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		JMenuItem aboutHelpItem = new JMenuItem("About this System", KeyEvent.VK_A);
		menuBar.add(fileMenu);
		fileMenu.add(newFileItem);
		fileMenu.add(openFileItem);
		fileMenu.add(saveFileItem);
		fileMenu.add(saveAsFileItem);
		fileMenu.addSeparator();
		fileMenu.add(exitFileItem);
		menuBar.add(editMenu);
		editMenu.add(undoEditItem);
		editMenu.add(redoEditItem);
		editMenu.addSeparator();
		editMenu.add(cutEditItem);
		editMenu.add(copyEditItem);
		editMenu.add(pasteEditItem);
		editMenu.add(findEditItem);
		editMenu.addSeparator();
		editMenu.add(selectAllEditItem);
		menuBar.add(buildMenu);
		buildMenu.add(compileBuildItem);
		menuBar.add(paths);
		
		JMenuItem mntmCompileRun = new JMenuItem("Compile & run");
		mntmCompileRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0));
		mntmCompileRun.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
			  if (filePath != null)
			  {
				eventController.saveFile(frame, editorPane, filePath, fileModified);
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
				filePath = eventController.compile(frame, editorPane, filePath, errorLog);
			  }
			
			else if (editorPane.getText().trim().equals(""))
			{
			  filePath = eventController.compile(frame, editorPane, filePath, errorLog);
			  fileModified = true;
			  fileName = filePath.getFileName().toString();
			  frame.setTitle(appName + " - " + fileName);
			}
			  
			  else
			  {
				filePath = eventController.saveAsFile(frame, editorPane, fileModified);
				fileName = filePath.getFileName().toString();
				frame.setTitle(appName + " - " + fileName);
				fileModified = false;
				filePath = eventController.compile(frame, editorPane, filePath, errorLog);
			  }	  
			
			
			
			///////////////////////Feedback History Prototype////////////////
			Feedback feedback = new Feedback(errorLog.getText(), editorPane.getText());
			feedbackHistory.addFeedback(feedback, filePath, editorPane);
			feedbackHistory.updateUI();
			//Save Feedback File///////////////////////
			feedbackHistory.saveFile(feedbackHistory.getFeedback(), filePath);
			///////////////////////////////////////////
			//feedbackScroll.getVerticalScrollBar().setValue(feedbackScroll.getVerticalScrollBar().getMaximum());
			/////////////////////////////////////////////////////////////////
			eventController.runProgram(filePath);
			}
		});
		buildMenu.add(mntmCompileRun);
		buildMenu.add(debugBuildItem);
		buildMenu.add(addBreakItem);
		buildMenu.add(delBreakItem);
		buildMenu.add(delallBreakItem);
		buildMenu.add(manageBreakpointItem);
		menuBar.add(helpMenu);
		helpMenu.add(helpHelpItem);
		helpMenu.add(aboutHelpItem);
		
		frame.setJMenuBar(menuBar);
		frame.getContentPane().setLayout(new BorderLayout());
		//frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		///////////////////////////////////////////////////////////////////LOGS
		frame.setVisible(true);
		horizontalPane = new JSplitPane();
		horizontalPane.setOrientation(JSplitPane.VERTICAL_SPLIT);		
		horizontalPane.setDividerLocation(450);
		frame.getContentPane().add(horizontalPane, BorderLayout.CENTER);
		horizontalPane.setOneTouchExpandable(true);	
		
		//for editor text and cbrc
		verticalPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT){
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private final int location = 900;
		    {
		        setDividerLocation( location );
		    }
		    @Override
		    public int getDividerLocation() {
		        return location ;
		    }
		    @Override
		    public int getLastDividerLocation() {
		        return location ;
		    }
		};
		verticalPane.setLeftComponent(scrollPane);
		verticalPane.setRightComponent(tabbedVerticalPane);
		verticalPane.setOneTouchExpandable(false);	
		
		horizontalPane.setTopComponent(verticalPane);
		
		tabbedHorizontalPane = new JTabbedPane();
		tabbedHorizontalPane.add("Error Log", cL);
		//tabbedHorizontalPane.add("Debug Log", dL);
		//tabbedHorizontalPane.add("Test Log", cL);
		
		tabbedVerticalPane.addTab("Feedback History", new JScrollPane(feedbackHistory));
		
		horizontalPane.setBottomComponent(tabbedHorizontalPane);
		horizontalPane.setResizeWeight(1);
	
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.NORTH, coreToolbar, 0, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, coreToolbar, 0, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, coreToolbar, 48, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, coreToolbar, 2500, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(coreToolbar, BorderLayout.NORTH);


	}
	
	/*
	
	public void addbreakpoint(Gutter gut){
		String input = JOptionPane.showInputDialog(
                frame,
                "Insert a breakpoint, enter a line number (must not exceed the end of file):");
		if(input == null || input.isEmpty())
		{
			// do nothing
		}
		else
		{
			try
			{
				int bpnum = Integer.parseInt(input) - 1;
				boolean existing = false;
				for(int i = 0; i < breakpoints.size(); i++)
				{
					if(breakpoints.get(i) == bpnum)
					{
						existing = true;
					}
				}
				if(!existing)
				{
					GutterIconInfo gii = gut.addLineTrackingIcon(bpnum, new ImageIcon("resources/images/materialsmall/breakpointeditor.png"));
					breakpoints.add(bpnum);
					breakpoints2.add(gii);
					JOptionPane.showMessageDialog(null, "Line " + input + " added successfully.", "Added!", JOptionPane.INFORMATION_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null, "Line " + input + " already exists.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (BadLocationException ble)
			{
				JOptionPane.showMessageDialog(null, "The line specified is not found. Discontinuing adding breakpoints...", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null, "You entered a non-integer number!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (NullPointerException npe)
			{
				
			}
		}
	}
	
	public void deletebreakpoint(Gutter gut){
		String input = JOptionPane.showInputDialog(
                frame,
                "Remove a breakpoint, enter a line number (must not exceed the end of file):");
		if(input == null || input.isEmpty())
		{
			// do nothing
		}
		else
		{
			try
			{
				int bpnum = Integer.parseInt(input) - 1;
				int target = -1;
				GutterIconInfo gii = null;
				for(int i = 0; i < breakpoints.size(); i++)
				{
					if(breakpoints.get(i) == bpnum)
					{
						gii = breakpoints2.get(i);
						target = i;
					}
				}
				if(target == -1)
					JOptionPane.showMessageDialog(null, "Line " + input + " does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
				else
				{
					gut.removeTrackingIcon(gii);
					breakpoints.remove(target);
					breakpoints2.remove(target);
					JOptionPane.showMessageDialog(null, "Line " + input + " removed successfully.", "Removed", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			catch (NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null, "You entered a non-integer number!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (NullPointerException npe)
			{
				
			}
		}
	}
	
	public void deleteallbreakpoint(Gutter gut){				
			gut.removeAllTrackingIcons();
			breakpoints.clear();
			breakpoints2.clear();
			JOptionPane.showMessageDialog(null, "All breakpoints removed successfully.", "Removed", JOptionPane.INFORMATION_MESSAGE);
	}
	*/
	
	public JSplitPane getHorizontalPane()
	{
		return horizontalPane;
	}
	
	public JSplitPane getVerticalPane()
	{
		return verticalPane;
	}
	
	public JTabbedPane getTabbedHorizontalPane()
	{
		return tabbedHorizontalPane;
	}
	
	public JTabbedPane getTabbedVerticalPane()
	{
		return tabbedVerticalPane;
	}
	
	public JToolBar getCoreToolbar()
	{
	  return coreToolbar;
	}

	public JMenuBar getMenuBar()
	{
	  return menuBar;
	}

	public JTextArea getErrorLog()
	{
	  return errorLog;
	}
	
	public JTextArea getfeedbackLog()
	{
	  return feedbackLog;
	}
	
	public JFrame getMainFrame()
	{
	  return frame;
	}
	
	public RSyntaxTextArea getEditor()
	{
	  return editorPane;
	}

	public String getStudentIdNum()
	{
	  return studentIdNum;
	}

	public void setStudentIdNum(String studentIdNum)
	{
	  this.studentIdNum = studentIdNum;
	}
	
	public void checkIfSendable()
	{
	  if (studentIdNum.equals("0"))
		{
		  sendButton.setVisible(false);
		}
	}
}
