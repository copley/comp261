public class BPlusTreeIntToString60 {

	private Node<Integer, Object> root;
	private int maxLeafKeys = 5; // TODO get correct value
	private int maxNodeKeys = 5;

	/**
	 * Returns the String associated with the given key, or null if the key is not in the B+ tree.
	 */
	public String find(int key) {
		// if root is empty return null, otherwise find(key, root)
		if (root == null || root.size() == 0) {
			return null;
		} else {
			find(key, root);
		}
		return null;
	}

	/**
	 * Finds an associated value with a key.
	 * @param key
	 * @param n
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String find(Integer key, Node<Integer, Object> n) {
		System.out.println(key);
		// LEAF CASE: for each node if key == node.key -> return node.value
		if (n instanceof LeafNode) {
			System.out.println("IS LEAF");
			for (int i = 0; i < n.size(); i++) {
				if (key == n.getKey(i)) {
					return (String) n.getChild(i);
				}
			}
			return null; // this should not happen
			// INTERNAL CASE: recursevely find using the left child (smaller than) or right child (larger)
		} else if (n instanceof InternalNode) {
			System.out.println("IS INTERNAL");
			for (int i = 1; i < n.size(); i++) {
				if (key < n.getKey(i)) {
					return find(key, (Node<Integer, Object>) n.getChild(i - 1));
				}
			}
			Node<Integer, Object> rightChild = (Node<Integer, Object>) n.getChild(n.size()-1);
			return find(key, rightChild);
		}
		return null;
	}

	/**
	 * Stores the value associated with the key in the B+ tree. If the key is already present, replaces the associated value. If the key is not present, adds the key
	 * with the associated value
	 * 
	 * @param key
	 * @param value
	 * @return whether pair was successfully added.
	 */
	public boolean put(int key, String value) {
		// If root is empty, create a new leafNode and add key/vlaue and set as root
		if (root == null || root.size() == 0) {
			System.out.println("CREATE ROOT");
			root = new LeafNode<Integer, Object>();
			root.addKV(key, value);
//			root.addKey(key);
//			root.addChild(value);
			return true;
		} else { // if root was full
			// if root was full return the new key and new leaf node
			System.out.println("ADD TO ROOT -> FIND LOCATION FIRST");
			Container<Integer, Node<Integer, Object>> container = add(key, value, root);
			if (container != null) {
				System.out.println("LEAF NODE IS FULL -> MAKE A NEW NODE");
				// create new internal node
				Node<Integer, Object> newNode = new InternalNode<Integer, Object>();
				// set the node size to 1 (although internally is 2)
				newNode.setSize(1);
				// add root as left child
				newNode.addChild(root);
				// add key to position 1
				newNode.addKey((Integer) container.getKey());
				// this will add to element 1 of child list of this node
				newNode.addChild(container.getValue());
				root = newNode;
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to add a node. It will check if is a leaf or an internal, then based on the type of node it will either add 
	 * to the other leafs or use recursion to dive to through the other internal nodes until reaching the bottom
	 * @param key
	 * @param value
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Container<Integer, Node<Integer, Object>> add(int key, String value, Node<Integer, Object> node) {
		// IF NODE IS LEAF :
		if (node instanceof LeafNode) {
			// if node size is less then maximum number of keys in a leaf
			if (node.size() < maxLeafKeys) {
				// insert key and value -- easy part INTO RIGHT PLACE !
				node.addKV(key, value);
//				node.addKey(key);
//				node.addChild(value);
				return null;
			} else {
				// if the leaf is full, split and return an object conatining new key and new leaf node
				return splitLeaf(key, value, node);
			}
		}
		// IF NODE IS INTERNAL NODE -> RECURSION DOWN
		if (node instanceof InternalNode) {
			// from i to node size (now we have a null to start with so we can have same number of childs
			for (int i = 1; i < node.size()-1; i++) {
				System.out.println(node.size()+" "+i+" "+key);
				if (key < node.getKey(i)) {
					Container<Integer, Node<Integer, Object>> container = add(key, value, (Node<Integer, Object>) node.getChild(i - 1));
					if (container == null) {
						return null;
					} else {
						return dealWithPromote(container.getKey(), container.getValue(), node);
					}
				}
			}
			Container<Integer, Node<Integer, Object>> container = add(key, value, (Node<Integer, Object>) node.getChild(node.size() - 1));
			if (container == null) {
				return null;
			} else {
				return dealWithPromote(container.getKey(), container.getValue(), node);
			}
		}

		return null;
	}

	/**
	 * Method to split a leaf node into two new nodes
	 * @param key
	 * @param value
	 * @param node
	 * @return
	 */
	private Container<Integer, Node<Integer, Object>> splitLeaf (Integer key, String value, Node<Integer, Object> node) {
		// insert key and value into leaf in correct place (spilling over end)
		node.addKV(key, value);
//		node.addKey(key);
//		node.addChild(value);
		// TODO: make sure theys are sorted .. ?
		Node<Integer, Object> sibling = new LeafNode<Integer, Object>();
		int mid = (node.size() + 1) / 2;
		int s = node.size();
		for (int i = s - 1; i >= mid; i--) {
			// add index to addChild
			sibling.addKV(node.removeKey(i), node.removeChild(i));
//			sibling.addChild(node.removeChild(i));
//			sibling.addKey(node.removeKey(i));
		}
		sibling.setNext(node.getNext());
		node.setNext(sibling);
		return new Container<Integer, Node<Integer, Object>>(sibling.getKey(0), sibling);
	}

	/**
	 * Method to promote the new added node to the top (or where it's approriate to be set)
	 * 
	 * @param newKey
	 * @param rightChild
	 * @param node
	 * @return
	 */
	private Container<Integer, Node<Integer, Object>> dealWithPromote(Integer newKey, Node<Integer, Object> rightChild, Node<Integer, Object> node) {
		
		if (rightChild == null) {
			return null;
		}
	
		if (newKey > node.getKey(node.size()-1)) {
			// insert newKey  at node.keys[node.size+1]
			node.addKey(newKey);
			node.addChild(rightChild);
		} else {
			for (int i = 1; i < node.size(); i++) {
				if (newKey < node.getKey(i)) {
					node.addKey(newKey, i);
					node.addChild(rightChild, i);
				}
			}
		}
	
		if (node.size() <= maxNodeKeys) {
			return null;
		}
	
		Node<Integer, Object> sibling = new InternalNode<Integer, Object>();
		int mid = (node.size() / 2) + 1;
		// move node.keys[mid+1… node.size] to sibling.node [1 … node.size-mid]
		for (int i = 1; i <= node.size() - mid; i++) {
			sibling.addKey(node.removeKey(mid + i), i);
		}
		// move node.child[mid … node.size]   to sibling.child [0 … node.size-mid]	 
		for (int i = 0; i <= node.size() - mid; i++) {
			sibling.addChild(node.removeChild(mid + i), i);
		}
		int promoteKey = node.getKey(mid);
		node.removeKey(mid);
		return new Container<Integer, Node<Integer, Object>>(promoteKey, sibling);
	}

}