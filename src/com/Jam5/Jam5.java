package com.Jam5;

import java.io.*;
import java.util.Enumeration;

import javax.servlet.*;
import javax.servlet.http.*;

import com.JPane.JPane5;

// =============================================================================
//
// =============================================================================
public class Jam5 extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String mymsg;

	public void init() throws ServletException {
		mymsg = "Jam5 Servlet";
	}

	// =========================================================================
	//
	// =========================================================================
	public void doPost(HttpServletRequest req
			,HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
	public void doGet(HttpServletRequest req
			,HttpServletResponse res) throws ServletException, IOException {

		JPane5 jp = new JPane5();
		mymsg  = jp.JPaneReq("ok");
		
		System.out.println("--- " + req.getRequestURI());
		String uri		= req.getRequestURI();
		String svr		= req.getServerName();
		String rest		= req.getPathInfo();

		System.out.println("--- svr:" + svr + " rest:" + rest);

		Enumeration<String> parameterNames = req.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			System.out.print("--- " + paramName);

			String[] paramValues = req.getParameterValues(paramName);
			for (int i = 0; i < paramValues.length; i++) {
				String paramValue = paramValues[i];
				System.out.print("\t= " + paramValue);
				System.out.print("\n");
			}
		}
		System.out.print("\n");

		if(req.getRequestURI().contains("/pane")
		 ||req.getRequestURI().contains("/load")
		 ||req.getRequestURI().contains("/find")
		 ||req.getRequestURI().contains("/save")) {

			res.setContentType("application/json");
			PrintWriter out = res.getWriter();
			
			if(req.getRequestURI().contains("/save"))
				mymsg  = jp.JPaneSave(req.getParameter("data"));
			else
				mymsg  = jp.JPaneReq(req.getRequestURI());
			System.out.println("--- get: " + req.getRequestURI());
//			System.out.println("--- get: " + mymsg);
			out.println(mymsg);

			return;
		}

		if(req.getRequestURI().contains("/get")) {
			res.setContentType("text/html");
			PrintWriter out = res.getWriter();

			String temp ="{\n" + 
					"    \"matches\": [\n" + 
					"        {\n" + 
					"            \"text\": \"Piero\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"3G\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"3rd Eye\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"3rensho\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"3T\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"45North\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"4ZA\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"6KU\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"9 zero 7\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        },\n" + 
					"        {\n" + 
					"            \"text\": \"A2B e-bikes\",\n" + 
					"            \"category\": \"default\",\n" + 
					"            \"\": null\n" + 
					"        }\n" + 
					"    ]\n" + 
					"}"
					;			

			temp = ("{\n" + 
					"\"matches\": [\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 1,\n" + 
					"      \"text\": \"Option 1\"\n" + 
					"    },\n" + 
					"    {\n" + 
					"      \"id\": 2,\n" + 
					"      \"text\": \"Option 2\"\n" + 
					"    }\n" + 
					"  ],\n" + 
					"  \"pagination\": {\n" + 
					"    \"more\": true\n" + 
					"  }\n" + 
					"}");

			out.println(temp);

			return;
		}
		
		res.setContentType("text/html");

		// Writing message to the web page
		PrintWriter out = res.getWriter();
		out.println("<h1>" + mymsg + "</h1>");
	}

	// =========================================================================
	//
	// =========================================================================
	public void destroy() {
	}
}
