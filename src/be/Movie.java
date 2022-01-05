package be;

import java.text.DecimalFormat;
import java.util.Date;

public class Movie {
    private int id;
    private String name;
    private double IMDBRating;
    private String pathToFile;
    private Date lastView;
    private double personalRating;

    private DecimalFormat oneDecimal = new DecimalFormat("#.#");

    /**
     *  constructor for song used for creating an instance of a Movie.
     * @param id the int id of the movie.
     * @param name the name of the movie.
     * @param IMDBRating the IMDBRating of the movie.
     * @param pathToFile the local location of the movie.
     * @param lastView the date of the last time the movie was viewed.
     */
    public Movie(int id, String name, double IMDBRating, String pathToFile, Date lastView) {
        this.id = id;
        this.name = name;
        this.IMDBRating = IMDBRating;
        this.pathToFile = pathToFile;
        this.setLastView(null);
    }

    /**
     *  used for getting an id of the movie.
     * @return the id og movie.
     */
    public int getId() {
        return id;
    }

    /**
     * ysed for getting the name of the movie.
     * @return the name of the movie.
     */
    public String getName() {
        return name;
    }

    /**
     * used for getting IMDB rating of a movie as a double with one decimal.
     * @return the Artist of a song.
     */
    public double getIMDBRating() {
        return Double.valueOf(oneDecimal.format(this.IMDBRating));
    }

    /**
     * used for getting the location of the song.
     * @return the path to file used. as a string.
     */
    public String getPathToFile() {
        return pathToFile;
    }

    /**
     * used for getting the Date of when the song was viewed last.
     * @return returns a Date object of when the movie was last viewed.
     */
    public Date getLastView() {
        return lastView;
    }

    /**
     * sets the Date of when the movie was last viewed.
     * @param lastView
     */
    public void setLastView(Date lastView) {
        this.lastView = lastView;
    }

    /**
     * used for getting the personal rating of a movie as a double with one decimal.
     * @return returns a double with one decimal
     */
    public double getPersonalRating() {
        return Double.valueOf(oneDecimal.format(this.personalRating));
    }

    /**
     * used for setting the personal rating of a movie as a double.
     * @param personalRating
     */
    public void setPersonalRating(double personalRating) {
        this.personalRating = personalRating;
    }
}