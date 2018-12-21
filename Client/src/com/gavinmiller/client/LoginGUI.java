package com.gavinmiller.client;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import com.gavinmiller.main.Main;

/**
 * Simple login GUI using Java's swing library
 * @author Gavin Miller
 * @version 0.1
 */
public class LoginGUI extends JFrame{
    
    // New font size to fill GUI
    private static final Font DEFAULT_FONT = new Font("Calibri", 0, 50);
    
    // Components of the GUI
    private JPanel contentPanel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JTextField usernameTF;
    private JPasswordField passwordTF;
    private JButton loginButton;
    private JButton exitButton;
    
    // Used to interact with the mainclient object when packets are sent or received (i.e. login details out, authorisation packet in)
    private final MainClient mainClient;
    
    public LoginGUI(MainClient mainClient){
        setUIFont(new javax.swing.plaf.FontUIResource(DEFAULT_FONT)); // Disable this to reduce size of text, only increased as does not look very big on 4K screen
        
        this.mainClient = mainClient;
        
        initComponents();
    }
    
    public void loginSuccess(){
        JOptionPane.showMessageDialog(this, "Success!");
        Main.startGame();
        this.setVisible(false);
    }
    
    public void loginFailed(){
        JOptionPane.showMessageDialog(this, "Login failed! Please check the details you entered");
    }
    
    private void initComponents(){
        this.setTitle("MMO Login");
        this.setSize(1280, 1024);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        contentPanel = new JPanel(new GridLayout(3, 2));
        usernameLabel = new JLabel("Username:");
        passwordLabel = new JLabel("Password:");
        
        usernameTF = new JTextField();
        
        passwordTF = new JPasswordField();
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ButtonListener());
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ButtonListener());
        
        contentPanel.add(usernameLabel);
        contentPanel.add(usernameTF);
        contentPanel.add(passwordLabel);
        contentPanel.add(passwordTF);
        contentPanel.add(loginButton);
        contentPanel.add(exitButton);
        
        this.add(contentPanel);
    }
    
    private void login(){
        mainClient.requestLogin(usernameTF.getText(), passwordTF.getText());
    }
    
    private void exit(){
        System.exit(0);
    }
    
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
    
    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Login")){
                login();
            }
            else {
                exit();
            }
        }
        
    }
}
