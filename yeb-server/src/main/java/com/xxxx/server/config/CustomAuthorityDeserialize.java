package com.xxxx.server.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @description: 自定义权限json序列化
 * @author: 吉祥
 * @created: 2021/11/08 20:58
 */
public class CustomAuthorityDeserialize extends JsonDeserializer {


    @Override   //反序列化方法   解析json
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        //获取json
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        //获取json节点
        JsonNode jsonNode = mapper.readTree(jsonParser);
        Iterator<JsonNode> elements = jsonNode.elements();  //迭代器
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        if (elements.hasNext()){
            JsonNode next = elements.next();
            //获取authority节点
            JsonNode authority = next.get("authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        }
        return grantedAuthorities;
    }
}

