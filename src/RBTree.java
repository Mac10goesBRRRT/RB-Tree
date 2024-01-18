import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

public class RBTree<T extends Comparable <T>> {

        private static final boolean RED = true;
        private static final boolean BLACK = false;
        Node<T> root;

        public void insertNode(T key) throws IllegalArgumentException {
                Node<T> node = root;
                Node<T> parent = null;
                while(node != null) {
                        parent = node;
                        if (key.compareTo(node.data) == 0) {
                                throw new IllegalArgumentException("BST already contains a node with key " + key);
                        }
                        node = (key.compareTo(node.data) < 0) ? node.left : node.right;
                }
                Node<T> newNode = new Node<>(key);
                newNode.color = RED;
                if(parent == null) {
                        root = newNode;
                        newNode.color = BLACK;
                        return;
                }
                if (key.compareTo(parent.data) < 0) {
                        parent.left = newNode;
                } else {
                        parent.right = newNode;
                }
                newNode.parent = parent;
                // Does nothing
                fixRedBlackPropertiesAfterInsert(newNode);
        }
        private void fixRedBlackPropertiesAfterInsert(Node<T> node){
                //Case 3:
                if(!checkUncleBlack(node) && node.parent.color == RED){
                        Node<T> grandparent = node.parent.parent;
                        grandparent.left.color = BLACK;
                        grandparent.right.color = BLACK;
                        grandparent.color = RED;
                        if(node.parent.parent.parent != null && node.parent.parent.parent.color == RED)
                                fixRedBlackPropertiesAfterInsert(node.parent.parent);
                        root.color = BLACK;
                }

                //Case 4:
                else if(checkInnerGrandson(node) && node.parent.color == RED){
                        //System.out.println("Case4");
                        case4Shuffle(node);
                }
                //Case 5:
                else if(!checkInnerGrandson(node) && node.parent.color == RED && checkUncleBlack(node)){
                        //System.out.println("Case5");

                        Node<T> grandfather = node.parent.parent;
                        Node<T> father = node.parent;
                        if(grandfather.left == father) {
                                rotateRight(node.parent.parent);

                        } else if(grandfather.right == father){
                                rotateLeft(node.parent.parent);
                        }
                        grandfather.color = RED; father.color = BLACK;
                        root.color = BLACK;
                        //if(node.parent.parent!=null) //just testing
                        //    case4Shuffle(node);
                }
        }

        private void case4Shuffle(Node<T> node){
                if(node.parent.left == node){
                        rotateRight(node.parent);
                        rotateLeft(node.parent);
                        node.color = BLACK;
                        if(node.left != null)
                                node.left.color = RED;
                } else {
                        rotateLeft(node.parent);
                        rotateRight(node.parent);
                        node.color = BLACK;
                        if(node.right != null)
                                node.right.color = RED;
                }
                root.color = BLACK;
        }
        private boolean checkInnerGrandson(Node<T> grandson){
                Node<T> father = grandson.parent;
                Node<T> grandfather = father.parent;
                if(grandfather == null)
                        return false;
                //Checks if the Node is an Inner Grandson
                return grandson == father.left && father == grandfather.right || grandson == father.right && father == grandfather.left;
        }

        //Check if one of the uncles is Black or Red
        private boolean checkUncleBlack(Node<T> grandson) {
                Node<T> father = grandson.parent;
                Node<T> grandfather = father.parent;
                //Does not exist, is not black
                if(grandfather == null)
                        return false;
                if(grandfather.left == father && (grandfather.right == null || grandfather.right.color == BLACK))
                        return true;
                if(grandfather.right == father && (grandfather.left == null || grandfather.left.color == BLACK))
                        return true;
                return false;
        }

        private void rotateLeft(Node<T> node){
                Node<T> parent = node.parent;
                Node<T> rightChild = node.right;

                node.right = rightChild.left;
                if(rightChild.left != null)
                        rightChild.left.parent = node;
                rightChild.left = node;
                node.parent = rightChild;

                replaceParentsChild(parent, node, rightChild);
        }

        private void rotateRight(Node<T> node){
                Node<T> parent = node.parent;
                Node<T> leftChild = node.left;

                node.left = leftChild.right;
                if(leftChild.right != null)
                        leftChild.right.parent = node;
                leftChild.right = node;
                node.parent = leftChild;

                replaceParentsChild(parent, node, leftChild);
        }

        private void replaceParentsChild(Node<T> parent, Node<T> oldChild, Node<T> newChild){
                if(parent == null)
                        root = newChild;
                else if(parent.left == oldChild)
                        parent.left = newChild;
                else if(parent.right == oldChild)
                        parent.right = newChild;
                else
                        throw new IllegalStateException("Node is not a child of its parent");

                if(newChild != null)
                        newChild.parent = parent;
        }

        public void printDot(String filename) throws IOException {
                String str = generateDot();
                BufferedWriter writer = new BufferedWriter(new FileWriter("./output/"+filename));
                writer.write(str);
                writer.close();
        }

        private String generateDot(){
                String header = """
                        digraph G {
                        \tgraph [ratio=.48];
                        \tnode [style=filled, color=black, shape=circle, width=.6\s
                        \t\tfontname=Helvetica, fontweight=bold, fontcolor=white,\s
                        \t\tfontsize=24, fixedsize=true];
                        \tordering="out";
                        """;
                StringBuilder coloredRed = new StringBuilder();
                StringBuilder nilLeaves = new StringBuilder();
                StringBuilder arrows = new StringBuilder();
                int numLeaves = 1;
                Deque<Node<T>> queue = new ArrayDeque<>();
                queue.offer(root);
                while(!queue.isEmpty()){
                        Node<T> current = queue.pop();
                        String l = "";
                        String r = "";
                        if(current.color == RED) {
                                if (coloredRed.isEmpty())
                                        coloredRed.append("\t").append(current.data);
                                else
                                        coloredRed.append(", ").append(current.data);
                        }
                        Node<T> left = current.left;
                        if(left == null) {
                                l = "n" + numLeaves;
                                if(nilLeaves.isEmpty())
                                        nilLeaves.append("\t").append(l);
                                else
                                        nilLeaves.append(", ").append(l);
                                numLeaves++;
                        } else {
                                l = left.data.toString();
                                queue.offer(left);
                        }
                        Node<T> right = current.right;
                        if(right == null) {
                                r = "n" + numLeaves;
                                if(nilLeaves.isEmpty())
                                        nilLeaves.append("\t").append(r);
                                else
                                        nilLeaves.append(", ").append(r);
                                numLeaves++;
                        } else {
                                r = right.data.toString();
                                queue.offer(right);
                        }
                        arrows.append("\t").append(current.data.toString()).append("->").append(l).append(", ").append(r).append(";\n");
                }
                if(!coloredRed.isEmpty())
                        coloredRed.append("\n\t[fillcolor=red];\n");
                nilLeaves.append("\n\t[label=\"NIL\", shape=record, width=.4,height=.25, fontsize=16];\n");
                return header + coloredRed + nilLeaves + arrows + "}";
        }

}

class Node <T extends Comparable <T>> {
        T data;
        Node<T> left;
        Node<T> right;
        Node<T> parent;

        //Red is true, Black is false
        boolean color;
        public Node(T data) {
                this.data = data;
        }
}
