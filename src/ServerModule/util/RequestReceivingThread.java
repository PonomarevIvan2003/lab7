package ServerModule.util;

import common.util.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class RequestReceivingThread extends Thread {
    private RequestManager requestManager;
    private Request request;
    private SocketChannel socketChannel;

    public RequestReceivingThread(RequestManager requestManager, Request request, SocketChannel socketChannel) {
        this.requestManager = requestManager;
        this.request = request;
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
            socketChannel.read(byteBuffer);
            try {
                request = deserialize(byteBuffer);
                System.out.println("Получена команда '" + request.getCommandName() + "'");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public Request getRequest() {
        return request;
    }

    private Request deserialize(ByteBuffer byteBuffer) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Request request = (Request) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        byteBuffer.clear();
        return request;
    }
}
