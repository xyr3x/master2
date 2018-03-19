package application;
	
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import controller.DrawingToolGreedy;
import controller.EvolutionaryAlgo;
import controller.EvolutionaryAlgoConnected;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class Main extends Application {
	public static int FighterID = 0;
	public static int CrewID = 0;
	public static int GridLength = 100;
	public static int GridSize = GridLength * GridLength;
	public static int TimeInterval = 20;
	public static Random rnd = new Random(1337);

	public static int CrewSize = 16;
	public static int PopulationSize = 100;
	public static int RecombinationSize = PopulationSize / 2;
	public static int MutationProbability = 10;
	private Stage primaryStage;
	private BorderPane rootLayout;
	EvolutionaryAlgo evAlgo = new EvolutionaryAlgo();
	EvolutionaryAlgoConnected evAlgoConnected = new EvolutionaryAlgoConnected();
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("FireFighter");

			initRootLayout();
			showLayout();
			//startAlgo();
			

			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void initRootLayout() {
		try {
			// Load fxml file for RootLayout
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show Scene containing RootLayout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main app.

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

		}
	
	
	public void showLayout() {
		try {
			// Load fxml file for LineOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("LayoutNew.fxml"));
			AnchorPane startScreen = (AnchorPane) loader.load();

			// lineOerview in Center of RootLayout
			rootLayout.setCenter(startScreen);

			LayoutController controller = loader.getController();
			controller.setMain(this);
			controller.setEvAlgo(evAlgo);
			controller.setEvAlgoConnected(evAlgoConnected);

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*private void startAlgo(){		
		//evolutionaryAlgo ausf�hren
		//daf�r service aufrufen
		EvolutionaryAlgoService service = new EvolutionaryAlgoService();
		service.setEvAlgo(evAlgo);
		service.setMain(this);
		
		
		
		service.start();
	}*/
	
	
	/**
	 * Returns the crew file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getCrewFilePath() {
	    Preferences prefs = Preferences.userNodeForPackage(Main.class);
	    String filePath = prefs.get("filePath", null);
	    if (filePath != null) {
	        return new File(filePath);
	    } else {
	        return null;
	    }
	}

	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file the file or null to remove the path
	 */
	public void setCrewFilePath(File file) {
	    Preferences prefs = Preferences.userNodeForPackage(Main.class);
	    if (file != null) {
	        prefs.put("filePath", file.getPath());

	        // Update the stage title.
	        primaryStage.setTitle("FireFighting - " + file.getName());
	    } else {
	        prefs.remove("filePath");

	        // Update the stage title.
	        primaryStage.setTitle("FireFighting");
	    }
	}
	
	/**
	 * Loads person data from the specified file. The current person data will
	 * be replaced.
	 * 
	 * @param file
	 */
	public void loadPersonDataFromFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Unmarshaller um = context.createUnmarshaller();

	        // Reading XML from the file and unmarshalling.
	        PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

	        personData.clear();
	        personData.addAll(wrapper.getPersons());

	        // Save the file path to the registry.
	        setPersonFilePath(file);

	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not load data");
	        alert.setContentText("Could not load data from file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}

	/**
	 * Saves the current person data to the specified file.
	 * 
	 * @param file
	 */
	public void savePersonDataToFile(File file) {
	    try {
	        JAXBContext context = JAXBContext
	                .newInstance(PersonListWrapper.class);
	        Marshaller m = context.createMarshaller();
	        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	        // Wrapping our person data.
	        PersonListWrapper wrapper = new PersonListWrapper();
	        wrapper.setPersons(personData);

	        // Marshalling and saving XML to the file.
	        m.marshal(wrapper, file);

	        // Save the file path to the registry.
	        setPersonFilePath(file);
	    } catch (Exception e) { // catches ANY exception
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Could not save data");
	        alert.setContentText("Could not save data to file:\n" + file.getPath());

	        alert.showAndWait();
	    }
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
