package com.example.demo;

import com.example.demo.core.model.ResultSendDeparture;
import com.example.demo.model.DepartureResponse;
import com.example.demo.model.SettingsEmailDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.sql.Struct;
import java.util.Properties;

public class SendEmailService {


    public ResultSendDeparture send(SettingsEmailDto settingsEmailDto, DepartureResponse response){

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        String json = gson.toJson(response);

        Properties properties = System.getProperties (); // Получить системные свойства
        properties.put ("mail.smtp.host", settingsEmailDto.getSmtpServer()); // Установить почтовый сервер
        properties.put ("mail.smtp.port", settingsEmailDto.getSmtpPort()); // номер порта // установить почтовый порт
        properties.put ("mail.transport.protocol", "smtp"); // Протокол должен быть выбран
        properties.put ("mail.smtp.auth", "true"); // Установить, проверена ли почта
        // Зашифрованное соединение SSL легко отложить
        properties.put ("mail.smtp.ssl.enable", "" + true + ""); // Установить, использовать ли безопасное соединение ssl setProperty
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(settingsEmailDto.getEmail(), settingsEmailDto.getPasswordEmail()); //Kj3JG0XXMXDWPMrf5i3A
            }
        };
        Session session = Session.getInstance(properties, auth);

        MimeMessage message = new MimeMessage(session);

        try {// подтверждение учетной записи почты
            Transport ts = session.getTransport();
            ts.connect();// user, password
        } catch (MessagingException au) {
            return new ResultSendDeparture(false, au.getMessage());
        }

        try {
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText (settingsEmailDto.getEmail()); // Установить пользовательское имя отправителя
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            message.setFrom(new InternetAddress(nick + "<" + settingsEmailDto.getEmail() + ">"));

            message.addRecipient (Message.RecipientType.TO, new InternetAddress (settingsEmailDto.getServerEmail())); // Установить получателя
            message.setSubject ("Departure"); // Установить заголовок сообщения
            message.setText(json);

            // Отправить письмо

            try {
                Transport.send(message);
                return new ResultSendDeparture(true, "");
            }catch (MessagingException ex) {
                return new ResultSendDeparture(false, ex.getMessage());
            }

        } catch (MessagingException mex) {
            return new ResultSendDeparture(false, mex.getMessage());
        }

    }

}
