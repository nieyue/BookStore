package com.nieyue.rabbitmq.confirmcallback;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nieyue.bean.BookOrder;
import com.nieyue.bean.DataRabbitmqDTO;
import com.nieyue.bean.FlowWater;

/**
 * 消息生产者
 * @author 聂跃
 * @date 2017年5月31日
 */
@Component 
public class Sender  implements RabbitTemplate.ConfirmCallback{
	 private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);  
	/**
	 * 不能注入，否则没回调
	 */
	 private RabbitTemplate rabbitTemplate;
	 @Resource
	 private AmqpConfig amqpConfig;
	@Autowired  
	    public Sender(RabbitTemplate rabbitTemplate) {  
	        this.rabbitTemplate = rabbitTemplate;  
	        this.rabbitTemplate.setConfirmCallback(this); 
	    } 
	
	/**
	 * 书订单
	 * @param orderRabbitmqDTO
	 */
	 public void sendBookOrder(BookOrder bookOrder) { 
	        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
	        this.rabbitTemplate.convertAndSend(amqpConfig.BOOKORDER_DIRECT_EXCHANGE, amqpConfig.BOOKORDER_DIRECT_ROUTINGKEY, bookOrder, correlationData);  
	     
	 }  
	 /**
	  * 书支付
	  * @param orderRabbitmqDTO
	  */
	 public void sendBookPayment(List<Integer> bookOrderDetailIdList) { 
		 CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		 this.rabbitTemplate.convertAndSend(amqpConfig.BOOKPAYMENT_DIRECT_EXCHANGE, amqpConfig.BOOKPAYMENT_DIRECT_ROUTINGKEY, bookOrderDetailIdList, correlationData);  
		 
	 }  
	 /**
	  * 书订单流水
	  * @param orderRabbitmqDTO
	  */
	 public void sendBookOrderFlowWater(FlowWater flowWater) { 
		 CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		 this.rabbitTemplate.convertAndSend(amqpConfig.BOOKORDERFLOWWATER_DIRECT_EXCHANGE, amqpConfig.BOOKORDERFLOWWATER_DIRECT_ROUTINGKEY, flowWater, correlationData);  
		 
	 }  
	 
	 /**
	  * web书阅读
	  * @param dataRabbitmqDTO
	  */
	 public void sendBookWebRead(DataRabbitmqDTO dataRabbitmqDTO) {  
		 CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		 this.rabbitTemplate.convertAndSend(amqpConfig.BOOKWEBREAD_DIRECT_EXCHANGE, amqpConfig.BOOKWEBREAD_DIRECT_ROUTINGKEY, dataRabbitmqDTO, correlationData);  
	 }
	 /** 回调方法 */
	 @Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
	        if (ack) {
	        	LOGGER.info("消息发送确认成功");
	        } else {
	        	LOGGER.info("消息发送确认失败:" + cause);

	        }  
		
	}

}
