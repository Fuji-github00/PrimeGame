package com.company;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Component.*;
import java.awt.Container;
import java.util.*;
import java.io.*;
import java.util.Timer;
import javax.swing.*;
import javax.imageio.*;

public class PPanel extends JPanel {

    private final BufferedImage[] image = new BufferedImage[10];
    private final InputStream[] is = new InputStream[10];
    private final String[] image_name = {"title.jpg", "target.png", "heart.png"};

    private PMouseAdapter pma = null;
    private PKeyAdapter pka = null;

    private Target[] tg = new Target[20];

    private java.util.Timer timerThis = null;

    private final Font FONT_SCORE = new Font("Arial", Font.BOLD, 30);
    private final Font FONT_FINISH = new Font("Arial", Font.BOLD, 80);

    private final byte PHASE_TITLE = 0;
    private final byte PHASE_GAME = 1;
    private final byte PHASE_GAME_OVER = 2;
    private final byte PHASE_PRINT_SCORE = 3;

    private byte phase = 0;

    private final byte MODE_EASY = 1;
    private final byte MODE_NORMAL = 2;
    private final byte MODE_HARD = 3;
    private final byte MODE_ENDLESS = 4;
    private final byte MODE_SMASH = 5;

    private byte gameMode = 0;

    private int turn = 0;

    private int score = 0;

    private int heart = 0;

    private int target_article = 0;

    private boolean hasSleep = true;

    private Random r = new Random();

    public PPanel() {
        super();

        try {
            super.setPreferredSize(new Dimension(800, 600));
            super.setLayout(null);

            for (int i = 0; i < image_name.length; i++) {
                is[i] = this.getClass().getResourceAsStream(image_name[i]);
                image[i] = ImageIO.read(is[i]);
                is[i].close();
            }

            pma = new PMouseAdapter();
            super.addMouseListener(pma);
            super.addMouseMotionListener(pma);

            pka = new PKeyAdapter();
            this.addKeyListener(pka);

            /*
            timerThis = new java.util.Timer();
            timerThis.scheduleAtFixedRate(new TimerActionListener(), 1000l, 6l);
             */

            init();

        } catch(Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "ERROR : " + ex.toString());
        }
    }

    public void init() {
        phase = PHASE_TITLE;
        gameMode = 0;
        score = 0;
        heart = 0;

    }

    public void paint(Graphics g) {

        if (phase == PHASE_TITLE) {
            g.drawImage(image[0], 0, 0, 800, 600, this);
        }

        if (phase == PHASE_GAME) {
            g.clearRect(0, 0, 800, 600);
            g.setFont(FONT_SCORE);
            g.drawString("SCORE :  " + score, 550, 30);
            for (int i = 0; i < heart; i++) {
                g.drawImage(image[2], 33 * i + 3, 5, 30, 30, this);
            }

            if (CanPlusTurn()) {
                // ボールを作成するとき
                int random = r.nextInt(100);
                if (random < 50) {
                    try {
                        tg[searchNullTg()] = new Target(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    target_article++;
                } else {
                    try {
                        tg[searchNullTg()] = new Target(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    target_article++;
                    try {
                        tg[searchNullTg()] = new Target(2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    target_article++;
                }

                turn++;
            }

            for (int i = 0; i < tg.length; i++) {
                if (tg[i] == null) {
                    continue;
                }
                // 一番の核
                if (tg[i].isWillDelete()) {
                    // delete soon
                    g.setFont(new Font("Arial", Font.BOLD, 30));

                    if (tg[i].getSpa() <= 0) {
                        tg[i] = null;
                        target_article--;
                    } else {
                        tg[i].setSpa();
                        g.setColor(new Color(255, 0, 0, tg[i].getSpa()));
                        g.drawString(tg[i].getMessage(), tg[i].getPosition()[0], tg[i].getPosition()[1]);
                    }

                } else {
                    // moving
                    tg[i].move();
                    g.drawImage(image[1], tg[i].getPosition()[0] - tg[i].getRadius() / 2, tg[i].getPosition()[1] - tg[i].getRadius() / 2, tg[i].getRadius(), tg[i].getRadius(), this);
                    g.setFont(new Font("Arial", Font.BOLD, 53));
                    g.setColor(Color.BLACK);
                    g.drawString(String.valueOf(tg[i].getNumber()), tg[i].getPosition()[0] - tg[i].getStrDesign()[0], tg[i].getPosition()[1] + tg[i].getStrDesign()[1]);

                }
                try {
                    Thread.sleep(10 / target_article);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }

            if (this.heart == 0) {
                phase =PHASE_GAME_OVER;
            }

        }

        if (phase == PHASE_GAME_OVER) {
            g.clearRect(0, 0, 800, 600);
            setFont(FONT_FINISH);
            g.drawString("Finish!", 270, 300);
            setFont(FONT_SCORE);
            g.drawString("Total Score is ... " + score , 270, 400);
        }

        repaint();
    }


    public int searchNullTg() {
        // nullなtgを探して必要なとき順番にインスタンスを作る
        int returnNum = 0;
        for (int i = 0; i < tg.length; i++) {
            if (this.tg[i] == null) {
                returnNum = i;
                break;
            }
         }
        return returnNum;
    }

    public boolean CanPlusTurn() {
        // ターンを増やしても良いか
        boolean canPlusTurn = true;
        for (int i = 0; i< tg.length; i++) {
            if (tg[i] != null) {
                if (!tg[i].isWillDelete()) {
                    canPlusTurn = false;
                    break;
                }

            }
        }
        return canPlusTurn;
    }

    public void setHeart(byte num) {
        this.heart += num;
        if (this.heart >= 10) {
            this.heart = 10;
        }
    }

    public void setScore(int num) {
        if (this.score + num < 0) {
            this.score = 0;
        } else {
            this.score += num;
        }
    }


    private class PMouseAdapter extends MouseAdapter {

        private int x = 0;
        private int y = 0;

        public void mousePressed(MouseEvent me) {
            if (phase == PHASE_TITLE) {
                x = me.getX();
                y = me.getY();

                if (x >= 64 && x <= 378) {
                    if (y >= 184 && y <= 292) {
                        gameMode = MODE_EASY;
                       // phase = PHASE_GAME;
                    } else if (y >= 300 && y <= 424) {
                        gameMode = MODE_HARD;
                      //  phase = PHASE_GAME;
                    }
                } else if (x >= 419 && x <= 745) {
                    if (y >= 184 && y <= 292) {
                        gameMode = MODE_NORMAL;
                      //  phase = PHASE_GAME;
                    } else if (y >= 300 && y <= 424) {
                        gameMode = MODE_ENDLESS;
                        phase = PHASE_GAME;
                        heart = 7;
                        repaint();
                    }
                }
                if (x >= 232 && x <= 597) {
                    if (y >= 431 && y <= 561) {
                        gameMode = MODE_SMASH;
                       // phase = PHASE_GAME;
                    }
                }
                x = 0;
                y = 0;
            }

            if (phase == PHASE_GAME) {
                int distance_min = 1000001;
                int distance_min_i = 0;
                boolean hasPassFor = false;
                x = me.getX();
                y = me.getY();

                for (int i = 0; i < tg.length; i++) {
                    if (tg[i] == null) {
                        continue;
                    }
                    int dx = tg[i].getPosition()[0] - x;
                    int dy = tg[i].getPosition()[1] - y;
                    int distance = dx * dx + dy * dy;
                    if (distance < distance_min) {
                        distance_min = distance;
                        distance_min_i = i;
                        hasPassFor = true;
                    }
                }
                if (hasPassFor) {
                    if (Math.sqrt(distance_min) <= tg[distance_min_i].getRadius() / 2.0) {
                        tg[distance_min_i].pressed();
                        hasPassFor = false;
                    }
                }


            }
        }

        public void mouseDragged(MouseEvent me) {
            if (phase == PHASE_GAME) {
                int distance_min = 1000001;
                int distance_min_i = 0;
                boolean hasPassFor = false;
                x = me.getX();
                y = me.getY();

                for (int i = 0; i < tg.length; i++) {
                    if (tg[i] == null) {
                        continue;
                    }
                    int dx = tg[i].getPosition()[0] - x;
                    int dy = tg[i].getPosition()[1] - y;
                    int distance = dx * dx + dy * dy;
                    if (distance < distance_min) {
                        distance_min = distance;
                        distance_min_i = i;
                        hasPassFor = true;
                    }
                }
                if (hasPassFor) {
                    if (Math.sqrt(distance_min) <= tg[distance_min_i].getRadius() / 2.0) {
                        tg[distance_min_i].dragged();
                        hasPassFor = false;
                    }
                }
            }
        }

    }

    private class PKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {

        }
    }

    // デバッグメソッド
    public void p(String s) {
        System.out.println(s);
    }
    public void p(int n) {
        System.out.println(n);
    }


}
