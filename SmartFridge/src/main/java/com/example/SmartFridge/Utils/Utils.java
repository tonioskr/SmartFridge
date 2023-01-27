package com.example.SmartFridge.Utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Utils {
    protected
    static String onOver = "-fx-background-color: #34cfeb;";
    static String exitOver = "-fx-background-color: #24a0ed; -fx-text-fill: WHITE;";
    static String onClick = "-fx-background-color: WHITE; -fx-text-fill: BLACK; -fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5";
    static String onReleased = "-fx-background-color: #24a0ed; -fx-text-fill: WHITE;";
    final private static ObservableList<String> countries = FXCollections.observableArrayList();
    static final public String pattern = "dd/MM/yyyy";
    static final public DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
    // remove quotation marks and space within the string passed as parameter
    public static String CleanString(String str) {
        return str.replace("\"", "").replaceAll("[\\p{Ps}\\p{Pe}]", "").trim();
    }

    public static String parseYear(String date){
        StringBuilder sb = new StringBuilder(date);
        sb.insert(6,"19");
        //sb.insert(7,"1");
        return sb.toString();

    }

    public static boolean CheckEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern EMAIL_REGEX = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

        return EMAIL_REGEX.matcher(email).matches();
    }

    // Password must contain at least one digit [0-9].
    // Password must contain at least one lowercase Latin character [a-z].
    // Password must contain at least one uppercase Latin character [A-Z].
    // Password must contain at least one special character like ! @ # & ( ).
    // Password must contain a length of at least 8 characters and a maximum of 20 characters.
    public static boolean CheckPassword(String password) {
        String regexPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern PASSWORD_REGEX = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);

        return PASSWORD_REGEX.matcher(password).matches();
    }

    public static boolean isNumeric(String string) {
        int intValue;

        if (string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    public static LocalDate LOCAL_DATE_OLD(String string_date) {
        LocalDate ld = LocalDate.parse(parseYear(string_date),dateFormatter);
        return ld;
    }
    public static LocalDate LOCAL_DATE(String string_date) {
        LocalDate ld = LocalDate.parse(string_date,dateFormatter);
        return ld;
    }
    public static void setClick(MouseEvent mouseEvent) {
        //((Button) mouseEvent.getSource()).setStyle(onClick);
        //Application.setMousePointer();

        Button b = ((Button) mouseEvent.getSource());
        b.setStyle(onClick);
        b.setCursor(Cursor.HAND);
    }

    public static void unsetClick(MouseEvent mouseEvent) {
        //((Button) mouseEvent.getSource()).setStyle(onReleased);
        //Application.setMousePointer();

        Button b = ((Button) mouseEvent.getSource());
        b.setStyle(onReleased);
    }
    public static void setOver(MouseEvent mouseEvent) {
        Button b = ((Button) mouseEvent.getTarget());
        b.setStyle(onOver);
        b.setCursor(Cursor.HAND);
    }

    public static void unsetOver(MouseEvent mouseEvent) {
        Button b = ((Button) mouseEvent.getTarget());
        b.setStyle(exitOver);
        b.setCursor(Cursor.DEFAULT);
        //Application.unSetMousePointer();
    }
    public static ObservableList getCountryData() {

        if (!countries.isEmpty())
            return countries;
        countries.add("Afghanistan");
        countries.add("Albania");
        countries.add("Algeria");
        countries.add("Andorra");
        countries.add("Angola");
        countries.add("Antigua & Deps");
        countries.add("Argentina");
        countries.add("Armenia");
        countries.add("Australia");
        countries.add("Austria");
        countries.add("Azerbaijan");
        countries.add("Bahamas");
        countries.add("Bahrain");
        countries.add("Bangladesh");
        countries.add("Barbados");
        countries.add("Belarus");
        countries.add("Belgium");
        countries.add("Belize");
        countries.add("Benin");
        countries.add("Bhutan");
        countries.add("Bolivia");
        countries.add("Bosnia Herzegovina");
        countries.add("Botswana");
        countries.add("Brazil");
        countries.add("Brunei");
        countries.add("Bulgaria");
        countries.add("Burkina");
        countries.add("Burundi");
        countries.add("Cambodia");
        countries.add("Cameroon");
        countries.add("Canada");
        countries.add("Cape Verde");
        countries.add("Central African Rep");
        countries.add("Chad");
        countries.add("Chile");
        countries.add("China");
        countries.add("Colombia");
        countries.add("Comoros");
        countries.add("Congo");
        countries.add("Congo {Democratic Rep}");
        countries.add("Costa Rica");
        countries.add("Croatia");
        countries.add("Cuba");
        countries.add("Cyprus");
        countries.add("Czech Republic");
        countries.add("Denmark");
        countries.add("Djibouti");
        countries.add("Dominica");
        countries.add("Dominican Republic");
        countries.add("East Timor");
        countries.add("Ecuador");
        countries.add("Egypt");
        countries.add("El Salvador");
        countries.add("Equatorial Guinea");
        countries.add("Eritrea");
        countries.add("Estonia");
        countries.add("Ethiopia");
        countries.add("Fiji");
        countries.add("Finland");
        countries.add("France");
        countries.add("Gabon");
        countries.add("Gambia");
        countries.add("Georgia");
        countries.add("Germany");
        countries.add("Ghana");
        countries.add("Greece");
        countries.add("Grenada");
        countries.add("Guatemala");
        countries.add("Guinea");
        countries.add("Guinea-Bissau");
        countries.add("Guyana");
        countries.add("Haiti");
        countries.add("Honduras");
        countries.add("Hungary");
        countries.add("Iceland");
        countries.add("India");
        countries.add("Indonesia");
        countries.add("Iran");
        countries.add("Iraq");
        countries.add("Ireland {Republic}");
        countries.add("Israel");
        countries.add("Italy");
        countries.add("Ivory Coast");
        countries.add("Jamaica");
        countries.add("Japan");
        countries.add("Jordan");
        countries.add("Kazakhstan");
        countries.add("Kenya");
        countries.add("Kiribati");
        countries.add("Korea North");
        countries.add("Korea South");
        countries.add("Kosovo");
        countries.add("Kuwait");
        countries.add("Kyrgyzstan");
        countries.add("Laos");
        countries.add("Latvia");
        countries.add("Lebanon");
        countries.add("Lesotho");
        countries.add("Liberia");
        countries.add("Libya");
        countries.add("Liechtenstein");
        countries.add("Lithuania");
        countries.add("Luxembourg");
        countries.add("Macedonia");
        countries.add("Madagascar");
        countries.add("Malawi");
        countries.add("Malaysia");
        countries.add("Maldives");
        countries.add("Mali");
        countries.add("Malta");
        countries.add("Marshall Islands");
        countries.add("Mauritania");
        countries.add("Mauritius");
        countries.add("Mexico");
        countries.add("Micronesia");
        countries.add("Moldova");
        countries.add("Monaco");
        countries.add("Mongolia");
        countries.add("Montenegro");
        countries.add("Morocco");
        countries.add("Mozambique");
        countries.add("Myanmar, {Burma}");
        countries.add("Namibia");
        countries.add("Nauru");
        countries.add("Nepal");
        countries.add("Netherlands");
        countries.add("New Zealand");
        countries.add("Nicaragua");
        countries.add("Niger");
        countries.add("Nigeria");
        countries.add("Norway");
        countries.add("Oman");
        countries.add("Pakistan");
        countries.add("Palau");
        countries.add("Panama");
        countries.add("Papua New Guinea");
        countries.add("Paraguay");
        countries.add("Peru");
        countries.add("Philippines");
        countries.add("Poland");
        countries.add("Portugal");
        countries.add("Qatar");
        countries.add("Romania");
        countries.add("Russian Federation");
        countries.add("Rwanda");
        countries.add("Saint Vincent & the Grenadines");
        countries.add("Samoa");
        countries.add("San Marino");
        countries.add("Sao Tome & Principe");
        countries.add("Saudi Arabia");
        countries.add("Senegal");
        countries.add("Serbia");
        countries.add("Seychelles");
        countries.add("Sierra Leone");
        countries.add("Singapore");
        countries.add("Slovakia");
        countries.add("Slovenia");
        countries.add("Solomon Islands");
        countries.add("Somalia");
        countries.add("South Africa");
        countries.add("South Sudan");
        countries.add("Spain");
        countries.add("Sri Lanka");
        countries.add("St Kitts & Nevis");
        countries.add("St Lucia");
        countries.add("Sudan");
        countries.add("Suriname");
        countries.add("Swaziland");
        countries.add("Sweden");
        countries.add("Switzerland");
        countries.add("Syria");
        countries.add("Taiwan");
        countries.add("Tajikistan");
        countries.add("Tanzania");
        countries.add("Thailand");
        countries.add("Togo");
        countries.add("Tonga");
        countries.add("Trinidad & Tobago");
        countries.add("Tunisia");
        countries.add("Turkey");
        countries.add("Turkmenistan");
        countries.add("Tuvalu");
        countries.add("Uganda");
        countries.add("Ukraine");
        countries.add("United Arab Emirates");
        countries.add("United Kingdom");
        countries.add("United States");
        countries.add("Uruguay");
        countries.add("Uzbekistan");
        countries.add("Vanuatu");
        countries.add("Vatican City");
        countries.add("Venezuela");
        countries.add("Vietnam");
        countries.add("Yemen");
        countries.add("Zambia");
        countries.add("Zimbabwe");
        return countries;
    }

    public static void numericOnly(final TextField field) {
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

}
