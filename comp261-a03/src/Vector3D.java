/**
 * An immutable 3D vector or position. Note that it is safe to make the fields
 * public because they are final and cannot be modified.
 * 
 * @author Pondy
 */
public class Vector3D {
	public final float x;
	public final float y;
	public float z;
	public final float mag;

	/**
	 * Construct a new vector, with the specified x, y, z components computes
	 * and caches the magnitude.
	 */
	public Vector3D(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mag = (float) Math.sqrt(x * x + y * y + z * z);
	}

	/** A private constructor, used only within this class */
	private Vector3D(float x, float y, float z, float mag) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.mag = mag;
	}

	/**
	 * Constructs and returns a unit vector in the same direction as this
	 * vector.
	 */
	public Vector3D unitVector() {
		if (mag <= 0.0)
			return new Vector3D(1.0f, 0.0f, 0.0f, 1.0f);
		else
			return new Vector3D(x / mag, y / mag, z / mag, 1.0f);
	}

	/** Returns the new vector that is this vector minus the other vector. */
	public Vector3D minus(Vector3D other) {
		return new Vector3D(x - other.x, y - other.y, z - other.z);
	}

	/** Returns the new vector that is this vector plus the other vector. */
	public Vector3D plus(Vector3D other) {
		return new Vector3D(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3D mult(float v) {
		Vector3D target = new Vector3D(x*v, y*v, z*v);
		return target;
	}
	
	public Vector3D mult(Vector3D other) {
		Vector3D target = new Vector3D(x*other.x, y*other.y, z*other.z);
		return target;
	}
	
	/**
	 * Returns the float that is the dot product of this vector and the other
	 * vector.
	 */
	public float dotProduct(Vector3D other) {
		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Returns the vector that is the cross product of this vector and the other
	 * vector. Note that the resulting vector is perpendicular to both this and
	 * the other vector.
	 */
	public Vector3D crossProduct(Vector3D other) {
		float x = this.y * other.z - this.z * other.y;
		float y = this.z * other.x - this.x * other.z;
		float z = this.x * other.y - this.y * other.x;
		return new Vector3D(x, y, z);
	}

	/**
	 * Returns the cosine of the angle between this vector and the other vector.
	 */
	public float cosTheta(Vector3D other) {
		return (x * other.x + y * other.y + z * other.z) / mag / other.mag;
	}

	@Override
	public String toString() {
		StringBuilder ans = new StringBuilder("Vect:");
		ans.append('(').append(x).append(',').append(y).append(',').append(z)
				.append(')');
		return ans.toString();
	}
}