package commons.konoha2.kclass;

/**
 * _kString in original konoha2
 * @author okachin
 *
 */

public class K_String extends K_Object {
	
	public String text;
	// TODO const char inline_text[SIZEOF_INLINETEXT];
	
	public K_String(String text) {
		this.text = text;
	}
	
	/**
	 * size
	 * @return int
	 */
	
	public final int size() {
		return this.text.length();
	}
}
