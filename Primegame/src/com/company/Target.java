package com.company;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Target {

    private File file = new File("PrimeList.txt");
    private FileReader fr = new FileReader(file);
    private BufferedReader br = new BufferedReader(fr);

    private int number = 0;

    private int radius = 0;

    private int[] strDesign = new int[2];
    // Targetの真ん中に表示される文字の位置を決めるデザイン的なやつ

    private int[] position = {-100, -100};

    private byte isPrime = 0;
    // 1 素数　2　合成数　3　回復

    private String message = null;
    // 消えるときに表示される言葉

    private boolean willDelete = false;

    private int sleep = 0;

    private int spa = 255;

    private double value = 0.0;
    private int startX = 0;
    // 1.. ボールの動き方(関数の式に必要な定数)
    // 2.. ボールが下から飛び出すx座標
    // move()で使う動かすための定数

    // Targetの飛び方(飛ぶタイプ)
    private final byte TYPE_1 = 1;
    // 一つのボールが (Random(x) , 600)から y=(Random(a))をたどる
    private final byte TYPE_2 = 2;
    // 一つのボールが (800, Random(y)-300)からy=-0.25xをたどる
    private final byte TYPE_3 = 3;
    // 二つのボールが (250, 600) , (550, 600)からy=250, y=550をたどる
    private final byte TYPE_4 = 4;
    // 三つのボールが (200, 600) , (400, 600) , (600, 600)からy=200, y=400, y=600をたどる
    private int type = 0;

    private Random r = new Random();
    // インスタンスメソッド Targetが作られるとき

    private String text;

    private int j = 0;

    public Target(int type) throws IOException {

        int limted = r.nextInt(25)+1;
        this.makeNumber(limted);

        // そんなわけないやん

        if (this.number < 10) {
            this.radius = 100;
            strDesign[0] = 15;
            strDesign[1] = 18;
            this.sleep = 9;
        } else if (this.number < 100) {
            this.radius = 100;
            strDesign[0] = 29;
            strDesign[1] = 19;
            this.sleep = 9;
        } else if (this.number < 1000) {
            this.radius = 126;
            strDesign[0] = 44;
            strDesign[1] = 21;
            this.sleep = 13;
        } else if (this.number < 10000) {
            this.radius = 150;
            strDesign[0] = 57;
            strDesign[1] = 20;
            this.sleep = 15;
        }
        this.type = type;

    }

    public void makeNumber(int lim) throws IOException {
        // ファイルからの読み込み処理
        int FileLine = 0;

        while ((text = br.readLine()) != null) {
            if (FileLine == lim) {
                if (r.nextInt(100) < 50) {
                    this.number = Integer.parseInt(text);
                    isPrime = 1;
                } else {
                    this.number = Integer.parseInt(text) + 1;
                    isPrime = 2;
                }

            }
            FileLine++;
        }
        br.close();

    }

    public void move() {
        if (this.type == TYPE_1) {
            if (value == 0.0) {
                // 最初だけ
                startX = 150 + r.nextInt(500);
                position[0] = startX;
                position[1] = 600;
            } else {
                // 他
                position[1] = 600 - (int)(450 * Math.sin(value));
            }

            value += 0.01;
        }

        if (this.type == TYPE_2) {
            if (value == 0.0) {
                startX = 100 + r.nextInt(300);
                position[0] = startX;
                position[1] = 600;
            } else {
                position[0] = startX + (int)(20 * value * value);
                position[1] = 600 - (int)(450 * Math.sin(value));
            }
            value += 0.01;

        }

        if (this.type == TYPE_3) {

        }

        if (this.type == TYPE_4) {

        }

        if (position[1] > 600) {
            // どこにせよ失敗
            switch (isPrime) {
                case 1:
                    deleteThis(4);
                    break;

                case 2:
                    deleteThis(2);
                    break;

                case 3:
                    deleteThis(5);
            }
        }
    }

    public void pressed() {
        if (isPrime == 1) {
            // 成功 -> 消える  パターン1
            deleteThis(1);
        }
        if (isPrime == 2) {
            // 失敗 -> 消える　パターン2
            deleteThis(2);
        }
        if (isPrime == 3) {
            // 回復　-> 消える　パターン5
            deleteThis(5);
        }
    }

    public void dragged() {
        if (isPrime == 1) {
            // 失敗　-> 消える　パターン4
            deleteThis(4);
        }
        if (isPrime == 2) {
            // 成功　-> 2つに分解　パターン3
            deleteThis(3);
        }
        if (isPrime == 3) {
            // 回復　-> 消える　パターン5
            deleteThis(5);
        }

    }

    // 消えるときに呼ばれる
    public void deleteThis(int pattern) {
        willDelete = true;
        switch (pattern) {
            case 1:
                // 素数で、成功時
                this.message = "PRIME!";
                PFrame.pp.setScore(this.number);
                break;

            case 2:
                // 合成数で、失敗時
                this.message = "" + "..";
                PFrame.pp.setHeart((byte)-1);
                PFrame.pp.setScore(-1 * this.number);
                break;

            case 3:
                // 合成数で、成功時 -> 分解へ
                this.message = "";
                PFrame.pp.setScore(this.number);
                PFrame.pp.setScore(this.number);
                break;


            case 4:
                // 素数で、失敗時
                this.message = "PRIME..";
                PFrame.pp.setHeart((byte)-1);
                PFrame.pp.setScore(-1 * this.number);
                break;

            case 5:
                // 回復時
                this.message = "";
                PFrame.pp.setHeart((byte)1);
        }
        this.number = 0;
        this.radius = 0;
        this.j = 0;
        this.strDesign[0] = 0;
        this.strDesign[1] = 0;
        this.isPrime = 0;
        this.spa = 255;



    }

    public boolean isWillDelete() {
        return willDelete;
    }

    public int getRadius() {
        return radius;
    }

    public int getNumber() {
        return number;
    }

    public int[] getStrDesign() {
        return strDesign;
    }

    public String getMessage() {
        return message;
    }

    public int[] getPosition() {
        return position;
    }

    public int getSleep() {
        return sleep;
    }

    public void setSpa() {
        this.spa -= 3;
    }

    public int getSpa() {
        return spa;
    }
}
