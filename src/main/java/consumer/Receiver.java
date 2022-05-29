package consumer;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class Receiver {

    private static final String EXCHANGER_NAME = "Test";
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите тему для получения сообщений: set +ваша тема+ ");
            String msg = scanner.nextLine();
            String[] temp = msg.split(" ", 2);
            String routingKey = temp[1];
            channel.queueBind(queueName, EXCHANGER_NAME, routingKey);
            System.out.println(" [*] Waiting for messages");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };

            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
            });
        }
    }
}
