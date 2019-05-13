import java.awt.*;
import javax.swing.*;

public class DoublePendulum extends JPanel implements Runnable {

    private int L = 100;              //length of rod
    private double M1 = 1;            //mass of inner mass
    private double M2 = 1;              //mass of outer mass
    private double THETA1 = Math.PI / 2;  //angle of inner mass
    private double THETA2 = Math.PI /2;   //angle of outer mass
		private double P1 = 0;						//momentum of inner mass
		private double P2 = 0;						//momentum of outer mass
		private double h = 0.1;						//step height
		private double G = 9.81;					//force of gravity
    private int i = 1;

    public DoublePendulum() {
        this.L = L;
        this.M1 = M1;
        this.M2 = M2;
        this.THETA1 = THETA1;
        this.THETA2 = THETA2;
				this.P1 = P1;
				this.P2 = P2;
				this.h = h;
				this.G = G;
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

		//Hamiltonian Equations straight into here?
    public void run() {
        double angleAccel, angleVelocity = 0, dt = 0.1;
				//double theta1dot, theta2dot, p1dot, p2dot = 0.;
				//double A1, A2 = 0.;
        while (true) {
            angleAccel = -9.81 / L * Math.sin(THETA1);
            angleVelocity += angleAccel * dt;
            THETA1 += angleVelocity * dt;
						/*
						A1 = (P1 * P2 * Math.sin(THETA1 - THETA2)) / (L*L * (M1 + M2 * Math.pow(sin(THETA1-THETA2), 2)));
						A2 = (M2 * L*L * P1*P1 + (M1 + M2) * L*L * P2*P2 - 2 * M2 * L*L * P1 * P2 * Math.cos(THETA1-THETA2)) /
									(2 * Math.pow(L, 4) * Math.pow((M1 + M2 * Math.pow(sin(THETA1-THETA2), 2), 2));
						theta1dot = P1 * L - P2 * L * Math.cos(THETA1 - THETA2);
						theta2dot = - M2 * L * P1 * Math.cos(THETA1 - THETA2) + (M1 + M2) * L * P2;
						p1dot = - (M1 + M2) * G * L * Math.sin(THETA1) - A1 + A2 * Math.sin(2*(THETA1-THETA2));
						p2dot = - M2 * G * L * Math.sin(THETA2) + A1 - A2 * Math.sin(2*(THETA1-THETA2));
						*/
            repaint();
            try { Thread.sleep(15); } catch (InterruptedException ex) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * L + 50, L / 2 * 3);
    }

		public static double rungeKutta(double xn, double yn, double h) {
				double k1, k2, k3, k4, ynplus1;
				k1 = h * f(xn, yn);
				k2 = h * f(xn + h/2, yn + k1/2);
				k3 = h * f(xn + h/2, yn + k2/2);
				k4 = h * f(xn + h, yn + k3);
				ynplus1 = yn + k1/6 + k2/3 + k3/3 + k4/6; // + O(h^5)???
				return ynplus1;
		}

		//random function, adds stuff for now
		public static double f(double x, double y) {
				return x + y;
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
