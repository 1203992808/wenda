package com.example.service;


import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService  implements InitializingBean{
    private static final Logger logger= LoggerFactory.getLogger(SensitiveService.class);
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader  reader=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(reader);
            String lineText;
            while((lineText=bufferedReader.readLine())!=null){
                lineText = lineText.trim();
                addWord(lineText);
            }
            reader.close();

        }catch(Exception e){
            logger.error("读取敏感词失败"+e.getMessage());
        }

    }
    private void addWord(String lineText){
        TrieNode tempNode=rootNode;
        for (int i = 0; i <lineText.length() ; i++) {
            Character  c=lineText.charAt(i);
            if(isSymbol(c)){
                continue;
            }
            TrieNode node=tempNode.getSubNode(c);
            if(node==null){
                node=new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;
            if(i==lineText.length()-1){
                tempNode.setKeyWordEnd(true);
            }

        }

    }


    private class TrieNode{
        private boolean  end=false;
        private Map<Character,TrieNode> subNodes=new HashMap<Character,TrieNode>();
        public void  addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
        public boolean isKeyWordEnd(){
            return end;
        }
        public  void setKeyWordEnd(boolean end){
            this.end=end;

        }
    }
    public String filter(String text){
        if(StringUtils.isEmpty(text)){
            return text;

        }
        StringBuilder result=new StringBuilder();
        String replacement="***";
        TrieNode tempNode=rootNode;
        int begin=0;
        int position=0;
        while(position<text.length()){
            char c=text.charAt(position);
            if(isSymbol(c)){
                if(tempNode==rootNode){
                    ++begin;
                    result.append(c);
                }
                ++position;
                continue;
            }
            tempNode=tempNode.getSubNode(c);

            if(tempNode==null){
                result.append(text.charAt(begin));
                position=begin+1;
                begin=position;
                tempNode=rootNode;
            }else if(tempNode.isKeyWordEnd()){
                result.append(replacement);
                position=position+1;
                begin=position;
                tempNode=rootNode;


            }else{
                ++position;
            }

        }
        result.append(text.substring(begin));
        return result.toString();
    }
    private  TrieNode rootNode=new TrieNode();
    private boolean isSymbol(char c){
        int ic=(int)c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic<0x2E80||ic>0x9FFF);
    }




}
