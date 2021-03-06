package ch.hslu.bda.watogo.controller;

import ch.hslu.bda.watogo.util.DateParser;
import ch.hslu.bda.watogo.model.Job;
import ch.hslu.bda.watogo.model.Setting;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Controller für die Interaktion mit Scrapinghub.
 *
 * @author Muhamed und Niklaus
 */
public class ScrapinghubController {

    private final String key;
    private final String projectID;
    private ObservableList<Job> jobData = FXCollections.observableArrayList();
    private ObservableList<String> spiderList = FXCollections.observableArrayList();
    private static final Object[] zeitstandard = {"UTC-12",
        "UTC-11",
        "UTC-10",
        "UTC-9",
        "UTC-8",
        "UTC-7",
        "UTC-6",
        "UTC-5",
        "UTC-4",
        "UTC-3",
        "UTC-2",
        "UTC-1",
        "UTC+0",
        "UTC+1",
        "UTC+2",
        "UTC+3",
        "UTC+4",
        "UTC+5",
        "UTC+6",
        "UTC+7",
        "UTC+8",
        "UTC+9",
        "UTC+10",
        "UTC+11",
        "UTC+12"};

    private JComboBox myZeitList = new JComboBox(zeitstandard);

    /**
     * Konstruktor.
     *
     * @param key - API Key
     * @param projectID - Projekt ID
     */
    public ScrapinghubController(String key, String projectID) {
        this.key = key;
        this.projectID = projectID;
    }

    /**
     * Gibt alle Jobs zurück, die sich auf Scrapinghub befinden.
     *
     * @return alle Jobs
     */
    public ObservableList<Job> retrieveJobs() {
        JSONObject response;
        try {
            Process p;
            String cmd = "curl -u " + this.key + ": \"https://app.scrapinghub.com/api/jobs/list.json?project="
                    + this.projectID + "\"";
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                response = new JSONObject(s);
                JSONArray jobsArray = response.getJSONArray("jobs");
                JSONObject jobJSON;
                String spiderName, startedTime, state, jobID;
                int itemsScraped;
                for (int j = 0; j < jobsArray.length(); j++) {
                    jobJSON = jobsArray.getJSONObject(j);
                    spiderName = jobJSON.getString("spider");
                    jobID = jobJSON.getString("id");

                    if (jobJSON.has("started_time")) {
                        startedTime = jobJSON.getString("started_time");
                    } else {
                        startedTime = "-";
                    }
                    itemsScraped = jobJSON.getInt("items_scraped");

                    if (jobJSON.has("close_reason")) {
                        state = jobJSON.getString("close_reason");
                    } else {
                        state = jobJSON.getString("state");
                    }

                    jobData.add(new Job(spiderName, jobID, startedTime, itemsScraped, state));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in the correct Settings and restart the application",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return jobData;
    }

    /**
     * Gibt alle Spidernamen zurück, die sich auf Scrapinghub befinden.
     *
     * @return alle Spidernamen
     */
    public ObservableList<String> getSpiderNames() {
        if (!jobData.isEmpty()) {
            for (Job element : jobData) {
                if (!spiderList.contains(element.spider.getValue())) {
                    spiderList.add(element.spider.getValue());
                }
            }
        }

        return spiderList;
    }

    /**
     * Zeigt das Log an.
     *
     * @param contentControl - Kontroller vom content
     * @param job - Der Job, welcher ausgegeben werden soll.
     */
    public void showLog(ContentController contentControl, Job job) {
        try {
            Process p;
            String cmd = "curl -u " + this.key + ": https://storage.scrapinghub.com/logs/" + job.jobID.getValue();
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String totalS = "";
            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                totalS += s + "\r\n";
            }
            contentControl.addText(totalS); //Add total String to logArea
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Please fill in the correct Settings and restart the application",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Gibt alle Items zurück, die zum Job vorhanden sind.
     *
     * @param contentControl - Controller vom Content
     * @param job - Job von dem die Items sind
     * @return JSON Array mit allen Items
     */
    public JSONArray getJsonArray(ContentController contentControl, Job job) {
        String totalS = "";
        JSONArray myJSONArray = null;
        try {
            Process p;
            String cmd = "curl -u " + key + ": https://storage.scrapinghub.com/items/" + job.jobID.getValue() + "?format=json";
            p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while (true) {
                String s = br.readLine();
                if (s == null) {
                    break;
                }
                totalS += s + "\r\n";
            }

            myJSONArray = new JSONArray(totalS);

            if (Setting.INSTANCE.getIsParseDate().equals("1")) {

                myZeitList.setSelectedIndex(13); //UTC+1 per default wählen
                Frame myFrame = new Frame();
                myFrame.setAlwaysOnTop(true);
                JOptionPane.showMessageDialog(myFrame, myZeitList, "Bitte Zeitstandard auswählen", JOptionPane.QUESTION_MESSAGE);
                
                ArrayList<String> myDateVonStringArray = new ArrayList<>();
                ArrayList<String> myDateBisStringArray = new ArrayList<>();

                String myDateVonString = "";
                String myDateBisString = "";

                for (int i = 0; i < myJSONArray.length(); i++) {
                    JSONObject current = myJSONArray.getJSONObject(i);
                    
                    try{
                        myDateVonStringArray.add(current.get(Setting.INSTANCE.getDbBezDatumVon()).toString() + "~");
                        myDateBisStringArray.add(current.get(Setting.INSTANCE.getDbBezDatumBis()).toString() + "~");
                    }catch(Exception e){
                        myJSONArray.remove(i);
                        i -= 1;
                    }
                    //myDateVonString += myJSONArray.getJSONObject(i).get(Setting.INSTANCE.getDbBezDatumVon()).toString() + "~";
                    //myDateBisString += myJSONArray.getJSONObject(i).get(Setting.INSTANCE.getDbBezDatumBis()).toString() + "~";
                }
                
                for(int i = 0; i < myDateVonStringArray.size(); i++){
                    myDateVonString += myDateVonStringArray.get(i);
                    myDateBisString += myDateBisStringArray.get(i);
                    
                    if(((i % 799 == 0)&&(i!= 0)) || (i == myDateVonStringArray.size()-1)){ 
                        
                        //800 Elemente
                        myDateVonString = myDateVonString.replace(' ', ';');
                        myDateVonString = myDateVonString.substring(0, myDateVonString.length() - 1); //letztes zeichen abschneiden
                        myDateBisString = myDateBisString.replace(' ', ';');
                        myDateBisString = myDateBisString.substring(0, myDateBisString.length() - 1); //letztes zeichen abschneiden
                        
                        if(i > 799){
                            myJSONArray = runPythonScript(myJSONArray, myDateVonString, myDateBisString, i-799);
                        }else{
                            myJSONArray = runPythonScript(myJSONArray, myDateVonString, myDateBisString, 0);
                        }
                        
                        myDateVonString = ""; //reset
                        myDateBisString = "";//reset
                    }
                }
            }
        } catch (IOException | JSONException | HeadlessException ex) {
            JOptionPane.showMessageDialog(null,
                    ex.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return myJSONArray;
    }

    private JSONArray runPythonScript(JSONArray myArray, String myDateVonString, String myDateBisString, int indexOf) {

        DateParser parser = new DateParser();

        try {
            Process pro;
            Process pro2;

            String command[] = {"python", "scripts/parseDate.py", "--date=" + myDateVonString};
            String command2[] = {"python", "scripts/parseDate.py", "--date=" + myDateBisString};
            pro = Runtime.getRuntime().exec(command);
            pro2 = Runtime.getRuntime().exec(command2);

            BufferedReader buffRead = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            BufferedReader buffRead2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));

            String s = "";
            String s2 = "";
            String datumVon = "";
            String datumBis = "";
            //int count = 0;

            while (true) {
                s = buffRead.readLine();
                s2 = buffRead2.readLine();

                if (s == null || s2 == null) {
                    break;
                }
                datumVon = parser.parseDate(s);
                datumBis = parser.parseDate(s2);

                Calendar calendarVon = Calendar.getInstance();
                Calendar calendarBis = Calendar.getInstance();

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                Date dateVon = format.parse(datumVon);
                Date dateBis = format.parse(datumBis);

                calendarVon.setTime(dateVon);
                calendarBis.setTime(dateBis);

                if (myZeitList.getSelectedIndex() < 12) {
                    calendarVon.add(Calendar.HOUR_OF_DAY, (-1) * myZeitList.getSelectedIndex() - 12);
                    calendarBis.add(Calendar.HOUR_OF_DAY, (-1) * myZeitList.getSelectedIndex() - 12);
                } else if (myZeitList.getSelectedIndex() > 12) {
                    calendarVon.add(Calendar.HOUR_OF_DAY, -myZeitList.getSelectedIndex() - 12);
                    calendarBis.add(Calendar.HOUR_OF_DAY, -myZeitList.getSelectedIndex() - 12);
                }

                if (!format.format(calendarVon.getTime()).equals("")) {
                    myArray.getJSONObject(indexOf).put(Setting.INSTANCE.getDbBezDatumVon(), format.format(calendarVon.getTime()));
                }
                if (!format.format(calendarBis.getTime()).equals("")) {
                    myArray.getJSONObject(indexOf).put(Setting.INSTANCE.getDbBezDatumBis(), format.format(calendarBis.getTime()));
                }
                indexOf++;
            }
        } catch (IOException | ParseException | JSONException ex) {
            JOptionPane.showMessageDialog(null,
                    ex.toString(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return myArray;
    }

}
