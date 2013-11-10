package gigabytedx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class ReadAndWrite {

	public ReadAndWrite(Main plugin) {

		// Create data file if not already created.
		writeFile();
	}

	public static void writeFile() {

		try {

			// if region data is saved read from it otherwise create a new one.
			read("regions");

			// copy data from file to a new object
			RegionHandling.setRegions();

		} catch (FileNotFoundException e) {

			// if file was not found, attempt to create a new one
			Main.sendDebugInfo("Can't find region data file. Attepting to create a new one");

			try {

				// attempt to write regions object to file
				save("regions");
				Main.sendDebugInfo("New region data file created successfully!");

			} catch (FileNotFoundException e1) {

				// if FileNotFoundException send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the region data file : FileNotFoundException");
				e1.printStackTrace();
			}
		}

	}

	public static void save(String fileName) throws FileNotFoundException {

		// prepare to write to file
		FileOutputStream fout = new FileOutputStream(fileName);
		ObjectOutputStream oos;

		try {

			// attempt to write to file
			oos = new ObjectOutputStream(fout);

			try {

				// attempt to save regions object to file
				oos.writeObject(StaticVariables.getRegions());
				Main.sendDebugInfo("Region data saved successfully!");

				try {

					// attempt to close file output stream
					fout.close();

				} catch (IOException e) {

					// if IOexception send error msg to console
					Main.sendSevereInfo("A problem occurred attempting to read from the region data file : IOException");
					e.printStackTrace();
				}
			} catch (IOException e) {

				// if IOexception send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the region data file : IOException");
				e.printStackTrace();
			}

		} catch (IOException e1) {

			// if IOexception send error msg to console
			Main.sendSevereInfo("A problem occurred attempting to read from the region data file : IOException");
			e1.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public static void read(String fileName) throws FileNotFoundException {

		// prepare to read file
		FileInputStream fin = new FileInputStream(fileName);
		ObjectInputStream ois;

		try {
			// attempt to read file
			ois = new ObjectInputStream(fin);
			try {
				// attempt to save data from file to object.
				StaticVariables.setRegions((List<RegionData>) ois.readObject());
				Main.sendDebugInfo("Region data accessed successfully!");

			} catch (ClassNotFoundException e) {

				// if class wasn't found send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the region data file : ClassNotFoundException");
				e.printStackTrace();

			} catch (IOException e) {

				// if IOexception send error msg to console
				Main.sendSevereInfo("A problem occurred attempting to read from the region data file : IOException");
				e.printStackTrace();
			}
		} catch (IOException e) {

			// if IOexception send error msg to console
			Main.sendSevereInfo("A problem occurred attempting to read from the region data file : IOException");
			e.printStackTrace();
		}
		try {

			// attempt to close input stream
			fin.close();

		} catch (IOException e) {

			// if IOexception send error msg to console
			Main.sendSevereInfo("A problem occurred attempting to read from the region data file : IOException");
			e.printStackTrace();

		}
	}
}
