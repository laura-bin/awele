package awele.services;

import awele.model.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/**
 * Manages the connection to the database to insert and get scores
 */
public class Database {

    private static Database DB;

    private final ObservableList<Score> scores = FXCollections.observableArrayList();

    /**
     * Private constructor for Singleton pattern
     */
    private Database() {
        refreshScores();
    }

    /**
     * @return the one and only instance of Database class
     */
    public static Database getInstance() {
        if (DB == null) {
            DB = new Database();
        }
        return DB;
    }

    /**
     * @return the observable list of scores
     */
    public ObservableList<Score> getScores() {
        return scores;
    }

    /**
     * Initiates the connection with the database after checking the schema version :
     * creates the database if needed then sets the schema version to 1
     *
     * @return the connection to the database
     */
    private Connection getConnection() throws SQLException {
        int userVersion = 0;
        Connection connection = DriverManager.getConnection("jdbc:sqlite:awele.sqlite");
        try (PreparedStatement userVersionStatement = connection.prepareStatement("PRAGMA user_version");
             ResultSet resultset = userVersionStatement.executeQuery()) {
            if (resultset.next()) {
                userVersion = resultset.getInt(1);
            }
        }
        if (userVersion < 1) {
            try (PreparedStatement createScoreTableStatement = connection.prepareStatement("CREATE TABLE score (\n" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    date_time TIMESTAMP NOT NULL,\n" +
                    "    duration INTEGER NOT NULL,\n" +
                    "    virtual_player_name VARCHAR(255) NOT NULL,\n" +
                    "    human_player_name VARCHAR(255) NOT NULL,\n" +
                    "    virtual_player_score INTEGER NOT NULL,\n" +
                    "    human_player_score INTEGER NOT NULL,\n" +
                    "    winner_name VARCHAR(255) NOT NULL);")) {
                createScoreTableStatement.execute();
            }
            try (PreparedStatement userVersionStatement = connection.prepareStatement("PRAGMA user_version=1")) {
                userVersionStatement.execute();
            }
        }

        return connection;
    }

    /**
     * Inserts a score into the database
     *
     * @param score Score to insert
     */
    public void insertScore(Score score) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement insertScoreStatement = connection.prepareStatement(
                    "INSERT INTO score (date_time, duration, virtual_player_name, human_player_name,\n"
                    + " virtual_player_score, human_player_score, winner_name) VALUES\n" +
                    "(?, ?, ?, ?, ?, ?, ?);")) {
                insertScoreStatement.setTimestamp(1, score.getDate());
                insertScoreStatement.setInt(2, score.getDuration());
                insertScoreStatement.setString(3, score.getVirtualPlayerName());
                insertScoreStatement.setString(4, score.getHumanPlayerName());
                insertScoreStatement.setInt(5, score.getVirtualPlayerScore());
                insertScoreStatement.setInt(6, score.getHumanPlayerScore());
                insertScoreStatement.setString(7, score.getWinnerName());
                insertScoreStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        refreshScores();
    }

    /**
     * Get the scores ordered by human player score (best score before)
     * then duration (shortest before) then date time (most recent before)
     * and add them to the observable list of scores
     */
    public void refreshScores() {
        scores.clear();
        try (Connection connection = getConnection()) {
            try (PreparedStatement getScoreStatement = connection.prepareStatement(
                    "SELECT * FROM score ORDER BY human_player_score DESC, duration ASC, date_time DESC;")) {
                ResultSet result = getScoreStatement.executeQuery();
                while (result.next()) {
                    Score score = new Score();
                    score.setId(result.getInt("id"));
                    score.setDate(result.getTimestamp("date_time"));
                    score.setDuration(result.getInt("duration"));
                    score.setVirtualPlayerName(result.getString("virtual_player_name"));
                    score.setHumanPlayerName(result.getString("human_player_name"));
                    score.setVirtualPlayerScore(result.getInt("virtual_player_score"));
                    score.setHumanPlayerScore(result.getInt("human_player_score"));
                    score.setWinnerName(result.getString("winner_name"));
                    scores.add(score);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
