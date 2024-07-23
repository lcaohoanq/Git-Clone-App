package com.lcaohoanq.view;

import com.lcaohoanq.constant.Notification;
import javax.swing.JOptionPane;

public class UIPrompts {

    public static void IS_EXIST_REPO_CLONED(){
        JOptionPane.showMessageDialog(null, "Repo is already exist", "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public static void REPO_NOT_EXIST(){
        JOptionPane.showMessageDialog(null, Notification.REPOSITORY_NOT_FOUND, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public static void INTERNAL_SERVER_ERROR(){
        JOptionPane.showMessageDialog(null, Notification.INTERNAL_SERVER_ERROR, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public static void UNEXPECTED_ERROR(){
        JOptionPane.showMessageDialog(null, Notification.UNEXPECTED_ERROR, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    public static void CLONED_SUCCESSFULLY(){
        JOptionPane.showMessageDialog(null, Notification.REPOSITORY_CLONED_SUCCESSFULLY,
            "Success", JOptionPane.INFORMATION_MESSAGE);
    }

}
