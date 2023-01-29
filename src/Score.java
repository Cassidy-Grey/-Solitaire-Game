/**
 * This is the Score class!
 * @author Cassidy
 * @version 1.1
 * */

public class Score implements Comparable<Score> {
    private String name;
    private final Integer points;

    /**
     * Constructor for a Score
     *
     * @param n - the name of the score-holder
     * @param p - The amount of points
     */
    public Score(String n, Integer p){
        this.name = n;
        this.points = p;
    }

    /**
     * This is the compareTo Method
     * It enables Scores to be sorted by amount of points!
     *
     * @param other - The Score to be compared to
     * */
    public int compareTo(Score other){
        return points.compareTo(other.points);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }
}
