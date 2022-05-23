class Huffman_Node
{
    Character ch;
    Integer frequency;
    Huffman_Node left = null, right = null;

    Huffman_Node(Character ch, Integer freq)
    {
        this.ch = ch;
        this.frequency = freq;
    }

    public Huffman_Node(Character ch, Integer freq, Huffman_Node left, Huffman_Node right)
    {
        this.ch = ch;
        this.frequency = freq;
        this.left = left;
        this.right = right;
    }
}