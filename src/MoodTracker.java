import java.util.Queue;

import components.queue.Queue1L;

public class MoodTracker {
    private static class MoodEntry {
        String mood;
        String note;
        int entryNumber;

        MoodEntry(String mood, String note, int entryNumber) {
            this.mood = mood;
            this.note = note;
            this.entryNumber = entryNumber;
        }

        @Override
        public String toString() {
            return "[Entry #" + this.entryNumber + "] Mood: " + this.mood + " | Note: "
                    + this.note;
        }
    }

    private Queue<MoodEntry> moodLog;
    private int entryCounter;

    public MoodTracker() {
        this.moodLog = new Queue1L<>();
        this.entryCounter = 0;
    }

}
