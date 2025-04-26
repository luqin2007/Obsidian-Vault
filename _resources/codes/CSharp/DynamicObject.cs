using System.Dynamic;

namespace ConsoleApp1;

class MyDynamicObject
{
    public static void Main(string[] args)
    {
        dynamic wrox = new WroxDynamicObject();
        wrox.CHZY = 10;
        Console.WriteLine(wrox.CHZY);
    }
}

public class WroxDynamicObject: DynamicObject
{
    private readonly Dictionary<string, object> _dynamicData = new();
    
    public override bool TryGetMember(GetMemberBinder binder, out object result)
    {
        var success = false;
        if (_dynamicData.TryGetValue(binder.Name, out var value))
        {
            result = value;
            success = true;
        }
        else
        {
            result = "Property Not Found!";
            success = false;
        }
        return success;
    }
    
    public override bool TrySetMember(SetMemberBinder binder, object? value)
    {
        _dynamicData[binder.Name] = value ?? string.Empty;
        return true;
    }
    
    public override bool TryInvokeMember(InvokeMemberBinder binder, object?[]? args, out object result)
    {
        dynamic method = _dynamicData[binder.Name];
        result = method(args);
        return result != null;
    }
}