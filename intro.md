# flashflow是什么
# quick start
## 创建流程模块
已一个简单的订单系统为例，假设下单流程分为「创建订单」和「订单处理」两个流程
简单定义两个流程模块：
```java
private IFlowAction startAction = new IFlowAction() {

        @Override
        public void invoke(FlowContext context) {
            Object simpleValue = context.getValue("simpleValue");
            System.out.println("start:"+simpleValue);
        }
    };

    private IFlowAction endAction = new IFlowAction() {

        @Override
        public void invoke(FlowContext context) {
            System.out.println("end");
        }
    };
```
## 快速流程调用
创单流程就是将两个流程串在一起调用
```java
Flow.execSimple(new OrderContext(),startAction, endAction);
```
## 模块自定义属性
每个模块都是抽象的独立功能模块，可以拥有自定义属性
如startAction中从上下文中获取了一个自定义属性「simpleValue」
```java
Object simpleValue = context.getValue("simpleValue");
System.out.println("start:"+simpleValue);
```
自定义属性只需在上下文里对属性进行设计即可
```java
 /** 构建上下文 */
OrderContext context = new OrderContext();
context.putValue("simpleValue", "this is a test");//传入扩展属性，startAction会获取

Flow.execSimple(context,startAction, endAction);
```
## 复杂业务流程需要的特性
以上是最简单的流程使用方式，实际上的业务流程要远比这复杂的多，如：
* 模块之间的交互
* 各种业务模块之间的排列方式，及流程编排
* 通用模块如果要做到可复用，必须提供扩展及业务自定义的能力，flashflow提供「模块参数」及「模块扩展」的方式
* 业务配置：复杂的业务流程通常需要提供配置方式，如API定义工作流、工作流配置文件、业务配置的可视化运营平台等
# 模块
业务流程要做到可扩展、可复用、可视化、可编排，一个重要的前提就是模块化。
## 定义
flashflow的模块定位为一个接口：com.lvbby.flashflow.core.IFlowAction
```java
public interface IFlowAction<IContext extends FlowContext> {
    void invoke(IContext context) throws Exception;
}
```
每个模块需要做的事情就是基于上下文「FlowContext」实现invoke方法里的自定义业务逻辑
上下文不用多说，IFlowAction的上下文类型可以自行定义
## 模块id
每个IFlowAction都具有一个id，这个id是在流程引擎的唯一标识，类似spring的唯一beanName
> 在流程编配时对action的复用便是基于actionId

actionId默认的取值方式如下：
1. IFlowAction上标记注解@FlowAction，通过id属性指明
2. IFlowAction 类名的lowerCamel模式，同spring bean的命名方式
3. 在spring环境中便是beanName

# 流程编排
## 1. API方式
API方式的流程编排基于树形数据结构的「FlowNode」，每个模块支持condition
如：
```java
@Test
public void nodeDemo() {

    /** 1. 定义流程编排*/
    FlowNode pipeline = FlowNode.node(initAction)
            .next(node(processAction) );

    /** 2. 定义场景：场景名称+流程编排 */
    FlowScript.of("example", pipeline).register();

    /** 启动流程：按照场景名称进行触发 */
    Flow.exec(new OrderContext("example"));
}
```
### condition
条件触发
```java
@Test
    public void conditionDemo() {

        /** 1. 定义流程编排*/
        FlowNode pipeline = FlowNode.node(initAction)
                .next(node(processAction)
                        .when(ctx -> FlowUtils.equals(ctx.getValueString("props"), "test"))
                );

        /** 2. 定义场景：流程+扩展点 */
        FlowScript.of("pipeline", pipeline)
                .register()
        ;

        /** 启动流程 */
        Flow.exec(new OrderContext("pipeline").putValue("props", "test"));
        Flow.exec(new OrderContext("pipeline").putValue("props", "not match property"));
    }
```
## 2. 配置文件
flashflow提供json配置文件的方式进行流程定义，json的格式定义为FlowConfig
```java
public class FlowConfig implements Serializable {

    /***
     * 流程script
     */
    private List<FlowScript> scripts;
    /***
     * 全局prop
     */
    private JSONObject props;

}
```
已mybatis生成器为例：
> 这里不用关心每个具体action是做什么，主要是示意json配置文件格式
```json
{
  "scripts":[
    {
      "code":"mybatisTest",
      "pipeline":{
        "actionId":"datasourceAction",
        "children":[
          {"actionId":"datasourceAction"},
          {"actionId":"dbTablesAction"},
          {"actionId":"dbBeanGenAction"},
          {"actionId":"javaSrcWriterAction"},
          {"actionId":"mybatisDaoGenAction"},
          {"actionId":"javaSrcWriterAction"},
          {"actionId":"mybatisMapperXmlGenAction"},
          {"actionId":"fileWriterAction"}
        ]
      }
    }
  ],
 "props":{
   "mybatisGenerator": {
     "mapperPackage":"com.lvbby.test.mapper",
     "mapperXmlDir":"/xxxx/coderflow/src/test/resousrces/_gen"
   },
   "java.src": "/xxxx/coderflow/src/test/java",
   "db.tables": ["article"],
   "db.bean.package": "com.lvbby.test",
   "db.jdbcUrl": "jdbc:mysql://localhost:3306/xx",
   "db.jdbcUser": "test",
   "db.jdbcPwd": "xxxx"
 }
}
```
## 动态属性
属性通常是静态数据类型，如String、int等，某些场景需要动态特性类来进行扩展，如函数扩展；
具体而言，加入一个mybatis生成器要支持动态指定生成DO的名称，则需要支持用户基于函数来根据表名来动态定制，
所以flashflow的属性支持Function接口，使用如下:
```java
@FlowAction
public class DbBeanGenAction implements IFlowAction {

    /***
     * 定义动态属性：生成bean的name命名方式
     */
    @FlowProp
    public static final FlowKey<Function<String,String>> beanNameFunc = new FlowKey<>("db.bean.nameFunc");

    public void invoke(FlowContext context) throws Exception {
        SqlTable table = FlowHelper.getValueOrProp(CoderFlowKeys.dbTable);

        String beanName = table.getName();
        //bean名称自定义
        Function<String, String> beanNameFunction = FlowHelper.getValueOrProp(beanNameFunc);
        if(beanNameFunction!=null){
            beanName = beanNameFunction.apply(beanName);
        }
        System.out.println(beanName);
    }
}
```
代码详情可参考：com.lvbby.coderflow.compont.DbBeanGenAction
json配置文件中可以使用groovy脚本来实现Function扩展的逻辑，自定义
```json
{
  "scripts":[
    {
      "code":"mybatisTest",
      "pipeline":{
        "actionId":"dbBeanGenAction"
      },
      "props":{
        "db.bean.nameFunc": "src+'DO'"
      }
    }
  ]
}
```
上面配置文件定义了一个流程名称为mybatisTest，流程编排使用了dbBeanGenAction模块，
dbBeanGenAction的动态属性`db.bean.nameFunc`内容为一段groovy脚本，逻辑是「生成DO的类名为表名+DO后缀」
需要说明的是：
> groovy脚本里的入参名固定为src

实现原理是将groovy脚本动态生成Function的代理
# 进阶使用
