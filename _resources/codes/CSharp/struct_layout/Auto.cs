using System.Runtime.InteropServices;

namespace ConsoleApp1;

[StructLayout(LayoutKind.Auto)]
struct MyStruct
{
    public bool i;   // 1byte
    public double c; // 8byte
    public bool b;   // 1byte
}

public class Explicit
{
    public static unsafe void Main(string[] args)
    {
        var size = sizeof(MyStruct);
        MyStruct s;
        // 8
        Console.WriteLine($"Size is {size}");
        Console.WriteLine($"Address[i] is {(int) &s.i}");
        Console.WriteLine($"Address[c] is {(int) &s.c}");
        Console.WriteLine($"Address[b] is {(int) &s.b}");
    }
}