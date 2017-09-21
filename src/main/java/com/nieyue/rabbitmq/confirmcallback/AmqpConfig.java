package com.nieyue.rabbitmq.confirmcallback;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
/**
 * 配置
 * @author 聂跃
 * @date 2017年5月31日
 */
@Configuration  
public class AmqpConfig {
	/**
	 * 书订单
	 */
	@Value("${myPugin.rabbitmq.BOOKORDER_DIRECT_EXCHANGE}")
	public  String BOOKORDER_DIRECT_EXCHANGE ;  
	@Value("${myPugin.rabbitmq.BOOKORDER_DIRECT_ROUTINGKEY}")
	public String BOOKORDER_DIRECT_ROUTINGKEY;  
	@Value("${myPugin.rabbitmq.BOOKORDER_DIRECT_QUEUE}")
	public  String BOOKORDER_DIRECT_QUEUE; 
	/**
	 * 书支付
	 */
	@Value("${myPugin.rabbitmq.BOOKPAYMENT_DIRECT_EXCHANGE}")
    public  String BOOKPAYMENT_DIRECT_EXCHANGE ;  
	@Value("${myPugin.rabbitmq.BOOKPAYMENT_DIRECT_ROUTINGKEY}")
    public String BOOKPAYMENT_DIRECT_ROUTINGKEY;  
	@Value("${myPugin.rabbitmq.BOOKPAYMENT_DIRECT_QUEUE}")
    public  String BOOKPAYMENT_DIRECT_QUEUE; 
	/**
	 *书订单流水
	 */
	@Value("${myPugin.rabbitmq.BOOKORDERFLOWWATER_DIRECT_EXCHANGE}")
	public  String BOOKORDERFLOWWATER_DIRECT_EXCHANGE ;  
	@Value("${myPugin.rabbitmq.BOOKORDERFLOWWATER_DIRECT_ROUTINGKEY}")
	public String BOOKORDERFLOWWATER_DIRECT_ROUTINGKEY;  
	@Value("${myPugin.rabbitmq.BOOKORDERFLOWWATER_DIRECT_QUEUE}")
	public  String BOOKORDERFLOWWATER_DIRECT_QUEUE; 
	
	/**
	 * web书阅读
	 */
	@Value("${myPugin.rabbitmq.BOOKWEBREAD_DIRECT_EXCHANGE}")
	public  String BOOKWEBREAD_DIRECT_EXCHANGE ;  
	@Value("${myPugin.rabbitmq.BOOKWEBREAD_DIRECT_ROUTINGKEY}")
	public String BOOKWEBREAD_DIRECT_ROUTINGKEY; 
	@Value("${myPugin.rabbitmq.BOOKWEBREAD_DIRECT_QUEUE}")
	public  String BOOKWEBREAD_DIRECT_QUEUE; 
	
	
    @Autowired
    ConnectionFactory  connectionFactory ;
    /** 因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置 */  
    @Bean  
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)  
    public RabbitTemplate rabbitTemplate() {  
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);  
        return rabbitTemplate;  
    } 
    /** 
     *书订单
     */  
    /*
     * 设置交换机类型
     */   
    @Bean  
    public DirectExchange bookOrderDirectExchange() {  
        /** 
         * DirectExchange:按照routingkey分发到指定队列 
         * TopicExchange:多关键字匹配 
         * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念 
         * HeadersExchange ：通过添加属性key-value匹配 
         */  
    	DirectExchange de = new DirectExchange(BOOKORDER_DIRECT_EXCHANGE);
        return de;
    }
    /*
     * 设置队列
     */
    @Bean  
    public Queue bookOrderDirectQueue() {  
        return new Queue(BOOKORDER_DIRECT_QUEUE);  
    }
    /*
     * 设置绑定
     */
    @Bean  
    public Binding bookOrderDirectBinding() {  
        /** 将队列绑定到交换机 */  
        return BindingBuilder.bind(bookOrderDirectQueue()).to(bookOrderDirectExchange()).with(BOOKORDER_DIRECT_ROUTINGKEY);  
    } 
    
    /** 
     *书支付
     */  
    /*
     * 设置交换机类型
     */   
    @Bean  
    public DirectExchange bookPaymentDirectExchange() {  
        /** 
         * DirectExchange:按照routingkey分发到指定队列 
         * TopicExchange:多关键字匹配 
         * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念 
         * HeadersExchange ：通过添加属性key-value匹配 
         */  
    	DirectExchange de = new DirectExchange(BOOKPAYMENT_DIRECT_EXCHANGE);
        return de;
    }
    /*
     * 设置队列
     */
    @Bean  
    public Queue bookPaymentDirectQueue() {  
        return new Queue(BOOKPAYMENT_DIRECT_QUEUE);  
    }
    /*
     * 设置绑定
     */
    @Bean  
    public Binding bookPaymentDirectBinding() {  
        /** 将队列绑定到交换机 */  
        return BindingBuilder.bind(bookPaymentDirectQueue()).to(bookPaymentDirectExchange()).with(BOOKPAYMENT_DIRECT_ROUTINGKEY);  
    } 
    
    
    /** 
     *书订单流水
     */  
    /*
     * 设置交换机类型
     */   
    @Bean  
    public DirectExchange bookOrderFlowWaterDirectExchange() {  
    	DirectExchange de = new DirectExchange(BOOKORDERFLOWWATER_DIRECT_EXCHANGE);
    	return de;
    }
    
    // 设置队列
    @Bean  
    public Queue bookOrderFlowWaterDirectQueue() {  
    	return new Queue(BOOKORDERFLOWWATER_DIRECT_QUEUE);  
    }
    
     // 设置绑定
    @Bean  
    public Binding bookOrderFlowWaterDirectBinding() {  
    	/** 将队列绑定到交换机 */  
    	return BindingBuilder.bind(bookOrderFlowWaterDirectQueue()).to(bookOrderFlowWaterDirectExchange()).with(BOOKORDERFLOWWATER_DIRECT_ROUTINGKEY);  
    } 
    
    
    /** 
     *书web阅读
     */  
    /*
     * 设置交换机类型
     */   
    @Bean  
    public DirectExchange bookWebReadDirectExchange() {  
    	DirectExchange de = new DirectExchange(BOOKWEBREAD_DIRECT_EXCHANGE);
    	return de;
    }
    
    // 设置队列
    @Bean  
    public Queue bookWebReadDirectQueue() {  
    	return new Queue(BOOKWEBREAD_DIRECT_QUEUE);  
    }
    
    // 设置绑定
    @Bean  
    public Binding bookWebReadDirectBinding() {  
    	/** 将队列绑定到交换机 */  
    	return BindingBuilder.bind(bookWebReadDirectQueue()).to(bookWebReadDirectExchange()).with(BOOKWEBREAD_DIRECT_ROUTINGKEY);  
    } 

}
