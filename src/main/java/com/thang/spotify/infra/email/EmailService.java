package com.thang.spotify.infra.email;

import com.thang.spotify.common.util.DateFormatterUtil;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

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

    public void sendEmailRegistrationHtml(String toEmail, String username, String linkToken) throws MessagingException {

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
                <title>Welcome to Spotify</title>
                <style>
                    * {
                        margin: 0;
                        padding: 0;
                        box-sizing: border-box;
                    }
      \s
                    body {
                        font-family: 'Helvetica Neue', Arial, sans-serif;
                        line-height: 1.6;
                        color: #ffffff;
                        background: #000000;
                        min-height: 100vh;
                        padding: 20px;
                    }
      \s
                    .email-container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: #121212;
                        border-radius: 8px;
                        overflow: hidden;
                        border: 1px solid #333333;
                    }
      \s
                    .header {
                        background: linear-gradient(135deg, #1db954 0%, #1ed760 100%);
                        color: #000000;
                        padding: 40px 30px;
                        text-align: center;
                        position: relative;
                    }
      \s
                    .spotify-logo {
                        font-size: 2.5em;
                        font-weight: bold;
                        margin-bottom: 10px;
                        letter-spacing: -1px;
                    }
      \s
                    .header .tagline {
                        font-size: 1.1em;
                        opacity: 0.8;
                        font-weight: 500;
                    }
      \s
                    .content {
                        padding: 40px 30px;
                        color: #ffffff;
                    }
      \s
                    .welcome-message {
                        text-align: center;
                        margin-bottom: 40px;
                    }
      \s
                    .welcome-message h2 {
                        color: #ffffff;
                        font-size: 2.2em;
                        margin-bottom: 20px;
                        font-weight: 700;
                    }
          \s
                    .welcome-message p {
                        color: #b3b3b3;
                        font-size: 1.1em;
                        line-height: 1.6;
                        max-width: 480px;
                        margin: 0 auto;
                    }
          \s
                    .features {
                        background: #181818;
                        padding: 30px;
                        border-radius: 8px;
                        margin: 30px 0;
                        border-left: 4px solid #1db954;
                    }
          \s
                    .features h3 {
                        color: #ffffff;
                        margin-bottom: 20px;
                        font-size: 1.4em;
                        font-weight: 600;
                    }
          \s
                    .feature-list {
                        color: #b3b3b3;
                        line-height: 1.8;
                    }
          \s
                    .feature-item {
                        margin: 15px 0;
                        padding-left: 30px;
                        position: relative;
                        font-size: 1em;
                    }
          \s
                    .feature-item::before {
                        content: '♪';
                        position: absolute;
                        left: 0;
                        color: #1db954;
                        font-weight: bold;
                        font-size: 1.4em;
                        top: -2px;
                    }
          \s
                    .cta-section {
                        text-align: center;
                        margin: 40px 0;
                    }
          \s
                    .cta-text {
                        color: #b3b3b3;
                        font-size: 1.1em;
                        margin-bottom: 25px;
                    }
          \s
                    .cta-button {
                        display: inline-block;
                        background: #1db954;
                        color: #000000;
                        text-decoration: none;
                        padding: 16px 48px;
                        border-radius: 50px;
                        font-size: 1.1em;
                        font-weight: 700;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                        transition: all 0.3s ease;
                        box-shadow: 0 4px 15px rgba(29, 185, 84, 0.3);
                    }
          \s
                    .cta-button:hover {
                        background: #1ed760;
                        transform: translateY(-2px);
                        box-shadow: 0 6px 20px rgba(29, 185, 84, 0.4);
                    }
          \s
                    .premium-highlight {
                        background: linear-gradient(135deg, #1db954, #1ed760);
                        padding: 25px;
                        border-radius: 8px;
                        margin: 30px 0;
                        text-align: center;
                        color: #000000;
                    }
          \s
                    .premium-highlight h4 {
                        font-size: 1.3em;
                        margin-bottom: 10px;
                        font-weight: 700;
                    }
          \s
                    .premium-highlight p {
                        font-size: 1em;
                        opacity: 0.9;
                        font-weight: 500;
                    }
          \s
                    .footer {
                        background: #000000;
                        color: #b3b3b3;
                        padding: 30px;
                        text-align: center;
                        border-top: 1px solid #333333;
                    }
          \s
                    .footer p {
                        margin: 8px 0;
                        font-size: 0.9em;
                    }
          \s
                    .footer .brand {
                        color: #1db954;
                        font-weight: 600;
                        font-size: 1em;
                    }
          \s
                    .social-links {
                        margin: 25px 0 15px 0;
                    }
          \s
                    .social-links a {
                        display: inline-block;
                        width: 40px;
                        height: 40px;
                        background: #333333;
                        color: #ffffff;
                        text-decoration: none;
                        border-radius: 50%;
                        margin: 0 8px;
                        line-height: 40px;
                        transition: all 0.3s ease;
                        font-weight: bold;
                    }
          \s
                    .social-links a:hover {
                        background: #1db954;
                        color: #000000;
                        transform: scale(1.1);
                    }
          \s
                    @media (max-width: 600px) {
                        .email-container {
                            margin: 10px;
                            border-radius: 8px;
                        }
          \s
                        .header {
                            padding: 30px 20px;
                        }
          \s
                        .spotify-logo {
                            font-size: 2em;
                        }
          \s
                        .content {
                            padding: 30px 20px;
                        }
          \s
                        .welcome-message h2 {
                            font-size: 1.8em;
                        }
          \s
                        .cta-button {
                            padding: 14px 32px;
                            font-size: 1em;
                        }
          \s
                        .features {
                            padding: 20px;
                        }
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <div class="header">
                        <div class="spotify-logo">Spotify</div>
                        <p class="tagline">Music for everyone</p>
                    </div>
          \s
                    <div class="content">
                        <div class="welcome-message">
                            <h2>Welcome to Spotify, %s!</h2>
                            <p>Thanks for joining Spotify. You're now ready to start listening to the music you love, and discover new favorites from a world of artists.</p>
                        </div>
          \s
                        <div class="features">
                            <h3>What you can do with Spotify:</h3>
                            <div class="feature-list">
                                <div class="feature-item">Stream millions of songs and podcasts</div>
                                <div class="feature-item">Create and share playlists with friends</div>
                                <div class="feature-item">Discover new music with personalized recommendations</div>
                                <div class="feature-item">Download music for offline listening</div>
                                <div class="feature-item">Follow your favorite artists and get updates</div>
                            </div>
                        </div>
          \s
                        <div class="premium-highlight">
                            <h4>🎵 Try Spotify Premium Free for 1 Month</h4>
                            <p>Ad-free music, offline downloads, and unlimited skips</p>
                        </div>
          \s
                        <div class="cta-section">
                            <p class="cta-text">Ready to start your musical journey?</p>
                            <a href="%s" class="cta-button">
                                Start Listening
                            </a>
                        </div>
                    </div>
          \s
                    <div class="footer">
                        <div class="social-links">
                            <a href="#" title="Facebook">f</a>
                            <a href="#" title="Twitter">𝕏</a>
                            <a href="#" title="Instagram">📷</a>
                            <a href="#" title="YouTube">▶</a>
                        </div>
                        <p class="brand">Spotify</p>
                        <p>Your soundtrack to life</p>
                        <p style="font-size: 0.8em; margin-top: 20px; opacity: 0.7;">
                            This email was sent because you created a Spotify account.<br>
                            If you have any questions, contact us at support@spotify.com
                        </p>
                    </div>
                </div>
            </body>
            </html>
  \s""";
        String safeHtml = htmlContentTest.replace("%", "%%").replace("%%s", "%s");
        String htmlContentFormatted = String.format(safeHtml, username, linkToken);
        helper.setText(htmlContentFormatted, true);
        mailSender.send(message);
    }


}