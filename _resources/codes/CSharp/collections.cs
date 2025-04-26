namespace ConsoleApp1;

class Racer
{
    public string Name;
    public string Country;
}

public class Collections
{
    public static void Main(string[] args)
    {
        // list
        var intList = new List<int>(10) { 0, 1 };
        intList.Capacity = 10;
        intList.TrimExcess();
        var intListReadonly = intList.AsReadOnly();
        
        // queue
        var intQueue = new Queue<int>();
        intQueue.Enqueue(1);            // 入列
        var qg = intQueue.Dequeue(); // 出列
        var qr = intQueue.Peek();    // 出列但不删除
        
        // stack
        var intStack = new Stack<int>();
        intStack.Push(1);              // 入栈
        var q = intStack.Pop();        // 出栈
        var e = intStack.Contains(1);  // 是否存在
        
        // linked list
        LinkedList<int> intLinkedList = new LinkedList<int>();
        intLinkedList.AddFirst(1);
        int il = intLinkedList.First();
        
        // dictionary
        var dictionary = new Dictionary<string, int>()
        {
            ["1"] = 1,
            ["2"] = 2
        };
        var id1 = dictionary["1"];
        var defInt = -1;
        var has1 = dictionary.TryGetValue("3", out defInt);

        // 有序字典: 二叉搜索树
        // 使用内存比 SortedList 多但插入删除操作比其快
        // key 类型必须实现 IComparable<TKey> 接口
        var sorted = new SortedDictionary<string, int>();

        // Lookup: Key -> [Value], 键指向的值是一组值
        // 不能用一般方法创建, 只能使用 toLookup(selector) 方法
        // selector 是一个选择器, value => key
        var racers = new List<Racer>();
        var lookup1 = racers.ToLookup(r => r.Country);
        foreach (Racer r in lookup1["ca"])
        {
            Console.WriteLine(r);
        }
    }
}