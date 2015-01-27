package fclib;

import java.util.*;

// hash table class that uses linear probing to resolve collisions,
// and omits the values (it stores just a set of keys)

public class LinearSet<K> implements Iterable<K> {

	// array of keys

	private K[] keys;

	// current number of keys

	private int currsize;

	// load factor for the table

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

		for (int i = 0; i < tablesize; i++) {
			if (keys[i] != null) {
				int slot = find(keys[i], newkeys);
				newkeys[slot] = keys[i];
			}
		}

		keys = newkeys;
	}

	// constructor

	@SuppressWarnings(value="unchecked")
	public LinearSet(int cap) {
		if (cap < 1)
			throw new IllegalArgumentException();

		while (!isprime(cap))
			cap++;

		keys = (K[])new Object[cap];

		currsize = 0;
	}

	// default constructor

	public LinearSet() {
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

	// get the value for a specific key (the value is the key itself)

	public K get(K key) {
		return keys[find(key, keys)];
	}

	// put a key in the table

	public void put(K key) {
		int slot = find(key, keys);

		if (keys[slot] == null) {
			keys[slot] = key;

			if (++currsize >= (int)(keys.length * LOADFACTOR))
				rehash();
		}
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
