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
    public static SettingsController settingControl;

    private static Settings singleton = new Settings();

    /* A private Constructor prevents any other 
     * class from instantiating.
     */
    private Settings() {
    }

    /* Static 'instance' method */
    public static Settings getInstance() {
        if (singleton != null) {
            singleton = new Settings();
            settingControl = new SettingsController();
            apiKey = settingControl.getSetting("apiKey");
            projectId = settingControl.getSetting("projectId");
            serverip = settingControl.getSetting("serverIp");
            port = settingControl.getSetting("port");
            //initSettings();
        }
        return singleton;
    }
    
    protected void initSettings() {
        Settings.apiKey = settingControl.getSetting("apiKey");
        Settings.projectId = settingControl.getSetting("projectId");
        Settings.serverip = settingControl.getSetting("serverIp");
        Settings.port = settingControl.getSetting("port");
    }
}
