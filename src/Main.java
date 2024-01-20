import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArraySet;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
        public static void main(String[] args) {
                Random r = new Random();
                CopyOnWriteArraySet<Integer> randNum = new CopyOnWriteArraySet<>();
                while(randNum.size() < 15){
                        randNum.add(r.nextInt(100));
                }
                System.out.println(randNum);
                RBTree<Integer> Tree = new RBTree<>();
                Iterator<Integer> randIt = randNum.iterator();
                int filenameCounter = 0;
                while(randIt.hasNext()){
                        int toInsert = randIt.next();
                        System.out.println("Inserting Node: " + toInsert);
                        Tree.insertNode(toInsert);
                        try {
                                Tree.printDot("tree_" + filenameCounter++ + ".dot");
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                        }
                }
        }
}