import java.awt.*;
import javax.swing.*;

public class DoublePendulum extends JPanel implements Runnable {

	//constants
  private int L = 100;             		    //length of rod
  private double M1 = 1;            			//mass of inner bob
  private double M2 = 1;              		//mass of outer bob
	private double G = 9.81;					//force of gravity
	private double dt = 0.01;					//step height

	//variables
  private double t1 = Math.PI / 2;  			//initial angle of inner bob
  private double t2 = Math.PI;   			//initial angle of outer bob
	private double w1 = 0;						//initial angular velocity of inner bob
	private double w2 = 0;						//initial angular velocity of outer bob
	private double p1;
	private double p2;
  private double p1dot;             //is this truly necessary
  private double p2dot;             //is this truly necessary

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
        int outerX = innerX + (int) (Math.sin(t2) * L);
        int outerY = innerY + (int) (Math.cos(t2) * L);

        g.drawLine(anchorX, anchorY, innerX, innerY);
        g.drawLine(innerX, innerY, outerX, outerY);
        g.fillOval(anchorX - 3, anchorY - 4, 7, 7);
        g.fillOval(innerX - 7, innerY - 7, 14, 14);
        g.fillOval(outerX - 7, outerY - 7, 14, 14);
    }

    public void run() {
        double angleAccel, angleVelocity = 0, dt = 0.1; //remember there's a random dt here
        while (true) {
            //angleAccel = -9.81 / L * Math.sin(t1);
            //angleVelocity += angleAccel * dt;
            //t1 += angleVelocity * dt;

						////////////////
						///in here is you run the rungeKutta method
            double newVar[] = rungeKutta(1); //the one is the "total time" or something
            t1 = newVar[0];
            t2 = newVar[1];
            p1 = newVar[2];
            p2 = newVar[3];
						///////////////

            repaint();
            try { Thread.sleep(1); } catch (InterruptedException ex) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 400);
    }

		public double[] hamilton(double[] variables) {

				// are these supposed to actually be declared or nah?
				double t1 = variables[0];
				double t2 = variables[1];
				double p1 = variables[2];
				double p2 = variables[3];

				double A1 = (p1 * p2 * Math.sin(t1 - t2)) / (L*L * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				double A2 = (M2 * L*L * p1*p1 + (M1 + M2) * L*L * p2*p2 - 2 * M2 * L*L * p1 * p2 * Math.cos(t1-t2)) /
							(2 * Math.pow(L, 4) * Math.pow((M1 + M2 * Math.pow(Math.sin(t1-t2), 2)), 2));

				w1 = (p1 * L - p2 * L * Math.cos(t1 - t2)) / (Math.pow(L, 3) * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				w2 = (- M2 * L * p1 * Math.cos(t1 - t2) + (M1 + M2) * L * p2) / (Math.pow(L, 3) * M2 * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				p1dot = - (M1 + M2) * G * L * Math.sin(t1) - A1 + A2 * Math.sin(2*(t1-t2));
				p2dot = - M2 * G * L * Math.sin(t2) + A1 - A2 * Math.sin(2*(t1-t2));

				double[] hamVar = {w1, w2, p1dot, p2dot};
				return hamVar;
		}

		//is xn the total time?
		//ynplus1 will actually be an array at this rate
		public double[] rungeKutta(double xn) {
				dt = this.dt; //I really hope this works

        double[] baseVar = {this.t1, this.t2, this.p1, this.p2}; //initial variables (yn)
        double[] interVar = new double[baseVar.length];
        double[] newVar = new double[baseVar.length];            //updated variables (yn+1)
        double[][] k = new double[4][baseVar.length];

        k[0] = hamilton(baseVar);

        interVar = intermediate(baseVar, k[0], 2);
        k[1] = hamilton(interVar);

        interVar = intermediate(baseVar, k[1], 2);
        k[2] = hamilton(interVar);

        interVar = intermediate(baseVar, k[2], 1);
        k[3] = hamilton(interVar);

        for (int i = 0; i < baseVar.length; i++) {
          newVar[i] = baseVar[i] + dt * (k[0][i]/6 + k[1][i]/3 + k[2][i]/3 + k[3][i]/6);
        }

        return newVar;
		}

    public double[] intermediate(double[] base, double[] k, double div) {
      dt = this.dt;   //again, I hope this works

      double[] inter = new double[base.length];
      for (int i = 0; i < base.length; i++) {
        inter[i] = base[i] + (k[i] * dt) / div;
      }
      return inter;
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
