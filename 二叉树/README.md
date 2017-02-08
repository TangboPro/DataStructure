# DataStructure-数据结构
数据结构实现
##1、BST java实现

###数据结构
```java
class BiTreeNode{
    public int key;
    public BiTreeNode lchild;//左子树
    public BiTreeNode rchild;//右子树
    public BiTreeNode parent;//父结点
}
```
###查找过程

在二叉搜索树b中查找x的过程为：
>若b是空树，则搜索失败，否则：

>若x等于b的根节点的数据域之值，则查找成功；否则：

>若x小于b的根节点的数据域之值，则搜索左子树；否则：

>查找右子树。

查找结果成功返回该结点，失败返回失败结点的父节点，主要用于其他操作做准备

###添加过程

>查找若成功则代表存在，不允许插入

>若s->data等于b的根节点的数据域之值，则返回，否则：

>若s->data小于b的根节点的数据域之值，则把s所指节点插入到左子树中，否则：

>把s所指节点插入到右子树中。（新插入节点总是叶子节点）

###删除过程
删除分为4种情况、叶子结点不存在、只有左叶子结点、只有右叶子结点、左右叶子结点都存在

>查找到待删除的结点

>若*p结点为叶子结点，即PL（左子树）和PR（右子树）均为空树。由于删去叶子结点不破坏整棵树的结构，则只需修改其双亲结点的指针即可。

>若*p结点只有左子树PL或右子树PR，此时只要令PL或PR直接成为其双亲结点*f的左子树（当*p是左子树）或右子树（当*p是右子树）即可，作此修改也不破坏二叉查找树的特性。

>若*p结点的左子树和右子树均不空。在删去*p之后，为保持其它元素之间的相对位置不变，可按中序遍历保持有序进行调整，可以有两种做法：其一是令*p的左子树为*f的左/右（依*p是*f的左子树还是右子树而定）子树，*s为*p左子树的最右下的结点，而*p的右子树为*s的右子树；其二是令*p的直接前驱（in-order predecessor）或直接后继（in-order successor）替代*p，然后再从二叉查找树中删去它的直接前驱（或直接后继）

##AVL
###AVL树
AVL树是最先发明的自平衡二叉查找树。在AVL树中任何节点的两个子树的高度最大差别为一。
AVL树主要用于解决BST树在极端情况下会退化成链表，从而操作的时间复杂度变成**O(n)**
###平衡因子
在AVL树节点的平衡因子是左子树高度减去右子树高度，平衡因子在-1到1之间代表是平衡的，若超过则被认为是不平衡的
###查找
查找算法和BST的查找算法一致
###添加
在添加一个结点之后，有可能会导致树中的结点失衡，但只需要执行一次旋转操作即可让全树再度平衡。

旋转操作分为单旋和双旋具体原理请看一下链接
> https://zh.wikipedia.org/wiki/AVL%E6%A0%91

代码
``` java
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
```
###删除（O(logn)）

在删除结点之后可能会引起父结点的失衡，调整之后父节点的父节点仍然有可能失衡，所以最坏需要O(logn)次调整，这是一个非常大的开销

代码
```
    public boolean DeleteAvl(BiTreeNode T,int e){

        BiTreeNode _hot=DeleteBST(T,e);
        for(BiTreeNode g=_hot;g!=null;g=g.parent) {//遍历父结点
            if(isAvlBalanced(g)){
                BiTreeNode rotateHead=rotateAt(tallerChild(tallerChild(g)));//获得转换后的头结点
                if(rotateHead.parent==null)//根结点被调整
                    head=rotateHead;
            }
            updateInsertNode(g);//更新删除节点的父节点以及更上层节点
        }
        return true;
    }
```

###3+4重构
单旋和双旋太麻烦，我们可以利用3+4重构一次完成
**思想**：将失衡结点的祖孙三代按中序遍历排序重新命名为a,b,c其下必然有4个节点（或许有空节点）重新命名为t0，t1,t2,t3，之后进行连接，a连t0,t1.c连接t2,t3.b连接a,b，最终结果和单旋或双旋一致.虽然从时间上来看是相同的都是常量级，但3+4重构代码结构更加清晰，逻辑非常清楚，更加容易理解。

>https://jinzihao.me/2015/07/avl%E6%A0%91%E7%9A%8434%E9%87%8D%E6%9E%84/

``` java
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
```

---------------------------------------------------------
##伸展树

##红黑树
