package com.thang.spotify.infra.email;

import com.thang.spotify.common.util.DateFormatterUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendRegistrationEmail(String toEmail,
                                String subject,
                                String body) {


        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("${spring.mail.username}");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public void sendEmailRegistrationHtml(String toEmail, String username) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(toEmail);
        helper.setSubject("Welcome to Spotify! Please Confirm Your Email.");

        String htmlContentTest = """
                <!DOCTYPE html>
                        <html lang="en">
                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <title>Welcome to Spotify clone</title>
                            <style>
                                * {
                                    margin: 0;
                                    padding: 0;
                                    box-sizing: border-box;
                                }
               \s
                                body {
                                    font-family: 'Arial', sans-serif;
                                    line-height: 1.6;
                                    color: #333;
                                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                                    min-height: 100vh;
                                    padding: 20px;
                                }
               \s
                                .email-container {
                                    max-width: 600px;
                                    margin: 0 auto;
                                    background: #ffffff;
                                    border-radius: 15px;
                                    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
                                    overflow: hidden;
                                    animation: slideIn 0.8s ease-out;
                                }
               \s
                                @keyframes slideIn {
                                    from {
                                        opacity: 0;
                                        transform: translateY(30px);
                                    }
                                    to {
                                        opacity: 1;
                                        transform: translateY(0);
                                    }
                                }
               \s
                                .header {
                                    background: linear-gradient(135deg, #07f7b6 0%, #00c3c1 50%, #007a78 100%);
                                    color: white;
                                    padding: 40px 30px;
                                    text-align: center;
                                    position: relative;
                                    overflow: hidden;
                                }
               \s
                                .header::before {
                                    content: '';
                                    position: absolute;
                                    top: -50%;
                                    left: -50%;
                                    width: 200%;
                                    height: 200%;
                                    background: repeating-linear-gradient(
                                        45deg,
                                        transparent,
                                        transparent 10px,
                                        rgba(255, 255, 255, 0.05) 10px,
                                        rgba(255, 255, 255, 0.05) 20px
                                    );
                                    animation: float 20s linear infinite;
                                }
               \s
                                @keyframes float {
                                    0% { transform: translate(-50%, -50%) rotate(0deg); }
                                    100% { transform: translate(-50%, -50%) rotate(360deg); }
                                }
               \s
                                .header h1 {
                                    font-size: 2.5em;
                                    margin-bottom: 10px;
                                    position: relative;
                                    z-index: 1;
                                    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
                                }
               \s
                                .header .tagline {
                                    font-size: 1.1em;
                                    opacity: 0.9;
                                    position: relative;
                                    z-index: 1;
                                }
               \s
                                .content {
                                    padding: 40px 30px;
                                }
               \s
                                .welcome-message {
                                    text-align: center;
                                    margin-bottom: 30px;
                                }
               \s
                                .welcome-message h2 {
                                    color: #2c3e50;
                                    font-size: 2em;
                                    margin-bottom: 15px;
                                    background: linear-gradient(135deg, #667eea, #764ba2);
                                    -webkit-background-clip: text;
                                    -webkit-text-fill-color: transparent;
                                    background-clip: text;
                                }
                       \s
                                .welcome-message p {
                                    color: #666;
                                    font-size: 1.1em;
                                    line-height: 1.8;
                                }
                       \s
                                .features {
                                    background: #f8f9fa;
                                    padding: 30px;
                                    border-radius: 10px;
                                    margin: 30px 0;
                                    border-left: 5px solid #ff6b6b;
                                }
                       \s
                                .features h3 {
                                    color: #2c3e50;
                                    margin-bottom: 20px;
                                    font-size: 1.3em;
                                }
                       \s
                                .feature-list {
                                    color: #555;
                                    line-height: 1.8;
                                }
                       \s
                                .feature-item {
                                    margin: 10px 0;
                                    padding-left: 20px;
                                    position: relative;
                                }
                       \s
                                .feature-item::before {
                                    content: '✓';
                                    position: absolute;
                                    left: 0;
                                    color: #27ae60;
                                    font-weight: bold;
                                    font-size: 1.2em;
                                }
                       \s
                                .cta-section {
                                    text-align: center;
                                    margin: 40px 0;
                                }
                       \s
                                .cta-button {
                                    display: inline-block;
                                    background: linear-gradient(135deg,#07f7b6 0%,#00c3c1 50%,#007a78 100%);
                                    color: white;
                                    text-decoration: none;
                                    padding: 15px 40px;
                                    border-radius: 50px;
                                    font-size: 1.1em;
                                    font-weight: bold;
                                    transition: all 0.3s ease;
                                    box-shadow: 0 8px 15px rgba(255, 107, 107, 0.3);
                                    position: relative;
                                    overflow: hidden;
                                }
                       \s
                                .cta-button::before {
                                    content: '';
                                    position: absolute;
                                    top: 0;
                                    left: -100%;
                                    width: 100%;
                                    height: 100%;
                                    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
                                    transition: left 0.5s;
                                }
                       \s
                                .cta-button:hover::before {
                                    left: 100%;
                                }
                       \s
                                .cta-button:hover {
                                    transform: translateY(-2px);
                                    box-shadow: 0 12px 25px rgba(255, 107, 107, 0.4);
                                }
                       \s
                                .footer {
                                    background: #2c3e50;
                                    color: white;
                                    padding: 30px;
                                    text-align: center;
                                }
                       \s
                                .footer p {
                                    margin: 10px 0;
                                    opacity: 0.8;
                                }
                       \s
                                .social-links {
                                    margin: 20px 0;
                                }
                       \s
                                .social-links a {
                                    display: inline-block;
                                    width: 40px;
                                    height: 40px;
                                    background: #34495e;
                                    color: white;
                                    text-decoration: none;
                                    border-radius: 50%;
                                    margin: 0 10px;
                                    line-height: 40px;
                                    transition: all 0.3s ease;
                                }
                       \s
                                .social-links a:hover {
                                    background: #ff6b6b;
                                    transform: scale(1.1);
                                }
                       \s
                                @media (max-width: 600px) {
                                    .email-container {
                                        margin: 10px;
                                        border-radius: 10px;
                                    }
                       \s
                                    .header {
                                        padding: 30px 20px;
                                    }
                       \s
                                    .header h1 {
                                        font-size: 2em;
                                    }
                       \s
                                    .content {
                                        padding: 30px 20px;
                                    }
                       \s
                                    .welcome-message h2 {
                                        font-size: 1.6em;
                                    }
                       \s
                                    .cta-button {
                                        padding: 12px 30px;
                                        font-size: 1em;
                                    }
                                }
                            </style>
                        </head>
                        <body>
                            <div class="email-container">
                                <div class="header">
                                    <h1>CheapDeals.com</h1>
                                    <p class="tagline">Your Gateway to Amazing Savings</p>
                                </div>
                       \s
                                <div class="content">
                                    <div class="welcome-message">
                                        <h2>Welcome, %s!</h2>
                                        <p>We're thrilled to have you join the CheapDeals.com family! Get ready to discover incredible deals, exclusive offers, and unbeatable savings on products you love.</p>
                                    </div>
                       \s
                                    <div class="features">
                                        <h3>What's waiting for you:</h3>
                                        <div class="feature-list">
                                            <div class="feature-item">Daily deals with up to 80% off retail prices</div>
                                            <div class="feature-item">Exclusive member-only flash sales</div>
                                            <div class="feature-item">Personalized recommendations just for you</div>
                                            <div class="feature-item">Early access to seasonal promotions</div>
                                            <div class="feature-item">15% off when purchasing via Mobile App.</div>
                                        </div>
                                    </div>
                       \s
                                    <div class="cta-section">
                                        <p style="margin-bottom: 20px; color: #666; font-size: 1.1em;">Ready to start saving?</p>
                                        <a href="http://localhost:5173/login" class="cta-button">
                                            Start Shopping Now
                                        </a>
                                    </div>
                                </div>
                       \s
                                <div class="footer">
                                    <div class="social-links">
                                        <a href="#" title="Facebook">f</a>
                                        <a href="#" title="Twitter">t</a>
                                        <a href="#" title="Instagram">i</a>
                                        <a href="#" title="YouTube">y</a>
                                    </div>
                                    <p><strong>CheapDeals.com LTD</strong></p>
                                    <p>Making quality affordable for everyone</p>
                                    <p style="font-size: 0.9em; margin-top: 15px;">
                                        This email was sent because you registered for an account with Spotify.com<br>
                                        If you have any questions, contact us at cheapdeal466@gmail.com
                                    </p>
                                </div>
                            </div>
                        </body>
                        </html>
               \s""";
        String safeHtml = htmlContentTest.replace("%", "%%").replace("%%s", "%s");
        String htmlContentFormatted = String.format(safeHtml, username);

        helper.setText(htmlContentFormatted, true);

        mailSender.send(message);
    }

//    public EmailConfirmResponse confirmToken(String token) {
//        if (token.equals("123456")) {
//            return new EmailConfirmResponse("Token is valid!", token);
//        }
//        return null;
//    }



}