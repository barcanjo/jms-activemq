package br.com.caelum.jms.fila;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;

public class Navegador {

    public static void main(String[] args) throws NamingException, JMSException {
        // Cria um contexto para procurar pela conexao e filas
        InitialContext context = new InitialContext();

        // Cria uma fabrica de conexões através do nome seu nome padrão
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        // Abre uma nova conexão do ActiveMQ utilizando a fábrica de conexões
        Connection connection = factory.createConnection("user","senha");
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
        Queue fila = (Queue) context.lookup("financeiro");

        // Cria um nevegador para consultar mensagens sem consumi-las
        javax.jms.QueueBrowser browser = session.createBrowser(fila);

        // Retorna as mensagens contidas no navegador, encontradas na fila
        Enumeration msgs = browser.getEnumeration();
        while (msgs.hasMoreElements()) {
            TextMessage msg = (TextMessage) msgs.nextElement();
            System.out.println("Message: " + msg.getText());
        }

        // Fecha a sessão, conexão e contexto.
        session.close();
        connection.close();
        context.close();
    }
}
