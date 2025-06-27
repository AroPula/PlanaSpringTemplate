package com.richuff;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class App {
    private static PrintWriter writer;
    private static BufferedReader reader;

    public static void main(String[] args){
        String host = "127.0.0.1";
        int port = 6379;
        try(Socket socket = new Socket(host, port)){
            //获取输入输出流
             writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            //发请求
            sendRequest("set","name","te");

            sendRequest("get","name");
            //解析响应
            Object object = HandlerResponse();
            System.out.println("object = " + object);
        }catch ( IOException e){
            throw new RuntimeException(e.getMessage());
        }finally {
            try{
                if (reader != null) reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            if (writer != null) writer.close();
        }
    }

    private static Object HandlerResponse() throws IOException {
        int prefixed = reader.read();
        if (prefixed == '+'){
            return reader.readLine();
        } else if (prefixed == '-') {
            throw new RuntimeException(reader.readLine());
        } else if (prefixed == '*'){
            return ReadBulkString();
        }else if (prefixed == ':'){
            return Long.parseLong(reader.readLine());
        } else if (prefixed == '$') {
            int len = Integer.parseInt(reader.readLine());
            if (len == -1){
                return null;
            }
            if (len == 0){
                return "";
            }
            return reader.readLine();
        }else{
            throw new RuntimeException("error");
        }
    }

    private static Object ReadBulkString() throws IOException {
        int len = Integer.parseInt(reader.readLine());
        if (len <= 0){
            return null;
        }
        List<Object> objects = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            objects.add(HandlerResponse());
        }
        return objects;
    }

    private static void sendRequest(String ...args) {
        writer.println("*"+args.length);
        for (String str:args) {
            writer.println("$"+str.getBytes(StandardCharsets.UTF_8).length);
            writer.println(str);
        }
        writer.flush();
    }
}
