K.import("konoha");

class TreeNode {
	TreeNode left;
	TreeNode right;
	int item;
	boolean isNull;

	void f() {
		System.p("hoge");
	};

	int itemCheck() {
		if(this.isNull) {
			return this.item;
		}
		return this.item + this.left.itemCheck() - this.right.itemCheck();
	}
}

TreeNode bottomUpTree(int item, int depth) {
	if(depth > 0) {
		TreeNode ret1 = new TreeNode();
		ret1.left = bottomUpTree(2 * item - 1, depth - 1);
		ret1.right = bottomUpTree(2 * item, depth - 1);
		ret1.item = item;
		ret1.isNull = false;
		return ret1;
	}
	else {
		TreeNode ret2 = new TreeNode();
		ret2.item = item;
		ret2.isNull = true;
		return ret2;
	}
}

void kmain() {
	int c_MINDEPTH = 4;
	int n = 16;
	int maxDepth = 0;
	if(c_MINDEPTH + 2 > n) {
		maxDepth = c_MINDEPTH + 2;
	}
	else {
		maxDepth = n;
	}
	System.p(maxDepth);
	int stretchDepth = maxDepth + 1;
	int check = bottomUpTree(0, maxDepth).itemCheck();
	System.p("stretch tree of depth:");
	System.p(stretchDepth);
	System.p("check:");
	System.p(check);
	TreeNode logLivedTree = bottomUpTree(0, maxDepth);
	int depth = c_MINDEPTH;
	while(depth <= maxDepth) {
		int iterations = 1 << (maxDepth - depth + c_MINDEPTH);
		check = 0;
		int i = 1;
		while(i<= iterations) {
			check = check + bottomUpTree(i, depth).itemCheck();
			check = check + bottomUpTree(-i, depth).itemCheck();
			i = i + 1;
		}
		System.p(iterations * 2);
		System.p("trees of depth:");
		System.p(depth);
		System.p("check");
		System.p(check);
		depth = depth + 2;
	}
	check = longLivedTree.itemCheck();
	System.p("long lived tree of depth:");
	System.p(maxDepth);
	System.p("check:");
	System.p(check);
}

kmain();
