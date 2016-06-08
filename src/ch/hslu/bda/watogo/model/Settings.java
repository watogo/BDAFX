/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.bda.watogo.model;

import ch.hslu.bda.watogo.view.SettingsController;

public class Settings {

    public static String apiKey = "";
    public static String projectId = "";
    public static String port = "";
    public static String serverip = "";
    public static String dbName = "";
    public static String dbCollection = "";
    public static String dbUsername = "";
    public static String dbPassword = "";
    
    public static SettingsController settingControl;

    private static Settings singleton;

    private Settings() {
    }

    public static Settings getInstance() {
        if (singleton == null) {
            singleton = new Settings();
            settingControl = new SettingsController();
            apiKey = settingControl.getSetting("apiKey");
            projectId = settingControl.getSetting("projectId");
            serverip = settingControl.getSetting("serverIp");
            port = settingControl.getSetting("port");
            dbName = settingControl.getSetting("dbName");
            dbCollection = settingControl.getSetting("dbCollection");
            dbUsername = settingControl.getSetting("dbUsername");
            dbPassword = settingControl.getSetting("dbPassword");
        }
        return singleton;
    }
}
