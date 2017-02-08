/**
 * 红黑树规则：
 * 1、节点是红色或黑色。
 * 2、根是黑色。
 * 3、所有叶子都是黑色（叶子是NIL节点）。
 * 4、每个红色节点必须有两个黑色的子节点。（从每个叶子到根的所有路径上不能有两个连续的红色节点。）
 * 5、从任一节点到其每个叶子的所有简单路径都包含相同数目的黑色节点。
 * 最好最坏时间复杂度都是o（logn）

 * b树
 每个超级节点关键码┌m/2┐ - 1 <= j <= m - 1;
 除根结点和叶子结点外，其它每个结点至少有[ceil(m / 2)]个孩子（其中ceil(x)是一个取上限的函数）每个结点至多有m颗子树
 所有的叶子结点都位于同一层
 * Created by Tang on 2017/2/6.
 */



public class RBTree<V>{

    /**在B树中，黑色是超级节点，红色依附在黑色节点周围，一个黑色节点代表一个超级节点*/
    private RBTreeNode<V> head;//头节点

    /**
     * 获取value
     * @param key
     * @return
     */
    public V get(int key){
        return rBSearch(head,key,head).value;
    }

    /**
     * 添加
     * @param key
     * @param value
     */
    public void put(int key,V value){
        RBTreeNode<V> node=rBInsert(head,key,value);/**获取插入结点*/
        if(node!=null)
        insertFixUp(node);
    }

    /**
     * 红黑树添加调整(双红结点)
     * @param node
     */
    private void insertFixUp(RBTreeNode<V> node) {
        RBTreeNode<V> parent,gparent;
        while((parent = node.parent)!=null&&parent.color==RBTreeNode.RED) {/**如果插入节点的父节点是红色的就出现双红问题*/
            gparent=parent.parent;//祖父结点

            if(gparent.lchild==parent)//左孩子
            {
                RBTreeNode<V> uncle = gparent.rchild;//叔父结点
                if (uncle != null && uncle.color == RBTreeNode.RED) {/**第二种情况：叔父结点为红色 相等于b树的分裂*/
                    /**
                     *    ？    调整过程     红
                     *   红 红             黑   黑
                     *  红               红
                     */
                    gparent.color = RBTreeNode.RED;
                    uncle.color = RBTreeNode.BLACK;
                    parent.color = RBTreeNode.BLACK;
                    node = gparent;//还需判断是否还有双红情况
                    continue;//调整完成
                }
                if (node == parent.rchild) {/**第三种情况：叔父结点为黑色且当前结点是右孩子*/
                    leftRotate(parent);//双旋 先左后右

                    //旋转之后node结点变成parent的父结点了，需要调换
                    RBTreeNode<V> temp = node;
                    node = parent;
                    parent = temp;
                }
                parent.color = RBTreeNode.BLACK;
                gparent.color = RBTreeNode.RED;
                rightRotate(gparent);
                /**红黑红*/

            }
            else {//右孩子
                RBTreeNode<V> uncle = gparent.lchild;//叔父结点
                if (uncle != null && uncle.color == RBTreeNode.RED) {/**第二种情况：叔父结点为红色 相等于b树的分裂*/

                    gparent.color = RBTreeNode.RED;
                    uncle.color = RBTreeNode.BLACK;
                    parent.color = RBTreeNode.BLACK;
                    node = gparent;//还需判断是否还有双红情况
                    continue;//调整完成
                }
                if (node == parent.lchild) {/**第三种情况：叔父结点为黑色且当前结点是左孩子*/
                    rightRotate(parent);//双旋 先左后右
                    //旋转之后node结点变成parent的父结点了，需要调换
                    RBTreeNode<V> temp = node;
                    node = parent;
                    parent = temp;
                }
                parent.color = RBTreeNode.BLACK;
                gparent.color = RBTreeNode.RED;
                leftRotate(gparent);
                /**红黑红*/
            }
        }
        head.color=RBTreeNode.BLACK;//根结点为红。第一种情况
    }

    /**
     * 移除
     * @param key
     */
    public void remove(int key){
        RBTreeNode<V> node=rBRemove(head,key);//获取移除结点的父结点
    }

    public void display(){
        ShowRB(head);
    }

    /**
     * 查找
     * @param node
     * @param e
     * @param p
     * @return
     */
    private RBTreeNode<V> rBSearch(RBTreeNode<V> node, int e, RBTreeNode<V> p) {
        //为空则查找失败
        if (node == null) {
            return p;
        }
        if (node.key > e)//比他大就在左孩子查找
            return rBSearch(node.lchild, e, node);
        else if (node.key < e) //比他小在右孩子查找
            return rBSearch(node.rchild, e, node);
        else {
            return node;
        }
    }

    /**
     * 插入
     * @param node 根结点
     * @param e 插入key
     */
    private RBTreeNode<V> rBInsert(RBTreeNode<V> tree, int e,V value) {

        RBTreeNode<V> node = new RBTreeNode<V>();
        node.key = e;
        node.value=value;
        node.color=RBTreeNode.RED;//初始化为红结点
        if (head == null) {//根节点为空
            head = node;//node就为根节点
            head.parent = null;
            head.color=RBTreeNode.BLACK;//情况1：根结点为空，则将其变为空结点
            return head;
        }

        RBTreeNode<V> f = rBSearch(tree, e, tree);//查找
        if (f.key == e) {
            System.out.print("该key已经存在：" + e);
        } else {
            node.parent = f;//指向父节点
            if (f.key > e) {
                f.lchild = node;//在左孩子插入
            } else {
                f.rchild = node;//在右孩子插入
            }
        }
        return node;
    }

    /**
     * 删除
     * @param node 头节点
     * @param e 删除key
     * 返回删除节点的父节点
     */
    private RBTreeNode<V> rBRemove(RBTreeNode<V> node, int e) {

        RBTreeNode<V> p = rBSearch(node, e, node);

        if(p.parent==null) {head=null;return null;}//删除根结点
        RBTreeNode<V> parent=null;
        if (p.key == e) {
            parent=p.parent;//获取父节点
            RBTreeNode<V> q, s;
            if (p.rchild == null && p.lchild == null)//叶子结点
            {
                if (p.parent.lchild!=null&&p.parent.lchild.key == e) {
                    p.parent.lchild = null;
                }
                else if (p.parent.rchild!=null&&p.parent.rchild.key == e) {
                    p.parent.rchild = null;
                }
            } else if (p.lchild == null) {//左子树为空
                p.key = p.rchild.key;
                p.lchild = p.rchild.lchild;
                p.rchild = p.rchild.rchild;
            } else if (p.rchild == null) {//右子树为空
                p.key = p.lchild.key;
                p.rchild = p.lchild.rchild;
                p.lchild = p.lchild.lchild;
            } else {//左右都在

                q = p;
                s = p.lchild;
                while (s.rchild != null) {
                    q = s;
                    s = s.rchild;
                }    //转左，然后向右到尽头
                p.key = s.key;    //s指向被删结点的“前驱”
                if (q != p)
                    q.rchild = s.lchild;    //重接q的右子树
                else
                    q.lchild = s.lchild;    //重接q的左子树

            }
        }
        else {
            System.out.print("该key不存在："+e+"\n");
        }
        return parent;//返回父节点
    }

    /**
     * 显示
     * @param node
     */
    private void ShowRB(RBTreeNode<V> node){
        if(node==null) return;
        if(node.lchild!=null)
            ShowRB(node.lchild);
        System.out.print(node.key+" ");
        if(node.rchild!=null)
            ShowRB(node.rchild);
    }


    /**
     * 对红黑树的节点(y)进行右旋转
     *
     * 右旋示意图(对节点y进行左旋)：
     *            py                               py
     *           /                                /
     *          y                                x
     *         /  \      --(右旋)-.            /  \                     #
     *        x   ry                           lx   y
     *       / \                                   / \                   #
     *      lx  rx                                rx  ry
     *
     */
    private void rightRotate(RBTreeNode node){
        RBTreeNode<V> y=node;
        RBTreeNode<V> x=node.lchild;

        y.lchild=x.rchild;
        if(x.rchild!=null) {
            x.rchild.parent=y;
        }
        x.parent=y.parent;
        if(y.parent==null) {
            head=x;
        }
        else {//进行连接
            if(y.parent.lchild==y)
                y.parent.lchild=x;
            else y.parent.rchild=x;
        }
        x.rchild=y;
        y.parent=x;
    }

    /**
     * 对红黑树的节点(x)进行左旋转
     *
     *   左旋示意图(对节点x进行左旋)：
     *      px                              px
     *     /                               /
     *    x                               y
     *   /  \      --(左旋)-.           / \                #
     *  lx   y                          x  ry
     *     /   \                       /  \
     *    ly   ry                     lx  ly
     *
     *
     */
    private void leftRotate(RBTreeNode<V> node){
        RBTreeNode<V> x=node;
        RBTreeNode<V> y=node.rchild;
        x.rchild=y.lchild;
        if(x.rchild!=null) {
            x.rchild.parent=y;
        }
        y.parent=x.parent;
        if(y.parent==null) {
            head=y;
        }
        else {
            if(x.parent.lchild==x)
                x.parent.lchild=y;
            else x.parent.rchild=y;
        }
        y.lchild=x;
        x.parent=y;
    }
 }

class RBTreeNode<V> {
    public static final boolean RED   = true;
    public static final boolean BLACK = false;
    public int key;
    public V value;
    public boolean color;//true为红
    public RBTreeNode lchild;//左子树
    public RBTreeNode rchild;//右子树
    public RBTreeNode parent;
    public int height;

}
