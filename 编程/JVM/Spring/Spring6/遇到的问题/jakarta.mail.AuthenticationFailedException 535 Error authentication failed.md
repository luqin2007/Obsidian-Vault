> [!error] jakarta. mail. AuthenticationFailedException: 535 Error: authentication failed

```
org.springframework.jms.listener.adapter.ListenerExecutionFailedException: Listener method 'public void com.example.mybank.listener.EmailMessageListeners.onSendEmail(jakarta.jms.Message)' threw exception
	at org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:118)
	at org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter.onMessage(MessagingMessageListenerAdapter.java:84)
	at org.springframework.jms.listener.AbstractMessageListenerContainer.doInvokeListener(AbstractMessageListenerContainer.java:783)
	at org.springframework.jms.listener.AbstractMessageListenerContainer.invokeListener(AbstractMessageListenerContainer.java:741)
	at org.springframework.jms.listener.AbstractMessageListenerContainer.doExecuteListener(AbstractMessageListenerContainer.java:719)
	at org.springframework.jms.listener.AbstractPollingMessageListenerContainer.doReceiveAndExecute(AbstractPollingMessageListenerContainer.java:333)
	at org.springframework.jms.listener.AbstractPollingMessageListenerContainer.receiveAndExecute(AbstractPollingMessageListenerContainer.java:246)
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.invokeListener(DefaultMessageListenerContainer.java:1258)
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.executeOngoingLoop(DefaultMessageListenerContainer.java:1248)
	at org.springframework.jms.listener.DefaultMessageListenerContainer$AsyncMessageListenerInvoker.run(DefaultMessageListenerContainer.java:1141)
	at java.base/java.lang.Thread.run(Thread.java:833)
Caused by: org.springframework.mail.MailAuthenticationException: Authentication failed
	at org.springframework.mail.javamail.JavaMailSenderImpl.doSend(JavaMailSenderImpl.java:402)
	at org.springframework.mail.javamail.JavaMailSenderImpl.send(JavaMailSenderImpl.java:317)
	at org.springframework.mail.MailSender.send(MailSender.java:42)
	at com.example.mybank.listener.EmailMessageListeners.onSendEmail(EmailMessageListeners.java:29)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:169)
	at org.springframework.messaging.handler.invocation.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:119)
	at org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter.invokeHandler(MessagingMessageListenerAdapter.java:110)
	... 10 common frames omitted
Caused by: jakarta.mail.AuthenticationFailedException: 535 Error: authentication failed

	at org.eclipse.angus.mail.smtp.SMTPTransport$Authenticator.authenticate(SMTPTransport.java:954)
	at org.eclipse.angus.mail.smtp.SMTPTransport.authenticate(SMTPTransport.java:865)
	at org.eclipse.angus.mail.smtp.SMTPTransport.protocolConnect(SMTPTransport.java:769)
	at jakarta.mail.Service.connect(Service.java:345)
	at org.springframework.mail.javamail.JavaMailSenderImpl.connectTransport(JavaMailSenderImpl.java:480)
	at org.springframework.mail.javamail.JavaMailSenderImpl.doSend(JavaMailSenderImpl.java:399)
	... 20 common frames omitted
```

‍
