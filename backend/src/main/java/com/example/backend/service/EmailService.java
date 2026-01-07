package com.example.backend.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Async
    public void sendRegistrationEmail(String recipientEmail, String regId, String bandName) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            if(senderEmail != null)
            {
                helper.setFrom(senderEmail);
            }

            if(recipientEmail != null)
            {
                helper.setTo(recipientEmail);
            }
            helper.setSubject("Oikotaan 8 | Registration Confirmation | " + regId);

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0;'>"
                    + "<div style='background-color: #f9f9f9; padding: 20px;'>"
                    + "<div style='text-align: center; padding: 10px;'>"
                    + "<img src='cid:eventLogo' alt='Event Logo' style='width: 180px; height: 160px; margin: 0px;' />"
                    + "</div>"
                    + "<div style='background-color: #ffffff; padding: 20px; border-radius: 8px;'>"
                    + "<h2 style='text-align: center; color: #4CAF50; font-size: 22px;'>Greetings from Team Euphony Oikotaan!</h2>"
                    + "<p style='font-size: 16px;'>Hi <b>" + bandName + "</b>,</p>"
                    + "<p style='font-size: 16px;'>We are thrilled to inform you that your registration for <b>Oikotaan 8</b> has been successfully processed.</p>"
                    + "<p style='font-size: 16px;'><b>Registration Number:</b> " + regId + "</p>"
                    + "<p style='font-size: 16px;'>Please keep this number handy for any future communication with us.</p>"
                    + "<h3 style='color: #333; font-size: 18px;'>Next Steps:</h3>"
                    + "<ul style='font-size: 15px; line-height: 1.5;'>"
                    + "<li>Your submitted composition will be reviewed by the <b>Euphony Oikotaan Selection Team</b>.</li>"
                    + "<li>Selected participants will be contacted via phone by <b>10 Jan 2026</b>.</li>"
                    + "<li>Confirmed performers will receive a detailed confirmation letter by <b>15 Jan 2025</b>.</li>"
                    + "</ul>"
                    + "<p style='font-size: 16px;'>We look forward to seeing you rock the stage and create magic!</p><br/>"
                    + "<h3 style='color: #333; font-size: 18px;'>Contact Us:</h3>"
                    + "<table style='width: auto; font-size: 15px; line-height: 1.8;'>"
                    + "<tr><td style='padding-right: 10px;'><b>Farhan</b>:</td><td>+91 7384928393</td></tr>"
                    + "<tr><td style='padding-right: 10px;'><b>Shubhayu</b>:</td><td>+91 93192 00435</td></tr>"
                    + "<tr><td style='padding-right: 10px;'><b>Aditi</b>:</td><td>+91 8910207436</td></tr>"
                    + "<tr><td style='padding-right: 10px;'><b>Aryan</b>:</td><td>+91 7044596472</td></tr>"
                    + "</table>"
                    + "</div>"
                    + "<div style='text-align: center; padding: 10px; color: #777; font-style: italic;'>"
                    + "Welcome to the Oikotaan Family!"
                    + "</div>"
                    + "<div style='text-align: left; padding: 10px;'>"
                    + "<img src='cid:euphonyLogo' alt='Euphony Logo' style='width: 120px; height: 140px; margin: 5px;' />"
                    + "<img src='cid:iiestLogo' alt='IIEST Logo' style='width: 120px; height: 140px; margin: 5px;' />"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";


            helper.setText(htmlContent, true);

            // Add inline resources
            Resource euphonyLogo = new ClassPathResource("static/euphony-logo-white.png");
            helper.addInline("euphonyLogo", euphonyLogo);
            Resource iiestLogo = new ClassPathResource("static/iiest-white.png");
            helper.addInline("iiestLogo", iiestLogo);
            Resource eventLogo = new ClassPathResource("static/fest-logo_8.jpg");
            helper.addInline("eventLogo", eventLogo);

            javaMailSender.send(message);
            logger.info("Registration email sent successfully to {}", recipientEmail);
        } catch (Exception e) {
            logger.error("Failed to send registration email", e);
        }
    }
}
