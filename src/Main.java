import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        int[] l = new int[15];
        for(int i = 0; i < l.length; i++)
            l[i] = r.nextInt(100);
        //l = new int[]{39, 22, 7, 99, 63, 76, 27, 58, 68, 16, 66, 30, 95, 89, 77};
        System.out.println(Arrays.toString(l));
        RBTree Tree = new RBTree();
        for(int i = 0; i < l.length; i++){
            System.out.println("Inserting Node: " + l[i]);
            Tree.insertNode(l[i]);
            try {
                Tree.printDot("tree_" + i + ".dot");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}