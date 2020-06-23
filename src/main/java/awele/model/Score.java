package awele.model;

import java.sql.Timestamp;

/**
 * Score model used to log the game results in database
 * representation of the fields with getters and setters
 */
public class Score {

    private int id;
    private Timestamp date;
    private int duration;
    private String humanPlayerName;
    private String virtualPlayerName;
    private int humanPlayerScore;
    private int virtualPlayerScore;
    private String winnerName;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getHumanPlayerName() {
        return humanPlayerName;
    }

    public void setHumanPlayerName(String humanPlayerName) {
        this.humanPlayerName = humanPlayerName;
    }

    public String getVirtualPlayerName() {
        return virtualPlayerName;
    }

    public void setVirtualPlayerName(String virtualPlayerName) {
        this.virtualPlayerName = virtualPlayerName;
    }

    public int getHumanPlayerScore() {
        return humanPlayerScore;
    }

    public void setHumanPlayerScore(int humanPlayerScore) {
        this.humanPlayerScore = humanPlayerScore;
    }

    public int getVirtualPlayerScore() {
        return virtualPlayerScore;
    }

    public void setVirtualPlayerScore(int virtualPlayerScore) {
        this.virtualPlayerScore = virtualPlayerScore;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

}
