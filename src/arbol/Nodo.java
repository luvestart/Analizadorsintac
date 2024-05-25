package arbol;

import javax.swing.*;
import java.util.Stack;

// Clase que representa un nodo en el árbol binario
class Node {
    String value;
    Node left, right;

    Node(String value) {
        this.value = value;
        this.left = this.right = null;
    }
}