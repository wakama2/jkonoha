package jkonoha;

import commons.klib.KLib;
import tool.Konoha;
import commons.konoha2.*;
import commons.konoha2.kclass.K_String;
import commons.sugar.CtxSugar;
import commons.sugar.K_Expr;

public class Perror {
	
	public static boolean CTX_isInteractive = true;
	public static boolean CTX_isCompileOnly = false;
	
	public static String T_emsg(CTX ctx, int pe) { 
		switch(pe) {
		case CRIT_:							// not exist
		case ERR_: return "(error)";		// not exist
		case WARN_: return "(warning)";		// not exist
		case INFO_:							// not exist
			if(CTX_isInteractive || CTX_isCompileOnly || Konoha.verboseSugar) { // not exist
				return "(info)";
			}
			return null;
		case DEBUG_:							// not exist
			if(Konoha.verboseSugar) {
				return "(debug)";
			}
			return null;
		}
		return "(unknown)";
	}
	
	public static int vperrorf(CTX ctx, int pe, int uline, int lpos, String fmt, Object... ap) {
		String msg = T_emsg(ctx, pe);
		int errref = -1;
		if(msg != null) {
			CtxSugar base = new CtxSugar();//ctx.KArrayList<KModLocal>;
			Kwb wb = new Kwb(base.cwb);
			if(uline > 0) {
				String file = T_file(uline);//TODO T_file
				wb.printf(ctx, msg + KLib.shortname(file) + uline);//TODO shortname is in KLib.java
			}
			else {
				wb.printf(ctx, msg);
			}
			wb.vprintf(ctx, ap);//TODO
			msg = wb.top(ctx, true);
			//TODO new_kSting, kArray_size, kArray_add
			K_String emsg = new K_String(msg);
			errref = base.errors.size();
			base.errors.add(emsg);//TODO
			if(pe == ERR_ || pe == CRIT_) {//TDOO ERR, CRIT_ enum in (/include/konoha2/konoha2.h)
				base.errCount ++;
			}
			//kreport(pe, (String)emsg.h.ct.unbox(ctx, (K_Object)emsg));//TODO
			//kreport(pe, (String)O_ct(emsg).unbox(ctx, (K_Object)emsg));//TODO O_ct(o) ((o)->h.ct)
			kreport(pe, S_text(emsg));//TODO S_text(s) ((const char*) (O_ct(s)->unbox(_ctx, (kObject*)s)))
		}									//			(String)O_ct(s).unbox(ctx, (K_Object)s)
		return errref;
	}
	
	public static int sugar_p(CTX ctx, int pe, int uline, int lpos, Object... ap) {
		int errref = Perror.vperrorf(ctx, pe, uline, lpos, null, ap); //TODO What is the arg, "null"
		return errref;
	}

	public static K_Expr Token_p(CTX ctx, K_Token tk, int pe, Object... ap) {
		Perror.vperrorf(ctx, pe, tk.uline, tk.lpos, null, ap); //TODO What is null?
		// return K_NULLEXPR; TODO
		return null;
	}	
	
	public static int Kerrno(CTX ctx) {//joseph: Kerrno in original konoha2 (src/sugar/perror.h)
		//K_Array<K_Object> i = new K_Array<K_Object>();
		return ctx.modlocal.errors.size();//TODO
	}
	
	public static K_String KStrerror(CTX ctx, int eno) {
		CtxSugar base = ctx.modlocal.get(CTX.MOD_sugar); //TODO
		for(int i = eno; i < base.errors.size(); i++) {
			K_String emsg = base.errors.get(i);          //TODO
			if(emsg.text.indexOf("(error)") != -1) {
				return emsg;
			}
		}
		//DBG_ABORT("kerrno=%d, |errmsgs|=%d", kArray_size(base->errors)); TODO DBG_ABORT is not exist
		return ctx.share.emptyString; //TODO emptyString is not exist
	}
}
