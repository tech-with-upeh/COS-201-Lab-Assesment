import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CourseManager {
    private List<Course> courses;

    public CourseManager() {
        courses = new ArrayList<>();
    }

    public void addCourse(String code, String title, int units) throws DuplicateCourseException {
        String cleanCode = code.trim().toUpperCase();
        String cleanTitle = title.trim();

        if (cleanCode.isEmpty() || cleanTitle.isEmpty()) {
            throw new IllegalArgumentException("Course code and title cannot be empty.");
        }
        if (units <= 0 || units > 6) {
            throw new IllegalArgumentException("Units must be between 1 and 6.");
        }
        if (findCourseRecursive(cleanCode, 0) != null) {
            throw new DuplicateCourseException("A course with code '" + cleanCode + "' already exists.");
        }

        courses.add(new Course(cleanCode, cleanTitle, units));
    }

    public void displayAllCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses recorded yet.");
            return;
        }
        System.out.println("\n--- Registered Courses ---");
        System.out.printf("%-10s %-30s %-6s%n", "CODE", "TITLE", "UNITS");
        for (Course c : courses) {
            System.out.println(c);
        }
        System.out.println("---------------------------");
    }

    public Course searchCourse(String code) {
        return findCourseRecursive(code.trim().toUpperCase(), 0);
    }

    private Course findCourseRecursive(String code, int index) {
        if (index >= courses.size()) {
            return null; // base case: reached the end without a match
        }
        if (courses.get(index).getCode().equalsIgnoreCase(code)) {
            return courses.get(index);
        }
        return findCourseRecursive(code, index + 1); // recursive case
    }

    /**
     * Computes total units using a simple loop.
     */
    public int computeTotalUnits() {
        int total = 0;
        for (Course c : courses) {
            total += c.getUnits();
        }
        return total;
    }

    public boolean isEmpty() {
        return courses.isEmpty();
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Course c : courses) {
                writer.write(c.toFileFormat());
                writer.newLine();
            }
        }
    }


    public int loadFromFile(String filename) throws IOException {
        List<Course> loaded = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    loaded.add(Course.fromFileFormat(line));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipped invalid line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        for (Course course : loaded) {
            courses.add(course);
        }
        return loaded.size();
    }
}
