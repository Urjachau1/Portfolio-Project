import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * A MoodTracker component that allows users to log, remove, and analyze their
 * mood history. This implementation follows OSU's component discipline using
 * the Queue1L data structure. It includes an interactive text-based user input
 * system.
 *
 * @author Urja Chauhan
 * @version 02/20/2025
 */
public class MoodTracker {

    /**
     * A nested class to represent an individual mood entry.
     */
    private static class MoodEntry {
        /** The recorded mood (e.g., Happy, Stressed). */
        private String mood;
        /** An optional note associated with the mood entry. */
        private String note;
        /**
         * A unique identifier for the entry (used as a timestamp alternative).
         */
        private int entryNumber;

        /**
         * Constructs a MoodEntry with the given mood, note, and entry number.
         *
         * @param mood
         *            The recorded mood.
         * @param note
         *            An optional note associated with the mood.
         * @param entryNumber
         *            The unique entry number.
         * @requires mood != null AND note != null
         * @ensures this.mood = mood AND this.note = note AND this.entryNumber =
         *          entryNumber
         */
        MoodEntry(String mood, String note, int entryNumber) {
            this.mood = mood;
            this.note = note;
            this.entryNumber = entryNumber;
        }

        /**
         * Returns a string representation of the mood entry.
         *
         * @return A formatted string containing the entry number, mood, and
         *         note.
         * @ensures toString = "[Entry #entryNumber] Mood: mood | Note: note"
         */
        @Override
        public String toString() {
            return "[Entry #" + this.entryNumber + "] Mood: " + this.mood
                    + " | Note: " + this.note;
        }
    }

    /** A queue to store mood entries in FIFO (First-In-First-Out) order. */
    private Queue<MoodEntry> moodLog;
    /** A counter to keep track of the number of logged moods. */
    private int entryCounter;

    /**
     * Constructs a MoodTracker instance with an empty mood log.
     *
     * @ensures this.moodLog = empty AND this.entryCounter = 0
     */
    public MoodTracker() {
        this.moodLog = new Queue1L<>();
        this.entryCounter = 0;
    }

    /**
     * Logs a new mood entry into the tracker.
     *
     * @param mood
     *            The mood recorded (e.g., Happy, Stressed).
     * @param note
     *            An optional note describing the user's mood.
     * @requires mood != null AND note != null
     * @ensures this.moodLog = #this.moodLog * <new MoodEntry>
     */
    public void logMood(String mood, String note) {
        MoodEntry entry = new MoodEntry(mood, note, ++this.entryCounter);
        this.moodLog.enqueue(entry);
    }

    /**
     * Removes the oldest mood entry from the tracker.
     *
     * @requires this.moodLog.length() > 0
     * @ensures this.moodLog = #this.moodLog[1, |this.moodLog| - 1]
     */
    public void removeLog() {
        if (this.moodLog.length() > 0) {
            this.moodLog.dequeue();
        }
    }

    /**
     * Returns the total number of mood entries logged.
     *
     * @return The number of mood logs currently stored.
     * @ensures numberOfLogs = |this.moodLog|
     */
    public int numberOfLogs() {
        return this.moodLog.length();
    }

    /**
     * Displays all logged moods.
     *
     * @ensures prints each logged mood entry in order
     */
    public void displayMoodHistory(SimpleWriter out) {
        if (this.moodLog.length() == 0) {
            out.println("No mood entries found.");
            return;
        }

        Queue<MoodEntry> temp = this.moodLog.newInstance();
        out.println("Mood History:");
        while (this.moodLog.length() > 0) {
            MoodEntry entry = this.moodLog.dequeue();
            out.println(entry);
            temp.enqueue(entry);
        }
        this.moodLog.transferFrom(temp);
    }

    /**
     * Analyzes mood trends and prints the count of positive and negative moods.
     *
     * @ensures maintains the state of this.moodLog AND prints analysis
     */
    public void analyzeMoodTrends(SimpleWriter out) {
        if (this.moodLog.length() == 0) {
            out.println("No mood data available for analysis.");
            return;
        }

        int positiveCount = 0, negativeCount = 0;
        Queue<MoodEntry> temp = this.moodLog.newInstance();

        while (this.moodLog.length() > 0) {
            MoodEntry entry = this.moodLog.dequeue();
            temp.enqueue(entry);

            // Convert mood string to lowercase manually
            String moodLower = "";
            for (int i = 0; i < entry.mood.length(); i++) {
                char c = entry.mood.charAt(i);
                if (c >= 'A' && c <= 'Z') {
                    c = (char) (c + ('a' - 'A')); // Convert uppercase to lowercase
                }
                moodLower += c;
            }

            if (moodLower.equals("happy") || moodLower.equals("excited")) {
                positiveCount++;
            } else if (moodLower.equals("sad")
                    || moodLower.equals("stressed")) {
                negativeCount++;
            }
        }

        this.moodLog.transferFrom(temp);

        out.println("Mood Analysis:");
        out.println("Positive Moods: " + positiveCount);
        out.println("Negative Moods: " + negativeCount);
    }

    /**
     * Runs an interactive console menu for the MoodTracker.
     *
     * @requires System.in is open
     * @ensures user can log moods, remove logs, view history, and analyze moods
     *          interactively
     */
    public void runMoodTracker() {
        // Create input and output streams using OSU's SimpleReader and SimpleWriter
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        // Boolean flag to control the loop
        boolean running = true;

        // Loop until the user chooses to exit
        while (running) {
            // Display the menu options
            out.println("Mood Tracker Menu:");
            out.println("1. Log Mood");
            out.println("2. View Mood History");
            out.println("3. Analyze Mood Trends");
            out.println("4. Remove Oldest Mood Entry");
            out.println("5. Exit");
            out.print("Choose an option: ");

            // Get user choice
            int choice = in.nextInteger();

            // Option 1: Log a new mood
            if (choice == 1) {
                out.print("Enter your mood (happy, sad, stressed, excited): ");
                String mood = in.nextLine(); // Get mood input
                out.print("Enter a note (optional): ");
                String note = in.nextLine(); // Get optional note input
                this.logMood(mood, note); // Log the mood
                out.println("Mood logged successfully!");

            } else { // Handle other options
                if (choice == 2) {
                    // Option 2: Display the mood history
                    this.displayMoodHistory(out);
                } else {
                    if (choice == 3) {
                        // Option 3: Analyze mood trends
                        this.analyzeMoodTrends(out);
                    } else {
                        if (choice == 4) {
                            // Option 4: Remove the oldest mood entry
                            this.removeLog();
                            out.println("Oldest mood entry removed.");
                        } else {
                            if (choice == 5) {
                                // Option 5: Exit the tracker
                                out.println(
                                        "Exiting Mood Tracker. Have a great day!");
                                running = false; // Set flag to false to stop the loop
                            } else {
                                // Handle invalid input
                                out.println(
                                        "Invalid choice. Please try again.");
                            }
                        }
                    }
                }
            }
        }

        // Close input and output streams
        in.close();
        out.close();
    }

    /**
     * Main method to start the interactive Mood Tracker.
     *
     * @param args
     *            Command-line arguments (not used).
     * @ensures starts interactive mood tracking session
     */
    public static void main(String[] args) {
        MoodTracker tracker = new MoodTracker();
        tracker.runMoodTracker();
    }
}
