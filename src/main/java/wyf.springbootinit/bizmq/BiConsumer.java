package springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import springbootinit.common.ErrorCode;
import springbootinit.exception.BusinessException;
import springbootinit.manager.AiManager;
import springbootinit.model.entity.Chart;
import springbootinit.service.ChartService;

import javax.annotation.Resource;


@Component
@Slf4j
public class BiConsumer {

    @Resource
    private ChartService chartService;

    private AiManager aiManager;

    @SneakyThrows
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME} , ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        log.info("reveiveMessage message = {}", message);
        if (StringUtils.isBlank(message)){
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long chartId = Long.parseLong(message);
        Chart chart = chartService.getById(chartId);
        if (chart == null){
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        channel.basicAck(deliveryTag, false);
        //修改状态
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus("running");
        boolean b = chartService.updateById(updateChart);
        if(!b){
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(),"图表状态更改失败");
            return;
        }
        //调用AI
        String result = aiManager.doChat(buildUserInput(chart));
        String[] splits = result.split("【【【【【");
        if (splits.length < 3){
            handleChartUpdateError(chart.getId(), "生成格式错误");
            return;
        }
        String genChart = splits[1];
        String genResult = splits[2];
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus("succeed");
        boolean updateResult = chartService.updateById(updateChartResult);
        if(!updateResult){
            channel.basicNack(deliveryTag, false, false);
            handleChartUpdateError(chart.getId(),"更新图表成功状态失败");
        }

        channel.basicAck(deliveryTag, false);
    }
    private String buildUserInput(Chart chart){
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        //用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求: ").append("\n");
        //拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)){
            userGoal += ". 请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据: \n").append("\n");
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

    private void handleChartUpdateError(long chartId, String execMessage){
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = chartService.updateById(updateChartResult);
        if(!updateResult){
            log.error("更新图表失败" + chartId + "," + execMessage);
        }
    }
}
