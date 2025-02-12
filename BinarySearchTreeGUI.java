import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchTreeGUI extends JFrame {

    private BinarySearchTree bst;
    private JPanel treePanel;
    private JTextField insertField;
    private JButton insertButton;
    private JTextField deleteField;
    private JButton deleteButton;

    private static final int NODE_DIAMETER = 30;
    private static final int HORIZONTAL_SPACING = 50;
    private static final int VERTICAL_SPACING = 70;

    public BinarySearchTreeGUI() {
        bst = new BinarySearchTree();

        setTitle("Binary Search Tree GRaphic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTree(g, bst.root, getWidth() / 2, 30, HORIZONTAL_SPACING);
            }
        };
        treePanel.setBackground(Color.WHITE);
        add(treePanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        insertField = new JTextField(5);
        insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(insertField.getText());
                    bst.add(new BinaryNode(value));
                    insertField.setText("");
                    treePanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BinarySearchTreeGUI.this, "Invalid input. Please enter an integer.");
                }
            }
        });
        controlPanel.add(new JLabel("Insert:"));
        controlPanel.add(insertField);
        controlPanel.add(insertButton);


        deleteField = new JTextField(5);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int value = Integer.parseInt(deleteField.getText());
                    bst.remove(value);
                    deleteField.setText("");
                    treePanel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(BinarySearchTreeGUI.this, "Invalid input. Please enter an integer.");
                }
            }
        });
        controlPanel.add(new JLabel("Delete:"));
        controlPanel.add(deleteField);
        controlPanel.add(deleteButton);


        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void drawTree(Graphics g, BinaryNode node, int x, int y, int horizontalSpacing) {
        if (node == null) {
            return;
        }

        if (node.left() != null) {
            int xLeft = x - horizontalSpacing;
            int yLeft = y + VERTICAL_SPACING;
            g.drawLine(x, y + NODE_DIAMETER / 2, xLeft, yLeft + NODE_DIAMETER / 2);
            drawTree(g, node.left(), xLeft, yLeft, horizontalSpacing / 2);
        }

        if (node.right() != null) {
            int xRight = x + horizontalSpacing;
            int yRight = y + VERTICAL_SPACING;
            g.drawLine(x, y + NODE_DIAMETER / 2, xRight, yRight + NODE_DIAMETER / 2);
            drawTree(g, node.right(), xRight, yRight, horizontalSpacing / 2);
        }

        g.setColor(Color.BLUE);
        g.fillOval(x - NODE_DIAMETER / 2, y - NODE_DIAMETER / 2, NODE_DIAMETER, NODE_DIAMETER);
        g.setColor(Color.WHITE);
        String value = String.valueOf(node.getValue());
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(value);
        g.drawString(value, x - textWidth / 2, y + fm.getHeight() / 4);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(BinarySearchTreeGUI::new);
    }
}

class BinaryNode {
    private BinaryNode left, right;
    private Comparable myValue;

    public BinaryNode(Comparable x) {
        myValue = x;
    }

    public String toString() {
        String temp = "Value:" + myValue +
                ", Left:" + (left == null ? null : left.myValue) +
                ", Right:" + (right == null ? null : right.myValue);
        return temp;
    }

    //other methods not shown.
    public Comparable getValue() {
        return myValue;
    }

    public BinaryNode left() {
        return left;
    }

    public BinaryNode right() {
        return right;
    }

    public void setLeft(BinaryNode left) {
        this.left = left;
    }

    public void setRight(BinaryNode right) {
        this.right = right;
    }

    public void setValue(Comparable myValue) {
        this.myValue = myValue;
    }

}


class BinarySearchTree {
    public BinaryNode root;
    private int count = 0;

    public BinarySearchTree() {
        root = null;
    }

    public void add(BinaryNode x) {
        if (root == null) {
            root = x;
            return;
        }
        add(root, x);
        count++;
    }

    private void add(BinaryNode parent, BinaryNode x) {
        if (parent == null) return;
        if (x.getValue().compareTo(parent.getValue()) < 0) {
            if (parent.left() == null) {
                parent.setLeft(x);
            } else {
                add(parent.left(), x);
            }
        } else {
            if (parent.right() == null) {
                parent.setRight(x);
            } else {
                add(parent.right(), x);
            }
        }
    }

    public String preOrder() {
        return preOrder(root).trim();
    }

    private String preOrder(BinaryNode k) {
        String temp = "";
        if (k != null) {
            temp += k.getValue() + " ";
            temp += preOrder(k.left());
            temp += preOrder(k.right());
        }
        return temp;
    }

    public String inOrder() {
        return inOrder(root).trim();
    }

    private String inOrder(BinaryNode k) {
        String temp = "";
        if (k != null) {
            if (k.left() != null)
                temp += inOrder(k.left());
            temp += k.getValue() + " ";
            if (k.right() != null)
                temp += inOrder(k.right());
        }
        return temp;
    }

    public String postOrder() {
        return postOrder(root).trim();
    }

    private String postOrder(BinaryNode k) {
        String temp = "";
        if (k != null) {
            if (k.left() != null)
                temp += postOrder(k.left());
            if (k.right() != null)
                temp += postOrder(k.right());
            temp += k.getValue() + " ";
        }
        return temp;
    }

    public String levelOrder() {
        String temp = "";
        Queue<BinaryNode> queue = new LinkedList<BinaryNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            BinaryNode k = queue.poll();
            temp += k.getValue() + " ";
            if (k.left() != null)
                queue.offer(k.left());
            if (k.right() != null)
                queue.offer(k.right());
        }
        return temp.trim();
    }


    public int getWidth() {
        return -1;
    }

    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(BinaryNode k) {
        if (k == null) return -1;
        return 1 + Math.max(getHeight(k.left()), getHeight(k.right()));
    }

    public BinaryNode remove(Comparable target) {
        if (root == null) return null;
        BinaryNode temp = root;
        BinaryNode inorderSuccessor;
        if (root.getValue().equals(target)) {
            if (root.left() == null && root.right() == null) {
                root = null;
                return temp;
            } else if (root.left() == null) {
                root = root.right();
                temp.setRight(null);
                return temp;
            } else if (root.right() == null) {
                root = root.left();
                temp.setLeft(null);
                return temp;
            } else {
                inorderSuccessor = successor(root);
                swap(root, inorderSuccessor);
                if (root.right() == inorderSuccessor) {
                    root.setRight(inorderSuccessor.right());
                    inorderSuccessor.setRight(null);
                    return inorderSuccessor;
                }
                return remove(root.right(), target);
            }
        }
        return remove(root, target);
    }


    protected BinaryNode search(BinaryNode parent, Comparable target) {
        if (parent == null) return null;
        if (parent.left() != null && parent.left().getValue().equals(target) || parent.right() != null && parent.right().getValue().equals(target)) {
            return parent;
        } else if (target.compareTo(parent.getValue()) < 0) {
            return search(parent.left(), target);
        } else {
            return search(parent.right(), target);
        }

    }

    private BinaryNode remove(BinaryNode startNode, Comparable target) {
        BinaryNode nodeToRemove, inOrderSuccessor;
        BinaryNode parent = search(startNode, target);
        if (parent == null) return null;
        boolean isLeft = parent.left() != null && parent.left().getValue().equals(target);
        nodeToRemove = isLeft ? parent.left() : parent.right();
        if (nodeToRemove.left() == null && nodeToRemove.right() == null) {
            if (isLeft) {
                parent.setLeft(null);
            } else {
                parent.setRight(null);
            }
            return nodeToRemove;
        } else if (nodeToRemove.left() == null) {
            if (isLeft) {
                parent.setLeft(nodeToRemove.right());
            } else {
                parent.setRight(nodeToRemove.right());
            }
            nodeToRemove.setRight(null);
            return nodeToRemove;
        } else if (nodeToRemove.right() == null) {
            if (isLeft) {
                parent.setLeft(nodeToRemove.left());
            } else {
                parent.setRight(nodeToRemove.left());
            }
            nodeToRemove.setLeft(null);
            return nodeToRemove;
        } else {
            inOrderSuccessor = successor(nodeToRemove);
            swap(inOrderSuccessor, nodeToRemove);
            if (nodeToRemove.right() == inOrderSuccessor) {
                nodeToRemove.setRight(null);
                return inOrderSuccessor;
            }
            return remove(nodeToRemove.right(), target);
        }
    }

    private BinaryNode successor(BinaryNode k) {
        BinaryNode temp = k;
        temp = temp.right();
        while (temp.left() != null)
            temp = temp.left();
        return temp;
    }

    private void swap(BinaryNode x, BinaryNode y) {
        Comparable k = x.getValue();
        x.setValue(y.getValue());
        y.setValue(k);
    }

    public int countLeaves() {
        return countLeaves(root);
    }

    private int countLeaves(BinaryNode k) {
        if (root == null) return 0;
        int count = 0;
        if (k != null) {
            count += countLeaves(k.left());
            count += countLeaves(k.right());
        } else {
            count++;
        }
        return count;
    }

    public int numNodes() {
        return count;
    }

    public int diameter() {
        return diameter(root);
    }

    private int diameter(BinaryNode k) {
        return -1;

    }

    public boolean isFull() {
        if (root == null) return false;

        return isFull(root);
    }

    private boolean isFull(BinaryNode k) {
        if (k != null) {
            if (k.left() != null && k.right() != null) {
                isFull(k.left());
                isFull(k.right());
                return true;
            } else if (k.left() == null && k.right() == null) {
                return true;
            }
        }
        return false;
    }

    public Comparable getLargest() {
        return getLargest(root);
    }

    private Comparable getLargest(BinaryNode k) {
        if (root == null) return -1;
        if (k.right() != null) {
            return getLargest(k.right());
        }

        return k.getValue();
    }

    public Comparable getSmallest() {
        return getSmallest(root);
    }

    private Comparable getSmallest(BinaryNode k) {
        if (root == null) return -1;
        if (k.left() != null) {
            return getSmallest(k.left());
        }
        return k.getValue();
    }
}