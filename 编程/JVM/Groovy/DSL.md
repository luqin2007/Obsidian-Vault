>[!note] DSL：领域特定语言 Domain-Specific Languages

Groovy 可以快速方便的创建 DSL
# 链式调用

- 允许省略顶级调用的括号
- 允许省略链式调用的 .
- 无参方法需要括号

  ```groovy
  // turn(left).then(right)
  turn left then right
  
  // take(2.pills).of(chloroquinine).after(6.hours)
  take 2.pills of chloroquinine after 6.hours
  
  // paint(wall).with(red, green).and(yellow)
  paint wall with red, green and yellow
  
  // check(that: margarita).tastes(good)
  check that: margarita tastes good
  
  // give({}).when({}).then({})
  given {} when {} then {}
  
  // select(all).unique().from(names)
  select all unique() from names
  
  // take(3).cookies, take(3).getCookies()
  take 3 cookies
  ```

- 编写

  ```groovy
  show = { println it }
  
  square_root = { Math.sqrt(it) }
  
  def please(action) {
      [
              the: {what -> [
                      of: { n -> action(what(n)) }
              ]}
      ]
  }
  
  please(show).the(square_root).of(100) // 10.0
  
  please show the square_root of 100 // 10.0
  ```

- 创建：以 Guava 的 Splitter 为例

```groovy
import com.google.common.base.CharMatcher
import com.google.common.base.Splitter
def splite(String string) { [
        on: { String sep -> [
                trimming: { trimChar ->
                    Splitter.on(sep).trimResults(CharMatcher.is(trimChar as char)).split(string).toList()
                }
        ]}
] }
println splite '__Hello_ _____world__' on ' ' trimming '_'
```
## 操作符重载

只需要重写某些方法即可

| 操作符               | 对应方法                      |
| -------------------- | ----------------------------- |
| a+b                  | a.plus(b)                     |
| a-b                  | a.minus(b)                    |
| `a*b`                | a.multiply(b)                 |
| `a**b`               | a.power(b)                    |
| a/b                  | a.div(b)                      |
| a%b                  | a.mod(b)                      |
| a\|b                 | a.or(b)                       |
| a&b                  | a.and(b)                      |
| a^b                  | a.xor(b)                      |
| a++/++a              | a.next()                      |
| a--/--a              | a.previous()                  |
| `a[b]`               | a.getAt(b)                    |
| `a[b]=c`             | a.putAt(b, c)                 |
| a<<b                 | a.leftShift(b)                |
| a>>b                 | a.rightShift(b)               |
| a>>>b                | a.rightShiftUnsigned(b)       |
| switch(a) {case(b):} | b.isCase(a)                   |
| if(a)                | a.asBoolean()                 |
| ~a                   | a.bitwiseNegate()             |
| -a                   | a.negative()                  |
| +a                   | a.positive()                  |
| a as b               | a.asType(b)                   |
| a == b, a != b       | a.equals(b), !equals          |
| a <=> b              | a.compareTo(b)                |
| a>b, a>=b, a<b, a<=b | a.compareTo(b) > 0/>=0/<0/<=0 |
# 配置编译器

使用 CompilerConfiguration 对 groovyc 或 GroovyShell 的编译进行配置

```groovy
def config = new CompilerConfiguration()
config.addCompilationCustomizers(...)
```
## 导入定制

使用 ImportCustomizer 导入类、静态导入、别名等

```groovy
def icz = new ImportCustomizer()
// import java.lang.String as Str
icz.addImport('Str', 'java.lang.String')
// import java.util.concurrent.atomic.AtomicInteger
icz.addImports('java.util.concurrent.atomic.AtomicInteger')
// import static java.lang.Math.PI
icz.addStaticImport('java.lang.Math', 'PI')
// import static java.lang.Math.PI as pi
icz.addStaticImport('pi', 'java.lang.Math', 'PI')
// import java.util.concurrent.*
icz.addStarImports('java.util.concurrent')
// import static java.lang.Math.*
icz.addStaticStars('java.lang.Math')
```
## AST转换定制

使用 ASTTransformationCustomizer 定制 AST 转换器的对象
## AST转换安全性定制

SecureASTCustomizer

```groovy
import org.codehaus.groovy.control.customizers.SecureASTCustomizer
import static org.codehaus.groovy.syntax.Types.* 

def scz = new SecureASTCustomizer()
scz.with {
    closuresAllowed = false // user will not be able to write closures
    methodDefinitionAllowed = false // user will not be able to define methods
    importsWhitelist = [] // empty whitelist means imports are disallowed
    staticImportsWhitelist = [] // same for static imports
    staticStarImportsWhitelist = ['java.lang.Math'] // only java.lang.Math is allowed
    // the list of tokens the user can find
    // constants are defined in org.codehaus.groovy.syntax.Types
    tokensWhitelist = [ 
            PLUS,
            MINUS,
            MULTIPLY,
            DIVIDE,
            MOD,
            POWER,
            PLUS_PLUS,
            MINUS_MINUS,
            COMPARE_EQUAL,
            COMPARE_NOT_EQUAL,
            COMPARE_LESS_THAN,
            COMPARE_LESS_THAN_EQUAL,
            COMPARE_GREATER_THAN,
            COMPARE_GREATER_THAN_EQUAL,
    ].asImmutable()
    // limit the types of constants that a user can define to number types only
    constantTypesClassesWhiteList = [ 
            Integer,
            Float,
            Long,
            Double,
            BigDecimal,
            Integer.TYPE,
            Long.TYPE,
            Float.TYPE,
            Double.TYPE
    ].asImmutable()
    // method calls are only allowed if the receiver is of one of those types
    // be careful, it's not a runtime type!
    receiversClassesWhiteList = [ 
            Math,
            Integer,
            Float,
            Double,
            Long,
            BigDecimal
    ].asImmutable()
}
```
## 源代码定制

用于其他定制器的过滤器

```groovy
import org.codehaus.groovy.control.customizers.SourceAwareCustomizer
import org.codehaus.groovy.control.customizers.ImportCustomizer

def delegate = new ImportCustomizer()
def sac = new SourceAwareCustomizer(delegate)

// the customizer will only be applied to classes contained in a file name ending with 'Bean'
sac.baseNameValidator = { baseName ->
    baseName.endsWith 'Bean'
}

// the customizer will only be applied to files which extension is '.spec'
sac.extensionValidator = { ext -> ext == 'spec' }

// source unit validation
// allow compilation only if the file contains at most 1 class
sac.sourceUnitValidator = { SourceUnit sourceUnit -> sourceUnit.AST.classes.size() == 1 }

// class validation
// the customizer will only be applied to classes ending with 'Bean'
sac.classValidator = { ClassNode cn -> cn.endsWith('Bean') }
```
## 定制器构建器

```groovy
import org.codehaus.groovy.control.CompilerConfiguration
import static org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder.withConfig 

def conf = new CompilerConfiguration()
withConfig(conf) {
    imports { // imports customizer
        normal 'my.package.MyClass' // a normal import
        alias 'AI', 'java.util.concurrent.atomic.AtomicInteger' // an aliased import
        star 'java.util.concurrent' // star imports
        staticMember 'java.lang.Math', 'PI' // static import
        staticMember 'pi', 'java.lang.Math', 'PI' // aliased static import
    }
    ast(Log)
    secureAst {
       closuresAllowed = false
       methodDefinitionAllowed = false
   }
    source(extension: 'sgroovy') {
        ast(CompileStatic) 
    }
    inline(phase:'CONVERSION') { source, context, classNode ->  
        println "visiting $classNode"                           
    }
}
```
## 脚本标志

```groovy
withConfig(configuration) { 
   ast(groovy.transform.CompileStatic)
}
// groovyc -configscript
```
# Builder
- MarkupBuilder：[Creating Xml - MarkupBuilder](https://groovy-lang.org/dsls.html#_markupbuilder).
- StreamingMarkupBuilder：[Creating Xml - StreamingMarkupBuilder](https://groovy-lang.org/dsls.html#_streamingmarkupbuilder).
- SaxBuilder

```groovy
class LogHandler extends org.xml.sax.helpers.DefaultHandler {

    String log = ''

    void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) {
        log += "Start Element: $localName, "
    }

    void endElement(String uri, String localName, String qName) {
        log += "End Element: $localName, "
    }
}

def handler = new LogHandler()
def builder = new groovy.xml.SAXBuilder(handler)

builder.root() {
    helloWorld()
}
```

- StaxBuilder

```groovy
def factory = javax.xml.stream.XMLOutputFactory.newInstance()
def writer = new StringWriter()
def builder = new groovy.xml.StaxBuilder(factory.createXMLStreamWriter(writer))

builder.root(attribute:1) {
    elem1('hello')
    elem2('world')
}

assert writer.toString() == '<?xml version="1.0" ?><root attribute="1"><elem1>hello</elem1><elem2>world</elem2></root>'
An external library such as Jettison can be used as follows:

@Grab('org.codehaus.jettison:jettison:1.3.3')
@GrabExclude('stax:stax-api') // part of Java 6 and later
import org.codehaus.jettison.mapped.*

def writer = new StringWriter()
def mappedWriter = new MappedXMLStreamWriter(new MappedNamespaceConvention(), writer)
def builder = new groovy.xml.StaxBuilder(mappedWriter)

builder.root(attribute:1) {
     elem1('hello')
     elem2('world')
}

assert writer.toString() == '{"root":{"@attribute":"1","elem1":"hello","elem2":"world"}}'
```

-  DOMBuilder

```groovy
String recordsXML = '''
    <records>
      <car name='HSV Maloo' make='Holden' year='2006'>
        <country>Australia</country>
        <record type='speed'>Production Pickup Truck with speed of 271kph</record>
      </car>
      <car name='P50' make='Peel' year='1962'>
        <country>Isle of Man</country>
        <record type='size'>Smallest Street-Legal Car at 99cm wide and 59 kg in weight</record>
      </car>
      <car name='Royale' make='Bugatti' year='1931'>
        <country>France</country>
        <record type='price'>Most Valuable Car at $15 million</record>
      </car>
    </records>'''
def reader = new StringReader(recordsXML)
def doc = groovy.xml.DOMBuilder.parse(reader)
And then processed further e.g. by using DOMCategory:

def records = doc.documentElement
use(groovy.xml.dom.DOMCategory) {
    assert records.car.size() == 3
}
```

- NodeBuilder

```groovy
def nodeBuilder = new NodeBuilder()
def userlist = nodeBuilder.userlist {
    user(id: '1', firstname: 'John', lastname: 'Smith') {
        address(type: 'home', street: '1 Main St.', city: 'Springfield', state: 'MA', zip: '12345')
        address(type: 'work', street: '2 South St.', city: 'Boston', state: 'MA', zip: '98765')
    }
    user(id: '2', firstname: 'Alice', lastname: 'Doe')
}
```

- JsonBuilder

```groovy
String carRecords = '''
    {
        "records": {
        "car": {
            "name": "HSV Maloo",
            "make": "Holden",
            "year": 2006,
            "country": "Australia",
            "record": {
              "type": "speed",
              "description": "production pickup truck with speed of 271kph"
            }
          }
      }
    }
'''
JsonBuilder builder = new JsonBuilder()
builder.records {
  car {
        name 'HSV Maloo'
        make 'Holden'
        year 2006
        country 'Australia'
        record {
            type 'speed'
            description 'production pickup truck with speed of 271kph'
        }
  }
}
String json = JsonOutput.prettyPrint(builder.toString())
```

```groovy
import groovy.json.*

def generator = new JsonGenerator.Options()
        .excludeNulls()
        .excludeFieldsByName('make', 'country', 'record')
        .excludeFieldsByType(Number)
        .addConverter(URL) { url -> "http://groovy-lang.org" }
        .build()

JsonBuilder builder = new JsonBuilder(generator)
builder.records {
  car {
        name 'HSV Maloo'
        make 'Holden'
        year 2006
        country 'Australia'
        homepage new URL('http://example.org')
        record {
            type 'speed'
            description 'production pickup truck with speed of 271kph'
        }
  }
}

assert builder.toString() == '{"records":{"car":{"name":"HSV Maloo","homepage":"http://groovy-lang.org"}}}'
```

- StreamingJsonBuilder

```groovy
String carRecords = '''
    {
        "records": {
        "car": {
            "name": "HSV Maloo",
            "make": "Holden",
            "year": 2006,
            "country": "Australia",
            "record": {
              "type": "speed",
              "description": "production pickup truck with speed of 271kph"
            }
          }
      }
    }
'''
StringWriter writer = new StringWriter()
StreamingJsonBuilder builder = new StreamingJsonBuilder(writer)
builder.records {
  car {
        name 'HSV Maloo'
        make 'Holden'
        year 2006
        country 'Australia'
        record {
            type 'speed'
            description 'production pickup truck with speed of 271kph'
        }
  }
}
String json = JsonOutput.prettyPrint(writer.toString())
```

```groovy
def generator = new JsonGenerator.Options()
        .excludeNulls()
        .excludeFieldsByName('make', 'country', 'record')
        .excludeFieldsByType(Number)
        .addConverter(URL) { url -> "http://groovy-lang.org" }
        .build()

StringWriter writer = new StringWriter()
StreamingJsonBuilder builder = new StreamingJsonBuilder(writer, generator)

builder.records {
  car {
        name 'HSV Maloo'
        make 'Holden'
        year 2006
        country 'Australia'
        homepage new URL('http://example.org')
        record {
            type 'speed'
            description 'production pickup truck with speed of 271kph'
        }
  }
}

assert writer.toString() == '{"records":{"car":{"name":"HSV Maloo","homepage":"http://groovy-lang.org"}}}'
```

- SwingBuilder

```groovy
import groovy.swing.SwingBuilder
import java.awt.BorderLayout as BL

count = 0
new SwingBuilder().edt {
  frame(title: 'Frame', size: [300, 300], show: true) {
    borderLayout()
    textlabel = label(text: 'Click the button!', constraints: BL.NORTH)
    button(text:'Click Me',
         actionPerformed: {count++; textlabel.text = "Clicked ${count} time(s)."; println "clicked"}, constraints:BL.SOUTH)
  }
}
```

- AntBuilder

```groovy
def ant = new AntBuilder()          
ant.echo('hello from Ant!') 

def ant = new AntBuilder()
ant.zip(destfile: 'sources.zip', basedir: 'src')
```
