package ClientModule;

import ClientModule.util.AuthManager;
import ClientModule.util.Console;
import common.exceptions.IncorrectInputInScriptException;
import common.util.Request;
import common.util.Response;
import common.util.ResponseCode;
import common.util.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Client {
    private String host;
    private int port;
    private Console console;
    private AuthManager authManager;
    private User user;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(16384);
    private SocketChannel socketChannel;
    private SocketAddress address;

    public Client(String host, int port, Console console, AuthManager authManager){
        this.host = host;
        this.port = port;
        this.console = console;
        this.authManager = authManager;
    }

    public void run() {
        try {
            socketChannel = SocketChannel.open();
            address = new InetSocketAddress("localhost", this.port);
            Request requestToServer;
            Response serverResponse = null;
            do {
                requestToServer = serverResponse != null ? console.interactiveMode(serverResponse.getResponseCode(), user) :
                        console.interactiveMode(null, user);
                if (requestToServer.isEmpty()) continue;
                makeByteBufferToRequest(requestToServer);
                socketChannel = SocketChannel.open();
                socketChannel.connect(address);
                socketChannel.write(byteBuffer);
                byteBuffer.clear();
                socketChannel.read(byteBuffer);
                byteBuffer.flip();
                serverResponse = deserialize();
                if (serverResponse.getResponseCode().equals(ResponseCode.OK) && (requestToServer.getCommandName().equals("sign_in") || requestToServer.getCommandName().equals("sign_up")))
                    user = requestToServer.getUser();
                if (serverResponse.getResponseCode().equals(ResponseCode.OK) && requestToServer.getCommandName().equals("log_out"))
                    user = null;
                System.out.print(serverResponse.getResponseBody());
                socketChannel.close();
            } while(!requestToServer.getCommandName().equals("exit"));

        } catch (IOException | ClassNotFoundException | IncorrectInputInScriptException | ClassCastException exception) {
            System.out.println("К сожалению, сервер не найден!");
            System.out.println("Давайте подождем 5 секунд пока сервер оживет!");
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            run();
        }
    }

    private void makeByteBufferToRequest(Request request) throws IOException {
        byteBuffer.clear();
        byteBuffer.put(serialize(request));
        byteBuffer.flip();
    }

    private byte[] serialize(Request request) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        byte[] buffer = byteArrayOutputStream.toByteArray();
        objectOutputStream.flush();
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        objectOutputStream.close();
        return buffer;
    }

    private Response deserialize() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteBuffer.array());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Response response = (Response) objectInputStream.readObject();
        byteArrayInputStream.close();
        objectInputStream.close();
        byteBuffer.clear();
        return response;
    }
}
