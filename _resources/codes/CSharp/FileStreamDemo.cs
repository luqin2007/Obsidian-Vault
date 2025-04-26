using System.Text;

namespace ConsoleApp1;

using static Console;

class FileWatcherDemo   
{
    private const int RECORDSIZE = 4096; 
    
    public static void Main(string[] args)
    {
        using var stream = File.OpenRead("./samplefile.data");
        var buffer = new byte[RECORDSIZE];
        while (true)
        {
            try
            {
                WriteLine("record number (or 'bye' to End): ");
                var line = ReadLine();
                
                if (line.ToUpper().CompareTo("BYE") == 0)
                    break;

                if(int.TryParse(line, out var record))
                {
                    stream.Seek((record - 1) * RECORDSIZE, SeekOrigin.Begin);
                    stream.Read(buffer, 0, RECORDSIZE);
                    var s = Encoding.UTF8.GetString(buffer);
                    WriteLine($"record: {s}");
                }
            }
            catch (Exception ex)
            {
                WriteLine(ex.Message);
            }
        }
        WriteLine("finished");
    }
}
