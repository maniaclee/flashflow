package com.lvbby.flashflow.core.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.lvbby.flashflow.core.IFlowActionExtension;
import com.lvbby.flashflow.core.utils.FlowUtils;
import org.yaml.snakeyaml.Yaml;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Predicate;

/**
 *
 * @author dushang.lp
 * @version $Id: FlowConfigParser.java, v 0.1 2020年03月24日 下午8:46 dushang.lp Exp $
 */
public class FlowConfigParser {

    public static FlowConfig  parseJson(String s ){
        ParserConfig config = new ParserConfig();
        config.putDeserializer(IFlowActionExtension.class, new ObjectDeserializer() {

            @Override
            public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
                /**

                 "extensions":{
                     "CreateOrderActionExt":{
                            "getTitle":"return 'groovyExtension-title' "
                        }
                 }

                 */
                Object parse = parser.parse();
                if(parse instanceof String){
                    return FlowUtils.groovyInstance((String) parse);
                }
                if(parse instanceof Map){
                    Map map = (Map) parse;
                    for (Object key : map.keySet()) {
                        String extensionName = (String) key;
                        Map<String, String> ext = (Map<String, String>) map.get(extensionName);
                    }
                }
                return (T) parse;
            }

            @Override
            public int getFastMatchToken() {
                return 0;
            }
        });
        config.putDeserializer(Predicate.class, new ObjectDeserializer() {
            @Override
            public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
                String parse = (String) parser.parse();
                return (T) new GroovyCondition(parse);
            }

            @Override
            public int getFastMatchToken() {
                return 0;
            }
        });
        return JSON.parseObject(s,FlowConfig.class, config);
    }

    public static String json2yaml(String s){
        //TODO
        return new Yaml().dump(JSON.parseObject(s));
    }

    public static FlowConfig  parseYaml(String s ){
        Yaml yaml = new Yaml();
        //TODO
        FlowConfig ret = yaml.loadAs(s, FlowConfig.class);
        return ret;
    }





}