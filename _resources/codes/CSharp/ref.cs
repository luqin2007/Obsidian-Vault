void method(A a) {
    a.X = 2;
    a = new A { X = 3 };
}

void methodRef(ref A a) {
    a.X = 2;
    a = new A { X = 3 };
}

A a1 = new A { X = 1 };
A a2 = new A { X = 1 };
method(a1);
methodRef(ref a2);
Console.WriteLine(a1.X); // 3 a 是 a1 的引用
Console.WriteLine(a2.X); // 2 a 是 a1

class A {
    public int X { get; set; }
}