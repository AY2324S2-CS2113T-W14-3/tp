package seedu.duke;

import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import static seedu.duke.storage.Storage.*;
import static seedu.duke.ui.Ui.printGreeting;
import static seedu.duke.ui.Ui.printUserGreeting;

import seedu.duke.user.User;
import seedu.duke.command.Command;
import seedu.duke.json.JsonManager;
import seedu.duke.modules.ModuleList;
import seedu.duke.parser.Parser;
import seedu.duke.ui.Ui;

public class FAP {

    public static User user = new User();
    public static ModuleList moduleList = new ModuleList();
    public static final Logger LOGGER = Logger.getLogger(FAP.class.getName());

    public static JsonManager jsonManager = new JsonManager();
    public static String filePath = Paths.get(System.getProperty("user.dir"), "data", "moduleList.txt").toString();

    public static void main(String[] args) {
        LOGGER.setLevel(Level.OFF);
        try {
            loadDataFromFile(filePath);
            user.resetModuleStatuses();
            if (user.getName().equals("")) {
                printGreeting();
            } else {
                printUserGreeting(user.getName(), user.getCurrentSemester(), user.getGraduationSemester());
            }
            assert moduleList != null : "moduleList should not be null";
            runApplication();
        } catch (AssertionError e) {
            LOGGER.log(Level.SEVERE, "Assertion failed: " + e.getMessage(), e);
            System.err.println("Critical assertion failure: " + e.getMessage());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred: " + e.getMessage(), e);
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void runApplication() {
        Ui ui = new Ui();
        boolean continueRunning = true;

        while (continueRunning) {
            try {
                String userInput = ui.getUserCommand();
                LOGGER.log(Level.INFO, "User input: " + userInput);
                Command command = Parser.getCommand(userInput);
                command.execute(userInput);
                user.resetModuleStatuses();
                saveModulesToFile(filePath);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred: " + e.getMessage());
                System.out.println("An error occurred: " + e.getMessage());
                continueRunning = false; // Exit loop on error
            }
        }
    }
}
