package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class SourceCodeUploaderView {

	private JFrame frmActivityUpload;
	private JTextField txtCrecursionpdf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SourceCodeUploaderView window = new SourceCodeUploaderView();
					window.frmActivityUpload.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SourceCodeUploaderView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmActivityUpload = new JFrame();
		frmActivityUpload.setTitle("Source Code Upload");
		frmActivityUpload.setBounds(100, 100, 450, 135);
		frmActivityUpload.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmActivityUpload.getContentPane().setLayout(null);
		
		JLabel lblActivityName = new JLabel("Select Activity:");
		lblActivityName.setBounds(10, 14, 100, 14);
		frmActivityUpload.getContentPane().add(lblActivityName);
		
		JLabel lblActivityFileName = new JLabel("Source Code:");
		lblActivityFileName.setBounds(10, 39, 100, 14);
		frmActivityUpload.getContentPane().add(lblActivityFileName);
		
		txtCrecursionpdf = new JTextField();
		txtCrecursionpdf.setEditable(false);
		txtCrecursionpdf.setText("C:\\Recursion.c");
		txtCrecursionpdf.setBounds(120, 36, 212, 20);
		frmActivityUpload.getContentPane().add(txtCrecursionpdf);
		txtCrecursionpdf.setColumns(10);
		
		JButton btnUpload = new JButton("Browse...");
		btnUpload.setBounds(335, 35, 89, 23);
		frmActivityUpload.getContentPane().add(btnUpload);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(335, 66, 89, 23);
		frmActivityUpload.getContentPane().add(btnSubmit);
		
		JComboBox comboBox_6 = new JComboBox();
		comboBox_6.setModel(new DefaultComboBoxModel(new String[] {"Basic Functions", "Conditional Satements", "Functions", "Loops", "ASCII Art I", "Recursion"}));
		comboBox_6.setSelectedIndex(5);
		comboBox_6.setBounds(120, 11, 304, 20);
		frmActivityUpload.getContentPane().add(comboBox_6);
	}
}