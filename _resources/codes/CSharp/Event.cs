namespace ConsoleApp1;

class EventDemo
{
    public static void Main(string[] args)
    {
        // 创建事件管理器
        var dealer = new CarDealer();

        var daniel = new Consumer("Daniel");
        // 注册事件处理器
        dealer.NewCarInfo += daniel.NewCarHere;
        // 发送事件
        dealer.NewCar("Mercedes");

        var sebastian = new Consumer("sebastian");
        // 注册事件处理器
        dealer.NewCarInfo += sebastian.NewCarHere;
        // 发送事件
        dealer.NewCar("Ferrari");

        // 注销事件处理器
        dealer.NewCarInfo -= sebastian.NewCarHere;
        // 发送事件
        dealer.NewCar("Red Bull Racing");
    }
}

// event 事件对象
public class CarInfoEventArgs(string car) : EventArgs
{
    public string Car { get; } = car;
}

// 事件处理方法
internal class Consumer(string v)
{
    internal void NewCarHere(object sender, CarInfoEventArgs e)
    {
        Console.WriteLine($"{v}: car {e.Car} is now");
    }
}

// 事件管理器
public class CarDealer
{
    private EventHandler<CarInfoEventArgs> _newCarInfo;
    public event EventHandler<CarInfoEventArgs> NewCarInfo
    {
        add => _newCarInfo += value;
        remove => _newCarInfo -= value;
    }

    public void NewCar(string car)
    {
        Console.WriteLine($"CarDealer, new car {car}");
        // 触发事件 
        _newCarInfo.Invoke(this, new CarInfoEventArgs(car));
    }
}