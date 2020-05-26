package com.JPane;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Security;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;

// =============================================================================
//
// =============================================================================
public class JPaneMail {
	static String temp		= "/Volumes/Macintosh HD/Users/pierosbarato/Downloads/";
	final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	static String	host	= "pop3s.pec.aruba.it";
	static String	mail	= "formoto@pec.it";
	static String	username= "formoto@pec.it";
	static String	password= "mauro1933";
	Store	store	= null;
	Folder	inbox	= null;

	// =========================================================================
	JPaneMail(String mail, String host, String username, String password) {
		this.host = host;
		this.mail = mail;
		this.username = username;
		this.password = password;
		
		final String PORT = "995";

		Properties props = new Properties();
//		Session session = Session.getDefaultInstance(props, null);

		try {			
			props.setProperty("mail.pop3.port", "995");
			props.setProperty("mail.pop3.ssl.trust", "*");

//			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
			props.setProperty("mail.pop3.socketFactory.fallback", "false");
			props.setProperty("mail.pop3.port", PORT);
			props.setProperty("mail.pop3.socketFactory.port", PORT);
			URLName urln = new URLName("pop3", host, Integer.parseInt(PORT), null, username, password);

			Session session = Session.getInstance(props, null);
			Store store = session.getStore(urln);

			store.connect();
			
			
			
			store = session.getStore("pop3");
			store.connect(host, username, password);

			
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			
			Message[] messages = inbox.getMessages();

		    System.out.println("--- Legge:" + messages.length);

		    if (messages.length == 0) System.out.println("--- No messages found.");
		    else
		    for (int j = 0, l=0;
//		    		j < 100;
		    		j < messages.length;
		    		j++) {
		    	if(!messages[j].getSubject().contains("POSTA CERTIFICATA: Invio File"))
		    			continue;
		    	l++;
		        // stop after listing ten messages
/*		        if (l > 2) {
		          inbox.close(true);
		          store.close();
		          System.exit(0);
		        }
*/
		        System.out.println("Message : " + (j + 1));
		        System.out.println("From    : " + messages[j].getFrom()[0]);
		        System.out.println("Subject : " + messages[j].getSubject());
		        System.out.println("Date    : " + messages[j].getSentDate());
		        System.out.println();

		        Multipart multipart = (Multipart) messages[j].getContent();
		        for (int i = 0; i < multipart.getCount(); i++) {
		            BodyPart bodyPart = multipart.getBodyPart(i);

		            if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()))
//		            		&& StringUtils.isBlank(bodyPart.getFileName())) 
		            {
//		                continue; // dealing with attachments only
		            } 
//		            System.out.println("--- Allegati:" + bodyPart.getFileName());

		            String fileName = bodyPart.getFileName();
		            if(fileName == null) {
		            	fileName = "fattura.enc";
		            	Multipart mp = (Multipart)bodyPart.getContent();		            	
				        System.out.println("--- Len:" + multipart.getCount());//.getCount());
		            	for (int k = 0; k < mp.getCount(); k++) {
		                    BodyPart b = mp.getBodyPart(k);

		                    System.out.println("--- Type: " + b.getContentType());
		                    System.out.println("--- Name: " +b.getFileName()); 
		                    System.out.println("--- Cont: " +b.getContent()); 
		            	}
		            }

//		            System.out.println("--- Name:" + fileName);
		            InputStream is = bodyPart.getInputStream();
		            File f = new File(temp + "inFormoto/ITfile"+ j + "_" + i + "_" + fileName);
		            FileOutputStream fos = new FileOutputStream(f);
		            byte[] buf = new byte[4096];
		            int bytesRead;
		            while((bytesRead = is.read(buf))!=-1) {
		                fos.write(buf, 0, bytesRead);
		            }
		            fos.close();
//		            attachments.add(f);
		        }
		      }

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// =========================================================================
	static void decodeFile() {
//		String dir = JPane.temp + "inFormoto/";
//		String dir = JPane.temp + "fe_fornitori/";
		String dir = JPane.temp + "Fatture/in/";
		System.out.println("--- DecodeFileDir: " + dir);
		File f = new File(dir);
		File[] allSubFiles = f.listFiles();
		for (File file : allSubFiles) {
			{
				String name = file.getName().toString();
//				System.out.println("--- DecodeFile: " + val);

//				if (!val.endsWith(".enc") && !val.endsWith(".ENC"))
				if (!name.endsWith(".p7m") && !name.endsWith(".P7M"))
					continue;				
				String contenuto = JPane.readFile(dir + name);

				File f2 = new File(dir + name);
				byte[] bytes = null;
				try {
					bytes = Files.readAllBytes(f2.toPath());
				} catch (IOException e) {e.printStackTrace();}
				
				System.out.println("--- DecodeFile: " + name);				
				
//				contenuto = JPane.removeUTF8BOM(contenuto);
//				System.out.println("--- DecodeFile: " + name + "\n" + contenuto.length());
				String nameOut = name.replace(".p7m", "");

				contenuto = removeP7MCodes(nameOut, bytes);

//    			if(name.contains(".p7m")) 
/*    			{
    				String decode = "openssl smime -verify -in " + name
    						+ " -inform der -noverify -signer cert.pem -out "
    						+ nameOut;

    				int begin = contenuto.indexOf("<?xml version");
    				if(begin < 0) continue;
    				System.out.println("--- Begin: " + begin);
    				
    				contenuto = contenuto.substring(contenuto.indexOf("<?xml version"));
    				String end = ":FatturaElettronica>";
    				contenuto = contenuto.substring(0, contenuto.indexOf(end)+ end.length());
//    				contenuto = contenuto.replace("��", "");
//    				contenuto = contenuto.replace("","");
    			}
*/
    			JPane.writeTemp(nameOut, contenuto, "Fatture/in/");

/*
				try {
					MimeTokenStream stream = new MimeTokenStream();
					stream.parse(new FileInputStream(dir + val));
					for (EntityState state = stream.getState();
							state != EntityState.T_END_OF_STREAM; state = stream
							.next()) {
						switch (state) {
						case T_BODY:
							System.out.println("--- Body detected, contents = " + stream.getInputStream()
									+ ", header data = " + stream.getBodyDescriptor());
							break;
						case T_FIELD:
							System.out.println("--- Header field detected: " + stream.getField());
							break;

						case T_START_MULTIPART:
							System.out.println(
									"--- Multipart message detexted," + " header data = " + stream.getBodyDescriptor());
						}
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
*/
				
//				break;
			}
		}
	}

	// =========================================================================
	List<Message> read() {
		Message message[] = null;
		try {
			message = inbox.getMessages();
			return Arrays.stream(message).collect(Collectors.toList());

		} catch (MessagingException e) {
			e.printStackTrace();
		}

		return null;
	}

	// =========================================================================
	public static String removeP7MCodes(final String NOME_FILE, byte [] p7bytes) {
		System.out.println("--- Decode: " + NOME_FILE);
//		byte[] p7bytes = str.getBytes();
		try {
//			if (p7bytes == null || !NOME_FILE.toUpperCase().endsWith(".P7M")) {
//				return str;
//			}
			try {
				p7bytes = org.bouncycastle.util.encoders.Base64.decode(p7bytes);
			} catch (Exception e) {
				System.out.println("--- File P7m non in base64, tengo content standard" + e.getMessage());
			}

			CMSSignedData cms = new CMSSignedData(p7bytes);
			if (cms.getSignedContent() == null)
				System.out
					.println("--- Impossibile trovare signed Content durante decodifica da P7M per file: " + NOME_FILE);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			cms.getSignedContent().write(out);
			out.flush();
			return new String(out.toByteArray());
		} catch (CMSException | IOException e) {
			System.out.println("--- Impossibile decodificare P7M per File: " + NOME_FILE);
		}
		return new String(p7bytes);
	}

	// =========================================================================
	public static void main(String[] args) {
//		decodeFile();
//		System.exit(0);
		
		
		JPaneMail m = new JPaneMail(mail, host, username, password);
		m.read();

	}

}
