import java.util.ArrayList;
import java.util.Scanner;

/**
 * StudentGradeTracker
 * Single-file console program to add/manage student grades,
 * compute average, highest and lowest scores, and display a summary.
 *
 * To compile: javac StudentGradeTracker.java
 * To run:     java StudentGradeTracker
 */
public class StudentGradeTracker {

    static class Student {
        String name;
        double score;
        Student(String name, double score) {
            this.name = name;
            this.score = score;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Student> students = new ArrayList<>();

        while (true) {
            printMenu();
            System.out.print("Choose an option: ");
            String opt = sc.nextLine().trim();

            switch (opt) {
                case "1":
                    addStudent(sc, students);
                    break;
                case "2":
                    editStudent(sc, students);
                    break;
                case "3":
                    removeStudent(sc, students);
                    break;
                case "4":
                    viewAllStudents(students);
                    break;
                case "5":
                    showSummary(students);
                    break;
                case "6":
                    loadSampleData(students);
                    System.out.println("Sample data loaded.");
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }

            System.out.println(); // blank line between operations
        }
    }

    static void printMenu() {
        System.out.println("=== Student Grade Tracker ===");
        System.out.println("1) Add student");
        System.out.println("2) Edit student");
        System.out.println("3) Remove student");
        System.out.println("4) View all students");
        System.out.println("5) Summary (average / highest / lowest)");
        System.out.println("6) Load sample data");
        System.out.println("0) Exit");
    }

    static void addStudent(Scanner sc, ArrayList<Student> students) {
        System.out.print("Student name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Name cannot be empty. Cancelled.");
            return;
        }

        Double score = readScore(sc, "Score (0-100): ");
        if (score == null) {
            System.out.println("Invalid score. Cancelled.");
            return;
        }

        students.add(new Student(name, score));
        System.out.println("Added: " + name + " -> " + formatScore(score));
    }

    static void editStudent(Scanner sc, ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students to edit.");
            return;
        }
        viewAllStudents(students);
        Integer idx = readIndex(sc, students.size(), "Enter student number to edit: ");
        if (idx == null) return;

        Student s = students.get(idx);
        System.out.println("Editing " + s.name + " (current score: " + formatScore(s.score) + ")");
        System.out.print("New name (leave empty to keep): ");
        String newName = sc.nextLine().trim();
        if (!newName.isEmpty()) s.name = newName;

        Double newScore = readScoreOptional(sc, "New score (leave empty to keep): ");
        if (newScore != null) s.score = newScore;

        System.out.println("Updated: " + s.name + " -> " + formatScore(s.score));
    }

    static void removeStudent(Scanner sc, ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students to remove.");
            return;
        }
        viewAllStudents(students);
        Integer idx = readIndex(sc, students.size(), "Enter student number to remove: ");
        if (idx == null) return;

        Student removed = students.remove((int)idx);
        System.out.println("Removed: " + removed.name);
    }

    static void viewAllStudents(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("[No students yet]");
            return;
        }
        System.out.println("--- All students ---");
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            System.out.printf("%d) %s  -  %s%n", i + 1, s.name, formatScore(s.score));
        }
    }

    static void showSummary(ArrayList<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students. Summary unavailable.");
            return;
        }

        double sum = 0;
        double highest = Double.NEGATIVE_INFINITY;
        double lowest = Double.POSITIVE_INFINITY;
        ArrayList<String> highNames = new ArrayList<>();
        ArrayList<String> lowNames = new ArrayList<>();

        for (Student s : students) {
            sum += s.score;
            if (s.score > highest) {
                highest = s.score;
                highNames.clear();
                highNames.add(s.name);
            } else if (s.score == highest) {
                highNames.add(s.name);
            }
            if (s.score < lowest) {
                lowest = s.score;
                lowNames.clear();
                lowNames.add(s.name);
            } else if (s.score == lowest) {
                lowNames.add(s.name);
            }
        }

        double avg = sum / students.size();

        System.out.println("=== Summary Report ===");
        System.out.println("Students count: " + students.size());
        System.out.println("Average score: " + formatScore(avg));
        System.out.println("Highest score: " + formatScore(highest) + "  -  " + String.join(", ", highNames));
        System.out.println("Lowest  score: " + formatScore(lowest)  + "  -  " + String.join(", ", lowNames));
    }

    // Helpers -----------------------------------------------------

    static Double readScore(Scanner sc, String prompt) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        try {
            double v = Double.parseDouble(line);
            if (v < 0 || v > 100) {
                System.out.println("Score must be between 0 and 100.");
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    static Double readScoreOptional(Scanner sc, String prompt) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return null;
        try {
            double v = Double.parseDouble(line);
            if (v < 0 || v > 100) {
                System.out.println("Score must be between 0 and 100.");
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            System.out.println("Not a number.");
            return null;
        }
    }

    static Integer readIndex(Scanner sc, int size, String prompt) {
        System.out.print(prompt);
        String line = sc.nextLine().trim();
        try {
            int n = Integer.parseInt(line);
            if (n < 1 || n > size) {
                System.out.println("Number out of range.");
                return null;
            }
            return n - 1;
        } catch (NumberFormatException e) {
            System.out.println("Not a valid number.");
            return null;
        }
    }

    static String formatScore(double s) {
        if (s == (long) s) {
            return String.format("%d", (long) s);
        } else {
            return String.format("%.2f", s);
        }
    }

    static void loadSampleData(ArrayList<Student> students) {
        students.clear();
        students.add(new Student("Alice", 88.5));
        students.add(new Student("Bob", 73.0));
        students.add(new Student("Carlos", 95.0));
        students.add(new Student("Diana", 60.0));
        students.add(new Student("Eva", 95.0));
    }
}
