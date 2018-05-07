package application;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import controller.DrawingToolGreedy;
import controller.EvolutionaryAlgo;
import controller.EvolutionaryAlgoConnected;
import controller.SaveFunctions;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;
import tester.stringTester;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Main extends Application {
	public static int FighterID = 0;
	public static int CrewID = 0;
	public static int GridLength = 100;
	public static int GridSize = GridLength * GridLength;
	public static int BoundedGridSize = GridLength * GridLength * 2;
	public static int TimeInterval = 20;
	public static Random rnd = new Random();
	// indices for strategies, etc
	public static int crewBoxIndex = 0;
	public static int strategyBoxIndex = 0;
	public static int gridBoxIndex = 0;

	public static int CrewSize = 16;
	public static int PopulationSize = 100;
	public static int RecombinationSize = PopulationSize / 2;
	public static int MutationProbability = 10;
	private Stage primaryStage;
	private BorderPane rootLayout;
	EvolutionaryAlgo evAlgo = new EvolutionaryAlgo();
	EvolutionaryAlgoConnected evAlgoConnected = new EvolutionaryAlgoConnected();

	// list to save and open
	private List<ConnectedFireFighterCrew> connectedCrewData = new ArrayList<ConnectedFireFighterCrew>();
	private List<FireFighterCrew> crewData = new ArrayList<FireFighterCrew>();

	/**
	 * starts the application
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("FireFighter");

			initRootLayout();
			showLayout();
			// startAlgo();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the rootlayout
	 */
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
			RootLayoutController controller = loader.getController();
			controller.setMain(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * opens the standard layout
	 */
	public void showLayout() {
		try {
			// Load fxml file for LineOverview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("LayoutNew2.fxml"));
			BorderPane startScreen = (BorderPane) loader.load();

			// lineOerview in Center of RootLayout
			rootLayout.setCenter(startScreen);

			LayoutController controller = loader.getController();
			controller.setMain(this);
			controller.setEvAlgo(evAlgo);
			controller.setEvAlgoConnected(evAlgoConnected);
			evAlgo.setMain(this);
			evAlgoConnected.setMain(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * opens the properties dialog
	 */
	public void showPropertiesDialog() {
		try {
			// Load fxml file and create a new stage for the popup dialog
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("PropertiesDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Properties");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// controller
			PropertiesDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.showProperties();

			// Show the dialog until the user closes it
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	/*
	 * private void startAlgo(){ //evolutionaryAlgo ausf�hren //daf�r service
	 * aufrufen EvolutionaryAlgoService service = new EvolutionaryAlgoService();
	 * service.setEvAlgo(evAlgo); service.setMain(this);
	 * 
	 * 
	 * 
	 * service.start(); }
	 */

	/**
	 * Returns the crew file preference, i.e. the file that was last opened. The
	 * preference is read from the OS specific registry. If no such preference can
	 * be found, null is returned.
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
	 * Sets the file path of the currently loaded file. The path is persisted in the
	 * OS specific registry.
	 * 
	 * @param file
	 *            the file or null to remove the path
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
	 * Loads crew data from the specified file. The current crew data will be
	 * replaced.
	 * 
	 * @param file
	 */
	public void loadCrewDataFromFile(File file) {
		try {
			if (crewBoxIndex == 0) {
				JAXBContext context = JAXBContext.newInstance(CrewWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				CrewWrapper wrapper = (CrewWrapper) um.unmarshal(file);

				crewData.clear();
				crewData.addAll(wrapper.getCrews());

				// set Best crew in evalgo
				int index = 0;
				int tempFit = 0;
				for (int i = 0; i < Main.CrewSize; i++) {
					if (crewData.get(i).getFitness() > tempFit) {
						tempFit = crewData.get(i).getFitness();
						index = i;
					}
				}
				evAlgo.setBestCrew(crewData.get(index));

				// Save the file path to the registry.
				setCrewFilePath(file);
			} else if (crewBoxIndex == 1) {
				JAXBContext context = JAXBContext.newInstance(ConnectedCrewWrapper.class);
				Unmarshaller um = context.createUnmarshaller();

				// Reading XML from the file and unmarshalling.
				ConnectedCrewWrapper wrapper = (ConnectedCrewWrapper) um.unmarshal(file);

				connectedCrewData.clear();
				connectedCrewData.addAll(wrapper.getCrews());

				// Save the file path to the registry.
				setCrewFilePath(file);
			}

		} catch (Exception e) { // catches ANY exception
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Loads crew data from the specified file. The current crew data will be
	 * replaced.
	 * 
	 * @param file
	 */
	public void loadCrewDataFromFile2(File file) {
		System.out.println("Test");
		SaveFunctions sf = new SaveFunctions();
		try {
			if (crewBoxIndex == 0) {
				// clear lists
				crewData.clear();
				Main.crewBoxIndex = 0;
			} else if (crewBoxIndex == 1) {
				System.out.println("Test1");
				Main.crewBoxIndex = 1;
				// clear lists
				connectedCrewData.clear();

				String fileText = new String();
				//fileText = file.toString();
				Charset charset = Charset.forName("UTF8");
				fileText = stringTester.readFile(file.getAbsolutePath(), charset);
				
				int count = stringTester.frequency(fileText, "</ConnectedFireFighterCrew>");
				System.out.println("Zähler: " + count);

				// split file into single crews
				String[] fileTextSplit = fileText.split("</ConnectedFireFighterCrew>", 100);
				
				// create crew from text
				for (int i = 0; i < fileTextSplit.length; i++) {
					ConnectedFireFighterCrew crew = sf.stringToConnectedCrew(fileTextSplit[i]);
					connectedCrewData.add(crew);
				}
				// Save the file path to the registry.
				setCrewFilePath(file);
				System.out.println("Test2");
				System.out.println(connectedCrewData.get(10).getID());
				
				// set Best crew in evalgo
				int index = 0;
				int tempFit = 0;
				for (int i = 0; i < connectedCrewData.size(); i++) {
					if (connectedCrewData.get(i).getMaxNonBurningVertices() > tempFit) {
						tempFit = connectedCrewData.get(i).getMaxNonBurningVertices();
						index = i;
						System.out.println("tempFit:" + tempFit );
					}
				}
				evAlgoConnected.setBestCrew(connectedCrewData.get(index));

			}

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not load data");
			alert.setContentText("Could not load data from file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current crew data to the specified file.
	 * 
	 * @param file
	 */
	public void saveCrewDataToFile(File file) {
		try {
			if (crewBoxIndex == 0) {
				JAXBContext context = JAXBContext.newInstance(CrewWrapper.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Wrapping our person data.
				CrewWrapper wrapper = new CrewWrapper();
				wrapper.setCrews(crewData);

				// Marshalling and saving XML to the file.
				m.marshal(wrapper, file);

				// Save the file path to the registry.
				setCrewFilePath(file);
			} else if (crewBoxIndex == 1) {
				JAXBContext context = JAXBContext.newInstance(ConnectedCrewWrapper2.class);
				Marshaller m = context.createMarshaller();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				// Wrapping our person data.
				ConnectedCrewWrapper2 wrapper = new ConnectedCrewWrapper2();
				wrapper.setFighter(connectedCrewData.get(0).getCrew());
				wrapper.setDefendedVertices(connectedCrewData.get(0).getDefendedVertices());
				wrapper.setNonBurningVertices(connectedCrewData.get(0).getNonBurningVertices());
				wrapper.setFitness(connectedCrewData.get(0).getFitness());

				// Marshalling and saving XML to the file.
				m.marshal(wrapper, file);

				// Save the file path to the registry.
				setCrewFilePath(file);
			}

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * Saves the current crew data to the specified file.
	 * 
	 * @param file
	 */
	public void saveCrewDataToFile2(File file) {
		SaveFunctions sf = new SaveFunctions();
		StringBuilder dummy = new StringBuilder();
		try {
			if (crewBoxIndex == 0) {

			} else if (crewBoxIndex == 1) {
				// write every crew into string
				for (ConnectedFireFighterCrew k : connectedCrewData) {
					// System.out.println("Test 1");
					dummy.append(sf.connectedCrewToString(k));
					// System.out.println(dummy);

				}
				// write string into file
				System.out.println("Test");
				String text = dummy.toString();
				InputStream is = new ByteArrayInputStream(text.getBytes());
				Path target = Paths.get(file.getAbsolutePath());
				Files.copy(is, target, StandardCopyOption.REPLACE_EXISTING);
			}

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Could not save data");
			alert.setContentText("Could not save data to file:\n" + file.getPath());

			alert.showAndWait();
		}
	}

	/**
	 * launches the app
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	// getter and setter
	public List<ConnectedFireFighterCrew> getConnectedCrewData() {
		return connectedCrewData;

	}

	public List<FireFighterCrew> getCrewData() {
		return crewData;

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

}
