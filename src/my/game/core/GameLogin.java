package my.game.core;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameLogin extends JFrame {

	private static final long serialVersionUID = 3231919365849153231L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private boolean running = true;

	/**
	 * Create the dialog.
	 */
	public GameLogin() {
		setResizable(false);
		setBounds(100, 100, 294, 190);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JButton btnJoin = new JButton("Join");
		btnJoin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField.getText().isEmpty()) return;
				running = false;
			}
		});
		btnJoin.setBounds(99, 122, 89, 23);
		contentPanel.add(btnJoin);
		
		textField = new JTextField();
		textField.setBounds(10, 36, 266, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(117, 11, 71, 14);
		contentPanel.add(lblUsername);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setEnabled(false);
		textField_1.setBounds(10, 91, 266, 20);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblreserved = new JLabel("[Reserved]");
		lblreserved.setBounds(117, 67, 71, 14);
		contentPanel.add(lblreserved);
		setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		setVisible(true);
		
		while(running) {System.out.println();}
		setVisible(false);
	}
	
	public String[] getVars() {
		return new String[] {textField.getText(), textField_1.getText()};
	}
}
