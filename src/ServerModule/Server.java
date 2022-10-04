package ServerModule;

import ServerModule.util.RequestManager;
import ServerModule.util.RequestReceivingThread;
import common.util.Request;
import common.util.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class Server {
    private int port;
    private RequestManager requestManager;
    private InetSocketAddress address;
    private ServerSocketChannel serverSocketChannel;
    private Scanner scanner;

    private ForkJoinPool forkJoinPool = new ForkJoinPool(5);

    private Request request;
    private Response response;

    public Server(int port, RequestManager requestManager) throws IOException {
        this.port = port;
        this.requestManager = requestManager;
        checkInput();
    }

    private Response executeRequest(Request request) {
        return requestManager.manage(request);
    }

    public void run() throws IOException {
        System.out.println("Сервер запущен.");
        address = new InetSocketAddress(port);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);
        while (true) {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept();
                RequestReceivingThread readThread = new RequestReceivingThread(requestManager, request, socketChannel);
                readThread.start();
                try {
                    readThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (this) {
                    request = readThread.getRequest();
                }

                Runnable manageRequest = () -> {
                    try {
                        response = requestManager.manage(request);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                };
                Thread manageThread = new Thread(manageRequest);
                manageThread.start();
                try {
                    manageThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                SendResponse sendResponse = new SendResponse(response, socketChannel);
                forkJoinPool.invoke(sendResponse);
                forkJoinPool.execute(sendResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод, запускающий новый поток для реализациии особых серверных комманд.
     */
    private void checkInput() {
        scanner = new Scanner(System.in);
        Runnable userInput = () -> {
            try {
                while (true) {
                    String[] userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                    if (userCommand[0].equals("exit")) {
                        System.out.println("Сервер заканчивает работу!");
                        System.exit(0);
                    }

                }
            } catch (Exception ignored) {
            }
        };
        Thread thread = new Thread(userInput);
        thread.start();
    }

    static class SendResponse extends RecursiveAction {

        Response response;
        SocketChannel socketChannel;

        public SendResponse(Response response, SocketChannel socketChannel) {
            this.response = response;
            this.socketChannel = socketChannel;
        }

        @Override
        protected void compute() {
            try {
                sendResponse(response);
            } catch (IOException exception) {
                System.out.println("Произошла ошибка при отправке ответа!");
            }
        }

        private void sendResponse(Response response) throws IOException {
            ByteBuffer byteBuffer = ByteBuffer.allocate(65536);
            byteBuffer.put(serialize(response));
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        private byte[] serialize(Response response) throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            byte[] buffer = byteArrayOutputStream.toByteArray();
            objectOutputStream.flush();
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            objectOutputStream.close();
            return buffer;
        }
    }
}

