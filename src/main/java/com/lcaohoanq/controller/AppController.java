package com.lcaohoanq.controller;

import com.lcaohoanq.constant.HttpStatusCode;
import com.lcaohoanq.constant.Notification;
import com.lcaohoanq.constant.Path;
import com.lcaohoanq.error.ErrorHandler;
import com.lcaohoanq.service.GithubService;
import com.lcaohoanq.util.AudioHandler;
import com.lcaohoanq.view.AppView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class AppController implements ActionListener, KeyListener {

    private AppView appView;
    private String repoUrl;
    private String folderRepoName;
    private File selectedDirectory;
    private Timer timer;
    private final AudioHandler audioHandler = new AudioHandler();
    private final GithubService githubService = new GithubService();

    public AppController(AppView appView) {
        this.appView = appView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repoUrl = appView.getjTextField_RepoUrl().getText();
        if (!appView.validateRepoUrl(repoUrl)) {
            return;
        }

        // check is public or private repo
        try {
            boolean isPublic = githubService.isPublicRepository(repoUrl);

            System.out.println("Url = " + repoUrl);

            selectedDirectory = appView.selectDirectory();
            if (selectedDirectory == null) {
                System.out.println("You cancel the directory selection");
                return;
            }

            folderRepoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1);

            timer = appView.startLoadingEffect();

            new Thread(() -> {
                try {
                    cloneRepository(repoUrl, folderRepoName);
                } catch (ErrorHandler ex) {
                    if (ex.getStatusCode() == 400) {
                        JOptionPane.showMessageDialog(null, "Repo is already exist", "Error",
                            JOptionPane.ERROR_MESSAGE);
                        appView.stopLoadingEffect(timer);
                    }
                }
            }).start();
        } catch (ErrorHandler ex) {
            if (ex.getStatusCode() == HttpStatusCode.NOT_FOUND.getCode()) {
                audioHandler.playAudio(Path.AUDIO_ERROR);
                JOptionPane.showMessageDialog(null, Notification.REPOSITORY_NOT_FOUND, "Error",
                    JOptionPane.ERROR_MESSAGE);
            } else if (ex.getStatusCode() == HttpStatusCode.INTERNAL_SERVER_ERROR.getCode()) {
                audioHandler.playAudio(Path.AUDIO_ERROR);
                JOptionPane.showMessageDialog(null, Notification.INTERNAL_SERVER_ERROR, "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            audioHandler.playAudio(Path.AUDIO_ERROR);
            JOptionPane.showMessageDialog(null, Notification.UNEXPECTED_ERROR, "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cloneRepository(String repoUrl, String folderRepoName) throws ErrorHandler {
        try {
            File repoDir = new File(selectedDirectory, folderRepoName);
            // Create a unique directory for each cloned repository
            Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(repoDir)
                .call();
            appView.stopLoadingEffect(timer);
            audioHandler.playAudio(Path.AUDIO_SUCCESS);
            JOptionPane.showMessageDialog(null, Notification.REPOSITORY_CLONED_SUCCESSFULLY,
                "Error", JOptionPane.ERROR_MESSAGE);
        } catch (GitAPIException ex) {
            appView.getjTextArea_StatusArea().append("Error: " + ex.getMessage() + "\n");
            audioHandler.playAudio(Path.AUDIO_ERROR);
        } catch (Exception ex) {
            appView.getjTextArea_StatusArea().append("Unexpected error: " + ex.getMessage() + "\n");
            audioHandler.playAudio(Path.AUDIO_ERROR);
            throw new ErrorHandler("Repo is already exist", 400);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            this.actionPerformed(null);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
