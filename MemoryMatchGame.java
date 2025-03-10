import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemoryMatchGame extends JFrame {
    private JButton[] buttons = new JButton[16];
    private String[] symbols = {"A", "A", "B", "B", "C", "C", "D", "D", 
                                "E", "E", "F", "F", "G", "G", "H", "H"};
    private String[] cardValues;
    private JLabel statusLabel;
    private JLabel movesLabel;
    private JLabel timeLabel;
    private JButton firstCard = null;
    private JButton secondCard = null;
    private int matchesFound = 0;
    private int moves = 0;
    private int secondsElapsed = 0;
    private Timer gameTimer;

    public MemoryMatchGame() {
        setTitle("Memory Match Game");
        setSize(400, 500); // Increased height for extra labels
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize and shuffle cards
        cardValues = symbols.clone();
        shuffleCards();

        // Game panel with 4x4 grid
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(4, 4, 5, 5));

        // Create buttons
        for (int i = 0; i < 16; i++) {
            buttons[i] = new JButton("?");
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 20));
            int index = i;
            buttons[i].addActionListener(e -> handleClick(index));
            gamePanel.add(buttons[i]);
        }

        // Status, moves, and time labels
        statusLabel = new JLabel("Click a card to start!", SwingConstants.CENTER);
        movesLabel = new JLabel("Moves: 0", SwingConstants.CENTER);
        timeLabel = new JLabel("Time: 0s", SwingConstants.CENTER);

        // Timer for elapsed time
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            timeLabel.setText("Time: " + secondsElapsed + "s");
        });
        gameTimer.start();

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetGame());

        // Header panel for labels
        JPanel headerPanel = new JPanel(new GridLayout(3, 1));
        headerPanel.add(statusLabel);
        headerPanel.add(movesLabel);
        headerPanel.add(timeLabel);

        // Add components to frame
        add(headerPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(resetButton, BorderLayout.SOUTH);
    }

    private void shuffleCards() {
        List<String> cardList = Arrays.asList(cardValues);
        Collections.shuffle(cardList);
        cardList.toArray(cardValues);
    }

    private void handleClick(int index) {
        JButton clickedButton = buttons[index];
        if (clickedButton.getText().equals("?") && firstCard != clickedButton) {
            clickedButton.setText(cardValues[index]);

            if (firstCard == null) {
                firstCard = clickedButton;
                statusLabel.setText("Pick another card.");
            } else {
                secondCard = clickedButton;
                moves++; // Increment moves when a pair is attempted
                movesLabel.setText("Moves: " + moves);
                checkMatch();
            }
        }
    }

    private void checkMatch() {
        if (firstCard.getText().equals(secondCard.getText())) {
            statusLabel.setText("Match found!");
            matchesFound++;
            firstCard = null;
            secondCard = null;
            if (matchesFound == 8) {
                statusLabel.setText("You won! All matches found!");
                gameTimer.stop();
                disableButtons();
            }
        } else {
            statusLabel.setText("No match. Try again.");
            Timer flipTimer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    firstCard.setText("?");
                    secondCard.setText("?");
                    firstCard = null;
                    secondCard = null;
                    statusLabel.setText("Pick a card.");
                }
            });
            flipTimer.setRepeats(false);
            flipTimer.start();
        }
    }

    private void resetGame() {
        shuffleCards();
        for (JButton button : buttons) {
            button.setText("?");
            button.setEnabled(true);
        }
        matchesFound = 0;
        moves = 0;
        secondsElapsed = 0;
        firstCard = null;
        secondCard = null;
        statusLabel.setText("Click a card to start!");
        movesLabel.setText("Moves: 0");
        timeLabel.setText("Time: 0s");
        gameTimer.restart();
    }

    private void disableButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MemoryMatchGame game = new MemoryMatchGame();
            game.setVisible(true);
        });
    }
}
