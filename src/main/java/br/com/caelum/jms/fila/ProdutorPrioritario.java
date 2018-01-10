package br.com.caelum.jms.fila;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Cria um produtor que prioriza as mensagens.
 * Para que a priorização seja habilitada é necessário às configurações
 * do ActiveMQ a tag <policyEntry queue=">" prioritizedMessages="true"/>
 * onde o valor ">" indica que todas as filas terão prioridade.
 */
public class ProdutorPrioritario {

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

        // Cria um produtor de mensagens relacionado à fila financeiro
        MessageProducer producer = session.createProducer(fila);

        // Cria uma mensagem que será envada para a fila
        Message message = session.createTextMessage("DEBUG: ...");
        /*
            Envia a mensagem para a fila.
            Utiliza o DeliveryMode.PERSISTENT para informar ao ActiveMQ que guarde
            a mensagem no seu banco de dados para reutilizá-la caso ele seja reiniciado.
            Define a prioridade 3 (0~9, quanto maior o número maior é a prioridade).
            E define o TTL (Time To Live, ou tempo de vida) da mensagem em 60 segundos.
         */
        producer.send(message, DeliveryMode.PERSISTENT, 1, 60000);

        // Fecha a sessão, conexão e contexto.
        session.close();
        connection.close();
        context.close();
    }
}
