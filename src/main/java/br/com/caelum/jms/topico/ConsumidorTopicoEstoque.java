package br.com.caelum.jms.topico;

import br.com.caelum.jms.listener.TextMessageListenerImpl;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

/**
 * Cria um consumir com assinatura durável.
 * É necessário rodar esta classe antes do produtor enviar a mensagem
 * para que sua assinatura seja registrada e o ActiveMQ deixe a
 * mensagem guardada para ele (o que não é o padrão).
 *
 */
public class ConsumidorTopicoEstoque {

    public static void main(String[] args) throws NamingException, JMSException {
        // Cria um contexto para procurar pela conexao e filas
        InitialContext context = new InitialContext();

        // Cria uma fabrica de conexões através do nome seu nome padrão
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        // Abre uma nova conexão do ActiveMQ utilizando a fábrica de conexões
        Connection connection = factory.createConnection("user", "senha");
        // Define um ID para identificar a conexão (necessário para criar a assinatura durável)
        connection.setClientID("estoque");
        // Inicia uma conexão no ActiveMQ
        connection.start();

        /*
            Com a conexão iniciada abre uma sessão capaz de receber mensagens,
            sem transação (createSession(false...))
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        /*
            Com a sessão aberta recupera a fila "financeiro" que receberá a mensagem.
            Este nome vem de jndi.properties, na propriedade chamada "fila.topico",
            omitindo a palavra fila.
         */
        Topic topico  = (Topic) context.lookup("loja");

        // Cria um consumidor de mensagens relacionado ao tópico loja
        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");
        // Identifica o consumidor (necessário para criar a assinatura durável)

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
