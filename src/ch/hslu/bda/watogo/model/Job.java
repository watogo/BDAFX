package ch.hslu.bda.watogo.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model für einen Job.
 *
 * @author Muhamed und Niklaus
 */
public class Job {

    public final StringProperty spider;
    public final StringProperty jobID;
    public final StringProperty startTime;
    public final IntegerProperty numberOfEvents;
    public final StringProperty state;

    /**
     * Konstruktor.
     *
     * @param spider - Spider
     * @param jobID - Job ID
     * @param startTime - Startzeit
     * @param numEvents - Anzahl Events
     * @param state - Status
     */
    public Job(String spider, String jobID, String startTime, int numEvents, String state) {
        this.spider = new SimpleStringProperty(spider);
        this.jobID = new SimpleStringProperty(jobID);
        this.startTime = new SimpleStringProperty(startTime);
        this.numberOfEvents = new SimpleIntegerProperty(numEvents);
        this.state = new SimpleStringProperty(state);
    }
}
