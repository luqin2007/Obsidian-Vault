void outMethod(out int a, out object b) {
    // ↓ out 必须在使用前对其赋值
    a = 1;
    b = new object();
    //  1, System.Object
    Console.WriteLine($"{a}, {b}");
}
   
int a = 0;
object b = new { name = "b" };
//  0, { name = b }
Console.WriteLine($"{a}, {b}");
outMethod(out a, out b);
//  1, System.Object
Console.WriteLine($"{a}, {b}");