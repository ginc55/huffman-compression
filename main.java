import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class main {
    static Huffman_Node root_node;
    private static void comp(String filename, String archive) throws IOException {
        String text = "";
        text = new String(Files.readAllBytes(Paths.get(filename)));

        if (text == null || text.length() == 0) {
            return;
        }


        Map<Character, Integer> frequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Huffman_Node> prio_queue;
        prio_queue = new PriorityQueue<>(Comparator.comparingInt(l -> l.frequency));


        for (var entry : frequency.entrySet()) {
            prio_queue.add(new Huffman_Node(entry.getKey(), entry.getValue()));
        }

        while (prio_queue.size() != 1) {

            Huffman_Node left = prio_queue.poll();
            Huffman_Node right = prio_queue.poll();

            int sum = left.frequency + right.frequency;
            prio_queue.add(new Huffman_Node(null, sum, left, right));
        }

        root_node = prio_queue.peek();

        Map<Character, String> huffmanCode = new HashMap<>();
        Helper_Classes.encode_huffman(root_node, "", huffmanCode);


        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(huffmanCode.get(c));
        }

        StringBuilder huffcode = new StringBuilder("{");
        for (Character key : huffmanCode.keySet()) {
            huffcode.append(key + "=" + huffmanCode.get(key) + ", ");
        }
        huffcode.delete(huffcode.length()-2, huffcode.length()).append("}");


        File file = new File(archive);
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(file));
        writer.append(sb);
        writer.append("\n");
        writer.append(huffcode);

        if (writer != null) writer.close();

        System.out.println("=> The encoded text is: " + sb);
    }

    private static void decomp(String filename, String archive) throws IOException {
        // lasa arhiveto failu
        StringBuilder sb, codes;
        File file=new File(archive);    //jauna faila instance
        FileReader fr=new FileReader(file);   //nolasa failu
        BufferedReader br=new BufferedReader(fr);  
        sb = new StringBuilder(br.readLine());
        codes = new StringBuilder(br.readLine());


        // atkode string
        String decoded_text = "";
        System.out.print("=> Decoded Text: ");

        if (Helper_Classes.is_Leaf(root_node)) {
            while (root_node.frequency-- > 0) {
                decoded_text += root_node.ch;
                System.out.print(root_node.ch);
            }

            File file2 = new File(filename);
            BufferedWriter writer2 = null;
            writer2 = new BufferedWriter(new FileWriter(file));
            writer2.append(decoded_text);
            if (writer2 != null) writer2.close();

        } else {
            int index = -1;
            while (index < sb.length() - 1) {
                index = Helper_Classes.decode_huffman(root_node, index, sb, filename);
            }
        }




        System.out.println();
    }

    private static void size(String filename) {
        File f = new File(filename);
        if (f.exists()) {
            System.out.println("=> Size of file is " + new File(filename).length() + " bytes.");
        }
        else {
            System.out.println("=> File does not exist.");
        }
    }

    private static void equal(String file1, String file2) {
        try {
            BufferedReader bf1 = Files.newBufferedReader(Path.of(file1));
            BufferedReader bf2 = Files.newBufferedReader(Path.of(file2));
            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    System.out.println("=> Files Comparison returned: False");
                    return;
                }
            }
            if (bf2.readLine() == null) {
                System.out.println("=> Files Comparison returned: True");
            } else {
                System.out.println("=> Files Comparison returned: False");
            }
        }
        catch (IOException e) {
            System.out.println("=> Error During File Reading.");
        }
    }

    public static void main(String []args) throws IOException {
        String query;
        System.out.println("Welcome to Java Console App!");
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print(">> ");
            query = sc.nextLine();
            if (query.equalsIgnoreCase("equal")) {
                String file1, file2;
                System.out.println("=> Enter name with path of first file:");
                file1 = sc.nextLine();
                System.out.println("=> Enter name with path of second file:");
                file2 = sc.nextLine();;;
                equal(file1, file2);
            }
            else if (query.equalsIgnoreCase("size")) {
                String filename;
                System.out.print("=> Enter name with path of file: ");
                filename = sc.nextLine();
                size(filename);
            }
            else if (query.equalsIgnoreCase("comp")) {
                String file, archive;
                System.out.print("=> Enter name with path of file: ");
                file = sc.nextLine();
                System.out.print("=> Enter name with path of archive file: ");
                archive = sc.nextLine();
                try {
                    comp(file, archive);
                }
                catch (Exception e) {
                    System.out.println("=> Error while Compressing.");
                }
            }
            else if (query.equalsIgnoreCase("decomp")) {
                String file, archive;
                System.out.print("=> Enter name with path of archive file: ");
                archive = sc.nextLine();
                System.out.print("=> Enter name with path of file: ");
                file = sc.nextLine();
                try {
                    decomp(file, archive);
                }
                catch (Exception e) {
                    System.out.println("=> Error while Decompressing.");
                }
            }
            else {
                System.out.println("=> Enter correct query.");
            }

        } while (!query.equalsIgnoreCase("exit"));
        System.out.println("Goodbye!");
    }
}
