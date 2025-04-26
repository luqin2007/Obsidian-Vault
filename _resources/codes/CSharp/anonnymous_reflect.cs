using System.Reflection;

namespace ConsoleApp1;

public class AnonnymousClass
{
    public static void Main(string[] args)
    {
        var object1 = new
        {
            name = "test1",
            age = 10,
        };

        Type obj1Type = object1.GetType();
        foreach (var field in obj1Type.GetRuntimeFields())
        {
            Console.WriteLine(field.Name);
            Console.WriteLine(field.GetValue(object1));
        }
    }
}