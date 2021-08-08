package com.cjh.websocket.controller;

import com.cjh.websocket.listener.SubscribeListener;
import com.cjh.websocket.util.SpringUtils;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 * 使用springboot的唯一区别是要@Component声明下，而使用独立容器是由容器自己管理websocket的，但在springboot中连容器都是spring管理的。
 * 虽然@Component默认是单例模式的，但springboot还是会为每个websocket连接初始化一个bean，所以可以用一个静态set保存起来。
 */
@Component
@ServerEndpoint(value = "/prescriptionWebSocket")
public class WebSocketServer {
    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     */
    private RedisMessageListenerContainer redisMessageListenerContainer = SpringUtils.getBean(RedisMessageListenerContainer.class);

    // 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    //用来存放每个客户端对应的MyWebSocket对象,若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketServer> user = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private SubscribeListener subscribeListener;

    @OnOpen
    public void onOpen(Session session){
        System.out.println(session.getId()+" open...");
        this.session = session;
        user.add(this);
        addOnlineCount();
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        subscribeListener = new SubscribeListener();
        subscribeListener.setSession(session);
        redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic("PRESCRIPTION"));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        System.out.println(this.session.getId()+" close...");
        user.remove(this);
        subOnlineCount();
        redisMessageListenerContainer.removeMessageListener(subscribeListener);
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }


    /**
     * 连接建立成功调用的方法
     * @param message 客户端发送过来的消息
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnMessage
    public void onMessage(String message,Session session) throws IOException {
        //群发消息
        for (WebSocketServer webSocketServer : user) {
            webSocketServer.session.getBasicRemote().sendText(session.getId()+"说："+message);
            //webSocketServer.session.getBasicRemote().sendText("<img src=''/>");
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session,Throwable error){
        System.out.println(this.session.getId()+" error...");
        error.printStackTrace();
    }

    public   int getOnlineCount() {
        return onlineCount.get();
    }

    public   void addOnlineCount() {
        WebSocketServer.onlineCount.getAndIncrement();
    }

    public   void subOnlineCount() {
        WebSocketServer.onlineCount.getAndDecrement();
    }
}
