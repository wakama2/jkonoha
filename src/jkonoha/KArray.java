package jkonoha;

import java.util.List;

public class KArray {
	
	public static <T> void clear(List<T> self, int n) {
		if(self != null) {
			if(n == 0) self.clear();
			else while(self.size() >= n) self.remove(self.size()-1);
		}
	}

}
