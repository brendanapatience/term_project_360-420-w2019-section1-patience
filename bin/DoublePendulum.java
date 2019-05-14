import java.awt.*;
import javax.swing.*;

public class DoublePendulum extends JPanel implements Runnable {

	//constants
    private int L = 100;             		    //length of rod
    private double M1 = 1;            			//mass of inner bob
    private double M2 = 1;              		//mass of outer bob
	private double G = 9.81;					//force of gravity
	private double dt = 0.1;					//step height
	
	//variables
    private double t1 = Math.PI / 2;  			//initial angle of inner bob
    private double t2 = Math.PI /2;   			//initial angle of outer bob
	private double p1 = 0;						//initial momentum of inner bob
	private double p2 = 0;						//initial momentum of outer bob

    public DoublePendulum() {
        this.L = L;
        this.M1 = M1;
        this.M2 = M2;
		this.G = G;
		this.dt= dt;
        this.t1 = t1;
        this.t2 = t2;
		this.p1 = p1;
		this.p2 = p2;
        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        int anchorX = getWidth() / 2, anchorY = getHeight() / 4;

        //First mass (inner means ball)
        int innerX = anchorX + (int) (Math.sin(t1) * L);
        int innerY = anchorY + (int) (Math.cos(t1) * L);

        //Second mass
        int outerX = innerX + (int) (Math.sin(t1) * L);
        int outerY = innerY + (int) (Math.cos(t1) * L);

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
            angleAccel = -9.81 / L * Math.sin(t1);
            angleVelocity += angleAccel * dt;
            t1 += angleVelocity * dt;
						/*
						A1 = (p1 * p2 * Math.sin(t1 - t2)) / (L*L * (M1 + M2 * Math.pow(sin(t1-t2), 2)));
						A2 = (M2 * L*L * p1*p1 + (M1 + M2) * L*L * p2*p2 - 2 * M2 * L*L * p1 * p2 * Math.cos(t1-t2)) /
									(2 * Math.pow(L, 4) * Math.pow((M1 + M2 * Math.pow(sin(t1-t2), 2), 2));
						theta1dot = p1 * L - p2 * L * Math.cos(t1 - t2);
						theta2dot = - M2 * L * p1 * Math.cos(t1 - t2) + (M1 + M2) * L * p2;
						p1dot = - (M1 + M2) * G * L * Math.sin(t1) - A1 + A2 * Math.sin(2*(t1-t2));
						p2dot = - M2 * G * L * Math.sin(t2) + A1 - A2 * Math.sin(2*(t1-t2));
						*/
            repaint();
            try { Thread.sleep(15); } catch (InterruptedException ex) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * L + 50, L / 2 * 3);
    }

		public static double rungeKutta(double xn, double yn, double dt) {
				double k1, k2, k3, k4, ynplus1;
				k1 = dt * f(xn, yn);
				k2 = dt * f(xn + dt/2, yn + k1/2);
				k3 = dt * f(xn + dt/2, yn + k2/2);
				k4 = dt * f(xn + dt, yn + k3);
				ynplus1 = yn + k1/6 + k2/3 + k3/3 + k4/6; // + O(dt^5)???
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
