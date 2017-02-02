/**
 * BST  JAVA实现
 * 查找，添加，删除，显示
 */
public class Main {
    public static void main(String[] args) {
        //测试
        BiTree tree=new BiTree();
        tree.InsertBst(tree.heard, 8);
        tree.InsertBst(tree.heard, 3);
        tree.InsertBst(tree.heard, 1);
        tree.InsertBst(tree.heard, 6);
        tree.InsertBst(tree.heard, 4);
        tree.InsertBst(tree.heard, 7);
        tree.InsertBst(tree.heard, 10);
        tree.InsertBst(tree.heard, 14);
        tree.InsertBst(tree.heard, 13);
        tree.ShowBST(tree.heard);
        tree.DeleteBST(tree.heard,13);
        System.out.println();
        tree.ShowBST(tree.heard);
    }
}

/**
 * 结点类
 */
class BiTreeNode{
    public int key;
    public BiTreeNode lchild;//左子树
    public BiTreeNode rchild;//右子树
    public BiTreeNode parent;
}

/**
 * BST类
 */
class BiTree {
    public BiTreeNode heard;//头结点
    /**
     * BST查找
     *
     * @param T 当前结点
     * @param e 查找的关键字
     * @param p p为T的父结点
     * @return 返回查找结点, 查找失败返回失败结点的父节点
     */
    public BiTreeNode SearchBST(BiTreeNode T, int e, BiTreeNode p) {
        //为空则查找失败
        if (T == null) {
            return p;
        }
        if (T.key > e)//比他大就在左孩子查找
            return SearchBST(T.lchild, e, T);
        else if (T.key < e) //比他小在右孩子查找
            return SearchBST(T.rchild, e, T);
        else {
            return T;
        }
    }

    /**
     * 插入
     *
     * @param T 根结点
     * @param e 插入key
     */
    public void InsertBst(BiTreeNode T, int e) {
        BiTreeNode node = new BiTreeNode();
        node.key = e;
        if (heard == null) {//根节点为空
            heard = node;//node就为根节点
            heard.parent = null;
            return;
        }
        BiTreeNode f = SearchBST(T, e, T);//查找
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
        return;
    }

    /**
     * 删除
     * @param T 头节点
     * @param e 删除key
     */
    public void DeleteBST(BiTreeNode T, int e) {

        BiTreeNode p = SearchBST(T, e, T);
        if (p.key == e) {
            BiTreeNode q, s;
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
            System.out.print("该key不存在："+e);
        }
    }

    /**
     * 显示
     * @param T
     */
    public void ShowBST(BiTreeNode T){
        if(T.lchild!=null)
            ShowBST(T.lchild);
        System.out.print(T.key+" ");
        if(T.rchild!=null)
            ShowBST(T.rchild);
    }

}



