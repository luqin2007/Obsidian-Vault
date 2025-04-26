using System.Runtime.InteropServices;

namespace ConsoleApp1;

[StructLayout(LayoutKind.Explicit)]
struct MyStruct
{
    [FieldOffset(0)]
    public bool i;   // 1byte
    [FieldOffset(0)]
    public double c; // 8byte
    [FieldOffset(0)]
    public bool b;   // 1byte
}

public class Explicit
{
    public static unsafe void Main(string[] args)
    {
        var size = sizeof(MyStruct);
        // 8
        Console.WriteLine($"Size is {size}");
    }
}