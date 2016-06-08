/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo.model;

import ch.hslu.bda.watogo.view.SettingsController;


public class Settings {
    
    private String apiKey = "";
    private String projectId = "";
    private String port = "";
    private String serverip = "";
    private SettingsController settingControl;
    
    public Settings(){
        settingControl = new SettingsController();
        initSettings();
    }
    
    private void initSettings(){
        this.apiKey = settingControl.getSetting("apiKey");
        this.projectId = settingControl.getSetting("projectId");
        this.serverip = settingControl.getSetting("serverIp");
        this.port = settingControl.getSetting("port");
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }
}
