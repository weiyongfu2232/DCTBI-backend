package com.wyf.springbootinit.manager;

import org.apache.logging.log4j.message.Message;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class AiManagerTest {
    @Resource
    private AiManager aiManager;
    @Test
    void doChat() {
//        StringBuilder input = new StringBuilder();
//        String sample = "分析需求：\n" +
//                "分析网站用户的增长情况\n" +
//                "原始数据：\n" +
//                "日期,用户数\n" +
//                "1号,10\n" +
//                " 2号,20\n" +
//                " 3号,30";
//        input.append("context:\n").append("你是一个数据分析师和前端开发专家，接下来我会按照以下固定格式给你提供内容：\n" +
//                "分析需求：\n" +
//                "{数据分析的需求或者目标}\n" +
//                "原始数据：\n" +
//                "{csv格式的原始数据，用,作为分隔符}\n" +
//                "请根据这两部分内容，按照以下指定格式生成内容（此外不要输出任何多余的开头、结尾、注释）\n" +
//                "【【【【【\n" +
//                "{前端 Echarts V5 的 option 配置对象js代码，合理地将数据进行可视化，不要生成任何多余\n" +
//                "【【【【【\n" +
//                "{明确的数据分析结论、越详细越好，不要生成多余的注释}\n" +
//                "例如:\n" +
//                "当我输入:\n" + sample +
//                "\n你应当输出:\n" +
//                "【【【【【\n" +
//                "{\n" +
//                " }\n" +
//                "    title: {\n" +
//                "        text: '网站用户增长情况',\n" +
//                "        subtext: ''\n" +
//                "    },\n" +
//                "    tooltip: {\n" +
//                "        trigger: 'axis',\n" +
//                "        axisPointer: {\n" +
//                "            type: 'shadow'\n" +
//                "        }\n" +
//                "    },\n" +
//                "    legend: {\n" +
//                "        data: ['用户数']\n" +
//                "    },\n" +
//                "    xAxis: {\n" +
//                "        data: ['1号', '2号', '3号']\n" +
//                "    },\n" +
//                "    yAxis: {},\n" +
//                "    series: [{\n" +
//                "        name: '用户数',\n" +
//                "        type: 'bar',\n" +
//                "        data: [10, 20, 30]\n" +
//                "    }]\n" +
//                "【【【【【\n" +
//                "根据数据分析可得，该网站用户数量逐日增长，时间越长，用户数量增长越多。\n");
        String message = "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,30\n" +
                " 2号,20\n" +
                " 3号,10";
//        input.append("question:\n").append(message);

//        System.out.println(message);
        String ans = aiManager.doChat(message);
        System.out.println(ans);
    }
}