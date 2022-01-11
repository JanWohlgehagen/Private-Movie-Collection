package be;


public class  Category {
    private int id;
    private String name;

    /**
     * A constructor for a category object, used for categorize movies
     * @param id an int
     * @param name a String
     */
    public Category(int id, String name){
        setId(id);
        setName(name);
    }

    /**
     * used for getting the id of a movie
     * @return id an int
     */
    public int getId() {
        return id;
    }

    /**
     * used for setting the id of a movie object
     * @param id an int
     */
    public void setId(int id) {
        this.id = id;
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
        return  id + " " + name;
    }
}
