package org.treil.comptes;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.finance.Account;
import org.treil.comptes.finance.MonthList;
import org.treil.comptes.formatter.CentsFormatter;
import org.treil.comptes.parser.CsvParsedResult;
import org.treil.comptes.parser.CsvParser;
import org.treil.comptes.widgets.MonthTab;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static final String OPEN_PARAM = "open";

    private TabPane tabPane;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n", Locale.getDefault());

    @Override
    public void start(Stage primaryStage) throws IOException, ParseException {
        logger.info(String.format("Starting %s", resourceBundle.getString("appTitle")));

        // init scene
        VBox root = new VBox();
        Scene scene = new Scene(root, 300, 275);
        URL resource = getClass().getResource("/styles.css");
        String css = resource.toExternalForm();
        scene.getStylesheets().add(css);

        // add menu bar
        root.getChildren().add(makeMenuBar());

        // add month tabs
        tabPane = new TabPane();
        root.getChildren().add(tabPane);

        // parse data
        Parameters parameters = getParameters();
        String fileName = parameters.getNamed().get(OPEN_PARAM);
        if (fileName != null) {
            long startMs = System.currentTimeMillis();
            CsvParser parser = new CsvParser();
            CsvParsedResult parsedResult = parser.parse(new File(fileName));
            logger.info(String.format(" Done in %dms. Balance : %s / Expenses : %d",
                    System.currentTimeMillis() - startMs,
                    CentsFormatter.format(parsedResult.getInitialBalanceCents()), parsedResult.getExpenseList().size()));

            Account account = new Account(parsedResult.getInitialBalanceCents(), parsedResult.getExpenseList());
            account.getHistory().stream()
                    .sorted(Comparator.comparing(MonthList::getMonth).reversed())
                    .forEach(monthList -> {
                        MonthTab monthTab = new MonthTab(resourceBundle, monthList);
                        tabPane.getTabs().add(monthTab);
                    });
        }

        // start
        primaryStage.setTitle(resourceBundle.getString("accounts"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private Node makeMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(makeFileMenu());
        return menuBar;
    }

    private Menu makeFileMenu() {
        Menu menu = new Menu(resourceBundle.getString("file"));
        menu.getItems().add(new MenuItem(resourceBundle.getString("open")));
        menu.getItems().add(new MenuItem(resourceBundle.getString("quit")));
        return menu;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
