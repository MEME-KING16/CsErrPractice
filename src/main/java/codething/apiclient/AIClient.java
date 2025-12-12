package main.java.codething.apiclient;

import com.google.gson.*;
import java.net.*;
import java.io.*;

public class AIClient {

    private static final Gson gson = new Gson();

    public static String sendMessage(String prompt) {
        try {
            URL url = new URL("http://ai.webseb.ca/v1/chat/completions");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            JsonObject body = new JsonObject();
            body.addProperty("model", "lmstudio-community/DeepSeek-R1-Distill-Qwen-7B-GGUF");

            JsonArray messages = new JsonArray();
            JsonObject usr = new JsonObject();
            usr.addProperty("role", "user");
            usr.addProperty("content", prompt);
            messages.add(usr);

            body.add("messages", messages);
            body.addProperty("temperature", 0.7);
            body.addProperty("max_tokens", 1024);
            body.addProperty("stream", false);

            String json = gson.toJson(body);
            con.getOutputStream().write(json.getBytes());

            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null)
                sb.append(line);

            reader.close();

            JsonObject resp = JsonParser.parseString(sb.toString()).getAsJsonObject();
            String reply = resp.getAsJsonArray("choices")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content").getAsString();

            return extractJavaCode(reply);

        } catch (Exception e) {
            e.printStackTrace();
            return "AI error";
        }
    }


    private static String stripComments(String code) {
        if (code == null) return "";

        code = code.replaceAll("/\\*.*?\\*/", "");

        code = code.replaceAll("//.*", "");

        return code;
    }


    private static String extractJavaCode(String text) {
        if (text == null) return "";

        int start = text.indexOf("```java");
        int end = text.indexOf("```", start + 7);

        if (start != -1 && end != -1) {
            String code = text.substring(start + 7, end);
            return stripComments(code).strip(); 
        }

        return text.strip();
    }
}