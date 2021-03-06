package banking;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class AppState {
    private final Scanner scanner;
    private final BankCardDao cardDao;
    private BankCard loggedInCard;

    public AppState(DBConnection dbConnection) {
        this.cardDao = new BankCardDao(dbConnection);
        this.scanner = new Scanner(System.in);
    }

    public boolean tryLogin(String cardNumber, String pin) throws SQLException {
        BankCard card = this.cardDao.getCard(cardNumber, pin);
        if (card != null) {
            loggedInCard = card;
            return true;
        }
        return false;
    }

    public String getLoggedInCardNumber() {
        return loggedInCard.getCardNumber();
    }

    public void addIncome(int income) throws SQLException {
        cardDao.addIncome(loggedInCard.getId(), income);
    }

    public void transferIncome(int toId, int income) throws SQLException {
        cardDao.transferIncome(loggedInCard.getId(), toId, income);
    }

    public int getBalance() throws SQLException {
        return cardDao.getBalance(loggedInCard.getId());
    }

    public void logout() {
        loggedInCard = null;
    }

    public boolean isLoggedIn() {
        return loggedInCard != null;
    }

    public int readOption() {
        try {
            String str = this.scanner.next();
            return Integer.parseInt(str);
        } catch (InputMismatchException | NumberFormatException ex) {
            return -1;
        }
    }

    public String readNext() {
        return this.scanner.next();
    }

    public void addCard(BankCard card) throws SQLException {
        cardDao.insertCard(card);
    }

    public boolean isLuhnValid(String cardNumber) {
        try {
            char[] chars = cardNumber.toCharArray();

            int sum = 0;
            for (int i = 1; i <= chars.length; i++) {
                int num = Character.getNumericValue(chars[i - 1]);
                if (i % 2 == 1) {
                    num *= 2;
                }
                if (num > 9) {
                    num -= 9;
                }
                sum += num;
            }
            return sum % 10 == 0;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public int getCardId(String cardNumber) throws SQLException {
        return cardDao.getCardId(cardNumber);
    }

    public void closeAccount() throws SQLException {
        cardDao.deleteCard(loggedInCard.getId());
    }
}
