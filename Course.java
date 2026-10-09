public class Course {
    private String code;
    private String title;
    private int units;

    public Course(String code, String title, int units) {
        this.code = code;
        this.title = title;
        this.units = units;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public int getUnits() {
        return units;
    }

    public String toFileFormat() {
        return code + "|" + title + "|" + units;
    }

    public static Course fromFileFormat(String line) throws IllegalArgumentException {
        String[] parts = line.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Malformed course record: " + line);
        }
        try {
            String code = parts[0].trim();
            String title = parts[1].trim();
            int units = Integer.parseInt(parts[2].trim());
            return new Course(code, title, units);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid unit value in record: " + line);
        }
    }

    @Override
    public String toString() {
        return String.format("%-10s %-30s %3d unit(s)", code, title, units);
    }
}
