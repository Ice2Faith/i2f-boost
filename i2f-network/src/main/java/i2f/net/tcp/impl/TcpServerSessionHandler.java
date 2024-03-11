package i2f.net.tcp.impl;

import i2f.net.tcp.TcpServer;
import i2f.net.tcp.TcpServerHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ice2Faith
 * @date 2024/3/11 16:28
 * @desc
 */
public abstract class TcpServerSessionHandler implements TcpServerHandler {
    private ConcurrentHashMap<Socket,String> clientSessionIdMap=new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Map<String,Object>> sessionStorage=new ConcurrentHashMap<>();

    public String getSessionId(Socket socket){
        return clientSessionIdMap.get(socket);
    }

    public Map<String,Object> getSession(Socket socket){
        return sessionStorage.get(getSessionId(socket));
    }

    public Map<String,Object> getSession(String sessionId){
        return sessionStorage.get(sessionId);
    }

    public Set<String> getOnlineSessionIds(){
        return new LinkedHashSet<>(clientSessionIdMap.values());
    }

    public Set<Socket> getOnlineSockets(){
        return new LinkedHashSet<>(clientSessionIdMap.keySet());
    }

    public void removeSession(String sessionId){
        Socket sock=null;
        for (Map.Entry<Socket, String> entry : clientSessionIdMap.entrySet()) {
            if(entry.getValue().equals(sessionId)){
                sock=entry.getKey();
                break;
            }
        }
        if(sock!=null){
            clientSessionIdMap.remove(sock);
        }
        sessionStorage.remove(sessionId);
    }

    @Override
    public void onClientArrive(Socket clientSocket, TcpServer tcpServer) throws Exception {
        String sessionId = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
        clientSessionIdMap.put(clientSocket,sessionId);
        sessionStorage.put(sessionId,new HashMap<>());

        onHandleClient(clientSocket,sessionId,sessionStorage.get(sessionId),tcpServer);
    }

    @Override
    public void onClientException(Socket clientSocket, Exception e, TcpServer tcpServer) throws IOException {
        if(clientSocket.isClosed()){
            String sessionId = clientSessionIdMap.get(clientSocket);
            onClientClosed(clientSocket,sessionId,
                    sessionStorage.get(sessionId),
                    e,tcpServer);
            String key = clientSessionIdMap.remove(clientSocket);
            sessionStorage.remove(key);
        }
        e.printStackTrace();
    }

    public void onHandleClient(Socket clientSocket, String sessionId, Map<String, Object> session, TcpServer tcpServer) {
        Thread thread = new Thread(() -> {
            try{
                onLoopMessage(clientSocket, sessionId, session, tcpServer);
            }catch(Exception e){
                try {
                    if(e instanceof SocketException){
                        clientSocket.close();
                    }
                    tcpServer.getHandler().onClientException(clientSocket,e,tcpServer);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }, "tcp-session-" + sessionId);
        thread.start();
    }

    public abstract void onLoopMessage(Socket clientSocket, String sessionId, Map<String, Object> session, TcpServer tcpServer) throws Exception;

    public void onClientClosed(Socket clientSocket, String sessionId, Map<String, Object> session, Exception e, TcpServer tcpServer){

    }
}
