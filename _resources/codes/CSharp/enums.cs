namespace ConsoleApp1;

enum Days
{
    Monday = 0x1,
    Tuesday = 0x2,
    Wednesday = 0x4,
    Thursday = 0x8,
    Friday = 0x10,
    Saturday = 0x20,
    Sunday = 0x40,

    // 结合单个位设置不同值
    Weekend = Saturday | Sunday,
    Workday = 0x1f,
    AllWeek = Workday | Weekend,
}

public class Enums
{
    public static void Main(string[] args)
    {
        Days day = Days.Monday | Days.Tuesday;
        // Monday | Tuesday
        //   = 1 | 2
        //   = 3
        Console.WriteLine(day);
    }
}