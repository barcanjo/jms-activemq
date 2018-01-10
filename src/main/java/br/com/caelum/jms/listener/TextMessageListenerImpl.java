package br.com.caelum.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TextMessageListenerImpl implements MessageListener {

    private boolean acknowledged;

    public TextMessageListenerImpl(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    @Override
    public void onMessage(Message message) {
        // Converte a mensagem para o sub-tipo TextMessage
        TextMessage textMessage = (TextMessage) message;

        // Exibe a mensagem no console
        try {
            System.out.println(textMessage.getText());

            if (acknowledged) {
                message.acknowledge();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
