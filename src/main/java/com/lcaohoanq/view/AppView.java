package com.lcaohoanq.view;

import com.lcaohoanq.constant.Notification;
import com.lcaohoanq.constant.Path;
import com.lcaohoanq.constant.Size;
import com.lcaohoanq.util.AudioHandler;
import com.lcaohoanq.controller.AppController;
import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import com.lcaohoanq.constant.Regex;

public class AppView extends JFrame {

    private static final AudioHandler audioHandler = new AudioHandler();
    private final URL iconURL = AppView.class.getResource(Path.IMAGE_LOGO);
    public Image icon = Toolkit.getDefaultToolkit().createImage(iconURL);
    private final JPanel jPanel = new JPanel();
    private final JTextField jTextField_RepoUrl = new JTextField();
    private final JButton jButton_CloneButton = new JButton("Clone");
    private final JTextArea jTextArea_StatusArea = new JTextArea();
    private JFileChooser jFileChooser;
    private AppController appController = new AppController(this);

    public AppView() {
        setTitle("Git Clone App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Size.FRAME_WIDTH, Size.FRAME_HEIGHT);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setIconImage(icon);
        setVisible(false);
        initFileChooser();
        initUI();
    }

    private void initUI() {
        jButton_CloneButton.setBackground(Color.decode("#F27BBD"));
        jButton_CloneButton.setForeground(Color.WHITE);
        jButton_CloneButton.setFont(new Font("Arial", Font.BOLD, 14));
        jButton_CloneButton.addActionListener(appController);

        jTextField_RepoUrl.setFont(new Font("Roboto", Font.PLAIN, 14));
        jTextField_RepoUrl.addKeyListener(appController);

        jTextArea_StatusArea.setBackground(Color.decode("#121212"));
        jTextArea_StatusArea.setForeground(Color.decode("#16FF00"));
        jTextArea_StatusArea.setFont(new Font("Roboto", Font.PLAIN, 14));
        jTextArea_StatusArea.setEditable(false);
        jTextArea_StatusArea.setLineWrap(true);

        jPanel.setLayout(new BorderLayout());
        jPanel.add(jTextField_RepoUrl, BorderLayout.CENTER);
        jPanel.add(jButton_CloneButton, BorderLayout.EAST);
        this.add(jPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(jTextArea_StatusArea), BorderLayout.CENTER);
    }

    private void initFileChooser() {
        jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public boolean validateRepoUrl(String repoUrl) {
        if (repoUrl.isEmpty()) {
            audioHandler.playAudio(Path.AUDIO_ERROR);
            JOptionPane.showMessageDialog(null, Notification.PLEASE_ENTER_A_REPOSITORY_URL);
            return false;
        } else {
            //check if the url is match with github url
            if (isValidGithubUrl(repoUrl)) {
                return true;
            }else{
                audioHandler.playAudio(Path.AUDIO_ERROR);
                JOptionPane.showMessageDialog(null, Notification.PLEASE_ENTER_A_VALID_REPOSITORY_URL);
                return false;
            }
        }
    }

    private boolean isValidGithubUrl(String repoUrl) {
        return repoUrl.matches(Regex.GITHUB_HTTP_URL) || repoUrl.matches(Regex.GITHUB_SSH_URL);
    }

    public File selectDirectory() {
        UIManager.put("OptionPane.background", Color.decode("#f1f1f1"));
        UIManager.put("Panel.background",  Color.decode("#f1f1f1"));
        UIManager.put("Button.background", Color.decode("#90D26D"));
        UIManager.put("Button.foreground", Color.WHITE);

        Object[] customizeOptions = {"Select", "Desktop"};
        int userChoice = JOptionPane.showOptionDialog(null,
            "Choose the selected directory or placed in your Desktop?", "Confirm",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, customizeOptions,
            customizeOptions[1]);
        if (userChoice == JOptionPane.YES_OPTION) {
            int option = jFileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                return jFileChooser.getSelectedFile();
            }
        } else if (userChoice == JOptionPane.NO_OPTION) {
            // Default to user's Desktop if no directory is selected
            return new File(System.getProperty("user.home"), "Desktop");
        }
        return null;
    }

    public Timer startLoadingEffect() {
        jTextArea_StatusArea.setText("Cloning repository");
        jButton_CloneButton.setEnabled(false); // Disable the clone button during cloning

        // Create a timer that appends a dot to the text area every 500 milliseconds
        Timer timer = new Timer(500, null);
        timer.addActionListener(event -> jTextArea_StatusArea.append("."));
        timer.start();

        return timer;
    }

    public void stopLoadingEffect(Timer timer) {
        // Stop the timer and append a newline character when the cloning is done
        timer.stop();
        jButton_CloneButton.setEnabled(true); // Re-enable the clone button after cloning
    }

    public JTextField getjTextField_RepoUrl() {
        return jTextField_RepoUrl;
    }

    public JTextArea getjTextArea_StatusArea() {
        return jTextArea_StatusArea;
    }
}

