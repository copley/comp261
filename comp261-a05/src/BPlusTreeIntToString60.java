public class BPlusTreeIntToString60 <T> {

	private Node root;
	private int maxLeafKeys = 3; // TODO get correct value
	private int maxNodeKeys = 3;

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

	@SuppressWarnings({ "rawtypes" })
	public String find(int key, Node n) {
		// if is a leaf: for each node if key == node.key -> return node.value
		if (n instanceof LeafNode) {
			LeafNode leaf = (LeafNode) n;
			for (int i = 0; i < leaf.size(); i++) {
				if (key == leaf.getKey(i)) {
					return leaf.getValue(i).toString();
				}
			}
			return null; // this should not happen
		// if is an internal node: recursevely find using the left child (smaller than) or right child (larger)
		} else if (n instanceof InternalNode) {
			InternalNode iNode = (InternalNode) n;
			for (int i = 1; i <= iNode.size(); i++) {
				if (key < iNode.getKey(i)) {
					Node leftChild = (Node) iNode.getChild(i - 1);
					return find(key, leftChild);
				}
			}
			Node rightChild = (Node) iNode.getChild(n.size());
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
			LeafNode<String> newLeaf = new LeafNode<String>();
			newLeaf.addKey(key);
			newLeaf.addValue(value);
			root = newLeaf;
			return true;
		} else { // if root was full
			Object[] container = add(key, value, root);
			if (container != null) {
				InternalNode newNode = new InternalNode();
				newNode.setSize(1);
				newNode.addChild(root);
				newNode.addKey((Integer) container[0]);
				newNode.addChild((Node) container[1]);
				root = newNode;
				return true;
			}
		}
		return false;
	}

	private Object[] add(int key, String value, Node node) {
		// if is a leafNode: insert key/value into correct place
		if (node instanceof LeafNode) {
			@SuppressWarnings("unchecked")
			LeafNode<String> leafNode = (LeafNode<String>) node;
			if (leafNode.size() < maxLeafKeys) {
				leafNode.addKey(key);
				leafNode.addValue(value);
				return null;
			} else {
				// returns new key and leaf node
				return splitLeaf(key, value, leafNode);
			}
		}
		// if is an internal node 
		if (node instanceof InternalNode) {
			InternalNode iNode = (InternalNode) node;
			for (int i = 1; i < iNode.size(); i++) {
				if (key < iNode.getKey(i)) {
					// Add key, value and node.child[i-1]
					Object[] container = add(key, value, iNode.getChild(i - 1));
					if (container == null) {
						return null;
					} else {
						return dealWithPromote((Integer) container[0], (InternalNode) container[1], iNode);
					}
				}
			}
			Object[] container = add(key, value, iNode.getChild(iNode.size()));
			if (container == null) {
				return null;
			} else {
				return dealWithPromote((Integer) container[0], (InternalNode) container[1], iNode);
			}
		}

		return null;
	}
	
	private Object[] dealWithPromote(int newKey, InternalNode rightChild , InternalNode iNode) {
		// nothing to promote
		if (newKey == 0 && rightChild  == null) {
			return null;
		}
		if (newKey > iNode.getKey(iNode.size() - 1)) {
			iNode.addKey(newKey);
			iNode.addChild(rightChild );
		} else {
			for (int i = 1; i < iNode.size(); i++) {
				if (newKey < iNode.getKey(i)) {
					iNode.addKey(newKey, i);
					iNode.addChild(rightChild , i);
				}
			}
		}
		// no need to perform further
		if (iNode.size() <= maxNodeKeys) {
			return null;
		}
		// node is overfull: have to split and promote
		InternalNode sibling = new InternalNode();
		int mid = (iNode.size() / 2) + 1;
		// move nodes KEYS and CHILDS from mid+1 to end to sibling node
		for (int i = 1; i <= iNode.size() - mid; i++) {
			sibling.addKey(iNode.removeKey(mid + 1 + i), i);
		}
		for (int i = 0; i <= iNode.size() - mid; i++) {
			sibling.addChild(iNode.removeChild(mid + i), i);
		}
		// promote mid key up
		int promoteKey = iNode.getKey(mid);
		// remove mid key
		iNode.removeKey(mid);
		return new Object[] { promoteKey, sibling };
		
	}
	
	private Object[] splitLeaf(int key, String value, LeafNode<String> leafNode) {
		// insert  key and value into leaf in correct place (spilling over end)
		leafNode.addKey(key);
		leafNode.addValue(value);
		LeafNode<String> sibling = new LeafNode<String>();
		// since now size is now MaxL+1 it's liek saying (maxL+2)/2
		int mid = (leafNode.size() + 1) / 2;
		// move keys and vlaue from mid to size, out of the node into sibling
		for (int i = leafNode.size() - 1; i >= mid; i--) {
			sibling.addValue(leafNode.removeValue(i));
			sibling.addKey(leafNode.removeKey(i));
		}
		// set sibling newxt to next node
		sibling.setNext(leafNode.getNext());
		leafNode.setNext(sibling);
		return new Object[] {sibling.getKey(0), sibling};
	}


}