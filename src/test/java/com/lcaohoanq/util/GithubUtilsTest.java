package com.lcaohoanq.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class GithubUtilsTest {

    @Test
    public void convertToApiUrl() {
        String expected = "https://api.github.com/repos/lcaohoanq/docker-tomcat-tutorial";

        assertEquals(expected, GithubUtils.convertToApiUrl("https://github.com/lcaohoanq/docker-tomcat-tutorial.git"));
        assertEquals(expected, GithubUtils.convertToApiUrl("https://github.com/lcaohoanq/docker-tomcat-tutorial"));
        assertEquals(expected, GithubUtils.convertToApiUrl("lcaohoanq/docker-tomcat-tutorial"));
        assertEquals(expected, GithubUtils.convertToApiUrl("lcaohoanq/docker-tomcat-tutorial.git"));
    }
}