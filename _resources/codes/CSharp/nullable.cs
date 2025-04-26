class Problem
{
    public static void Main(string[] args)
    {
        int? a = 0;
        int? b = null;
   
        bool av = a.HasValue;  // true
        bool bv = b.HasValue;  // false
        int c = a ?? -1;       // 0
        int d = b ?? -1;       // -1

        Console.WriteLine($"a={a}, hasValue={av}, a??-1={c}");
        Console.WriteLine($"b={b}, hasValue={bv}, b??-1={d}");
    }
}