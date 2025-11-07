import p02.game.Board;
import p02.pres.GamePanel;
import p02.pres.SevenSegmentDigit;

import javax.swing.*;
import java.awt.*;

public class Autoslalom extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Autoslalom());
    }

    public Autoslalom() {
        setTitle("Autoslalom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLayout(new BorderLayout());

        SevenSegmentDigit ones = new SevenSegmentDigit();
        SevenSegmentDigit tens = new SevenSegmentDigit();
        SevenSegmentDigit hundreds = new SevenSegmentDigit();

        Board board = new Board(ones, tens, hundreds);
        JPanel gamePanel = new GamePanel(board);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 3));
        scorePanel.add(hundreds);
        scorePanel.add(tens);
        scorePanel.add(ones);

        add(scorePanel, BorderLayout.CENTER);
        add(gamePanel, BorderLayout.EAST);

        setVisible(true);
        gamePanel.requestFocusInWindow();
    }

}
