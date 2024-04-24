package tankrotationexample.menus;

import tankrotationexample.Launcher;
import tankrotationexample.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EndGamePanel extends JPanel {

    private BufferedImage menuBackground;
    private JButton start;
    private JButton exit;
    private Launcher lf;
    private int WinnerId;


    public EndGamePanel(Launcher lf, int winnerId) {
        this.lf = lf;
        if(winnerId == 1){
            menuBackground = Resources.getSprite("t1win");
        } else if(winnerId == 2){
            menuBackground = Resources.getSprite("t2win");
        }else{
            menuBackground = Resources.getSprite("menu");
        }

        this.setBackground(Color.BLACK);
        this.setLayout(null);

        start = new JButton("Restart");
        start.setFont(new Font("Courier New", Font.BOLD, 20));
        start.setBounds(200, 250, 150, 35);
        start.addActionListener((actionEvent -> {

            this.lf.setFrame("game");
        }));


        exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 20));
        exit.setBounds(200, 400, 150, 30);
        exit.addActionListener((actionEvent -> {
            this.lf.closeGame();
        }));


        this.add(start);
        this.add(exit);

    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
    }


}
