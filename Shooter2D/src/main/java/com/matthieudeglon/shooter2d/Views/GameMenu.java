package com.matthieudeglon.shooter2d.Views;


import com.matthieudeglon.shooter2d.API.Menu;
import com.matthieudeglon.shooter2d.Customs.CustomCheckedException;
import com.matthieudeglon.shooter2d.Models.Simulation;

public class GameMenu extends Menu {


    public GameMenu(){
        super();
    }

    public GameMenu(Menu otherMenu){
        super(otherMenu);
    }

    public GameMenu(Simulation gameInstance){
        super();
        setSimulationInstance(gameInstance);

    }

    @Override
    public void createContent() throws CustomCheckedException.MissingMenuComponentException {

        if(isSimulationRunning()) {
            this.addItem("CONTINUE");
        } else {
            this.addNonAnimatedItem("CONTINUE");
        }

        this.addItem("NEW GAME");
        this.addItem("OPTIONS");
        this.addItem("EXIT");
        getStage().setTitle("VIDEO GAME");
        setTitle("E V E R S C A P E");


        if(isSimulationRunning()) {
            getItem("CONTINUE").setOnMouseReleased(event -> {
                getStage().close();
                getStage().setScene(getSimulationInstance().getScene());
                getStage().show();
                getStage().toFront();
            });
        }

        getItem("NEW GAME").setOnMouseReleased(event -> {
            SubmenuView submenuLaunchGame = new SubmenuView(this);
            tryToStart(submenuLaunchGame);
        });
        getItem("EXIT").setOnMouseReleased(event -> getStage().close());
        getItem("OPTIONS").setOnMouseReleased(event -> {
            OptionsMenu optionsMenu = new OptionsMenu(this);
            tryToStart(optionsMenu);
        });


    }

    @Override
    public void setMenuScale(double widthScale, double heightScale) throws CustomCheckedException.MissingMenuComponentException {
        super.setMenuScale(widthScale, heightScale);
        GameMenu newMenu = new GameMenu(this);
        newMenu.start(getStage());
    }

    @Override
    public void setScaledPosition(double scaledPositionX, double scaledPositionY) throws CustomCheckedException.MissingMenuComponentException {
        super.setScaledPosition(scaledPositionX, scaledPositionY);
        GameMenu newMenu = new GameMenu(this);
        newMenu.start(getStage());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
