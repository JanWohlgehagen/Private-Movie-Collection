package be;

import dal.db.DAOMovie;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DummyMovie {
    public DAOMovie daoMovie = new DAOMovie();

    public DummyMovie() throws IOException {
    }

    public static void main(String[] args) {

        LocalDate currentData = LocalDate.now();
        Date date = Date.from(currentData.minusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant());
        //DummyMovie dummyMovie = new DummyMovie();

        //Movie movie = new Movie("Dette er en test", 9.0, "En Path");

        //dummyMovie.daoMovie.createMovie("Dette er en test", 9.0, "En Path");
    }
}
