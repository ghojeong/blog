package algo.sort.code;

public class Insertion {

  public static char[] javaSort(char[] charArray) {
    java.util.Arrays.sort(charArray);
    return charArray;
  }

  public static char[] insertionSort(char[] charArray) {
    // TODO: 삽입 정렬 구현
    for (int i = 1; i < charArray.length; i++) {
      char temp = charArray[i];
      int i2 = i - 1;

      while ((i2 >= 0) && charArray[i2] > temp) {
        charArray[i2 + 1] = charArray[i2];
        i2--;
      }
      charArray[i2 + 1] = temp;
    }
    return charArray;
  }

  public static void main(String[] args) {
    String s = "neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur";

    String javaSorted = new String(javaSort(s.toCharArray()));
    String insertionSorted = new String(insertionSort(s.toCharArray()));

    System.out.println();
    System.out.println(javaSorted.equals(insertionSorted));
    System.out.println(javaSorted);
    System.out.println(insertionSorted);
    System.out.println();
  }
}