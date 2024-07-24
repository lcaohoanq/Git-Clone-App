package com.lcaohoanq.util;

public class GithubUtils {

    public static String convertToApiUrl(String repoUrl) {
        if (repoUrl.startsWith("https://github.com/")) {
            repoUrl = repoUrl.replace("https://github.com/", "");
            if (repoUrl.endsWith(".git")) {
                repoUrl = repoUrl.replace(".git", "");
            }
        }
        return !repoUrl.endsWith("git") ? "https://api.github.com/repos/".concat(repoUrl)
            : "https://api.github.com/repos/".concat(repoUrl.replace(".git", ""));
    }
}
