namespace ConsoleApp1;

public interface IInterface1 {
    void MethodInInterface1(int i);
}

public interface IInterface2: IInterface1 {
    int ValueInInterface2 {get;set;}
}

public class MyClass: IInterface2 {
    private int Value { get; set; }

    public void MethodInInterface1(int i) {
        Console.WriteLine(i);
    }

    public int ValueInInterface2
    {
        get => Value;
        set => Value = value;
    }
}

public class interfaces
{
    public static void Main(string[] args)
    {
        }
}