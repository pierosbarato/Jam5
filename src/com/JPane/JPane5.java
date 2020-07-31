package com.JPane;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

import org.json.JSONObject;

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
	static void JPaneLoadPane(String paneId, String req, JPane frame
			,JPaneMaster master, String sysId, JPaneDoc requ, tmpJSON tmpJson) {
		String cmd		= "$$" + paneId + ";";

		master = frame.execBase(requ, cmd, 0, requ, master);
		JPaneDoc jpTmp = master.get(paneId, "");
		if(paneId.contains("_")) paneId = paneId.substring(paneId.indexOf('_')+1);
		paneId = paneId.toLowerCase();
		sysId += "." + paneId;
//		putPane("test", "TD01_FAT", "null", data);
		
		String paneMode		= jpTmp.getItem("id", "state", "mode", "Master", 1);
		String paneXmlPath	= jpTmp.getItem("id", "state", "xmlPath", "", 1);
		String paneEdit		= jpTmp.getItem("id", "state", "edit", "", 1);
		String paneBoot		= jpTmp.getItem("id", "state", "boot", "", 1);
		String paneBootLine	= jpTmp.getItem("id", "state", "bootLine", "", 1);
		int rigMax			= jpTmp.getItem("id", "state", "linemax" , 1, 1);

//		String tmp1 = jpTmp.jO.toString();
//		String tmp2 = jpTmp.getItem("id", "header", "title", "", 1);
//		System.out.println("--- ??????????? " + tmp1);
//		System.out.println("--- ??????????? " + paneXmlPath);
//		System.out.println("--- ??????????? " + paneHtml);

		tmpJson.addList("{\"id\":\"" + sysId + "\"," + "\"tit\":\"" + paneId + "\""
				+ "," + "\"tt\":\"pane\","
				+ "");

		if(req.contains("/find")) {
			tmpJson.addList("\"classes\":[\"dd-collapsed\"],");
		}

		tmpJson.addList("\"iconClass\":\"fa-address-card\","
									+ "\"children\":[");

		if(paneXmlPath.length() > 0) {
			tmpJson.addListAtt("xml", paneXmlPath);
		}
		if(paneEdit.length() > 0) {
			tmpJson.addListAtt("edit", paneEdit);
		}
		if(paneBoot.length() > 0) {
			tmpJson.addListAtt("boot", paneBoot);
		}
		if(paneBootLine.length() > 0) {
			tmpJson.addListAtt("bootLine", paneBootLine);
		}
		String sortMode	= "sort-name";
		JPaneLoop jl = frame.new JPaneLoop(jpTmp, null, sortMode);

		String beginArray = "";
//		if(paneId.equalsIgnoreCase("linee")
//		 ||paneId.equalsIgnoreCase("iva"))

		if(paneMode.contentEquals("Detail"))
			beginArray = "[";
		else
			if(tmpJson.JSON.length() > 3) tmpJson.add(",");

		tmpJson.add("\"" + paneId + "\":" + beginArray + "{");
		tmpJson.add("\"_pane\":" + "{\"id\":\"" + paneId + "\"}");

		if(!paneMode.contentEquals("Detail")) rigMax = 1;

		for(int i=1; i<=rigMax; i++) {
			if(i> 1) tmpJson.add("{");
			
		jl.reset(i);
		int iCmp = 0;
		int nCmp = 0;
		while((iCmp = jl.loop()) > 0) {
			
			String id	= jl.get("id"		,"x999");
			String is	= jl.get("is"		,"");
//			String ix	= jl.get("ix"		,"Item_" + iCmp + " " + id;
			String ix	= jl.get("ix"		,is);
			String tit	= jl.get("tit"		,ix);
			String type	= jl.get("type"		,"text");
			String val	= JPane.vDec(jl.get("val"	,""));
			String xml	= jl.get("xmlPath"	,"");
			String edit	= jl.get("edit"		,"");
			String view	= JPane.vDec(jl.get("view"	,""));
			String exec	= JPane.vDec(jl.get("exec"	,""));

			if(exec.length() > 0) view = exec;

			System.out.println("--- id:" +id + " ix:" + ix + " view:" + view);

			if(id.equalsIgnoreCase("x000")) continue;

			nCmp++;

			if(view.length()> 0) {
				JPaneLoadPane(view.substring(2), "", frame, master, sysId, requ, tmpJson);
				continue;
			}

			if(ix.length() > 0
			&&!ix.contains("IMP_RATA2".toLowerCase())
			&&!ix.contains("DATA_SCAD2".toLowerCase())
			&&!ix.contains("ImportoPagamento")
			&&!ix.contains("DataScadenzaPagamento")
			) {
				String tmp = paneId + "_" + ix;
//				if(nCmp > 0) tmpJson.add(",");
				tmpJson.add("\"" + ix.replace(".", "_") + "\":\"" + val + "\"");
			}

			tmpJson.addList("{");
			tmpJson.addList("\"id\":\"" + sysId + "." + ix + "\",");
			tmpJson.addList("\"ix\":\"" + ix + "\",");
			tmpJson.addList("\"val\":\"" + val + "\",");

			tmpJson.addList("\"tt\":\"item\"");
			
			tmpJson.addList("\"classes\": [\"dd-collapsed\"],");
			tmpJson.addList("\"iconClass\":\"fa-equals\",");
			tmpJson.addList("\"children\":[");

//			data += "{\"tt\":\"att\",\"ix\":\"" + "id:" + ix +"\"}";
//			if(view.length()>0)
//			data += ",{\"tt\":\"att\",\"ix\":\"" + "view:" + view +"\"}";
			tmpJson.addList("{\"tt\":\"att:type\","
					+ "\"iconClass\":\"fa-list-alt\","
					+ "\"id\":\"" + sysId + "." + ix + "\","
					+ "\"type\":\"" + type + "\","
					+ "\"tit\":\"" + tit +"\"}");

			if(is.length()>0)
				tmpJson.addList(",{\"tt\":\"att:sql\","
						+ "\"iconClass\":\"fa-list-alt\","
						+ "\"sql\":\"" + is +"\"}");
			if(xml.length()>0 && !xml.equalsIgnoreCase("null"))
				tmpJson.addList(",{\"tt\":\"att:xml\","
						+ "\"iconClass\":\"fa-list-alt\","
						+ "\"xml\":\"" + xml +"\"}");
			if(edit.length()>0 && !edit.equalsIgnoreCase("null"))
				tmpJson.addList(",{\"tt\":\"att:edit\","
						+ "\"iconClass\":\"fa-list-alt\","
						+ "\"edit\":\"" + edit +"\"}");

			tmpJson.addList("]}");
		}

		tmpJson.add("}");
	
		}

//		if(paneId.equalsIgnoreCase("linee")
//		 ||paneId.equalsIgnoreCase("iva"))

		if(paneMode.contentEquals("Detail"))
			tmpJson.add("]");

		tmpJson.addList("]}");
	}

	// =========================================================================
	public class tmpJSON {
		String JSON = "";
		String List	= "";
		String Sql	= "";
		tmpJSON(String tmp) {JSON += tmp;}
		void add(String tmp) {JSON = addJSON(tmp,JSON);}
		void addList(String tmp) {List = addJSON(tmp,List);}
		void addListAtt(String att, String val) {
			if(val.length() > 0)
				addList("{\"tt\":\"att:" + att + "\","
					+ "\"iconClass\":\"fa-list-alt\","
					+ "\"" + att + "\":\"" + val +"\"},");
		}
		void addSql(String tmp) {Sql = addSQL(tmp,Sql);}
		String addJSON (String tmp, String JSON) {
			if(tmp.startsWith(",") && JSON.endsWith("{"))
				JSON += tmp.substring(1);
			else if(tmp.startsWith(",") && JSON.endsWith(","))
				JSON += tmp.substring(1);
			else if(tmp.startsWith(":") && JSON.endsWith(","))
				JSON += tmp.substring(1);
			else if(tmp.startsWith("{") && JSON.endsWith("}"))
				JSON += "," + tmp;
			else if(tmp.startsWith("\"") && JSON.endsWith("}"))
				JSON += "," + tmp;
			else if(tmp.startsWith("\"") && JSON.endsWith("]"))
				JSON += "," + tmp;
			else if(tmp.startsWith("\"") && JSON.endsWith("\""))
				JSON += "," + tmp;
			else JSON += tmp;
			return JSON;
		}
		String addSQL (String tmp, String SQL) {
			return SQL;
		}
		String get() {return JSON;}
		String getList() {return List;}
		String getSql() {return Sql;}
	}

	// =========================================================================
	static String JPaneLoad(String req) {
		System.out.println("--- JPaneLoad ...");
		String paneId = "TD01_FAT";
		if(req.contains("load:formula")) {
			JPane5 jp5 = new JPane5();
			tmpJSON tmpJson = jp5.new  tmpJSON("");
			paneId = "FORMULA";
			tmpJson.addList("[");

			tmpJson.addList("{\"id\":\"" + paneId + "\""
					+ "," + "\"tt\":\"pane\"}");

//			{
//				data += "\"classes\": [\"dd-collapsed\"],";
//				data +=  "\"children\":[";
//			}
			tmpJson.addList("]");

			return tmpJson.getList();
		}

		String sysId = "main";
		JPane frame = new JPane();
		System.out.println("--- Server: " + JPane.VERSION);
		JPaneDoc requ = frame.new JPaneDoc("req", "");
		JPaneMaster master = frame.new JPaneMaster(term);
		JPane.FatturePara(term, "$$" + para, frame, requ, master);
//		String jsonEncodedString = JSONObject.quote(data);

		String cmd = "";
		cmd = "$def:boot_Css::state-mode:master::-tit:Css_File"
//				+ "::-edit:DatHtml"
				+ "::-boot:boot"
				+ "::-bootLine:bootLine"
				+ "::x002:null::-ix:CssName"
				+ "::x008:null::-ix:CssPath::-val:"+JSONObject.quote(""
					+  "https://use.fontawesome.com/releases/v5.8.2/css/all.css"
					+ "|https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/css/bootstrap.min.css"
					+ "|https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.0/css/mdb.min.css"
					+ "|bootstrap-iconpicker/css/bootstrap-iconpicker.min.css"
					+ "|http://cdn.jsdelivr.net/npm/select2@4.0.13/dist/css/select2.min.css"
					+ "|../summernote/summernote-bs4.css"
					+ "|http://cdnjs.cloudflare.com/ajax/libs/codemirror/3.20.0/codemirror.css"
					+ "|http://cdnjs.cloudflare.com/ajax/libs/codemirror/3.20.0/theme/monokai.css")
				;
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$def:boot_Js::state-mode:master::-tit:Js_File"
//				+ "::-edit:Dat2Html"
				+ "::-boot:boot"
				+ "::-bootLine:bootLine"
				+ "::x002:null::-ix:JsName"
				+ "::x008:null::-ix:JsPath::-val:"+JSONObject.quote(""
					+  "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js"
					+ "|https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"
					+ "|https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/js/bootstrap.min.js"
					+ "|https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.19.0/js/mdb.min.js"
					+ "|https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"
					+ "|http://cdnjs.cloudflare.com/ajax/libs/codemirror/3.20.0/codemirror.js"
					+ "|http://cdnjs.cloudflare.com/ajax/libs/codemirror/3.20.0/mode/xml/xml.js"
					+ "|http://cdnjs.cloudflare.com/ajax/libs/codemirror/2.36.0/formatting.js")
				;
		master = frame.execBase(requ, cmd, 0, requ, master);

		cmd = "$def:boot::state-mode:master" // ::tit:JsFile"
				+ "::x002:null::-view:" + JPane.vEnc("$$boot_Css")
				+ "::x005:null::-ix:Body"
				+ "::x008:null::-view:" + JPane.vEnc("$$boot_Js")
				;
		master = frame.execBase(requ, cmd, 0, requ, master);
		
//		master.listSort(0);

//		String paneId = "TD01_PDT";

		JPane5 jp5 = new JPane5();
		tmpJSON tmpJson = jp5.new tmpJSON("{\"sid\":\"" + frame.sid + "\",");

		tmpJson.addList("[");

		JPaneLoadPane(paneId, req, frame, master, sysId, requ, tmpJson);

		tmpJson.add("}");

		JPane.writeTemp("data.json", tmpJson.get(), "", true);

		jp5 = null;

		if(req.contains("/find")) {
			paneId = "TD01_LINEE";
			JPaneLoadPane(paneId, req, frame, master, sysId, requ, tmpJson);

			paneId = "TD01_IVA";
			JPaneLoadPane(paneId, req, frame, master, sysId, requ, tmpJson);
			
			paneId = "boot";
			JPaneLoadPane(paneId, "", frame, master, sysId, requ, tmpJson);
		}

		tmpJson.addList("]");

		System.out.println("--- JSON: " + tmpJson.get());
		System.out.println("--- List: " + tmpJson.getList());

		return tmpJson.getList();		
	}

	// =========================================================================
	//
	// =========================================================================
	static void JPaneReadFattura(int num) {
		
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
