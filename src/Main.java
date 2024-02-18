import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main extends JFrame implements MouseListener {
    private final int STONE_SIZE = 90;
    private final Reversi reversi;
    private final JPanel panel;
    private final JTextField eventField;

    private Main(String title) {
        setTitle(title);
        setBounds(600, 100, 740, 830);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        reversi = new Reversi();

        panel = new JPanel();
        run();
        eventField = new JTextField(1);
        eventField.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 20));
        eventField.setHorizontalAlignment(JTextField.CENTER);
        eventField.setEditable(false);
        eventField.setBounds(0, STONE_SIZE * 8 + 23, STONE_SIZE * 8, 23);
    }

    public static void main(String[] args) {
        Main main = new Main("");
        main.setVisible(true);
    }

    private JTextField getReversiText(String stone, int x, int y) {
        JTextField textField = new JTextField(stone, 1);
        textField.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, STONE_SIZE));
        textField.setEditable(false);
        textField.setBounds(x, y, STONE_SIZE, STONE_SIZE);
        textField.addMouseListener(this);
        return textField;
    }

    private void run() {
        panel.removeAll();
        getContentPane().removeAll();
        panel.setLayout(null);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (reversi.getStoneLocated(i, j) == Reversi.Stone.Black) {
                    panel.add(getReversiText("●", i * STONE_SIZE, j * STONE_SIZE));
                } else if (reversi.getStoneLocated(i, j) == Reversi.Stone.White) {
                    panel.add(getReversiText("〇", i * STONE_SIZE, j * STONE_SIZE));
                } else {
                    panel.add(getReversiText("", i * STONE_SIZE, j * STONE_SIZE));
                }
            }
        }

        JTextField statusField = getjTextField();
        panel.add(statusField);

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private JTextField getjTextField() {
        JTextField statusField = new JTextField("NEXT:" + reversi.getPlayer() + "(" + reversi.getPlayer().getStone() + ")"
                + " WINNER:" + reversi.getWinner()
                + " 黒:" + reversi.getBlackStones()
                + " 白:" + reversi.getWhiteStones(), 1);
        statusField.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, 20));
        statusField.setHorizontalAlignment(JTextField.CENTER);
        statusField.setEditable(false);
        statusField.setBounds(0, STONE_SIZE * 8, STONE_SIZE * 8, 24);
        return statusField;
    }

    private void setEventField(String s) {
        eventField.setText(s);
        panel.add(eventField);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int stoneX = (e.getLocationOnScreen().x - panel.getLocationOnScreen().x) / STONE_SIZE;
        int stoneY = (e.getLocationOnScreen().y - panel.getLocationOnScreen().y) / STONE_SIZE;

        int player = reversi.play(stoneX, stoneY);
        run();
        switch (player) {
            case 0:
                setEventField("");
                break;
            case 1:
                setEventField("そこには置けません。");
                break;
            case 2:
                setEventField("終了です。");
                break;
            case 3:
                setEventField("詰みました。");
                break;
            case 4:
                setEventField("詰みました。PLAYERが入れ替わります。");
        }
        panel.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}