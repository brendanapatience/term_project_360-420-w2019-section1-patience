import java.awt.*;
import javax.swing.*;

public class DoublePendulum extends JPanel implements Runnable {

	//constants
  private int L = 100;             		//length of rod (cm)
  private double M1 = 1;            	//mass of inner bob (kg)
  private double M2 = 1;              //mass of outer bob (kg)
	private double G = 9.81;					  //force of gravity (N/kg)
	private double DT = 0.01;					  //step height (s)
  private boolean difSol = false;      //differential equation solver: set to true for fourth order runge kutta, false for euler's

	//variables
  private double t1 = Math.PI /2;  	 	//initial angle of inner bob (rad)
  private double t2 = Math.PI /2;   	//initial angle of outer bob (rad)
	private double w1;					       	//angular velocity of inner bob (rad/s)
	private double w2;						      //angular velocity of outer bob (rad/s)
	private double p1;                  //angular momentum of inner bob (kg*m^2 / s)
	private double p2;                  //angular momentum of outer bob (kg*m^2 / s)
  private double p1dot;               //derivative of p1 (N*m)
  private double p2dot;               //derivative of p2 (N*m)

  private double kinetic;
  private double potential;
  private double mechanical;

    public DoublePendulum() {
        this.t1 = t1;
        this.t2 = t2;
				this.p1 = (M1 + M2) * w1 * L*L + M2 * w2 * L*L * Math.cos(t1-t2);
				this.p2 = M2 * w2 * L*L + M2 * w1 * L*L * Math.cos(t1-t2);
        setDoubleBuffered(true);
    }

    @Override
    //method for displaying the objects on the screen
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);
        int anchorX = getWidth() / 2, anchorY = getHeight() / 4;

        //First bob
        int innerX = anchorX + (int) (Math.sin(t1) * L);
        int innerY = anchorY + (int) (Math.cos(t1) * L);

        //Second bob
        int outerX = innerX + (int) (Math.sin(t2) * L);
        int outerY = innerY + (int) (Math.cos(t2) * L);

        g.drawLine(anchorX, anchorY, innerX, innerY);
        g.drawLine(innerX, innerY, outerX, outerY);
        g.fillOval(anchorX - 3, anchorY - 4, 7, 7);
        g.fillOval(innerX - 7, innerY - 7, 14, 14);
        g.fillOval(outerX - 7, outerY - 7, 14, 14);
    }

    public void run() {
        while (true) {
            if (difSol) {
                //4th order Runge-Kutta method
                double newVar[] = rungeKutta();
                t1 = newVar[0];
                t2 = newVar[1];
                p1 = newVar[2];
                p2 = newVar[3];
            } else {
                //Euler's method
                double newVar[] = euler();
                t1 = newVar[0];
                t2 = newVar[1];
                p1 = newVar[2];
                p2 = newVar[3];
            }

            calculateEnergies();

            //function to display the next frame
            repaint();
            try { Thread.sleep(1); } catch (InterruptedException ex) {}
        }
    }

    @Override
    public Dimension getPreferredSize() {
        //set dimensions of the created window
        return new Dimension(500, 400);
    }

    public double[] euler() {

        double[] baseVar = {this.t1, this.t2, this.p1, this.p2};
        double[] newVar = new double[baseVar.length];

        double[] increment = hamilton(baseVar);

        for (int i = 0; i < baseVar.length; i++) {
            newVar[i] = baseVar[i] + DT * increment[i];
        }

        return newVar;
    }

    public double[] rungeKutta() {

        double[] baseVar = {this.t1, this.t2, this.p1, this.p2}; //initial variables (yn)
        double[] interVar = new double[baseVar.length];          //intermediate variables
        double[] newVar = new double[baseVar.length];            //updated variables (yn+1)
        double[][] k = new double[4][baseVar.length];            //2d array containing the 4 k's of each variable

        k[0] = hamilton(baseVar);

        interVar = intermediate(baseVar, k[0], 2);
        k[1] = hamilton(interVar);

        interVar = intermediate(baseVar, k[1], 2);
        k[2] = hamilton(interVar);

        interVar = intermediate(baseVar, k[2], 1);
        k[3] = hamilton(interVar);

        //calculates the updated variables
        for (int i = 0; i < baseVar.length; i++) {
            newVar[i] = baseVar[i] + DT * (k[0][i]/6 + k[1][i]/3 + k[2][i]/3 + k[3][i]/6);
        }

        return newVar;
    }

    //this function is an intermediate step to be able to manipulate k in the rungeKutta method
    public double[] intermediate(double[] base, double[] k, double div) {

        double[] inter = new double[base.length];

        for (int i = 0; i < base.length; i++) {
            inter[i] = base[i] + (k[i] * DT) / div;
        }
        return inter;
		}

    public double[] hamilton(double[] variables) {

				double t1 = variables[0];
				double t2 = variables[1];
				double p1 = variables[2];
				double p2 = variables[3];

        //these are sections in the equations that repeat and are therefore clearer in a seperate variable
				double A1 = (p1 * p2 * Math.sin(t1 - t2)) / (L*L * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				double A2 = (M2 * L*L * p1*p1 + (M1 + M2) * L*L * p2*p2 - 2 * M2 * L*L * p1 * p2 * Math.cos(t1-t2)) /
							(2 * Math.pow(L, 4) * Math.pow((M1 + M2 * Math.pow(Math.sin(t1-t2), 2)), 2));

        //All Hamiltonian equations
				w1 = (p1 * L - p2 * L * Math.cos(t1 - t2)) / (Math.pow(L, 3) * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				w2 = (- M2 * L * p1 * Math.cos(t1 - t2) + (M1 + M2) * L * p2) / (Math.pow(L, 3) * M2 * (M1 + M2 * Math.pow(Math.sin(t1-t2), 2)));
				p1dot = - (M1 + M2) * G * L * Math.sin(t1) - A1 + A2 * Math.sin(2*(t1-t2));
				p2dot = - M2 * G * L * Math.sin(t2) + A1 - A2 * Math.sin(2*(t1-t2));

        //array with all the derivatives of the variables
				double[] hamVar = {w1, w2, p1dot, p2dot};
				return hamVar;
		}

    public void calculateEnergies() {

        potential = G * L * (M1 * (1 - Math.cos(t1)) + M2 * (2 - Math.cos(t1) - Math.cos(t2)));

        kinetic = 0.5 * (M1 + M2) * w1*w1 * L*L + 0.5 * M2 * w2*w2 * L*L +
                      M2 * w1 * w2 * L*L * Math.cos(t1-t2);

        mechanical = potential + kinetic;

        System.out.println(mechanical);
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
