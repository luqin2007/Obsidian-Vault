using System.Reflection;

namespace ConsoleApp1;

public class Explicit
{
    public static unsafe void Main(string[] args)
    {
        var object1 = new
        {
            name = "test1",
            age = 10,
        };

        var object2 = new
        {
            name = "test2",
            age = 20,
        };
        Type obj1Type = object1.GetType();
        bool isSameType = obj1Type.IsInstanceOfType(object2);
        // True
        Console.WriteLine(isSameType);
    }
}