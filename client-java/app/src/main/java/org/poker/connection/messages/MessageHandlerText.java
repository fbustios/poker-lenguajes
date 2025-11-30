package org.poker.connection.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

public record MessageHandlerText(BufferedReader in, PrintWriter out) implements MessageHandler {

    @Override
    public void sendMessage(Map<String, String> data) {
        final StringBuilder messageBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            messageBuilder.append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue())
                    .append("\n");
        }

        final String messageBody = messageBuilder.toString();
        final int messageLength = messageBody.length();


        out.println(messageLength);

        out.print(messageBody);
        out.flush();

        System.out.println("Enviado (" + messageLength + " bytes):\n" + messageBody);
    }

    @Override
    public Optional<String> receiveMessage() {
        try {
            final Optional<String> lengthLine = Optional.ofNullable(in.readLine());

            if (lengthLine.isEmpty()) {
                return Optional.empty();
            }

            final int messageLength;
            try {
                messageLength = Integer.parseInt(lengthLine.get().trim());
            } catch (NumberFormatException e) {
                System.err.println("Error: longitud de mensaje inválida: " + lengthLine);
                return Optional.empty();
            }

            final char[] buffer = new char[messageLength];
            int totalRead = 0;
            while (totalRead < messageLength) {
                final int read = in.read(buffer, totalRead, messageLength - totalRead);
                if (read == -1) {
                    throw new IOException("Conexión cerrada mientras se leía el mensaje");
                }
                totalRead += read;
            }

            final String message = new String(buffer);
            System.out.println("Recibido (" + messageLength + " bytes):\n" + message);

            return Optional.of(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
