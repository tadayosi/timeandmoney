package timelanguage;

import java.io.IOException;
import java.net.Socket;

public class NetworkTimeSource {
	public static TimePoint nowNIST() throws IOException {
		String timeServer = System.getProperty("NIST.TIMESERVER", "time.nist.gov");
			byte buffer[] = new byte[256];
			Socket socket = new Socket(timeServer, 13);
			int length = socket.getInputStream().read(buffer);
			String nistTime = new String(buffer, 0, length);
			String nistGist = nistTime.substring(7, 24);
			String pattern = "y-M-d HH:mm:ss";
			return TimePoint.from(nistGist, pattern);
	}
}
