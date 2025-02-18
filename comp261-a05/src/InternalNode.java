import java.util.ArrayList;
import java.util.List;

public class InternalNode<K, C> implements Node<K, C> {

	public List<K> keys;
	private List<C> children;
	private int size;
	private C next;

	public InternalNode() {
		keys = new ArrayList<K>();
		keys.add(null);
		children = new ArrayList<C>();
	}

	@Override
	public void addChild(C c) {
		/// add index to add child so can be added in right place
		children.add(c);
//		size++;
	}

	@Override
	public void addKey(K k) {
		keys.add(k);
		size++;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public C getChild(int i) {
		return children.get(i);
	}

	@Override
	public K getKey(int i) {
		return keys.get(i);
	}

	@Override
	public void setSize(int s) {
		size = s;
	}

	@Override
	public void setNext(C c) {
		next = c;
	}

	@Override
	public C getNext() {
		return next;
	}

	@Override
	public void addChild(C c, int i) {
		children.set(i, c);
	}

	@Override
	public void addKey(K k, int i) {
		if (i >= size) {
			size++;
		}
		keys.set(i, k);
	}

	 @Override
	 public K removeKey(int i) {
	 return keys.remove(i);
	 }
	
	 @Override
	 public C removeChild(int i) {
	 size--;
	 return children.remove(i);
	 }
	
	@Override
	public String toString() {
		return "BPlusStringInternalNode [" + "\tsize=" + size + ", " + "\n\tkeys=" + keys + "," + "\n\tchild=" + children + "]";
	}

	@Override
	public void addKV(K k, C c) {
		throw new InvalidOperation(null);
		// TODO Auto-generated method stub
		
	}
}