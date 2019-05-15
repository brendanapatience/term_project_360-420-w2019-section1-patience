public class Testing {

  public static final double DT = 1;

  public static double[] hamilton(double[] variables) {
    double[] hamVar = new double[variables.length];
    for (int i = 0; i < variables.length; i++) {
      hamVar[i] = variables[i]*2;
    }
    return hamVar;
  }

  public static double[] intermediate(double[] base, double[] k, double div) {
    double[] inter = new double[base.length];
    for (int i = 0; i < base.length; i++) {
      inter[i] = base[i] + (k[i] * DT) / div;
    }
    return inter;
  }

  public static double[] rungeKutta(double t1, double t2, double p1, double p2) {
    double[] baseVar = {t1, t2, p1, p2};            //initial variables (yn)
    double[] interVar = new double[baseVar.length];
    double[] newVar = new double[baseVar.length];   //updated variables (yn+1)
    double[][] k = new double[4][baseVar.length];

    k[0] = hamilton(baseVar);

    interVar = intermediate(baseVar, k[0], 2);
    k[1] = hamilton(interVar);

    interVar = intermediate(baseVar, k[1], 2);
    k[2] = hamilton(interVar);

    interVar = intermediate(baseVar, k[2], 1);
    k[3] = hamilton(interVar);

////////////////////////////////////////just printing out to check
    for (int i = 0; i < baseVar.length; i++) {
      for (int j = 0; j < baseVar.length; j++) {
        System.out.println(k[i][j]);
      }
      System.out.println();
    }
///////////////////////////////////////////////////////////////////

    for (int i = 0; i < baseVar.length; i++) {
      newVar[i] = baseVar[i] + DT * (k[0][i]/6 + k[1][i]/3 + k[2][i]/3 + k[3][i]/6);
    }

    return newVar;
  }

  public static void main(String[] args) {
    ///////////////won't be necessary
    double t1 = 1.;
    double t2 = 2.;
    double p1 = 3.;
    double p2 = 4.;
    //////////////////////////////////

    double[] funstuff = rungeKutta(t1, t2, p1, p2);
    for (int i = 0; i < funstuff.length; i++) {
      System.out.println(funstuff[i]);
    }
  }

}
