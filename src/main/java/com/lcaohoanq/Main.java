package com.lcaohoanq;

import com.lcaohoanq.util.LogUtils;
import com.lcaohoanq.view.AppView;
import java.awt.EventQueue;

public class Main {

    public static void main(String[] args) {

        LogUtils.ensureLogsFolderExists();

        EventQueue.invokeLater(() -> {
            new AppView().setVisible(true);
        });
    }

    // test
    // https://github.com/lcaohoanq/Web-Snake-Game-Frontend
}