package fclib;

// interface for classes that split apart strings into tokens

public interface ExtractTokens {
	void setInput(String s);
	String nextToken();
}
