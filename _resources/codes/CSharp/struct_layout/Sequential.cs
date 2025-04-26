struct MyStruct
{
    public bool i;
    public double c;
    public bool b;
}

class Problem
{
    public static unsafe void Main(string[] args)
    {
        var size = sizeof(MyStruct);
        // 24
        Console.WriteLine($"Size is {size}");
    }
}