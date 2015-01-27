package fclib;

// interface for window signatures

public interface WinSig {

	// length in bytes

	int length();

	// check for equality with another WinSig

	boolean equals(Object o);

	// compute the hash code

	int hashCode();

	// get the underlying byte sequence

	byte[] getBytes();

	// convert to a string

	String toString();
}
