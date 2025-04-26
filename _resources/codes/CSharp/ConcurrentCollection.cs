using System.Collections.Concurrent;

namespace ConsoleApp1;

class PipelineSample
{
    public static void Main(string[] args)
    {
            StartPipelineAsync().Wait();
    }
    
    // 示例异步任务
    public static async Task StartPipelineAsync()
    {
        var fileNames = new BlockingCollection<string>();
        var lines = new BlockingCollection<string>();
        var words = new ConcurrentDictionary<string, int>();
        var items = new BlockingCollection<Info>();
        var coloredItems = new BlockingCollection<Info>();
        // 读入文件名
        Task t1 = PipelineStages.ReadFilenameAsync(@"../../..", fileNames);
        ColoredConsole.WriteLine("started stage 1");
        // 加载文件内容
        Task t2 = PipelineStages.LoadContentAsync(fileNames, lines);
        ColoredConsole.WriteLine("started stage 2");
        // 提取文件关键字
        Task t3 = PipelineStages.ProcessContentAsync(lines, words);
        await Task.WhenAll(t1, t2, t3);
        ColoredConsole.WriteLine("stage 1, 2, 3 completed");

        // 转换为 Info 对象
        Task t4 = PipelineStages.TransferContentAsync(words, items);
        // 附加颜色
        Task t5 = PipelineStages.AddColorAsync(items, coloredItems);
        // 输出
        Task t6 = PipelineStages.ShowContentAsync(coloredItems);
        ColoredConsole.WriteLine("stage 4, 5, 6 started");
        await Task.WhenAll(t4, t5, t6);
        ColoredConsole.WriteLine("all stages finished");
    }
}

// 数据类
internal class Info
{
    public string Word { get; set; }
    public int Count { get; set; }
    public string Color { get; set; }
    public override string ToString() => $"{Count} times: {Word}";
}

// 异步任务类
internal static class PipelineStages
{
    public static Task ReadFilenameAsync(string path, BlockingCollection<string> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var filename in Directory.EnumerateFiles(path, "*.cs", SearchOption.AllDirectories))
            {
                output.Add(filename);
                ColoredConsole.WriteLine($"stage 1: added {filename}");
            }
            output.CompleteAdding();
        }, TaskCreationOptions.LongRunning);
    }

    public static async Task LoadContentAsync(BlockingCollection<string> input, BlockingCollection<string> output)
    {
        foreach (var filename in input.GetConsumingEnumerable())
        {
            await using var stream = File.OpenRead(filename);
            var reader = new StreamReader(stream);
            while (await reader.ReadLineAsync() is { } line)
            {
                output.Add(line);
                ColoredConsole.WriteLine($"stage 2: added {line}");
            }
        }
        output.CompleteAdding();
    }

    public static Task ProcessContentAsync(BlockingCollection<string> input, ConcurrentDictionary<string, int> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var line in input.GetConsumingEnumerable())
            {
                var words = line.Split(' ', ';', '\t', '{', '}', '(', ')', ':', ',', '"');
                foreach (var word in words.Where(w => !string.IsNullOrEmpty(w)))
                {
                    output.AddOrUpdate(key: word, addValue: 1, updateValueFactory: (s, i) => ++i);
                    ColoredConsole.WriteLine($"stage3: added {word}");
                }
            }
        }, TaskCreationOptions.LongRunning);
    } 

    public static Task TransferContentAsync(ConcurrentDictionary<string, int> input, BlockingCollection<Info> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var word in input.Keys)
            {
                if (input.TryGetValue(word, out var value))
                {
                    var info = new Info
                    {
                        Word = word,
                        Count = value
                    };
                    output.Add(info);
                    ColoredConsole.WriteLine($"stage 4: added {info}");
                }
            }
            output.CompleteAdding();
        }, TaskCreationOptions.LongRunning);
    }

    public static Task AddColorAsync(BlockingCollection<Info> input, BlockingCollection<Info> output)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var item in input.GetConsumingEnumerable())
            {
                item.Color = item.Count switch
                {
                    > 40 => "Red",
                    > 20 => "Yellow",
                    _ => "Green"
                };
                output.Add(item);
                ColoredConsole.WriteLine($"stage 5: added color {item.Color} to {item}");
            }
            output.CompleteAdding();

        }, TaskCreationOptions.LongRunning);
    }

    public static Task ShowContentAsync(BlockingCollection<Info> input)
    {
        return Task.Factory.StartNew(() =>
        {
            foreach (var item in input.GetConsumingEnumerable())
            {
                ColoredConsole.WriteLine($"stage 6: {item}", item.Color);
            }
        }, TaskCreationOptions.LongRunning);
    }
}

internal static class ColoredConsole
{
    private static readonly object SyncOutput = new object();

    public static void WriteLine(string message)
    {
        lock (SyncOutput)
        {
            Console.WriteLine(message);
        }
    }

    public static void WriteLine(string message, string color)
    {
        lock (SyncOutput)
        {
            Console.ForegroundColor = Enum.Parse<ConsoleColor>(color);
            Console.WriteLine(message);
            Console.ResetColor();
        }
    }
}