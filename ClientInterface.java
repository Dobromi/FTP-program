package project;

import java.io.File;
import java.io.IOException;

public interface ClientInterface {

	public boolean action(File fileName,File directoryToDownload)throws IOException;
}
