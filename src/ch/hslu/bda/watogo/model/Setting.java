/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo.model;

import ch.hslu.bda.watogo.controller.SettingsController;

/**
 *
 * @author Mumi
 */
public enum Setting {
    INSTANCE;
    
    private String apiKey;
    private String projectId;
    private String port;
    private String serverip;
    private String dbName;
    private String dbCollection;
    private String dbUsername;
    private String dbPassword;
    private String dbBezDatumVon;
    private String dbBezDatumBis;
    private String isParseDate;
    
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbCollection() {
        return dbCollection;
    }

    public void setDbCollection(String dbCollection) {
        this.dbCollection = dbCollection;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbBezDatumVon() {
        return dbBezDatumVon;
    }

    public void setDbBezDatumVon(String dbBezDatumVon) {
        this.dbBezDatumVon = dbBezDatumVon;
    }

    public String getDbBezDatumBis() {
        return dbBezDatumBis;
    }

    public void setDbBezDatumBis(String dbBezDatumBis) {
        this.dbBezDatumBis = dbBezDatumBis;
    }

    public String getIsParseDate() {
        return isParseDate;
    }

    public void setIsParseDate(String isParseDate) {
        this.isParseDate = isParseDate;
    }
    
    
    
    public void updateSettings(){
        SettingsController settingControl = new SettingsController();
        setApiKey(settingControl.getSetting("apiKey"));
        setProjectId(settingControl.getSetting("projectId"));
        setServerip(settingControl.getSetting("serverIp"));
        setPort(settingControl.getSetting("port"));
        setDbName(settingControl.getSetting("dbName"));
        setDbCollection(settingControl.getSetting("dbCollection"));
        setDbUsername(settingControl.getSetting("dbUsername"));
        setDbPassword(settingControl.getSetting("dbPassword"));
        setDbBezDatumVon(settingControl.getSetting("dbBezDatumVon"));
        setDbBezDatumBis(settingControl.getSetting("dbBezDatumBis"));
        setIsParseDate(settingControl.getSetting("isParseDate"));
    }
    
}
