package recommender;

import java.util.*;
import java.util.stream.Collectors;

public class RecommendationEngine {

    public static List<Book> recommend(
            List<Book> books,
            String preferredGenre,
            String preferredAuthor) {

        Map<Book, Integer> scores = new HashMap<>();

        for (Book book : books) {
            int score = 0;

            if (preferredGenre != null &&
                    book.getGenre().equalsIgnoreCase(preferredGenre)) {
                score += 2;
            }

            if (preferredAuthor != null &&
                    !preferredAuthor.isEmpty() &&
                    book.getAuthor().equalsIgnoreCase(preferredAuthor)) {
                score += 3;
            }

            if (score > 0) {
                scores.put(book, score);
            }
        }

        return scores.entrySet()
                .stream()
                .sorted(Map.Entry.<Book, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
