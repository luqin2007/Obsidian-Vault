[[转]深入理解ruby闭包 - 简书 (jianshu.com)](https://www.jianshu.com/p/bf4c7ba961bb)

> I recommend executing this file, then reading it alongside its output. Alteratively, you can give yourself a sort of Ruby test by deleting all the comments, then trying to guess the output of the code! A closure is a block of code which meets three criteria:  
> * 它可以作为一个值被传入  
> * 在任何时候根据不同人的需要来执行它  
> * 它可以使用创建它的那个上下文中的变量（即它是对封闭变量的访问，数学意义上的词“封闭”）

```ruby
def example(num)  
    puts  
    puts "------ Example #{num} ------"  
end  
```

-------------------- Section 1: Blocks ----------------------------  
----------------------- 章节一：块 -------------------------------

> Blocks are like closures, because they can refer to variables from their defining context:  
> 块就像闭包，因为他们可以使用定义它们的那个上下文中的变量。
### example 1

```ruby
def thrice  
   yield  
   yield  
   yield  
end  
x = 5  
puts "value of x before: #{x}"  
thrice { x += 1 }  
puts "value of x after: #{x}"  
```

> A block refers to variables in the context it was defined, not the context in which it is called:  
> 一个块使用定义它的上下文中的变量，而不是被调用的上下文中的变量。
### example 2

```ruby
def thrice_with_local_x  
   x = 100  
   yield  
   yield  
   yield  
   puts "value of x at end of thrice_with_local_x: #{x}"  
end  
 
x = 5  
thrice_with_local_x { x += 1 }  
puts "value of outer x after: #{x}"  
```

> A block only refers to _existing_ variables in the outer context; if they don't exist in the outer, a  
> block won't create them there:  
> 一个块仅仅使用定义它的上下文中已经存在的变量，如果变量不存在外于部上下文，块不会去创建他们。
### example 3

```ruby
# note that {...} and do...end are completely equivalent
# 注意{...}和do...end是完全相等的  
thrice do 
   y = 10  
   puts "Is y defined inside the block where it is first set?"  
   puts "Yes." if defined? y  
end  
puts "Is y defined in the outer context after being set in the block?"  
puts "No!" unless defined? y  
```

> OK, so blocks seem to be like closures: they are closed with respect to variables defined in the context  
> where they were created, regardless of the context in which they're called.  
> But they're not quite closures as we've been using them, because we have no way to pass them around:  
> "yield" can _only_ refer to the block passed to the method it's in.
> We can pass a block on down the chain, however, using &:  
> 
> 所以块看起来像闭包：他们封闭了定义它们的上下文中的变量，不管他们在哪里调用。  
> 但是在我们使用它们时，它们不完全是闭包，因为我们没有办法传递他们。  
> “yield”仅仅可以将块传给它所在的方法。
### example 4

```ruby
def six_times(&block)  
    thrice(&block)  
    thrice(&block)  
end  
  
x = 4  
six_times { x += 10 }  
puts "value of x after: #{x}"  
```

> So do we have closures? Not quite! We can't hold on to a &block and call it later at an arbitrary  
> time; it doesn't work. This, for example, will not compile:  
> def save_block_for_later(&block)  
> saved = █  
> end  
> But we _can_ pass it around if we use drop the &, and use block.call(...) instead of yield:  
> 所以我们是否有闭包？不完全！我们不能保存一个&block，然后稍后在任意时间来调用它。  
> 但是如果我们丢掉&，我们就有办法传递它了，使用block,call(...)代替yield。
### example 5

```ruby
def save_for_later(&b)  
    # Note: no ampersand! This turns a block into a closure of sorts. 
    # 注意：没有&符号！这个做法将一个块变成了一个闭包。
    @saved = b  
end    
save_for_later { puts "Hello!" }  
puts "Deferred execution of a block:"  
@saved.call  
@saved.call  
```

> But wait! We can't pass multiple blocks to a function! As it turns out, there can be only zero  
> or one &block_params to a function, and the &param _must_ be the last in the list.  
> None of these will compile:  
> def f(&block1, &block2) ...
> def f(&block1, arg_after_block) ...  
> f { puts "block1" } { puts "block2" }  
> What the heck?  
> I claim this single-block limitation violates the "principle of least surprise." The reasons for  
> it have to do with ease of C implementation, not semantics.  
> So: are we screwed for ever doing anything robust and interesting with closures?  
> 
> 等等！我们不能传递多个块给一个函数！一个函数只能接收0或1个&block_params参数，并且&block_params必须是最后一个参数。  
> 我觉得这个单block违反了最小惊讶原则，因为这很容用C来实现，不是语义上的。

------------------ Section 2: Closure-Like Ruby Constructs ----------------------------  
-------------------- 章节二：闭包风格的Ruby构造器 ----------------------------------

> 实际上，没有。当我们传递一个块&param，然后不带&来使用param，这是Proc.new(&param)的隐式同义词。
### example 6

```ruby
def save_for_later(&b)  
    @saved = Proc.new(&b) # same as: @saved = b  
end  
  
save_for_later { puts "Hello again!" }  
puts "Deferred execution of a Proc works just the same with Proc.new:"  
@saved.call  
```

> We can define a Proc on the spot, no need for the &param:  
> 我们可以随意定义一个Proc，不需要&param。
### example 7

```ruby
@saved_proc_new = Proc.new { puts "I'm declared on the spot with Proc.new." }  
puts "Deferred execution of a Proc works just the same with ad-hoc Proc.new:"  
@saved_proc_new.call  
```

> Behold! A true closure!  
> But wait, there's more.... Ruby has a whole bunch of things that seem to behave like closures,  
> and can be called with .call:  
> 看！一个真的闭包！  
> 等等，还有跟精彩的。。。Ruby还有一堆东西的行为看起来像闭包，并且能用.call来调用。
### example 8

```ruby
@saved_proc_new = Proc.new { puts "I'm declared with Proc.new." }  
@saved_proc = proc { puts "I'm declared with proc." }  
@saved_lambda = lambda { puts "I'm declared with lambda." }  
def some_method   
  puts "I'm declared as a method."  
end  
@method_as_closure = method(:some_method)  

puts "Here are four superficially identical forms of deferred execution:"  
@saved_proc_new.call  
@saved_proc.call  
@saved_lambda.call  
@method_as_closure.call  
```

> So in fact, there are no less than seven -- count 'em, SEVEN -- different closure-like constructs in Ruby:  
> 1. block (implicitly passed, called with yield)  
> 2. block (&b => f(&b) => yield)  
> 3. block (&b => b.call)  
> 4. Proc.new  
> 5. proc  
> 6. lambda  
> 7. method  
> Though they all look different, some of these are secretly identical, as we'll see shortly.  
> We already know that (1) and (2) are not really closures -- and they are, in fact, exactly the same thing.  
> Numbers 3-7 all seem to be identical. Are they just different syntaxes for identical semantics?
> No, they aren't! One of the distinguishing features has to do with what "return" does.  
> Consider first this example of several different closure-like things _without_ a return statement.  
> They all behave identically:  
> 
> 实际上Ruby有不少于7中不同的闭包风格的构造器  
> 尽管他们看起来不一样，有些其实是完全相同的，我们后面马上会看到。  
> 我们已经知道(1)和(2)不是真正的闭包，其实(1)和(2)是同样的东西。  
> 3-7看上去是完全一样的，他们是不是语义相同的不用语法？

----------------- Section 3: Closures and Control Flow ----------------------------  
-------------------- 章节三：闭包后控制流 --------------------------------------
### example 9

```ruby
def f(closure)  
    puts  
    puts "About to call closure"  
    result = closure.call  
    puts "Closure returned: #{result}"  
    "Value from f"  
end  
  
puts "f returned: " + f(Proc.new { "Value from Proc.new" })  
puts "f returned: " + f(proc { "Value from proc" })  
puts "f returned: " + f(lambda { "Value from lambda" })  
def another_method  
    "Value from method"  
end  
puts "f returned: " + f(method(:another_method))  
  
```

> But put in a "return," and all hell breaks loose!  
> 但是加上一个“return”语句，一切都变的不可收拾。
### example 10

```ruby
begin  
    f(Proc.new { return "Value from Proc.new" })  
rescue Exception => e  
    puts "Failed with #{e.class}: #{e}"  
end  
```

> The call fails because that "return" needs to be inside a function, and a Proc isn't really quite a full-fledged function: 
> 这样的调用会抛出异常因为“return”必须在一个函数内，而一个Proc不是一个真正的完全独立的函数。
### example 11

```ruby
def g  
   result = f(Proc.new { return "Value from Proc.new" })  
   puts "f returned: " + result #never executed  
   "Value from g"               #never executed  
end  
 
puts "g returned: #{g}"  
```

> Note that the return inside the "Proc.new" didn't just return from the Proc -- it returned  
> all the way out of g, bypassing not only the rest of g but the rest of f as well! It worked  
> almost like an exception.  
> This means that it's not possible to call a Proc containing a "return" when the creating  
> context no longer exists:  
> 注意在“Proc.new”中的return语句不仅仅从Proc中返回，它将从g方法中返回，不仅绕过g方法中剩余的代码  ，而且f方法中也是一样的!它就像一个异常一样。  
> 这意味着当创建Proc的上下文不存在时，无法调用一个包含“return”的Proc  

> 译者本人的理解：  
> 在Proc.new中的return语句，会尝试返回到创建它的上下文之后，继续执行，如果创建它的上下文已经“消失”或者返回到了全局上下文，就会出现LocalJumpError。  
> 在#example10中，在全局上下文中用Proc.new创建了一个proc，在f方法中使用call，然后proc会返回到创建它的上下文，即全局上下文，于是出了LocalJumpError。  
> 在#example11中，在g方法的上下文中用Proc.new创建了一个proc，在f方法中使用call，然后proc会返回到创建它的上下文，即g方法之后，这种情况不会出异常。
### example 12

```ruby
def make_proc_new  
  begin  
      Proc.new { return "Value from Proc.new" } 
 # this "return" will return from make_proc_new 这个“return”会返回到make_proc_new方法之外  
  ensure  
      puts "make_proc_new exited"  
  end  
end  

begin  
  puts make_proc_new.call  
rescue Exception => e  
  puts "Failed with #{e.class}: #{e}"  
end  
```

> (Note that this makes it unsafe to pass Procs across threads.)  
> （注意跨线程的传递Proc会导致它不安全。）

> A Proc.new, then, is not quite truly closed: it depends in the creating context still existing,  
> because the "return" is tied to that context.  
> Proc.new 不是一个真正完全的闭包。它依赖于创建它的上下文要一直存在，因为“return”和那个上下文联席在了一起。  
> Not so for lambda:  
> lambda就不是这个样子：

> 译者本人的理解：  
> 就和刚刚讲的一样，如果创建它的上下文已经“消失”，在这里make_proc_new方法把创建的proc返回了出去，所以make_proc_new的上下文就不存在了，  
> 这时候再使用call，也会发生LocalJumpError。  
### example 13

```ruby
def g  
    result = f(lambda { return "Value from lambda" })  
    puts "f returned: " + result  
    "Value from g"  
end  
  
puts "g returned: #{g}"  
```

> And yes, you can call a lambda even when the creating context is gone:  
> 你可以调用lambda，即使创建它的上下文已经不在。
### example 14

```ruby
def make_lambda  
    begin  
        lambda { return "Value from lambda" }  
    ensure  
        puts "make_lambda exited"  
    end  
end  
  
puts make_lambda.call  
```

> Inside a lambda, a return statement only returns from the lambda, and flow continues normally.  
> So a lambda is like a function unto itself, whereas a Proc remains dependent on the control  
> flow of its caller.  
> 在lambda内部，return语句仅仅从lambda中返回，代码流程不会改变。  
> 所以lambda就像一个方法一样，而Proc需要依赖于它调用者的控制流。  
> A lambda, therefore, is Ruby's true closure.  
> 所以lambda才是ruby真正的闭包。  
> As it turns out, "proc" is a synonym for either "Proc.new" or "lambda."  
> Anybody want to guess which one? (Hint: "Proc" in lowercase is "proc.")  
> 事实证明，"proc"是“Proc.new”或者“lambda”的同义词。  
> 有人想猜一猜到底是哪个吗？（提示：“Proc”的小写是“proc”。）
### example 15

```ruby
def g  
    result = f(proc { return "Value from proc" })  
    puts "f returned: " + result  
    "Value from g"  
end  
  
puts "g returned: #{g}"  
```

> The answer: Ruby changed its mind. If you're using Ruby 1.8, it's a synonym for "lambda."  
> That's surprising (and also ridiculous); somebody figured this out, so in 1.9, it's a synonym for  
> Proc.new. Go figure.  
> 答案是如果你使用ruby1.8，那么它是“lambda”的同义词。  
> 这真得让人惊讶（而且荒谬），有些人指出了这一点，所以在ruby1.9，它是“Proc.new”的同义词了。  
> I'll spare you the rest of the experiments, and give you the behavior of all 7 cases:  
> 我就不进行剩下的实验了，但是会给出所有的7种示例。

```objectivec
# "return" returns from caller:  
#      1. block (called with yield)  
#      2. block (&b  =>  f(&b)  =>  yield)    
#      3. block (&b  =>  b.call)      
#      4. Proc.new  
#      5. proc in 1.9  
#  
# "return" only returns from closure:  
#      5. proc in 1.8  
#      6. lambda      
#      7. method  
```

-------------------- Section 4: Closures and Arity ----------------------------  
--------------------- 章节四：闭包和元数 ----------------------------------

> 注：arity => 一个方法或者函数可以接受的参数个数

> The other major distinguishing of different kinds of Ruby closures is how they handle mismatched  
> arity-- in other words, the wrong number of arguments.  
> 另外一个主要辨别不同种类的ruby闭包是他们如何处理不匹配的元数，换句话说，就是不正确的参数个数。  
> In addition to "call," every closure has an "arity" method which returns the number of expected  
> arguments:  
> 除了“call”以外，每个闭包还有一个“arity”方法可以返回期望的参数个数。
### example 16

```cpp
puts "One-arg lambda:"  
puts (lambda {|x|}.arity)  
puts "Three-arg lambda:"  
puts (lambda {|x,y,z|}.arity)  
 
puts "No-args lambda: "  
puts (lambda {}.arity) 
# This behavior is also subject to change in 1.9. #这个行为在1.9中也会有变化。 1.8中是-1，1.9中是0  
puts "Varargs lambda: "  
puts (lambda {|*args|}.arity)  
```

> Watch what happens when we call these with the wrong number of arguments:  
> 看看当我们使用不真确的参数个数来调用他们时，会发生什么。

### example 17

```ruby
def call_with_too_many_args(closure)  
   begin  
       puts "closure arity: #{closure.arity}"  
       closure.call(1,2,3,4,5,6)  
       puts "Too many args worked"  
   rescue Exception => e  
       puts "Too many args threw exception #{e.class}: #{e}"  
   end  
end  
 
def two_arg_method(x,y)  
end  
 
puts; puts "Proc.new:"; call_with_too_many_args(Proc.new {|x,y|})  
puts; puts "proc:"    ; call_with_too_many_args(proc {|x,y|})  
puts; puts "lambda:"  ; call_with_too_many_args(lambda {|x,y|})  
puts; puts "Method:"  ; call_with_too_many_args(method(:two_arg_method))  
 
def call_with_too_few_args(closure)  
   begin  
       puts "closure arity: #{closure.arity}"  
       closure.call()  
       puts "Too few args worked"  
   rescue Exception => e  
       puts "Too few args threw exception #{e.class}: #{e}"  
   end  
end  
 
puts; puts "Proc.new:"; call_with_too_few_args(Proc.new {|x,y|})  
puts; puts "proc:"    ; call_with_too_few_args(proc {|x,y|})  
puts; puts "lambda:"  ; call_with_too_few_args(lambda {|x,y|})  
puts; puts "Method:"  ; call_with_too_few_args(method(:two_arg_method))  
```
### example 18

```cpp
def one_arg_method(x)  
end  
  
puts; puts "Proc.new:"; call_with_too_many_args(Proc.new {|x|})  
puts; puts "proc:"    ; call_with_too_many_args(proc {|x|})  
puts; puts "lambda:"  ; call_with_too_many_args(lambda {|x|})  
puts; puts "Method:"  ; call_with_too_many_args(method(:one_arg_method))  
puts; puts "Proc.new:"; call_with_too_few_args(Proc.new {|x|})  
puts; puts "proc:"    ; call_with_too_few_args(proc {|x|})  
puts; puts "lambda:"  ; call_with_too_few_args(lambda {|x|})  
puts; puts "Method:"  ; call_with_too_few_args(method(:one_arg_method))  
```

> Yet when there are no args...  
> 当他们没有参数时。。。
### example 19

```cpp
def no_arg_method  
end  
  
puts; puts "Proc.new:"; call_with_too_many_args(Proc.new {||})  
puts; puts "proc:"    ; call_with_too_many_args(proc {||})  
puts; puts "lambda:"  ; call_with_too_many_args(lambda {||})  
puts; puts "Method:"  ; call_with_too_many_args(method(:no_arg_method))  
```

> 注：#example17-19的结果在ruby1.8和1.9下，1.8下proc和lambda的执行结果完全一样，1.9下proc和Proc.new的结果完全相同。

------------------ Section 5: Rant ----------------------------  
------------------- 章节五：咆哮 -----------------------------

> This is quite a dizzing array of syntactic options, with subtle semantics differences that are not  
> at all obvious, and riddled with minor special cases. It's like a big bear trap from programmers who  
> expect the language to just work.  
> 真是让人蛋疼的语法，这些语法上的微妙差异不是那么显而易见的，而且还有不少小的特殊情况。  
> Why are things this way? Because Ruby is:  
> 为什么会这样呢？因为ruby是：  
> (1) designed by implementation, and  
> (2) defined by implementation.  
> 通过实现来设计和定义 （看不明白  
> The language grows because the Ruby team tacks on cool ideas, without maintaining a real spec apart from CRuby. A spec would make clear the logical structure of the language, and thus help highlight inconsistencies like the ones we've just seen. Instead, these inconsinstencies creep into the language, confuse the crap out of poor souls like me who are trying to learn it, and then get submitted as bug reports. Something as fundamental as the semantics of proc should not get so screwed up that they have to backtrack between releases, for heaven's sake! Yes, I know, language design is hard - but something like this proc/lambda issue or the arity problem wasn't so hard to get right the first time.  
> Yammer yammer.

--------------------- Section 6: Summary ----------------------------  
--------------------- 章节六：总结 -------------------------------

> So, what's the final verdict on those 7 closure-like entities?  
> 所以，那7个闭包风格的实体最终结论如下：  
> "return" returns from closure  
> True closure? or declaring context...? Arity check?

1. block (called with yield) N declaring no
2. block (&b => f(&b) => yield) N declaring no
3. block (&b => b.call) Y except return declaring warn on too few
4. Proc.new Y except return declaring warn on too few
5. proc <<< alias for lambda in 1.8, Proc.new in 1.9 >>>
6. lambda Y closure yes, except arity 1
7. method Y closure yes  
    The things within each of these groups are all semantically identical -- that is, they're different  
    syntaxes for the same thing:  
    下面的分组中的每个在语义上都是相同的，也就是说，只是语法不同的同一个东西。
    1. block (called with yield)
    2. block (&b => f(&b) => yield)
    3. block (&b => b.call)
    4. Proc.new
    5. proc in 1.9
    6. proc in 1.8
    7. lambda
    8. method (may be identical to lambda with changes to arity checking in 1.9) 在1.9中参数检查和lambda一样  
        Or at least, this is how I _think_ it is, based on experiment. There's no authoritative answer other  
        than testing the CRuby implementation, because there's no real spec -- so there may be other differences  
        I haven't discovered.  
        至少，根据实验，我是这么认为的。除了测试CRuby实现外，也没有其他的官方答案。因为没有真实的规范--所以也许和我发现的有些不同。  
        The final verdict: Ruby has four types of closures and near-closures, expressible in seven syntactic  
        variants. Not pretty. But you sure sure do cool stuff with them! That's up next....  
        最终结论：Ruby有四个类型的闭包和近似闭包，使用7中语法变形来体现，不是很好，但是你能他们来做一些很cool的东西。  
        This concludes the "Ruby sucks" portion of our broadcast; from here on, it will be the "Ruby is  
        awesome" portion.

------------------ Section 7: Doing Something Cool with Closures -----------------  
------------------ 章节七：用闭包来做些Cool的东西 -------------------------------

> Let's make a data structure containing all of the Fibonacci numbers. Yes, I said _all_ of them.  
> How is this possible? We'll use closures to do lazy evaluation, so that the computer only calculates  
> as much of the list as we ask for.  
> 我们创建一个数据结构包含所有的菲波那契数。是的，我说“所有的”。  
> 这怎么可能？我们将使用闭包来做到延迟求值，所以计算机仅仅会计算我们所要的。  
> To make this work, we're going to use Lisp-style lists: a list is a recursive data structure with  
> two parts: "car," the next element of the list, and "cdr," the remainder of the list.  
> 要让他工作，我们要使用Lisp风格的lists：一个包含2部分的可递归的数据结构：“car”，list中的下一个元素，“cdr”，list中剩余的元素。  
> For example, the list of the first three positive integers is `[1,[2,[3]]]`. Why? Because:  
> `[1,[2,[3]]] <--- car=1, cdr=[2,[3]]`  
> `[2,[3]] <--- car=2, cdr=[3]`  
> `[3] <--- car=3, cdr=nil`  
> Here's a class for traversing such lists:  
> 这有一个类来遍历这样的list
### example 20

```ruby
class LispyEnumerable  
    include Enumerable  
  
    def initialize(tree)  
        @tree = tree  
    end  
  
    def each  
        while @tree  
            car,cdr = @tree  
            yield car  
            @tree = cdr  
        end  
    end  
end  
  
list = [1,[2,[3]]]  
LispyEnumerable.new(list).each do |x|  
    puts x  
end  
```

> So how to make an infinite list? Instead of making each node in the list a fully built  
> data structure, we'll make it a closure -- and then we won't call that closure  
> until we actually need the value. This applies recursively: the top of the tree is a closure,  
> and its cdr is a closure, and the cdr's cdr is a closure....  
> 所以如和去创建一个无限的list？我们通过创建一个闭包来代替原来在list中内建的基本数据结构。  
> 除非我们真得须要数据，否则我们不会调用它。它会是一个递归。。。

### example 21
```ruby
class LazyLispyEnumerable  
   include Enumerable  
 
   def initialize(tree)  
       @tree = tree  
   end  
 
   def each  
       while @tree  
           car,cdr = @tree.call # <--- @tree is a closure  
           yield car  
           @tree = cdr  
       end  
   end  
end  
 
list = lambda{[1, lambda {[2, lambda {[3]}]}]}
# same as above, except we wrap each level in a lambda 和前面的一样，只是多包了一个lambda  
LazyLispyEnumerable.new(list).each do |x|  
   puts x  
end  
```
### example 22

> Let's see when each of those blocks gets called:  
> 让我们来看看这些block是什么时候被调用的

```cpp
list = lambda do  
    puts "first lambda called"  
    [1, lambda do  
        puts "second lambda called"  
        [2, lambda do  
            puts "third lambda called"  
            [3]  
        end]  
    end]  
end  
  
puts "List created; about to iterate:"  
LazyLispyEnumerable.new(list).each do |x|  
    puts x  
end  
```

> Now, because the lambda defers evaluation, we can make an infinite list:  
> 现在，因为lamdba的延迟求值，我们可以创建无限list。

### example 23

```ruby
def fibo(a,b)  
    lambda { [a, fibo(b,a+b)] }
 # <---- this would go into infinite recursion if it weren't in a lambda 这个会进入死循环如果它不在lambda内部  
end  
  
LazyLispyEnumerable.new(fibo(1,1)).each do |x|  
    puts x  
    break if x > 100 
   # we don't actually want to print all of the Fibonaccis! 我们实际上不会要打印所有的菲波那契数。  
end  
```

> This kind of deferred execution is called "lazy evaluation" -- as opposed to the "eager  
> evaluation" we're used to, where we evaluate an expression before passing its value on.  
> (Most languages, including Ruby, use eager evaluation, but there are languages (like Haskell)  
> which use lazy evaluation for everything, by default! Not always performant, but ever so very cool.)  
> 这就是延迟求值，和我们通常用得立即求值相反，我们在传送值之前就求值了表达式。  
> 大部分语言，包括ruby，都是立即求值，但是有些其他语言，比如Haskell，他默认就是对任何都延迟求值。  
> 这不是一直是高性能的，但是这非常cool。  
> This way of implementing lazy evaluation is terribly clunky! We had to write a separate  
> LazyLispyEnumerable that _knows_ we're passing it a special lazy data structure. How unsatisfying! Wouldn't it be nice of the lazy evaluation were invisible to callers of the lazy object?  
> 这种实现延迟求值的方式是非常笨拙的。我们不得不写一个单独的LazyLispyEnumerable，让他知道我们传了一个  
> 特别的延迟数据结构给他。能不能让他更好的处理延迟求值，让他对于调用者隐藏延迟对象？  
> As it turns out, we can do this. We'll define a class called "Lazy," which takes a block, turns it  
> into a closure, and holds onto it without immediately calling it. The first time somebody calls a  
> method, we evaluate the closure and then forward the method call on to the closure's result. 事实证明，我们可以这么做。我们定一个叫“Lazy”的类，然后使用block，把它转成闭包，然后保存它。

```ruby
class Lazy  
    def initialize(&generator)  
        @generator = generator  
    end  
  
    def method_missing(method, *args, &block)  
        evaluate.send(method, *args, &block)  
    end  
  
    def evaluate  
        @value = @generator.call unless @value  
        @value  
    end  
end  
  
def lazy(&b)  
    Lazy.new &b  
end  
```

> This basically allows us to say:  
> 你可以这样来使用：  
> lazy {value}  
> ...and get an object that _looks_ exactly like value -- except that value won't be created until the  
> first method call that touches it. It creates a transparent lazy proxy object. Observe:  
> 然后就能得到一个看起来像指定的value一样的对象—— 除了value只有在一次调用method的以后才会创建。  
> 它创建了一个透明的延迟代理对象。
### example 24

```cpp
x = lazy do  
    puts "<<< Evaluating lazy value >>>"  
    "lazy value"  
end  
  
puts "x has now been assigned"  
puts "About to call one of x's methods:"  
puts "x.size: #{x.size}"          # <--- .size triggers lazy evaluation .size会触发延迟求值  
puts "x.swapcase: #{x.swapcase}"  
```

> So now, if we define fibo using lazy instead of lambda, it should magically work with our  
> original LispyEnumerable -- which has no idea it's dealing with a lazy value! Right?  
> 现在，我们使用lazy代替lambda来创建菲波那契函数，它应该可以神奇地和我们来原的LispyEnumerable工作。  
> LispyEnumerable并不知道如何处理一个lazy value！是不是？
### example 25

```ruby
def fibo(a,b)  
    lazy { [a, fibo(b,a+b)] }  
end  
  
LispyEnumerable.new(fibo(1,1)).each do |x|  
    puts x  
    break if x.instance_of?(Lazy) || x > 200  
end  
```
### example 26

```ruby
car,cdr = fibo(1,1)  
puts "car=#{car}  cdr=#{cdr}"  
```

> Here's the problem. When we do this:  
> 问题就在这里，当我们这样赋值时：  
> x,y = z  
> ...Ruby calls z.respond_to?(to_a) to see if z is an array. If it is, it will do the multiple  
> assignment; if not, it will just assign x=z and set y=nil.  
> Ruby会调用z.respond_to?(to_a)来看看z是否能变成一个数组。如果可以，它会进行多次赋值，  
> 否则，它只会赋值 x=z 和 y = nil。  
> We want our Lazy to forward the respond_to? call to our fibo list. But it doesn't forward it,  
> because we used the method_missing to do the proxying -- and every object implements respond_to?  
> by default, so the method isn't missing! The respond_to? doesn't get forwarded; instead, out Lazy  
> says "No, I don't respond to to_a; thanks for asking." The immediate solution is to forward  
> respond_to? manually:

我们想要的Lazy来转发respond_to?，让它调用到我们的菲波那契。但是它没有做到，因为我们用method_missing  
来进行代理 -- 而每个对象默认都有respond_to?方法，所以无法触发到method_missing！respond_to?方法没有被转发，  
所以Lazy说：“我没有to_a方法，谢谢你的调用。”最快捷的办法是手动转发respond_to?方法。

```ruby
class Lazy  
    def initialize(&generator)  
        @generator = generator  
    end  
  
    def method_missing(method, *args, &block)  
        evaluate.send(method, *args, &block)  
    end  
  
    def respond_to?(method)  
        evaluate.respond_to?(method)  
    end  
  
    def evaluate  
        @value = @generator.call unless @value  
        @value  
    end  
end  
  
# And *now* our original Lispy enum can work:  
# 现在我们原来的Lispy enum就能工作了。  
  
example 27  
  
LispyEnumerable.new(fibo(1,1)).each do |x|  
    puts x  
    break if x > 200  
end  
```

> Of course, this only fixes the problem for respond_to?, and we have the same problem for every other  
> method of Object. There is a more robust solution -- frightening, but it works -- which is to undefine  
> all the methods of the Lazy when it's created, so that everything gets forwarded.  
> 当然，这只是修改了respond_to?的问题，Object的其他方法也有同样的问题。这里有一个更健壮的办法，虽然有点可怕，  
> 但是它能工作。那就是在Lazy对象创建时取消定义Lazy所有的方法，那样就都能被转发了。  
> And guess what? There's already a slick little gem that will do it:  
> 其实已经有一个gem做了这样的事：  
> [http://moonbase.rydia.net/software/lazy.rb/](https://link.jianshu.com?t=http://moonbase.rydia.net/software/lazy.rb/)  
> Read the source. It's fascinating.  
> 看看它的源码把，非常让人着迷。

---------------------------- Section 8: Wrap-Up ----------------------------  
------------------------------- 章节八：总结 -------------------------------

> 假设你有一个对象须要一个网络或者数据库的调用才能被创建，或者会一旦创建会占用大量内存。你也不知道什么 时候会用到它。使用lazy将防止它消耗资源，除非它真得需要。Hibernate使用延迟加载来阻止不必要的数据库查询， 而Hibernate做到它需要或多或少的Java对象。（不像ActiveRecord，它依赖于一个base class来做到延迟加载）  
> Ruby可以使用更少的代码做到相同的事情。  
> That's just an example. Use your imagination.  
> 这只是个示例，发挥你的想象力。  
> If you're a functional langauge geek, and enjoyed seeing Ruby play with these ideas from Lisp and  
> Haskell, you may enjoy this thread:  
> 如果你是个函数式语言的极客，并且喜欢使用Ruby的这些来自于Lisp和Haskell的特性，你也许会想加入  
> [http://redhanded.hobix.com/inspect/curryingWithArity.html](https://link.jianshu.com?t=http://redhanded.hobix.com/inspect/curryingWithArity.html)

OK, I'll stop making your brain hurt now. Hope this has been a bit enlightening! The experience  
of working it out certainly was for me.  
好了，我不再伤害你的大脑了。希望这能够给你一点启发！理解这些的经验对我非常有用。

Paul

  
  
作者：YongpingZhao  
链接：https://www.jianshu.com/p/bf4c7ba961bb  
来源：简书  
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。\
