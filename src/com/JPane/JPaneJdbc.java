package com.JPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

// =============================================================================
//
// =============================================================================
public class JPaneJdbc {
	Connection				conn		= null;
	Statement				stmt		= null;
	CallableStatement		cs 			= null;
	ResultSet				rset		= null;
	String					driver		= null;
	String					connect		= null;
	boolean					isSelect	= false;
	boolean					isScript	= false;
	String					sql			= null;

	// =========================================================================
/*	public class rset {
		ResultSet			rs			= null;
		rset(ResultSet rset) {
			rs = rset;
		}
	} */

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
	void log(String mode, String msg, String attach) {
//		jPaneCloud.log(mode, msg, attach);
	}

	// =========================================================================
	static void log(String mode, String msg) {
//		jPaneCloud.log(mode, msg, null);
	}

	// =========================================================================
	int createSQlite() {
		String docRoot = "";
		String dbName = docRoot + "/sample.db";
		File dbFile = new File(dbName);
		if(dbFile.exists()) return 1;
//		conn = new SQLite.JDBCDataSource("jdbc:sqlite:" + docRoot +
//	                                       "/sample.db").getConnection();

		return 0;
	}

	// =========================================================================
	//
	// =========================================================================
	String connect(String connectTxt) {
		String errore = "";
		long startTime = System.currentTimeMillis();
		
		{
			if(conn != null) try {conn.close();} catch (NullPointerException e) {e.printStackTrace();} catch (SQLException e) {e.printStackTrace();}
			connect = "";
		}
		
		if(connect != null && connect.equalsIgnoreCase(connectTxt) && conn != null) {
			long endTime = System.currentTimeMillis();
//			jPaneCloud.log("w6-1", "jDbc connect End [" + (endTime - startTime) + " ms]", null);
//			jPaneDoc.setHelpStatic("--- jDbc CONNECT:" + connect, null);
			try {
				conn.setAutoCommit(true);
				stmt = conn.createStatement();
				return errore;
			} catch (SQLException e) {
//				jPaneCloud.log("w6-1", "jDbc RECONNECT [" + (endTime - startTime) + " ms]", null);
//				jPaneDoc.setHelpStatic("--- jDbc RECONNECT [" + (endTime - startTime) + " ms]:" + e, null);
				e.printStackTrace();
				connect = "";
//				return "KO";
			} catch (NullPointerException e) {
				connect = "";
			}
		}

		connect = connectTxt;

		try {
			String con = "";
			String usr = "";
			String pwd = "";

			con = connect.substring(connect.indexOf("--jdbc")+2);
			log("w6-1", "con: " + con);
			usr = connect.substring(8, connect.indexOf("/"));
			log("w6-1", "usr: " + usr);
			pwd= connect.substring(connect.indexOf("/")+1, connect.indexOf("@"));
			log("w6-1", "pwd: " + pwd);
/*
			usr 		= "SYSTEM";
			pwd			= "MANAGER";
			con 		= "jdbc:oracle:thin:hr/hr@192.168.178.26:1521:XE"; // piero
*/
			if(con.startsWith("jdbc:ora"))
				DriverManager.registerDriver (new oracle.jdbc.OracleDriver());
			conn = DriverManager.getConnection(con, usr, pwd);
//			jPaneDoc.oracleHost = con.substring(con.lastIndexOf("@")+1);
//			jPaneDoc.oracleHost = jPaneDoc.oracleHost.substring(0, jPaneDoc.oracleHost.indexOf(":"));

			long endTime = System.currentTimeMillis();
//			jPaneDoc.setHelpStatic("--- jDbc CONNECT END[" + (endTime - startTime) + " ms]:", null);

			for(SQLWarning warn = conn.getWarnings(); warn != null;
												warn = warn.getNextWarning()) {
				log("w6-1", "SQL Warning:" ) ;
				log("w6-1", "State  : " + warn.getSQLState()  ) ;
				log("w6-1", "Message: " + warn.getMessage()   ) ;
				log("w6-1", "Error  : " + warn.getErrorCode() ) ;
			}
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			while( e != null ) {
				this.log("w6-1", "SQLException: " + e.getMessage(), e.toString());
				this.log("w6-1", "SQLState:     " + e.getSQLState(), null);
				this.log("w6-1", "VendorError:  " + e.getErrorCode(), null);
				e = e.getNextException();
				errore = "KO";
				connect = "";
			}
		}

		long endTime = System.currentTimeMillis();
//		jPaneDoc.timeConnect = (endTime - startTime);
//		jPaneCloud.log("w6-1", "jDbc connect End [" + (endTime - startTime) + " ms]", null);
		return errore;
	}

	// =========================================================================
	static String createConnect(String user, String pwd, String alias
												,String con, String jdbc) {
		String connect
				= "CONNECT " + user + "/" + pwd + "@" + alias + "\n\r"
				+ "REM CON " + user + "/" + pwd + "@" + alias +";"
				+ "--" + jdbc + "\n";

		return connect;
	}

	// =========================================================================
	public JPaneJdbc() {
		this(null, false);
	}

	// =========================================================================
	public JPaneJdbc(String text, Boolean immediate) {
		this(text, null, null, null, null, null, immediate);
	}

	// =========================================================================
	//
	// =========================================================================
	public JPaneJdbc(String text, String user, String pwd, String alias
									,String con, String jdbc, Boolean immediate) {
		if(user != null) text = createConnect(user, pwd, alias, con, jdbc) + text;
//		log("w6-1", "jDbc begin:" + text);
		isSelect = false;
		try {
//			conn.setAutoCommit(false);
/*			InputStream scriptStream =
				Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("META-INF/initialize.sql");
			BufferedReader scriptReader = new BufferedReader
									(new InputStreamReader(scriptStream)); */ 
			int flagAutoCommit	= 0;
			String nextLine;
			StringBuffer nextStatement = new StringBuffer();
			BufferedReader lines = new BufferedReader(new StringReader(text));


			stmt = null;
			try {
				while((nextLine = lines.readLine()) != null) {
					if(nextLine.startsWith("REM CON ")) {
						log("w6-1", "jDbc connect:" + nextLine);
						connect(nextLine);
//						conn.setNetworkTimeout(null, 30000);
						conn.setAutoCommit(true);
						flagAutoCommit = 1;
						stmt = conn.createStatement();
//						stmt.setQueryTimeout(1);

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
//						nextStatement.append(";");
						break;
					}

					nextStatement.append(" " + nextLine + "\n");

					if(!isScript && nextLine.endsWith(";")) {
						sql = nextStatement.toString().replace(";", "");
						log("w7-1", "jDbc Exec: ");

						try {
							rset = stmt.executeQuery(sql);
						} catch(java.sql.SQLSyntaxErrorException er) {
							log("w7-1", "jDbc Exec: ERRORE SINTASSI ORACLE" + er);	
//							jPaneDoc.setHelpStatic("\n--- jDbc Exec: ERRORE SINTASSI ORACLE:\n--- " + er, sql);
//							jPaneDoc.setHelpStatic("SQLState:     " + er.getSQLState(), null);
//							jPaneDoc.setHelpStatic("VendorError:  " + er.getErrorCode(), null);
						}

						// jPaneJdbc.rset rs = new rset(rset);
						if(sql.toUpperCase().contains("SELECT ")
						 &&!sql.toUpperCase().contains("BEGIN")) {
//							log("w6-1", "jDbc Exec SELECT: " + sql);
//							while(rset.next()) {log("w6-1", "LineRead:");}
							isSelect = true;
							return;
						}
						nextStatement = new StringBuffer();
					}
				}
				if(isScript) {
					sql = nextStatement.toString();
					log("w6-1", "jDbc Exec [" + immediate + "] Script: " + sql);
					if(immediate) stmt.execute(sql);
					else cs = conn.prepareCall(sql);
					return;
				}
			} catch (SQLException e) {
				log("w6-1", "jDbc Exec Script: " + sql);
				e.printStackTrace();
				
				this.log("w6-1", "SQLException: " + e.getMessage(), e.toString());
//				jPaneDoc.setHelpStatic("--- SQLException: " + e.getMessage(), e.toString());
				this.log("w6-1", "SQLState:     " + e.getSQLState(), null);
//				jPaneDoc.setHelpStatic("--- SQLState:     " + e.getSQLState(), null);
				this.log("w6-1", "VendorError:  " + e.getErrorCode(), null);
//				jPaneDoc.setHelpStatic("--- VendorError:  " + e.getErrorCode(), null);
//				jPaneDoc.setHelpStatic("---\n" + e.toString(), "execSql:" + immediate);

			} catch (IOException e) {
//				log("w6-1", "jDbc Exec Script: " + sql);
//				e.printStackTrace();
			} catch (NullPointerException e) {
				log("w6-1", "jDbc Exec Script: " + sql);
//				jPaneDoc.setHelpStatic("\n--- jDbc Exec: ERRORE ORACLE NullPointer:\n--- " + e, sql);
//				jPaneDoc.setHelpStatic("--- SQLState:     " + e.getClass(), null);
//				jPaneDoc.setHelpStatic("--- VendorError:  " + e.getLocalizedMessage(), null);
				e.printStackTrace();				
			}
			stmt.close();
			if(flagAutoCommit == 0) conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();				
		}
	}

	// =========================================================================
	String getSelect(String type) {
		String ritorno = "";
		int j = 0;

		ResultSetMetaData metaData = null;
		int columnCount = 0;
		try {
			metaData = rset.getMetaData();
			columnCount = metaData.getColumnCount();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}

		if(type.startsWith("xml")) {
			String sqlTmp = sql; //= StringEscapeUtils.escapeXml(sql);
			ritorno = "<data>\n<sql>" + sqlTmp + "</sql>\n<lines>\n";
		}

		try {
			while(rset.next()) {
				j++; if(j > 100) break;
				if(type.startsWith("xml")) {ritorno += "<line_" + j + ">";}
//				System.out.println(rset.getString(1));
				if(type.startsWith("xml")) {
					for(int n=1; n<=columnCount; n++) {
						String val = rset.getString(n);
						if(val != null && val.length() > 0) {
							ritorno += "<"
								+ metaData.getColumnName(n).toLowerCase() + ">";
							ritorno += xmlEscText(rset.getString(n));
//							ritorno += rset.getString(n);
							ritorno += "</"
								+ metaData.getColumnName(n).toLowerCase() + ">";
						}
					}
				}

				if(type.startsWith("xml")) {ritorno += "</line_" + j + ">\n";}
				this.log("w5", "Read Sql line: " + j);
			}
			if(type.startsWith("xml")) {ritorno += "</lines>\n</data>\n";}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ritorno;
	}

	// =========================================================================
	public static void main(String[] args) {
/*		String text = createConnect("GLOBE", "MANAGER", "GLOBE"
						,"jdbc:oracle:thin:hr/hr", "88.62.97.66:1521:ORCL");
		text += "select * from cat;";
		log("w5", "Text:\n" + text); */

		String sqlText = 
		"REM CON GLOBE/MANAGER@;--jdbc:oracle:thin:hr/hr@serverbox.boxint.com:1522:XE\n"
		+ "SELECT C_CNTMOVT.COD_DITTA x001,C_CNTMOVT.TI_OPE x002,C_CNTMOVT.CO_CAU x003,C_CNTMOVT.NUM_MOV x004 \n"
//		+ ",C_CNTMOVT.NUM_MOV_ORI x005,C_CNTMOVT.COD_AGE x006,AGANA.RAG_S1 x007,AGANA.PREVLEV1 x008,AGANA.SALUTI x009,AGANA.SALUTI_TESTATA x010,C_CNTMOVT.DA_MOV x011,C_CNTMOVT.NUM_ACQ x012,C_CNTMOVT.ORA x013,C_CNTMOVT.RIF_OR x014,C_CNTMOVT.DIVISA x015,TBEDIV.DES_DIVISA x016,C_CNTMOVT.CO_CON x019,C_CNTMOVT.T_CONTO x020,C_CNTMOVT.RAG_SOCIALE x022,C_CNTMOVT.INDIRIZZO x023,C_CNTMOVT.LOCALITA x024,C_CNTMOVT.PROV x025,C_CNTMOVT.CAP x026,C_CNTMOVT.TELEFONO x027,C_CNTMOVT.FAX x028,C_CNTMOVT.MAIL x029,C_CNTMOVT.CONTATTO x030,C_CNTMOVT.CO_PAG x031,TABPAG.DES_PAG x032,C_CNTMOVT.CATEG x033,TABCTC.DES_CAT_CLI x034,C_CNTMOVT.NOTE_SALUTI_TESTATA x040,C_CNTMOVT.NOTE_SALUTI x041,C_CNTMOVT.NOTE_RIFERIMENTI x042,C_CNTMOVT.NOTE_TESTA x043,C_CNTMOVT.NOTE_TECNICHE x044,C_CNTMOVT.NOTE_PAG x045,C_CNTMOVT.NOTE_VALID x046,C_CNTMOVT.NOTE_FINALE x047,C_CNTMOVT.NUM_RDO x051,C_CNTMOVT.NUM_ORDINE x052,C_CNTMOVT.COD_AGE x053,C_CNTMOVT.CO_PAG x054,C_CNTMOVT.CATEG x055,C_CNTMOVT.DIVISA x056 \n"
		+ "FROM C_CNTMOVT \n" // ,AGANA,TBEDIV,TABPAG,TABCTC \n"
		+ "WHERE C_CNTMOVT.COD_DITTA  =  '01' AND  C_CNTMOVT.TI_OPE  =  '?'"
		+ " AND  C_CNTMOVT.CO_CAU  =  'PVC' AND  C_CNTMOVT.NUM_MOV  =  '44';"
		;

		JPaneJdbc sql = new JPaneJdbc(sqlText
			,null, "MANAGER", "GLOBE"
			,"oracle.jdbc.driver.OracleDriver"
			,"jdbc:oracle:thin:hr/hr@88.62.97.66:1521:ORCL", false);
		if(sql.isSelect) {log("w5-a", sql.getSelect("xml"));}
//		if(sql.isSelect) {log("w5-a", sql.getSelect("xml"));}
	}
}
