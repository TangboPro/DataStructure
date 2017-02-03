
/**
 * BBST-JAVA实现
 * 继承父类(BST类)
 * Created by Tang on 2017/2/2.
 */

public class BbstAvl extends BiTree{
    /**
     *
     * @param T 头节点
     * @param e key
     * @return
     */
    public boolean DeleteAvl(BiTreeNode T,int e){

        BiTreeNode _hot=DeleteBST(T,e);
        for(BiTreeNode g=_hot;g!=null;g=g.parent) {//遍历父结点
            if(isAvlBalanced(g)){
                BiTreeNode rotateHead=rotateAt(tallerChild(tallerChild(g)));//获得转换后的头结点
                if(rotateHead.parent==null)//根结点被调整
                    head=rotateHead;
            }
            updateInsertNode(g);
        }
        return true;
    }

    /**插入算法,调整其中一个即可保证全局不失衡*/
    public BiTreeNode InsertAvl(BiTreeNode T,int e){

        BiTreeNode node=InsertBst(T,e);//父类BST插入算法
        //追溯祖先
        for(BiTreeNode g=node.parent;g!=null;g=g.parent)
        {
            if(isAvlBalanced(g)){//判断是否失衡
                //调整g为失衡
                //3+4重构
                BiTreeNode rotateHead=rotateAt(tallerChild(tallerChild(g)));
                if(rotateHead.parent==null)//根结点被调整
                    head=rotateHead;
                break;
            }
            else{
                //更新祖先高度
                updateInsertNode(g);
            }
        }
        return node;
    }

    /**
     * 调整，分情况旋转
     * @param v
     * @return
     */
    private BiTreeNode rotateAt(BiTreeNode v) {

        BiTreeNode p = v.parent;//父结点
        BiTreeNode g = p.parent;//祖父结点(失衡结点)

        if (islChild(p))//zig
            if (islChild(v)) {//zig-zig
                p.parent = g.parent;
                return connect34(v, p, g, v.lchild, v.rchild, p.rchild, g.rchild);
            } else {//zig-zag
                v.parent = g.parent;
                return connect34(p, v, g, p.lchild, v.lchild, v.rchild, g.rchild);
            }
            //
        else if (islChild(v)) {//zag-zig
            v.parent = g.parent;
            return connect34(g, v, p, g.lchild, v.lchild, v.rchild, p.rchild);
            } else {//zag-zag
                p.parent = g.parent;
                return connect34(g, p,v, g.lchild, p.lchild, v.lchild, v.rchild);
            }
    }

    /**
     * 3+4重构
     * @return
     */
    private BiTreeNode connect34(BiTreeNode a, BiTreeNode b, BiTreeNode c, BiTreeNode T0, BiTreeNode T1, BiTreeNode T2, BiTreeNode T3) {

        a.lchild=T0;if(T0!=null) T0.parent=a;
        a.rchild=T1;if(T1!=null) T1.parent=a; updateInsertNode(a);
        c.lchild=T2;if(T2!=null) T2.parent=c;
        c.rchild=T3;if(T3!=null) T3.parent=c; updateInsertNode(c);
        b.lchild=a;a.parent=b;
        b.rchild=c; c.parent=b; updateInsertNode(b);
        return b;
    }

    /**
     * 更新node高度，插入算法
     * @param node
     */
    private void updateInsertNode(BiTreeNode node) {

        if(node.lchild==null&&node.rchild==null)//左右子树为空
            node.height=0;
        else if(node.lchild==null)
            node.height=node.rchild.height+1;
        else if(node.rchild==null)
            node.height=node.lchild.height+1;
        else
            node.height=(node.lchild.height>node.rchild.height?node.lchild.height:node.rchild.height)+1;
    }

    /**
     * 获取子孩子失衡结点 插入操作必然在高度高的
     * @param node
     * @return
     */
    private BiTreeNode tallerChild(BiTreeNode node) {
        //有空孩子
        if(node.lchild==null) return node.rchild;
        if(node.rchild==null) return node.lchild;

        if(node.lchild.height>node.rchild.height)
            return node.lchild;
        else return node.rchild;
    }

    /**判断g是否失衡*/
    private boolean isAvlBalanced(BiTreeNode g) {
        int rHeight;
        int lHeight;
        //右子树高度
        if(g.rchild==null){
            rHeight=0;
        }
        else{
            rHeight=g.rchild.height+1;
        }
        //左子树高度
        if(g.lchild==null){
            lHeight=0;
        }
        else{
            lHeight=g.lchild.height+1;
        }
        //判断是否失衡
        int balance=lHeight-rHeight;//获取高度
        return balance<-1||balance>1;
    }

    /**判断是否是左孩子*/
     private boolean islChild(BiTreeNode node){
        if(node.parent==null) return false;//根结点
        return node.parent.lchild==node;
    }

    /**判断是右孩子*/
    private boolean isrChild(BiTreeNode node){
        if(node.parent==null) return false;//根节点
        return node.parent.rchild==node;
    }

}
