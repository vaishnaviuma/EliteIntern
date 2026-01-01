package recommender;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class BookRecommendationUI extends Application {

    @Override
    public void start(Stage stage) {
        List<Book> books = BookLoader.loadBooks();

        ComboBox<String> genreBox = new ComboBox<>();
        genreBox.getItems().addAll(
                "Fantasy", "Self Help", "Fiction", "Thriller",
                "Finance", "Dystopian", "Non Fiction", "Biography"
        );

        TextField authorField = new TextField();
        authorField.setPromptText("Enter favorite author (optional)");

        Button recommendBtn = new Button("Recommend Books");

        ListView<Book> results = new ListView<>();

        recommendBtn.setOnAction(e -> {
            results.getItems().clear();
            results.getItems().addAll(
                    RecommendationEngine.recommend(
                            books,
                            genreBox.getValue(),
                            authorField.getText()
                    )
            );
        });

        VBox layout = new VBox(10,
                new Label("Select Genre"),
                genreBox,
                new Label("Favorite Author"),
                authorField,
                recommendBtn,
                results
        );

        layout.setStyle("-fx-padding: 15;");
        stage.setScene(new Scene(layout, 450, 500));
        stage.setTitle("AI Book Recommendation System");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
