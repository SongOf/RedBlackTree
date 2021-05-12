package com.songof;

import java.util.Scanner;
//        注 红黑树 测试 2,4,6,8,10,12,14...就不平衡了...
//        这里说下原因：因为这是课程源码，考虑的并没那么多 我比对节点大小时 直接使用的是    node.key.compareTo(parent.key);
//        明眼的同学都能看出来，这个其实是按照字符串比对的！ 所以，大家尽量使用 a,b,c,d,e,f,g,h,i...这种风格去测试...
//        或者自己改改这块的逻辑，可以去参考HashMap的实现去改
public class TestRBTree {
    public static void main(String[] args) {
        RBTree<String, Object> rbt = new RBTree();
        //测试输入：ijkgefhdabc
        while(true) {
            Scanner sc = new Scanner(System.in);
            System.out.println("请输入key:");
            String key = sc.next();

            rbt.insert(key, null);
            TreeOperation.show(rbt.getRoot());
        }
    }
}
