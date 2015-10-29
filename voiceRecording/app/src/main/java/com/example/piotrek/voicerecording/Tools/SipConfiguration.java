package com.example.piotrek.voicerecording.Tools;

/**
 * Created by Piotrek on 2015-10-29.
 */
public class SipConfiguration {

    private static String sipConfXMLName = "./sipConf.xml";
    private String username = null;
    private String password = null;
    private String serverName = null;
    private String port = null;


    public SipConfiguration() {
        readFromXML();
    }

    public SipConfiguration(String username, String password, String serverName, String port) {
        this.username = username;
        this.password = password;
        this.serverName = serverName;
        this.port = port;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    private void readFromXML()
    {
        username = "Default";
        password = "Default";
        serverName = "Default";
        port = "Default";

    }
    public void saveInXML()
    {

    }
}
