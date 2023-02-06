package sample;


import javafx.application.Application;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class HelloApplication extends Application {
    int score = 0;
    int mistakes = 0;
    boolean gameOver = false;
    Random random = new Random();
    String[] recipes = new String[]{"marshmallows","s'mores","pizza","salad","burger","cake","blue stuff","grilled cheese"};
    SimpleStringProperty recipe = new SimpleStringProperty(this, "recipe", "");
    SimpleBooleanProperty gameStart = new SimpleBooleanProperty(this, "gameStart", false);
    SimpleBooleanProperty kitchenOpen = new SimpleBooleanProperty(this, "kitchenOpen", false);
    String[] pantryItems = new String[]{"bread","flour","crackers","pepperoni","chocolate","marinara sauce","marshmallows",
            "cheese","lettuce","patty","dressing","sugar","eggs","icing","methylamine"};
    Button[] pantry = new Button[pantryItems.length];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        for (int i=0; i< pantryItems.length; i++) {
            pantry[i] = new Button(pantryItems[i]);
        }

        primaryStage.setTitle("Cooking Game");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 600, 775);
        primaryStage.setScene(scene);

        Text scenetitle = new Text("Welcome to the Cooking Game!");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label recipeLabel = new Label("Recipe: " + recipe.get());
        recipeLabel.textProperty().bind(new ObjectBinding<String>() {
            {bind(recipe);}
            @Override
            protected String computeValue() {
                return "Recipe: "+ recipe.get();
            }
        });
        grid.add(recipeLabel, 0, 1);

        Label scoreLabel = new Label("Score: " + score);
        grid.add(scoreLabel, 1, 1);

        Label mistakeLabel = new Label("Mistakes: " + mistakes);
        grid.add(mistakeLabel, 0, 2);

        Button startBtn = new Button("Start Cooking");
        startBtn.setOnAction(e -> gameStart.setValue(true));
        startBtn.disableProperty().bind(gameStart);
        startBtn.textProperty().bind(new ObjectBinding<String>() {
            {bind(gameStart);}
            @Override
            protected String computeValue() {
                return gameStart.get()?"Let's go!":"Start Cooking";
            }
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(startBtn);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        for (int i=0; i< pantryItems.length; i++) {
            pantry[i].setOnAction(actionEvent -> tickGame(scoreLabel,actiontarget));
        }

        VBox kitchen = new VBox();
        Button kitchenBtn = new Button("Kitchen"); kitchenBtn.visibleProperty().bind(gameStart);
        kitchenBtn.setOnAction(actionEvent -> kitchenOpen.set(!kitchenOpen.get()));
        kitchenBtn.textProperty().bind(new ObjectBinding<String>() {
            {bind(kitchenOpen);}
            @Override
            protected String computeValue() {
                return kitchenOpen.get() ?"Close kitchen":"Open kitchen";
            }
        });
        VBox pantryBox = new VBox(pantry); pantryBox.visibleProperty().bind(kitchenOpen);
        //kitchen.getChildren().addAll(kitchenBtn,pantryBox);
        grid.add(kitchenBtn,0,7);
        grid.add(pantryBox,0,8);

        Button getRecipeBtn = new Button("Get recipe"); getRecipeBtn.visibleProperty().bind(gameStart);
        getRecipeBtn.setOnAction(actionEvent -> recipe.set(recipes[random.nextInt(recipes.length)]));
        grid.add(getRecipeBtn,1,7);

        primaryStage.show();
    }

    public void tickGame(Label scoreLabel, Text actiontarget) {
        if (!gameOver) {
            score++;
            scoreLabel.setText("Score: " + score);
        } else {
            actiontarget.setFill(Color.FIREBRICK);
            actiontarget.setText("Game Over! Please start a new game.");
        }
    }
}

