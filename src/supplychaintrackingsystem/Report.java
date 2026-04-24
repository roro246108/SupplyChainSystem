package supplychaintrackingsystem;

public class Report {
    private final String title;

    public Report(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Report{" + "title='" + title + '\'' + '}';
    }
}
