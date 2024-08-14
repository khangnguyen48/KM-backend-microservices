package com.nskay.microservices.notification.service;

import com.nskay.microservices.notification.dto.OrderPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "order-placed")
    public void listen(OrderPlacedEvent orderPlacedEvent){
        log.info("Received orderPlacedEvent {} from Kafka topic order-placed", orderPlacedEvent);
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("nhatkhangnguyen1111@gmail.com");
            mimeMessageHelper.setTo(orderPlacedEvent.getEmail());
            mimeMessageHelper.setSubject(String.format("Your order with order number %s has been placed", orderPlacedEvent.getOrderNumber()));
            mimeMessageHelper.setText(String.format("""
                            Dear customer,
                            Your order with order number %s has been placed successfully.
                            
                            Thanks for shopping with us.
                            Best Regards,
                            NNK
                            """, orderPlacedEvent.getOrderNumber()));

        };
        try {
            javaMailSender.send(mimeMessagePreparator);
            log.info("Order Notifcation email sent!!");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
            throw new RuntimeException("Exception occurred when sending mail to springshop@email.com", e);
        }

    }

}
