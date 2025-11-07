package p02.pres;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SevenSegmentDigit extends JPanel {
    private int value;
    private List<Runnable> observers;
    //StartEvent
    public SevenSegmentDigit() {
        this.value = 0;
        this.observers = new ArrayList<>();
    }
    public void addObserver(Runnable observer) {
        this.observers.add(observer);
    }
    private void notifyObservers() {
        for (Runnable observer : observers) {
            observer.run();
        }
    }
    //ResetEvent
    public void reset() {
        this.value = 0;
        repaint();
        notifyObservers();
    }
    //PlusOneEvent
    public void increment() {
        this.value++;
        if (this.value > 9) {
            this.value = 0;
            notifyObservers();
        }
        repaint();
    }
    private static final boolean[][] DIGITS = {
            {true, true, true, true, true, true, false}, //0
            {false, true, true, false, false, false, false}, //1
            {true, true, false, true, true, false, true}, //2
            {true, true, true, true, false, false, true}, //3
            {false, true, true, false, false, true, true}, //4
            {true, false, true, true, false, true, true}, //5
            {true, false, true, true, true, true, true}, //6
            {true, true, true, false, false, false, false}, //7
            {true, true, true, true, true, true, true}, //8
            {true, true, true, true, false, true, true} //9
    };

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        Rectangle[] segments = {
                new Rectangle(10, 10, 40, 10),   // top
                new Rectangle(50, 20, 10, 40),   // top right
                new Rectangle(50, 70, 10, 40),   // bottom right
                new Rectangle(10, 110, 40, 10),   // bottom
                new Rectangle(0, 70, 10, 40),    // bottom left
                new Rectangle(0, 20, 10, 40),    // top left
                new Rectangle(10, 60, 40, 10)    // middle
        };
        boolean[] onSegments = DIGITS[value];
        for (int i = 0; i < 7; i++) {
            if (onSegments[i]) {
                g.fillRect(segments[i].x, segments[i].y, segments[i].width, segments[i].height);
            }
        }
    }
}