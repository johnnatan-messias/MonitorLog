package util;
/**
 * @author Johnnatan Messias
 */
public class Pair<Fst, Snd, Trd> {
	private Fst dir;
	private Snd url;
	private Trd size;

	public Pair(Fst dir, Snd url, Trd size) {
		this.dir = dir;
		this.url = url;
		this.size = size;
	}

	public Fst getDir() {
		return this.dir;
	}

	public void setDir(Fst dir) {
		this.dir = dir;
	}

	public Snd getURL() {
		return this.url;
	}

	public void setURL(Snd url) {
		this.url = url;
	}

	public Trd getSize() {
		return this.size;
	}

	public void setLength(Trd size) {
		this.size = size;
	}

}
