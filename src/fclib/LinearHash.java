package fclib;

import java.util.*;

// hash table class that uses linear probing to resolve collisions

public class LinearHash<K,V> implements Iterable<K> {

	// arrays for keys and values

	private K[] keys;
	private V[] values;

	// current number of keys

	private int currsize;

	// load factor for table

	private static final float LOADFACTOR = 0.75f;

	// check whether a number is prime

	private static boolean isprime(int p) {
		if (p <= 2)
			return p == 2;

		if (p % 2 == 0)
			return false;

		int limit = (int)Math.sqrt(p);

		for (int i = 3; i <= limit; i += 2) {
			if (p % i == 0)
				return false;
		}

		return true;
	}

	// find a key in a table, and return the slot number

	private int find(K key, K[] keytab) {
		if (key == null || keytab == null)
			throw new NullPointerException();

		int hc = key.hashCode() & 0x7fffffff;

		int tablesize = keytab.length;

		int slot = hc % tablesize;

		while (keytab[slot] != null && !keytab[slot].equals(key)) {
			if (++slot >= tablesize)
				slot -= tablesize;
		}

		return slot;
	}

	// rehash the table

	@SuppressWarnings(value="unchecked")
	private void rehash() {
		int tablesize = keys.length;

		int newsize = tablesize * 2;
		while (!isprime(newsize))
			newsize++;

		K[] newkeys = (K[])new Object[newsize];
		V[] newvalues = (V[])new Object[newsize];

		for (int i = 0; i < tablesize; i++) {
			if (keys[i] != null) {
				int slot = find(keys[i], newkeys);
				newkeys[slot] = keys[i];
				newvalues[slot] = values[i];
			}
		}

		keys = newkeys;
		values = newvalues;
	}

	// constructor

	@SuppressWarnings(value="unchecked")
	public LinearHash(int cap) {
		if (cap < 1)
			throw new IllegalArgumentException();

		while (!isprime(cap))
			cap++;

		keys = (K[])new Object[cap];
		values = (V[])new Object[cap];

		currsize = 0;
	}

	// default constructor

	public LinearHash() {
		this(2);
	}

	// get the number of keys in the table

	public int size() {
		return currsize;
	}

	// see if a specific key is in the table

	public boolean containsKey(K key) {
		return keys[find(key, keys)] != null;
	}

	// get the value for a specific key

	public V get(K key) {
		return values[find(key, keys)];
	}

	// put a key/value in the table

	public V put(K key, V value) {
		int tablesize = keys.length;

		int slot = find(key, keys);

		if (keys[slot] != null)
			return values[slot] = value;

		keys[slot] = key;
		values[slot] = value;

		if (++currsize >= (int)(tablesize * LOADFACTOR))
			rehash();

		return value;
	}

	// get an Iterator for the table

	public Iterator<K> iterator() {
		return new Itr();
	}

	// class that implements Iterator interface

	private class Itr implements Iterator<K> {
		private int curr;

		// scan for the next key

		private void scan() {
			while (curr < keys.length && keys[curr] == null)
				curr++;
		}

		// constructor

		public Itr() {
			curr = 0;
			scan();
		}

		// see if there are more keys

		public boolean hasNext() {
			return curr < keys.length;
		}

		// return the next key

		public K next() {
			if (hasNext()) {
				K ret = keys[curr++];
				scan();
				return ret;
			}
			else {
				throw new NoSuchElementException();
			}
		}

		// remove a key

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
