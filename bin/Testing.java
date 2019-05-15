public class Testing {

  public static void main(String[] args) {
    double[] array = {1, 2, 3};
    double[] newarray = new double[array.length];

    for (int i = 0; i < array.length; i++) {
      newarray[i] = array[i] + 2;
    }

    for (int i = 0; i < array.length; i++) {
      System.out.println(newarray[i]);
    }
  }

}
