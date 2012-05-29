package commons.sugar;

import java.util.*;

import commons.konoha2.CTX;
import commons.konoha2.kclass.K_Array;
import commons.konoha2.kclass.K_String;

public class KMap extends HashMap <Object, Object> {
	
	public int getcode(CTX ctx, ArrayList<String> list, String name, int def) {
		 Integer ret = (Integer)this.get(name);
		 if(ret != null) return ret;
		 if(def == CTX.FN_NEWID) {
			 int sym = list.size();
			 list.add(name);
			 this.put(name, sym);
			 return sym;
		 }
		 return def;
	}
}