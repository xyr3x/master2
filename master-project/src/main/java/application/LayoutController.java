package application;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import controller.EvolutionaryAlgo;
import controller.EvolutionaryAlgoConnected;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import model.*;

public class LayoutController {
	private Main main;
	private EvolutionaryAlgo evAlgo;
	private EvolutionaryAlgoConnected evAlgoConnected;

	@FXML
	private Label FitnessLabel;
	@FXML
	private Label CrewLabel;
	@FXML
	private Label GenerationLabel;
	@FXML
	private Button StartButton;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Canvas drawCanvas;
	@FXML
	private Pane drawPane;
	@FXML
	private Label TimeStepLabel;
	@FXML
	private ChoiceBox<String> GridBox;
	@FXML
	private ChoiceBox<String> CrewBox;
	@FXML
	private ChoiceBox<String> StrategyBox;
	@FXML
	private ChoiceBox<String> OptimizeBox;
	@FXML
	private TextField OptimumTextField;
	@FXML
	private TextField CompareTextField;
	


	private int rectangleSize = 8;
	private int paneHeight = Main.GridLength * (rectangleSize + 1) - 1;
	private int paneWidth = Main.GridLength * (rectangleSize + 1) - 1;

	private int currentCrew = 0;
	private FireFighterCrew bestCrew = new FireFighterCrew();
	private ConnectedFireFighterCrew bestCrewConnected = new ConnectedFireFighterCrew();

	private int timestep = 0;
	private boolean gridInit = false;
	private AnimationTimer drawLoop;
	private List<Rectangle> grid = new ArrayList<Rectangle>();

	public LayoutController() {
	}

	@FXML
	private void initialize() {
		// setup choiceboxes
		CrewBox.setItems(FXCollections.observableArrayList("Random Fighter", "Connected Fighter"));
		CrewBox.getSelectionModel().selectFirst();
		CrewBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) {
				// TODO Auto-generated method stub
				Main.crewBoxIndex = new_value.intValue();
				if (new_value.intValue() == 0) {
					setOptimumTextField();
					CompareTextField.setText(""	);
				}
				else {
					setOptimumConnectedTextField();
					setCompareTextField();
				}
				
				System.out.println(Main.crewBoxIndex);
			}
		});

		GridBox.setItems(FXCollections.observableArrayList("Infinite Grid", "Bounded Grid"));
		GridBox.getSelectionModel().selectFirst();
		GridBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) {
				// TODO Auto-generated method stub
				Main.gridBoxIndex = new_value.intValue();
			}
		});

		StrategyBox.setItems(FXCollections.observableArrayList("None", "Diamond Crew"));
		StrategyBox.getSelectionModel().selectFirst();
		StrategyBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) {
				// TODO Auto-generated method stub
				Main.strategyBoxIndex = new_value.intValue();
			}
		});
		
		OptimizeBox.setItems(FXCollections.observableArrayList("One Step Optimization", "Average Optimization", "Number of good Steps Optimization"));
		OptimizeBox.getSelectionModel().selectFirst();
		OptimizeBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number value, Number new_value) {
				// TODO Auto-generated method stub
				Main.optimizeIndex = new_value.intValue();
			}
		});
		
	}

	private void startAlgo() {
		// evolutionaryAlgo ausf�hren
		// daf�r service aufrufen
		EvolutionaryAlgoService service = new EvolutionaryAlgoService();
		service.setEvAlgo(evAlgo);
		service.setMain(main);
		service.setController(this);
		service.start();
	}

	private void startAlgoConnected() {
		// evolutionaryAlgo ausf�hren
		// daf�r service aufrufen
		EvolutionaryAlgoConnectedService service = new EvolutionaryAlgoConnectedService();
		service.setEvAlgo(evAlgoConnected);
		service.setMain(main);
		service.setController(this);

		service.start();
	}

	@FXML
	private void handleButtonSetValues() {
		String optimumString = OptimumTextField.getText();
		int optimum = isIntegerValue(optimumString);
		if (optimum >= 0) {
			if(Main.crewBoxIndex == 0) {
				evAlgo.setOptimum(optimum);
			}
			else {
				evAlgoConnected.setOptimum(optimum);
			}
		}
		
		String compareString = CompareTextField.getText();
		int compare = isIntegerValue(compareString);
		if (compare >= 0) {
			if(Main.crewBoxIndex == 0) {
				
			}
			else {
				evAlgoConnected.setCompareValue(compare);
			}
		}
		
		
		
	}
	
	@FXML
	private void handleButtonCalculate() {
		System.out.println("test");
		// start random or connected ev Algo
		if (Main.crewBoxIndex == 0) {
			System.out.println("Hallo");
			startAlgo();
		}

		if (Main.crewBoxIndex == 1) {
			startAlgoConnected();
		}
	}

	@FXML
	private void handleButtonAnimate() {
		if (!gridInit) {
			gridInit = true;
			initalizeDrawing();
		}

		for (int i = 0; i < grid.size(); i++) {
			grid.get(i).setFill(Color.RED);
		}
		// start random or connected ev Algo
		if (Main.crewBoxIndex == 0) {
			FireFighterCrew shownCrew = evAlgo.getBestCrew();
			draw(shownCrew);
			CrewLabel.setText(Integer.toString(shownCrew.getID()));
			FitnessLabel.setText(Integer.toString(shownCrew.getMaxNonBurningVertices()));
			GenerationLabel.setText(Integer.toString(shownCrew.getGeneration()));
		}

		// connected
		if (Main.crewBoxIndex == 1) {
			ConnectedFireFighterCrew shownCrew = evAlgoConnected.getBestCrew();
			drawConnected(shownCrew);
			CrewLabel.setText(Integer.toString(shownCrew.getID()));
			FitnessLabel.setText(Integer.toString(shownCrew.getMaxNonBurningVertices()));
			GenerationLabel.setText(Integer.toString(shownCrew.getGeneration()));
		}

	}

	@FXML
	private void handleButtonLastStep() {
		// init drawing if needed
		if (!gridInit) {
			gridInit = true;
			initalizeDrawing();
		}
		
		// if timestep 0, do nothing
		if (timestep == 0) {
			return;
		}

		// else draw last timestep
		timestep = timestep - 1;
		TimeStepLabel.setText(Integer.toString(timestep));
		// decide between crewBoxIndex
		// random crew
		if (Main.crewBoxIndex == 0) {
			FireFighterCrew shownCrew = evAlgo.getBestCrew();
			drawCrewTimeStep(shownCrew);
			CrewLabel.setText(Integer.toString(shownCrew.getID()));
			FitnessLabel.setText(Integer.toString(shownCrew.getMaxNonBurningVertices()));
			GenerationLabel.setText(Integer.toString(shownCrew.getGeneration()));
		}

		// connected crew
		if (Main.crewBoxIndex == 1) {
			ConnectedFireFighterCrew shownCrew = evAlgoConnected.getBestCrew();
			drawCrewConnectedTimeStep(shownCrew);
			CrewLabel.setText(Integer.toString(shownCrew.getID()));
			FitnessLabel.setText(Integer.toString(shownCrew.getMaxNonBurningVertices()));
			GenerationLabel.setText(Integer.toString(shownCrew.getGeneration()));

		}

	}

	@FXML
	private void handleButtonNextStep() {
		// init drawing if needed
		if (!gridInit) {
			gridInit = true;
			initalizeDrawing();
		}
		
		// if last timestep , do nothing
		if (timestep == Main.TimeInterval) {
			return;
		}

		// else draw last timestep
		
		TimeStepLabel.setText(Integer.toString(timestep));
		// decide between crewBoxIndex
		// random crew
		if (Main.crewBoxIndex == 0) {
			FireFighterCrew shownCrew = evAlgo.getBestCrew();
			drawCrewTimeStep(shownCrew);
			CrewLabel.setText(Integer.toString(shownCrew.getID()));
			FitnessLabel.setText(Integer.toString(shownCrew.getMaxNonBurningVertices()));
			GenerationLabel.setText(Integer.toString(shownCrew.getGeneration()));
		}

		// connected crew
		if (Main.crewBoxIndex == 1) {
			ConnectedFireFighterCrew shownCrew = evAlgoConnected.getBestCrew();
			drawCrewConnectedTimeStep(shownCrew);
			CrewLabel.setText(Integer.toString(shownCrew.getID()));
			FitnessLabel.setText(Integer.toString(shownCrew.getMaxNonBurningVertices()));
			GenerationLabel.setText(Integer.toString(shownCrew.getGeneration()));
		}
		
		timestep = timestep + 1;

	}

	@FXML
	private void handleButtonResume() {
		drawLoop.start();
	}

	@FXML
	private void handleButtonPause() {
		drawLoop.stop();

	}

	private void initalizeDrawing() {
		drawPane.setPrefHeight(paneHeight);
		drawPane.setPrefWidth(paneWidth);

		// cells malen -- inital alle rot
		for (int j = 0; j < Main.GridLength; j++) {
			for (int i = 0; i < Main.GridLength; i++) {
				Rectangle rect = new Rectangle();
				rect.setFill(Color.RED);
				rect.setStroke(Color.BLACK);
				rect.setStrokeWidth(0.5);
				rect.setHeight(rectangleSize);
				rect.setWidth(rectangleSize);
				rect.setX((i) * (rectangleSize + 1));
				rect.setY(paneHeight + 1 - (j + 1) * (rectangleSize + 1));

				grid.add(rect);

			}
		}
		drawPane.getChildren().addAll(grid);

	}

	private void drawConnected(final ConnectedFireFighterCrew crew) {
		final LongValue prevNanos = new LongValue(System.nanoTime());

		drawLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {

				// calculate elapsed time
				double elapsedTime = (now - prevNanos.value) / 1000000000.0;

				// more than 1 second, draw next step
				if (elapsedTime >= 1) {
					prevNanos.value = now;
					TimeStepLabel.setText(Integer.toString(timestep));
					drawCrewConnected(crew);
				}

			}

		};

		int temp;
		// draw initial setup
		for (int i = 0; i < Main.CrewSize; i++) {
			temp = crew.getCrew().get(i).getCurrentVertice();
			grid.get(temp).setFill(Color.BLACK);
		}

		// start animaition
		drawLoop.start();

	}

	private void drawCrewConnected(ConnectedFireFighterCrew crew) {
		// timestep aktualisieren
		if (timestep == Main.TimeInterval - 1) {
			timestep = 0;
			drawLoop.stop();

			// TODO: abschluss"bild"

			return;
		} else {
			timestep = timestep + 1;
		}

		drawCrewConnectedTimeStep(crew);

	}

	private void drawCrewConnectedTimeStep(ConnectedFireFighterCrew crew) {
		// draw every Timestep
		// set all rectangles to red
		for (int i = 0; i < grid.size(); i++) {
			grid.get(i).setFill(Color.RED);
		}

		int dummy;
		// Draw nonBurningVertices
		for (int i = 0; i < Main.CrewSize * Main.CrewSize; i++) {
			dummy = crew.getNonBurningVerticesIndex(timestep, i);
			if (!(dummy == 0)) {
				grid.get(dummy).setFill(Color.WHITE);
			}
		}

		// Draw defendedVertices
		for (int i = 0; i < Main.CrewSize; i++) {
			grid.get(crew.getDefendedVerticesIndex(timestep, i)).setFill(Color.BLACK);
		}
	}

	private void draw(final FireFighterCrew crew) {
		final LongValue prevNanos = new LongValue(System.nanoTime());

		drawLoop = new AnimationTimer() {

			@Override
			public void handle(long now) {

				// calculate elapsed time
				double elapsedTime = (now - prevNanos.value) / 1000000000.0;

				// more than 1 second, draw next step
				if (elapsedTime >= 0.33) {
					prevNanos.value = now;
					TimeStepLabel.setText(Integer.toString(timestep));
					drawCrew(crew);
				}

			}

		};

		int temp;
		// draw initial setup
		for (int i = 0; i < Main.CrewSize; i++) {
			temp = crew.getCrew().get(i).getCurrentVertice();
			grid.get(temp).setFill(Color.BLACK);
		}

		// start animaition
		drawLoop.start();

	}

	private void drawCrew(FireFighterCrew crew) {
		// timestep aktualisieren
		if (timestep == Main.TimeInterval - 1) {
			timestep = 0;
			drawLoop.stop();

			// TODO: abschluss"bild"

			return;
		} else {
			timestep = timestep + 1;
		}

		drawCrewTimeStep(crew);

	}

	private void drawCrewTimeStep(FireFighterCrew crew) {
		// draw every Timestep
		// set all rectangles to red
		for (int i = 0; i < grid.size(); i++) {
			grid.get(i).setFill(Color.RED);
		}

		int dummy;
		// Draw nonBurningVertices
		for (int i = 0; i < Main.CrewSize * Main.CrewSize; i++) {
			dummy = crew.getNonBurningVerticesIndex(timestep, i);
			if (!(dummy == 0)) {
				grid.get(dummy).setFill(Color.WHITE);
			}
		}

		// Draw defendedVertices
		for (int i = 0; i < Main.CrewSize; i++) {
			grid.get(crew.getDefendedVerticesIndex(timestep, i)).setFill(Color.BLACK);
		}
	}

	// getter and setter
	public FireFighterCrew getBestCrew() {
		return bestCrew;
	}

	public void setBestCrew(FireFighterCrew bestCrew) {
		this.bestCrew = bestCrew;
	}

	public ConnectedFireFighterCrew getBestCrewConnected() {
		return bestCrewConnected;
	}

	public void setBestCrewConnected(ConnectedFireFighterCrew bestCrewConnected) {
		this.bestCrewConnected = bestCrewConnected;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public void setEvAlgo(EvolutionaryAlgo evAlgo) {
		this.evAlgo = evAlgo;
	}

	public void setEvAlgoConnected(EvolutionaryAlgoConnected evAlgoConnected) {
		this.evAlgoConnected = evAlgoConnected;
	}
	
	//set Textfields
	public void setOptimumConnectedTextField() {		
		OptimumTextField.setText(Integer.toString(evAlgoConnected.getOptimum()));		
	}
	
	public void setOptimumTextField() {		
		OptimumTextField.setText(Integer.toString(evAlgo.getOptimum()));		
	}
	
	public void setCompareTextField() {
		CompareTextField.setText(Integer.toString(evAlgoConnected.getCompareValue()));
	}
	
	//help functions
	
	private int isIntegerValue(String str) {
		try {
			int result = Integer.parseInt(str);
			return result;
		} 
		catch(Exception e){
			return -1;
		}
	}
	

}



class LongValue {
	public long value;

	public LongValue(long i) {
		value = i;
	}
}
