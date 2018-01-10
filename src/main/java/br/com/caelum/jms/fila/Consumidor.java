package br.com.caelum.jms.fila;

import br.com.caelum.jms.listener.TextMessageListenerImpl;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Consumidor {

    public static void main(String[] args) throws NamingException, JMSException {
        // Cria um contexto para procurar pela conexao e filas
        InitialContext context = new InitialContext();

        // Cria uma fabrica de conexões através do nome seu nome padrão
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        // Abre uma nova conexão do ActiveMQ utilizando a fábrica de conexões
        Connection connection = factory.createConnection("user", "senha");
        // Inicia uma conexão no ActiveMQ
        connection.start();

        /*
            Com a conexão iniciada abre uma sessão capaz de receber mensagens,
            sem transação (createSession(false...))
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /*
            Com a sessão aberta recupera a fila "financeiro" que receberá a mensagem.
            Este nome vem de jndi.properties, na propriedade chamada "fila.financeiro",
            omitindo a palavra fila.
         */
        Queue fila = (Queue) context.lookup("log");

        // Cria um consumidor de mensagens relacionado à fila financeiro
        MessageConsumer consumer = session.createConsumer(fila);

        // Cria um Listner, que ficará escutando por novas mensagens.
        TextMessageListenerImpl messageListenerImpl = new TextMessageListenerImpl(false);
        consumer.setMessageListener(messageListenerImpl);

        System.out.println("Aguardando mensagens... Pressione qualquer tecla para sair.");

        // Apertar qualquer tecla para continuar.
        new Scanner(System.in).nextLine();

        // Fecha a sessão, conexão e contexto.
        session.close();
        connection.close();
        context.close();
    }
}
