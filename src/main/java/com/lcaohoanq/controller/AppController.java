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
            selectedDirectory = appView.selectDirectory();
            if (selectedDirectory == null) {
                System.out.println("You cancel the directory selection");
                return;
            }

            folderRepoName = repoUrl.substring(repoUrl.lastIndexOf("/") + 1);

            timer = appView.startLoadingEffect();

            new Thread(() -> {
                cloneRepository(repoUrl, folderRepoName);
            }).start();
        } catch (ErrorHandler ex) {
            if(ex.getStatusCode() == HttpStatusCode.NOT_FOUND.getCode()){
                audioHandler.playAudio(Path.AUDIO_ERROR);
                JOptionPane.showMessageDialog(null, Notification.REPOSITORY_NOT_FOUND);
            }else if(ex.getStatusCode() == HttpStatusCode.INTERNAL_SERVER_ERROR.getCode()){
                audioHandler.playAudio(Path.AUDIO_ERROR);
                JOptionPane.showMessageDialog(null, Notification.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException ex) {
            audioHandler.playAudio(Path.AUDIO_ERROR);
            JOptionPane.showMessageDialog(null, Notification.UNEXPECTED_ERROR);
        }

    }

    private void cloneRepository(String repoUrl, String folderRepoName) {
        try {
            // Create a unique directory for each cloned repository
            File repoDir = new File(selectedDirectory, folderRepoName);
            Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(repoDir)
                .call();
            appView.stopLoadingEffect(timer);
            audioHandler.playAudio(Path.AUDIO_SUCCESS);
            JOptionPane.showMessageDialog(null, Notification.REPOSITORY_CLONED_SUCCESSFULLY);
        } catch (GitAPIException ex) {
            appView.getjTextArea_StatusArea().append("Error: " + ex.getMessage() + "\n");
            audioHandler.playAudio(Path.AUDIO_ERROR);
        } catch (Exception ex) {
            appView.getjTextArea_StatusArea().append("Unexpected error: " + ex.getMessage() + "\n");
            audioHandler.playAudio(Path.AUDIO_ERROR);
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
