package com.example.galerie_artisanale.security;

import com.example.galerie_artisanale.entity.Ordered;
import com.example.galerie_artisanale.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Component
public class MailConstructor {
    @Autowired
    private Environment env;

    @Autowired
    private TemplateEngine templateEngine;

    public  SimpleMailMessage constructResetTokenEmail(
            String contextPath, Locale locale, String token, User user
            ){

        String url = contextPath + "/newUser?token="+token+"\n ";


        String message = "\n Valider votre inscription en cliquant sur le lien suivant :\n Lien de validation: ";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Galerie Artisanale- Nouvelle inscription ");
        email.setText(message+url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    /*public MailConstructor(Environment env) {
        this.env = env;
    }*/

    public MimeMessagePreparator constructOrderConfirmationEmail (User user, Ordered ordered, Locale locale) {
        Context context = new Context();
        context.setVariable("ordered", ordered);
        context.setVariable("user", user);
        context.setVariable("cartItemList", ordered.getCartItemList());
        String text = templateEngine.process("orderConfirmationEmail", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setTo(user.getEmail());
                email.setSubject("Confirmation de la commande - "+ordered.getId());
                email.setText(text, true);
                email.setFrom(new InternetAddress("faelkhadkhoudi@gmail.com"));
            }
        };

        return messagePreparator;
    }

     public  SimpleMailMessage constructResetTokenPasswordEmail(
            String contextPath, Locale locale, String token, User user
    ){


        String url = contextPath + "/resetPassword?token="+token;
        String message = "\n Changer votre mot passe en cliquant sur  ce lien :\n";
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Galerie Artisanale- Réenitialisez le mot de passe ");
        email.setText(message+url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}
