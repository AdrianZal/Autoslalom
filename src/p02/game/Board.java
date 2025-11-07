package p02.game;

import p02.pres.SevenSegmentDigit;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Board extends Thread implements KeyListener {
    private int[] road = {2, 0, 0, 0, 0, 0, 0};
    private int[] sideLines = {1, 0, 0, 1, 0, 0, 1};
    private int score;
    private int interval;
    private Runnable observer;
    private boolean running;
    private SevenSegmentDigit ones;
    private SevenSegmentDigit tens;
    private SevenSegmentDigit hundreds;
    private Thread gameThread;
    public Board(SevenSegmentDigit units, SevenSegmentDigit tens, SevenSegmentDigit hundreds) {
        this.ones = units;
        this.tens = tens;
        this.hundreds = hundreds;
        this.ones.addObserver(this::incrementTens);
        this.tens.addObserver(this::incrementHundreds);
    }
    public void addObserver(Runnable observer) {
        this.observer = observer;
    }
    private void notifyObservers() {
        if (observer != null) {
            observer.run();
        }
    }
    private void resetBoard() {
        this.road = new int[]{2, 0, 0, 0, 0, 0, 0};
        this.score = 0;
        this.interval = 1000;
        ones.reset();
        tens.reset();
        hundreds.reset();
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
        notifyObservers();
    }
    //TickEvent
    private void boardTick() {
        if ((this.road[0] & this.road[1]) != 0) {
            running = false;
            gameThread.interrupt();
        } else {
            //Add speed
            if (this.interval > 500) {
                this.interval -= 5;
            }

            for (int i = 0; i < 6; i++) {
                this.sideLines[i]=this.sideLines[i+1];
            }
            if(this.sideLines[5]==0 && this.sideLines[4]==0)this.sideLines[6]=1;
            else this.sideLines[6]=0;
            boolean empty = true;
            for (int i = 1; i < 7; i++) {
                if (this.road[i] != 0) {
                    empty = false;
                    break;
                }
            }
            if (empty) this.road[6] = (int) (Math.random() * 6) + 1;
            else {
                if (this.road[1] != 0) {
                    this.score++;
                    ones.increment();
                    if (score ==999) {
                        running = false;
                        gameThread.interrupt();
                    }
                }

                for (int i = 1; i < 6; i++) {
                    this.road[i] = this.road[i + 1];
                }
                this.road[6] = 0;

                if (this.score == 0) {
                    if (this.road[3] == 0 && this.road[4] == 0 && this.road[5] == 0)
                        this.road[6] = (int) (Math.random() * 6) + 1;
                } else if (this.score < 10) {
                    if (this.road[4] == 0 && this.road[5] == 0) this.road[6] = (int) (Math.random() * 6) + 1;
                } else if (this.score < 100) {
                    if (this.road[5] == 0) this.road[6] = (int) (Math.random() * 6) + 1;
                } else {
                    do {
                        this.road[6] = (int) (Math.random() * 6) + 1;
                    } while ((this.road[5] & this.road[6]) != 0);
                }
            }
        }
        notifyObservers();
    }
    public int[] getRoad() {
        return road;
    }
    public int[] getSideLines() {
        return sideLines;
    }
    private void incrementTens() {
        tens.increment();
    }
    private void incrementHundreds() {
        hundreds.increment();
    }
    @Override
    public void run() {
        while (running) {
            boardTick();
            try {
                Thread.sleep(this.interval);
            } catch (Exception e) {
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (running) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                road[0] = road[0] == 4 ? road[0] : road[0] << 1;
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                road[0] = road[0] == 1 ? road[0] : road[0] >> 1;
            }
            notifyObservers();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {
            if (!running) {
                resetBoard();
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
}
