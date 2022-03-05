package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PFrame extends JFrame {

    public static PPanel pp = null;

    public static void main(String[] args){
        PFrame pf = new PFrame();
    }

    public PFrame() {
        super();
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());
        pp = new PPanel();
        super.setContentPane(pp);
        super.setTitle("そすうそすうそすうそすうそすうそすうそすうそすうそすうそすうそすうそすうそすうそすうそすう");
        super.setVisible(true);
        super.pack();
        super.requestFocus();
        pp.requestFocus();

    }

    public void changeTitle(String s) {
        super.setTitle(s);
    }

    /* targetの構成...
        素数が書いてあるファイルと合成数が書いてあるファイルのどちらかのファイルから1つ選び、（確率は50%）
        何行目かから1つ数字を取り出す。それがtargetの数字。

        当たり判定について...
        クリックもしくはドラッグされたとき、マウスがある座標を記憶する。
        また、場にある全てのボールの中心点の座標の情報をとってきて、マウスの座標との距離を三平方の定理を使って求める
        マウスがある座標から一番近い中心点を探して、その中心点を持つボールの半径より距離が短かったらあたっていると判断します。


         g.drawImage(image[1], 150 - 50, 150 - 50, 100, 100, this);
            g.drawString("9", 150 - 15, 150 + 18);
            g.drawImage(image[1], 150 - 50, 350 - 50, 100, 100, this);
            g.drawString("63", 150 - 29, 350 + 19);
            g.drawImage(image[1], 363 - 63, 363 - 63, 126, 126, this);
            g.drawString("169", 360 - 40, 360 + 22);
            g.drawImage(image[1], 575 - 75, 375 - 75, 150, 150, this);
            g.drawString("2048", 575 - 57, 375 + 20);


            ファイル読み込みは完成
     */




}
