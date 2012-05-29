package commons.konoha2;

import commons.sugar.KArray;

public class Kwb {
	KArray m;
	int pos;
	
	public Kwb(KArray init) {
		this.m = init;
		this.pos = init.byteSize;
	}

	public void write(CTX ctx, String data, int byteLen) {
		if(!(m.byteSize + byteLen < m.byteMax)) {
			/*karray_expand(_ctx, m, m->bytesize + byteLen);*/
		}
		/*memcpy(m.bytebuf + m.byteSize, data, byteLen);*/
		m.bytebuf += m.byteSize + data.substring(0,byteLen + 1);	
		m.byteSize += byteLen;
	}

	public void putc(CTX ctx,int... ap) {
		String buf = "";
		int len = 0;
		for(int ch : ap) {
			buf += ch;
			len++;
	 	}
		write(ctx, buf, len);
	}

	public void vprintf(CTX ctx, Object... ap) {//String... ap
		Object ap2 = ap;
		int s = m.byteSize;
		int n = vsnprintf( m.bytebuf + s, m.byteMax - s, ap);//TODO
		if(n >= (m.byteMax - s) ) {
			karray_expand(ctx, m, n + 1);//TODO
			n = vsnprintf(m.bytebuf + s, m.byteMax - s, ap);
		}
		m.byteSize += n;
	}

	public void printf (CTX ctx, Object... ap) {//TODO NO IDEA
		vprintf(ctx, ap);
	}

	public String top(CTX ctx, boolean ensureZero) {
		if(ensureZero) {
			if(  !(m.byteSize + 1 < m.byteMax)  ) {
				karray_expand(ctx, m, m.byteSize + 1);//TODO
			}
			m.bytebuf += 0;//TODO NO IDEA
		}
		return (m.bytebuf + pos);
	}

	public void free() {
		bzero(m.bytebuf + pos, m.byteSize - pos);//TODO
		m.byteSize = pos;
	}
}
