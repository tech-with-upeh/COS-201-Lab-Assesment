import java.io.IOException;
import java.util.Scanner;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CourseManager manager = new CourseManager();
    private static final String DEFAULT_FILE = "courses.txt";

    public static void main(String[] args) {
       
        runMenu(); // recursive menu loop
        System.out.println("Goodbye!");
    }

    
    private static void runMenu() {
        printMenu();
        int choice = readIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                handleAddCourse();
                break;
            case 2:
                manager.displayAllCourses();
                break;
            case 3:
                handleSearchCourse();
                break;
            case 4:
                handleComputeTotalUnits();
                break;
            case 5:
                handleSaveToFile();
                break;
            case 6:
                handleLoadFromFile();
                break;
            case 7:
                return; // base case - stop recursing
            case 8:
                clearConsole();
                break;
            default:
                System.out.println("Invalid option. Please choose a number from 1 to 8.");
        }

        runMenu(); // recursive call to show the menu again
    }

    private static void printMenu() {
    System.out.println("\n╔══════════════════════════════════════╗");
    System.out.println("║          COURSE MANAGEMENT MENU      ║");
    System.out.println("╠══════════════════════════════════════╣");
    System.out.println("║  [1]  Add Course                     ║");
    System.out.println("║  [2]  View All Courses               ║");
    System.out.println("║  [3]  Search Course by Code          ║");
    System.out.println("║  [4]  Compute Total Units            ║");
    System.out.println("║  [5]  Save to File                   ║");
    System.out.println("║  [6]  Load from File                 ║");
    System.out.println("║  [7]  Exit Program                   ║");
    System.out.println("║  [8]  Clear Console                  ║");
    System.out.println("╚══════════════════════════════════════╝");
    System.out.print("  Enter your choice: ");
}

    private static void clearConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO()
                        .start()
                        .waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing isn't supported in this environment, just skip it.
            System.out.println("(Could not clear console in this environment.)");
        }
    }

    private static void handleAddCourse() {
        System.out.print("Enter course code (e.g., COS201): ");
        String code = scanner.nextLine();
        System.out.print("Enter course title: ");
        String title = scanner.nextLine();
        int units = readIntInput("Enter number of units: ");

        try {
            manager.addCourse(code, title, units);
            System.out.println("Course added successfully.");
        } catch (DuplicateCourseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }

    private static void handleSearchCourse() {
        System.out.print("Enter course code to search: ");
        String code = scanner.nextLine();
        Course found = manager.searchCourse(code);
        if (found != null) {
            System.out.println("Course found: " + found);
        } else {
            System.out.println("No course found with code '" + code.trim().toUpperCase() + "'.");
        }
    }

    private static void handleComputeTotalUnits() {
        if (manager.isEmpty()) {
            System.out.println("No courses recorded yet.");
            return;
        }
        System.out.println("Total units: " + manager.computeTotalUnits());
    }

    private static void handleSaveToFile() {
        System.out.print("Enter filename to save (leave blank for '" + DEFAULT_FILE + "'): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = DEFAULT_FILE;
        }
        try {
            manager.saveToFile(filename);
            System.out.println("Courses saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void handleLoadFromFile() {
        System.out.print("Enter filename to load (leave blank for '" + DEFAULT_FILE + "'): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            filename = DEFAULT_FILE;
        }
        try {
            int count = manager.loadFromFile(filename);
            System.out.println("loaded " + count + " courses from " + filename);
        } catch (IOException e) {
            System.out.println("Error loading file: " + e.getMessage() +
                    " (make sure the file exists and has been saved before).");
        }
    }

    private static int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}