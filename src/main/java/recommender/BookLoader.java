package recommender;

import java.io.*;
import java.util.*;

public class BookLoader {

    public static List<Book> loadBooks() {
        List<Book> books = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        BookLoader.class.getResourceAsStream("/books.csv")))) {

            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                books.add(new Book(
                        Integer.parseInt(data[0]),
                        data[1],
                        data[2],
                        data[3]
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }
}
