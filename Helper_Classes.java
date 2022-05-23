import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

class Helper_Classes {
    public static void encode_huffman(Huffman_Node root_node, String str, Map<Character, String> huffman_Code) {
        if (root_node == null) {
            return;
        }

        if (is_Leaf(root_node)) {
            huffman_Code.put(root_node.ch, str.length() > 0 ? str : "1");
        }

        encode_huffman(root_node.left, str + '0', huffman_Code);
        encode_huffman(root_node.right, str + '1', huffman_Code);
    }

    public static int decode_huffman(Huffman_Node root_node, int index, StringBuilder sb, String filename) {
        if (root_node == null) {
            return index;
        }

        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;
        try {
            fw = new FileWriter(filename, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            if (is_Leaf(root_node)) {
                System.out.print(root_node.ch);
                pw.print(root_node.ch);
                return index;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        } finally {
        try {
            pw.close();
            bw.close();
            fw.close();
        } catch (IOException io) { }
        }

        index++;
        root_node = (sb.charAt(index) == '0') ? root_node.left : root_node.right;
        index = decode_huffman(root_node, index, sb, filename);
        return index;
    }

    public static boolean is_Leaf(Huffman_Node root_node) {
        return root_node.left == null && root_node.right == null;
    }
}
