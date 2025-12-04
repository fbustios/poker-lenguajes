package org.poker.connection.messages;

import java.io.*;
import java.util.Map;
import java.util.Optional;

public record MessageHandlerText(DataInputStream in, OutputStream out) implements MessageHandler {

    @Override
    public void sendMessage(Map<String, String> data) {
        try{
            final StringBuilder messageBuilder = new StringBuilder();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                messageBuilder.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\n");
            }

            final String messageBody = messageBuilder.toString();
            final int messageLength = messageBody.length();

            DataOutputStream outputStream = new DataOutputStream(out);
            outputStream.writeInt(messageLength);

            outputStream.writeBytes(messageBody);
            outputStream.flush();

            System.out.println("Enviado (" + messageLength + " bytes):\n" + messageBody);
        } catch (Exception e){
            //xd
        }

    }

    @Override
    public Optional<String> receiveMessage() {
        try {
            final int messageLength = in.readInt();
            byte[] data = new byte[messageLength];
            in.readFully(data);

            final String message = new String(data);
            System.out.println("Recibido (" + messageLength + " bytes):\n" + message);

            return Optional.of(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
