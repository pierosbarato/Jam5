package com.JPane;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.JPane.JPane.JPaneDoc;
import com.JPane.JPane.JPaneMaster;

public class JPaneBase {
	// =========================================================================
	//
	// =========================================================================
	static JPaneMaster paneBASE(JPane frame, JPaneDoc jp, JPaneMaster master
											,String terminal, int flagLog) {
		boolean debug 	= false;
		String	cmd		= "";
		long startTime	= jp.beginHelp("w5", "paneBASE:[" + cmd + "]");

		String isIban = "::-is:TABBANG.IBAN";
		String isIban2 = "::-is:TABBANG.NOME_BANCA";
		if(JPane.utente.equalsIgnoreCase("PETRA71"))
			{isIban = "::-is:ANAC.IBAN_1";isIban2 = "";}

		if(JPane.utente.equalsIgnoreCase("JAM80")) // manzoni
			{isIban = "";isIban2 = "";}

		cmd = "$def:TD01_FAT::state-mode:master::state-xmlPath:none"
			+ "::x002:null::-view:" + JPane.vEnc("$$TD01_HEA")
			+ "::x008:null::-view:" + JPane.vEnc("$$TD01_BOD")
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd	= "$def:TD01_HEA::state-mode:master::state-xmlPath:FatturaElettronicaHeader"
			+ "::x002:null::-view:" + JPane.vEnc("$$TD01_PDT")
			+ "::x003:null::-view:" + JPane.vEnc("$$TD01_PAN") + "::-xmlPath:CedentePrestatore/PAN"
			+ "::x004:null::-view:" + JPane.vEnc("$$TD01_PSE") + "::-xmlPath:CedentePrestatore/PSE"
//			+ "::x005:null::-view:" + JPane.vEnc("$$TD01_PIR") + "::-xmlPath:CedentePrestatore"
			+ "::x006:null::-view:" + JPane.vEnc("$$TD01_CAN") + "::-xmlPath:CessionarioCommittente/CAN"
			+ "::x007:null::-view:" + JPane.vEnc("$$TD01_CSE") + "::-xmlPath:CessionarioCommittente/CSE"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd	= "$def:TD01_PDT::state-mode:master::state-xmlPath:DatiTrasmissione"
			+ "::x001:IT::-ix:IdPaese::-xmlPath:IdTrasmittente/IdPaese"
			+ "::x002:null::-ix:IdCodice::-xmlPath:IdTrasmittente/IdCodice"
				+ "::-is:CONFG.COD_FI_DITTA"
			+ "::x003:00001::-ix:ProgressivoInvio::-xmlPath:ProgressivoInvio"
			+ "::x004:FPR12::-ix:FormatoTrasmissione::-xmlPath:FormatoTrasmissione"
			+ "::x005:0000000::-ix:CodiceDestinatario::-xmlPath:CodiceDestinatario"
				+ "::-is:ANAC.FAMIGLIA"
			+ "::x006:null::-ix:PECDestinatario::-xmlPath:PECDestinatario::-xmlNotNull:val"
				+ "::-is:ANAC.NOTE_N3"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_PAN::state-mode:master::-xmlPath:DatiAnagrafici"
			+ "::x001:IT::-ix:IdPaese::-xmlPath:IdFiscaleIVA/IdPaese"
			+ "::x002:null::-ix:IdCodice::-xmlPath:IdFiscaleIVA/IdCodice"
				+ "::-is:CONFG.PA_IVA_DITTA"
				+ "::-isWrite:CGIVAT.P_IVA"
			+ "::x003:null::-ix:CodiceFiscale::-xmlPath:CodiceFiscale"
				+ "::-is:CONFG.COD_FI_DITTA"
			+ "::x004:null::-ix:Denominazione::-xmlPath:Anagrafica/Denominazione"
				+ "::-is:CONFG.DES_DITTA"
//				+ "::-isWrite:CGIVAT.DESCRIZ_AGG"
			+ "::x005:RF01::-ix:RegimeFiscale::-xmlPath:RegimeFiscale"
//			+ "::x006:null::-view:" + JPane.vEnc("$$TD01_REA")
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_PSE::state-mode:master::-xmlPath:Sede"
			+ "::x001:null::-ix:Indirizzo::-xmlPath:Indirizzo"
			+ 	"::-is:CONFG.IND_DITTA"
			+ "::x002:null::-ix:CAP::-xmlPath:CAP"
			+ 	"::-is:CONFG.CAP_DITTA"
			+ "::x003:null::-ix:Comune::-xmlPath:Comune"
			+ 	"::-is:CONFG.CITTA_DITTA"
			+ "::x004:null::-ix:Provincia::-xmlPath:Provincia::-xmlNotNull:val"
			+ 	"::-is:CONFG.PR_DITTA"
			+ "::x005:IT::-ix:Nazione::-xmlPath:Nazione"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_PIR::state-mode:master::-xmlPath:IscrizioneREA"
//			+ "::x001:VE::-ix:Ufficio::-xmlPath:Ufficio"
//			+ "::x002:64113::-ix:NumeroREA::-xmlPath:NumeroREA"
//			+ "::x003:60000::-ix:CapitaleSociale::-xmlPath:CapitaleSociale"
			+ "::x004:LN::-ix:StatoLiquidazione::-xmlPath:StatoLiquidazione"
			+ "::x005:RF01::-ix:RegimeFiscale::-xmlPath:RegimeFiscale"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_CAN::state-mode:master::-xmlPath:DatiAnagrafici"
			+ "::x001:IT::-ix:IdPaese::-len:2::-xmlPath:IdFiscaleIVA/IdPaese"
				+ "::-is:ANAC.VAT"
			+ "::x002:null::-ix:IdCodice::-xmlPath:IdFiscaleIVA/IdCodice"
				+ "::-is:ANAC.P_IVA"
				+ "::-isWrite:CGIVAT.P_IVA_CLI"
			+ "::x003:null::-ix:CodiceFiscale::-xmlPath:CodiceFiscale"
//				+ "::-is:ANAC.COD_FISCALE"
			+ "::x004:null::-ix:Denominazione::-xmlPath:Anagrafica/Denominazione"
				+ "::-is:ANAC.RAG_SOCIALE"
				;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_CSE::state-mode:master::-xmlPath:Sede"
			+ "::x001:null::-ix:Indirizzo::-xmlPath:Indirizzo"
				+ "::-is:ANAC.INDIRIZZO"
			+ "::x002:null::-ix:CAP::-xmlPath:CAP"
				+ "::-is:ANAC.CAP"
			+ "::x003:null::-ix:Comune::-xmlPath:Comune"
				+ "::-is:ANAC.LOCALITA"
			+ "::x004:null::-ix:Provincia::-xmlPath:Provincia::-xmlNotNull:val"
				+ "::-is:ANAC.PROV"
			+ "::x005:IT::-ix:Nazione::-xmlPath:Nazione"
				+ "::-is:ANAC.VAT"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);
	
		cmd = "$def:TD01_BOD::state-mode:master::-xmlPath:FatturaElettronicaBody"
			+ "::x001:null::-view:" + JPane.vEnc("$$TD01_BDG")
			+ "::x002:null::-view:" + JPane.vEnc("$$TD01_BSE")
			+ "::x003:null::-view:" + JPane.vEnc("$$TD01_PAG")
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_BDG::state-mode:master::-xmlPath:DatiGenerali/DatiGeneraliDocumento"
			+ "::x001:TD01::-ix:TipoDocumento::-xmlPath:TipoDocumento"
				+ "::-is:TABCCO.FETIPMOV"
				+ "::-isWrite:CGIVAT.FETIPMOV"
			+ "::x002:null::-ix:Giornale::-inp:n"
				+ "::-is:CGIVAT.COD_GIORNALE"
			+ "::x003:null::-ix:Divisa::-xmlPath:Divisa"
				+ "::-is:CGIVAT.DIVISA"
			+ "::x004:null::-ix:Data::-type:Date::-xmlPath:Data"
				+ "::-is:CGIVAT.DATA_DOC"
			+ "::x005:null::-ix:Numero::-xmlPath:Numero"
				+ "::-is:MAGMVT.NUM_FATTURA::-isWrite:CGIVAT.NUM_DOC"
			+ "::x006:0::-ix:ImportoTotaleDocumento"
				+ "::-type:Decimal::-xmlPath:ImportoTotaleDocumento"
				+ "::-is:CGIVAT.TOT_FAT_DARE"
				+ "::-isRead:" + JPane.vEnc("nvl(CGIVAT.TOT_FAT_DARE,0)+nvl(CGIVAT.TOT_FAT_AVERE,0)")
			+ "::x007:null::-ix:Causale::-xmlPath:Causale"
				+ "::-is:CGIVAT.CAU_CONTAB"
			+ "::x007b:null::-ix:CauDes::-xmlPath:Cau::-xml:n::-inp:n"
				+ "::-is:TABCAM.DES_CAM"
			+ "::x007c:null::-ix:CauDes2::-xmlPath:Cau::-xml:n::-inp:n"
				+ "::-is:TABCAM.DES_CAM_FAT"			
			+ "::x007d:null::-ix:FattImm::-xmlPath:Cau::-xml:n::-inp:n"
				+ "::-is:TABCAM.FLG_FAT_IMM"
			+ "::x008:null::-ix:Cau::-xmlPath:Cau::-xml:n::-inp:n"
				+ "::-is:ANAC.REFNUM_ID"
//			+ "::x008:SI::-ix:Art73::-xmlPath:Art73"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_PAG::state-mode:master::-xmlPath:DatiPagamento"
			+ "::x001:TP02::-ix:CondizioniPagamento::-xmlPath:CondizioniPagamento"
				+ "::-is:TABPAG.FECONPAG"
			+ "::x002:MP05::-ix:ModalitaPagamento::-xmlPath:DettaglioPagamento/ModalitaPagamento"
				+ "::-is:TABPAG.FEMODPAG"

			+ "::x015:null::-ix:DataScadenzaPagamento::-xmlPath:DettaglioPagamento/DataScadenzaPagamento"
				+ "::-type:Date::-xmlNotNull:val"
				+ "::-is:FATMVT.DATA_SCAD1"

			+ "::x003:0::-ix:ImportoPagamento::-type:Decimal::-xmlPath:DettaglioPagamento/ImportoPagamento"
//				+ "::-is:CGIVAT.TOT_FAT_DARE"
				+ "::-is:FATMVT.IMP_RATA1"
//			+ "::x013:null::-ix:CAB"
//			+ "::x015:null::-ix:ABI"
//			+ "::x016:null::-ix:Benificiario::-xmlPath:Benificiario"
//			+ "::x017:0::-ix:GiorniTerminiPagamento"

			+ "::x010:0"
				+ "::-type:Number::-xmlNotNull:val::-xml:n"
				+ "::-is:FATMVT.TOT_BOLLI_ES"
			+ "::x011:0"
				+ "::-type:Number::-xmlNotNull:val::-xml:n"
				+ "::-is:FATMVT.SCONTO"
			+ "::x013:0"
				+ "::-type:Number::-xmlNotNull:val::-xml:n"
				+ "::-is:FATMVT.TOT_SP_INCASSO"

			+ "::x014:0" // imponibile non ivato soggetto ritenuta
				+ "::-type:Number::-xmlNotNull:val::-xml:n"
				+ "::-is:FATMVT.TOT_IVATO_V"

			+ "::x014b:0" // % ritenuta
				+ "::-type:Number::-xmlNotNull:val::-xml:n"
//				+ "::-is:MAGMVT.RIT_ACC_XI"

			+ "::x014c:0" // totale ritenuta
				+ "::-type:Number::-xmlNotNull:val::-xml:n"
				+ "::-is:FATMVT.TOT_VARIE_V"

			+ "::x014d:null" // codice ritenuta
				+ "::-xmlNotNull:val::-xml:n"
				+ "::-is:MAGMVT.DIV2"

			+ "::x014f:null" // CodiceCUP
				+ "::-xmlNotNull:val::-xml:n"
				+ "::-is:MAGMVT.DIV6"

			+ "::x014g:null" // CodiceGIG
				+ "::-xmlNotNull:val::-xml:n"
				+ "::-is:MAGMVT.COD_VETT_DES"

			+ "::x014h:null" // Commessa
				+ "::-xmlNotNull:val::-xml:n"
				+ "::-is:MAGMVT.DIV5"

			+ "::x014i:null" // Data
				+ "::-xmlNotNull:val::-xml:n"
				+ "::-is:MAGMVT.DIV4"

			+ "::x014l:null" // Id
				+ "::-xmlNotNull:val::-xml:n"
				+ "::-is:MAGMVT.DIV3"

			+ "::x016:0::-ix:ImportoPagamento::-type:Decimal::-xmlPath:DettaglioPagamento/ImportoPagamento"
				+ "::-ixml:n::-is:FATMVT.IMP_RATA2::-xml:n"

			+ "::x017:null::-ix:DataScadenzaPagamento::-xmlPath:DettaglioPagamento/DataScadenzaPagamento"
				+ "::-type:Date::-xmlNotNull:val::-xml:n"
				+ "::-is:FATMVT.DATA_SCAD2"

			+ "::x019:null::-ix:IstitutoFinanziario::-xmlNotNull:val::-xmlPath:DettaglioPagamento/IstitutoFinanziario"
				+ isIban2
			+ "::x020:null::-ix:IBAN::-xmlNotNull:val::-xmlPath:DettaglioPagamento/IBAN"
				+ isIban
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_BSE::state-mode:master::-xmlPath:DatiBeniServizi"
			+ "::x001:null::-exec:" + JPane.vEnc("$$TD01_LINEE")
			+ "::x002:null::-exec:" + JPane.vEnc("$$TD01_IVA")
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		String tmpBronx = ""
			+ "::x001a:0::-ix:PrezzoTotaleLordo::-type:Decimal::-inp:n"
//				+ "::-is:MAGMVR.IMPORTO_UNIT_01"
				+ "::-is:MAGMVR.TOT_LOR_RIGA"
			+ "::x001b:null::-ix:Scontrino::-inp:n"
				+ "::-is:MAGMVT.ASPETTO"
			+ "::x001c:0::-ix:PrezzoTotaleNetto::-type:Decimal::-inp:n"
				+ "::-is:MAGMVR.TOT_LOR_RIGA_EU"
			;
//		MAGMVT.ASPETTO
		if(!JPane.utente.equalsIgnoreCase("JAM51") // GIO e GIZ BRONX
		 &&!JPane.utente.equalsIgnoreCase("JAM52"))
			tmpBronx = "";

		if(JPane.utente.equalsIgnoreCase("JAM70")) // SUNRISE
			tmpBronx = ""
			+ "::x0010:null::-ix:Ordine::-inp:n"
			+ "::-is:MAGMVT.NOTE_10"
			;

		cmd = "$def:TD01_LINEE::state-mode:detail::-xmlPath:DettaglioLinee"
			+ "::x001:0::-ix:NumeroLinea::-type:Integer::-xmlPath:NumeroLinea"
				+ "::-is:MAGMVR.NUM_RIGA"
			+ tmpBronx
			+ "::x001d:0::-ix:Sconto1::-type:Integer::-inp:n"
				+ "::-is:MAGMVR.SCONTO_1"
			+ "::x001e:0::-ix:Sconto2::-type:Integer::-inp:n"
				+ "::-is:MAGMVR.SCONTO_2"
			+ "::x001i:0::-ix:Sconto5::-type:Integer::-inp:n"
				+ "::-is:MAGMVR.SCO_05"
			+ "::x002:COD::-ix:CodiceTipo::-def:COD::-xmlPath:CodiceArticolo/CodiceTipo"
			+ "::x003:null::-ix:CodiceValore::-xmlPath:CodiceArticolo/CodiceValore"
				+ "::-is:MAGMVR.COD_ARTICOLO"
			+ "::x004:null::-ix:Descrizione::-xmlPath:Descrizione"
//				+ "::-is:MAGMVR.DESCRD"
				+ "::-is:MAGMVR.DES_ARTICOLO||MAGMVR.DESCRD"
			+ "::x005:0::-ix:Quantita::-xmlPath:Quantita::-type:Decimal"
				+ "::-is:MAGMVR.QUANTITA"

			+ "::x006:N::-ix:UnitaMisura::-xmlPath:UnitaMisura::-def:N"
				+ "::-is:MAGMVR.UNITA_MISURA"

			+ "::x007:0::-ix:PrezzoUnitario::-xmlPath:PrezzoUnitario::-type:Decimal"
				+ "::-fmt:##########0.00###::-is:"
				+ JPane.vEnc("MAGMVR.TOT_NET_RIGA_VA/decode(MAGMVR.QUANTITA,0,1,MAGMVR.QUANTITA)")

			+ "::x008:0::-ix:PrezzoTotale::-xmlPath:PrezzoTotale::-type:Decimal"
				+ "::-is:MAGMVR.TOT_NET_RIGA_VA"

			+ "::x009:0::-ix:AliquotaIVA::-xmlPath:AliquotaIVA::-type:Decimal"
				+ "::-is:TABIVA.PERC_IVA"

			+ "::x009b:null::-ix:Ritenuta::-xmlPath:Ritenuta::-xmlNotNull:val::-xmlNot:*"
				+ "::-is:TABIVA.CODICE_2"

			+ "::x010:null::-ix:Natura::-xmlPath:Natura::-xmlNotNull:val::-xmlNot:*"
				+ "::-is:TABIVA.NATURA"

			+ "::x015:0::-type:Integer::-inp:n"
				+ "::-is:MAGMVR.NUM_MOVIM"
			+ "::x016:null::-type:Data::-inp:n"
				+ "::-is:MAGMVR.DATA_MOVIM"

//			+ "::x810:null::-ix:Codice::-tit:Codice::-type:Button::-view:"
//								+ JPane.vEnc("$$TD01_LINEE_COD")+"::-inp:n"
//			+ "::x811:null::-ix:Altri::-tit:Altri::-type:Button::-view:"
//								+ JPane.vEnc("$$TD01_LINEE_ALTRI")+"::-inp:n"
			;

		if(JPane.utente.equalsIgnoreCase("PETRA79"))
		cmd += ""
			+ "::x021:null::-inp:n"
			+ "::-is:MAGMVT.TARGA"
			+ "::x022:null::-inp:n"
			+ "::-is:MAGMVT.DATI_CAR"
			+ "::x023:null::-inp:n"
			+ "::-is:MAGMVT.DATI_PROP"
			+ "::x024:null::-inp:n"
			+ "::-is:MAGMVT.DATI_COMP"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);
//		if(JPane.utente.equalsIgnoreCase("JAM50") // FORMOTO
//		 ||JPane.utente.equalsIgnoreCase("PETRA71") // MANZONI
//		 ||JPane.utente.equalsIgnoreCase("JAM70")) {// SUNRISE
		if(!JPane.utente.equalsIgnoreCase("JAM51") // GIO
		 &&!JPane.utente.equalsIgnoreCase("JAM52")) {// GIZ
			cmd = "$def:TD01_LINEE::x007-is:MAGMVR.IMPORTO_UNIT_VA"
					+ "::x008-is:MAGMVR.TOT_NET_RIGA_VA";
			master = frame.execBase(jp, cmd, 0, jp, master);
		}
		if(!JPane.utente.equalsIgnoreCase("JAM70"))
		{// SUNRISE)
			cmd = "$def:TD01_LINEE::001i-is:null";
			master = frame.execBase(jp, cmd, 0, jp, master);
		}

		cmd = "$def:TD01_LINEE_COD::state-mode:master::-xmlPath:CodiceArticolo"
			+ "::x001:INTERNO|EAN::-ix:CodiceTipo"
			+ "::x002:733433254|1234567890::-ix:CodiceValore"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_LINEE_ALTRI::state-mode:master::-xmlPath:AltriDatiGestionali"
			+ "::x001:CONFEZIONI::-ix:TipoDato"
			+ "::x002:Confezioni::-ix:RiferimentoTesto"
			+ "::x003:3.00::-ix:RiferimentoNumero"
			+ "::x004:2018-04-27::-ix:RiferimentoData"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:TD01_IVA::state-mode:detail::-xmlPath:DatiRiepilogo"
			+ "::x001:0::-ix:AliquotaIVA::-type:Decimal::-xmlPath:AliquotaIVA"
				+ "::-is:TABIVA.PERC_IVA::-isWrite:CGIVAR.PERC_IVA"
			+ "::x002:0::-ix:Natura::-xmlPath:Natura::-xmlNotNull:val::-xmlNot:*"
				+ "::-is:TABIVA.NATURA::-isWrite:CGIVAR.COD_IVA"
			+ "::x003:0::-ix:ImponibileImporto::-xmlPath:ImponibileImporto::-type:Decimal"
				+ "::-is:CGIVAR.IMP_A_VAL"
				+ "::-isRead:" + JPane.vEnc("nvl(CGIVAR.IMP_A_VAL,0)"
						+ "+nvl(CGIVAR.IMP_D_VAL,0)")
				+ "::-fmt:##########0.00"
			+ "::x005:0::-ix:Imposta::-type:Decimal::-xmlPath:Imposta"
				+ "::-is:CGIVAR.IMPOST_A_VAL"
				+ "::-isRead:" + JPane.vEnc("nvl(CGIVAR.IMPOST_A_VAL,0)"
						+ "+nvl(CGIVAR.IMPOST_D_VAL,0)")
				+ "::-fmt:##########0.00"
			+ "::x006:I::-ix:EsigibilitaIVA::-def:I::-xmlPath:EsigibilitaIVA"
				+ "::-is:TABIVA.MOD_101_102"
			+ "::x007:null::-ix:RiferimentoNormativo::-xmlPath:RiferimentoNormativo"
				+ "::-xmlNotNull:val"
				+ "::-is:TABIVA.DE_IVA"
				+ "::-isRead:"
				+ JPane.vEnc("decode(TABIVA.PERC_IVA,0,TABIVA.DE_IVA,'')")
				
				;
		master = frame.execBase(jp, cmd, 0, jp, master);

		cmd = "$def:jamFat_base_master::x001:001"
			+ "::x002:null::-ix:HtmlBegin"
			+ "::x002-val:" 
			+ JPane.vEnc("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<?xml-stylesheet type=\"text/xsl\" href=\"fatturapa_v1.2.xsl\"?>\n"
				+ "<p:FatturaElettronica"
				+ " xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\""
				+ " xmlns:p=\"http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2\""
				+ " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " versione=\"FPR12\">\n"
			)

			+ "::x003:null::-ix:HtmlBeginEnd"
			+ "::x003-val:_"

			+ "::x004:null::-ix:PaneBegin"
			+ "::x004-val:" + JPane.vEnc("<@{pane-xmlPath}>\n")

//			+ "::x005:null::-ix:LineBegin"
//			+ "::x005-val:" + JPane.vEnc("<!-- lineBegin:@{pane-xmlPath}-->\n")

			+ "::x010:null::-ix:ItemTextEdit"
			+ "::x010-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x011:null::-ix:ItemDecimalEdit"
			+ "::x011-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x012:null::-ix:ItemIntegerEdit"
			+ "::x012-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x013:null::-ix:ItemDateEdit"
			+ "::x013-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

//			+ "::x070:null::-ix:LineEnd"
//			+ "::x070-val:" + JPane.vEnc("<!-- lineEnd:@{pane-xmlPath}-->\n")

			+ "::x080:null::-ix:PaneEnd"
			+ "::x080-val:" + JPane.vEnc("</@{pane-xmlPath}>\n")

			+ "::x090:null::-ix:HtmlEnd"
			+ "::x090-val:" + JPane.vEnc("</p:FatturaElettronica>\n")
			;
		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpFat = master.get("jamFat_base_master", "");
		if(debug) System.out.println(jpFat.jO.toString(4));

		cmd = "$def:jamFat_base_detail::x001:001"
			+ "::x002:null::-ix:HtmlBegin"

			+ "::x003:null::-ix:HtmlBeginEnd"
			+ "::x003-val:_"

			+ "::x004:null::-ix:LineBegin"
			+ "::x004-val:" + JPane.vEnc("<@{pane-xmlPath}>\n")

			+ "::x010:null::-ix:ItemTextEdit"
			+ "::x010-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x011:null::-ix:ItemDecimalEdit"
			+ "::x011-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x012:null::-ix:ItemIntegerEdit"
			+ "::x012-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x013:null::-ix:ItemDateEdit"
			+ "::x013-val:" + JPane.vEnc("<@{att:xmlPath}>@{att:val}</@{att:xmlPath}>\n")

			+ "::x080:null::-ix:LineEnd"
			+ "::x080-val:" + JPane.vEnc("</@{pane-xmlPath}>\n")

			+ "::x090:null::-ix:HtmlEnd"
			;
		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpFatD = master.get("jamFat_base_master", "");
		if(debug) System.out.println(jpFatD.jO.toString(4));
		
		
		cmd = "$def:jam001_base_master::x001:001"
			+ "::x002:null::-ix:HtmlBegin"
			+ "::x002-val:" 
			+ JPane.vEnc("<html>\n<head>\n<title>Rilevazione Prezzi Emmi</title>\n"
				+ "<link href='/main/css/emmi3.css' rel='stylesheet'>\n"
				+ "<link href='/main/css/emmi9.css' rel='stylesheet'>\n"
			)

			+ "::x003:null::-ix:HtmlBeginEnd"
			+ "::x003-val:"
			+ JPane.vEnc("<body style='background-color:#175ab0; color:white; font-family:\"Open Sans\", Helvetica, Arial, sans-serif;'>\n"
				+ "<div style='font-size:25px;'><div class='container' onclick='myMenu(this)'>\n"
				+ "<div class='bar1'></div><div class='bar2'></div><div class='bar3'></div></div>\n"
				+ "Rilevazione Prezzi\n"
				+ "<input type='button' value='Tool' id='@{att:id_tool}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
				+ "<input type='button' value='Ritorno' id='@{att:id_ritorno}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
				+ "<input type='button' value='Nuovo' id='@{att:id_nuovo}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
				+ "<input type='button' value='OkSalva' id='@{att:id_oksalva}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
				+ "<img class='logo' width='80' align='right' src='https://it.emmi.com/typo3temp/assets/_processed_/a/8/csm_mainlogo_dd7328a6fc.png' "
				+ "alt='Emmi Italia' title='Emmi Italia'>\n"
				+ "</div>")

			+ "::x004:null::-ix:PaneBegin"
			+ "::x004-val:" + JPane.vEnc("<div>\n")

			+ "::x005:null::-ix:PaneBeginEnd"
			+ "::x005-val:" + JPane.vEnc("\n")

			+ "::x007:null::-ix:LineBegin"
			+ "::x007-val:" + JPane.vEnc("\n")

			+ "::x010:null::-ix:ItemBegin"
			+ "::x010-val:" + JPane.vEnc("<div>\n")

			+ "::x012:null::-ix:ItemLabel"
			+ "::x012-val:" + JPane.vEnc("<div style='color:yellow'>@{att:tit}</div>\n")

			+ "::x015:null::-ix:ItemTextEdit"
			+ "::x015-val:" + JPane.vEnc("<input type='text' value='@{att:val}' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

			+ "::x017:null::-ix:ItemTextView"
			+ "::x017-val:" + JPane.vEnc("<input type='text' value='@{att:val}' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

			+ "::x019:null::-ix:ItemCercaEdit"
			+ "::x019-val:" + JPane.vEnc("<select id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'>"
											+ "<option value='' selected='selected'>-- @{att:tit} --</option>\n"
											+ "@{sys.option}\n"
											+ "</select>\n")

			+ "::x020:null::-ix:ItemDecimalEdit"
			+ "::x020-val:" + JPane.vEnc("<input type='text' value='@{att:val}' style='width:100px;'></input>\n")

			+ "::x022:null::-ix:ItemDecimalView"
			+ "::x022-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

			+ "::x024:null::-ix:ItemNumberEdit"
			+ "::x024-val:" + JPane.vEnc("<input type='number' value='@{att:val}' style='width:100px;' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

			+ "::x026:null::-ix:ItemNumberView"
			+ "::x026-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

			+ "::x040:null::-ix:ItemButtonEdit"
			+ "::x040-val:" + JPane.vEnc("<input type='button' value='@{att:tit}' id='@{att:id}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'>")

			+ "::x045:null::-ix:ItemButtonView"
			+ "::x045-val:" + JPane.vEnc("<input type='button' value='@{att:tit}' id='@{att:id}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'>")

			+ "::x050:null::-ix:ItemEnd"
			+ "::x050-val:" + JPane.vEnc("</div>\n")

			+ "::x070:null::-ix:LineEnd"
			+ "::x070-val:" + JPane.vEnc("\n")

			+ "::x080:null::-ix:PaneEnd"
			+ "::x080-val:" + JPane.vEnc("</div>\n"
				+ "<script src='/view17/main/jquery.min.js'></script>\n"
				+ "<script src='/view17/jam001.js?time=" + frame.getDate() + "'></script>\n"
						)

			+ "::x090:null::-ix:HtmlEnd"
			+ "::x090-val:" + JPane.vEnc("</body>\n</html>\n")
			;

		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpFmt = master.get("jam001_base_master", "");
		if(debug) System.out.println(jpFmt.jO.toString(4));
//		ritorno += jpFmt.paneJSON.toString(4) + "\n";
//		if(true) return master;

		cmd 	= "$def:jam001_tool_master::x001:001"
				+ "::x002:null::-ix:HtmlBegin"
				+ "::x002-val:" + JPane.vEnc("<html>\n<head>\n<title>Rilevazione Prezzi Emmi</title>\n"
						+ "<link href='/view17/emmi3.css' rel='stylesheet'>\n"
						+ "<link href='/view17/emmi9.css' rel='stylesheet'>\n"
						+ "<link href='/view17/main/jquery-ui.css' rel='stylesheet'/>\n")

				+ "::x003:null::-ix:HtmlBeginEnd0"
				+ "::x003-val:" + JPane.vEnc("<body style='background-color:#175ab0; color:white; font-family:\"Open Sans\", Helvetica, Arial, sans-serif;'>\n"
						+ "<div style='font-size:25px;'><div class='container' onclick='myMenu(this)'>\n"
						+ "<div class='bar1'></div><div class='bar2'></div><div class='bar3'></div></div>\n"
						+ "Rilevazione Prezzi"
						+ "<input type='button' value='Base' id='@{att:id_base}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
						+ "<input type='button' value='Ritorno' id='@{att:id_ritorno}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
						+ "<input type='button' value='Nuovo' id='@{att:id_nuovo}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
						+ "<input type='button' value='OkSalva' id='@{att:id_oksalva}' onClick='jamSendDataClick(this,\"@{sys.terminal}\");'></input>\n"
						+ "<img class='logo' width='80' align='right' "
						+ "src='https://it.emmi.com/typo3temp/assets/_processed_/a/8/csm_mainlogo_dd7328a6fc.png' "
						+ "alt='Emmi Italia' title='Emmi Italia'>\n"
						+ "</div>")

				+ "::x004:null::-ix:PaneBegin"
				+ "::x004-val:" + JPane.vEnc("<div>\n")

				+ "::x005:null::-ix:PaneBeginEnd"
				+ "::x005-val:" + JPane.vEnc("\n")

				+ "::x007:null::-ix:LineBegin"
				+ "::x007-val:" + JPane.vEnc("\n")

				+ "::x010:null::-ix:ItemBegin"
				+ "::x010-val:" + JPane.vEnc("<div class='itemdrag' style='width:300px'>\n"
				+ "<div style='' class='drag-input'>\n")

				+ "::x012:null::-ix:ItemLabel"
				+ "::x012-val:" + JPane.vEnc("<div style='color:yellow'>@{att:tit}</div>\n")
				
				+ "::x015:null::-ix:ItemTextEdit"
				+ "::x015-val:" + JPane.vEnc("<input type='text' value='@{att:val}' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x017:null::-ix:ItemTextView"
				+ "::x017-val:" + JPane.vEnc("<input type='text' value='@{att:val}' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x019:null::-ix:ItemCercaEdit"
				+ "::x019-val:" + JPane.vEnc("<select id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'>"
											+ "<option value='' selected='selected'>-- @{att:tit} --</option>"
											+ "</select>\n")
				
				+ "::x020:null::-ix:ItemButtonEdit"
				+ "::x020-val:" + JPane.vEnc("<input type='button' value='@{att:tit}'>")

				+ "::x040:null::-ix:ItemDecimalEdit"
				+ "::x040-val:" + JPane.vEnc("<input type='text' value='@{att:val}' style='width:100px;' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x042:null::-ix:ItemDecimalView"
				+ "::x042-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

				+ "::x044:null::-ix:ItemNumberEdit"
				+ "::x044-val:" + JPane.vEnc("<input type='number' value='@{att:val}' style='width:100px;' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x046:null::-ix:ItemNumberView"
				+ "::x046-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

				+ "::x048:null::-ix:ItemButtonView"
				+ "::x048-val:" + JPane.vEnc("<input type='button' value='@{att:tit}'>")

				+ "::x050:null::-ix:ItemEnd"
				+ "::x050-val:" + JPane.vEnc("</div>\n</div>\n")

				+ "::x070:null::-ix:LineEnd"
				+ "::x070-val:" + JPane.vEnc("\n")

				+ "::x080:null::-ix:PaneEnd"
				+ "::x080-val:" + JPane.vEnc("</div>\n"
						+ "<script src='/view17/main/jquery.min.js'></script>\n"
						+ "<script src='/view17/main/jquery-ui.min.js'></script>\n"
						+ "<script src='/view17/jam001.js?time=" + frame.getDate() + "'></script>\n"
						+ "<script>\n"
						+ "$(document).ready(function() {\n"
						+ "$('.itemdrag').draggable({ grid: [12, 24], containment: 'parent', revertDuration:1000, zIndex:96})\n"
						+ ".resizable({ grid: [12, 24], containment: 'parent', zIndex:96, resize:function(event, ui) {\n"

						+ "$('input', this).css({width:(parseInt(this.style.width))\n"
						+ ",height:(parseInt(this.style.height)-24),top:0,float:'left'});\n"

						+ "$('button', this).css({width:(parseInt(this.style.width)-12)\n"
						+ ",height:(parseInt(this.style.height)),top:0,float:'left'});\n"
//						getIpvData('pwd1', 'cmd=edit&val='+this.id +'|'+this.style.left +'|'+this.style.top +'|'+this.style.width +'|'+this.style.height);
						+ "event.stopPropagation();\n"
						+ "}});\n"
						+ "$('.itemdrag').bind('dragstop', function(event, ui) {\n"
//						getIpvData('pwd1', 'cmd=edit&val='+this.id +'|'+this.style.left +'|'+this.style.top +'|'+this.style.width +'|'+this.style.height);
						+ "});\n"
						+ "});\n"
						+ "</script>\n")

				+ "::x090:null::-ix:HtmlEnd"
				+ "::x090-val:" + JPane.vEnc("</body>\n</html>\n")
				;

		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpFmtTool = master.get("jam001_tool_master", "");
		if(debug) System.out.println(jpFmtTool.jO.toString(4));
//		ritorno += jpFmtTool.paneJSON.toString(4) + "\n";

		cmd 	= "$def:jam001_base_detail::x001:001"
				+ "::x004:null::-ix:PaneBegin"
				+ "::x004-val:" + JPane.vEnc("<br/>\n<div>\n")

				+ "::x005:null::-ix:PaneBeginEnd"
				+ "::x005-val:" + JPane.vEnc("<table>\n")

				+ "::x007:null::-ix:LineBegin"
				+ "::x007-val:" + JPane.vEnc("<tr>\n")

				+ "::x010:null::-ix:ItemBegin"
				+ "::x010-val:" + JPane.vEnc("<td><div>\n")

				+ "::x012:null::-ix:ItemLabel"
				+ "::x012-val:" + JPane.vEnc("<div style='color:yellow'>@{att:tit}</div>\n")
				
				+ "::x015:null::-ix:ItemTextEdit"
				+ "::x015-val:" + JPane.vEnc("<input type='text' value='@{att:val}' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x017:null::-ix:ItemTextView"
				+ "::x017-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

				+ "::x020:null::-ix:ItemDecimalEdit"
				+ "::x020-val:" + JPane.vEnc("<input type='text' value='@{att:val}' style='width:100px;' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x022:null::-ix:ItemDecimalView"
				+ "::x022-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

				+ "::x024:null::-ix:ItemNumberEdit"
				+ "::x024-val:" + JPane.vEnc("<input type='number' value='@{att:val}' style='width:100px;' id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'></input>\n")

				+ "::x026:null::-ix:ItemNumberView"
				+ "::x026-val:" + JPane.vEnc("<div>@{att:val}</div>\n")

				+ "::x030:null::-ix:ItemButtonEdit"
				+ "::x030-val:" + JPane.vEnc("<input type='button' value='@{att:tit}' id='@{att:id}' onChange='jamSendDataClick(this,\"@{sys.terminal}\");'>")

				+ "::x040:null::-ix:ItemCercaEdit"
				+ "::x040-val:" + JPane.vEnc("<select id='@{att:id}' onChange='jamSendData(this,\"@{sys.terminal}\");'>"
											+ "<option value='' selected='selected'>-- @{att:tit} --</option>\n"
											+ "@{sys.option}\n"
											+ "</select>\n")

				+ "::x050:null::-ix:ItemEnd"
				+ "::x050-val:" + JPane.vEnc("</div></td>\n")

				+ "::x070:null::-ix:LineEnd"
				+ "::x070-val:" + JPane.vEnc("</tr>\n")

				+ "::x080:null::-ix:PaneEnd"
				+ "::x080-val:" + JPane.vEnc("</table>\n")
				;

		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpFmtDetail = master.get("jam001_base_detail", "");
		if(debug) System.out.println(jpFmtDetail.jO.toString(4));
//		ritorno += jpFmtDetail.paneJSON.toString(4) + "\n";

		cmd 	= "$def:jam001_base_option::x001:001"
				+ "::x004:null::-ix:PaneBegin"
				+ "::x004-val:" + JPane.vEnc("_")

				+ "::x005:null::-ix:PaneBeginEnd"
				+ "::x005-val:" + JPane.vEnc("_")

				+ "::x007:null::-ix:LineBegin"
				+ "::x007-val:" + JPane.vEnc("<option value='@{att:val}'>")

				+ "::x010:null::-ix:ItemBegin"
				+ "::x010-val:" + JPane.vEnc("_")

//				+ "::x012:null::-ix:ItemLabel"
//				+ "::x012-val:" + jPane.vEnc("<div style='color:yellow'>@{att:tit}</div>\n")

				+ "::x017:null::-ix:ItemTextView"
				+ "::x017-val:" + JPane.vEnc("@{att:val} ")

				+ "::x022:null::-ix:ItemDecimalView"
				+ "::x022-val:" + JPane.vEnc("@{att:val} ")

				+ "::x026:null::-ix:ItemNumberView"
				+ "::x026-val:" + JPane.vEnc("@{att:val} ")

				+ "::x050:null::-ix:ItemEnd"
				+ "::x050-val:" + JPane.vEnc("_")

				+ "::x070:null::-ix:LineEnd"
				+ "::x070-val:" + JPane.vEnc("</option>\n")

				+ "::x080:null::-ix:PaneEnd"
				+ "::x080-val:" + JPane.vEnc("_")
				;

		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpFmtOption = master.get("jam001_base_option", "");
		if(debug) System.out.println(jpFmtOption.jO.toString(4));
//		ritorno += jpFmtOption.paneJSON.toString(4) + "\n";

		cmd 	= "$def:jam001_sql_select::x001:001"
				+ "::x004:null::-ix:PaneBegin"
				+ "::x004-val:" + JPane.vEnc("_")

				+ "::x005:null::-ix:PaneBeginEnd"
				+ "::x005-val:" + JPane.vEnc("select ")

				+ "::x007:null::-ix:LineBegin"
				+ "::x007-val:" + JPane.vEnc("_")

				+ "::x009:null::-ix:ItemSepa"
				+ "::x009-val:" + JPane.vEnc("\n\t,")

				+ "::x010:null::-ix:ItemBegin"
				+ "::x010-val:" + JPane.vEnc("@{att:is} @{att:item_id}")

//				+ "::x012:null::-ix:ItemLabel"
//				+ "::x012-val:" + jPane.vEnc("<div style='color:yellow'>@{att:tit}</div>\n")

				+ "::x017:null::-ix:ItemTextView"
				+ "::x017-val:" + JPane.vEnc("_")

				+ "::x022:null::-ix:ItemDecimalView"
				+ "::x022-val:" + JPane.vEnc("_")

				+ "::x026:null::-ix:ItemNumberView"
				+ "::x026-val:" + JPane.vEnc("_")

				+ "::x050:null::-ix:ItemEnd"
				+ "::x050-val:" + JPane.vEnc("_")

				+ "::x070:null::-ix:LineEnd"
				+ "::x070-val:" + JPane.vEnc("\n")

				+ "::x080:null::-ix:PaneEnd"
				+ "::x080-val:" + JPane.vEnc("from @{is^file^uniq}\n")
				;

		master = frame.execBase(jp, cmd, 0, jp, master);
		JPaneDoc jpSqlSelect = master.get("jam001_sql_select", "");
		if(debug) System.out.println(jpSqlSelect.jO.toString(4));
//		ritorno += jpSqlSelect.paneJSON.toString(4) + "\n";

		cmd 	= "$def:fmvx_det::state-mode:detail"
				+ "::x001:0::-len:6::-ix:Numero::-tit:Numero::-nkey:1::-type:Number"
				+ "::x002:1::-len:6::-ix:Riga::-tit:Riga::-nkey:1::-type:Number"
				+ "::x003:null::-len:20::-ix:Causale::-tit:Causale::-type:Cerca::-cerca:"+JPane.vEnc("$bro:causali")
				+ "::x004:null::-len:20::-ix:Prodotto::-tit:Prodotto::-type:Cerca::-cerca:"+JPane.vEnc("$bro:articoli")+"::-from:articoli::Prodotto"
				+ "::x005:null::-len:80::-ix:Descrizione::-tit:Descrizione"+"::-from:articoli::Descrizione"
				+ "::x006:null::-len:5::-ix:Prezzo::-tit:Prezzo::-type:Decimal::-touch:"+JPane.vEnc("Lordo,Netto")+"::-from:articoli::Prezzo"
				+ "::x007:null::-len:5::-ix:Qta::-tit:Qta::-type:Number::-touch:"+JPane.vEnc("Lordo,Netto")
				+ "::x008:null::-len:5::-ix:Lordo::-tit:Lordo::-type:Decimal::-for:"+JPane.vEnc("@{Qta}*@{Prezzo}")+"::-input:o"
				+ "::x009:null::-len:5::-ix:Sconto::-tit:Sconto::-type:Decimal::-touch:"+JPane.vEnc("Netto")
				+ "::x010:null::-len:5::-ix:Netto::-tit:Netto::-type:Decimal::-for:"+JPane.vEnc("@{Lordo}-((@{Lordo}*@{Sconto})/100)")+"::-input:o"
				+ "::x011:null::-len:5::-ix:Iva::-tit:Iva::-type:Cerca::-touch:"+JPane.vEnc("Ivato")+"::-from:articoli::Iva::-cerca:"+JPane.vEnc("$bro:tabiva")
				+ "::x012:null::-len:5::-ix:Esenzione::-tit:Esen::-type:Number::-touch:"+JPane.vEnc("Ivato")+"::-from:tabiva::Esenzione"
				+ "::x013:null::-len:5::-ix:Percentuale::-tit:Perc::-type:Number::-touch:"+JPane.vEnc("Ivato")+"::-from:trabiva::Percentuale"
				+ "::x014:null::-len:8::-ix:Ivato::-tit:Ivato::-type:Decimal::-for:"+JPane.vEnc("@{Netto}+((@{Netto}*@{Percentuale})/100)")+"::-input:o"
				+ "::x015:null::-len:80::-ix:Note::-tit:Note"
				+ "::x891:null::-ix:NewLine::-tit:NewLine::-type:Cerca::-cerca:"+JPane.vEnc("$bro:causali")
				+ "";
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpDet = master.get("fmvx_det", "");
		if(debug) System.out.println(jpDet.jO.toString(4));
//		ritorno += jpDet.paneJSON.toString(4) + "\n";

		cmd 	= "$def:causali"
				+ "::x001:null::-ix:Causale::-len:80::x001-val:"+JPane.vEnc("ORD|VEN|RIL")+"::-tit:Causale::-nkey:1"
				+ "::x002:null::-ix:DesCausale::-len:80::x002-val:"+JPane.vEnc("Ordine|Vendita|Rilevazione")+"::-tit:DesCausale"
				+ "::x898:null::-ix:NewCode::-tit:NewCode::-type:Button::-exec:"+JPane.vEnc("$NewCode")
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$OkSalva")
				;
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpCau = master.get("causali", "");
		if(debug) System.out.println(jpCau.jO.toString(4));
//		ritorno += jpCau.paneJSON.toString(4) + "\n";

/*		cmd 	= "$def:tabiva"
				+ "::x001:null::-ix:CodIva::-len:80::x001-val:"+JPane.vEnc("18|22|ES")+"::-tit:CodIva::-nkey:1"
				+ "::x002:null::-ix:DesIva::-len:80::x002-val:"+JPane.vEnc("Iva 18%|Iva 22%|Iva Esente")+"::-tit:DesIva"
				+ "::x003:null::-ix:Esenzione::-len:80::x003-val:"+JPane.vEnc("null|null|Art.8")+"::-tit:Esen"
				+ "::x004:null::-ix:Percentuale::-len:80::x004-val:"+JPane.vEnc("18|22|0")+"::-tit:Perc"
				+ "::x898:null::-ix:NewCode::-tit:NewCode::-type:Button::-exec:"+JPane.vEnc("$NewCode")
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$OkSalva")
				;
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpIva = master.get("tabiva", "");

		if(debug) System.out.println(jpIva.jO.toString(4));
//		ritorno += jpIva.paneJSON.toString(4) + "\n";
*/
		cmd 	= "$def:agenti"
				+ "::x001:null::-ix:Agente::-len:80::x001-val:"+JPane.vEnc("001|002|003")+"::-tit:Agente::-nkey:1"
				+ "::x002:null::-ix:DesAgente::-len:80::x002-val:"+JPane.vEnc("Agente Uno|Agente Due|Agente Tre")+"::-tit:DesAgente"
				+ "::x898:null::-ix:NewCode::-tit:NewCode::-type:Button::-exec:"+JPane.vEnc("$NewCode")
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$OkSalva")
				;
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpAge = master.get("agenti", "");
		if(debug) System.out.println(jpAge.jO.toString(4));
//		ritorno += jpAge.paneJSON.toString(4) + "\n";

		cmd 	= "$def:articoli"
				+ "::x001:null::-ix:Articolo::-len:80::x001-val:"+JPane.vEnc("001|002|003")+"::-tit:Articolo::-nkey:1"
				+ "::x002:null::-ix:Descrizione::-len:80::x002-val:"+JPane.vEnc("ArticoloUno|ArticoloDue|ArticoloTre")+"::-tit:Descrizione"
				+ "::x898:null::-ix:NewCode::-tit:NewCode::-type:Button::-exec:"+JPane.vEnc("$NewCode")
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$OkSalva")
				;
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpArt = master.get("agenti", "");
		if(debug) System.out.println(jpArt.jO.toString(4));
//		ritorno += jpArt.paneJSON.toString(4) + "\n";

		cmd 	= "$def:fmvx::state-new:" + JPane.vEnc("@{max^Numero}")
				+ "::x001:0::-len:6::-ix:Numero::-tit:Numero::-nkey:1::-type:Number"
				+ "::x002:null::-len:20::-ix:PuntoVendita::-tit:PuntoVendita"
				+ "::x003:null::-len:6::-ix:Data::-tit:Data::-for:"+JPane.vEnc("@{sys.data}")
				+ "::x004:null::-len:3::-ix:Agente::-tit:Agente::-type:Cerca::-cerca:"+JPane.vEnc("$bro:agenti")+"::-from:"+JPane.vEnc("agenti::Agente")
				+ "::x005:null::-len:80::-ix:DesAgente::-tit:DesAgente::-from:"+JPane.vEnc("agenti::DesAgente")
				+ "::x006:null::-len:80::-ix:Note::-tit:Note::-vsi:3::lsi:40"
				+ "::x891:null::-ix:Dettaglio::-tit:Dettaglio::-type:Button::-view:"+JPane.vEnc("$$fmvx_det")+"::-inp:n"
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$okSalva")
				+ "";
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpPane = master.get("fmvx", "");
		if(debug) System.out.println(jpPane.jO.toString(4));
//		ritorno += jpPane.paneJSON.toString(4) + "\n";
	
		cmd 	= "$def:tbxl::state-new:" + JPane.vEnc("@{max^Numero}")
				+ "::x001:0::-len:2::-ix:Numero::-tit:Numero::-nkey:1::-type:Number"
				+ "::x002:null::-len:20::-ix:PuntoVendita::-tit:PuntoVendita"
				+ "::x003:null::-len:6::-ix:Data::-tit:Data::-for:"+JPane.vEnc("@{sys.data}")
				+ "::x891:null::-ix:Dettaglio::-tit:Dettaglio::-type:Button::-view:"+JPane.vEnc("$$fmvx_det")+"::-inp:n"
				+ "::x899:null::-ix:OkSalva::-tit:OkSalva::-type:Button::-exec:"+JPane.vEnc("$okSalva")
				+ "";
		master = frame.execBase(jp, cmd, 0, jp, master);

		JPaneDoc jpXl = master.get("tbxl", "");
		if(debug) System.out.println(jpXl.jO.toString(4));
//		ritorno += jpXl.paneJSON.toString(4) + "\n";

		jp.endHelp("w9", "paneNUOVO End", startTime);
		return master;
	}

	static String helpIva = "";
	//==========================================================================
	String ivaDetDTE(JPaneDoc jp) {
		int totDoc = 0;
		long totImponi = 0;
		long totImpost = 0;
		String outDet = "";
		String fmtDet = ""
		+ "<CessionarioCommittenteDTE>\n" // Ogni Cliente
        + "<IdentificativiFiscali>\n"                                                                                                                      
           +"<IdFiscaleIVA>\n"                                                                                                                           
           + "<IdPaese>@{x003}</IdPaese>\n"                                                                                                                
           + "<IdCodice>@{x001}</IdCodice>\n"                                                                                                     
           + "</IdFiscaleIVA>\n"                                                                                                                          
           + "<CodiceFiscale>@{x004}</CodiceFiscale>\n"                                                                                               
        +"</IdentificativiFiscali>\n"    

        + "<AltriDatiIdentificativi>\n"                                                                                                                    
        + "<Denominazione>@{x005}</Denominazione>\n"                                                                       
        + "<Sede>\n"                                                                                                                                   
        + "<Indirizzo>@{x006}</Indirizzo>\n"                                                                                       
        + "<CAP>@{x007}</CAP>\n"                                                                                                                     
        + "<Comune>@{x008}</Comune>\n"                                                                                                              
        + "<Provincia>@{x009}</Provincia>\n"                                                                                                            
        + "<Nazione>@{x010}</Nazione>\n"                                                                                                                
        + "</Sede>\n"                                                                                                                                  
        + "</AltriDatiIdentificativi>\n"                                                                                                                   
//        + "<DatiFatturaBodyDTE>\n"                                                                                                                         
        ;

		String fmtDet2 = ""
		+ "<DatiFatturaBodyDTE>\n"                                                                                                                         
        + "<DatiGenerali>\n" // Ognifattura
        + "<TipoDocumento>@{x011}</TipoDocumento>\n"                                                                                                  
        + "<Data>@{x012}</Data>\n"                                                                                                              
        + "<Numero>@{x002}</Numero>\n"                                                                                                               
//        + "<DataRegistrazione>@{x012}</DataRegistrazione>\n"                                                                                    
        + "</DatiGenerali>\n"
        ;

		String fmtDet3 = ""
        	+ "<DatiRiepilogo>\n" // Ogni aliquota
        	+ "<ImponibileImporto>@{x013}</ImponibileImporto>\n"                                                                                        
            + "<DatiIVA>\n"                                                                                                                            
            + "<Imposta>@{x014}</Imposta>\n"                                                                                                         
            + "<Aliquota>@{x015}</Aliquota>\n"                                                                                                       
            + "</DatiIVA>\n"
              + "<Natura>@{x016}</Natura>\n"                                                                                                                           
//            + "<Deducibile>SI</Deducibile>\n"                                                                                                          
              + "<EsigibilitaIVA>I</EsigibilitaIVA>\n"                                                                                                   
            + "</DatiRiepilogo>\n" 
            ;

    	String fmtDet9 = ""
 //       + "</DatiFatturaBodyDTE>\n"         
        + "</CessionarioCommittenteDTE>\n";

    	String fmtDet9b = ""
    	     + "</DatiFatturaBodyDTE>\n"         
    	        ;
    
		String sqlTxt = "select"
				+ " val_idcodice_2_2_1 x001"
				+ ",val_numero_2_3_1_3 x002"
				+ ",VAL_IDPAESE_2_2_1_1_2 x003"
				+ ",VAL_IDENTIFICFISCALI_2_2_1_2 x004"
				+ ",VAL_DENOMINAZIONE_2_2_2_1 x005"
				+ ",VAL_INDIRIZZO_2_2_2_4_1 x006"
				+ ",VAL_CAP_2_2_2_4_3 x007"
				+ ",VAL_COMUNE_2_2_2_4_4 x008"
				+ ",VAL_PROVINCIA_2_2_2_4_5 x009"
				+ ",VAL_IDPAESE_2_2_1_1_2 x010"
				+ ",VAL_TIPODOCUMENTO_2_3_1_1 x011"
				+ ",VAL_DATA_2_3_1_2 x012"
				+ ",IMPON_VEN x013"
				+ ",IMPOST_VEN x014"
				+ ",PERC_IVA_VEN x015"
				+ ",VAL_NATURA_2_3_2_3 x016"
				+ ",COD_CLI_VEN x017"
				+ " from vdtexml1 order by val_idcodice_2_2_1,2,COD_CLI_VEN,val_numero_2_3_1_3";
		
		System.out.println("--- sql:\n" + sqlTxt);

/*
		try {
			jp.jd.stmt = 	jp.jd.conn.createStatement();
			jp.jd.stmt.execute("ALTER SESSION SET NLS_NUMERIC_CHARACTERS='.,'");
			jp.jd.stmt.execute("ALTER SESSION SET NLS_DATE_FORMAT='YYMMDD'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
*/			
		execReadSql(sqlTxt, 0, 10, 1, true, jp);
		String tmpDet = "";

		ResultSet rs;
		try {
//			rs = jp.jd.stmt.executeQuery(sqlTxt);

			String codeOld = "";
			String numOld = "000";
			String natOld = "";
			int flagCli = 0;
			while (jp.jd.rset.next()) {
				flagCli = 0;
				String code = jp.jd.rset.getString("x017");
				if(code == null) code = "";
				String num = jp.jd.rset.getString("x002");
				if(num == null) num = "";
				String nat = jp.jd.rset.getString("x016");
				
				if(!numOld.equalsIgnoreCase(num)) {

				if(!codeOld.equalsIgnoreCase(code)) {
					if(codeOld.length() > 0) {
						if(totDoc > 0)
							outDet += fmtDet9b;					

						outDet += fmtDet9;
					}
					tmpDet = fmtDet;

					tmpDet = ivaReplace("x001", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x002", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x003", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x004", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x005", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x006", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x007", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x008", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x009", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x010", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x011", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x012", jp.jd.rset, tmpDet, "");
					
					outDet += tmpDet;
					codeOld = code;
					flagCli = 1;
				}


				if(!numOld.equalsIgnoreCase(num)) {
					if(flagCli == 0)
						outDet += fmtDet9b;
					totDoc++;
				}

				tmpDet = fmtDet2;
				tmpDet = ivaReplace("x011", jp.jd.rset, tmpDet, "");
				tmpDet = ivaReplace("x012", jp.jd.rset, tmpDet, "");
				tmpDet = ivaReplace("x002", jp.jd.rset, tmpDet, "");
				tmpDet = ivaReplace("x012", jp.jd.rset, tmpDet, "");
				outDet += tmpDet;

				}

				tmpDet = fmtDet3;
				if(nat == null || nat.length() == 0 || nat.equalsIgnoreCase(" "))  tmpDet = tmpDet.replace("<Natura>@{x016}</Natura>\n", "");
				tmpDet = ivaReplace("x013", jp.jd.rset, tmpDet, "0.00").replace("-", "");
				tmpDet = ivaReplace("x014", jp.jd.rset, tmpDet, "0.00").replace("-", "");
				tmpDet = ivaReplace("x015", jp.jd.rset, tmpDet, "0.00");
				tmpDet = ivaReplace("x016", jp.jd.rset, tmpDet, "");
//				tmpDet = ivaReplace("x017", jp.jd.rset, tmpDet, "");
				outDet += tmpDet;

				numOld = num;
				
				System.out.println("--- code:" + code);
			}

			jp.jd.rset.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		outDet += ""
		+ fmtDet9b
		+ fmtDet9
		+ "</DTE>\n";
		helpIva += "<!-- Prima di inviare controllare: Documenti DTE:" + totDoc // + " Totale  Imponibile:" + totImponi + " Totale Imposta:" + totImpost 
				+ "-->\n";

		return outDet;
	}
	
	//=========================================================================
	String ivaReplace(String id, ResultSet rs, String opeDet, String def)
														throws SQLException {
		String tmpVal = rs.getString(id);
		String paIva = rs.getString("x001");
		if(paIva != null && paIva.equalsIgnoreCase("01866970997") && (id.equalsIgnoreCase("x010") || id.equalsIgnoreCase("x003"))) tmpVal = "IN";

		if(tmpVal == null || tmpVal.length()==0 ||tmpVal.equalsIgnoreCase(" ")) tmpVal = def;
		if(id.contentEquals("x013") || id.contentEquals("x014") || id.contentEquals("x015")) {
//			if(!tmpVal.contains(".")) tmpVal = tmpVal += ".00";
			if(tmpVal == null || tmpVal.length() == 0) tmpVal = "0.00";
			double dub = 0;
			dub = Double.parseDouble(tmpVal);
			tmpVal = String.format( "%.2f", dub);
		}
		if(id.contentEquals("x018") && tmpVal.length() == 6){
			tmpVal = "20" + tmpVal.substring(0, 2) + "-" + tmpVal.substring(2, 4) + "-" + tmpVal.substring(4, 6);
		}
		opeDet = opeDet.replace("@{" + id +"}", tmpVal);
		opeDet = opeDet.replace("&", "e");
		opeDet = opeDet.replaceAll("amp;","");
		return opeDet;
	}

	//==========================================================================
	String ivaDetDTR(JPaneDoc jp) {
		int totDoc = 0;
		long totImponi = 0;
		long totImpost = 0;
		String outDet = "";
		String fmtDet = ""
		+ "<CedentePrestatoreDTR>\n" // Ogni Fornitore
        + "<IdentificativiFiscali>\n"                                                                                                                      
           +"<IdFiscaleIVA>\n"                                                                                                                           
           + "<IdPaese>@{x003}</IdPaese>\n"                                                                                                                
           + "<IdCodice>@{x001}</IdCodice>\n"                                                                                                     
           + "</IdFiscaleIVA>\n"                                                                                                                          
           + "<CodiceFiscale>@{x004}</CodiceFiscale>\n"                                                                                               
        +"</IdentificativiFiscali>\n"    

        + "<AltriDatiIdentificativi>\n"                                                                                                                    
        + "<Denominazione>@{x005}</Denominazione>\n"                                                                       
        + "<Sede>\n"                                                                                                                                   
        + "<Indirizzo>@{x006}</Indirizzo>\n"                                                                                       
        + "<CAP>@{x007}</CAP>\n"                                                                                                                     
        + "<Comune>@{x008}</Comune>\n"                                                                                                              
        + "<Provincia>@{x009}</Provincia>\n"                                                                                                            
        + "<Nazione>@{x010}</Nazione>\n"                                                                                                                
        + "</Sede>\n"                                                                                                                                  
        + "</AltriDatiIdentificativi>\n"                                                                                                                   
//        + "<DatiFatturaBodyDTR>\n"                                                                                                                         
        ;

		String fmtDet2 = ""
		+ "<DatiFatturaBodyDTR>\n"                                                                                                                         
        + "<DatiGenerali>\n" // Ognifattura
        + "<TipoDocumento>@{x011}</TipoDocumento>\n"                                                                                                  
        + "<Data>@{x012}</Data>\n"                                                                                                              
        + "<Numero>@{x002}</Numero>\n"                                                                                                               
        + "<DataRegistrazione>@{x018}</DataRegistrazione>\n"                                                                                    
        + "</DatiGenerali>\n"
        ;

		String fmtDet3 = ""
        	+ "<DatiRiepilogo>\n" // Ogni aliquota
        	+ "<ImponibileImporto>@{x013}</ImponibileImporto>\n"                                                                                        
            + "<DatiIVA>\n"                                                                                                                            
            + "<Imposta>@{x014}</Imposta>\n"                                                                                                         
            + "<Aliquota>@{x015}</Aliquota>\n"                                                                                                       
            + "</DatiIVA>\n"
              + "<Natura>@{x016}</Natura>\n"                                                                                                                           
//            + "<Deducibile>SI</Deducibile>\n"                                                                                                          
              + "<EsigibilitaIVA>I</EsigibilitaIVA>\n"                                                                                                   
            + "</DatiRiepilogo>\n" 
            ;

    	String fmtDet9 = ""
//        + "</DatiFatturaBodyDTR>\n"         
        + "</CedentePrestatoreDTR>\n";

    	String fmtDet9b = ""
    		+ "</DatiFatturaBodyDTR>\n"         
       	       ;

		String sqlTxt = "select"
				+ " val_idcodice_3_1_1_1_2 x001"
				+ ",NUM_FAT_ACQ x002"
				+ ",VAL_IDPAESE_3_1_1_1_1 x003"
				+ ",VAL_CODICEFISCALE_3_1_1_2 x004"
				+ ",VAL_DENOMINAZIONE_3_1_2_1 x005"
				+ ",VAL_INDIRIZZO_3_1_2_4_1 x006"
				+ ",VAL_CAP_3_1_2_4_3 x007"
				+ ",VAL_COMUNE_3_1_2_4_4 x008"
				+ ",VAL_PROVINCIA_3_1_2_4_5 x009"
				+ ",VAL_NAZIONE_3_1_2_4_6 x010"
				+ ",VAL_TIPODOCUMENTO_3_3_1_1 x011"
				+ ",VAL_DATA_3_3_1_2 x012"
				+ ",IMPON_ACQ x013"
				+ ",IMPOST_ACQ x014"
				+ ",PERC_IVA_ACQ x015"
				+ ",VAL_NATURA_3_3_2_3 x016"
				+ ",COD_FOR_ACQ x017"
				+ ",DATA_REG_ACQ x018"
				+ " from vdtrxml1 order by val_idcodice_3_1_1_1_2,NUM_FAT_ACQ";
		
		System.out.println("--- sql:\n" + sqlTxt);

/*
		try {
			jp.jd.stmt = 	jp.jd.conn.createStatement();
			jp.jd.stmt.execute("ALTER SESSION SET NLS_NUMERIC_CHARACTERS='.,'");
			jp.jd.stmt.execute("ALTER SESSION SET NLS_DATE_FORMAT='YYMMDD'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
*/			
		execReadSql(sqlTxt, 0, 10, 1, true, jp);
		String tmpDet = "";

		ResultSet rs;
		try {
//			rs = jp.jd.stmt.executeQuery(sqlTxt);

			String codeOld = "";
			String numOld = "000";
			String natOld = "";
			int flagCli = 0;
			while (jp.jd.rset.next()) {
				flagCli = 0;
				String code = jp.jd.rset.getString("x017");
				if(code == null) code = "";
				String num = jp.jd.rset.getString("x002");
				if(num == null) num = "";
				String nat = jp.jd.rset.getString("x016");

				if(!numOld.equalsIgnoreCase(num)) {

				if(!codeOld.equalsIgnoreCase(code)) {
					if(codeOld.length() > 0){
						if(totDoc > 0)
							outDet += fmtDet9b;					

						outDet += fmtDet9;
					}
					tmpDet = fmtDet;

					tmpDet = ivaReplace("x001", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x002", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x003", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x004", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x005", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x006", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x007", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x008", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x009", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x010", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x011", jp.jd.rset, tmpDet, "");
					tmpDet = ivaReplace("x012", jp.jd.rset, tmpDet, "");

					outDet += tmpDet;
					codeOld = code;
					flagCli = 1;
				}

				if(!numOld.equalsIgnoreCase(num)) {
					if(flagCli == 0)
						outDet += fmtDet9b;
					totDoc++;
				}

				tmpDet = fmtDet2;
				tmpDet = ivaReplace("x011", jp.jd.rset, tmpDet, " ");
				tmpDet = ivaReplace("x012", jp.jd.rset, tmpDet, " ");
				tmpDet = ivaReplace("x002", jp.jd.rset, tmpDet, " ");
				tmpDet = ivaReplace("x012", jp.jd.rset, tmpDet, " ");
				tmpDet = ivaReplace("x018", jp.jd.rset, tmpDet, " ");
				outDet += tmpDet;
				numOld = num;
				}
				
				tmpDet = fmtDet3;
				if(nat == null || nat.length() == 0 || nat.equalsIgnoreCase(" "))  tmpDet = tmpDet.replace("<Natura>@{x016}</Natura>\n", "");
				tmpDet = ivaReplace("x013", jp.jd.rset, tmpDet, "0.00").replace("-", "");
				tmpDet = ivaReplace("x014", jp.jd.rset, tmpDet, "0.00").replace("-", "");
				tmpDet = ivaReplace("x015", jp.jd.rset, tmpDet, "0.00");
				tmpDet = ivaReplace("x016", jp.jd.rset, tmpDet, " ");
//				tmpDet = ivaReplace("x017", jp.jd.rset, tmpDet, " ");
				outDet += tmpDet;

				System.out.println("--- code:" + code);
			}

			jp.jd.rset.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		outDet += ""
				+ fmtDet9b
		+ fmtDet9
//		+ "</CedentePrestatoreDTE>\n"
		+ "</DTR>\n";

		helpIva += "<!-- Prima di inviare controllare: Documenti DTR:" + totDoc 
//				+ " Totale  Imponibile:" + totImponi + " Totale Imposta:" + totImpost 
				+ "-->\n";
		outDet = outDet;
		return outDet;
	}

	// =========================================================================
	String execReadSql(String sqlText, int offset, int numMax, int flagMode
											,Boolean immediate, JPaneDoc jp) {
		String user 	= "GLOBE";
		String pwd		= "MANAGER";
		String jdbc 	= "oracle.jdbc.driver.OracleDriver";
		String conn 	= "jdbc:oracle:thin:hr/hr@93.43.22.2:1522:XE"; // formoto pubblico
		String ritorno	= null;

		long startTime = jp.beginHelp("w7-1", "execReadSql offSet:" + offset
				+ " max:" + numMax + " mode:" + flagMode
				+ " sql:\n" + sqlText + "\n");

		jp.jd = new JPaneJdbc(sqlText, user, pwd, "", jdbc, conn, immediate);

		if(immediate) {
			jp.endHelp("w9", "Eseguito Sql (Solo Sql):", startTime);
			return ritorno;
		}

		jp.endHelp("w9", "Eseguito Sql:", startTime);
		return null;
	}

	public static void main(String[] args) {

	}

}