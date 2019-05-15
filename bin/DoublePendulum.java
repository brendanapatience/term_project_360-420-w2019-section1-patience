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
	private double w1 = 0;						//initial angular velocity of inner bob
	private double w2 = 0;						//initial angular velocity of outer bob
	private double p1;
	private double p2;

    public DoublePendulum() {
        this.L = L;
        this.M1 = M1;
        this.M2 = M2;
				this.G = G;
				this.dt= dt;
        this.t1 = t1;
        this.t2 = t2;
				this.p1 = (M1 + M2) * w1 * L*L + M2 * w2 * L*L * Math.cos(t1-t2);
				this.p2 = M2 * w2 * L*L + M2 * w1 * L*L * Math.cos(t1-t2);
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

    public void run() {
        double angleAccel, angleVelocity = 0, dt = 0.1;
        while (true) {
            angleAccel = -9.81 / L * Math.sin(t1);
            angleVelocity += angleAccel * dt;
            t1 += angleVelocity * dt;

						////////////////
						///in here is probably where you run the rungeKutta method
						///////////////

            repaint();
            try { Thread.sleep(15); } catch (InterruptedException ex) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(2 * L + 50, L / 2 * 3);
    }

		public double[] hamilton(double time, double[] equations) {

				//what do I do with time in here???? (or dt I mean maybe)

				/*
				// are these supposed to actually be declared or nah?
				double t1 = equations[0];
				double t2 = equations[1];
				double p1 = equations[2];
				double p2 = equations[3];


				double A1 = (p1 * p2 * Math.sin(t1 - t2)) / (L*L * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				double A2 = (M2 * L*L * p1*p1 + (M1 + M2) * L*L * p2*p2 - 2 * M2 * L*L * p1 * p2 * Math.cos(t1-t2)) /
							(2 * Math.pow(L, 4) * Math.pow((M1 + M2 * Math.pow(Math.sin(t1-t2), 2)), 2));

				w1 = p1 * L - p2 * L * Math.cos(t1 - t2);
				w2 = - M2 * L * p1 * Math.cos(t1 - t2) + (M1 + M2) * L * p2;
				p1dot = - (M1 + M2) * G * L * Math.sin(t1) - A1 + A2 * Math.sin(2*(t1-t2));
				p2dot = - M2 * G * L * Math.sin(t2) + A1 - A2 * Math.sin(2*(t1-t2));

				double[] array = {w1, w2, p1dot, p2dot};
				return array;
				*/
				double[] array = {1, 2, 3};
				return array;
		}

		//is xn the total time?
		//ynplus1 will actually be an array at this rate
		public double[] rungeKutta(double xn) {
				dt = this.dt; //I really hope this works

				double p1dot, p2dot; //not sure what to do with these
				double[] equations = {this.t1, this.t2, this.p1, this.p2};
				double div; //to take into account the factors that might change depending on the k

				double[] k1 = hamilton(xn, equations);
				equations = intermediate(equations, k1, 2);
				/*
				double[] k1 = dt * hamilton(xn, equations);
				double[] k2 = dt * hamilton(xn + dt/2, equations + k1/2);
				double[] k3 = dt * hamilton(xn + dt/2, equations + k2/2);
				double[] k4 = dt * hamilton(xn + dt, equations + k3);
				double[] ynplus1 = yn + k1/6 + k2/3 + k3/3 + k4/6; // + O(dt^5)???
				return ynplus1;
				*/
				return equations;
		}

		public double[] intermediate(double[] equations, double[] k, double div) {
				dt = this.dt; //Again, I hope this works
				for (int i = 0; i < equations.length; i++) {
						equations[i] = (k[i] * dt) / div;
				}
				return equations;
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
