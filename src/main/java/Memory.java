import java.util.LinkedList;
import java.util.Scanner;

public class Memory {
    private int pointer;
    private final LinkedList<Fragment> fragments;// 内存分区链表
    public Memory() {
        this.pointer = 0;
        this.fragments = new LinkedList<>(); //链表
        int total_units = 128;
        fragments.add(new Fragment(0, total_units));
    }
    // 内存分配菜单
    public void allocation(int size) {
        System.out.println("1.首次适配 2.再次适配 3.最佳适配 4.最坏适配");
        System.out.print("请选择分配算法:");
        Scanner in = new Scanner(System.in);
        int choice = in.nextInt();
        switch (choice) {
            case 1:
                FF(size);
                break;
            case 2:
                NF(size);
                break;
            case 3:
                BF(size);
                break;
            case 4:
                WF(size);
                break;
            default:
                System.out.println("请重新选择!");
        }
    }

    private void FF(int size) {
        for (pointer = 0; pointer < fragments.size(); pointer++) {
            Fragment fragment = fragments.get(pointer);
            if (fragment.isFree && (fragment.size_units*2 >= size)) {
                //size:kb数，pointer:指针所在单元，fragment:指针对应的允许分配空闲区
                doAllocation(size, pointer, fragment);
                return;
            }
        }
        System.out.println("++++++无可用内存空间!++++++");
    }

    private void NF(int size) {
        Fragment fragment = fragments.get(pointer);
        if (fragment.isFree && (fragment.size_units*2 >= size)) {
            doAllocation(size, pointer, fragment);
            return;
        }
        int len = fragments.size();
        int i = (pointer + 1) % len;
        for (; i != pointer; i = (i + 1) % len) {
            fragment = fragments.get(i);
            if (fragment.isFree && (fragment.size_units*2 >= size)) {
                doAllocation(size, i, fragment);
                return;
            }
        }
        System.out.println("++++++无可用内存空间!++++++");
    }
    // 最佳适应算法
    private void BF(int size) {
        int flag = -1;
        int min = 10000;
        for (pointer = 0; pointer < fragments.size(); pointer++) {
            Fragment fragment = fragments.get(pointer);
            if (fragment.isFree && (fragment.size_units*2 >= size)) {
                if (min > fragment.size_units*2 - size) {
                    min = fragment.size_units*2 - size;
                    flag = pointer;
                }
            }
        }
        if (flag == -1) {
            System.out.println("++++++无可用内存空间!++++++");
        } else {
            doAllocation(size, flag, fragments.get(flag));
        }
    }
    // 最坏适应算法
    private void WF(int size) {
        int flag = -1;
        int max = 0;
        for (pointer = 0; pointer < fragments.size(); pointer++) {
            Fragment fragment = fragments.get(pointer);
            if (fragment.isFree && (fragment.size_units*2 >= size)) {
                if (max < fragment.size_units*2 - size) {
                    max = fragment.size_units*2 - size;
                    flag = pointer;
                }
            }
        }
        if (flag == -1) {
            System.out.println("++++++无可用内存空间!++++++");
        } else {
            doAllocation(size, flag, fragments.get(flag));
        }
    }
    // 开始分配
    private void doAllocation(int size, int location, Fragment fragment) {
        int jc_uints;
        if(size%2==1){
            jc_uints=size/2+1;
        }
        else {
            jc_uints = size/2;
        }
        int next_head_uint = fragment.head_unit + jc_uints;
        if(fragment.size_units != jc_uints){
            Fragment split = new Fragment(next_head_uint, fragment.size_units - jc_uints);
            fragments.add(location + 1, split);
            fragment.size_units = jc_uints;
        }
        fragment.actually_kb = size;
        fragment.isFree = false;
        System.out.println("成功分配了" + size + "KB 的内存!");
    }

    // 内存回收
    public void collection(int id) {
        if (id >= fragments.size()) {
            System.out.println("++++++无此分区编号!++++++");
            return;
        }
        Fragment fragment = fragments.get(id);
        if (fragment.isFree) {
            System.out.println("++++++指定分区未分配, 无需回收++++++");
            return;
        }
        int size_collection = fragment.size_units*2;
        fragment.actually_kb = 0;
        // 如果回收的分区后一个是空闲就和后一个合并
        if (id < fragments.size() - 1 && fragments.get(id + 1).isFree) {
            Fragment next_fragment = fragments.get(id + 1);
            fragment.size_units += next_fragment.size_units;
            fragments.remove(next_fragment);
        }
        // 回收的分区要是前一个是空闲就和前分区合并
        if (id > 0 && fragments.get(id - 1).isFree) {
            Fragment previous = fragments.get(id - 1);
            previous.size_units += fragment.size_units;
            fragments.remove(id);
            id--;
        }
        fragments.get(id).isFree = true;
        System.out.println("内存回收成功!, 本次回收了 " + size_collection + "KB 空间!");
    }

    public void showFragments() {
        System.out.println("分区编号\t分区始址kb\t分区大小kb\t进程实际大小kb\t是否空闲");
        for (int i = 0; i < fragments.size(); i++) {
            Fragment tmp = fragments.get(i);
            System.out.println(i + "\t\t" + tmp.head_unit*2 + "\t\t\t" + tmp.size_units*2 +
                    "\t\t\t" + tmp.actually_kb+"\t\t\t\t"+tmp.isFree);
        }
        int pieces = 0;
        for (Fragment fragment:fragments) {
            if(fragment.isFree && fragment.size_units<2){
                pieces++;
            }
        }
        System.out.println("碎片数（小于2个分配单元（units）的空闲区数）："+pieces);
    }
}
