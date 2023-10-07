package com.mobilegroup3.lifetaskhelper.ui.contact;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mobilegroup3.lifetaskhelper.R;
import com.mobilegroup3.lifetaskhelper.databinding.FragmentContactBinding;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ContactFragment extends Fragment {

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    String emailHost = "smtp.gmail.com";
    String emailPort = "587";// gmail's smtp port
    String fromUser = "";
    String fromUserEmailPassword = "";
    String[] toEmails = { "" };

    public void setMailServerProperties() {
        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
    }

    public void createEmailMessage(String emailBody) throws AddressException, MessagingException {
        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);
        for (int i = 0; i < toEmails.length; i++) {
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmails[i]));
        }
        emailMessage.setContent(emailBody, "text/html");
    }

    public void sendEmail() throws AddressException, MessagingException {
        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
    }

    private ContactViewModel contactViewModel;
    private FragmentContactBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentContactBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            new Thread(() -> {
                try {
                    Button sendBtn = view.findViewById(R.id.button);
                    EditText message = view.findViewById(R.id.edtTxtMessage);


                    sendBtn.setOnClickListener(v -> {
                        setMailServerProperties();
                        try {
                            System.out.println("body " + message.getText().toString());
                            createEmailMessage(message.getText().toString());

                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                        try {
                            sendEmail();
                            System.out.println("Message sent!");
                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return view;
        }




        /*
        contactViewModel =
                new ViewModelProvider(this).get(ContactViewModel.class);
        final TextView textView = binding.textGallery;
        contactViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
         */


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
