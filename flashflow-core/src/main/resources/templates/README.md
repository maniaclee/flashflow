# 配置文件
```json
{
  "props":{
   <%for( p in src.props){%>
    "${p.key}": ${@p.defaultValueJson()},
  <% }%>
  <%for( a in src.actions){%>
       <%for( ap in a.props){%>
    "${ap.key}": ${@ap.defaultValueJson()}, 
       <% }%>
  <% }%>
  }
}
```
# 全局属性
 <%for( p in src.props){%>
* ${p.key}: ${p.desc} ***[${p.typeName}]***
<% }%>
# 模块列表
<%for( a in src.actions){%>
## ${a.name}
类型: ${@a.actionClass.getName()} 
<% if(utils.isNotEmpty(a.props)) {%>
属性:
| key  | 描述 | 类型 |
| ---- | ---- | ---- |
        <%for( p in a.props){%>
| ${p.key} | ${p.desc}  | ${p.typeName}  |
        <% }%>
<% }%>

<% }%>