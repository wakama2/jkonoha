K.import("konoha");

class TreeNode {
	TreeNode left;
	TreeNode right;
	int item;
	boolean isNull;

	TreeNode(int item, boolean isNull) {
		this.item = item;
		this.isNull = isNull;
	}
	;
	int itemCheck() {
		if(this.isNull) {
			return this.item;
		} else {
			int item = this.item;
			TreeNode l = this.left;
			TreeNode r = this.right;
			return item + l.itemCheck() - r.itemCheck();
			/*return this.item + this.left.itemCheck() - this.right.itemCheck();*/
		}
	}
}

TreeNode bottomUpTree(int item, int depth) {
	if(depth > 0) {
		TreeNode t = new TreeNode(item, false);
		t.left = bottomUpTree(2 * item - 1, depth - 1);
		t.right = bottomUpTree(2 * item, depth - 1);
		return t;
	}
	else {
		return new TreeNode(item, true);
	}
}

void kmain() {
	int c_MINDEPTH = 4;
	int n = 18;
	int maxDepth = 0;
	if(c_MINDEPTH + 2 > n) {
		maxDepth = c_MINDEPTH + 2;
	} else {
		maxDepth = n;
	}
	;
	int stretchDepth = maxDepth + 1;
	int check = bottomUpTree(0, stretchDepth).itemCheck();
	System.assert(check == -1);
	System.p("stretch tree of depth " + stretchDepth + " check: " + check);
	TreeNode longLivedTree = bottomUpTree(0, maxDepth);
	int depth = c_MINDEPTH;
	while(depth <= maxDepth) {
		int iterations = 1 << (maxDepth - depth + c_MINDEPTH);
		check = 0;
		int i = 1;
		while(i <= iterations) {
			check = check + bottomUpTree(i, depth).itemCheck();
			check = check + bottomUpTree(-i, depth).itemCheck();
			i += 1;
		}
		System.assert(check == iterations * -2);
		System.p("" + iterations*2 + " trees of depth " + depth + " check:" + check);
		depth += 2;
	}
	;
	check = longLivedTree.itemCheck();
	System.assert(check == -1);
	System.p("long lived tree of depth " + maxDepth + " check:" + check);
}

kmain();

