import java.awt.*;
import javax.swing.*;

public class DoublePendulum extends JPanel implements Runnable {

    private int L = 100;              //length of rod
    private double M1 = 1;            //mass of inner mass
    private double M2 = 1;              //mass of outer mass
    private double THETA1 = Math.PI / 2;  //angle of inner mass
    private double THETA2 = Math.PI /2;   //angle of outer mass
    private int i = 1;

    public DoublePendulum() {
        this.L = L;
        this.M1 = M1;
        this.M2 = M2;
        this.THETA1 = THETA1;
        this.THETA2 = THETA2;
        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        int anchorX = getWidth() / 2, anchorY = getHeight() / 4;

        //First mass (inner means ball)
        int innerX = anchorX + (int) (Math.sin(THETA1) * L);
        int innerY = anchorY + (int) (Math.cos(THETA1) * L);

        //Second mass
        int outerX = innerX + (int) (Math.sin(THETA1) * L);
        int outerY = innerY + (int) (Math.cos(THETA1) * L);

        g.drawLine(anchorX, anchorY, innerX, innerY);
        g.drawLine(innerX, innerY, outerX, outerY);
        g.fillOval(anchorX - 3, anchorY - 4, 7, 7);
        g.fillOval(innerX - 7, innerY - 7, 14, 14);
        g.fillOval(outerX - 7, outerY - 7, 14, 14);
    }

    public void run() {
        double angleAccel, angleVelocity = 0, dt = 0.1;
        while (true) {
            angleAccel = -9.81 / L * Math.sin(THETA1);
            angleVelocity += angleAccel * dt;
            THETA1 += angleVelocity * dt;
            repaint();
            try { Thread.sleep(15); } catch (InterruptedException ex) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * L + 50, L / 2 * 3);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame("Double Pendulum");
        DoublePendulum p = new DoublePendulum();
        f.add(p);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        new Thread(p).start();
    }
}
