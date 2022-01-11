package be;


public class  Category {
    private String name;

    /**
     * A constructor for a category object, used for categorize movies
     * @param name a String
     */
    public Category(String name){
        setName(name);
    }

    /**
     * used for getting the id of a movie
     * @return name a String
     */
    public String getName() {
        return name;
    }

    /**
     * used for setting the name of a movie object
     * @param name a String
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
