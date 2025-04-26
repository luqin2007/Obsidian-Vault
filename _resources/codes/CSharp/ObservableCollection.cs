using System.Collections;
using System.Collections.ObjectModel;
using System.Text;

namespace ConsoleApp1;

public class ObservableCollection
{
    public static void Main(string[] args)
    {
        var observableCollection = new ObservableCollection<int>();

        observableCollection.CollectionChanged += (sender, e) =>
        {
            Console.WriteLine("-----------------------------------------------");
            Console.WriteLine("Action: " + e.Action);
            Console.WriteLine("Start Index: from " + e.OldStartingIndex + " to " + e.NewStartingIndex);
            Console.WriteLine("Data Change: " + ToString(e.OldItems) + " => " + ToString(e.NewItems));
        };
        
        observableCollection.Add(10);
        observableCollection.Add(20);
        observableCollection.Remove(10);
    }

    private static string ToString(IList? list)
    {
        if (list is null)
        {
            return "null";
        }

        if (list.Count == 0)
        {
            return "[]";
        }
        else
        {
            var sb = new StringBuilder("[").Append(list[0]);
            for (var i = 1; i < list.Count; i++)
            {
                sb.Append(", ").Append(list[i]);
            }

            return sb.Append(']').ToString();
        }
    }
}