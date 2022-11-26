import java.util.Scanner;

public class Computer {
    public static void main(String[] args) {
        Memory memory = new Memory();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要分配内存还是要释放内存");
            System.out.println("1.分配内存 2.释放内存 3.展示分区状况");
            int n = sc.nextInt();
            switch (n) {
                case 1: {
                    System.out.println("请输入要分配的内存大小（规定2——10个单元----2kb到20kb，整数）");
                    int size = sc.nextInt();
                    memory.allocation(size);
                    break;
                }
                case 2: {
                    System.out.println("想要释放内存的分区号");
                    int id = sc.nextInt();
                    memory.collection(id);
                    break;
                }
                case 3: {
                    memory.showFragments();
                    break;
                }
                default:
                    System.out.println("不是要求输入的选项");
            }
        }
    }
}
