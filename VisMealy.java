import java.util.Scanner;
public class VisMealy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int statesc = sc.nextInt();
        int absize = sc.nextInt();
        int ststate = sc.nextInt();
        int trmat[][] = new int[statesc][absize];
        String inmat[][] = new String[statesc][absize];
        for (int i = 0; i < statesc; i++) {
            for (int j = 0; j < absize; j++) {
                trmat[i][j] = sc.nextInt();
            }
        }
        for (int i = 0; i < statesc; i++) {
            for (int j = 0; j < absize; j++) {
                inmat[i][j] = sc.next();
            }
        }
        System.out.println("digraph {");
        System.out.println("\trankdir = LR");
        for (int i = 0; i < statesc; i++) {
            for (int j = 0; j < absize; j++) {
                System.out.printf("\t%d -> %d [label = \"%c(%s)\"]\n", i, trmat[i][j],
                        'a' + j, inmat[i][j]);
            }
        }
        System.out.println("}");
    }
}