package br.com.caelum.jms.topico;

import br.com.caelum.jms.modelo.Pedido;
import br.com.caelum.jms.modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;
import java.io.StringWriter;

public class PublicadorTopicoComObjeto {

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
        Topic topico = (Topic) context.lookup("loja");

        // Cria um produtor de mensagens relacionado à fila financeiro
        MessageProducer producer = session.createProducer(topico);

        // Cria um pedido que será enviado para a fila
        Pedido pedido = new PedidoFactory().geraPedidoComValores();

        // Cria uma mensagem com o pedido em xml
        Message message = session.createObjectMessage(pedido);

        // Envia a mensagem para a fila
        producer.send(message);

        // Fecha a sessão, conexão e contexto.
        session.close();
        connection.close();
        context.close();
    }
}
