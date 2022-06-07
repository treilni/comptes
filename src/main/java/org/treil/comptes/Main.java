package org.treil.comptes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.treil.comptes.conf.UserPreferences;
import org.treil.comptes.events.SavedEvent;
import org.treil.comptes.finance.Account;
import org.treil.comptes.finance.Category;
import org.treil.comptes.finance.MonthList;
import org.treil.comptes.formatter.CentsFormatter;
import org.treil.comptes.parser.CsvParsedResult;
import org.treil.comptes.parser.CsvParser;
import org.treil.comptes.save.DataSaveException;
import org.treil.comptes.save.DataSaver;
import org.treil.comptes.widgets.MonthTab;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private final TabPane tabPane = new TabPane();
    private final ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n", Locale.getDefault());
    private final DataSaver saver;
    private Account account = new Account(0, new ArrayList<>());
    private MenuItem saveItem;
    private final UserPreferences userPreferences = new UserPreferences();

    @SuppressWarnings("UnstableApiUsage")
    public Main() {
        EventBus eventBus = new EventBus();
        saver = new DataSaver(eventBus);
        eventBus.register(new Object() {
            @SuppressWarnings("unused")
            @Subscribe
            private void onSaved(SavedEvent event) {
                saveItem.setDisable(false);
                userPreferences.setLastSavedFile(event.getFile());
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info(String.format("Starting %s", resourceBundle.getString("appTitle")));
        logger.debug("Using debug trace");

        // init scene
        VBox root = new VBox();
        Scene scene = new Scene(root, 900, 700);
        URL resource = getClass().getResource("/styles.css");
        String css = resource != null ? resource.toExternalForm() : null;
        scene.getStylesheets().add(css);

        // add menu bar
        root.getChildren().add(makeMenuBar(primaryStage));

        // add month tabs container
        root.getChildren().add(tabPane);

        // start
        primaryStage.setTitle(resourceBundle.getString("accounts"));
        primaryStage.setScene(scene);
        primaryStage.show();
        File lastSavedFile = userPreferences.getLastSavedFile();
        if (lastSavedFile != null) {
            loadAccountFile(lastSavedFile);
        }
    }


    private Node makeMenuBar(Stage stage) {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(makeFileMenu(stage));
        return menuBar;
    }

    private Menu makeFileMenu(Stage stage) {
        Menu menu = new Menu(resourceBundle.getString("file"));
        menu.getItems().add(getOpenItem(stage));
        saveItem = getSaveItem();
        saveItem.setDisable(true);
        menu.getItems().add(saveItem);
        menu.getItems().add(getSaveAsItem(stage));
        menu.getItems().add(getQuitItem("quit"));
        return menu;
    }

    @NotNull
    private MenuItem getQuitItem(@SuppressWarnings("SameParameterValue") String i18nCode) {
        MenuItem result = new MenuItem(resourceBundle.getString(i18nCode));
        result.setOnAction(event -> {
            saver.manageUnsaved();
            System.exit(0);
        });
        return result;
    }

    @NotNull
    private MenuItem getOpenItem(Stage stage) {
        MenuItem result = new MenuItem(resourceBundle.getString("open"));
        result.setOnAction(event -> {
            FileChooser fileChooser = getFileChooser();
            addExtensionFilter(fileChooser, "bank.file", CsvParser.CSV_SUFFIX, CsvParser.TXT_SUFFIX);
            fileChooser.setTitle(resourceBundle.getString("open.file"));
            File file = fileChooser.showOpenDialog(stage);
            if (file.getName().toLowerCase().endsWith(DataSaver.SUFFIX)) {
                loadAccountFile(file);
            } else {
                loadBankFile(file);
            }
        });
        return result;
    }

    private void loadAccountFile(@NotNull File f) {
        long startMs = System.currentTimeMillis();
        try {
            account = saver.load(f);
            account.setCategories(Category.makeSampleCategories());
            int size = account.getHistory().size();
            logger.info(String.format(" Done in %dms. Balance : %s / Expenses : %d",
                    System.currentTimeMillis() - startMs,
                    CentsFormatter.format(size > 0 ? account.getHistory().get(0).getStartBalanceCents() : 0),
                    account.getExpensesCount()));

            updateMonthTabs();
        } catch (IOException e) {
            logger.error(String.format("Parsing failed for file %s", f.getAbsolutePath()), e);
            // TODO warn
        }
    }

    private void loadBankFile(File file) {
        long startMs = System.currentTimeMillis();
        CsvParser parser = new CsvParser();
        CsvParsedResult parsedResult;
        try {
            parsedResult = parser.parse(file);
            logger.info(String.format(" Done in %dms. Balance : %s / Expenses : %d",
                    System.currentTimeMillis() - startMs,
                    CentsFormatter.format(parsedResult.getInitialBalanceCents()), parsedResult.getExpenseList().size()));

            account = new Account(parsedResult.getInitialBalanceCents(), parsedResult.getExpenseList());
            updateMonthTabs();
        } catch (IOException e) {
            logger.error("IO Exception", e);
        } catch (ParseException e) {
            logger.warn("Parse Exception", e);
        }
    }

    private void updateMonthTabs() {
        tabPane.getTabs().clear();
        account.getHistory().stream()
                .sorted(Comparator.comparing(MonthList::getMonth).reversed())
                .forEach(monthList -> {
                    MonthTab monthTab = new MonthTab(resourceBundle, monthList, account.getCategories());
                    tabPane.getTabs().add(monthTab);
                });
    }

    private void addExtensionFilter(FileChooser fileChooser, String i18nKey, String... extensions) {
        String[] exts = Arrays.stream(extensions).map(s -> "*." + s).toArray(String[]::new);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(resourceBundle.getString(i18nKey), exts));
    }

    @NotNull
    private FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        addExtensionFilter(fileChooser, "account.file", DataSaver.SUFFIX);
        File lastSaveDir = userPreferences.getLastSaveDir();
        if (lastSaveDir != null) {
            fileChooser.setInitialDirectory(lastSaveDir);
        }
        return fileChooser;
    }

    @NotNull
    private MenuItem getSaveAsItem(Stage stage) {
        MenuItem result = new MenuItem(resourceBundle.getString("save.as"));
        result.setOnAction(event -> {
            FileChooser fileChooser = getFileChooser();
            fileChooser.setTitle(resourceBundle.getString("save.as"));
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                doSave(file);
            }
        });
        return result;
    }

    @NotNull
    private MenuItem getSaveItem() {
        MenuItem result = new MenuItem(resourceBundle.getString("save"));
        result.setOnAction(event -> doSave(null));
        return result;
    }

    private void doSave(@Nullable File f) {
        try {
            if (f == null) {
                saver.saveAgain(account);
            } else {
                saver.save(account, f);
            }
        } catch (JsonProcessingException e) {
            showError("write.error", "formatting.error");
            logger.error("Error while saving existing", e);
        } catch (DataSaveException e) {
            switch (e.getType()) {
                case FORMER_RENAME:
                    showError("write.error", "could.not.rename.former");
                    break;
                case IO_EXCEPTION:
                    showError("write.error", e.getMessage());
                    break;
                case NO_FILE_SPECIFIED:
                    showError("write.error", "no.file.specified");
                    break;
            }
            logger.error("Error while saving existing", e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void showError(String headerI18n, String messageI18n) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resourceBundle.getString("error"));
        alert.setHeaderText(resourceBundle.getString(headerI18n));
        alert.setContentText(resourceBundle.getString(messageI18n));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
