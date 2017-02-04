/**
 * Created by Tang on 2017/2/4.
 * 伸展树优点：（2-8定理：只有20%的数据是经常使用的）
 * 可靠的性能——它的平均效率不输于其他平衡树[2]。
 * 存储所需的内存少——伸展树无需记录额外的什么值来维护树的信息，相对于其他平衡树，内存占用要小。
 * 支持可持久化——可以将其改造成可持久化伸展树。可持久化数据结构允许查询修改之前数据结构的信息，对于一般的数据结构，每次操作都有可能移除一些信息，而可持久化的数据结构允许在任何时间查询到之前某个版本的信息。可持久化这一特性在函数式编程当中非常有用。另外，可持久化伸展树每次一般操作的均摊复杂度是O(log n
 * 缺点：伸展树最显著的缺点是它有可能会变成一条链。这种情况可能发生在以非降顺序访问n个元素之后
 * 在这里我们使用优化的双层伸展。逐层伸展是一次伸展一层，这种方法容易使这颗二叉树变成一条链
 */

public class SplayTree extends BiTree {


	/**
	*查找算法
	*/
    public BiTreeNode SearchSplay(BiTreeNode T, int e) {
        BiTreeNode node=SearchBST(T,e,T);//返回值为查找到的结点 或者 父节点
        head=splay(node);//伸展
        return head;//返回根节点
    }

	/**
	*调整算法
	*/
    private BiTreeNode splay(BiTreeNode v) {
	    
        if(v==null) return null;
        BiTreeNode p;
        BiTreeNode g;
        while((p=v.parent)!=null&&(g=p.parent)!=null){

            BiTreeNode gg=g.parent;//保存g的父结点，用于迭代
            /**旋转操作 采用重构实现，具体实现配合图形理解*/
            if(islChild(v))
                if(islChild(p)){/** zig-zig*/
                    attachAsLChild(g,p.rchild);
                    attachAsLChild(p,v.rchild);
                    attachAsRChild(p,g);
                    attachAsRChild(v,p);
                }
                else{/** zig-zag*/
                    attachAsRChild(g,v.lchild);
                    attachAsLChild(p,v.rchild);
                    attachAsLChild(v,g);
                    attachAsRChild(v,p);
                }
            else if(isrChild(p)){/** zag-zag*/
                    attachAsRChild(g,p.lchild);
                    attachAsRChild(p,v.lchild);
                    attachAsLChild(p,g);
                    attachAsLChild(v,p);
                }
                else{/** zag-zig*/
                    attachAsLChild(g,v.rchild);
                    attachAsRChild(p,v.lchild);
                    attachAsRChild(v,g);
                    attachAsLChild(v,p);
                }

            if(gg==null) v.parent=null;//如果gg为空代表 v是根结点
            else if(g == gg.lchild) attachAsLChild(gg, v);//如果不为空，将保存的gg和v连接起来
                else attachAsRChild(gg, v);
        }
        if((p=v.parent)!=null){/**双旋有可能会只有2个结点，但只需要进行一次单旋即可完成*/
            if(isrChild(v)){/**zag*/
                p.rchild=v.lchild;
                if(v.lchild!=null)//不能为空
                    v.lchild.parent=p;
                v.lchild=p;
                p.parent=v;
            }
            else{/**zig*/
                p.lchild=v.rchild;
                if(v.rchild!=null)//孩子有可能为空
                    v.rchild.parent=p;
                v.rchild=p;
                p.parent=v;
            }
        }//有可能会出现最后一次只有2个结点的情况
        v.parent=null;
        return head=v;//返回根结点
    }

    //旋转重构连接,左连接
    private void attachAsLChild(BiTreeNode node,BiTreeNode lc){
        node.lchild=lc;
        if(lc!=null)//不能为空
            lc.parent=node;
    }

    //旋转重构连接，右连接
    private void attachAsRChild(BiTreeNode node,BiTreeNode rc){
        node.rchild=rc;
        if(rc!=null)//子结点有可能为空
            rc.parent=node;
    }
    private boolean islChild(BiTreeNode node){
        if(node.parent==null) return false;//根结点情况
        return node.parent.lchild==node;
    }

    /**判断是右孩子*/
    private boolean isrChild(BiTreeNode node){
        if(node.parent==null) return false;//根节点情况
        return node.parent.rchild==node;
    }
}
