package com.JPane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.JPane.JPane.JPaneDoc;
import com.JPane.JPane.JPaneLoop;
import com.JPane.JPane.JPaneMaster;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

// =============================================================================
//
// =============================================================================
public class JPane5 {
	static String svrName = "https://project-3808894954146195088.firebaseio.com";
	static String svrRestOra = "http://localhost:8081/ords/";
	JPane jp;

	static void log(String key, String log) {
		System.out.println("--- " + log);
	}

	// =========================================================================
	public String JPaneSave(String data) {
		String paneId = "test";
		putCloud("/html/" + paneId, null, data, "POST");
		return "";
	}

	// =========================================================================
	public String JPaneReq(String req) {
		String paneId = "TD01_FAT";
		jp = new JPane();
		if(req.contains("/pane")) return getPane(null, paneId, null);

		if(req.contains("/load")) return JPaneLoad(req);
		if(req.contains("/find")) return JPaneLoad(req);

		return "<pre>"
			+ "--- Server: " + JPane.VERSION 
			+ "\n"
			+ JPane5.evalNew("{\"pane\":{\"id\":1,\"date\":33}}", "10", 1, null)
			+ "</pre>";
	}

	// =========================================================================
	public static String getRestOra(String group, String pwd, String sid, String sql) {
		String svr = svrRestOra + group + "/_/sql";
//		svr += "?print=pretty";
		String tmpPane = "";
		URL url;
		try {
			url = new URL(svr);
			URLConnection con = url.openConnection();
			con.setDoInput(true);
            con.setDoOutput(true);
			HttpURLConnection http = (HttpURLConnection) con;
			String userpass = group + ":" + pwd;
			String basicAuth = "Basic " 
				+ new String(Base64.getEncoder().encode(userpass.getBytes()));
			http.setRequestProperty ("Authorization", basicAuth);
			http.setRequestMethod("POST");
			http.setRequestProperty("Content-Type", "application/sql");
			http.setRequestProperty("Accept", "application/json");

			try(OutputStream os = con.getOutputStream()) {
			    byte[] input = sql.getBytes("utf-8");
			    os.write(input, 0, input.length);           
				os.flush();
				os.close();
			}

			int responseCode = http.getResponseCode();

			InputStream ins = http.getInputStream();
			InputStreamReader isr = new InputStreamReader(ins);
			BufferedReader in = new BufferedReader(isr);
			String inputLine;

			System.out.println("--- Read Data " + group + " sql:" + sql);

			while ((inputLine = in.readLine()) != null) {
				tmpPane += inputLine + "\n";
			}

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpPane;
	}

	// =========================================================================
	public static String getCloud(String group, String sid) {
		String svr = svrName + group + ".json";
		svr += "?print=pretty";
		String tmpPane = "";
		URL url;
		try {
			url = new URL(svr);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;
			http.setRequestMethod("GET");
			http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
			InputStream ins = http.getInputStream();
			InputStreamReader isr = new InputStreamReader(ins);
			BufferedReader in = new BufferedReader(isr);
			String inputLine;

			System.out.println("--- Read Data " + group);

			while ((inputLine = in.readLine()) != null) {
				tmpPane += inputLine + "\n";
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tmpPane;
	}

	// =========================================================================
	public static int putCloud(String group, String sid, String data, String mode) {
		String svr = svrName + group + ".json";
		if(mode == null) mode = "PATCH";

		System.out.println("--- svr: " + svr + " data:" + data);

		URL url;
		try {
			url = new URL(svr);
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection) con;
			http.setRequestMethod("POST");
			http.setDoOutput(true);
			http.setRequestProperty("X-HTTP-Method-Override", mode);
			http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Accept", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream());
			out.write(data);
			out.close();
			InputStream ins = http.getInputStream();
			InputStreamReader isr = new InputStreamReader(ins);
			BufferedReader in = new BufferedReader(isr);
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// =========================================================================
	static String getPane(String user, String paneId, String key) {
		String data = "";
		String request = "/pane/" + paneId + "/pane"; 
		if(key != null) request = "/data/" + paneId + ":" + key + "/pane";
		data = getCloud(request, null);
		
		return data;
	}

	// =========================================================================
	static void putPane(String user, String paneId, String key, String data) {
		String param = "?print=silent";
		param = "";
		putCloud("/story/pane/" + paneId + param, null, data, "POST");
		putCloud("/pane/" + paneId + param, null, data, "PATCH");
		putCloud("/story/data/" + user + "/" + paneId + ":" + key + param, null, data, "POST");
		putCloud("/data/" + user + "/" + paneId + ":" + key + param, null, data, "PATCH");		
	}

	static String term = "99";
	static String para = "CUST::x001:JAM50::x002:Formoto::x003:GLOBE"
				+ "::x004:MANAGER::x005:93.148.232.79_1522_XE::x006:2";
	// =========================================================================
	public static void main(String[] args) {
		String data =
		"\n{" + 
		"\"pane\": {\n" + 
			"\"id\": \"anam002\",\n" + 
			"\"date\": \"May 13, 2000\"\n" + 
		"}}";

		evalNew(data, "10", 2, null);
		evalNew(data, "33.3", 2, null);
		evalNew(data, "55", 2, null);
		if(true) return;

		String sql = "";
		sql = "CREATE TABLE T1 (col1 INT);\n" + 
				"DESC T1\n" + 
				"INSERT INTO T1 VALUES(1);\n" + 
				"SELECT * FROM T1;\n" + 
				"BEGIN\n" + 
				"INSERT INTO T1 VALUES(2);\n" + 
				"END;\n" + 
				"/\n" + 
				"SELECT * FROM T1;";
//		sql = "select * from anam where ROWNUM <= 1;";
		String tmp = getRestOra("globe", "MANAGER", null, sql);
		System.out.println("--- Read Data:\n" + tmp);
		if(true) return;

		JPane frame = new JPane();
		System.out.println("--- Server: " + JPane.VERSION);
		JPaneDoc requ = frame.new JPaneDoc("req", "");
		JPaneMaster master = frame.new JPaneMaster(term);
		JPane.FatturePara(term, "$$" + para, frame, requ, master);

		String paneId = "TD01_FAT";
		String cmd = "$$TD01_FAT;";
		master = frame.execBase(requ, cmd, 0, requ, master);
		JPaneDoc jpTmp = master.get(paneId, "");

//		master.listSort(0);

		data = jpTmp.jO.toString();

		putPane("test", "TD01_FAT", "null", data);

		String tmpPane = getPane(null, paneId, null);
		System.out.println(tmpPane);
		
		JPaneLoad(null);

//		getCloud("/master", null);
	}
	
	static String ret = "[{\"id\":1,\"content\":\"pane\"},{\"id\":2,\"children\":[{\"id\":3},{\"id\":4},{\"foo\":\"Bar\",\"value\":\"Item 5 value\",\"id\":5,\"children\":[{\"id\":6},{\"id\":7},{\"id\":8}]}]}"
			+ ",{\"id\":9}"
//			+ ",{\"id\":10,\"children\":[{\"id\":11,\"children\":[{\"id\":12}]}]}"
			+ "]";
	
	// =========================================================================
	static String JPaneLoadPane(String paneId, String req, JPane frame, JPaneMaster master, JPaneDoc requ) {
		String data = "";
		String cmd = "$$" + paneId + ";";
		master = frame.execBase(requ, cmd, 0, requ, master);
		JPaneDoc jpTmp = master.get(paneId, "");

//		putPane("test", "TD01_FAT", "null", data);
		
		data += "{\"id\":\"" + paneId + "\""
				+ "," + "\"tt\":\"pane\",";

		if(req.contains("/find"))
		data += "\"classes\": [\"dd-collapsed\"],";

		data +=  "\"children\":[";

		String sortMode	= "sort-name";
		JPaneLoop jl = frame.new JPaneLoop(jpTmp, null, sortMode);
		jl.reset(1);

		int iCmp = 0;
		int nCmp = 0;
		while((iCmp = jl.loop()) > 0) {
			
			String id = jl.get("id"			,"x999");
			String is = jl.get("is"			,"Item_" + iCmp + " " + id);
//			String ix = jl.get("ix"			,"Item_" + iCmp + " " + id;
			String ix = jl.get("ix"			,is);
			String val= jl.get("val"		,"");
			String xml= jl.get("xmlPath"	,"");
			String view = JPane.vDec(jl.get("view"	,""));

			System.out.println("--- id:" +id + " ix:" + ix + " view:" + view);

			if(id.equalsIgnoreCase("x000")) continue;

			if(nCmp > 0) data += ",";
			nCmp++;

			if(view.length()> 0) {
				data += "";
				data += JPaneLoadPane(view.substring(2), "", frame, master, requ);				
				data += "";
				continue;
			}
			
			data += "{";
			data += "\"id\":\"" + id + "\",";
//			data += "\"id\":" + iCmp + "";
			data += "\"ix\":\"" + ix + "\",";

			data += "\"tt\":\"item\","
				+ "\"classes\": [\"dd-collapsed\"],"	
				+ "\"children\":[";
			
			data += "{\"tt\":\"att\",\"ix\":\"" + "id:" + ix +"\"}";
//			if(view.length()>0)
//			data += ",{\"tt\":\"att\",\"ix\":\"" + "view:" + view +"\"}";
			if(is.length()>0)
			data += ",{\"tt\":\"att\",\"ix\":\"" + "sql:" + is +"\"}";
			if(val.length()>0 && !val.equalsIgnoreCase("null"))
			data += ",{\"tt\":\"att\",\"ix\":\"" + "val:" + val +"\"}";
			if(xml.length()>0 && !val.equalsIgnoreCase("null"))
			data += ",{\"tt\":\"att\",\"ix\":\"" + "xml:" + xml +"\"}";

			data += "]}";
		}
		data += "]}";
		System.out.println("--- Data " + data);

		return data;
	}

	// =========================================================================
	static String JPaneLoad(String req) {
		System.out.println("--- JPaneLoad ...");
		String data = "";
		String paneId = "TD01_FAT";
		if(req.contains("load:formula")) {
			paneId = "FORMULA";
			data += "[";

			data += "{\"id\":\"" + paneId + "\""
					+ "," + "\"tt\":\"pane\"}";

//			{
//				data += "\"classes\": [\"dd-collapsed\"],";
//				data +=  "\"children\":[";
//			}
			data += "]";

			return data;
		}

		JPane frame = new JPane();
		System.out.println("--- Server: " + JPane.VERSION);
		JPaneDoc requ = frame.new JPaneDoc("req", "");
		JPaneMaster master = frame.new JPaneMaster(term);
		JPane.FatturePara(term, "$$" + para, frame, requ, master);
//		master.listSort(0);

//		String paneId = "TD01_PDT";
		data += "[";
		data += JPaneLoadPane(paneId, req, frame, master, requ);

		if(req.contains("/find")) {
			paneId = "TD01_LINEE";
			data += ", " + JPaneLoadPane(paneId, req, frame, master, requ);			

			paneId = "TD01_IVA";
			data += ", " + JPaneLoadPane(paneId, req, frame, master, requ);			
		}

		data += "]";

		System.out.println("--- Data " + data);
		return data;
	}

	// =========================================================================
	//
	// =========================================================================
	final static NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
	static ScriptEngine engine = factory.getScriptEngine(new String[] { "-scripting" });

	static String evalNew(String data, String valore, int deci, JPaneDoc jp) {
		long startTime	= System.currentTimeMillis();

		String multi = "100";
		if(deci == 0) multi = "1";
		else if(deci == 1) multi = "10";
		else if(deci == 2) multi = "100";
		else if(deci == 3) multi = "1000";
		else if(deci == 4) multi = "10000";
		else if(deci == 5) multi = "100000";
		String ritorno = "";

		if(deci >= 0) valore = "(Math.round((" + valore + ")*"+multi+")/"+multi+")";

		String tmp = "";
		try {
			String valore2 = valore.replace("\"", "\\\"");
			tmp = "function esegue(jpTxt) {\n"
				+ "var jp = JSON.parse(jpTxt);\n"
				+ "var pane = jp.pane;\n"
				+ "print(\"--- EvalNew Esegue:" + valore2 + "\");"
//				+ "\nprint(jpTxt);"
				+ "\nprint(\"+++ obj: \" + JSON.stringify(jp) + \" + \""
//				+ " + JSON.stringify(pane.id)"
				+ " + \" = ${pane.id}\")"
				+ ";\n"
				+ "\nprint(\"++++++++ ${pane.id + ' ' + pane.date}\");\n"
				+ "return " + valore + " + ' ' + JSON.stringify(jp) ;"
				+ "};";
			engine.eval(tmp);
			Invocable inv = (Invocable) engine;
			ritorno = (String) inv.invokeFunction("esegue", data);
		} catch (ScriptException | NoSuchMethodException e) {
			JPane5.log("w6", tmp);
			JPane5.log("w6", "Script error");
			e.printStackTrace();
		}
		
		if(ritorno.equalsIgnoreCase("nan")) ritorno = "";

		String yourCommand = "ls -l";
		if(JPane.isWindows()) {
			yourCommand = "cmd /c cd";
		}
		Object  eval = null;
		try {eval = engine.eval("$EXEC(\"" + yourCommand + "\")");
		} catch (ScriptException e) {e.printStackTrace();}

		System.out.println(eval);

		long endTime = System.currentTimeMillis();
		JPane5.log("w6"
				, "evalNew End [" + (endTime - startTime) + " ms]"
				+ "\n--- " + ritorno + "\n");

		ritorno = (String) eval;
		return ritorno;
	}
}
