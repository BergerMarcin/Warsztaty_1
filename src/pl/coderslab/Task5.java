package pl.coderslab;

// Zaimportuj do projektu bibliotekę jsoup, możesz ją pobrać z adresu: https://jsoup.org/download
//Wyszukaj w popularnych serwisach internetowych nagłówków artykułów, a następnie zapisz
//pojedyncze słowa w nich występujące do pliku o nazwie popular_words.txt . Przykład pobrania
//tytułów z tagu html span z atrybutem class o wartości title :
//
//Connection connect = Jsoup.connect("http://www.onet.pl/");
//try {
//Document document = connect.get();
//Elements links = document.select("span.title");
//for (Element elem : links) {
//System.out.println(elem.text());
//}
//} catch (IOException e) {
//e.printStackTrace();
//}
//
//Wywołaj pobieranie dla wybranych serwisów internetowych.
//Pomiń wszystkie elementy krótsze niż 3-znakowe.
//Utwórz tablicę elementów wykluczonych np. oraz, ponieważ
//Wczytaj utworzony plik popular_words.txt i na jego podstawie utwórz plik
//filtered_popular_words.txt , który zawierać będzie wszystkie znalezione słowa, pomijając słowa
//wykluczone.

import javafx.beans.binding.StringBinding;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Task5 {

    /**
     * Method takes words from website, filters them and writes to fileNameFiltrated - as a main result
     * Filtration criteria:
     *  - no punctuation marks (punctuationMarksArr variable)
     *  - no words with numbers/digits (or other custom ones) in them (censoredChars variable)
     *  - no censored words (censoredWords variable)
     *  - no short words (words of shorter or equal to minnoChars length will be removed; minnoChars variable)
     *  - no short words (words of longer or equal to maxnoChars length will be removed; maxnoChars variable)
     *  - repeating words might be kept or canceled (repeatingWordsCancel)
     *  PLEASE SEE ALSO DOCUMENTATION OF method strArrFiltrationCensoredWordsCharsWordsLengthRepeating
     * @param args      - none
     */
    public static void main(String[] args) {

        // MAIN SETTINGS
        // PLEASE SEE ALSO DOCUMENTATION OF method strArrFiltrationCensoredWordsCharsWordsLengthRepeating
        String urlWebSite = "http://www.onet.pl/";      // source of words - website
                                                        // http://www.onet.pl/     - correct website
                                                        // https://bulldogjob.pl/  - to test website
                                                        //             (website without return texts acc. "span.title")
        String cssQuery = "span.title";                 // type of string/paragraphs to be collected from website
        String fileName = "popular_words.txt";          // file where taken words (slightly filtrated by removing
                                                        // punctuation marks, lower cases) are written (1 line = 1 word)
        String fileNameFiltrated = "filtrated_popular_words.txt";
                                                        // FINAL FILE (where popular, filtrated words are written 1 line = 1 word)
        String[] punctuationMarksArr = {"\\.", ",", ":", ";", "\"", "\\?", "!", "-", "\\[", "]", "\\{", "}", "\\+", "\\&",
                "\\*", "\\(", "\\)", "  "};             // punctuation marks string array
        String[] censoredWordsArr = {"oraz", "ponieważ", "żeby", "tylko", "tego", "tamtej", "temu", "tamten", "tamta",
                "tamto", "tamtędy", "stąd", "uwaga", "teraz", "wszędzie", "samym", "samej", "bardzo"};
                                                        // censored (to be removed) words
        char[] censoredCharsArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' , '„', '"'};
                                                        // if word contains that censored chars word will be removed
        int minNumChars = 3;                            // shorter or equal lenght words will be removed
        int maxNumChar = 12;                            // longer or equal lenght words will be removed
        boolean repeatingWordsCancel = true;            // repeating words to be canceled if repeatingWordsCancel == true


        // Reading words from web site, checking if everything is OK
        // Changing data format from StringBuild to single string
        StringBuilder readWebSB = null;
        readWebSB = readWebToStringBuilder(urlWebSite, cssQuery);
        String readWebStr = new String();
        if (readWebSB == null) {
            System.err.println("Data from \"" + urlWebSite + "\" acc. \"" + cssQuery + "\" could not be read");
            System.err.println("Error occured. End of application");
            return;
        } else {
            readWebStr = readWebSB.toString();
            if (readWebStr.equals("")) {
                System.err.println("Data from \"" + urlWebSite + "\" acc. \"" + cssQuery + "\" could not be read");
                System.err.println("Error occured. End of application");
                return;
            }
            System.out.println("Data was read successful from website: " + urlWebSite);
            System.out.println();
        }
//        System.out.println(readWebStr);       // To check code


        // DATA MANIPULATIONS - slight filtration
        // Filtration: changing to lower cases, remove all punctuation marks
        // Changing data format from single string to string array (single words elements; split method)
        readWebStr = readWebStr.toLowerCase();
        for (int i = 0; i < punctuationMarksArr.length; i++) {
            readWebStr = readWebStr.replaceAll(punctuationMarksArr[i], " ");
        }
        String[] readWebStrArr = readWebStr.split(" ");
//        System.out.println(Arrays.toString(readWebStrArr));       // To check code


        // Writing string array (with single words elements) into filename (each file line == 1 word)
        if (! writeStrArrToFile(fileName, readWebStrArr)) {
            System.err.println("Error occured. End of application");
            return;
        }
        System.out.println();



        // Reading words from file, checking if everything is OK
        // Changing data format from StringBuild to single string
        StringBuilder readFileSB = null;
        readFileSB = readFileToStringBuilder(fileName);
        String readFileStr = new String();
        if (readFileSB == null) {
            System.err.println("Data from file \"" + fileName + "\" could not be read");
            System.err.println("Error occured. End of application");
            return;
        } else {
            readFileStr = readFileSB.toString();
            if (readFileStr.equals("")) {
                System.err.println("Data from file \"" + fileName + "\" could not be read");
                System.err.println("Error occured. End of application");
                return;
            }
        }
        System.out.println();


        // DATA MANIPULATIONS - MAIN FILTRATION to get final popular words (based on chosen website)
        // Changing data format from single string to string array (single words elements; split method)
        // Filtration:
        //  - alphabetic sorting,
        //  - removing censored words,
        //  - removing small words (up to 3 chars),
        //  - removing words with numbers/digits,
        //  - removing repeating words
        String[] readFileStrArr = readFileStr.split(" ");
//        System.out.println(Arrays.toString(readFileStrArr));       // To check code
        Arrays.sort(readFileStrArr);
//        System.out.println(Arrays.toString(readFileStrArr));       // To check code
        String[] filtratedStrArr = strArrFiltrationCensoredWordsCharsWordsLengthRepeating
                (readFileStrArr, censoredWordsArr, censoredCharsArr, minNumChars, maxNumChar, repeatingWordsCancel);
//        System.out.println(Arrays.toString(filtratedStrArr));       // To check code
        System.out.println("Data-words (from file \"" + fileName + "\" was filtrated successful");
        System.out.println();


        // MAIN RESULT OUTPUT - FILE with POPULAR, FILTRATED WORDS (based on chosen website)
        // Writing string array (with single words elements) into filename (each file line == 1 word)
        if (! writeStrArrToFile(fileNameFiltrated, filtratedStrArr)) {
            System.err.println("Error occured. End of application");
            return;
        }
    }


//****************************************************************************************************************
// Applied methods @ main


    /**
     * Return data from urlWebSite acc. cssQuery
     * In case cssQuery=="span.title" method returns text from urlWebSite
     * In case any issue/problem (for example exception IOException) method returns null or empty StringBuilder
     * @param urlWebSite    - website URL address
     * @param cssQuery      - data from website acc. cssQuery
     * @return      - described above
     */
    static StringBuilder readWebToStringBuilder(String urlWebSite, String cssQuery) {
        StringBuilder sb = null;

        Connection connect = Jsoup.connect(urlWebSite);
        try {
            Document document = connect.get();
            Elements links = document.select(cssQuery);
            sb = new StringBuilder();
            for (Element elem : links) {
                sb.append(elem.text());
//                System.out.println(elem.text());    // To test code
            }
        } catch (IOException e) {
            System.err.println("Data from \"" + urlWebSite + "\" acc. \"" + cssQuery + "\" could not be read");
        }

        return sb;
    }


    /**
     * Creating fileName
     * Using Path class from java.nio.file
     * Writing messages to console
     * @param fileName      - filename with path
     * @return              - true if file exists or was created (true = success)
     */
    static boolean createFile (String fileName) {
        boolean methodNoError = false;

        // checking if file exists. If file exists method is finished successfully
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            System.out.println("File \"" + fileName + "\" already exists");
            methodNoError = true;
            return methodNoError;
        }

        // creating file
        try {
            Files.createFile(path);
            methodNoError = true;
        } catch (IOException e) {
            System.err.println("Error occured. File \"" + fileName + "\" could not be created");
            methodNoError = false;
        }

        // message if everything goes OK
        if (methodNoError) {
            System.out.println("File \"" + fileName + "\" was created");
        }

        return methodNoError;
    }


    /**
     * Creating file (if not exists) and writing data to fileName line per line (1 line == 1 string array element)
     * Using Path class from java.nio.file
     * Writing messages to console
     * @param fileName      - filename with path
     * @param strArr        - data to be written to the file, 1 element of string array is 1 line @ file
     * @return              - true if file exists or was created (true = success)
     */
    static boolean writeStrArrToFile(String fileName, String[] strArr) {
        boolean methodNoError = false;

        if (! createFile(fileName)) {
            return methodNoError;
        }

        // converting string array into string list (for Path class)
        List<String> outList = new ArrayList<>();
        for (int i = 0; i < strArr.length; i++) {
            outList.add(strArr[i]);
        }

        // Previous part (converting sting array to string list) might last long and meanwhile filename might be canceled
        // Therefore again check of filename existing is obligatory below
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            System.err.println("Error occured. File \"" + fileName + "\" was not created");
            return methodNoError;
        }

        // writing string list into filename
        try {
            Files.write(path, outList);
            methodNoError = true;
        } catch (IOException ex) {
            System.err.println("Error occured. Data was not written to file \"" + fileName + "\"");
            methodNoError = false;
        }

        // message if everything goes OK
        if (methodNoError) {
            System.out.println("Data was written into file \"" + fileName + "\"");
        }

        return methodNoError;
    }

    /**
     * return StringBuilder read from text file filename
     * @param filename      - path + file name of text file
     * @return              - as @ headline
     */
    static StringBuilder readFileToStringBuilder (String filename) {
        StringBuilder sb = null;

        // checking if file exists. If file does not exist method is finished with error
        Path path = Paths.get(filename);
        if (!Files.exists(path)) {
            System.err.println("Error occured. File \"" + filename + "\" not found");
            return sb;
        }

        // reading data from file
        Path path1 = Paths.get(filename);
        try {
            sb = new StringBuilder();
            for (String line : Files.readAllLines(path1)) {
                sb.append(line).append(" ");
            }
        } catch (IOException e) {
            System.err.println("Error occured. Data was not read from file \"" + filename + "\"");
        }

        // message if everything goes OK
        if (!((sb == null) || (sb.toString().equals("")))) {
            System.out.println("Data was read from file \"" + filename + "\"");
        }

        return sb;
    }

    /**
     * Take string array (strArr) with single words
     * Return filtrated string array (removed elements). Returned null if any problem with parameter or filtration
     * Filtrated means:
     *  - no elements/words from censoredWordsArr string array
     *  - no elements/words from censoredCharsArr char array
     *  - no elements/words shorter or equal than minNumChar
     *  - no elements/words longer or equal than maxNumChar
     *  - no elements/words with numbers/digits (they are not words)
     *  - no repeating elements/words (if array was previously alphabetical sorted)
     * Returned string array length is smaller or the same comparing to passed string array length
     * @param strArr            - to be filtrate string array, each element == single word
     * @param censoredWordsArr  - censored (ban) words to be removed
     *                            censoredWordsArr should have at least 1 element ""
     * @param censoredCharsArr  - words including censored (ban) chars to be removed. censoredCharsArr must be at least 1 element array
     *                            censoredCharsArr should have at least 1 element ""
     * @param minNumChars       - paramater in range >=-1 (when minNumChar == -1 then any word will be removed because of its length).
     *                            Parameter means chars quantity of single word.
     *                            Words (array elements) with char/letters number <= minNumChar will be canceled
     * @param maxNumChars       - paramater in range >=-1 (when maxNumChar == -1 then any word will be removed because of its length).
     *                            Parameter means chars quantity of single word.
     *                            Words (array elements) with char/letters number >= maxNumChar will be canceled
     * @param repeatingWordsCancel  - if true cancel repeating words (words in the array must be alphabetical sorted)
     * @return                  - filtrated string array
     */
    static String[] strArrFiltrationCensoredWordsCharsWordsLengthRepeating
                            (String[] strArr, String[] censoredWordsArr, char[] censoredCharsArr,
                             int minNumChars, int maxNumChars, boolean repeatingWordsCancel) {
        //validation of parameters (not done validation if all strArr or all censoredWords are empty strings)
        if ((strArr == null) || (strArr.length == 0) ||
                (censoredWordsArr == null) || (censoredCharsArr == null) ||
                (minNumChars < 0) || (maxNumChars < 0)) {
            return null;
        }

        // loop checking if word (i.e. strArr element will be removed)
        int[] keepWordArr = new int[strArr.length];     // array with flag: 0 == word to be cancel, 1 == word to be kept
        String censoredWords = Arrays.toString(censoredWordsArr);
        for (int i = 0; i < strArr.length; i++) {
            keepWordArr[i] = 1;
            // Below conditions are in order to speed-up filtration (i.e. from the fastest to the slowest to check)
            //      owing to "continue" after each single true check
            if ((minNumChars >- 1) && (strArr[i].length() <= minNumChars)) {
                keepWordArr[i] = 0;
                continue;
            } else if ((maxNumChars > -1) && (strArr[i].length() >= maxNumChars)) {
                keepWordArr[i] = 0;
                continue;
            } else if ((repeatingWordsCancel) && (i > 1) && (strArr[i].equals(strArr[i-1]))) {
                keepWordArr[i] = 0;
                continue;
//           Old version of canceling numbers (not worked properly for words including digits)
//            } else if (noDigits) {
//                if (strArr[i].matches("\\d+") {
//                    keepWordArr[i] = 0;
//                    continue;
//                }
            } else if (isContainCensoredChar(strArr[i], censoredCharsArr)) {
                keepWordArr[i] = 0;
                continue;
            } else if (censoredWords.contains(strArr[i])) {
                keepWordArr[i] = 0;
            }
        }

        // quantity/count of words to be keep
        int filtratedWordCount = 0;
        for (int elem: keepWordArr) {
            filtratedWordCount +=elem;
        }

        // rewriting string array - coping only elements filtrated
        String[] strArrFiltrated = new String[filtratedWordCount];
        int indexFiltrated = 0;
        for (int i = 0; i < strArr.length; i++) {
            if (keepWordArr[i] == 1) {
                if (indexFiltrated >= filtratedWordCount) {
                    return null;
                }
                strArrFiltrated[indexFiltrated] = strArr[i];
                indexFiltrated ++;
            }
        }

        return strArrFiltrated;
    }



    static boolean isContainCensoredChar(String str, char[] censoredCharsArr) {
        // validation
        if ((str.length() == 0) || (censoredCharsArr.length == 0)) {
            return false;
        }


        char[] strCharArr = str.toCharArray();
        for (int i = 0; i < strCharArr.length; i++) {
            for (int j = 0; j < censoredCharsArr.length; j++) {
                if (strCharArr[i] == censoredCharsArr[j]) {
                    return true;
                }
            }

        }
        return false;
    }


}
