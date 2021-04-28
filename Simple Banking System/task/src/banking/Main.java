package banking;

public class Main {

    public static void main(String[] args) {

        DBConnection dbConnection = new DBConnection(args[1]);
        AppState state = new AppState(dbConnection);
        MainMenuHandler mainMenuHandler = new MainMenuHandler(state);
        LoginMenuHandler loginMenuHandler = new LoginMenuHandler(state);

        while (true) {
            if (state.isLoggedIn()) {
                loginMenuHandler.start();
            } else {
                mainMenuHandler.start();
            }
        }
    }
}