import controller.BankService;
import controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginView;

// Starts the JavaFX app and opens the login screen.
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        BankService service = new BankService();
        LoginView loginView = new LoginView();
        new LoginController(stage, loginView, service).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
