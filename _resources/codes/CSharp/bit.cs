using System.Collections;
using System.Collections.Specialized;

namespace ConsoleApp1;

public class Bit
{
    public static void Main(string[] args)
    {
        var bits1 = new BitArray(10);
        bits1.Length = 11;
        bits1.Not();
        var bits2 = new BitVector32(10);
        var bit1Mask = BitVector32.CreateMask();         // 第一位
        var bit2Mask = BitVector32.CreateMask(bit1Mask); // 第二位
        var bit3Mask = BitVector32.CreateMask(bit2Mask); // 第三位
        var bit4Mask = BitVector32.CreateMask(bit3Mask); // 第四位
        bits2[bit1Mask] = true;
        bits2[0xacdef] = true; // 自定义掩码 结果为 0xabcdef
        var bitSection1 = BitVector32.CreateSection(0xfff);
        var bitSection2 = BitVector32.CreateSection(0xff, bitSection1);
        var bitSection3 = BitVector32.CreateSection(0xf, bitSection2);
        var bitSection4 = BitVector32.CreateSection(0x7, bitSection3);
        var bitSection5 = BitVector32.CreateSection(0x7, bitSection4);
        var bitSection6 = BitVector32.CreateSection(0x3, bitSection5);
        var b2s1 = bits2[bitSection1];
        var b2s2 = bits2[bitSection2];
        var b2s3 = bits2[bitSection3];
        var b2s4 = bits2[bitSection4];
        var b2s5 = bits2[bitSection5];
        var b2s6 = bits2[bitSection6];
    }
}