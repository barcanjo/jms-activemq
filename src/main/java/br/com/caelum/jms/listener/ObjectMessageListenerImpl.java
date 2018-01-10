package br.com.caelum.jms.listener;

import br.com.caelum.jms.modelo.Pedido;

import javax.jms.*;

public class ObjectMessageListenerImpl implements MessageListener {

    @Override
    public void onMessage(Message message) {
        // Converte a mensagem para o sub-tipo TextMessage
        ObjectMessage objectMessage = (ObjectMessage) message;

        try {
            Pedido pedido = (Pedido) objectMessage.getObject();

            // Exibe a mensagem no console
            System.out.println(pedido.toString());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
