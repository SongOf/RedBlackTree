package com.songof;

/**
 * @author SongOf
 * @ClassName RBTree
 * @Description 实现红黑树
 *  * ①创建RBTree，定义颜色
 *  * ②创建RBNode
 *  * ③辅助方法定义：parentOf(node)，isRed(node)，setRed(node)，setBlack(node)，inOrderPrint(RBNode tree)
 *  * ④左旋方法定义：leftRotate(node)
 *  * ⑤右旋方法定义：rightRotate(node)
 *  * ⑥公开插入接口方法定义：insert(K key, V value);
 *  * ⑦内部插入接口方法定义：insert(RBNode node);
 *  * ⑧修正插入导致红黑树失衡的方法定义：insertFIxUp(RBNode node);
 *  * ⑨测试红黑树正确性
 * @Date 2021/5/11 22:58
 * @Version 1.0
 */
public class RBTree <K extends Comparable<K>, V> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private RBNode root;

    public RBNode getRoot() {
        return root;
    }

    public void setRoot(RBNode root) {
        this.root = root;
    }

    /***
     * @Description 获取父节点
     * @Param node
     * @return RBNode
     */
    private RBNode parentOf(RBNode node) {
        if(node != null) return node.parent;
        return null;
    }

    /***
     * @Description 是否为红色
     * @Param node
     * @return boolean
     */
    private boolean isRed(RBNode node) {
        if(node != null) return node.color == RED;
        return false;
    }

    /***
     * @Description 是否为黑色
     * @Param node
     * @return boolean
     */
    private boolean isBlack(RBNode node) {
        if(node != null) return node.color == BLACK;
        return false;
    }

    /**
     * 设置节点为红色
     * @param node
     */
    private void setRed(RBNode node) {
        if(node != null) node.color = RED;
    }

    /**
     * 设置节点为红色
     * @param node
     */
    private void setBlack(RBNode node) {
        if(node != null) node.color = BLACK;
    }

    /**
     * 左旋方法
     *      * 左旋示意图：左旋x节点
     *      *    p                   p
     *      *    |                   |
     *      *    x                   y
     *      *   / \         ---->   / \
     *      *  lx  y               x   ry
     *      *     / \             / \
     *      *    ly  ry          lx  ly
     *      *
     *      * 左旋做了几件事？
     *      * 1.将y的左子节点赋值给x的右边，并且把x设置为y的左子节点的父节点
     *      * 2.将x的父节点（非空时）指向y，更新y的父节点为x的父节点
     *      * 3.将y的左子节点指向x，更新x的父节点为y
     * @param x
     */
    private void leftRotate(RBNode x) {
        RBNode y = x.right;
        x.right = y.left;
        if(y.left != null) {
            y.left.parent = x;
        }

        if(x.parent != null) {
            y.parent = x.parent;
            if(x.parent.left == x) {
                x.parent.left = y;
            }else {
                x.parent.right = y;
            }
        }else {
            this.root = y;
            this.root.parent = null;
        }
        x.parent = y;
        y.left = x;
    }

    /**
     * 右旋方法
     *      * 右旋示意图：右旋y节点
     *      *
     *      *    p                       p
     *      *    |                       |
     *      *    y                       x
     *      *   / \          ---->      / \
     *      *  x   ry                  lx  y
     *      * / \                         / \
     *      *lx  ly                      ly  ry
     *      *
     *      * 右旋都做了几件事？
     *      * 1.将x的右子节点 赋值 给了 y 的左子节点，并且更新x的右子节点的父节点为 y
     *      * 2.将y的父节点（不为空时）指向x，更新x的父节点为y的父节点
     *      * 3.将x的右子节点指向y，更新y的父节点为x
     * @param y
     */
    private void rightRotate(RBNode y) {
        RBNode x = y.left;
        y.left = x.right;
        if(x.right != null) {
            x.right.parent = y;
        }

        if(y.parent != null) {
            x.parent = y.parent;
            if(y == y.parent.left) {
                y.parent.left = x;
            }else {
                y.parent.right = x;
            }
        }else {
            this.root = x;
            this.root.parent = null;
        }
        x.right = y;
        y.parent = x;
    }

    /***
    * @Description 插入
    * @Param [key, value]
    * @return void
    */
    public void insert(K key, V value) {
        RBNode node = new RBNode();
        node.setKey(key);
        node.setValue(value);
        node.setColor(RED);

        insert(node);
    }

    private void insert(RBNode node) {
        RBNode parent = null;
        RBNode x = this.root;

        while (x != null) {
            parent = x;

            int cmp = node.getKey().compareTo(x.getKey());
            if(cmp > 0) x = x.right;
            else if(cmp == 0) {
                x.setValue(node.getValue());
                return;
            }else x = x.left;
        }
        node.parent = parent;

        if(parent != null) {
            int cmp = node.getKey().compareTo(parent.getKey());
            if(cmp > 0) {
                parent.right = node;
            }else {
                parent.left = node;
            }
        }else {
            this.root = node;
        }

        insertFixUp(node);
    }

    /***
    * @Description 平衡
    * * 插入后修复红黑树平衡的方法
    *      *     |---情景1：红黑树为空树 将根节点置黑
    *      *     |---情景2：插入节点的key已经存在 不需要平衡
    *      *     |---情景3：插入节点的父节点为黑色 不需要处理
    *      *
    *      *     情景4 需要咱们去处理
    *      *     |---情景4：插入节点的父节点为红色
    *      *          |---情景4.1：叔叔节点存在，并且为红色（父-叔 双红） 叔父置黑 爷爷置红 以爷爷为当前节点
    *      *          |---情景4.2：叔叔节点不存在，或者为黑色，父节点为爷爷节点的左子树
    *      *               |---情景4.2.1：插入节点为其父节点的左子节点（LL情况） 父亲置黑 爷爷置红 以爷爷右旋
    *      *               |---情景4.2.2：插入节点为其父节点的右子节点（LR情况） 以父节点左旋得到4.2.1 以父节点为当前节点
    *      *          |---情景4.3：叔叔节点不存在，或者为黑色，父节点为爷爷节点的右子树
    *      *               |---情景4.3.1：插入节点为其父节点的右子节点（RR情况） 父亲置黑 爷爷置红 以爷爷左旋
    *      *               |---情景4.3.2：插入节点为其父节点的左子节点（RL情况） 以父节点右旋得到4.3.1 以父节点为当前节点
    * @Param node
    * @return void
    */
    private void insertFixUp(RBNode node) {
        RBNode parent = parentOf(node);
        RBNode gparent = parentOf(parent);
        //存在父节点且父节点为红色
        if(parent != null && isRed(parent)) {
            //父节点是红色的，那么一定存在爷爷节点

            //父节点为爷爷节点的左子树
            if(parent == gparent.left) {
                RBNode uncle = gparent.right;
                //4.1：叔叔节点存在，并且为红色（父-叔 双红）
                //将父和叔染色为黑色，再将爷爷染红，并将爷爷设置为当前节点，进入下一次循环判断
                if(uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    insertFixUp(gparent);
                    return;
                }

                //叔叔节点不存在，或者为黑色，父节点为爷爷节点的左子树
                if(uncle == null || isBlack(uncle)) {
                    //插入节点为其父节点的右子节点（LR情况）=>
                    //左旋（父节点），当前节点设置为父节点，进入下一次循环
                    if(node == parent.right) {
                        leftRotate(parent);
                        insertFixUp(parent);
                        return;
                    }

                    //插入节点为其父节点的左子节点（LL情况）=>
                    //变色（父节点变黑，爷爷节点变红），右旋爷爷节点
                    if(node == parent.left) {
                        setBlack(parent);
                        setRed(gparent);
                        rightRotate(gparent);
                    }
                }

            } else {//父节点为爷爷节点的右子树
                RBNode uncle = gparent.left;
                //4.1：叔叔节点存在，并且为红色（父-叔 双红）
                //将父和叔染色为黑色，再将爷爷染红，并将爷爷设置为当前节点，进入下一次循环判断
                if(uncle != null && isRed(uncle)) {
                    setBlack(parent);
                    setBlack(uncle);
                    setRed(gparent);
                    insertFixUp(gparent);
                    return;
                }

                //叔叔节点不存在，或者为黑色，父节点为爷爷节点的右子树
                if(uncle == null || isBlack(uncle)) {
                    //插入节点为其父节点的左子节点（RL情况）
                    //右旋（父节点）得到RR情况，当前节点设置为父节点，进入下一次循环
                    if(node == parent.left) {
                        rightRotate(parent);
                        insertFixUp(parent);
                        return;
                    }

                    //插入节点为其父节点的右子节点（RR情况）=>
                    //变色（父节点变黑，爷爷节点变红），右旋爷爷节点
                    if(node == parent.right) {
                        setBlack(parent);
                        setRed(gparent);
                        leftRotate(gparent);
                    }
                }

            }
        }

        setBlack(this.root);
    }

    /*****************************************************************************
     * Print Method
     *****************************************************************************/

    public void padding ( String ch, int n ) {
        int i;
        for ( i = 0; i < n; i++ )
            System.out.printf(ch);

    }

    void print_node (RBNode root, int level ) {
        if ( root == null ) {
            padding ( "\t", level );
            System.out.println( "NIL" );

        } else {
            print_node ( root.right, level + 1 );
            padding ( "\t", level );
            if(root.color == BLACK) {
                System.out.printf(root.key + "(" + (root.isColor() ? "红" : "黑") +")" + "\n");
            } else
                System.out.printf(root.key  + "(" + (root.isColor() ? "红" : "黑") +")" + "\n");
            print_node ( root.left, level + 1 );
        }
    }

    void print_tree() {
        print_node(this.root,0);
        System.out.printf("-------------------------------------------\n");
    }

    /**
     * 中序遍历
     */
    public void inOrderPrint() {
        inOrderPrint(this.root);
    }

    /**
     * 中序遍历打印
     * @param root
     */
    private void inOrderPrint(RBNode root) {
        if(root != null) {
            inOrderPrint(root.left);
            System.out.println("key:" + root.key + ",value:" + root.value);
            inOrderPrint(root.right);
        }
    }

    static class RBNode <K extends Comparable<K>, V> {
        private RBNode parent;
        private RBNode left;
        private RBNode right;
        private boolean color;
        private K key;
        private V value;

        public RBNode() {}

        public RBNode(RBNode parent, RBNode left, RBNode right, boolean color, K key, V value) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.color = color;
            this.key = key;
            this.value = value;
        }

        public void setParent(RBNode parent) {
            this.parent = parent;
        }

        public void setLeft(RBNode left) {
            this.left = left;
        }

        public void setRight(RBNode right) {
            this.right = right;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public RBNode getParent() {
            return parent;
        }

        public RBNode getLeft() {
            return left;
        }

        public RBNode getRight() {
            return right;
        }

        public boolean isColor() {
            return color;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
