package api.editor.main.toolbar;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JToolBar;

import model.APIInterface;
import view.MainWindowView;

public class ToolBar implements APIInterface
{
  private static ToolBar instance = null;
  private MainWindowView mainWindow;
  private JToolBar toolBar;
  private HashMap<Integer, Component> listOfComponents;
  private JTextArea consoleLog;
  
  private ToolBar()
  {
	mainWindow = MainWindowView.getInstance();
	listOfComponents = new HashMap<Integer, Component>();
	toolBar = mainWindow.getCoreToolbar();
	consoleLog = mainWindow.getConsoleLog();
  }
  
  public static ToolBar getToolbar()
  {
	if (instance == null)
	{
	  instance = new ToolBar();
	}
	
	return instance;
  }
  
  public void addComponent(int id, Component comp)
  {
	toolBar.add(comp);
	listOfComponents.put(id, comp);
  }
  
  public void removeComponent(int id)
  {
	Component comp = listOfComponents.get(id);
	toolBar.remove(comp);
	listOfComponents.remove(id);
  }
  
  public void addSeparator()
  {
	toolBar.addSeparator();
  }
  
  public void setConsoleText(String message)
  {
	String consoleCurrentText = consoleLog.getText().toString();
	
	if (consoleCurrentText.isEmpty())
	{
	  consoleLog.setText(message);
	}
	
	else
	{
	  String newMessage = consoleLog.getText().toString() + "\n\n" + message;
	  consoleLog.setText(newMessage);
	}
  }
}
