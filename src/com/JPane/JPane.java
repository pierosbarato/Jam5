package com.JPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.JPane.JPane.JPaneDoc;
import com.JPane.JPane.JPaneMaster.Item;
import com.JPane.JPane5.tmpJSON;

// import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.FileOutputStream;
// =============================================================================
// JPane ver 5.17.4.17 Genoa Italy 2/aug/2020. JSON Jam5 (test)
// =============================================================================
public class JPane {
	static String VERSION	= "5.17.4.17 2/aug/2020 JPane JSON Jam5 (test)";
	String terminal			= "77";
	static String awsS3		= "https://s3.eu-central-1.amazonaws.com/jpane/pane/";
	static String temp		= "/Volumes/Macintosh HD/Users/pierosbarato/Downloads/";
//	static String temp		= "~/Downloads/";
	static String tempWin	= "c:/Jamfive/";
//	static String tempIn	= "inBronx/";
//	static String tempIn	= "inMauro/";
	static String tempIn	= "FattNew/in/";
	static String currPath	= "";
	SQL sql					= null;
	String sid				= null;
	static int flagLog		= 0;
	static int numFattura	= 1;
//	static int numFattura	= 500; // manca marino 243
	static String utente	= "null";
	static String esercizio	= "19";
	static String esercizio1= esercizio.substring(1);
	static String codGioAcq	= "7";
	static String codGioCur	= "3";
	static String codCauAcq	= "FEP";
	static String codCauNcr	= "FEN";
	static String codGioVen = "1"; // 2 formoto 1 altri
	static String causale	= "";
	static String partitaIva= "";

	static String mailProv	= "ARUBA";
	static String mailMitt	= "formoto@pec.it";
	static String mailPwd	= "mauro1933";
	static String mailDest	= "sdi45@pec.fatturapa.it";

	static String isName	= "is";

	static String mainLog	= "";
	static String sqlLog	= "";
	static String rcvLog	= "";
	static String csvLog	= "\"\"\n";

	static String sqlIns	= "";
	static String sqlInsVal= "";

	static String sqlIns2	= "";
	static String sqlInsVal2= "";

	static String sqlIns3	= "";
	static String sqlInsVal3= "";

	static String tmpOrdine = "";
	static String tmpBolla	= "";
	static String tmpBolla2	= "";

	public JPane() {this("77", null);terminal="77";}
	public JPane(String terminal, String sid) {
		this.terminal = terminal;
		this.sid = sid;
		if(this.sid == null) this.sid = getDate();
		if(this.sql == null) this.sql = new SQL();
		File currDir = new File(".");
		try {currPath = currDir.getCanonicalPath();
		} catch (IOException e) {e.printStackTrace();}

		if(isWindows()) {
			flagLog = 0;
			temp = tempWin;
			temp = currPath.substring(0, 1) + temp.substring(1);
			tempIn = "FattNew/in/";
		} else {
			temp.replaceFirst("^~", System.getProperty("user.home"));
		}

		new File(temp + "FattNew/out").mkdirs();
		new File(temp + "FattNew/outNew").mkdirs();
		new File(temp + "FattNew/log").mkdirs();

//		log("w5", 
		log("i5", ""  + 
				"JPane:" + this.terminal + " sid:" + this.sid
				+ " temp:" + temp + " dir:" + currPath);
	}

	// =========================================================================
	//
	// =========================================================================
	String tmpNumBolla	= "";
	String tmpDataBolla = "";
	String tmpLine		= "";
	void creaBolla(String NumBolla, String DataBolla, int line) {
		if(!tmpNumBolla.equalsIgnoreCase(NumBolla)) {
			tmpBolla2 += "<DatiDDT>\n"
					+ "<NumeroDDT>" + NumBolla + "</NumeroDDT>\n"
					+ "<DataDDT>" + formatDate(DataBolla, "yyyy-MM-dd", null) + "</DataDDT>\n"
					+ "<RiferimentoNumeroLinea>" + tmpLine + "</RiferimentoNumeroLinea>\n"
					+ "</DatiDDT>\n"
					;
			log("i5", "Bolla:" + line);
		}
		tmpNumBolla = NumBolla;
	}

	// =========================================================================
	//
	// =========================================================================
	public static String postStringSync(String urlString
						,String dati, String file, String mime, String name) {

		if(name == null) name = "upload";
		if(mime == null) mime = "text/plain";

		log("w5", "postStringSync:" + file + " name:" + name + " url:" + urlString);

		long startTime = System.nanoTime();

		String ret					= "";
		HttpURLConnection conn		= null;
		DataOutputStream dos		= null;
		DataInputStream inStream	= null;

		String path					= "";
//		String exsistingFileName	= "";

		String lineEnd				= "\r\n";
		String twoHyphens			= "--";
		String boundary				= "*****";

		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String uri					= file;
		if(file.startsWith("/")) uri = file.substring(1);

		if(!urlString.startsWith("http:")) urlString = "http://" + urlString;

		log("w5", "Upload: " + file + " URL: " + urlString);

		try {
			// ------------------ CLIENT REQUEST
//			FileInputStream fileInputStream = new FileInputStream(new File(
//															exsistingFileName));

			// convert String into InputStream
			ByteArrayInputStream fileInputStream = new ByteArrayInputStream(dati.getBytes());

			// open a URL connection to the Servlet
			URL url = new URL(urlString);

			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();

			conn.setConnectTimeout(20 * 1000);

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type"
								,"multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"" + name + "\";"
					+ " filename=\"" + file + "\"" + lineEnd);
			dos.writeBytes("Content-Type: " + mime + lineEnd);

			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while(bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			fileInputStream.close();
			dos.flush();
			dos.close();
		} catch (MalformedURLException ex) {
			log("e5", "From ServletCom CLIENT REQUEST:" + ex);
		} catch (IOException ioe) {
			log("e5", "From ServletCom CLIENT REQUEST:" + ioe);
		}

		// ------------------ read the SERVER RESPONSE
		try {
			inStream = new DataInputStream(conn.getInputStream());
			ret = "";
			String str = "";
			while ((str = inStream.readLine()) != null) {
				if(ret.length() > 0) ret += "\n";
				ret += str;
				log("w5", "Upload Server response: " + str, str);
			}
			inStream.close();
		} catch (IOException ioex) {
			log("e5", "IOException From (ServerResponse): " + ioex);
		}

		long endTime = System.nanoTime();
		log("w5", "postStringSync end [" + (endTime - startTime) + " ms]", null);

		return ret;
	}

	// =========================================================================
	public static boolean isWindows() {
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}

	// =========================================================================
	//
	// =========================================================================
	class JPaneValue {
		final Properties value = new Properties();
		JPaneValue() {

		}
		void add(String key, String val, String term) {
			JSONArray value;
			this.value.put("/" + term + "/" + key, val);
		}
		String get (String key, String term) {
			return (String) value.get("/" + term + "/" + key);
		}
		void clear(String term) {
			
		}
	};

	// =========================================================================
	static void log(String key, String log, String all) {
		if(log.startsWith("++++")) return;
		if(key.toLowerCase().startsWith("e")) System.err.println("--- " + log);
		if(key.toLowerCase().startsWith("i")) System.out.println("--- " + log);
		else if(flagLog == 1) System.out.println("--- " + log);
		
		if(key.toLowerCase().startsWith("e")
		 ||key.toLowerCase().startsWith("i")) {
			mainLog += log + "\n";
		}
	}

	static void log(String key, String log) {
		log(key, log, null);
	}

	// =========================================================================
	static String giraData(String data) {
		if(data.length() != 6) return data;
		data = data.substring(4, 6) + data.substring(2, 4) + data.substring(0, 2);
		return data;
	}

	// =========================================================================
	static String formatXml(String xml) {
		Writer out = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			DocumentBuilder db = dbf.newDocumentBuilder();

			org.w3c.dom.Document doc = db.parse(new InputSource(new StringReader(xml)));
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			out = new StringWriter();
			tf.transform(new DOMSource(doc), new StreamResult(out));
		} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}
	
	// =========================================================================
	static void writeTemp(String fileName, String data, String dir, boolean append) {
		fileName = fileName.replace(":", "_");
		fileName = fileName.replace("^", "_");
		fileName = fileName.replace("/", "_");
		if(dir == null) dir = "";
		try {
//			File baseDir = new File(System.getProperty("java.io.tmpdir"));
			File baseDir = new File(JPane.temp + dir);

			File temp = new File(baseDir, fileName);

			BufferedWriter out = new BufferedWriter(new FileWriter(temp, append));    
			out.write(data);
			out.close();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	// =========================================================================
	static String xmlEscText(String t) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < t.length(); i++){
			char c = t.charAt(i);
			switch(c) {
				case '<': sb.append("&lt;"); break;
				case '>': sb.append("&gt;"); break;
				case '\"': sb.append("&quot;"); break;
				case '&': sb.append("&amp;"); break;
				case '\'': sb.append("&apos;"); break;
				default:
					if(c > 0x7e || c < 32) {
						sb.append("&#"+((int)c)+";");
					} else sb.append(c);
			}
		}
		return sb.toString();
	}

	// =========================================================================
	static String vEnc(String para) {
		String ritorno = "";
//		para = para.replace("'", "");
		try {
			ritorno = URLEncoder.encode(para,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
/*		int len = para.length();
		for(int i=0; i<len; i++) {
			char c = para.charAt(i);
			if(c == '>') ritorno += "%11";
			else if(c == '<') ritorno += "%" +(c*1);
			else if(c == '\n') ritorno += "%" +(c*1);
			else if(c == '/') ritorno += "%" +(c*1);
			else if(c == '$') ritorno += "%" +(c*1);
			else if(c == '@') ritorno += "%"+(c*1);
			else if(c == ':') ritorno += "%"+(c*1);
			else if(c == ';') ritorno += "%"+(c*1);
			else if(c == '\'') ritorno += "%"+(c*1);
			else if(c == '"') ritorno += "%"+(c*1);
			else ritorno += c;
		} */
		return ritorno;
	}

	// =========================================================================
	//
	// =========================================================================
	public static byte[] hexStringToByteArray(String s) { 
		int len = s.length(); 
		byte[] data = new byte[len / 2]; 
		for(int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) 
								 + Character.digit(s.charAt(i+1), 16)); 
		}
		return data;
	}


	// =========================================================================
	//
	// =========================================================================
	public static String bytesToHex(byte[] bytes) { 
		final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9'
												,'A','B','C','D','E','F'}; 
		char[] hexChars = new char[bytes.length * 2]; 
		for(int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF; 
			hexChars[j * 2] = hexArray[v >>> 4]; 
			hexChars[j * 2 + 1] = hexArray[v & 0x0F]; 
		}
		return new String(hexChars); 
	}

	// =========================================================================
	//
	// =========================================================================
	public static String hexStringToString(String s) { 
		return new String(hexStringToByteArray(s));
	}

	// =========================================================================
	static String vDec(String para) {
		String ritorno = "";
//		para = para.replace("+", " ");
		if(!para.contains("%")) ritorno = para;
		else try {
				ritorno = java.net.URLDecoder.decode(para,"UTF-8");
			} catch (UnsupportedEncodingException
								| java.lang.IllegalArgumentException e) {
				log("e5", "vDec Error:" + para);
				e.printStackTrace();
			}
		return ritorno;
	}

	// =========================================================================
	//
	// =========================================================================
	String evalNew(String valore, int deci, JPaneDoc jp) {
		long startTime	= System.currentTimeMillis();
//		final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
//		ScriptEngine engine = factory.getScriptEngine(new String[] { "-scripting" });
		ScriptEngine engine = new ScriptEngineManager(null)
									.getEngineByName("nashorn"); // x gae
		
		String multi = "100";
		if(deci == 0) multi = "1";
		else if(deci == 1) multi = "10";
		else if(deci == 2) multi = "100";
		else if(deci == 3) multi = "1000";
		else if(deci == 4) multi = "10000";
		else if(deci == 5) multi = "100000";
		String ritorno = "";

		if(deci >= 0) valore = "(Math.round((" + valore + ")*"+multi+")/"+multi+")";

		try {
//			NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
//			ScriptEngine engine = factory.getScriptEngine(new String[] { "-scripting" });

			String valore2 = valore.replace("\"", "\\\"");
			engine.eval("function esegue(jp) {print(\"--- EvalNew Esegue:"
				+valore2+"\");/*\nprint(jp);*/\nreturn '' + " + valore + ";};");
			Invocable inv = (Invocable) engine;
			ritorno = (String) inv.invokeFunction("esegue", jp.jO.toString());
		} catch (ScriptException | NoSuchMethodException e) {
			JPane.log("w6", "Script error");
//			e.printStackTrace();
		}
		
		if(ritorno.equalsIgnoreCase("nan")) ritorno = "";

		long endTime = System.currentTimeMillis();
		JPane.log("w6"
				, "evalNew End [" + (endTime - startTime) + " ms]"
				+ "\n--- " + ritorno + "\n");

		return ritorno;
	}

	// =========================================================================
	static String execBaseHtml = "html";
	static String execBaseGetHtml() {return execBaseHtml;}
	// =========================================================================
	JPaneMaster execBase(JPaneDoc req, String cmd, int linePar, JPaneDoc pather
														,JPaneMaster master) {
		String custId	= "box";
		String restPath = "";
		int flagDefine	= 0;
		boolean debug 	= true;

		if(pather != null && pather.custId.length() > 0) pather.custId = custId;
		JPaneDoc jp = new JPaneDoc(pather).setCust(custId);

		long startTime	= jp.beginHelp("w5", "execBase:[" + cmd + "]");

		if(!cmd.contains("$")) return master;

		if(cmd.substring(0,cmd.indexOf("$")).contains("/")) {
			restPath = cmd.substring(0,cmd.indexOf("$"))
											.substring(0, cmd.lastIndexOf("/"));
			cmd = cmd.substring(cmd.substring(0,cmd.indexOf("$")).lastIndexOf("/") + 1);
			if(debug) jp.infoHelp("w5", "++++++ PARSER-REST: " + restPath);
			if(debug) jp.infoHelp("w5", "++++++ PARSER-CMD: " + cmd);
			custId = restPath.substring(restPath.lastIndexOf("/") + 1);
		}

		if(cmd.endsWith(";")) cmd = cmd.substring(0, cmd.length()-1);
		if(!cmd.endsWith("$end")) cmd += "$end";

		int pos = 0;
		int len = cmd.length();
		String token = "";
		String tmpPane = "";
		for(pos=0; pos<=len; pos++) {
			if(debug) jp.infoHelp("w5", "++++++ PARSER:"
										+ pos + "=" + cmd.substring(pos));
			if(cmd.charAt(pos) == '$') {
				if(cmd.substring(pos).equalsIgnoreCase("$end")) break;
				if(cmd.charAt(pos+1) == '$'
				 ||cmd.substring(pos).startsWith("$def:")
				 ||cmd.substring(pos).startsWith("$jpo:")) {
					if(cmd.substring(pos).startsWith("$jpo:")) pos += 3;
					if(cmd.substring(pos).startsWith("$def:")) {
						flagDefine = 1;
						pos += 3;
					}
					int line = 1;
					if(linePar > 0) line = linePar;
					token = cmd.substring(pos+2
						,cmd.substring(pos+2).indexOf("$")+pos+2);
					if(debug) jp.infoHelp("w5", "++++++ PARS:" + token);

					tmpPane = "";
					if(token.contains("::"))
						tmpPane = token.substring(0, token.indexOf("::"));
					else tmpPane = token;

					jp.req = req;
					JPaneDoc jpTmp = null;
					jpTmp = master.get(tmpPane, custId);
					if(jpTmp != null) jp = jpTmp;
					else {
						JPaneDoc patherPre = jp;
						jp = new JPaneDoc(pather).setCust(custId);
//						jp.init(tmpPane, pather).cloneDoc();
						jp.pather = patherPre;
						jp.req = pather;
					}
					jp.lineCurrent = line;
//					jp.setItem(tmpPane, "state", "linecur", ""+line, 1);

					if(token.contains("::")) {
						token += "::+";
						String tmpAss = "";

						int p = 0;
						int l = token.length();
						String tmpIdBase = "";
						while(true) {
							String tmpPane2 = "";
							int t1 = token.indexOf(",");
							int t2 = token.indexOf("::");
							if(t1 > 0 && t1 < t2) {
								tmpPane2 = token.substring(t1+1, t2);
								if(debug)
									jp.infoHelp("w5", "++++++ VIRGO:" + tmpPane2);
							}
							token = token.substring(token.indexOf("::")+ 2);
							if(debug) jp.infoHelp("w5", "++++++ LOOP:" + token);
							if(token.startsWith("+")) break;

							t1 = token.indexOf(",");
							t2 = token.indexOf("::");
							String tmpVal = "";
							String tmpAtt = "val";
							String tmpId = token.substring(0, token.indexOf(":"));
							if(token.indexOf("::") != token.indexOf(":")) {
								if(t1 > 0 && t1 < t2) t2 = t1;
								tmpVal = token.substring(token.indexOf(":")+1, t2);
							}

							if(debug) jp.infoHelp("w5", "++++++ PARAM:"
													+ tmpId + " - " + tmpVal);
							if(tmpId.startsWith("-")) {
								tmpId = tmpIdBase + tmpId;
								tmpAtt = tmpId.substring(tmpId.indexOf("-")+1);
							} else if(tmpId.contains("-")) {
								tmpIdBase = tmpId.substring(0,  tmpId.indexOf("-"));
								tmpAtt = tmpId.substring(tmpId.indexOf("-")+1);
							} else tmpIdBase = tmpId;

							if(tmpVal.equalsIgnoreCase("\"\"")) tmpVal = "";
							else if(tmpVal.startsWith("\""))
								tmpVal = tmpVal.substring(1, tmpVal.length()-1);

							if(debug) jp.infoHelp("w5", "++++++ PARAM_2:" + tmpId + " - " + tmpVal);
							if(tmpPane2.length()> 0
							 &&!tmpPane.equalsIgnoreCase(tmpPane2)) {
								if(debug) jp.infoHelp("w5", "++++++ ALIEN< " + tmpPane2 + "::" + tmpId + " - " + tmpVal);								
							} else {
								if(debug) jp.infoHelp("w5", "++++++ AGGIORNA ATTRIBUTO<" +tmpIdBase+"-"+tmpAtt +"> " + tmpPane2 + "::" + tmpId + ":" + tmpVal);
								if(tmpAtt.equalsIgnoreCase("val") &&(tmpVal.contains("|")||tmpVal.contains("%7C"))) {
									String tmpVal2 = vDec(tmpVal);
									if(debug) jp.infoHelp("w5", "++++++ AGGIORNA VETTORE<" +tmpIdBase+"-"+tmpAtt +"> " + tmpPane2 + "::" + tmpId + ":" + tmpVal2);
									String[] valEle = tmpVal2.split("[|]");
									for(int i=0;i<valEle.length;i++) {
										if(debug) jp.infoHelp("w5", "++++++ AGGIORNA VETTORE ELE:"+(i+1)+"<" +tmpIdBase+"-"+tmpAtt +"> " + tmpPane2 + "::" + tmpId + ":" + valEle[i]);
										jp.setItemJSONupdateAdd(tmpIdBase, tmpAtt, vEnc(valEle[i]), i+1);
									}
									jp.setItem(tmpPane, "state", "mode", "detail", 1);
									jp.setItem(tmpPane, "state", "linemax", ""+valEle.length, 1);
									jp.setItem(tmpPane, "state", "lineread", ""+valEle.length, 1);
								} else {
									if(flagDefine == 1) {
										jp.infoHelp("w5", "++++ SETITEM:" + tmpPane + "::" + tmpId + " val:" + tmpVal);
										jp.setItem(tmpPane, tmpId, "val", tmpVal, line);
									} else {
										int tmpLine = line;
										jp.execItem(master, tmpPane, tmpId, tmpVal, line, "@tab");
										if(line > 1) {
											jp.setItem(tmpPane, "state", "linecur", ""+line, 1);
											jp.setItem(tmpPane, "state", "linemax", ""+line, 1);
											jp.setItem(tmpPane, "state", "lineread", ""+line, 1);
										}
										if(tmpLine != line) if(debug) jp.infoHelp("w5", "++++++ CHANGE LINE:" + tmpLine + " -> " + line);
									}
								}
							}
						}
					}
// START ENGINE
/*					jp.reset(line);
					int mode = jp.getItem("state", "state", 0); // 0=new  1=init 2=read VA GESTITO LINEA x LINEA

					jp.infoHelp("w8", "== Esegue Lettura:" + mode + " line:" + line);
					long startTime2	= System.currentTimeMillis();

					if(mode < 2) {
						if(!jp.getItem("state", "glo_type", "").equalsIgnoreCase("x")
						 &&!jp.paneId.equalsIgnoreCase("login")) {
//							jp[iLoop].execItem(jp[iLoop+1], line, "", "", "@tab", line);
							int	rigCur	= jp.getItem("state", "linecur", 1);
							jp.infoHelp("w8", "FORZA_LINEA:" + line + " DIVENTA:" + rigCur);
//							jp.execItem(null, null, null, "@read");
							jp.execBaseNewItem(null, null, "@read", line);
						}
					}
					long endTime2 = System.currentTimeMillis();

					mode = jp.getItem("state", "state", 0); // 0=new  1=init 2=read
					jp.infoHelp("w8", "== Dopo Lettura:" + mode + " line:" + line + " [" + (endTime2 - startTime2) + " ms]");
					jp.writePane();	*/
// === FINE ENGINE
					master.put(tmpPane, jp, custId);
					pos = cmd.substring(pos+2).indexOf("$")+pos+1;
					continue;
				}
				if(cmd.substring(pos).startsWith("$csv")) {
					System.out.println("--- Legge Tabella iva ...");
					pos = cmd.substring(pos+1).indexOf("$")+pos;
					new READ_XLSX(temp + "Codifiche-103.csv", jp);

					continue;
				}
				if(cmd.substring(pos).startsWith("$read")) {
					pos = cmd.substring(pos+1).indexOf("$")+pos;

					JPaneDoc jpCust = master.get("CUST", "");
					String tmpUsr = jpCust.getItem("ix", "usr", "val", "", 1);
					String tmpPwd = jpCust.getItem("ix", "pwd", "val", "", 1);
					codGioVen = jpCust.getItem("ix", "vendite", "val", "", 1);
					String tmpSvr = vDec(jpCust.getItem("ix", "svr", "val", "", 1));
					tmpSvr = tmpSvr.replace("_", ":");
					log("i5", "usr:" + tmpUsr + " pwd:" + tmpPwd + " svr:" + tmpSvr + " gio:" + codGioVen + " jpo:" + tmpPane);

					if(tmpPane.equalsIgnoreCase("NUM")) {
						sql.select(jp, tmpUsr, tmpPwd, tmpSvr, 1, tmpPane, master);

						String tmp =
								"min(CGIVAT.NUM_MOV) NUM_x002\n" +
								"from CGIVAT\n" +
								" where CGIVAT.ESERCIZIO = '" + esercizio + "'\n" +
								" and CGIVAT.COD_GIORNALE = '" + codGioVen + "'\n" +
								" and CGIVAT.DATA_DOC > '181200'\n"
								;
						sql.sqlTxt = sql.sqlTxt.replace("from \n", "");
						sql.sqlTxt = sql.sqlTxt.replace("order by 1", tmp);
						sql.execSQL(jp, null, master, false);
//						System.out.println(jp.jO.toString(4));

						continue;
					}

					
					sql.select(jp, tmpUsr, tmpPwd, tmpSvr, 1, tmpPane, master).execSQL(jp, null, master, false);
//					sql.select(jp, "GLOBE", "MANAGER", "217.133.32.75:1521:XE", 1, tmpPane, master).execSQL(jp, null, master);
//					sql.select(jp, "GLOBE", "MANAGER", "93.145.178.222:1521:XE", 1, tmpPane, master).execSQL(jp, null, master);
//					sql.select(jp, "GLOBE", "MANAGER", "93.43.22.2:1522:XE", 1, tmpPane, master).execSQL(jp, null, master);
//					sql.select(jp, "PETRABG", "manager", "93.146.156.215:1521:ORCL", 1, tmpPane, master).execSQL(jp, null, master);

					continue;
				}
				if(cmd.substring(pos).startsWith("$sql")) {
					String name = cmd.substring(pos+1);
					name = name.substring(name.indexOf(":")+1);
					name = name.substring(0, name.indexOf("$"));

					pos = cmd.substring(pos+1).indexOf("$")+pos;

					JPaneDoc jpCust = master.get("CUST", "");
					String tmpUsr = jpCust.getItem("ix", "usr", "val", "", 1);
					String tmpPwd = jpCust.getItem("ix", "pwd", "val", "", 1);
					codGioVen = jpCust.getItem("ix", "vendite", "val", "", 1);
					String tmpSvr = vDec(jpCust.getItem("ix", "svr", "val", "", 1));
					tmpSvr = tmpSvr.replace("_", ":");

					log("i5", "SQL:" + name + " usr:" + tmpUsr + " pwd:" + tmpPwd + " svr:" + tmpSvr + " gio:" + codGioVen);

					String sqlText = getResource(name);
					sqlText = sqlText.replace("@FIVA03.COD_DITTA", "01");
					sqlText = sqlText.replace("@FIVA03.ESERCIZIO", "19");
					sqlText = sqlText.replace("@FIVA03.ESERCIZIO", "19");
					sqlText = sqlText.replace("@FIVA03.A_PERIODO", "03");
					sqlText = "REM CON "+ tmpUsr + "/" + tmpPwd + "@" + tmpSvr + ";"
							+ "-- jdbc:oracle:thin:hr/hr@" + tmpSvr + "\n"
							+ "-- ALTER SESSION SET NLS_NUMERIC_CHARACTERS='.,';\n"
							+ "-- ALTER SESSION SET NLS_DATE_FORMAT='YYMMDD';\n"
							+ sqlText
							;
					sql.execSQL(jp, sqlText, master, true);

//					sql.select(jp, "GLOBE", "MANAGER", "217.133.32.75:1521:XE", 1, tmpPane, master).execSQL(jp, null, master);
//					sql.select(jp, "GLOBE", "MANAGER", "93.145.178.222:1521:XE", 1, tmpPane, master).execSQL(jp, null, master);
//					sql.select(jp, "GLOBE", "MANAGER", "93.43.22.2:1522:XE", 1, tmpPane, master).execSQL(jp, null, master);
//					sql.select(jp, "PETRABG", "manager", "93.146.156.215:1521:ORCL", 1, tmpPane, master).execSQL(jp, null, master);

					continue;
				}
				if (cmd.substring(pos).startsWith("$dir")) {
					pos = cmd.substring(pos+1).indexOf("$")+pos;
					String dir = temp
							+ tempIn;
//							+ "FattNew/in";
					String type = "file";
					String filter = "";
					int jLine = 0;
					File f = new File(dir);
					File[] allSubFiles = f.listFiles();
					for (File file : allSubFiles) {
						{
							String val = file.getName().toString();
							String name = file.getAbsolutePath().toString();
							String date = new SimpleDateFormat("yyyyMMDDHHmmss").format(
									new Date(file.lastModified()));
							String size = "" + file.length();

							val = val.replace(".XML", ".xml");
							if(val.endsWith(".xml")) type = "xml";
							if(val.endsWith(".html")) type = "html";
							if(file.isDirectory()) type = "dir";

							if(!val.endsWith(".xml")) continue;

//							System.out.println(file.getAbsolutePath());
							jLine++;
							String cmd2 = "$$DIR::x001:" + JPane.vEnc(dir)
											+ "::x002:" + JPane.vEnc(type)
											+ "::x003:" + JPane.vEnc(name)
											+ "::x004:" + JPane.vEnc(val)
											+ "::x005:" + JPane.vEnc(date)
											+ "::x006:" + JPane.vEnc(size)
											;
							master = execBase(jp, cmd2, jLine, jp, master);

							log("w5", file.getName().toString());
						}
					}
					continue;
				}
				if(cmd.substring(pos).startsWith("$sort")) {
					String name = cmd.substring(pos+1);
					name = name.substring(name.indexOf(":")+1);
					name = name.substring(0, name.indexOf("$"));
					pos = cmd.substring(pos+1).indexOf("$")+pos;
					
					log("w5", "XXXXXXXXX SORT:" + name);
					continue;
				}
				if(cmd.substring(pos).startsWith("$loop")) {
					String name = cmd.substring(pos+1);
					name = name.substring(name.indexOf(":")+1);
					name = name.substring(0, name.indexOf("$"));
					pos = cmd.substring(pos+1).indexOf("$")+pos;
					
					log("e5", "XXXXXXXXX LOOP:" + name);
					log("e5", "XXXXXXXXX LOOP:" + JPane.vDec(name));
					
					
					continue;
				}
				if(cmd.substring(pos).startsWith("$readfattura")) {
					pos = cmd.substring(pos+1).indexOf("$")+pos;
					log("e5", "XXXXXXXXX " + "Legge Fattura");
					continue;
				}
				if(cmd.substring(pos).startsWith("$write")) {
					pos = cmd.substring(pos+1).indexOf("$")+pos;
					continue;
				}
				if(cmd.substring(pos).startsWith("$html")) {
					execBaseHtml = "Trovato TOKEN";
					
					String pageName = "main/blue.html";
					String tmpRend = jp.getItem("id", "view", "val", "view", 1);
					if(tmpRend.equalsIgnoreCase("grid")) pageName = "main/jamMenu.html";

//					pageHtml html = jp.new pageHtml(pageName);
//					String tmpView = "edit";
//					jp.execHtmlRend(null, html, null, tmpView, cmd, null, tmpRend);
//					execBaseHtml = html.get();

					pos = cmd.substring(pos+1).indexOf("$")+pos;
					continue;
				}
				if(cmd.substring(pos).startsWith("$end")) {
					break;
				}
			}
		}

		jp.writePane();
		jp.endHelp("w9", "execBase End", startTime);

		return master;
	}

	// =========================================================================
	//
	// =========================================================================
	class JPaneLoop {
		JSONObject paneJSON 			= null;
		private String sort 			= "sort-pos";
		private int mode				= 0;
		private int line				= 1;
		private JSONArray columns		= null;
		private int iCmp				= 0;
		private JSONObject column		= null;
		private String itemLast			= "";
		private JSONObject columnNext	= null;;

		private class ElementNameComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				String s1 = ((JSONObject)o1).getString("id");
				String s2 = ((JSONObject)o2).getString("id");
				// (A real implementation might use java.text)
				return s1.compareTo(s2);
			}
		}

		private class ElementPositionComparator implements Comparator {
			public int compare(Object o1, Object o2) {
				JSONObject ele = null;

				String s1 = "000";
				try { s1 = ((JSONObject)o1).getString("ver");
				} catch (JSONException e) {/* e.printStackTrace(); */}
				s1 = "" + (Integer.valueOf(s1) + 100);

				String s1o = "000";
				try {s1o = ((JSONObject)o1).getString("ori");
				} catch (JSONException e) {/* e.printStackTrace(); */}
				s1 = s1 + (Integer.valueOf(s1o) + 100);

				String s2 = "000";
				try {s2 = ((JSONObject)o2).getString("ver");
				} catch (JSONException e) {/* e.printStackTrace(); */}
				s2 = "" + (Integer.valueOf(s2) + 100);

				String s2o = "000";
				try {s2o = ((JSONObject)o2).getString("ori");
				} catch (JSONException e) {/* e.printStackTrace(); */}
				s2 = s2 + (Integer.valueOf(s2o) + 100);
				
//				System.err.println("--- Compare: " + s1 + " =  " + s2 + "...");

				return s1.compareTo(s2);
			}
		}

		// =========================================================================
		public JSONArray sortJSON(JSONArray array, Comparator c) {
//			System.err.println("--- sortJSON: "  + array.length() + "...");

			if(array.length() <= 1) return array;
			
			List    asList = new ArrayList(array.length());
			for(int i=0; i<array.length(); i++){
				asList.add(array.opt(i));
			}
			Collections.sort(asList, c);
			JSONArray  res = new JSONArray();
			for(Object o : asList){
				res.put(o);
			}
			return res;
		}

		JPaneLoop(JPaneDoc jp, String type, String sortPara) {
			paneJSON = jp.jO;
			if(sortPara != null && sortPara.length()>0) sort  = sortPara;

			if(type == null) type = "";
			else if(type.equalsIgnoreCase("sql")) mode  = 1;

			this.reset(1);
		}

		// =========================================================================
		//
		// =========================================================================
		public int reset(int line) {
			if(line == 0) line = 1;
			this.line = line;
			
			if(paneJSON == null) return iCmp; // Tappullo provvisorio

			if(line > 0) {
				try {
					JSONObject pane = null;
					pane = paneJSON.getJSONObject("pane");

					try {
						columns = pane.getJSONArray("items");
					} catch (JSONException e2) {
						e2.printStackTrace();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}; 

				if(sort != null && sort.equalsIgnoreCase("sort-pos")) {
					columns = sortJSON(columns, new ElementPositionComparator());
				} else
				if(sort != null && sort.equalsIgnoreCase("sort-name")) {
//					columns = sortJSON(columns, new ElementNameComparator());
				}
			}
			
			iCmp = 0;
			return iCmp;
		}

		// =========================================================================
		//
		// =========================================================================
		int loop() {return loop(0);}
		int loop(int flag) {
//			jPaneCloud.log("w5-c", "loop:" + iCmp + " flag:" + flag);
			if(flag > 0) iCmp = flag -1;
			if(columns == null) return 0; // TAppullo Provvisorio

			while(true) {
				iCmp++;
				if(iCmp > columns.length()) {
					iCmp = 0;
					break;
				} else {
					column = columns.getJSONObject(iCmp-1);
					itemLast = column.getString("id");

					if(iCmp < columns.length())
						columnNext = columns.getJSONObject(iCmp);
				}
				break;
			}
			return iCmp;
		}

		// =========================================================================
		//
		// =========================================================================
		String get(String att, String def) {return get(column, att, def);}
		String get(JSONObject column, String att, String def) {
//			System.err.print("--- Get:"+ attr + " ");
			if(att.equalsIgnoreCase("id")) {
//				System.err.println("--- Val:" + column.getName());
				if(column != null) return column.getString("id");
				else return "x001";
			} else {
				try {
					if(att.equalsIgnoreCase("val"))
						def = column.getJSONArray("values").getJSONObject(line-1)
															.getString(att);
					else if(column != null) def = column.getString(att);
				} catch (JSONException | NullPointerException e) {}

//				System.out.println("++++ Val:" + def + " att:" + att);
				return def;
			}
		}

		int NEXT_ITEM = 1;
		int NEXT_LINE = 2;
		int VALUE_MAX = 3;
		int VALUE_DIF = 4;

		// =========================================================================
		String get(int mode, String attr, String def) {
			if(mode == NEXT_ITEM) {
				if(iCmp >= columns.length()) return def;
				else return get(columnNext, attr, def);
			}

	/*		if(mode == NEXT_LINE) {
				Element rig = null;
				Element ele = null;
				rig = paneEle.getChild("line_" + line + 1);
				if(rig == null) return def;

				ele = rig.getChild(itemLast);
				Element tmp1 = null;
				Element tmp2 = null;
				if(ele != null) tmp1 = ele.getChild("jp_" + attr);
				if(tmp1 != null) {
					tmp2 = tmp1.getChild("jp_txt");
					if(tmp2 != null) {
//						System.err.println("--- Val:" + tmp2.getTextNormalize());
						return tmp2.getTextNormalize();
					}
				}
			}
	*/
			return def;
		}

		// =========================================================================
		int get(int mode, String attr, int def) {
			String temp = get(mode, attr, ""+def);
			if(temp.length() == 0) temp = "0";
			int tmp = 0;
			try {tmp = Integer.parseInt(temp);
			} catch (NumberFormatException e){tmp = 0; 
			log("e5", "JPaneLoop.get:"
					+ temp + ":" + tmp + " attr:" + attr + " parseIntError");
			e.printStackTrace();}
			return tmp;
		}

		// =========================================================================
//		String get(int mode, String attr, String def, String filter, String value) {
//			return def;
//		}

		// =========================================================================
		int get(String attr, int def) {
			String temp = get(attr, ""+def);
			if(temp.length() == 0) temp = "0";
			int tmp = 0;
			try {tmp = Integer.parseInt(temp);
			} catch (NumberFormatException e){tmp = 0; 
			log("e5", "jPaneLoop.get:"
								+ temp + " attr:" + attr + " parseIntError");
			e.printStackTrace();}
			return tmp;
		}
	}

	// =========================================================================
	//
	// =========================================================================
	public class JPaneDoc {
		public int lineCurrent	=	0;
		public String custId	=	"";
		public JPaneDoc req		= null;
		public JPaneDoc pather	= null;

		JSONObject jO			= null;
		private String lastDate	= null;
		private String paneGroup= null;
		private String lastGroup= null;
		private String lastCust	= null;
		private String lastUser	= null;
		private String lastZone	= null;
		private String lastLang	= null;
		private String lastEnco	= null;
		private String paneDate	= null;
		private String paneCust	= null;
		private String paneUser	= null;
		private String paneZone	= null;
		private String paneLang	= null;
		private String paneEnco	= null;

		JPaneJdbc jd			= null;
		String sqlText			= null;

		public JPaneDoc(String paneId, String key) {
			jO = JPaneDocNew(paneId);
		}

		public JPaneDoc(JPaneDoc pather) {
			this.pather = pather;
		}

		public void writePane() {
		}

		String getItem(String k, String item, String key, String def, int line) {
			return getItemJSON(k, item, key, def, line);
		}

		int getItem(String k, String item, String key, int def, int line) {
			return Integer.parseInt(getItem(k, item, key, ""+def, line));
		}

		String getItemId(String id, String key, String def, int line) {
			return getItem("id", id, key, def, line);
		}

		String getItemIx(String ix, String key, String def, int line) {
			return getItem("ix", ix, key, def, line);
		}

		String getItemIs(String is, String key, String def, int line) {
			return getItem("is", is, key, def, line);
		}

		// =========================================================================
		String getIx(String ixPara, String def) {
			return getItemIx(ixPara, "val", def, 1);
		}

		// =========================================================================
		//
		// =========================================================================
		public String getItemJSON(String keyName, String key, String attPara
													,String def, int line) {
			String value = def;
			if(line <= 0) line = 1;
//			System.out.println("--- getItemJSON::key:" + keyName + " key:" + key + " att:" + attPara);

			if(jO == null) return null; // jPaneDocNewJSON(null);

			if(key.equalsIgnoreCase("state")) {
				try {
					JSONObject pane = jO.getJSONObject("pane");; 
					JSONObject state = pane.getJSONObject(key);
					value = state.getString(attPara);
				} catch (JSONException e) {/* e.printStackTrace(); */}
			} else
			if(key != null && key.equalsIgnoreCase("header")) {
				try {
					JSONObject pane = jO.getJSONObject("pane");; 
					JSONObject header = pane.getJSONObject(key);
					value = header.getString(attPara);
				} catch (JSONException e) {/* e.printStackTrace(); */}
			} else {
				try {
					JSONObject pane = jO.getJSONObject("pane");;
					JSONArray array = pane.getJSONArray("items");
				    JSONObject obj = null;

					for(int i = 0; i < array.length(); i++) {
					    obj = array.getJSONObject(i);

						String itId = null;
						try {
							itId = obj.getString(keyName);
						} catch (JSONException e) { /* e.printStackTrace(); */}

						if(itId != null && itId.equalsIgnoreCase(key)) {
							if(attPara.equalsIgnoreCase("val")) {
								try {
									value = obj.getJSONArray("values")
										.getJSONObject(line-1).getString(attPara);
									if(value.length() == 0) value = def;
									if(value.equalsIgnoreCase("null")) value = def;
//									log("i5", "GETITEMJSON:" + itId + " line:" + line + " value:" + value + "\n");
								} catch (JSONException e) {
									log("w5", "GETITEMJSON:" + keyName + ":" + key 
										+ " line:" + line + " def:" + value + "\n");
//									e.printStackTrace();
								}
							} else value = obj.getString(attPara);

							break;
						}
					}
				} catch (JSONException e) {/* e.printStackTrace(); */}
			}
//			log("i5", "getItemJSON::val:" + value);
			return value;
		}

		// =========================================================================
		String fmtId(int idNum) {
			String tmp = "" + (1000 + idNum);
			return "x" + tmp.substring(1);
		}

		// =========================================================================
		String incItem(String id) {

			int i = 0;
			if(id == null || id.length() == 0) i = 0;
			else i = Integer.parseInt(id.substring(1));

			return fmtId(i + 1);
		}

		// =========================================================================
		String lastItem() {
			JSONObject pane;
			String last = "";
			try {
				pane = jO.getJSONObject("pane");
				JSONArray array = pane.getJSONArray("items");

			    JSONObject obj = null;
				for(int i = 0; i < array.length(); i++) {
				    obj = array.getJSONObject(i);
					last = obj.getString("id");
				}
			} catch (JSONException e) {
				log("e5", "JSON_ERROR:"); 
//				e.printStackTrace();
			};

			return last;
		}

		// =========================================================================
		JSONObject existItem(String nameId, String id) {
			JSONObject pane;
			if(nameId == null) nameId = "id";

			try {
				pane = jO.getJSONObject("pane");
				JSONArray array = pane.getJSONArray("items");

			    JSONObject obj = null;
				for(int i = 0; i < array.length(); i++) {
				    obj = array.getJSONObject(i);
					String itId = obj.getString(nameId);

					if(itId.equalsIgnoreCase(id)) {
						return obj;
					}
				}
			} catch (JSONException e) {
				log("e5", "JSON_ERROR:"); 
//				e.printStackTrace();
			};

			return null;
		}

		// =========================================================================
		JSONObject setItemJSONupdateAdd(String id, String att, String val, int linePar) {
//			JPane.log("w6", "SETITEMJSONUPDATE:" + id + " att:" + att
//									+ " val:" + val + " line:" + linePar);
		    JSONObject obj = null;
			int itemExist = 0;

			String value2 = "";
			if(id.contains("-")) {
				att = id.substring(id.indexOf("-")+1);
				if(id.indexOf("-") > 0)
					id = id.substring(0, id.indexOf("-"));
			}
//			JPane.log("w6", "SETITEMJSONUPDATE:" + id + " att:" + att
//					+ " val:" + val + " line:" + linePar);

			if(att.equalsIgnoreCase("val")) value2 = val;

			JSONObject pane;
//			JSONObject lines;

			if(jO == null) jO = JPaneDocNew("xxx");
//				return 0; // Tappullo Provvisorio

			try {
				pane = jO.getJSONObject("pane");
				JSONArray array = pane.getJSONArray("items");

				for(int i = 0; i < array.length(); i++) {
				    obj = array.getJSONObject(i);
					String itId = obj.getString("id");

					if(itId.equalsIgnoreCase(id)) {
						obj.put(att, val);

						if(att.equalsIgnoreCase("val")) {
							setItemJSONupdateVal(obj, id, att, val, linePar);

	/*						JSONArray output = obj.getJSONArray("values");
							for(int j=0;j<output.length();j++) {
								JSONObject ob = output.getJSONObject(j);
								System.err.println("--- val:" + ob.get("val"));
							}
	*/
						}
//						System.err.println("--- JSON_EXIST:" + array.length() + "\n"
//								+ paneJSON.toString());
						itemExist = 1;
						break;
					}
				}

				if(itemExist == 0) {
					array.put(new JSONObject()
						.put("id", id)
						.put(att, val)
						.put("values", new JSONArray()
							.put(new JSONObject()
									.put("val", value2).put("line", linePar))
							)
						);
//					System.err.println("--- JSON_NON_EXIST:" + array.length() + "\n"
//						+ paneJSON.toString());
				}
			} catch (JSONException e) {
				log("e5", "JSON_ERROR:"); 
//				e.printStackTrace();
			};

			return obj;
		}

		// =====================================================================
		JPaneDoc setItemJSONupdateVal(JSONObject obj, String id, String att
												,String value, int linePar) {
			if(linePar == 0) linePar = 1;

//			System.err.println("--- JSON_VAL:" + id + ":" + linePar + " [" + value + "]"); 

			if(id != null && id.equalsIgnoreCase("pane")) {
				try {
					JSONObject pane = obj.getJSONObject("pane");; 
					pane.put(att, value);
				} catch (JSONException e) {e.printStackTrace();}
//				System.out.println("--- JSONSET:" +att+ "\n" + paneJSON.toString());
				return this;
			}

			if(id != null && id.equalsIgnoreCase("state")) {
				try {
					JSONObject pane = obj.getJSONObject("pane");; 
					JSONObject state = pane.getJSONObject("state");
					state.put(att, value);
				} catch (JSONException e) {e.printStackTrace();}
//				System.out.println("--- JSONSET:" +att+ "\n" + paneJSON.toString());
				return this;
			}

			if(id != null && id.equalsIgnoreCase("header")) {
				try {
					JSONObject pane = obj.getJSONObject("pane");; 
					JSONObject header = pane.getJSONObject("header");
					header.put(att, value);
				} catch (JSONException e) {e.printStackTrace();}
//				System.out.println("--- JSONSET:" +att+ "\n" + paneJSON.toString());
				return this;
			}

//			if(id != null) lastItemId = id;
			
			JSONArray output = null;
			try {
				output = obj.getJSONArray("values");
			} catch (JSONException e) {
				log("e5", "JSON value not found"); 
			}

			if(output == null) {
				obj.put("values", new JSONArray()
					.put(new JSONObject().put("val", value).put("line", linePar))
				);
			}

			output = obj.getJSONArray("values");
			int outLen = output.length();
//			System.err.println("--- len:" + outLen);

			if(outLen <=(linePar-1)) {
				output.put(new JSONObject().put("val", ""));
				outLen = output.length();
			}

			if(outLen >(linePar-1)) {
				output.getJSONObject(linePar-1).put("val", value).put("line", linePar);
			} else
				log("e5", "JSON_ERRORE_VET:" + outLen
							+ " LINE_PARA:" + linePar + " VAL:" + value);

	/*		for(int i=0;i<output.length();i++){
				JSONObject ob = output.getJSONObject(i);
				System.err.println("--- Json: "+i+" val:" + ob.get("val"));
			} */

			return this;
		}

		// =========================================================================
		// Processa Item
		// =========================================================================
		public JSONObject execItem(JPaneMaster master, String paneId, String id
									,String val, int line, String terminator) {
			String tmpToken = "val";
			if(line == -1) line = this.lineCurrent;
			if(jO == null) jO = JPaneDocNew(paneId); // tappullo

			if(id.contains("-")) {
				tmpToken = id.substring(id.indexOf("-")+1);
				id = id.substring(0, id.indexOf("-"));
			}

			JSONObject obj	= setItem(paneId, id, tmpToken, val, line);
/*			Item item		= master.new Item();
			if(obj != null) {
				item.paneId	= paneId;
				item.id		= id;
				item.val	= val;
				item.ix		= obj.getString("ix");
				master.var.add(id, item);
			} */
			return obj;
		}

		// =========================================================================
		JSONObject setItem(String pane, String id, String att, String val, int line) {
			if(jO == null) jO = JPaneDocNew(pane);
			if(line == -1) line = this.lineCurrent;

			if(id.contains("-")) {
				att = id.substring(id.indexOf("-")+1);
				if(id.indexOf("-") > 0)
					id = id.substring(0, id.indexOf("-"));
			}

			if(att.contains("-")) {
				att = att.substring(att.indexOf("-")+1);
				if(att.indexOf("-") > 0)
					id = att.substring(0, att.indexOf("-"));
			}

			if(id.equalsIgnoreCase("pane99")
			 ||id.equalsIgnoreCase("state")
			 ||id.equalsIgnoreCase("header"))
				setItemJSONupdateVal(this.jO, id, att, val, line);
			else return setItemJSONupdateAdd(id, att, val, line);

			return null;
		}

		// =========================================================================
		public JPaneDoc setCust(String custId) {
			this.custId = custId;
			return this;
		}

		JPaneDoc(String json) {
			jO = new JSONObject(json);			
		}

		// =========================================================================
		public void infoHelp(String flag, String help) {
			JPane.log(flag, help);			
		}

		// =========================================================================
		long beginHelp(String flag, String help) {
			JPane.log(flag, help);
			return System.nanoTime();
		}

		// =========================================================================
		void endHelp(String flag, String help, long startTime) {
			long endTime =  System.nanoTime();
			help += " [" + ((endTime - startTime)/1000000) + " ms]";
			JPane.log(flag, help);
		}

		// =========================================================================
		JSONObject JPaneDocNew(String paneId) {
			paneDate = lastDate;
			paneGroup = lastGroup;
			paneCust = lastCust;
			paneUser = lastUser;
			paneZone = lastZone;
			paneLang = lastLang;
			paneEnco = lastEnco;

			try {
				jO = new JSONObject()
					.put("pane", new JSONObject()
						.put("paneid", paneId)
						.put("sid", sid)
						.put("date", lastDate)
						.put("group", lastGroup)
						.put("cust", lastCust)
						.put("user", lastUser)
						.put("lang", lastLang)
						.put("encode", lastEnco)
						.put("state", new JSONObject()
							.put("mode"		,"master")
							.put("state"	,"0")
							.put("linemax"	,"1")
							.put("lineread"	,"0")
//							.put("linenew"	,"0")
//							.put("linelast"	,"0")
							.put("linecur"	,"1")
							.put("itemcur", ""))
						.put("header", new JSONObject()
							.put("title", "Documento")
							)

						.put("items", new JSONArray()
							.put(new JSONObject()
								.put("id", "x000")
								.put("tit","Flag")
								.put("inp","n")
								.put("val", "0")

								.put("values", new JSONArray()
									.put(new JSONObject().put("line", "1")
														.put("val", "0"))
								)
							))
						)
						;

//				System.out.println("--- JSON:\n" + paneJSON.toString());
			} catch (JSONException e2) {
				e2.printStackTrace();
			}

			return jO;
		}

	};

	// =========================================================================
	//
	// =========================================================================
	static String getDate() {
		String data = "";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		data = formatter.format(new Date());

		return data;
	}

	// =========================================================================
	class JPaneMaster {
		class Item {
			String paneId;
			String type;
			String id;
			String ix;
			String val;
		}
		class Var {
			Properties varStore = new Properties();

			void add(String key, Item item) {
				varStore.put(key, item);
			}
			Item get(String key) {
				return (Item) varStore.get(key);
			}
		}

		// =====================================================================
		final Properties prop = new Properties();
		String tmpTerm	= "77";
		Var var = new Var();
		JPaneMaster(String tmpTerm) {
			this.tmpTerm = tmpTerm;
		}
		JPaneDoc get(String paneId, String custId) {
			paneId = "/" + tmpTerm + "/" + paneId;
			log("W5", "+++ GET:" + paneId + " = " + prop.get(paneId));
			return (JPaneDoc) prop.get(paneId);
		}
		void put(String paneId, JPaneDoc jp, String custId) {
			paneId = "/" + tmpTerm + "/" + paneId;
			log("W5", "+++ PUT:" + paneId + " = " + (jp));
			prop.put(paneId, jp);
			log("W5", "+++ PUT:" + paneId + " = " + prop.get(paneId));
		}
		void setTerm(String tmpTerm) {this.tmpTerm = tmpTerm;}
		String list(int tab) {
			String tmpJson = "";
			log("W5","List Pane:");
			Enumeration e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				JPaneDoc jp = (JPaneDoc)prop.get(key);

//				System.out.println("--- " + key + " = " + jp);
				if(jp.jO != null) {
					if(tab <= 0) System.out.println(jp.jO.toString());
					else System.out.println(jp.jO.toString(4));
					tmpJson += jp.jO.toString() + "\n";
				}
			}
			writeTemp("jam_" + getDate() + ".json", tmpJson, "jam", false);
			return tmpJson;
		}
		String listSort(int tab) {
			String tmpJson = "";
			log("W5", "List Sort Pane:");
			SortedMap sp = new TreeMap(prop);
			Set keySet = sp.keySet();
			Iterator iterator = keySet.iterator();
			
			String inizio = "{";
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				JPaneDoc jp = (JPaneDoc)prop.get(key);

//				System.out.println("--- " + key + " = " + jp);
				if(jp.jO != null) {
					if(tab <= 0) {System.out.println(jp.jO.toString());}
					else System.out.println(jp.jO.toString(4));
					tmpJson += inizio + "\"" + key + "\":" + jp.jO.toString() + "\n";
					inizio = ",";
				}
			}
			tmpJson += "}";
			tmpJson = tmpJson.replaceAll("/99/", "");
			writeTemp("jam_" + getDate() + ".json", tmpJson, "jam", false);
			return tmpJson;
		}
	}
	
	int nuovo = 0;
	// =========================================================================
	//
	// =========================================================================
	String jPaneGo(String json, String paraCmd) {
		JPaneDoc jp = new JPaneDoc(json);

		String ritorno	= "<pre>\npane:fmvx\n";
		String cmd		= "";
		String value	= jp.getItem("id", "val", "val", "", 1);
		String termi	= jp.getItem("id", "terminator", "val", "", 1);
		String path		= "$$login";
		String suffix	= ".html";

		ritorno += jp.jO.toString();

		if(paraCmd.contains("/")) path = paraCmd.substring(paraCmd.lastIndexOf("/")+1);
		if(path.contains(".")) {suffix = path.substring(path.indexOf("."));path = path.substring(0, path.indexOf("."));}
		if(!path.contains("$$") && path.startsWith("$z")) {
			path = path.substring(path.indexOf("$")+2);
			path = hexStringToString(path);
		}

//		if(!path.startsWith("$")) path="$$login$$fmvx";

		JPane.log("w5", "=====================================================================");
		long startTime	= jp.beginHelp("w9", "paneFmvx:" + paraCmd + " term: + terminal");
		JPane.log("w5", "java:" + System.getProperty("java.version") + " jPane:" + VERSION);
		JPane.log("w5", "time:" + getDate());
		JPane.log("w5", "cmd :" + paraCmd);
		JPane.log("w5", "term:" + terminal);
		JPane.log("w5", "path:" + path);
		JPane.log("w5", "value:" + value);
		JPane.log("w5", "termi:" + termi);
		JPane.log("w5", "suffix:" + suffix);

		JPaneMaster master = new JPaneMaster(terminal);
//		master = jp.execJP(jp, master, name);

		cmd		= "$mac:main:$$login$$main$$fmvx";
		master = JPaneBase.paneBASE(this, jp, master, terminal, 1);

		JPaneValue valore = new JPaneValue();
		if(nuovo == 0) valore.clear(terminal);
		nuovo++;

/*		cmd 	= "$$FMVC::x001:01";
		master = jp.execBaseNew(cmd, jp, master);
		JPaneDoc jpTes = master.get("FMVC", "");
		System.out.println(jpTes.paneJSON.toString(4));
*/
		ritorno += "--- " + evalNew("100/3", 2, jp);
		ritorno += "\n--- " + evalNew("'Piero'", 2, jp);
		ritorno += "\n--- " + evalNew("`pwd`", -1, jp);
		ritorno += "\n--- " + evalNew("\"parametro jp:${jp}\"", -1, jp);
		ritorno += "</pre>";

		ritorno = "";

		if(suffix.equalsIgnoreCase(".json")) {
			String jsonTxt = "";
			String page = "/view17/view17/jamfive/$$login$$fmvx?term=" + terminal;
			jsonTxt += "window.location.href = '" + page + "';\n";
			JPane.log("w9","=== json\n" + jsonTxt);
			return jsonTxt;
		}

		Properties param = new Properties();;

//		ritorno += paneViewRend(master, "jam01_formato", path, "jam01_formato", 1, param);
//		ritorno += paneViewRend(master, "fmvx", terminal, path, "jam001", "fmvx", 1, param);
		ritorno += paneViewRend(master, "tbxl", "jam001", 1, param);

		ritorno += "<pre>";
		ritorno += paneViewRend(master, "tbxl", "jam001", 10, param);
//		ritorno += paneViewRend(master, "fmvx_det", terminal, path, "jam001", "fmvx", 10, param);
		ritorno += "</pre>";

		String consoleVal = "";
		ritorno += console(consoleVal, "--- JamFive Ver:" +  VERSION);

		jp.endHelp("w5", "paneFmvx End", startTime);
		return ritorno;
	}

	// =========================================================================
/*	String viewItem(String name, String val) {
		String ritorno = "";
		val = val.replace("'", "");
		if(val != null && val.length() > 0) ritorno = name + "'" + val + "'";
		return ritorno;
	} */

	// =========================================================================
	String console(String valore, String out) {
		String ritorno = "";
		ritorno 
			+="<link href='https://deviouschimp.co.uk/build/css/main-56be79d6c8.css' rel='stylesheet' type='text/css'>\n"
			+ "<div id='cmd1'></div>\n"
			+ "<script type='text/javascript' src='https://ajax.googleapis.com/ajax/libs/jquery/2.0.3/jquery.min.js'></script>\n"
			+ "<script type='text/javascript' src='/view17/app/cmd-line/js/cmd.js'></script>\n"
			+ "<script type='text/javascript'>\n"
			+ "var interface1 = new Cmd({\n"
			+ "selector: '#cmd1',\n"
//     			dark_css: 'dist/cmd_dark.min.css',
//      		light_css: 'dist/cmd_light.min.css',
      		+ "history_id: 'interface1',\n"
      		+ "external_processor: function(input, cmd) {if (input === 'ver') {setTimeout(function() {cmd.handleResponse({cmd_out: '" + out + "'});}, 10);return true;}}\n"
			+ "});\n"
			+ "</script>\n"
			;

		return ritorno;
	}
	
	// =========================================================================
	String xmlFormatBegin(String in) {
		String out = "";
		if(in.contains("/")) {
			out = in.replace("/", ">\n<");
		} else out = in;
		return out;
	}

	// =========================================================================
	String xmlFormatEnd(String in) {
		String out = "";
		if(in.contains("/")) {
			String delimiter = "/";
		    List<String> words = Arrays.asList(in.split(delimiter));
		    Collections.reverse(words);
		    in = String.join(delimiter, words);
			out = in.replace("/", ">\n</");
		} else out = in;
		return out;
	}

	// =========================================================================
	static String formatDate(String in, String fmt, String fmtIn) {
		Date date = null;
		if(fmtIn == null) fmtIn = "yyMMdd";
		try {
			if(in == null) date = new Date();
			else date = new SimpleDateFormat(fmtIn).parse(in);
		} catch (ParseException e) {
//			e.printStackTrace();
			return null;
		}
		String formattedDate = new SimpleDateFormat(fmt).format(date);

		return formattedDate;
	}

	// =========================================================================
	static String formatDecimal(String in, String fmt) {
		in = in.replaceAll(",","").trim(); 
		in = in.replaceAll(" ", ""); 
		if(in.length() == 0) in = "0";
		double value = Double.parseDouble(in);
		DecimalFormat myFormatter = new DecimalFormat(fmt);
		String out = myFormatter.format(value);
		out = out.replaceAll(",", ".");
		return out;
	}

	// =========================================================================
	// mode 1=frame 2=pane 5=option 10=sql
	// =========================================================================
	String paneViewRend(JPaneMaster master, String paneId, String formId
												,int mode, Properties param) {
		String path			= param.getProperty("sys.path");
//		String xmlPath		= param.getProperty("sys.xmlPath");
//		String xmlPathPane	= xmlPath;
		String itemXmlPath0 = "";
		String html 		= "";
		JPaneDoc jp			= master.get(paneId, "");

		String modeTool		= "base";

		String modeView		= "Edit";

		String outTxt		= "";
		
		int flagBolle		= 0;
		String oldNumBolla	= null;

		int iCmp;
		int line			= 1;
		int iStart			= 1;
		int iLine			= 0;
		int iLineOld		= -1;
		int iEnd			= 1;
		int rigMax			= jp.getItem("id", "state", "linemax"	, 1, 1);
		int rigCur			= jp.getItem("id", "state", "linecur"	, 1, 1);
		int rigRead			= jp.getItem("id", "state", "lineread"	, 1, 1);
		int iState			= jp.getItem("id", "state", "state"		, 0, 1);
		String iMode		= jp.getItem("id", "state", "mode"		, "master", 1);
		String iFocus		= jp.getItem("id", "state", "focus"		, "", 1);
		String paneXmlPath	= jp.getItem("id", "state", "xmlPath"	, "", 1);

		if(mode == 5) {
			modeView		= "View";
			iMode			= "option";
		} else if(mode == 10) {
			modeView		= "View";
			modeTool		= "sql";
			iMode			= "select";
		}

		String mP_mode		= iMode;
		int mP				= 2; // modePane 0=master 1=detail 2=list 3=contact 4=drop
		JPaneDoc jpFmt		= master.get(formId + "_" + modeTool + "_" + mP_mode, "");

		String sortMode		= "sort-name";
//		if(mP == 0) sortMode = "sort-pos";
		JPaneLoop jl		= new JPaneLoop(jp, null, sortMode);
		jl.reset(1);

		long startTime = jp.beginHelp("w9", "paneViewRend:" + paneId + " form:" + formId +" mode:" + mP_mode);
		param.setProperty("pane-xmlPath", xmlFormatBegin(paneXmlPath));
		if(mode > 1) html += "<!-- " + paneId + " -->\n";

		if(mode == 1) {
//			String tmpSection = jpFmt.getIx("HtmlBegin","");
//			jp.infoHelp("w9", tmpSection);
			html +=  paneViewExpaDef(vDec(jpFmt.getIx("HtmlBegin","")), param, jp, master, paneId, formId, jl, "HtmlBegin");
			html +=  paneViewExpaDef(vDec(jpFmt.getIx("HtmlBeginEnd","")), param, jp, master, paneId, formId, jl, "HtmlBeginEnd");
		}

//		if(mode == 1 || mode == 2)
		{
			html +=  paneViewExpaDef(vDec(jpFmt.getIx("PaneBegin","")), param, jp, master, paneId, formId, jl, "PaneBegin");
			html +=  paneViewExpaDef(vDec(jpFmt.getIx("PaneBeginEnd","")), param, jp, master, paneId, formId, jl, "PaneBeginEnd");
		}

		iEnd = rigMax;
		int flagItem = 0;
		for(line = iStart; line <= iEnd; line++) {
			jl.reset(line); // iCmpN = 0; iVer = 0;
			flagItem = 0;

			String tmpLordo = "0";
			String tmpNetto = "0";
			String tmpSconto = "0.0";
			String tmpScoMag = "";
			String tmpScontrino = "";
			String tmpScontoPer	= "0";
			
			String tmpDataBolla = "";
			String tmpNumBolla = "";
			String tmpTarga = "";
			String tmpMezzo = "";
			String tmpRiferimento = "";
			String tmpKilometri = "";

			jp.infoHelp("w9", "line:" + line);
			while((iCmp = jl.loop()) > 0) {
				String id			= jl.get("id"			,"x999");
				String ix			= jl.get("ix"			,"");
				String is			= jl.get("is"			,"");
				String inp			= jl.get("inp"			,"s");
				String xml			= jl.get("xml"			,"s");
				String type			= jl.get("type"			,"Text");
				String fmt			= jl.get("fmt"			,"");
				String tit			= vDec(jl.get("tit"		,""));
				String def			= vDec(jl.get("def"		,""));
				String val			= vDec(jl.get("val"		,def));
				String view			= vDec(jl.get("view"	,""));
				String exec			= vDec(jl.get("exec"	,""));
				String cerca		= vDec(jl.get("cerca"	,""));
				String xmlNotNull	= jl.get("xmlNotNull"	,"");
				String xmlNot		= jl.get("xmlNot"		,"");
				String itemXmlPath	= jl.get("xmlPath"		,"");

				if(xml.equalsIgnoreCase("n")) continue;

//				if(JPane.utente.equalsIgnoreCase("JAM50") // FORMOTO
//				 ||JPane.utente.equalsIgnoreCase("PETRA71") // MANZONI
//				 ||JPane.utente.equalsIgnoreCase("JAM70")) {// SUNRISE

				if(paneId.equalsIgnoreCase("TD01_LINEE")) {
					if(id.equalsIgnoreCase("x015")) {
						if(oldNumBolla == null) oldNumBolla = val;
						tmpNumBolla = val;
						if(!tmpNumBolla.equalsIgnoreCase(oldNumBolla)) flagBolle++;
						oldNumBolla = tmpNumBolla;
					}
					if(id.equalsIgnoreCase("x016")) {
						tmpDataBolla = val;
//						creaBolla(tmpNumBolla, tmpDataBolla, line);
						tmpBolla += "<DatiDDT>\n"
								+ "<NumeroDDT>" + tmpNumBolla + "</NumeroDDT>\n"
								+ "<DataDDT>" + formatDate(tmpDataBolla, "yyyy-MM-dd", null) + "</DataDDT>\n"
								+ "<RiferimentoNumeroLinea>" + line + "</RiferimentoNumeroLinea>\n"
								+ "</DatiDDT>\n"
								;
					}
					if(id.equalsIgnoreCase("x021")) tmpTarga = val;
					if(id.equalsIgnoreCase("x022")) tmpMezzo = val;
					if(id.equalsIgnoreCase("x023")) tmpRiferimento = val;
					if(id.equalsIgnoreCase("x024")) tmpKilometri = val;

					if(id.equalsIgnoreCase("x001")) {
//						if(!isWindows()) System.err.println("--- Line:" + val + " " + line);
						val = "" + line;
					}
					if(id.equalsIgnoreCase("x0010")) {
						if(!isWindows()) System.err.println("--- Ordine:" + val + " " + line);
						if(!val.equalsIgnoreCase(" ") && val.length() > 0) {
							tmpOrdine += "<DatiOrdineAcquisto>\n"
									+ "<RiferimentoNumeroLinea>" + line + "</RiferimentoNumeroLinea>\n"
									+ "<IdDocumento>" + val + "</IdDocumento>\n"
									+ "</DatiOrdineAcquisto>\n"
									;
						}
					}
				}

				if(!JPane.utente.equalsIgnoreCase("JAM51") // GIO
				 &&!JPane.utente.equalsIgnoreCase("JAM52")) {// GIZ
					if(paneId.equalsIgnoreCase("TD01_LINEE")) {
						if(id.equalsIgnoreCase("x001d")
						 &&!val.equalsIgnoreCase("0")
						 &&!val.equalsIgnoreCase("null")) {
							tmpScontoPer = val;
							tmpScoMag = "<ScontoMaggiorazione>\n"
									+ "<Tipo>SC</Tipo>\n"
									+ "<Percentuale>" + formatDecimal(tmpScontoPer, "##########0.00") + "</Percentuale>\n"
									+ "</ScontoMaggiorazione>\n"
									;
							jp.infoHelp("w9", "paneViewRend SUNRISE "
								+ paneId + "::" + id + " ix:" + ix
								+ " val:" + val + " sconto:\n" 
								+ tmpScoMag
								);
						}
						if(id.equalsIgnoreCase("x001e")
						 &&!val.equalsIgnoreCase("0")
						 &&!val.equalsIgnoreCase("null")) {
							tmpScontoPer = val;
							tmpScoMag += "<ScontoMaggiorazione>\n"
									+ "<Tipo>SC</Tipo>\n"
									+ "<Percentuale>" + formatDecimal(tmpScontoPer, "##########0.00") + "</Percentuale>\n"
									+ "</ScontoMaggiorazione>\n"
									;
							jp.infoHelp("w9", "paneViewRend SUNRISE "
								+ paneId + "::" + id + " ix:" + ix
								+ " val:" + val + " sconto:" 
//								+ tmpScoMag
								);
						}
						if(id.equalsIgnoreCase("x001i")
								 &&!val.equalsIgnoreCase("0")
								 &&!val.equalsIgnoreCase("null")) {
									tmpScontoPer = val;
									tmpScoMag += "<ScontoMaggiorazione>\n"
											+ "<Tipo>SC</Tipo>\n"
											+ "<Percentuale>" + formatDecimal(tmpScontoPer, "##########0.00") + "</Percentuale>\n"
											+ "</ScontoMaggiorazione>\n"
											;
									jp.infoHelp("w9", "paneViewRend SUNRISE "
										+ paneId + "::" + id + " ix:" + ix
										+ " val:" + val + " sconto:" 
//										+ tmpScoMag
										);
								}
					}
				}
				if(JPane.utente.equalsIgnoreCase("JAM51") // GIO e GIZ BRONX
				 ||JPane.utente.equalsIgnoreCase("JAM52"))
				if(paneId.equalsIgnoreCase("TD01_LINEE")) {
					if(id.equalsIgnoreCase("x001a")) tmpLordo = val;
					if(id.equalsIgnoreCase("x001b")) tmpScontrino = val;
					if(id.equalsIgnoreCase("x007")) val = tmpLordo;
					if(id.equalsIgnoreCase("x008")) val = tmpNetto;
					if(id.equalsIgnoreCase("x001c")) {
						tmpNetto = val;
						if(tmpLordo.equalsIgnoreCase("null")) tmpLordo = "0";
						if(tmpLordo.length() == 0) tmpLordo = "0";
						double Lordo = Double.parseDouble(tmpLordo);
						if(tmpNetto.equalsIgnoreCase("null")) tmpNetto = "0";
						if(tmpNetto.length() == 0) tmpNetto = "0";
						double Netto = Double.parseDouble(tmpNetto);

						tmpSconto = "" + (Lordo - Netto);
						tmpSconto = formatDecimal(tmpSconto, "##########0.00");
						tmpLordo = formatDecimal(tmpLordo, "##########0.00");
						tmpNetto = formatDecimal(tmpNetto, "##########0.00");
//						tmpSconto = "0.0";
						tmpScoMag = "<ScontoMaggiorazione>\n"
								+ "<Tipo>SC</Tipo>\n"
								+ "<Importo>" + tmpSconto + "</Importo>\n"
								+ "</ScontoMaggiorazione>\n"
								;

						jp.infoHelp("i9", "paneViewRend SCONTO "
								+ paneId + "::" + id + " ix:" + ix
								+ " val:" + val + " Scontrino:" + tmpScontrino + " sconto:\n" 
//								+ tmpScoMag
								);
						if(tmpScontrino.length() > 0) {
							tmpScontrino = "<AltriDatiGestionali>\n"
									+ "<TipoDato>SCONTRINO</TipoDato>\n"
									+ "<RiferimentoTesto>" + tmpScontrino + "</RiferimentoTesto>\n"
									+ "</AltriDatiGestionali>\n";
						}
					}
				}

//alt \ = backslick
				if(!id.startsWith("x")) continue;
				if(mode == 10 && id.compareToIgnoreCase("x500") > 0) continue;
				if(val.equalsIgnoreCase("null") || val == null) val = "";

				if(xmlNotNull.equalsIgnoreCase("val") && val.length() == 0) continue;
				if(xmlNot.equalsIgnoreCase("*") && val.equalsIgnoreCase("*")) continue;
				val = xmlEscText(val);

				jp.infoHelp("w9", "paneViewRend "
						+ paneId + "::" + id + " ix:" + ix + " val:" + val);

				if(ix.equalsIgnoreCase("Numero")) val = val + "/" + codGioCur;

/*				param.setProperty("sys.option", "");
				if(cerca.toLowerCase().startsWith("$bro:")) {
					String tmpPane = cerca.substring(5);
					Properties paramNew = new Properties();

					for (Enumeration propertyNames = param.propertyNames()
											;propertyNames.hasMoreElements();){
						Object key = propertyNames.nextElement();
						paramNew.put(key, param.get(key));
					}

					String tmpOption = paneViewRend(master, tmpPane, formId, 5, paramNew);
					param.setProperty("sys.option", tmpOption);
				} */

				if(type.equalsIgnoreCase("Date") && val.length() == 6)
					val = formatDate(val, "yyyy-MM-dd", null);

				if(type.equalsIgnoreCase("Decimal")) {
					if(fmt.length()>0)
						val = formatDecimal(val, fmt);
					else val = formatDecimal(val, "##########0.00###");
				}
				param.setProperty("att:val", val);
				param.setProperty("att:tit", tit);
				param.setProperty("att:id", id);
				param.setProperty("att:ix", ix);
				param.setProperty("att:item_id", id);
				param.setProperty("att:is", paneId + "." + ix);
				param.setProperty("att:xmlPath", itemXmlPath);

				if(iCmp == 1)
					html += paneViewExpaDef(vDec(jpFmt.getIx("LineBegin","")), param, jp, master, paneId, formId, jl, "LineBegin");

				flagItem++;

//				outTxt += "\n--- $$" + jp.paneId + "::" + id + viewItem(":",val) +viewItem("::-tit:",tit) + viewItem("::-view:",view) + viewItem("::-exec:", exec)+ viewItem("::-ix:", ix)+ viewItem("::-type:", type);
				{
//				String itemXmlPathOri = itemXmlPath;
					String itemTmp = "";
					if (itemXmlPath.contains("/")) {
						itemTmp = itemXmlPath.substring(0, itemXmlPath.indexOf("/"));
						itemXmlPath = itemXmlPath.substring(itemXmlPath.lastIndexOf("/") + 1);
						param.setProperty("att:xmlPath", itemXmlPath);
					}
					if (itemTmp.length() < itemXmlPath0.length())
						html += "</" + itemXmlPath0 + ">\n";
					else if (!itemXmlPath0.equalsIgnoreCase(itemTmp))
						html += "<" + itemTmp + ">\n";

					itemXmlPath0 = itemTmp;
//					html += "<!-- path:" + itemXmlPath0 + " -->\n";
				}

				if(mode != 10 && (view.length() > 0 || exec.length() > 0)) {
					String tmpPane = "";
					if(view.length() > 0) tmpPane = view.substring(2);
					if(exec.length() > 0) tmpPane = exec.substring(2);

					Properties paramNew = new Properties();

					for (Enumeration propertyNames = param.propertyNames()
											;propertyNames.hasMoreElements();){
						Object key = propertyNames.nextElement();
						paramNew.put(key, param.get(key));
					}
					html += paneViewRend(master, tmpPane, formId, 2, paramNew);
				} else
				if(!inp.equalsIgnoreCase("n")) {
					if(flagItem > 1)
						html += paneViewExpaDef(vDec(jpFmt.getIx("ItemSepa","_")), param, jp, master, paneId, formId, jl, "ItemSepa");
					html += paneViewExpaDef(vDec(jpFmt.getIx("ItemBegin","")), param, jp, master, paneId, formId, jl, "ItemBegin");
					html += paneViewExpaDef(vDec(jpFmt.getIx("ItemLabel","")), param, jp, master, paneId, formId, jl, "ItemLabel");
					html += paneViewExpaDef(vDec(jpFmt.getIx("Item" + type + modeView,"")), param, jp, master, paneId, formId, jl, "Item" + type + modeView);
					html += paneViewExpaDef(vDec(jpFmt.getIx("ItemEnd","")), param, jp, master, paneId, formId, jl, "ItemEnd");
				}

				if(paneId.equalsIgnoreCase("TD01_LINEE")) {
					if(id.equalsIgnoreCase("x007"))
						if(tmpScoMag.length() > 0)
							html += tmpScoMag;

					if(id.equalsIgnoreCase("x024")) {
						String tmpBolla
							= "<AltriDatiGestionali>\n"
							+ "<TipoDato>RICEVUTA</TipoDato>\n"
							+ "<RiferimentoTesto>" + tmpNumBolla 
							+ "  del " + formatDate(tmpDataBolla,"dd/MM/yyyy",null) + "</RiferimentoTesto>\n"
							+ "</AltriDatiGestionali>\n";
						html += tmpBolla;
						if(tmpTarga.length() > 1) {
							String tmpAutomezzo
								= "<AltriDatiGestionali>\n"
								+ "<TipoDato>AUTOMEZZO</TipoDato>\n"
								+ "<RiferimentoTesto>" + tmpTarga + " " + tmpMezzo + " " + tmpRiferimento + " " + tmpKilometri
								+ "</RiferimentoTesto>\n"
								+ "</AltriDatiGestionali>\n";
							html += tmpAutomezzo;
						}
					}
				}
			}
			if(tmpScontrino.length()>0)
				html += tmpScontrino;
			html += paneViewExpaDef(vDec(jpFmt.getIx("LineEnd","")), param, jp, master, paneId, formId, jl, "LineEnd");
		}
		
		param.setProperty("pane-xmlPath", xmlFormatEnd(paneXmlPath));


		html +=  paneViewExpaDef(vDec(jpFmt.getIx("PaneEnd","")), param, jp, master, paneId, formId, jl, "PaneEnd");
		if(mode == 1)
			html +=  paneViewExpaDef(vDec(jpFmt.getIx("HtmlEnd","")), param, jp, master, paneId, formId, jl, "HtmlEnd");

//		if(Detail.length()>0)
//			html += paneViewRend(master, "fmvx_det", formId, mainId);

		jp.endHelp("w9", "paneViewRendEnd:" + paneId
				+ html
				+ "\n--- time:"
				,startTime);

		if(paneId.equalsIgnoreCase("TD01_LINEE")) {
			System.out.println("--- FlagBolle:" + flagBolle);
			if(flagBolle == 0) tmpBolla = null;
		}

		html += outTxt;
		return html;
	}

	//=========================================================================
	String paneViewExpaDef(String text, Properties prop, JPaneDoc jp
	,JPaneMaster master, String paneId, String formId, JPaneLoop jl, String k) {

		if(text.length() == 0 || text.equalsIgnoreCase("_")) return "";
		long startTime = jp.beginHelp("w9", "$expaDef: " + paneId + "::" + k + " text:" + text);

		Pattern pt = Pattern.compile("\\@\\{([^}]*)\\}");
		while(true) {
			Matcher m = pt.matcher(text);
			if(!m.find()) break;

			String name = m.group(1);
			if(name.length() == 0) return text;

			String tmp = "";
			if(prop != null) tmp = prop.getProperty(name, "");
			if(tmp.length() == 0)
			{
				if(name.startsWith("att:val")) {
					tmp = xmlEscText(jl.get("val", ""));
				}
				if(name.startsWith("att:")) {
					tmp = xmlEscText(jl.get(name.substring(name.indexOf(":")+1), ""));
				}
			}

			if(tmp.equalsIgnoreCase("none")) return "";
			if(tmp.equalsIgnoreCase("null")) tmp = "";

			jp.infoHelp("w9", "$expaDef:[" + paneId + "::" + name + " = " + tmp + "]" + text);

//			if(tmp.length()  == 0) return "";
			text = text.replace(m.group(0), tmp);
		}
		if(text.equalsIgnoreCase("_")) text = "";
		return text;
	}

	// =========================================================================
	static byte[] xml2pdf(String in, String out) {
//		log("i5", "xml2pdf:" + in.length() + " " + temp + out + " ...");
		if(in.length() < 65) in = JPane.readFile(temp + in);

		byte[] data = Base64Coder.decode(in);
		if(out == null) {return data;}

		try (OutputStream stream = new FileOutputStream(temp + out)) {
			stream.write(data);
		} catch (IOException e) {e.printStackTrace();}
		return null;
	}

	// =========================================================================
	public static String readFile(String path) {
		String response = "";
		try {
			log("i5", "readFile:" + path + " ...");

/*			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String strLine;
			StringBuffer sb = new StringBuffer();
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine);
			}
			fr.close();
			br.close();
			response = sb.toString(); */

			File f = new File(path);
			try {
				byte[] bytes = Files.readAllBytes(f.toPath());
				response = new String(bytes, "UTF-8");
			} catch (IOException e) {e.printStackTrace();return null;}

//			log("i5", response);
		} catch (Exception e) {e.printStackTrace();}
		return response;
	}

	// =========================================================================
	public static final String UTF8_BOM = "\uFEFF";
	public static String removeUTF8BOM(String s) {
	    if (s.startsWith(UTF8_BOM)) {
	        s = s.substring(1);
	    }
	    return s;
	}

	// =========================================================================
	static String FatturaXml2json(String nome, String out) {
		String line	= "";
		String str	= "";
//		String link	= "IT04028070169_DF_52.xml";
//		String link = "IT01234567890_33333.xml";
//		String link = "IT01729760163_03632_IZO.xml";
//		String link = "IT00228550273_63107.xml";
		String link = "IT01895030995_0BZKG.xml";
		if(nome == null) nome = link;

		
		str = readFile(temp + tempIn + nome);
/*
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(temp
					+ tempIn
//					+ "FattNew/in/"
					+ nome));
			while ((line = br.readLine()) != null) {
				str += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
		str = str.replace("\"xmlns:", "\" xmlns:");
		str = str.replace("\"xsi:", "\" xsi:");
		str = removeUTF8BOM(str);

//		if(!isWindows()) 
		{
			String tmp = xml2html(str, "Fatturapa_v1.101.xsl");
			nome = nome.replace(".xml", "");
//			writeTemp(nome + ".htm", tmp, "FattNew/in");
			writeTemp(nome + ".htm", tmp, "FattNew/inOK", false);
		}
		if(!isWindows()) 
		{
			String tmp3= xml2html(str, "Fatturapa_v1.2.xsl");
			nome = nome.replace(".xml", "");
			writeTemp(nome + ".html", tmp3, "FattNew/inOK", false);
		}
//		JSONObject jsondata = JSONML.toJSONObject(str);
//		String Xml = JSONML.toString(jsondata);

		JSONObject jsondata = null;
		try {
			jsondata = XML.toJSONObject(str);
//			String Xml = XML.toString(jsondata);

//			System.out.println(jsondata.toString(4));
//			System.out.println(Xml);

			String tagAll = "/FatturaElettronica/FatturaElettronicaBody/Allegati/";
			String allName = jsonQuery(jsondata, tagAll + "NomeAttachment");
//			log("e5", "Allegato:" + tagAll + "NomeAttachment");
/*			if(allName != null) {
				log("i5", "Allegato:" + allName);
				String allType = jsonQuery(jsondata, tagAll + "FormatoAttachment");
				String allTxt = jsonQuery(jsondata, tagAll + "Attachment");
				xml2pdf(allTxt, "FattNew/inOK/" + allName);
			} */
		} catch (org.json.JSONException e) {e.printStackTrace();return null;}
		return jsondata.toString(4);
	}

	// =========================================================================
	static String jsonQuery(JSONObject jsondata, String q) {
		Object allName = null;
		String ritorno = null;
		try {
			allName = jsondata.query(q);
			try {
				ritorno = (String) allName;
			} catch (java.lang.ClassCastException e) {
				ritorno = "" + allName;
			}
		} catch (org.json.JSONPointerException e) {
		}

		return ritorno;
	}

	// =========================================================================
	class SQL {
		Properties items		= new Properties();
		String sqlTxt			= "";
		String connect			= "";
		Connection conn			= null;
		Statement stmt			= null;
		CallableStatement cs	= null;
		ResultSet rset			= null;

		String sqlFrom			= "";
		String sqlWhere			= "";
		String sqlOrder			= "1";
		String sqlSepa			= " ";
		int nItem				= 0;

		// =========================================================================
		//
		// =========================================================================
		String connect(String connectTxt) {
			String errore = "OK";
			long startTime = System.currentTimeMillis();
			log("w5", "SQL connect Begin: " + connectTxt);

			if(connect != null && connect.equalsIgnoreCase(connectTxt) && conn != null) {
				long endTime = System.currentTimeMillis();
				try {
					conn.setAutoCommit(true);
					stmt = conn.createStatement();
					log("w5", "SQL CONNECT END[" + (endTime - startTime) + " ms]:");
					return errore;
				} catch (SQLException e) {
					e.printStackTrace();
					connect = "";
					log("w5", "SQL CONNECT KO[" + (endTime - startTime) + " ms]:");
				} catch (NullPointerException e) {
					connect = "";
				}
			}

			connect = connectTxt;
			String con = "";
			String usr = "";
			String pwd = "";
			try {
				con = connect.substring(connect.indexOf("-- jdbc")+3);
				log("w5", "SQL con: " + con);
				usr = connect.substring(8, connect.indexOf("/"));
				log("w5", "SQL usr: " + usr);
				pwd= connect.substring(connect.indexOf("/")+1, connect.indexOf("@"));
				log("w5", "SQL pwd: " + pwd);

				if(con.startsWith("jdbc:ora"))
					DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
				conn = DriverManager.getConnection(con, usr, pwd);
//				jPaneDoc.oracleHost = con.substring(con.lastIndexOf("@")+1);
//				jPaneDoc.oracleHost = jPaneDoc.oracleHost.substring(0, jPaneDoc.oracleHost.indexOf(":"));

				long endTime = System.currentTimeMillis();
				log("w5", "SQL CONNECT END[" + (endTime - startTime) + " ms]:");

				for(SQLWarning warn = conn.getWarnings(); warn != null;
													warn = warn.getNextWarning()) {
					log("w5", "SQL Warning:" ) ;
					log("w5", "SQL State  : " + warn.getSQLState()  ) ;
					log("w5", "SQL Message: " + warn.getMessage()   ) ;
					log("w5", "SQL Warning: " + warn.getErrorCode() ) ;
				}
				stmt = conn.createStatement();
			} catch (SQLException e) {
				while( e != null ) {
					log("e5", "SQL Connect [" + con + " " + usr + " " + pwd + "] ...");
					log("e5", "SQL Exception:  " + e.getMessage(), e.toString());
					log("e5", "SQL State:      " + e.getSQLState(), null);
					log("e5", "SQL VendorError:" + e.getErrorCode(), null);
					e = e.getNextException();
					errore = "KO";
					connect = "";
				}
//				e.printStackTrace();
			}

			long endTime = System.currentTimeMillis();
//			jPaneDoc.timeConnect = (endTime - startTime);
//			jPaneCloud.log("w6-1", "SQL connect End [" + (endTime - startTime) + " ms]", null);
			return errore;
		}

		// =========================================================================
		// Parse Line es $jpo=go sys.codice=ana.codice sys.flaf='01';
		// =========================================================================
		List<String> parseLine(String input, char sep) {
//			input = "foo,bar,c;qual=\"baz,blurb\",d;junk=\"quux,syzygy\"";
			if(sep == 0) sep = ';';
			List<String> result = new ArrayList<String>();
			int start = 0;
			boolean inQuotes = false;
			for(int current = 0; current < input.length(); current++) {
				if(input.charAt(current) == '\"') inQuotes = !inQuotes; // toggle state

				boolean atLastChar = (current == input.length() - 1);
				if(atLastChar) result.add(input.substring(start));
				else if((input.charAt(current) == sep) && !inQuotes) {
					result.add(input.substring(start, current));
					start = current + 1;
				}
			}
			return result;
		}

		// =========================================================================
		String setFrom(String from, String cmp) {
			List<String> cmp2 = parseLine(cmp, '.');
			String from2 = cmp2.get(0);
			List<String> vet = parseLine(from, ',');
			for(int i=0; i<vet.size(); i++) {
				if(from2.equalsIgnoreCase(vet.get(i))) return from;
			}
			if(from.length() > 0) from += ",";
			from += from2;
			return from;
		}

		// =========================================================================
		SQL() {
			
		}

		// =========================================================================
		SQL select(JPaneDoc jp, String user, String pwd, String svr
								,int giro, String paneId, JPaneMaster master) {
			if(giro == 1) {
				sqlTxt = "REM CON "+ user + "/" + pwd + "@" + svr + ";"
					+ "-- jdbc:oracle:thin:hr/hr@" + svr + "\n"
					+ "-- ALTER SESSION SET NLS_NUMERIC_CHARACTERS='.,';\n"
					+ "-- ALTER SESSION SET NLS_DATE_FORMAT='YYMMDD';\n"
//					+ "select * from cat order by 1;\n"
					;
				sqlTxt += "select\n";
				sqlWhere = "";
				sqlFrom = "";
				sqlOrder = "1";
				nItem = 0;
				if(paneId.equalsIgnoreCase("TD01_FAT")) {
					sqlWhere = "where CONFG.COD_DITTA='01'\n"
							 + "and CONFG.COD_DITTA = ANAC.COD_DITTA\n"
				 			 + "and CGIVAT.COD_DITTA = CONFG.COD_DITTA\n"
							 + "and CGIVAT.ESERCIZIO = '" + esercizio + "'\n"
							 + "and CGIVAT.NUM_MOV = " + numFattura + "\n"
				 			 + "and CGIVAT.COD_PAG = TABPAG.COD_PAG(+)\n"

//				 			 + "and CGIVAT.COD_CLI_FOR = ANAC.COD_CONTO\n"
				 			 + "and MAGMVT.CLI_O_FORN = ANAC.COD_CONTO\n"

				 			 + "and MAGMVT.COD_DITTA = CONFG.COD_DITTA\n"
							 + "and MAGMVT.ESERCIZIO = '" + esercizio + "'\n"
							 + "and MAGMVT.NUM_FATTURA = CGIVAT.NUM_MOV\n"
				 			 + "and MAGMVT.CAU_CONTAB = CGIVAT.CAU_CONTAB\n"
				 			 + "and MAGMVT.CAU_CONTAB = TABCCO.CAU_CONTAB\n"

				 			 + "and MAGMVT.CAU_MOVIM = TABCAM.CAU_MOVIM\n"

				 			 + "and FATMVT.COD_DITTA = CONFG.COD_DITTA\n"
							 + "and FATMVT.ESERCIZIO = '" + esercizio + "'\n"
							 + "and FATMVT.NUM_FATTURA = CGIVAT.NUM_MOV\n"
				 			 + "and FATMVT.COD_GIORNALE = CGIVAT.COD_GIORNALE\n"

				 			 ;

					if(codGioVen.contains(":"))
						sqlWhere += "and CGIVAT.COD_GIORNALE in(2,3)\n";
					else
					sqlWhere += "and CGIVAT.COD_GIORNALE = '" + codGioVen + "'\n";

					if(JPane.utente.equalsIgnoreCase("PETRA71")
					 ||JPane.utente.equalsIgnoreCase("JAM80")); // iban_1 da anac manzoni
					else {
						sqlWhere += "and CGIVAT.COD_BANCA = TABBANG.COD_BANCA(+)\n";
					}
					sqlFrom = "FATMVT";
				}
				if(paneId.equalsIgnoreCase("TD01_IVA")) {
					sqlWhere = "where CGIVAR.COD_DITTA = '01'\n"
							 + "and CGIVAR.ESERCIZIO = '" + esercizio + "'\n"
							 + "and CGIVAR.NUM_DOC = " + numFattura + "\n"
							 + "and CGIVAR.COD_IVA = TABIVA.COD_IVA\n"
							 ;
					if(codGioVen.contains(":"))
						sqlWhere += "and CGIVAR.COD_GIORNALE in(2,3)\n";
					else
					sqlWhere += "and CGIVAR.COD_GIORNALE = '" + codGioVen + "'\n";

					sqlFrom = "TABIVA";
				}
				if(paneId.equalsIgnoreCase("TD01_LINEE")) {
					sqlWhere = "where MAGMVR.COD_DITTA = '01'\n"
							 + "and MAGMVR.ESERCIZIO = '" + esercizio + "'\n"
							 + "and MAGMVR.NUM_FATTURA = " + numFattura + "\n"
							 + "and MAGMVR.COD_IVA = TABIVA.COD_IVA\n"
							 + "and MAGMVT.COD_DITTA = MAGMVR.COD_DITTA\n"
							 + "and MAGMVT.ESERCIZIO = MAGMVR.ESERCIZIO\n"
							 + "and MAGMVT.NUM_MOVIM = MAGMVR.NUM_MOVIM\n"
							 + "and MAGMVT.CAU_MOVIM = MAGMVR.CAU_MOVIM\n"
							 ;
					sqlFrom = "TABIVA,MAGMVT";
					if(codGioVen.contains(":"))
						sqlWhere += "and MAGMVR.COD_GIORNALE in(2,3)\n";
					else
					sqlWhere += "and MAGMVR.COD_GIORNALE = '" + codGioVen + "'\n";
					sqlOrder = "MAGMVR.NUM_MOVIM,1";
				}
				if(paneId.equalsIgnoreCase("IVP_DET")) {
					sqlWhere = "where COD_DITTA = '01'\n"
							 + "and ESERCIZIO = '" + esercizio + "'\n"
							 + "and TOT_IMP_ATT_01 is not NULL\n"
							 ;
					sqlFrom = "";
				}
			}

			long startTime = System.nanoTime();
			log("w5", "SQL Select Begin:" + giro);
			String sortMode	= "sort-name";
			JPaneLoop jl	= new JPaneLoop(jp, null, sortMode);
			int iCmp		= 0;

			while((iCmp = jl.loop()) > 0) {
				String id	= jl.get("id"				,"x999");
				String ix	= jl.get("ix"				,"");
				String is	= vDec(jl.get("is"			,""));
				String isRead= vDec(jl.get("isRead"		,""));
				String inp	= jl.get("inp"				,"s");
				String val	= vDec(jl.get("val"			,""));
				int nkey	= jl.get("nkey"				,0);
				String type	= jl.get("type"				,"Text");
				String view	= vDec(jl.get("view"		,""));

				log("W5", "Item:" + id + " ix:" + is + " val:" + val + " nIt:" + nItem);

				if(view.length() > 0) {
					String tmpPane = view.substring(2);
					log("W5", "--- VIEW:" + tmpPane); 
					JPaneDoc jp2 = master.get(tmpPane, "");
					select(jp2, user, pwd, svr, giro+1, tmpPane, master);
				}

				if(is.length() == 0) continue;
				if(is.equalsIgnoreCase("null")) continue;
				nItem++;

				String isTmp = is;
				if(isRead.length()>0) isTmp = isRead;

				sqlSepa = "";
				if(nItem > 1) sqlSepa = ",";
				sqlTxt += (sqlSepa +  isTmp + " " + "\"" + paneId + "_" + id + "\"\n");
				sqlFrom = setFrom(sqlFrom, is);
				if(nkey == 1 && val.length() > 0 && !val.equalsIgnoreCase("null")) {
					if(sqlWhere.length() == 0) sqlWhere += "where ";
					else sqlWhere += " and ";
/*					if(val.contains(":")) {
						sqlWhere += " " + is + " in(2,3)\n";						
					} else */
					sqlWhere += " " + is + " = '" + val + "'\n";
				}
			}

			if(giro == 1) {
				sqlTxt += "from " + sqlFrom + "\n";
				if(sqlWhere.length() > 0) sqlTxt += sqlWhere;
				sqlTxt += "order by " + sqlOrder + ";\n";

				if(JPane.utente.equalsIgnoreCase("PETRA71")) // MANZONI
					sqlTxt = sqlTxt.replace("ANAC.NOTE_N3", "ANAC.NOTE_N4");

				sqlLog += sqlTxt + "\n";

				log("W5", "Sql:" + sqlTxt);
			}

			long endTime = System.nanoTime();
			log("w5", "SQL Select: "
					+ " END[" + (endTime - startTime) + " ns]:");

			return this;
		}

		// =========================================================================
		SQL readSql(JPaneDoc jp, JPaneMaster master) {
			long startTime = System.currentTimeMillis();
			log("w5", "SQL Read Begin");

			int jLine = 0;
			int columnCount = 0;
			try {
				ResultSetMetaData metaData = rset.getMetaData();
				columnCount = metaData.getColumnCount();
				while(rset.next()) {
//					if(jLine > 1) break;
					String tmpId = "";
//					log("w5", "Line:" + jLine);
					
					String idSql = metaData.getColumnName(1);
					if(idSql.contains("TD01_IVA")) { // Elimina Bollo da Iva
						int l = idSql.lastIndexOf("_");
						tmpId = idSql.substring(0, l);
						String valTmp = rset.getString(6);
						log("i5", "SQL IVA: " + valTmp);

//						if(valTmp != null && valTmp.toUpperCase()
//								.contains("ART. 15")) continue;
					}

					jLine++;
					for(int n = 1; n <= columnCount; n++) {
						idSql = metaData.getColumnName(n);
						String tmpDef = "";

						String val = rset.getString(n);
						if(val == null) val = "null";
//						log("w5", "readSql::" + idSql + " type:" + tipo + " line:" + jLine + " val:" + val);
						if(jLine == 1) {
							int sqlType = rset.getType();
							int sqlLen = metaData.getColumnDisplaySize(n);
							int sqlDeci = metaData.getScale(n);
							String sqlName = metaData.getCatalogName(n);
							String sqlTypeName = metaData.getColumnTypeName(n);
//							jp.setItem(null, idSql, "sqlType", "" + sqlType, jLine);
//							jp.setItem(null, idSql, "sqlLen", "" + sqlLen, jLine);
//							jp.setItem(null, idSql, "sqlDeci", "" + sqlDeci, jLine);
//							jp.setItem(null, idSql, "sqlName", "" + sqlName, jLine);
//							jp.setItem(null, idSql, "sqlTypeName", "" + sqlTypeName, jLine);
							tmpDef = "::-sqlType:" + sqlTypeName + "::-sqlLen:" + sqlLen + "::-sqlDeci:" + sqlDeci;
						} else {
							tmpDef = "";
						}

/*						String type = jp.getItem("id", idSql, "type", "Text", 1);
						val = xmlEscText(val);
						if(type.equalsIgnoreCase("Date") && val.length() == 6)
							val = giraData(val);
//						items.put("[" + jLine + "]" + idSql, val);
						jp.setItem(null, idSql, "val", val, jLine);
*/
						int l = idSql.lastIndexOf("_");
						tmpId = idSql.substring(0, l);
						idSql = idSql.substring(0, l) + "::" + idSql.substring(l+1);
						String cmd = "$$" + idSql + ":" + JPane.vEnc(val) + tmpDef;
						master = execBase(jp, cmd, jLine, jp, master);

					}
//					log("i5", "SQL READ:" + tmpId); 
					if(tmpId.equalsIgnoreCase("TD01_PAG")) break;
				}
//				jp.setItem(null, "state", "lineread", ""+(jLine-1), 1);
//				jp.setItem(null, "state", "linemax", ""+(jLine-1), 1);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			long endTime = System.currentTimeMillis();
			log("w5", "SQL READ: " + jLine + ":" + columnCount
					+ " END[" + (endTime - startTime) + " ms]:");

			return this;
		}

		// =========================================================================
		SQL execSQL(JPaneDoc jp, String sqlTxtPara, JPaneMaster master, Boolean isScript) {
			if(sqlTxtPara == null) sqlTxtPara = sqlTxt;
			if(JPane.utente.equalsIgnoreCase("JAM51") // GIO e GIZ
			 ||JPane.utente.equalsIgnoreCase("JAM52")) {
				sqlTxtPara = sqlTxtPara.replace("ANAC", "VANATOT");
			}

			try {
				String sqlTxt		= "";
				Boolean immediate	= true;
				int flagAutoCommit	= 0;
				String nextLine		= null;
				StringBuffer nextStatement = new StringBuffer();
				BufferedReader lines = new BufferedReader(new StringReader(sqlTxtPara));

				stmt = null;
				try {
					while((nextLine = lines.readLine()) != null) {
						if(nextLine.startsWith("REM CON ")) {
							connect(nextLine);
							if(conn == null) return this;
//							conn.setNetworkTimeout(null, 30000);
							conn.setAutoCommit(true);
							flagAutoCommit = 1;
							stmt = conn.createStatement();
//							stmt.setQueryTimeout(1);

							stmt.execute("ALTER SESSION SET NLS_NUMERIC_CHARACTERS='.,'");
							stmt.execute("ALTER SESSION SET NLS_DATE_FORMAT='YYMMDD'");
							continue;
						} else
						if(nextLine.startsWith("REM")
						 ||nextLine.startsWith("--")
						 ||(nextLine.startsWith("COMMIT") && !isScript)
						 ||nextLine.startsWith("CONNECT")
						 ||nextLine.length() < 1) continue;

						if(nextLine.toUpperCase().startsWith("BEGIN")
						 ||nextLine.toUpperCase().startsWith("DECLARE")) {
							isScript = true;
						}

						if(nextLine.startsWith("/")) {
//							nextStatement.append(";");
							break;
						}

						nextStatement.append(" " + nextLine + "\n");

						if(!isScript && nextLine.endsWith(";")) {
							sqlTxt = nextStatement.toString().replace(";", "");
							log("w5", "SQL Exec:\n" + sqlTxt);

							try {rset = stmt.executeQuery(sqlTxt);
							} catch(java.sql.SQLSyntaxErrorException er) {
								log("e5", "SQL Exec:\n" + sqlTxt);
								log("e5", "SQL Exec: ERRORE SINTASSI ORACLE" + er);	
								log("e5", "\n--- SQL Exec: ERRORE SINTASSI ORACLE:\n--- " + er, sqlTxt);
								log("e5", "SQL State:      " + er.getSQLState(), null);
								log("e5", "SQL VendorError:" + er.getErrorCode(), null);
							}

							if(sqlTxt.toUpperCase().contains("SELECT")
							 &&!sqlTxt.toUpperCase().contains("BEGIN")) {
// Tappullo
//								log("w5", "SQL Exec SELECT: " + sql);
//								while(rset.next()) {log("w6-1", "LineRead:");}
								return readSql(jp, master);
							}
							nextStatement = new StringBuffer();
						}
					}
					if(isScript) {
						sqlTxt = nextStatement.toString();
						log("w5", "SQL Exec [" + immediate + "]\n" + sqlTxt);
						if(immediate) stmt.execute(sqlTxt);
						else cs = conn.prepareCall(sqlTxt);
						return this;
					}
				} catch (SQLException e) {
					log("e5", "SQL Exec Script: " + sqlTxt);
					e.printStackTrace();
				
					log("e5", "SQL Exception:   " + e.getMessage(), e.toString());
//					jPaneDoc.setHelpStatic("--- SQLException: " + e.getMessage(), e.toString());
					log("e5", "SQL State:       " + e.getSQLState(), null);
//					jPaneDoc.setHelpStatic("--- SQLState:     " + e.getSQLState(), null);
					log("e5", "SQL VendorError: " + e.getErrorCode(), null);
//					jPaneDoc.setHelpStatic("--- VendorError:  " + e.getErrorCode(), null);
//					jPaneDoc.setHelpStatic("---\n" + e.toString(), "execSql:" + immediate);
				} catch (IOException e) {
//					log("w6-1", "SQL Exec Script: " + sql);
//					e.printStackTrace();
				} catch (NullPointerException e) {
					log("e5", "SQL Exec Script: " + sqlTxt);
//					jPaneDoc.setHelpStatic("\n--- SQL Exec: ERRORE ORACLE NullPointer:\n--- " + e, sql);
//					jPaneDoc.setHelpStatic("--- SQLState:     " + e.getClass(), null);
//					jPaneDoc.setHelpStatic("--- VendorError:  " + e.getLocalizedMessage(), null);
					e.printStackTrace();
					return this;
				}
				stmt.close();
				if(flagAutoCommit == 0) conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();				
			}

			return this;
		}

		void close() {
			try {conn.close();
				conn = null;
				connect = null;
			} catch (SQLException e) {
				log("e5", "SQL Close Error...");
				e.printStackTrace();
			}
		}
	}

	// =========================================================================
	static String removePrefix(String fatturaJson, String token) {
		String ret = fatturaJson;
		String pref = "";

		int pos = fatturaJson.indexOf(":" + token);
		if(pos > 0) {
			for (int i = pos; i>0; i--) {
				if(fatturaJson.charAt(i) == '"') break;
				pref = fatturaJson.charAt(i) + pref;
			}
//			System.out.println("--- Token:" + pref + " token:" + "\"" + pref + token);
			ret = fatturaJson.replaceAll("\"" + pref + token, "\"" + token);
		}

		return ret;
	}

	// =========================================================================
	class FatturaElettronica {
		String fatturaJson	= null;
		JSONObject jsonData = null;
		Properties items	= new Properties();
		String tokenBegin	= "FatturaElettronica";
		String tokenHeader	= "FatturaElettronicaHeader";
		String tokenBody	= "FatturaElettronicaBody";

		boolean readFileXml(String nome) {
			fatturaJson = FatturaXml2json(nome, temp + tempIn + nome);
			fatturaJson = fatturaJson.replace("FatturaElettronicaSemplificata", tokenBegin);
			fatturaJson = removePrefix(fatturaJson, tokenBegin);

			jsonData = new JSONObject(fatturaJson);
//			System.out.println(jsonData.toString(4));

			if(jsonData != null) return true;
			else return false;
		}

		public void getJsonObject(JSONObject jsonObj, String path, Properties items) {
			for (Object key : jsonObj.keySet()) {
				String keyStr = (String) key;
				Object keyvalue = jsonObj.get(keyStr);

				if (keyvalue instanceof JSONObject) {
//					System.out.println("--- key: " +path + "/" +  keyStr + " -> " + keyvalue);
					getJsonObject((JSONObject) keyvalue, path + "/" + keyStr, items);
				} else if(keyvalue instanceof JSONArray) {
//					System.out.println("--- key: " +path + "/" +  keyStr + " -> " + keyvalue);
					getJsonArray((JSONArray) keyvalue, path + "/" + keyStr, items);
				} else {
					items.put(path + "/" +  keyStr , ""+keyvalue);
//					System.out.println("--- key: " +path + "/" +  keyStr + " -> " + keyvalue);
				}
			}
		}

		// =========================================================================
		public void getJsonArray(JSONArray jsonObj, String path, Properties items) {
			for(int i=0; i<jsonObj.length();i++) {
				Object keyvalue = jsonObj.get(i);

				if(keyvalue instanceof JSONObject) {
//					System.out.println("--- key: [" + i + "] -> " + keyvalue);
					items.put(path + "[" +  i + "]" , ""+keyvalue);
					getJsonObject((JSONObject) keyvalue, path+ "[" + i + "]", items);
				} else if(keyvalue instanceof JSONArray) {
//					System.out.println("--- key: [" + i + "] -> " + keyvalue);
					items.put(path + "[" +  i + "]" , ""+keyvalue);
					getJsonArray((JSONArray) keyvalue, path + "[" + i + "]", items);
				} else {
					items.put(path + "[" +  i + "]" , keyvalue);
//					System.out.println("--- key: [" + i + "] -> " + ""+keyvalue);
				}
			}
		}

		// =========================================================================
		boolean readNext() {
			items = new Properties();
			getJsonObject(jsonData, "", items);
			items.list(System.out);

			return true;
		}

		String getItem(String ix, String def) {
			String tmpVal = def;
			tmpVal = (String)items.get(ix);
			System.out.println("--- GETITEM:" + ix + " -> " + tmpVal);

			return tmpVal;
		}
 	}

	// =========================================================================
	String execRead(JPaneMaster master, String paneId, String key
								,JPane.FatturaElettronica fattura, String path) {
		String sortMode	= "sort-name";
		JPaneDoc jp		= master.get(paneId, "");
		JPaneLoop jl	= new JPaneLoop(jp, null, sortMode);
		String xmlPath	= jp.getItem("id", "state", "xmlPath", "", 1);

//		System.err.println("--- paneId:" + paneId);
//		System.err.println("--- state-xmlPath:" + xmlPath);

		if(xmlPath != null
		 &&!xmlPath.equalsIgnoreCase("null")
		 &&!xmlPath.equalsIgnoreCase("none")) path += "/" + xmlPath;

		int line		= 0;
		while(true) {
			int iCmp=0;

			line++;
			jl.reset(line); // iCmpN = 0; iVer = 0;

			sqlIns2 = "";
			sqlInsVal2 = "";

			sqlIns3 = "";
			sqlInsVal3 = "";

			while((iCmp = jl.loop()) > 0) {
				String id		= jl.get("id"		,"x999");
				String ix		= jl.get("ix"		,"");
				String is		= jl.get("is"		,"");
				String isWrite	= jl.get("isWrite"	,is);
				String inp		= jl.get("inp"		,"s");
				String view	= vDec(jl.get("view"	,""));
				String type		= jl.get("type"		,"Text");
				String pathPane = vDec(jl.get("xmlPath"	,""));
				
				if(view.length() > 0 && pathPane.contains("/"))
					pathPane = pathPane.substring(0, pathPane.lastIndexOf("/"));

				if(ix.length()>0 && isWrite.startsWith("CGIVA")) {
				String tmpPath = "/" + path + "/" + pathPane;
				tmpPath = tmpPath.replaceAll("//", "/");

//				System.err.println("--- Item:" + id + " ix:" + ix
//						+ " view:" + view + " pathPane:" + pathPane + " tmpPath:" + tmpPath);
				String val = jsonQuery(fattura.jsonData, tmpPath);
				if((val != null && val.length() > 0 && tmpPath.contains("/Causale"))) val = "";

				if((val == null || val.equalsIgnoreCase("null")) && tmpPath.contains("/DatiRiepilogo/")) {
					tmpPath = tmpPath.replace("/DatiRiepilogo/", "/DatiRiepilogo/0/");
					val = jsonQuery(fattura.jsonData, tmpPath);
//					System.err.println("--- Item:" + tmpPath + " ->" + val);

					tmpPath = tmpPath.replace("/DatiRiepilogo/0/", "/DatiRiepilogo/1/");
					String val2 = jsonQuery(fattura.jsonData, tmpPath);
					if(tmpPath.contains("/AliquotaIVA")) {
						sqlIns2 += ",PERC_IVA";
						sqlInsVal2 += ",'" + val2 + "'"; 
					}
					if(tmpPath.contains("/Natura")) {
						sqlIns2 += ",COD_IVA";
						sqlInsVal2 += ",'" + val2 + "'"; 
					}
					if(tmpPath.contains("/ImponibileImporto")) {
						sqlIns2 += ",IMP_D_VAL";
						sqlInsVal2 += ",'" + val2 + "'"; 
					}
					if(tmpPath.contains("/Imposta")) {
						sqlIns2 += ",IMPOST_D_VAL";
						sqlInsVal2 += ",'" + val2 + "'"; 
					}
//					System.err.println("--- Item:" + tmpPath + " ->" + val2);

					tmpPath = tmpPath.replace("/DatiRiepilogo/1/", "/DatiRiepilogo/2/");
					String val3 = jsonQuery(fattura.jsonData, tmpPath);
					if(tmpPath.contains("/AliquotaIVA")) {
						sqlIns3 += ",PERC_IVA";
						sqlInsVal3 += ",'" + val3 + "'"; 
					}
					if(tmpPath.contains("/Natura")) {
						sqlIns3 += ",COD_IVA";
						sqlInsVal3 += ",'" + val3 + "'"; 
					}
					if(tmpPath.contains("/ImponibileImporto")) {
						sqlIns3 += ",IMP_D_VAL";
						sqlInsVal3 += ",'" + val3 + "'"; 
					}
					if(tmpPath.contains("/Imposta")) {
						sqlIns3 += ",IMPOST_D_VAL";
						sqlInsVal3 += ",'" + val3 + "'"; 
					}
//					System.err.println("--- Item:" + tmpPath + " ->" + val3);
				}

//				String val	= fattura.getItem(tmpPath, "");
				if(type.equalsIgnoreCase("Date")) val = formatDate(val, "yyMMdd", "yyyy-MM-dd");
//				System.err.println("--- GetXml:" + tmpPath + " val:" + val + " is:" + isWrite);

				if(sqlIns.length() == 0) {sqlIns = "INSERT INTO "
					+ isWrite.substring(0, isWrite.indexOf("."))
					+ "(NUM_MOV," + isWrite;
				} else {sqlIns += "," + isWrite;}

				if(sqlInsVal.length() == 0) { sqlInsVal = "VALUES(1,'" + val + "'";
				} else {sqlInsVal += ",'" + val + "'";}

				if(rcvLog.length() > 0) rcvLog += ",";
				rcvLog += "\"" + val + "\"";
//				System.err.println(sqlIns);
//				System.err.println(fattura.jsonData.toString(4));
//				System.exit(0);
				}

				if(view.length() > 0) {
					String tmpPane = view.substring(2);
//					log("e5", "--- VIEW:" + tmpPane); 
//					JPaneDoc jp2 = master.get(tmpPane + "/" + pathPane , "");
					execRead(master, tmpPane, "", fattura, path + "/" + pathPane);
				}

			}

			if(sqlIns2.contains("IMP_D_VAL")) {
				sqlIns2 = "INSERT INTO FECGIVAR(NUM_MOV" + sqlIns2;
				sqlInsVal2 = "VALUES(1" + sqlInsVal2;
//				System.err.println("--- " + sqlIns2 + " " + sqlInsVal2);
			}
			if(sqlIns3.contains("IMP_D_VAL") && !sqlInsVal3.contains("'null','null','null'")) {
				sqlIns3 = "INSERT INTO FECGIVAR(NUM_MOV" + sqlIns3;
				sqlInsVal3 = "VALUES(1" + sqlInsVal3;
//				System.err.println("--- " + sqlIns2 + " " + sqlInsVal2);
			} else sqlIns3 = "";
	
			break;
		}
		return path;
	}

	class READ_XLSX {
		READ_XLSX(String name, JPaneDoc pane) {
			System.out.println("--- XLSX:" + name + " -> Pane ...");

			String valore = "";
			try {
				File file = new File(name);
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line;
				int flagOk = 0;

				while ((line = br.readLine()) != null) {
					if (line.startsWith("Codice Iva"))
						flagOk = 1;
					if (line.startsWith(";;;;"))
						flagOk = 0;
					if (flagOk == 1 && line.startsWith("N0")) {
						System.out.println(line);
						valore = valore + line + "\n";
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			BufferedReader lines = new BufferedReader(new StringReader(valore));
/*			CSVReader reader = new CSVReader(lines, ';');
			String[] vLine;

			int jLine = 0;
			try {
				while ((vLine = reader.readNext()) != null) {
					jLine++;
					int nLine = vLine.length;
					if (nLine > 1)
						System.out.println(vLine[0] + " - " + vLine[2] + " - " + vLine[1] + " - " + vLine[3]);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} */
		}
	}

	// =========================================================================
	public static String xml2html(String fattura, String xsltName) {
		Source text = null;
		Source xslt = null;

		TransformerFactory factory = TransformerFactory.newInstance();
		if (fattura.length() < 65) {
			fattura = temp + "FattNew/in/" + fattura;
			text = new StreamSource(new File(fattura));
		} else text = new StreamSource(new StringReader(fattura));

		if (xsltName.length() < 65) {
//			xsltName = temp + "FattNew/out/" + xsltName;
//			xsltName = getResource(xsltName);
//			System.out.println(xsltName);
//			xslt = new StreamSource(new StringReader(xsltName));
//			xslt = new StreamSource(new File(xsltName));
		} else {
			// Patch xslt
			xslt = new StreamSource(new StringReader(xsltName));
		}

		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		Transformer transformer;
		String strResult = "";
		try {
			transformer = factory.newTransformer(xslt);
//	        transformer.transform(text, new StreamResult(new File(temp + "output.html")));
			transformer.transform(text, result);
			strResult = writer.toString();
		} catch (TransformerException e) {
//			e.printStackTrace();
			System.err.println(fattura);
		}
//		System.out.println(strResult);
//		writeTemp("output.html", strResult, "");
		return strResult;
	}

	// =========================================================================
	public static String getResource(String name) {
		URL url = getResourceURL(name);
		return getUrlString(url);
	}

	// =========================================================================
	public static String getUrlString(URL url) {
		String urlString = "";
		URLConnection conn;
		try {
			conn = url.openConnection();
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()
												,StandardCharsets.UTF_8))) {
				urlString = reader.lines().collect(Collectors.joining("\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		urlString = JPane.removeUTF8BOM(urlString);
		return urlString;
	}
	
	// =========================================================================
	public static URL getResourceURL(String name) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL input = classLoader.getResource(name);
//		System.out.println("--- res:" + input);
		return input;
//		image = ImageIO.read(input);
	}

	// =========================================================================
	public static void pack(String sourceDirPath, String zipFilePath, String filter)
															throws IOException {
		Path p = Files.createFile(Paths.get(zipFilePath));
		try (
			ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
			Path pp = Paths.get(sourceDirPath);
			Files.walk(pp).filter(path -> !Files.isDirectory(path))
				.filter(path -> path.toString().endsWith(filter))
					.forEach(path -> {
						ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
						try {
							zs.putNextEntry(zipEntry);
							Files.copy(path, zs);
							zs.closeEntry();
						} catch (IOException e) {
							System.err.println(e);
						}
					});
		} catch (IOException e) {
			System.err.println(e);			
		}
	}
	
	// =========================================================================
	public static String toCustomBase(final int num, final String base) {
	    final int baseSize = base.length();
	    if(num < baseSize) {
	        return String.valueOf(base.charAt(num));
	    }
	    else {
	        return toCustomBase(num / baseSize - 1, base) + base.charAt(num % baseSize);
	    }
	}

	// =========================================================================
	static String formatNumFat(int num) {
		String tmpNum = "";
		if(num < 1000) tmpNum = formatDecimal(("" + num), "000");
		else {
			final String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			tmpNum = toCustomBase(num -1000, base);
//			tmpNum = Integer.toString(num, 36);
			tmpNum = "000" + tmpNum.toUpperCase();
			tmpNum = tmpNum.substring(tmpNum.length()-3);
		}
		return tmpNum;
	}

	// =========================================================================
	static String nomeFile(String esercizio1, String codGio, int num) {
		String tmpNum = "";
		tmpNum = esercizio1 + codGio + formatNumFat(num);

		return tmpNum;
	}

	// =========================================================================
	public static void main(String[] args) {
		String para = "";
		for(String s : args) {para += s;}

		log("i5", "JPane Main " + JPane.VERSION + " ...");
		log("i5", "Time: " + getDate() + " ...");
		log("i5", "Param: " + para + " ...");

//		for(int i=999;i<10002;i++)
//			System.err.println("--- " + i + " -> " + formatNumFat(i));
//		System.exit(0);

//		bigQuery:select pane.id as pane, i.*, v.val as vv from jam.jam
//						,UNNEST(pane.items) as i, UNNEST(i.values) as v

		String term = "90";
//		System.out.println("--- res:" + getResource("test.html"));

//		xml2html("IT11911290150_00009.xml", "fatturaPA_v1.2.xsl");
//		html2pdf();

//		ComunicazioniIva(term, "$$" + para);
//		System.exit(0);

		JPane frame = new JPane(term, null);
		JPaneDoc requ = frame.new JPaneDoc("req", "");
		JPaneMaster master = frame.new JPaneMaster(term);

		FatturePara(term, "$$" + para, frame, requ, master);

//		JPaneDoc jpNum = master.get("NUM", "");
//		numFattura = jpNum.getItem("id", "x002", "val", 0, 1);
//		System.out.println("--- Numero:" + numFattura);
//		if(numFattura == 0) numFattura = 1;

		new File(temp + "FattNew/in").mkdirs();
		new File(temp + "FattNew/inOK").mkdirs();
		new File(temp + "FattNew/inZip").mkdirs();

		int numFatturaFine = 10000;

		if(numFattura == 0) {
			numFattura = 1;
		} else {
//			numFatturaFine = numFattura;			
		}
/*		for(int i=numFattura; i<=numFatturaFine; i++, numFattura++) {

			if(partitaIva.length() > 0) {
				String tmpFile = "IT" + partitaIva + "_" + nomeFile(esercizio1, codGioCur, i) + ".xml";
				if(Files.isRegularFile(Paths.get(temp + "FattNew/out/" + tmpFile))) {
					log("i9", "Exist:" + tmpFile);
					continue;
				}
			}
			
			frame = new JPane(term, null);
			requ = frame.new JPaneDoc("req", "");
			master = frame.new JPaneMaster(term);
			FatturePara(term, "$$" + para, frame, requ, master);
			
			int ret = FattureOut(term, "$$" + para, numFattura, frame, requ, master);
			if(ret == -1) break;
		}
		*/
//2020
		numFattura	= 1;
		esercizio	= "20";
		esercizio1= esercizio.substring(1);

		String tmpOra
		= "CREATE TABLE jam\n"
		+ "  (ids	VARCHAR2 (100) NOT NULL PRIMARY KEY,\n"
		+ "   dati	CLOB NOT NULL);\n";
//		+ "   CONSTRAINT ensure_json CHECK (dati IS JSON));\n";

		for(int i=numFattura; i<=numFatturaFine; i++, numFattura++) {

/*			if(partitaIva.length() > 0) {
				String tmpFile = "IT" + partitaIva + "_" + nomeFile(esercizio1, codGioCur, i) + ".xml";
				if(Files.isRegularFile(Paths.get(temp + "FattNew/out/" + tmpFile))) {
					log("i9", "Exist:" + tmpFile);
					continue;
				}
			} */

			frame = new JPane(term, null);
			requ = frame.new JPaneDoc("req", "");
			master = frame.new JPaneMaster(term);
			FatturePara(term, "$$" + para, frame, requ, master);

			int ret = FattureOut(term, "$$" + para, numFattura, frame, requ, master);

			JPane5 jp5 = new JPane5();
			tmpJSON tmpJson = jp5.new tmpJSON("{\"sid\":\"" + frame.sid + "\",");

			String paneId = "TD01_FAT";
			String sysId = "main";
			String req = "";
			jp5.JPaneLoadPane(paneId, req, frame, master, sysId, requ, tmpJson);

			tmpJson.add("}");

			JPane.writeTemp("data.json", tmpJson.get() + "\n", "", true);
			tmpOra
			+= "INSERT INTO jam  VALUES ('" + frame.sid + "',\n"
			+ "'" + tmpJson.get() + "');\n";

			jp5 = null;

			if(ret == -1) break;
		}

		tmpOra = tmpOra.replace("&amp;", " e ");
		JPane.writeTemp("orajson.sql", tmpOra, "", false);
//		System.out.println("--- Oracle " + tmpOra);

		new File(temp + "FattNew/zip").mkdirs();

		try {pack(temp + "FattNew/out/"
				,temp + "FattNew/zip/out_" + JPane.getDate() + ".zip", ".xml");
		} catch (IOException e) {e.printStackTrace();}

//		System.err.println(mainLog);
		try(FileWriter fw = new FileWriter(temp + "FattNew/log/jamfive.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)) {
				out.println(mainLog);
				out.close();
		} catch (IOException e) {
		}

//		if(!isWindows())
		try(FileWriter fw = new FileWriter(temp + "FattNew/log/jamfive.sql", false);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw)) {
				out.println(sqlLog);
				out.close();
		} catch (IOException e) {
		}

//		if(!isWindows() && 1 == 1)
		{			
			JPaneUnzip.unzipDir();

			sqlLog = "";
			JPaneMail.decodeFile();
			FattureIn(term, "$$" + para, frame, requ, master);
			sqlLog += "COMMIT;\n";
			sqlLog += "EXIT\n";

			try(FileWriter fw = new FileWriter(temp + "FattNew/log/lettura.sql", false);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
						   out.println(sqlLog);
						   out.close();
				} catch (IOException e) {}
			try(FileWriter fw = new FileWriter(temp + "FattNew/log/lettura.csv", false);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
						   out.println(csvLog);
						   out.close();
				} catch (IOException e) {}
		}

		log("i5", "Fine Programma FattureNew ...");
		System.exit(0);

/*		byte[] b = new byte[1024];
		String input  = "";
		int count = 0;

		while(true) {
		try {
			System.out.print("--- cmd:");
			count = System.in.read(b);
			if(count > 1) {
				input = new String(b);
				input = input.substring(0, count-1);
				System.out.println(input);
			} else {
				System.out.println("--- End Cmd ...");break;
			}
		} catch (IOException e) {e.printStackTrace();}
*/

/*
		requ.setItemJSONupdateAdd("x000", "val", "00000", 1);
		requ.setItemJSONupdateAdd("x001", "val", "valore", 1);
		requ.setItemJSONupdateAdd("x001", "ix", "codice", 1);
		requ.setItemJSONupdateAdd("x001", "val", "val_2", 2);
		requ.setItemJSONupdateAdd("x001", "val", "val_3", 3);
		
		System.out.println("--- Exist: " + requ.existItem("x002"));
		System.out.println("--- getItem: " + requ.getItem("id", "x001", "val", null, 1));
		System.out.println("--- getItemId: " + requ.getItemId("x001", "val", null, 1));
		System.out.println("--- getItemIx: " + requ.getItemIx("codice", "val", null, 1));
		System.out.println("--- getItemIs: " + requ.getItemIs("anam.codice", "val", null, 1));

		System.out.println("--- getItem: " + requ.getItem("id", "x002", "val", "MANCANTE", 1));
		System.out.println("--- Last: " + requ.lastItem());
		System.out.println("--- Inc: " + requ.incItem(requ.lastItem()));

		requ.setItemJSONupdateAdd(requ.incItem(requ.lastItem()), "val", "nuovo", 1);

		System.out.println("--- Last: " + requ.lastItem());

		System.out.println("--- getItem: " + requ.getItem("id", "x002", "val", null, 1));

		System.out.println(requ.jO.toString(4));
*/
		master = JPaneBase.paneBASE(frame, requ, master, term, 1);

		FatturaElettronica fatture = frame.new FatturaElettronica();
		fatture.readFileXml(null);
		while(true) {
			Boolean test = fatture.readNext();
			if(test == false) break;
			frame.execRead(master, "TD01_FAT", "", fatture, "");
			break;
		}

		System.exit(0);

		String cmd = "$def:CATALOG::state-mode:detail"
					+ "::x001:null::-ix:name::-is:CAT.TABLE_NAME::-nkey:1"
					+ "::x002:null::-ix:type::-is:CAT.TABLE_TYPE"
//					+ "$read$html;"
					;
		master = frame.execBase(requ, cmd, 0, requ, master);
/*
		cmd = "$$CATALOG$read$html;";
		master = frame.execBase(requ, cmd, requ, master);

		JPaneDoc jpCatalog = master.get("CATALOG", "");
//		frame.sql.select(jpCatalog, "GLOBE", "MANAGER", "217.133.32.75:1521:XE")
//					.execSQL(jpCatalog, null);

		System.out.println(jpCatalog.jO.toString(4));
	
		System.out.println("--- " + jpCatalog.jO.query("/pane/items/1/id"));
		System.out.println("--- " + jpCatalog.jO.query("/pane/items/1/val"));
*/
/*
		String sqlTxt = "REM CON GLOBE/MANAGER@217.133.32.75:1521/XE;"
				+ "-- jdbc:oracle:thin:hr/hr@217.133.32.75:1521:XE\n"
				+ "select * from cat order by 1;";		
		sql.execSQL(requ, sqlTxt);
		sql.close();
*/
//		sql.items.list(System.out);
//		}
		System.out.println("--- JPane Main END ...");

		cmd 	= "$def:tabiva"
				+ "::x001:null::-ix:CodIva::-len:80::-val:"+JPane.vEnc("18|22|ES")+"::-tit:CodIva::-nkey:1"
				+ "::x002:null::-ix:DesIva::-len:80::-val:"+JPane.vEnc("Iva 18%|Iva 22%|Iva Esente")+"::-tit:DesIva"
				+ "::x003:null::-ix:Esenzione::-len:80::-val:"+JPane.vEnc("null|null|Art.8")+"::-tit:Esen"
				+ "::x004:null::-ix:Percentuale::-len:80::-val:"+JPane.vEnc("18|22|0")+"::-tit:Perc"
				+ "::x898:null::-ix:NewCode::-tit:NewCode::-type:Button::-exec:"+JPane.vEnc("$NewCode")
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$OkSalva")
				;
		master = frame.execBase(requ, cmd, 0, requ, master);

//		cmd		= "$$tabiva$csv:Codifiche-103.csv;";
//		master = frame.execBase(requ, cmd, 0, requ, master);

		JPaneDoc tabiva = master.get("tabiva", "");
//				frame.new JPaneDoc("tabiva", "");

//		frame.new READ_XSLX(temp + "Codifiche-103.csv", tabiva);
		System.out.println(tabiva.jO.toString(4));
		
		cmd = "$def:CUST::state-mode:detail"
				+ "::x001:null::-ix:code::-is:CUST.CODE::-nkey:1::-nseg:100"
				+ "::x002:null::-ix:des::-is:CUST.DES"
				+ "::x003:null::-ix:dbuser::-is:CUST.DBUSER"
				+ "::x004:null::-ix:dbpwd::-is:CUST.DBPWD"
				+ "::x005:null::-ix:dbserver::-is:CUST.DBSERVER"
				;
		master = frame.execBase(requ, cmd, 0, requ, master);
		cmd	= "$$CUST"
//				+   "::x001:PIERO::x002:piero_sbarato::x003:GLOBE::x004:MANAGER"
//				+	"::x005:" + JPane.vEnc("217.133.32.75:1521:XE")
//				+	"::x001:MAURO::x002:mauro_marino::x003:GLOBE::x004:MANAGER"
//				+	"::x005:" + JPane.vEnc("93.43.22.2:1522:XE")
				+	"::x001:JAM70::x002:Sunrise::x003:SUNRISE::x004:MANAGER"
				+	"::x005:" + JPane.vEnc("217.133.32.75:1521:XE")
				;

		master = frame.execBase(requ, cmd, 0, requ, master);
		JPaneDoc jpCust = master.get("CUST", "");
		System.out.println(jpCust.jO.toString(4));

		master.listSort(0);
	}

	// =========================================================================
	static void FatturePara(String term, String cust
							,JPane frame, JPaneDoc requ, JPaneMaster master) {
		String cmd = "";
		master = JPaneBase.paneBASE(frame, requ, master, term, 1);
		cmd	= "$def:CUST"
			+ "::x001:JAM70::-ix:utente::-nkey:1::-nseg:100"
			+ "::x002:Formoto::-ix:nome"
			+ "::x003:GLOBE::-ix:usr" + "::x004:MANAGER::-ix:pwd"
			+ "::x005:" + JPane.vEnc("93.148.232.79_1522_XE") + "::-ix:svr"

			+ "::x002:Jamfive::-ix:nome"
			+ "::x003:SUNRISE::-ix:usr" + "::x004:MANAGER::-ix:pwd"
			+ "::x005:" + JPane.vEnc("217.133.32.75:1521:XE") + "::-ix:svr"
			+ "::x006:2::-ix:vendite"
			;

//		"CUST::x001:JAM50::x002:Formoto::x003:GLOBE::x004:MANAGER::x005:93.148.232.79_1522_XE::x006:2"

		master = frame.execBase(requ,  cmd, 0, requ, master);
		master = frame.execBase(requ,  cust, 0, requ, master);

		JPaneDoc jpCust = master.get("CUST", "");
		utente = jpCust.getItem("ix", "utente", "val", "", 1);

		cmd	= "$def:NUM"
			+ "::x001:1::-ix:giornale::-nkey:1::-nseg:100"
			+ "::x002:1::-ix:numero"
			;	
		master = frame.execBase(requ,  cmd, 0, requ, master);

//		if(!utente.equals("PETRA71")) {
//			cmd = "$$NUM$read;";
//			master = frame.execBase(requ,  cmd, 0, requ, master);
//		}

		log("i5", "Utente:" + utente);
	}

	// =========================================================================
	static int FattureOut(String term, String cust, Integer i
							,JPane frame, JPaneDoc requ, JPaneMaster master) {
		int flagReturn	= 0;
		numFattura		= i;
		String cmd		= "";

		cmd = "$$TD01_FAT$read;";
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$$TD01_IVA$read;";
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$$TD01_LINEE$read;";
		master = frame.execBase(requ, cmd, 0, requ, master);

		JPaneDoc jpPdt = master.get("TD01_PAN", "");
		JPaneDoc jpPdt2 = master.get("TD01_PDT", "");
		partitaIva = jpPdt.getItem("id", "x002", "val", "", 1);
		if(partitaIva.length() < 8) return -1;
		
		JPaneDoc jpLinee = master.get("TD01_LINEE", "");
		tmpGetBolla(jpLinee);
		String numBolla = jpLinee.getItem("id", "x015", "val", "", 1);
		String dataBolla = jpLinee.getItem("id", "x016", "val", "", 1);
//		System.out.println("--- Bolla:" + numBolla + " Data:" + dataBolla);
		String bollaXml = "<DatiDDT>\n"
				+ "<NumeroDDT>" + numBolla + "</NumeroDDT>\n"
				+ "<DataDDT>" + formatDate(dataBolla, "yyyy-MM-dd", null) + "</DataDDT>\n"
				+ "</DatiDDT>\n";

		JPaneDoc jpPag = master.get("TD01_PAG", "");
		String codPag = jpPag.getItem("id", "x001", "val", "", 1);
		String modPag = jpPag.getItem("id", "x002", "val", "", 1);
		String iban = vDec(jpPag.getItem("id", "x020", "val", "", 1));
		String rata1 = jpPag.getItem("id", "x003", "val", "", 1);
		String rata2 = jpPag.getItem("id", "x016", "val", "", 1);
		String data2 = jpPag.getItem("id", "x017", "val", "", 1);
		String importoBollo = jpPag.getItem("id", "x010", "val", "", 1);
		String importoSpese = jpPag.getItem("id", "x013", "val", "", 1);
		String importoSconto = jpPag.getItem("id", "x011", "val", "", 1);

		String ritenutaImponibile = jpPag.getItem("id", "x014", "val", "", 1);
		String ritenutaAliquota = jpPag.getItem("id", "x014b", "val", "", 1);
		String ritenutaImporto = jpPag.getItem("id", "x014c", "val", "", 1);
		String ritenutaCodice = jpPag.getItem("id", "x014d", "val", "", 1);

		String codiceId = jpPdt2.getItem("id", "x005", "val", "", 1);

		if(!isWindows()) System.err.println("--- RIT_IMPONIBILE:" + ritenutaImponibile);
		if(!isWindows()) System.err.println("--- RIT_ALIUOTA   :" + ritenutaAliquota);
		if(!isWindows()) System.err.println("--- RIT_IMPORTO   :" + ritenutaImporto);
		if(!isWindows()) System.err.println("--- RIT_CODICE    :" + ritenutaCodice);

		JPaneDoc jpBdg = master.get("TD01_BDG", "");
		String totale = jpBdg.getItem("id", "x006", "val", "", 1);

		if(!isWindows()) System.err.println("--- SPESE:" + importoSpese);
		if(!isWindows()) System.err.println("--- RATA:" + rata1);
		if(!isWindows()) System.err.println("--- RATA2:" + rata2);
		if(!isWindows()) System.err.println("--- TOTA:" + totale);

		String xmlCUP = null;
		String CupCodice = jpPag.getItem("id", "x014f", "val", "", 1);
		if(!isWindows()) System.err.println("--- CUP:" + CupCodice);
		if(!isWindows()) System.err.println("--- COD:" + codiceId);
		if(CupCodice.length() > 1) {
			String CupCodiceCIG = jpPag.getItem("id", "x014g", "val", "", 1);
			String CupCommessa = jpPag.getItem("id", "x014h", "val", "", 1);
			String CupData = jpPag.getItem("id", "x014i", "val", "", 1);
			String CupId = jpPag.getItem("id", "x014l", "val", "", 1);

			xmlCUP
			= "<DatiContratto>\n"
			+ "<IdDocumento>" + CupId + "</IdDocumento>\n"
			+ "<Data>" + formatDate(CupData, "yyyy-MM-dd", null) + "</Data>\n"
			+ "<CodiceCommessaConvenzione>" + CupCommessa + "</CodiceCommessaConvenzione>\n"
			+ "<CodiceCUP>" +CupCodice + "</CodiceCUP>\n"
			+ "<CodiceCIG>" +CupCodiceCIG + "</CodiceCIG>\n"
			+ "</DatiContratto>\n"
			;
		}
	
		if(rata1.equalsIgnoreCase("0")) {
			cmd = "$$TD01_PAG::x003:" + totale;
			master = frame.execBase(requ, cmd, 0, requ, master);			
		}
		
		if(!modPag.equalsIgnoreCase("MP05") // bonifico
		 &&!modPag.equalsIgnoreCase("MP02") // assegno
		 &&!modPag.equalsIgnoreCase("MP12") // riba
		 ) {
			cmd = "$$TD01_PAG::x015:null";
			master = frame.execBase(requ, cmd, 0, requ, master);
		}

		String bolliXml = "</Numero>\n<DatiBollo>\n"
				+ "<BolloVirtuale>SI</BolloVirtuale>\n"
				+ "<ImportoBollo>" + formatDecimal(importoBollo, "##########0.00")
				+ "</ImportoBollo>\n"
				+ "</DatiBollo>\n";

		String scontoXml = "<ScontoMaggiorazione>\n"
				+ "<Tipo>SC</Tipo>\n"
				+ "<Importo>" + importoSconto + "</Importo>\n"
				+ "<ScontoMaggiorazione>\n"
				;

		String ritenutaXml = "<DatiRitenuta>\n"
				+ "<TipoRitenuta>RT01</TipoRitenuta>\n"
				+ "<ImportoRitenuta>" + formatDecimal(ritenutaImporto, "##########0.00") + "</ImportoRitenuta>\n"
				+ "<AliquotaRitenuta>"  + formatDecimal(ritenutaAliquota, "##########0.00") + "</AliquotaRitenuta>\n"
				+ "<CausalePagamento>" + ritenutaCodice + "</CausalePagamento>\n"
				+ "</DatiRitenuta>\n";

		if(!isWindows()) System.err.println(ritenutaXml);
/*
		FATMVT.TOT_VARIE_V  =  totale ritenuta
		FATMVT.TOT_IVATO_V  = imponibile non ivato soggetto ritenuta
		MAGMVT.RIT_ACC_XI    = % RITENUTA
*/
		String rate = "<DettaglioPagamento>\n"
				+ "<ModalitaPagamento>" + modPag + "</ModalitaPagamento>\n"
				+ "<DataScadenzaPagamento>" + formatDate(data2, "yyyy-MM-dd", null)
				+ "</DataScadenzaPagamento>\n"
				+ "<ImportoPagamento>" + formatDecimal(rata2, "##########0.00")
				+ "</ImportoPagamento>\n"
				+ "<IBAN>" + iban + "</IBAN>\n" + 
				"</DettaglioPagamento>\n"
				+ "</DatiPagamento>\n"
				;

//		if(!isWindows())
//			System.err.println("--- Pag:" + codPag + " mod:" + modPag + " rata2:" + rata2
//				+ " Sconto:" + importoSconto + " bollo:" + importoBollo);

//		if(!isWindows())
//			System.err.println("" + rate);

		JPaneDoc jpCan = master.get("TD01_CAN", "");
		String naz = jpCan.getItem("id", "x001", "val", "", 1);
//		System.out.println("--- Nazione:" + naz);

		codGioCur = jpBdg.getItem("id", "x002", "val", "", 1);
		cmd = "$$TD01_PDT::x003:"
				+ nomeFile(esercizio1, codGioCur, numFattura);

//		"OO 99999999999" extra ue

//		if(!naz.equalsIgnoreCase("IT")) cmd += "::x005:XXXXXXX::x006:null";
		master = frame.execBase(requ, cmd, 0, requ, master);

//		cmd = "$$TD01_BDG::x004:" + formatDate(null, "yyyy-MM-dd");;
//		master = frame.execBase(requ, cmd, 0, requ, master);		

		String esenzione = vDec(jpBdg.getItem("id", "x008", "val", "", 1));
		log("i5", "Esenzione:" + esenzione);

		String descam = vDec(jpBdg.getItem("id", "x007b", "val", "", 1));
//		log("i5", "Descam:" + descam);

		String descam2 = vDec(jpBdg.getItem("id", "x007c", "val", "", 1));
		log("i5", "Descam2:" + descam2);

		String fattImm = vDec(jpBdg.getItem("id", "x007d", "val", "", 1));
		log("i5", "FattImm:" + fattImm);

		String codCau = jpBdg.getItem("id", "x007", "val", "", 1);
		causale = codCau
//				+ " Bolla " + numBolla
//				+ " del " + formatDate(dataBolla, "dd/MM/yyyy", null)
				;
		cmd = "$$TD01_BDG::x007:" + vEnc(causale  // + " " + descam 
										+ " " + descam2 
										+ " " + esenzione);
		master = frame.execBase(requ, cmd, 0, requ, master);

		String nomeFile = "IT" + partitaIva + "_"
						+ nomeFile(esercizio1,codGioCur, numFattura);

		String ritorno	= "";
		Properties param = new Properties();
		param.setProperty("sys.terminal", frame.terminal);
		param.setProperty("sys.xmlPath", "");
		param.setProperty("sys.path", "");

		tmpBolla = null;
		ritorno += frame.paneViewRend(master, "TD01_FAT", "jamFat",  1, param);
//		if(!isWindows()) System.err.println(tmpBolla);
		
		ritorno = ritorno.replace("</Denominazione>\n</DatiAnagrafici>", "</Denominazione>\n</Anagrafica>\n</DatiAnagrafici>");
		ritorno = ritorno.replace("</FatturaElettronicaHeader>", "</CessionarioCommittente>\n</FatturaElettronicaHeader>");
		ritorno = ritorno.replace("<CessionarioCommittente>", "</CedentePrestatore>\n<CessionarioCommittente>");
		ritorno = ritorno.replace("</DatiPagamento>", "</DettaglioPagamento>\n</DatiPagamento>");

		ritorno = ritorno.replace("<TipoDocumento>*</TipoDocumento>"
				,"<TipoDocumento>TD01</TipoDocumento>");
		ritorno = ritorno.replace("<FormatoTrasmissione></FormatoTrasmissione>"
				,"<FormatoTrasmissione>FPR12</FormatoTrasmissione>");
		
//		if(naz.equalsIgnoreCase("")) naz = "IT";
//		if(naz.equalsIgnoreCase(" ")) naz = "IT";

		if(naz.equalsIgnoreCase("IT")) {
			ritorno = ritorno.replace("<CodiceDestinatario> </CodiceDestinatario>"
						,"<CodiceDestinatario>0000000</CodiceDestinatario>");
			ritorno = ritorno.replace("<CodiceDestinatario></CodiceDestinatario>"
						,"<CodiceDestinatario>0000000</CodiceDestinatario>");
			ritorno = ritorno.replace("<IdCodice> </IdCodice>\n", "");
			ritorno = ritorno.replace("<IdCodice>.</IdCodice>\n", "");
			ritorno = ritorno.replace("<IdCodice></IdCodice>\n", "");
			ritorno = ritorno.replace("<IdFiscaleIVA>\n" + 
										"<IdPaese>IT</IdPaese>\n" + 
										"</IdFiscaleIVA>\n", "");
		} else {
			ritorno = ritorno.replace("<IdCodice> </IdCodice>"
										,"<IdCodice>99999999999</IdCodice>");
			ritorno = ritorno.replace("<IdCodice></IdCodice>"
										,"<IdCodice>99999999999</IdCodice>");
		}

		ritorno = ritorno.replace("<IdPaese></IdPaese>", "<IdPaese>IT</IdPaese>");
		ritorno = ritorno.replace("<IdPaese> </IdPaese>", "<IdPaese>IT</IdPaese>");

		ritorno = ritorno.replace("<Nazione></Nazione>", "<Nazione>IT</Nazione>");
		ritorno = ritorno.replace("<Nazione> </Nazione>", "<Nazione>IT</Nazione>");

		if(ritenutaImporto.length() > 0 && !ritenutaImporto.equalsIgnoreCase("0")) {
			ritorno = ritorno.replace("<ImportoTotaleDocumento>"
					, ritenutaXml + "<ImportoTotaleDocumento>");
			
		}
		
		if(xmlCUP != null)
			ritorno = ritorno.replace("</DatiGenerali>", xmlCUP + "</DatiGenerali>");

		if(importoSpese.length() > 0 && !importoSpese.equalsIgnoreCase("0")) {
			ritorno = ritorno.replace("</DettaglioLinee>\n<!-- TD01_IVA -->"
					,"</DettaglioLinee>\n" +
					"<DettaglioLinee>\n" +
					"<NumeroLinea>998</NumeroLinea>\n" +
					"<Descrizione>Spese Incasso</Descrizione>\n" +
					"<Quantita>1.00</Quantita>\n" + 
					"<UnitaMisura>N</UnitaMisura>\n" + 
					"<PrezzoUnitario>" + formatDecimal(importoSpese, "##########0.00")
						+ "</PrezzoUnitario>\n" + 
					"<PrezzoTotale>" + formatDecimal(importoSpese, "##########0.00")
						+ "</PrezzoTotale>\n" + 
					"<AliquotaIVA>22.00</AliquotaIVA>\n"
					+ "</DettaglioLinee>\n<!-- TD01_IVA -->");

			if(ritorno.contains("<Natura>N3</Natura>"))
				ritorno = ritorno.replace(
					"<AliquotaIVA>22.00</AliquotaIVA>\n"
					+ "</DettaglioLinee>\n"
					+ "<!-- TD01_IVA -->\n"
					,"<AliquotaIVA>0.00</AliquotaIVA>\n"
					+ "<Natura>N3</Natura>\n"
					+ "</DettaglioLinee>\n"
					+ "<!-- TD01_IVA -->\n"
					);
		}
		if(!rata2.equalsIgnoreCase("0")
		 &&rata2.length() > 0
		 &&!rata2.equalsIgnoreCase("null")
//		 &&modPag.equalsIgnoreCase("MP05")
		 )
			ritorno = ritorno.replace("</DatiPagamento>\n", rate);
		
		if(!importoBollo.equalsIgnoreCase("0")
		 && importoBollo.length() > 0
		 &&!importoBollo.equalsIgnoreCase("null")) {
			ritorno = ritorno.replace("</Numero>\n", bolliXml);
			
			ritorno = ritorno.replace("</DettaglioLinee>\n<!-- TD01_IVA -->"
					,"</DettaglioLinee>\n" +
					"<DettaglioLinee>\n" +
					"<NumeroLinea>999</NumeroLinea>\n" +
					"<Descrizione>Imposta Bollo Assolta in Modo Virtuale</Descrizione>\n" +
					"<Quantita>1.00</Quantita>\n" + 
					"<UnitaMisura>N</UnitaMisura>\n" + 
					"<PrezzoUnitario>" + formatDecimal(importoBollo, "##########0.00")
						+ "</PrezzoUnitario>\n" + 
					"<PrezzoTotale>" + formatDecimal(importoBollo, "##########0.00")
						+ "</PrezzoTotale>\n" + 
					"<AliquotaIVA>0.00</AliquotaIVA>\n"
					+ "<Natura>N1</Natura>\n"
					+ "</DettaglioLinee>\n<!-- TD01_IVA -->");
		}

		if(tmpOrdine.length()> 0)
			ritorno = ritorno.replace("</DatiGenerali>", tmpOrdine + "</DatiGenerali>");
		tmpOrdine = "";

		if(tmpBolla != null) bollaXml = tmpBolla; 
		if(!fattImm.equalsIgnoreCase("S"))
			ritorno = ritorno.replace("</DatiGenerali>", bollaXml + "</DatiGenerali>");

		ritorno = ritorno.replace("<UnitaMisura> </UnitaMisura>", "<UnitaMisura></UnitaMisura>");
		ritorno = ritorno.replace("<UnitaMisura></UnitaMisura>", "<UnitaMisura>N</UnitaMisura>");
		
		ritorno = ritorno.replace("<Ritenuta> </Ritenuta>\n", "");
		ritorno = ritorno.replace("<Ritenuta></Ritenuta>\n", "");

		ritorno = ritorno.replace("<CodiceValore></CodiceValore>", "<CodiceValore>.</CodiceValore>");

		ritorno = ritorno.replace("<PECDestinatario></PECDestinatario>\n", "");
		ritorno = ritorno.replace("<PECDestinatario> </PECDestinatario>\n", "");

		ritorno = ritorno.replace("<Natura> </Natura>\n", "");
		ritorno = ritorno.replace("<Natura>*</Natura>\n", "");

		ritorno = ritorno.replace("<CAP> </CAP>\n", "<CAP>00000</CAP>");
		ritorno = ritorno.replace("<CAP></CAP>\n", "<CAP>00000</CAP>");

		ritorno = ritorno.replace("<Provincia></Provincia>\n", "");
		ritorno = ritorno.replace("<Provincia> </Provincia>\n", "");

		ritorno = ritorno.replace("<CodiceFiscale></CodiceFiscale>\n", "");
		ritorno = ritorno.replace("<CodiceFiscale> </CodiceFiscale>\n", "");
		ritorno = ritorno.replace("<CodiceFiscale>.</CodiceFiscale>\n", "");

		ritorno = ritorno.replace("<IstitutoFinanziario> </IstitutoFinanziario>\n", "");
		ritorno = ritorno.replace("<IBAN> </IBAN>\n", "");
		ritorno = ritorno.replace("<IBAN></IBAN>\n", "");

		ritorno = ritorno.replace("<EsigibilitaIVA></EsigibilitaIVA>", "<EsigibilitaIVA>I</EsigibilitaIVA>");
		ritorno = ritorno.replace("<EsigibilitaIVA> </EsigibilitaIVA>", "<EsigibilitaIVA>I</EsigibilitaIVA>");

		if(codiceId.length() == 6) {
			ritorno = ritorno.replace("FPR12", "FPA12");
		}
		ritorno = ritorno.replace("null","");
		ritorno = ritorno.replace("-- TD01_HEA", "-- TD01_HEA:" 
				+ utente + " " + VERSION + " ");

//		ritorno = formatXml(ritorno);

		if(!Files.isRegularFile(Paths.get(temp + "FattNew/out/" + nomeFile + ".xml")))
			writeTemp(nomeFile + ".xml", ritorno, "FattNew/outNew", false);

		writeTemp(nomeFile + ".xml", ritorno, "FattNew/out", false);

//		String tmp = xml2html(ritorno, "Fatturapa_v1.101.xsl");
//		writeTemp(nomeFile + ".htm", tmp, "FattNew/out");
//		writeTemp(nomeFile + ".htm", tmp, "FattNew/outNew");

//		if(!isWindows())
		if(false)
		{
			String tmp2 = xml2html(ritorno, "FatturaPA_v1.2.xsl");
			writeTemp(nomeFile + ".html", tmp2, "FattNew/out", false);
			writeTemp(nomeFile + ".html", tmp2, "FattNew/outNew", false);

//			String tmpXsl = getResource("fatturaPA_v1.2.xsl");
//			writeTemp("fatturapa_v1.2.xsl", tmpXsl, "FattNew/out");
		}

//		JPaneDoc jpF = master.get("CUST", "");
//		System.out.println(jpF.jO.toString(4));

//		System.out.println(ritorno);

//		System.out.println("--- " + new JSONSerializer().prettyPrint(false)
//													.deepSerialize(jpPdt));

		log("i5", "Creata:" + nomeFile);

		frame.sql.close();
		return flagReturn;
//		master.listSort(0);
	}

	// =========================================================================
	static void ComunicazioniIva(String term, String cust, Integer i
							,JPane frame, JPaneDoc requ, JPaneMaster master) {

		String cmd = "";
		cmd = "$def:IVP::state-mode:master"
				+ "::x001:01::-is:CONFG.COD_DITTA::-nkey:1::-nseg:1::-inp:n"
				+ "::x002:null::-view:" + JPane.vEnc("$$IVP_HEA")
				;
		master = frame.execBase(requ, cmd, 0, requ, master);
				
		cmd = "$def:IVP_HEA::state-mode:master"
				+ "::x002:IVP18::-x:CodiceFornitura::-xmlPath:Intestazione/CodiceFornitura"
				+ "::x003:2018::-ix:AnnoImposta::-xmlPath:AnnoImposta"
				+ "::x004:null::-ix:PartitaIva::-xmlPath:PartitaIva::-is:CONFG.PA_IVA_DITTA"
				+ "::x005:null::-ix:CodiceFiscale::-xmlPath:CodiceFiscale::-is:CONFG.COD_FI_DITTA"
				+ "::x006:null::-ix:CFDichiarante::-xmlPath:CFDichiarante::-is:CONFG.COD_FISCALE_RAPP"
				+ "::x007:null::-ix:CFIntermediario::-xmlPath:CFIntermediario::-is:CONFG.COD_FISC_INT"
				+ "::x008:null::-ix:DataImpegno::-xmlPath:DataImpegno::-type:Date::-is:CONFG.DATA_IMP"
				+ "::x009:1::-ix:FirmaDichiarazione::-xmlPath:FirmaDichiarazione"
				+ "::x010:1::-ix:CodiceCaricaDichiarante::-xmlPath:CodiceCaricaDichiarante"
				+ "::x011:1::-ix:ImpegnoPresentazione::-xmlPath:ImpegnoPresentazione"
				+ "::x012:1::-ix:FirmaIntermediario::-xmlPath:FirmaIntermediario"
				+ "::x013:null::-exec:" + JPane.vEnc("$$IVP_DET")
				;
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$def:IVP_DET::state-mode:detail::state-xmlPath:Modulo"
				+ "::x001:11::-ix:Mese::-nkey:0::-nseg:0::-xmlPath:Mese"
				+ "::-is:TBXL.PERIODO"
				+ "::x002:0::-ix:TotaleOperazioniAttive::-xmlPath:TotaleOperazioniAttive"
				+ "::-is:TBXL.TOT_IMP_ATT_01::-isSum:is"
				+ "::x003:0::-ix:TotaleOperazioniPassive::-xmlPath:TotaleOperazioniPassive"
				+ "::-is:TBXL.TOT_IMP_PAS_01::-isSum:is"
				;
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$$IVP$read;";
		master = frame.execBase(requ, cmd, 0, requ, master);

//		cmd = "$$IVP_DET$sql:UTXML.SQL$read;";
		cmd = "$$IVP_DET$read;";
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd ="$$jamFat_base_master"
		+ "::x002:" + JPane.vEnc("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<iv:Fornitura"
		+ " xmlns:iv='urn:www.agenziaentrate.gov.it:specificheTecniche:sco:ivp'"
		+ " xmlns:ds='http://www.w3.org/2000/09/xmldsig#'>\n")
		+ "::x090:" + JPane.vEnc("</iv:Fornitura>\n")
		;
		master = frame.execBase(requ, cmd, 0, requ, master);

		String ritorno	= "";
		Properties param = new Properties();
		param.setProperty("sys.terminal", frame.terminal);
		param.setProperty("sys.xmlPath", "");
		param.setProperty("sys.path", "");

		ritorno += frame.paneViewRend(master, "IVP", "jamFat",  1, param);
		
		JPaneDoc jp = master.get("IVP_DET", "");
		log("w5",  "\n" + ritorno + "\n" + jp.jO.toString(4));
	}

	// =========================================================================
	static void FattureIn(String term, String cust
							,JPane frame, JPaneDoc requ, JPaneMaster master) {
		String cmd = "";

		cmd = "$def:DIR::state-mode:detail"
				+ "::x001:null::-ix:path::-nkey:1::-nseg:100::-inp:n"
				+ "::x002:null::-ix:type"
				+ "::x003:null::-ix:name"
				+ "::x004:null::-ix:full"
				+ "::x005:null::-ix:date::-type:date"
				+ "::x006:null::-ix:size::-type:number"
				;
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$$DIR$dir:xml$sort:x003"
//			+ "$loop:" + JPane.vEnc(
//				"$$IVP"
//				+ "$readfattura:" + JPane.vEnc("@{DIR::full}")
//				)
;
		master = frame.execBase(requ, cmd, 0, requ, master);

		String ritorno = "";
		JPaneDoc jp = master.get("DIR", "");
		int rigMax	= jp.getItem("id", "state", "linemax"	, 1, 1);
		log("i5",  "Lettura Fattura: lines:" + rigMax +"\n" + ritorno
//				+ "\n" + jp.jO.toString(0)
				);

		for(int i=1; i<= rigMax; ++i) {
			ritorno = vDec(jp.getItem("id", "x004", "val", "", i));
//			if(ritorno.length() > 23) continue;
			if(ritorno.contains("_MT_"))	 continue;

//			ritorno = "IT01895030995_0BZKG.xml";
//			ritorno = "IT03963511005_F000O.xml";
//			ritorno = "IT00439110966_00001.xml";
//			ritorno = "IT03386690170_00014.xml";
//			ritorno = "IT10596540152_00Gp3.xml";

			String nomeFile = ritorno.replaceAll(".xml", "");

			log("i5",  "Legge:" + ritorno);
			if(nomeFile.length() == 0) return;

//			FatturaXml2json(ritorno, nomeFile);
//			master = JPaneBase.paneBASE(frame, requ, master, term, 1);

			FatturaElettronica fatture = frame.new FatturaElettronica();
			fatture.readFileXml(ritorno);

			if(sqlLog.length() == 0) {
				sqlLog = "set define off\n"
						+ "TRUNCATE TABLE FECGIVAT;\n"
						+ "TRUNCATE TABLE FECGIVAR;\n"
						+ "ALTER SESSION SET NLS_NUMERIC_CHARACTERS='.,';\n"
						;
			}
			rcvLog = "";
			sqlIns = "";
			sqlInsVal = "";
			frame.execRead(master, "TD01_FAT", "", fatture, "FatturaElettronica");
			sqlInsVal += ");\n";
			sqlInsVal = sqlInsVal.replace("VALUES(1", "VALUES(" + i);
			sqlInsVal = sqlInsVal.replace(",'null','EUR',", ",'7','EUR',");
			sqlInsVal = sqlInsVal.replace(");", ",'" + ritorno + "');");

			sqlIns += ")\n";
			sqlIns = sqlIns.replace(",CGIVAT.CAU_CONTAB)"
					,",CGIVAT.CAU_CONTAB,CGIVAT.NOME_FILE)");

			sqlLog +=  sqlIns + sqlInsVal;
			
			csvLog += rcvLog + ",\"" + ritorno + "\"\n";

			sqlIns = "";
			sqlInsVal = "";
			frame.execRead(master, "TD01_IVA", "", fatture
				,"FatturaElettronica/FatturaElettronicaBody/DatiBeniServizi");
			sqlInsVal = sqlInsVal.replace("VALUES(1", "VALUES(" + i);
			sqlInsVal = sqlInsVal.replace("'22','null'", "'22','22'");
			sqlInsVal = sqlInsVal.replace("'10','null'", "'10','10'");
			sqlInsVal = sqlInsVal.replace("'4','null'", "'4','4'");
			sqlLog +=  sqlIns + ") " + sqlInsVal + ");\n";

			if(sqlIns2.contains("IMP_D_VAL")) {
				sqlInsVal2 = sqlInsVal2.replace("VALUES(1", "VALUES(" + i);
				sqlInsVal2 = sqlInsVal2.replace("'22','null'", "'22','22'");
				sqlInsVal2 = sqlInsVal2.replace("'10','null'", "'10','10'");
				sqlInsVal2 = sqlInsVal2.replace("'4','null'", "'4','4'");
				sqlLog +=  sqlIns2 + ") " + sqlInsVal2 + ");\n";
				
//				System.err.println("--- " + sqlIns2 + ") " + sqlInsVal2 + ");\n");
			}
			
			if(sqlIns3.contains("IMP_D_VAL")) {
				sqlInsVal3 = sqlInsVal3.replace("VALUES(1", "VALUES(" + i);
				sqlInsVal3 = sqlInsVal3.replace("'22','null'", "'22','22'");
				sqlInsVal3 = sqlInsVal3.replace("'10','null'", "'10','10'");
				sqlInsVal3 = sqlInsVal3.replace("'4','null'", "'4','4'");
				sqlLog +=  sqlIns3 + ") " + sqlInsVal3 + ");\n";
				
//				System.err.println("--- " + sqlIns3 + ") " + sqlInsVal3 + ");\n");
			}
		}
		sqlLog = sqlLog.replace("_FAT_DARE", "_FAT_AVERE");
		sqlLog = sqlLog.replace("_A_VAL", "_D_VAL");
		sqlLog = sqlLog.replace("INTO CGIVAT", "INTO FECGIVAT");
		sqlLog = sqlLog.replace("INTO CGIVAR", "INTO FECGIVAR");
		sqlLog = sqlLog.replace("CGIVAT.", "");
		sqlLog = sqlLog.replace("CGIVAR.", "");
		sqlLog = sqlLog.replace("'null'", "''");
//		JPaneDoc jp = master.get("DIR", "");
//		log("e5",  "\n" + ritorno + "\n" + jp.jO.toString(4));
	}

	static String tmpGetBolla(JPaneDoc jpLinee) {
		String xmlBolla = "";
		
		if(!isWindows()) log("e5",  "Bolla:" + xmlBolla);		
		return xmlBolla;
	}

	void XMLValidate() {
		try {
			DocumentBuilder  parser = DocumentBuilderFactory.newInstance()
														.newDocumentBuilder();
			org.w3c.dom.Document document = parser.parse(new File("instance.xml"));
			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory
							.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Source schemaFile = new StreamSource(new File("mySchema.xsd"));
			Schema schema = factory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
		    validator.validate(new DOMSource(document));
		} catch (ParserConfigurationException | SAXException | IOException e2) {
			e2.printStackTrace();
		}
	}
}