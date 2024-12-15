package springbootinit.manager;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;
import springbootinit.model.domain.ChatRequest;
import springbootinit.model.domain.ChatResponse;

@Service
public class AiManager {
    public String doChat(String message){
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";//这里看官方文档
        String ApiKey = "sk-6ee48df68fde45d3b8ff9af4cc6659b1";//这里换成你自己的ApiKey
        StringBuilder userInput = new StringBuilder();
        String sample = "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                " 2号,20\n" +
                " 3号,30";
        userInput.append("context:\n").append("你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
                "分析需求：\n" +
                "{数据分析的需求或者目标}\n" +
                "原始数据：\n" +
                "{csv格式的原始数据，用,作为分隔符}\n" +
                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
                "【【【【【\n" +
                "{前端 Echarts V5 的 option 配置对象json代码，合理地将数据进行可视化，不要生成任何多余\n" +
                "【【【【【\n" +
                "{明确的数据分析结论、越详细越好，不要生成多余的注释}\n" +
                "例如:\n" +
                "当我输入:\n" + sample +
                "\n你应当输出:\n" +
                "【【【【【\n" +
                "{\n" +
                " }\n" +
                "    \"title\": {\n" +
                "        \"text\": \"网站用户增长情况\",\n" +
                "        \"subtext\": \"\"\n" +
                "    },\n" +
                "    \"tooltip\": {\n" +
                "        \"trigger\": \"axis\",\n" +
                "        \"axisPointer\": {\n" +
                "            \"type\": \"shadow\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"legend\": {\n" +
                "        \"data\": [\"用户数\"]\n" +
                "    },\n" +
                "    \"xAxis\": {\n" +
                "        \"data\": [\"1号\", \"2号\", \"3号\"]\n" +
                "    },\n" +
                "    \"yAxis\": {},\n" +
                "    \"series\": [{\n" +
                "        \"name\": \"用户数\",\n" +
                "        \"type\": \"bar\",\n" +
                "        \"data\": [10, 20, 30]\n" +
                "    }]\n" +
                "【【【【【\n" +
                "根据数据分析可得，该网站用户数量逐日增长，时间越长，用户数量增长越多。\n");
        message = userInput.toString() + message;
        ChatRequest chatRequest = new ChatRequest(message);
        String json = JSONUtil.toJsonStr(chatRequest);
        String result = HttpRequest.post(url)
                .header("Authorization","Bearer "+ ApiKey)
                .header("Content-Type","application/json")
                .body(json)
                .execute().body();
        ChatResponse chatResponse = JSONUtil.toBean(result, ChatResponse.class);
        return chatResponse.getOutput().text;
    }
}
