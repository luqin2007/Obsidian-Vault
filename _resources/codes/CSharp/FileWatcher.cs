namespace ConsoleApp1;

class FileWatcher
{
    public static void Main(string[] args)
    {
        Directory.CreateDirectory("./files");
        
        while (true)
        {
            var watcher = new FileSystemWatcher("./files", "*.txt");
            watcher.IncludeSubdirectories = true;
            watcher.EnableRaisingEvents = true;
            watcher.Created += (_, e) => Console.WriteLine($"Created: {e.FullPath}");
            watcher.Changed += (_, e) => Console.WriteLine($"Changed: {e.FullPath} {e.ChangeType}");
            watcher.Deleted += (_, e) => Console.WriteLine($"Deleted: {e.FullPath}");
            watcher.Renamed += (_, e) => Console.WriteLine($"Renamed: {e.OldName} -> {e.Name}");
            watcher.Error += (_, e) => Console.WriteLine("Error: " + e.GetException().Message);
        }
    }
}
