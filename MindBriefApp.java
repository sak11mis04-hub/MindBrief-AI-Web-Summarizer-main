import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MindBriefApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MindBriefFrame().setVisible(true));
    }
}

class MindBriefFrame extends JFrame {
    private final JTextArea inputArea;
    private final JTextArea outputArea;
    private final SummaryService summaryService;

    public MindBriefFrame() {
        super("MindBrief – AI-Powered Text Summarizer");
        this.summaryService = new SummaryService(new TextRepository());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("MindBrief – AI-Powered Text Summarizer", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 12, 12));

        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setBorder(BorderFactory.createTitledBorder("Input Text"));

        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Summary"));

        center.add(inputScroll);
        center.add(outputScroll);
        add(center, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        JButton summarizeButton = new JButton("Generate Summary");
        JButton saveButton = new JButton("Save to SQL");
        JButton clearButton = new JButton("Clear");

        summarizeButton.addActionListener((ActionEvent e) -> summarizeText());
        saveButton.addActionListener((ActionEvent e) -> saveSummary());
        clearButton.addActionListener((ActionEvent e) -> clearFields());

        controls.add(summarizeButton);
        controls.add(saveButton);
        controls.add(clearButton);
        add(controls, BorderLayout.SOUTH);

        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel status = new JLabel("Ready");
        statusBar.add(status);
        add(statusBar, BorderLayout.EAST);

        setVisible(true);
    }

    private void summarizeText() {
        String text = inputArea.getText();
        if (text == null || text.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter some text first.", "Input required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String summary = summaryService.summarize(text);
        outputArea.setText(summary);
    }

    private void saveSummary() {
        String original = inputArea.getText();
        String summary = outputArea.getText();
        if (original == null || original.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No text available to save.", "Save error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SummaryEntry entry = new SummaryEntry(original, summary.isEmpty() ? summaryService.summarize(original) : summary);
        summaryService.saveSummary(entry);
        JOptionPane.showMessageDialog(this, "Summary saved successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearFields() {
        inputArea.setText("");
        outputArea.setText("");
    }
}

class SummaryEntry {
    private final String originalText;
    private final String summaryText;

    public SummaryEntry(String originalText, String summaryText) {
        this.originalText = originalText;
        this.summaryText = summaryText;
    }

    public String getOriginalText() { return originalText; }
    public String getSummaryText() { return summaryText; }
}

class TextRepository {
    public void save(SummaryEntry entry) {
        System.out.println("[SQL] Saved summary for text of length " + entry.getOriginalText().length());
    }

    public List<String> getHistory() {
        return List.of("Sample saved summary", "Another previous summary");
    }
}

class SummaryService {
    private final TextRepository repository;

    public SummaryService(TextRepository repository) {
        this.repository = repository;
    }

    public String summarize(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "";
        }

        String[] sentences = text.split("(?<=[.!?])\\s+");
        int targetSentences = Math.min(3, Math.max(1, sentences.length / 3));

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < targetSentences && i < sentences.length; i++) {
            String sentence = sentences[i].trim();
            if (!sentence.isEmpty()) {
                builder.append(sentence).append("\n\n");
            }
        }

        if (builder.length() == 0) {
            return text.substring(0, Math.min(250, text.length()));
        }

        return builder.toString().trim();
    }

    public void saveSummary(SummaryEntry entry) {
        repository.save(entry);
    }
}
