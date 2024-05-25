package arbol;

import java.util.Stack;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

class BinaryTree {
    Node root;

    // Método para construir el árbol a partir de una expresión en notación postfija
    Node constructTree(String[] postfix) {
        Stack<Node> stack = new Stack<>();
        Node t, t1, t2;

        // Recorre cada token de la expresión postfija
        for (String token : postfix) {
            // Si el token es un operador
            if (isOperator(token)) {
                t = new Node(token);

                // Elimina los dos nodos superiores de la pila
                t1 = stack.pop();
                t2 = stack.pop();

                // Asigna los nodos eliminados como hijos del nodo actual
                t.right = t1;
                t.left = t2;

                // Añade el subárbol a la pila
                stack.push(t);
            } else { // Si el token es un operando
                t = new Node(token);
                stack.push(t);
            }
        }

        // El último nodo de la pila es la raíz del árbol
        t = stack.peek();
        stack.pop();

        return t;
    }

    // Método para verificar si un string es un operador
    boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    // Métodos para los diferentes recorridos del árbol
    void inorder(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }
        if (isOperator(node.value)) {
            result.append("(");
        }
        inorder(node.left, result);
        result.append(node.value + " ");
        inorder(node.right, result);
        if (isOperator(node.value)) {
            result.append(")");
        }
    }

    void preorder(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }
        result.append(node.value + " ");
        preorder(node.left, result);
        preorder(node.right, result);
    }

    void postorder(Node node, StringBuilder result) {
        if (node == null) {
            return;
        }
        postorder(node.left, result);
        postorder(node.right, result);
        result.append(node.value + " ");
    }

    // Método para convertir una expresión infija a postfija usando el algoritmo Shunting Yard
    String[] infixToPostfix(String infix) {
        Stack<String> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        StringTokenizer tokens = new StringTokenizer(infix, "+-*/() ", true);

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.isEmpty()) {
                continue;
            }
            if (isOperator(token)) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.pop();
            } else {
                postfix.append(token).append(' ');
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString().trim().split("\\s+");
    }

    // Método para obtener la precedencia de los operadores
    int precedence(String op) {
        switch (op) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                return -1;
        }
    }

    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();

        // Solicitar la expresión al usuario
        String input = JOptionPane.showInputDialog("Ingrese la expresión aritmética en notación infija:");
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Expresión inválida");
            return;
        }

        // Convertir la expresión infija a postfija
        String[] postfix = tree.infixToPostfix(input);
        tree.root = tree.constructTree(postfix);

        // Mostrar el menú al usuario
        String[] options = {"Inorden", "Preorden", "Posorden"};
        int choice = JOptionPane.showOptionDialog(null, "Seleccione el tipo de recorrido:", "Opciones de Recorrido",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        // Realizar el recorrido seleccionado y mostrar el resultado
        StringBuilder result = new StringBuilder();
        switch (choice) {
            case 0:
                tree.inorder(tree.root, result);
                break;
            case 1:
                tree.preorder(tree.root, result);
                break;
            case 2:
                tree.postorder(tree.root, result);
                break;
            default:
                JOptionPane.showMessageDialog(null, "Opción inválida");
                return;
        }

        JOptionPane.showMessageDialog(null, "El recorrido es: " + result.toString().trim());
    }
}